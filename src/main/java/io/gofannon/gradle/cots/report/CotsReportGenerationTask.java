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
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.reporting.ReportingExtension;
import org.gradle.api.tasks.diagnostics.AbstractDependencyReportTask;
import org.gradle.api.tasks.diagnostics.internal.ConfigurationFinder;
import org.gradle.api.tasks.diagnostics.internal.DependencyReportRenderer;
import org.gradle.work.DisableCachingByDefault;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@NonNullApi
@DisableCachingByDefault(because = "Not worth caching")
public abstract class CotsReportGenerationTask extends AbstractDependencyReportTask {

    public void initialize(CotsReportExtension extension) {
        initializeReportRenderer(extension);
        initializeReportFile(extension);
        initializeConfiguration(extension);
    }

    private void initializeReportRenderer(CotsReportExtension extension) {
        CotsContext context = createContext(extension);
        DependencyCollector dependencyCollector = new DependencyCollector(context);
        DependencyReportRenderer reportRenderer = new CotsReportRenderer(dependencyCollector);
        setRenderer(reportRenderer);
    }


    private CotsContext createContext(CotsReportExtension extension) {
        CotsContextImpl context = new CotsContextImpl();

        ListProperty<String> ignorableGroupIdsProperty = extension.getIgnorableGroupIds();
        if (ignorableGroupIdsProperty.isPresent() && !ignorableGroupIdsProperty.get().isEmpty()) {
            context.addIgnoredGroupIds(ignorableGroupIdsProperty.get());
        }

        return context;
    }


    private void initializeReportFile(CotsReportExtension extension) {
        RegularFileProperty reportFileProperty = extension.getReportFile();

        File reportFile = reportFileProperty.isPresent()
                ? reportFileProperty.getAsFile().get()
                : getDefaultReportFile();

        setOutputFile(reportFile);
    }

    private File getDefaultReportFile() {
        File directory = getProject().getExtensions().getByType(ReportingExtension.class).file("project");
        return new File(directory, "cots-report.txt");
    }


    private void initializeConfiguration(CotsReportExtension extension) {
        var configurationNames = computeConfigurationNames(extension);
        var configurations = toConfigurationSet(configurationNames);
        setConfigurations(configurations);
    }


    private Set<String> computeConfigurationNames(CotsReportExtension extension) {
        ListProperty<String> configuredConfigurations = extension.getConfigurations();
        if (configuredConfigurations.isPresent() && !configuredConfigurations.get().isEmpty()) {
            return new HashSet<>(configuredConfigurations.get());
        }
        return Set.of("runtimeClasspath");
    }


    @Override
    public ConfigurationContainer getTaskConfigurations() {
        return getProject().getConfigurations();
    }

    private Set<Configuration> toConfigurationSet(Collection<String> configurationNames) {
        return configurationNames
                .stream()
                .map(this::configurationOf)
                .collect(Collectors.toSet());
    }

    private Configuration configurationOf(String configurationName) {
        return ConfigurationFinder.find(getTaskConfigurations(), configurationName);
    }
}
