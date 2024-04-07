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
import org.gradle.api.tasks.diagnostics.internal.graph.nodes.RenderableDependency;

/**
 * Configuration for dependency collectors
 */
@NonNullApi
public interface DependencyCollectorConfiguration {
    /**
     * Check if a dependency is accepted by the filter set in Gradle configuration
     *
     * @param dependency a dependency
     * @return true is the dependency is accepted by the filter, false otherwise
     */
    default boolean accept(RenderableDependency dependency) {
        return accept(dependency.getName());
    }

    /**
     * Check if a dependency is accepted by the filter set in Gradle configuration
     *
     * @param dependencyId the dependency identifier (groupId:artifactId:version)
     * @return true is the dependency is accepted by the filter, false otherwise
     */
    boolean accept(String dependencyId);
}