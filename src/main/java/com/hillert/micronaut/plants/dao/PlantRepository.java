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

package com.hillert.micronaut.plants.dao;

import com.hillert.micronaut.plants.model.Plant;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.annotation.TypeDef;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.DataType;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;
import org.locationtech.jts.geom.Point;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author Gunnar Hillert
 */
@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
public interface PlantRepository extends PageableRepository<Plant, Long> {

	@Query("SELECT * from Plants p WHERE st_dwithin(p.location, CAST( :location as geometry), :radius, true) = true")
	public abstract List<Plant> getPlantsWithinRadius(@TypeDef(type = DataType.STRING) Point location, double radius);

    //@Join("plant")
	//@Query("SELECT * from Plants p WHERE p.location = :location")
    public abstract Optional<Plant> findByLocation(@NotNull @TypeDef(type = DataType.STRING) Point location);
    //Optional<Plant> findByLocation(@NotNull String location);
}
