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

import org.gradle.api.NonNullApi;
import org.gradle.api.tasks.diagnostics.internal.ProjectDetails;
import org.gradle.internal.logging.text.StyledTextOutput;

import java.util.List;

/**
 * Dependency report formatter that generates report in text format
 */
@NonNullApi
public class TextReportFormatter implements ReportFormatter {

    private StyledTextOutput output;

    @Override
    public void setOutput(StyledTextOutput output) {
        this.output = output;
    }

    @Override
    public void printProjectHeader(ProjectDetails project) {
        output.println("---------------------------------")
                .println("--- " + project.getDisplayName())
                .println("---------------------------------");
    }

    @Override
    public void printConfigurations(List<String> configurationNames) {
        output.println("--- COTS configurations");
        configurationNames.stream()
                .sorted(String::compareTo)
                .forEach(output::println);
        output.println();
    }


    @Override
    public void printDependencies(List<String> dependencyIdList) {
        output.println("--- COTS dependencies");
        dependencyIdList.stream()
                .sorted(String::compareTo)
                .forEach(output::println);
    }
}
