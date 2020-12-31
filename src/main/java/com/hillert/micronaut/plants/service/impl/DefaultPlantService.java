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
package com.hillert.micronaut.plants.service.impl;

import com.hillert.micronaut.plants.dao.PlantRepository;
import com.hillert.micronaut.plants.model.Plant;
import com.hillert.micronaut.plants.service.PlantService;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKBWriter;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


/**
*
* @author Gunnar Hillert
*
*/
@Transactional
@Singleton
//@CacheConfig(cacheNames = "jcache-partitioned-plants")
public class DefaultPlantService implements PlantService{

	private PlantRepository plantRepository;

	public DefaultPlantService(PlantRepository plantRepository) {
		super();
		this.plantRepository = plantRepository;
	}

	//@Cacheable
	@Override
	public Optional<Plant> getSinglePlant(Long id) {
		return this.plantRepository.findById(id);
	}

	//@Cacheable
	@Override
	public List<Plant> getPlantsWithinRadius(Point location, double radius) {

		byte[] aux = new WKBWriter().write(location);
		String p = (WKBWriter.toHex(aux));

		return this.plantRepository.getPlantsWithinRadius(location, radius);
	}

	//@Cacheable
	@Override
	public Iterable<Plant> getAllPlants() {
		return this.plantRepository.findAll();
	}

	//@Cacheable
	@Override
	public io.micronaut.data.model.Page<Plant> getAllPlants(io.micronaut.data.model.Pageable pageable) {
		return this.plantRepository.findAll(pageable);
	}

}
