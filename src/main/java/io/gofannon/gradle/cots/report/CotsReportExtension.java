/*
 * Copyright (c) 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gofannon.gradle.cots.report;

import org.gradle.api.NonNullApi;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputFile;

import javax.inject.Inject;

@NonNullApi
public abstract class CotsReportExtension {
    private final RegularFileProperty reportFile;
    private final ListProperty<String> configurations ;
    private final ListProperty<String> ignorableGroupIds ;

    @Inject
    public CotsReportExtension(ObjectFactory objectFactory) {
        this.reportFile = objectFactory.fileProperty();
        this.configurations = objectFactory.listProperty(String.class);
        this.ignorableGroupIds = objectFactory.listProperty(String.class);
    }

    @OutputFile
    @Optional
    public RegularFileProperty getReportFile() {
        return this.reportFile;
    }

    @Input
    @Optional
    public ListProperty<String> getConfigurations() {
        return this.configurations;
    }


    @Input
    @Optional
    public ListProperty<String> getIgnorableGroupIds() {
        return ignorableGroupIds;
    }
}