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
package com.hillert.micronaut.plants.support;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import javax.validation.constraints.NotNull;

/**
 * Contains various methods to calculate distance.
 *
 * @author Gunnar Hillert
 *
 */
public class DistanceUtils {

	public static final double EARTH_RADIUS_WGS84 = 6378137; // In meters
	public static final double EARTH_RADIUS_MEAN  = 6371008; // Mean Earth radius in meters

	private DistanceUtils() {
		throw new AssertionError();
	}

	/**
	 * Calculate the distance between 2 latitude/longitude values using
	 * the Haversine method.
	 *
	 * See: {@link https://en.wikipedia.org/wiki/Haversine_formula}
	 *
	 * @param latitude1
	 * @param longitude1
	 * @param latitude2
	 * @param longitude2
	 *
	 * @return Distance in meters (2 decimal points)
	 */
	public static double calculateDistanceUsingHaversine(
			double latitude1, double longitude1,
			double latitude2, double longitude2) {

		double dLat = Math.toRadians(latitude2 - latitude1);
		double dLon = Math.toRadians(longitude2 - longitude1);
		double latitude1InRadians = Math.toRadians(latitude1);
		double latitude2InRadians = Math.toRadians(latitude2);

		double a = Math.pow(Math.sin(dLat / 2),2)
			+ Math.pow(Math.sin(dLon / 2),2)
			* Math.cos(latitude1InRadians)
			* Math.cos(latitude2InRadians);
		double c = 2 * Math.asin(Math.sqrt(a));

		// Rounding to 2 decimal points
		return Math.round((EARTH_RADIUS_MEAN * c) * 100.0) / 100.0;
	}

	/**
	 * Conducts a simplistic distance calculation via {@link Geometry#distance(Geometry)}.
	 *
	 * @param point1
	 * @param point2
	 * @return Distance in meters (2 decimal points)
	 */
	public static double calculateSimpleDistance(
			@NotNull  Point point1,
			@NotNull  Point point2) {

		double distance = point1.distance(point2);
		double distanceRadians = Math.toRadians(distance);

		// Rounding to 2 decimal points
		return Math.round((distanceRadians * EARTH_RADIUS_WGS84) * 100.0) / 100.0;
	}

	public static double convertSexagesimalDegreesToDecimal(int degrees, int minutes, double seconds)
	{
		return Math.signum(degrees) * (Math.abs(degrees) + (minutes / 60.0) + (seconds / 3600.0));
	}


}
