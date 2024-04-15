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
import org.gradle.api.tasks.diagnostics.internal.graph.nodes.RenderableDependency;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Parser of dependency graph
 */
@NonNullApi
public class DependencyGraphsParser {

    private final DependencyVisitor dependencyVisitor;

    public DependencyGraphsParser(DependencyVisitor dependencyVisitor) {
        this.dependencyVisitor =dependencyVisitor;
    }

    public void parse( Collection<RenderableDependency> dependencyList) {
        dependencyList.forEach(this::parseDependency);
    }


    private void parseDependency( RenderableDependency dependency) {
        Set<Object> visited  = new HashSet<>();
        visited.add(dependency.getId());
        renderChildren(dependency.getChildren(), visited);
    }


    private void renderChildren( Set<? extends RenderableDependency> children,  Set<Object> visited) {
        children.forEach ( it -> doRender(it, visited) );
    }

    private void doRender( RenderableDependency node, Set<Object> visited) {
        // Do a shallow render of any constraint edges, and do not mark the dependency as visited.
        if (node.getResolutionState() == RenderableDependency.ResolutionState.RESOLVED_CONSTRAINT) {
            analyseDependency(node);
            return;
        }

        boolean alreadyRendered = !visited.add(node.getId());
        analyseDependency(node);

        if (!alreadyRendered) {
            renderChildren(node.getChildren(), visited);
        }
    }


    private void analyseDependency( RenderableDependency dependency) {
        dependencyVisitor.visitDependency(dependency);
    }
}
