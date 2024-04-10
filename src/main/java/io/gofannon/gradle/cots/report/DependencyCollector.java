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
import org.gradle.api.tasks.diagnostics.internal.ConfigurationDetails;
import org.gradle.api.tasks.diagnostics.internal.graph.nodes.RenderableDependency;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Collector of dependencies
 */
@NonNullApi
public class DependencyCollector implements DependencyVisitor {

    private final DependencyCollectorConfiguration configuration;

    private final SortedSet<String> dependencyIdSet = new TreeSet<>();
    private final SortedSet<String> configurationNameSet = new TreeSet<>();


    /**
     * Create a new dependency collector
     *
     * @param configuration all information to select the dependencies to collect
     */
    public DependencyCollector(DependencyCollectorConfiguration configuration) {
        this.configuration = configuration;
    }


    /**
     * Visit a Gradle dependency (aka JAR)
     *
     * @param dependency a Gradle dependency
     */
    @Override
    public void visitDependency(RenderableDependency dependency) {
        if (configuration.accept(dependency))
            dependencyIdSet.add(dependency.getName());
    }

    /**
     * Get all collected dependencies
     *
     * @return the list of collected dependencies
     */
    public List<String> getDependencyIdList() {
        return new ArrayList<>(dependencyIdSet);
    }


    /**
     * Visit a Gradle configuration
     *
     * @param configuration the Gradle configuration details
     */
    @Override
    public void visitConfiguration(ConfigurationDetails configuration) {
        configurationNameSet.add(configuration.getName());
    }

    public List<String> getConfigurationList() {
        return new ArrayList<>(configurationNameSet);
    }
}
