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
package com.hillert.micronaut.plants.controller.problems;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class PlantNotFoundProblem extends AbstractThrowableProblem {

	private static final long serialVersionUID = -9065704227851160586L;

	private static final URI TYPE = URI.create("urn:problem-type:image-not-found");

	public PlantNotFoundProblem(Long plantId) {
		super(
			TYPE,
			"Not found",
			Status.NOT_FOUND,
			String.format("Plant '%s' not found", plantId));
	}
}
