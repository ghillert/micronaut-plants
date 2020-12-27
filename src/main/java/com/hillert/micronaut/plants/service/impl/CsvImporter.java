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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.inject.Singleton;
import javax.transaction.Transactional;

import com.hillert.micronaut.plants.model.Image;
import io.micronaut.core.util.StringUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hillert.micronaut.plants.config.PlantsConfig;
import com.hillert.micronaut.plants.dao.ImageRepository;
import com.hillert.micronaut.plants.dao.PlantRepository;
import com.hillert.micronaut.plants.model.Plant;
import com.hillert.micronaut.plants.service.Importer;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.runtime.event.annotation.EventListener;

/**
 * Implementation of the {@link Importer} that will import demo data from a CSV
 * source.
 *
 * @author Gunnar Hillert
 *
 * @see PlantsConfig
 *
 */
@Singleton
@Transactional
public class CsvImporter implements Importer {

	private static final Logger LOG = LoggerFactory.getLogger(CsvImporter.class);

	private final ResourceLoader resourceLoader;

	private final PlantRepository plantRepository;

	private final PlantsConfig plantsConfig;

	private final ImageRepository imageRepository;

	public CsvImporter(ResourceLoader resourceLoader, PlantRepository plantRepository, PlantsConfig plantsConfig,
			ImageRepository imageRepository) {
		super();
		this.resourceLoader = resourceLoader;
		this.plantRepository = plantRepository;
		this.plantsConfig = plantsConfig;
		this.imageRepository = imageRepository;
	}

	@Override
	public void importPlantData() {

		switch (plantsConfig.getDemoDataImportMode()) {
		case DELETE_AND_IMPORT:
			LOG.info("Clear database, then import plant data...");
			this.imageRepository.deleteAll();
			this.plantRepository.deleteAll();
			break;
		case IMPORT_ONCE:
			if (this.plantRepository.count() > 0) {
				LOG.info("Database not empty, will NOT import plant data...");
				return;
			}
			LOG.info("Database empty, will import plant data...");
			break;
		case DO_NOTHING:
			LOG.info("Will NOT import plant data...");
			return;
		}

		final BeanListProcessor<CsvRow> rowProcessor = new BeanListProcessor<CsvRow>(CsvRow.class);
		final CsvParserSettings settings = new CsvParserSettings();
		settings.setHeaderExtractionEnabled(true);
		settings.getFormat().setLineSeparator("\n");
		settings.setProcessor(rowProcessor);

		final CsvParser parser = new CsvParser(settings);
		parser.parseAll(getReader());
		final List<CsvRow> rows = rowProcessor.getBeans();

		LOG.info("Importing {} rows.", rows.size());
		final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

		for (CsvRow csvRow : rows) {
			LOG.info("Processing CSV row '{}'.", csvRow);
			final Plant plant = new Plant(csvRow.getGenus(), csvRow.getSpecies());

			plant.setPlantSignMissing(csvRow.getPlantSignMissing());
			plant.setLocation(geometryFactory.createPoint(new Coordinate(csvRow.getLongitude(), csvRow.getLatitude())));
			final Plant savedPlant = this.plantRepository.save(plant);

			if (StringUtils.hasText(csvRow.getImagePath())) {
				final String path = this.plantsConfig.getDemoDataImages() + "/" + csvRow.getImagePath();
				LOG.info("Importing image '{}'.", path);
				final InputStream is = resourceLoader.getResourceAsStream(path).get();
				final byte[] imageData;
				try {
					imageData = is.readAllBytes();
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
				final Image image = new Image(savedPlant.getId() + ".jpg", imageData, savedPlant);
				this.imageRepository.save(image);
			}
		}
	}

	public Reader getReader() {
		LOG.info("Importing data using '{}'", plantsConfig.getDemoDataFile());
		InputStream is = null;
		try {
			InputStream inputStream = is = plantsConfig.getDemoDataFile().asInputStream();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		try {
			return new InputStreamReader(is, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	@EventListener
	void init(StartupEvent event) {

		if (LOG.isInfoEnabled()) {
			LOG.info("Populating data");
		}

		this.importPlantData();

	}
}
