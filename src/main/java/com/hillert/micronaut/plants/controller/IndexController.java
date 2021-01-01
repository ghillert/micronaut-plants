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

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.UriMapping;
import io.micronaut.http.server.types.files.StreamedFile;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

/**
 * @author Gunnar Hillert
 */
@Controller
public class IndexController {

	@Inject
	ResourceLoader resourceLoader;

	@Get(uris = { "/plants{/path:.*}", "/garden{/path:.*}", "/about{/path:.*}" })
	@Produces(MediaType.TEXT_HTML)
	public Optional<StreamedFile> defaultPage(@Nullable String path) {
		return resourceLoader.getResource("classpath:public/index.html").map(StreamedFile::new);
	}

}