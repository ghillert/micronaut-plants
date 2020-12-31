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
package com.hillert.micronaut.plants.config;

import io.micronaut.context.annotation.Factory;
import io.micronaut.core.convert.TypeConverter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;

import javax.inject.Singleton;
import java.util.Optional;

/**
 * @author Gunnar Hillert
 */
@Factory
public class PointTypeConverter {

	@Singleton
	TypeConverter<String, Geometry> jtsGeometryToStringTypeConverter() {
		return (inputString, targetType, context) -> {
			Geometry geometry;
			byte[] aux = WKBReader.hexToBytes(inputString);
			try {
				geometry = new WKBReader().read(aux);
			} catch (ParseException e) {
				throw new IllegalArgumentException("Bad WKB string.", e);
			}
			return Optional.of(geometry);
		};
	}

	@Singleton
	TypeConverter<Point, String> jtsPointToStringTypeConverter() {
		return (object, targetType, context) -> {
			byte[] aux = new WKBWriter(2, true).write(object);
			return Optional.of(WKBWriter.toHex(aux));
		};
	}
}
