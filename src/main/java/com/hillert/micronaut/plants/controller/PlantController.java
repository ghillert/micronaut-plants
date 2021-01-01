/*
 * Copyright 2020 Gunnar Hillert
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hillert.micronaut.plants.controller;

import com.hillert.micronaut.plants.controller.problems.PlantNotFoundProblem;
import com.hillert.micronaut.plants.dto.PlantDto;
import com.hillert.micronaut.plants.dto.PlantDtoDistanceComparator;
import com.hillert.micronaut.plants.model.Plant;
import com.hillert.micronaut.plants.service.ImageService;
import com.hillert.micronaut.plants.service.PlantService;
import com.hillert.micronaut.plants.support.DistanceUtils;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.server.types.files.StreamedFile;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Explicit controller for retrieving plants.
 *
 * @author Gunnar Hillert
 *
 */
@Controller
public class PlantController implements ExceptionHandler<PlantNotFoundProblem, HttpResponse<?>> {

	private static final Logger LOG = LoggerFactory.getLogger(PlantController.class);

	@ReflectiveAccess
	@Inject
	private PlantService plantService;

	@ReflectiveAccess
	@Inject
	private ImageService imageService;

	@Get("/api/geojson")
	public FeatureCollection geojson(Pageable pageable) {
		FeatureCollection featureCollection = new FeatureCollection();

		for (Plant p : plantService.getAllPlants()) {
			Feature feature = new Feature();
			feature.setGeometry(new Point(p.getLocation().getX(), p.getLocation().getY()));
			feature.getProperties().put("name", p.getGenus() + " " + p.getSpecies());
			featureCollection.add(feature);
		}

		return featureCollection;
	}

	@Get("/api/plants")
	public Page<PlantDto> getPlants(Pageable pageable) {
		final Page<PlantDto> page = plantService.getAllPlants(pageable).map(new Function<Plant, PlantDto>() {
			@Override
			public PlantDto apply(Plant entity) {
				final PlantDto dto = new PlantDto(entity.getId(), entity.getGenus(), entity.getSpecies());
				if (entity.getLocation() != null) {
					System.out.println(entity.getLocation());
				}
				dto.setPlantSignMissing(entity.getPlantSignMissing());
				return dto;
			}
		});
		return page;
	}

	@Get("/api/plants/{plantId}")
	public PlantDto getSinglePlant(Long plantId, @QueryValue(defaultValue = "30") double radius) {
		final Plant plantFromDb = plantService.getSinglePlant(plantId).orElseGet(() -> {
			throw new IllegalStateException("No plant found for id " + plantId);
			//throw new PlantNotFoundProblem(plantId);
		});

		final List<Plant> plantsNearby = plantService.getPlantsWithinRadius(plantFromDb.getLocation(), radius);
		//final PlantDto plantDtoToReturn = new PlantDto(plantId, "ggg123", "species " + plantId);
		final PlantDto plantDtoToReturn = new PlantDto(plantId, plantFromDb.getGenus(), plantFromDb.getSpecies());
		plantDtoToReturn.setPlantSignMissing(plantFromDb.getPlantSignMissing());
		plantDtoToReturn.setPlantsNearby(
			plantsNearby
				.stream()
				.filter(plant -> !plant.getId().equals(plantFromDb.getId()))
				.map(plant -> {
					final PlantDto plantNearby = new PlantDto(plant.getId(), plant.getGenus(), plant.getSpecies());
					final double distance = DistanceUtils.calculateSimpleDistance(plant.getLocation(), plantFromDb.getLocation());
					final double distanceHaversine =
						DistanceUtils.calculateDistanceUsingHaversine(
							plant.getLocation().getY(),
							plant.getLocation().getX(),
							plantFromDb.getLocation().getY(),
							plantFromDb.getLocation().getX());

					plantNearby.setDistance(distance);
					plantNearby.setDistanceHaversine(distanceHaversine);
					return plantNearby;
				})
				.sorted(new PlantDtoDistanceComparator()).collect(Collectors.toList()));

		final List<Long> imageIds = imageService.getImageIdsForPlant(plantId);

		if (!CollectionUtils.isEmpty(imageIds)) {
			plantDtoToReturn.setImageIds(imageIds);
		}
		plantDtoToReturn.setLatitude(plantFromDb.getLocation().getY());
		plantDtoToReturn.setLongitude(plantFromDb.getLocation().getX());
		plantDtoToReturn.setLocation(plantFromDb.getLocation());
		return plantDtoToReturn;
	}

	@Override
	public HttpResponse<PlantNotFoundProblem> handle(HttpRequest request, PlantNotFoundProblem exception) {
		return HttpResponse.notFound(exception);
	}

}
