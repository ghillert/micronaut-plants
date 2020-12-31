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

import com.hillert.micronaut.plants.dao.ImageRepository;
import com.hillert.micronaut.plants.model.Image;
import com.hillert.micronaut.plants.service.ImageService;
import io.micronaut.core.io.ResourceLoader;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
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
public class DefaultImageService implements ImageService {

	final ResourceLoader resourceLoader;

	final ImageRepository imageRepository;

	public DefaultImageService(ResourceLoader resourceLoader, ImageRepository imageRepository) {
		this.resourceLoader = resourceLoader;
		this.imageRepository = imageRepository;
	}

	//@Cacheable
	@Override
	public Optional<Image> getSingleImage(Long id) {
		Object a = this.imageRepository.findById(id).get();
		return this.imageRepository.findById(id);
	}

	@Override
	public List<Long> getImageIdsForPlant(Long plantId) {
		return this.imageRepository.getImageIdsForPlantId(plantId);
	}

	@Override
	public byte[] loadImage(String path) {
		InputStream is = null;
		byte[] image;
		try {
			is = resourceLoader.getResourceAsStream(path).get();
			image = IOUtils.toByteArray(is);
			is.close();
		} catch (IOException e) {
			throw new IllegalStateException("Unable to process image resource.", e);
		}
		finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
			}
		}
		return image;
	}

}
