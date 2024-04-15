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
package xyz.gofannon.gradle.cots.report;

import org.gradle.api.Action;
import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * The plugin for generating COTS reports
 */
@NonNullApi
@SuppressWarnings("unused")
public abstract class CotsReportPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(CotsReportPlugin.class);

        CotsReportExtension extension =
                project.getExtensions().create("cotsReporting", CotsReportExtension.class);

        project.getTasks().register(
                "cotsReport",
                CotsReportGenerationTask.class,
                new CotsReportTaskAction(extension)
        );
    }

    @NonNullApi
    private static class CotsReportTaskAction implements Action<CotsReportGenerationTask> {
        private final CotsReportExtension extension;

        public CotsReportTaskAction(CotsReportExtension extension) {
            this.extension = extension;
        }

        @Override
        public void execute(CotsReportGenerationTask task) {
            task.setGroup("reporting");
            task.setDescription("Generates a report with all COTS.");
            task.setImpliesSubProjects(true);
            task.initialize(extension);
        }
    }
}
