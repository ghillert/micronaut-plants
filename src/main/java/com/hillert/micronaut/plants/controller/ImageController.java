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

import com.hillert.micronaut.plants.controller.problems.ImageNotFoundProblem;
import com.hillert.micronaut.plants.model.Image;
import com.hillert.micronaut.plants.service.ImageService;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;

import javax.inject.Inject;

/**
 * Explicit controller for retrieving Plant images.
 *
 * @author Gunnar Hillert
 *
 */
@Controller("/api/images")
public class ImageController {

	@ReflectiveAccess
	@Inject
	private ImageService imageService;

	/**
	 * Get a specific image for the given image id.
	 *
	 * @param imageId
	 * @return The image (Currently JPG only)
	 * @throws ImageNotFoundProblem in case no image was found
	 */
	@Get("/{imageId}")
	public HttpResponse<byte[]> getImage(@PathVariable Long imageId) {

		final Image imageFromDb = imageService.getSingleImage(imageId).orElseGet(() -> {
			throw new ImageNotFoundProblem(imageId);
		});

		return HttpResponse
			.ok()
			.contentType(MediaType.IMAGE_JPEG)
			.body(imageFromDb.getImage());
	}
}
