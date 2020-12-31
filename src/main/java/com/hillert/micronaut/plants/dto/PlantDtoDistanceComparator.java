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
package com.hillert.micronaut.plants.dto;

import java.util.Comparator;

/**
 * @author Gunnar Hillert
 */
public class PlantDtoDistanceComparator implements Comparator<PlantDto> {

	@Override
	public int compare(PlantDto plant1, PlantDto plant2) {
		return Double.compare(plant1.getDistance(), plant2.getDistance());
	}

}