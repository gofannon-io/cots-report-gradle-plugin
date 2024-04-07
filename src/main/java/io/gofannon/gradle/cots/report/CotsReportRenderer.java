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
import org.gradle.api.artifacts.result.ResolvedComponentResult;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.diagnostics.internal.ConfigurationDetails;
import org.gradle.api.tasks.diagnostics.internal.DependencyReportRenderer;
import org.gradle.api.tasks.diagnostics.internal.ProjectDetails;
import org.gradle.api.tasks.diagnostics.internal.TextReportRenderer;
import org.gradle.api.tasks.diagnostics.internal.graph.nodes.RenderableDependency;
import org.gradle.api.tasks.diagnostics.internal.graph.nodes.RenderableModuleResult;
import org.gradle.api.tasks.diagnostics.internal.graph.nodes.UnresolvableConfigurationResult;

import java.util.List;

@NonNullApi
public class CotsReportRenderer extends TextReportRenderer implements DependencyReportRenderer {

    private final DependencyCollector dependencyCollector;

    private DependencyGraphsParser dependencyGraphParser;

    public CotsReportRenderer(DependencyCollector dependencyCollector) {
        this.dependencyCollector = dependencyCollector;
    }

    @Override
    public void startProject(ProjectDetails project) {
        getTextOutput().println("---------------------------------");
        getTextOutput().println("--- " + project.getDisplayName());
        getTextOutput().println("---------------------------------");

        dependencyGraphParser = new DependencyGraphsParser(dependencyCollector);
    }


    @Override
    public void startConfiguration(ConfigurationDetails configuration) {
        dependencyCollector.visitConfiguration(configuration);
    }

    @Override
    public void completeConfiguration(ConfigurationDetails configuration) {
    }

    public void render(ConfigurationDetails configuration) {
        if (configuration.isCanBeResolved())
            parseResolvableConfiguration(configuration);
        else
            parseUnresolvableConfiguration(configuration);
    }

    private void parseResolvableConfiguration(ConfigurationDetails configuration) {
        Provider<ResolvedComponentResult> resolutionResultRootProvider = configuration.getResolutionResultRoot();
        if (resolutionResultRootProvider != null && resolutionResultRootProvider.isPresent()) {
            ResolvedComponentResult result = resolutionResultRootProvider.get();
            RenderableDependency rootModule = new RenderableModuleResult(result);
            parseRenderableDependency(rootModule);
        }
    }

    private void parseUnresolvableConfiguration(ConfigurationDetails configuration) {
        UnresolvableConfigurationResult unresolvableResult = configuration.getUnresolvableResult();
        if (unresolvableResult != null)
            parseRenderableDependency(unresolvableResult);
    }

    public void parseRenderableDependency(RenderableDependency root) {
        if (!root.getChildren().isEmpty()) {
            dependencyGraphParser.parse(List.of(root));
        }
    }

    @Override
    public void complete() {
        getTextOutput().println("--- COTS configurations");
        for (var configurationName : dependencyCollector.getConfigurationList()) {
            getTextOutput().println(configurationName);
        }

        getTextOutput().println();

        getTextOutput().println("--- COTS dependencies");
        List<String> dependencyList = dependencyCollector.getDependencyIdList();
        dependencyList.sort(String::compareTo);
        for (var dependencyId : dependencyList) {
            getTextOutput().println(dependencyId);
        }
    }
}