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
package com.hillert.micronaut.plants;

import com.hillert.micronaut.plants.service.impl.CsvRow;
import com.univocity.parsers.conversions.DoubleConversion;
import com.univocity.parsers.conversions.TrimConversion;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.micronaut.runtime.Micronaut;

/**
 * @author Gunnar Hillert
 */
@Introspected(classes = {
		Page.class, Pageable.class, Sort.class,
		CsvRow.class, TrimConversion.class, DoubleConversion.class})
public class Application {
	public static void main(String[] args) {
		Micronaut.run(Application.class, args);
	}
}
