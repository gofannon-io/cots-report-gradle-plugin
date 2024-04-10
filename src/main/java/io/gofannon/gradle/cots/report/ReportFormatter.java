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
import org.gradle.api.tasks.diagnostics.internal.ProjectDetails;
import org.gradle.internal.logging.text.StyledTextOutput;

import java.util.List;

/**
 * Formatter for dependency report
 */
@NonNullApi
public interface ReportFormatter {

    /**
     * Inject the  console output
     *
     * @param output the console output
     */
    void setOutput(StyledTextOutput output);

    /**
     * Print the header of the project
     *
     * @param project the project to print
     */
    void printProjectHeader(ProjectDetails project);

    /**
     * Print the configurations in the project
     *
     * @param configurationNames the names of the configurations
     */
    void printConfigurations(List<String> configurationNames);

    /**
     * Print the dependencies in the project
     *
     * @param dependencyIdList the list of the dependency identifiers
     */
    void printDependencies(List<String> dependencyIdList);
}
