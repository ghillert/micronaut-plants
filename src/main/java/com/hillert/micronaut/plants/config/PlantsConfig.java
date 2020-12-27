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

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Context;
import io.micronaut.core.io.Readable;

/**
 * Plants-application-specific configuration properties.
 *
 * @author Gunnar Hillert
 *
 */
@ConfigurationProperties("plants")
@Context
public class PlantsConfig {

	private Readable demoDataFile;
	private String demoDataImages;

	private DemoDataImportMode demoDataImportMode;

	public Readable getDemoDataFile() {
		return demoDataFile;
	}

	public void setDemoDataFile(Readable demoDataFile) {
		this.demoDataFile = demoDataFile;
	}

	public DemoDataImportMode getDemoDataImportMode() {
		return demoDataImportMode;
	}

	public void setDemoDataImportMode(DemoDataImportMode demoDataImportMode) {
		this.demoDataImportMode = demoDataImportMode;
	}

	public String getDemoDataImages() {
		return demoDataImages;
	}

	public void setDemoDataImages(String demoDataImages) {
		this.demoDataImages = demoDataImages;
	}

	public enum DemoDataImportMode {
		DO_NOTHING,
		DELETE_AND_IMPORT,
		IMPORT_ONCE
	}
}
