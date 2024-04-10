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

package io.gofannon.gradle.cots.report

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.TempDir

import static io.gofannon.gradle.cots.report.CotsReportHelper.*
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class BuildLogicFunctionalTest extends Specification {

    @TempDir
    File testProjectDir
    File settingsFile
    File buildFile
    File defaultReportFile
    BuildResult buildResult

    private static String baseBuildGradle = """
            plugins {
                id 'java'
                id 'io.gofannon.cots-report' version("1.0.0-SNAPSHOT")
            }

            dependencies {
                implementation "org.junit.platform:junit-platform-commons:1.10.2"
                testImplementation("commons-io:commons-io:2.16.0")
            }
            
            repositories {
                mavenCentral()
            }
        """

    def setup() {
        settingsFile = new File(testProjectDir, 'settings.gradle')
        buildFile = new File(testProjectDir, 'build.gradle')
        defaultReportFile = new File(testProjectDir, "build/reports/project/cots-report.txt")
    }

    def "can execute cotsReport task with no cotsReporting extension"() {
        given:
        buildFile << baseBuildGradle
        settingsFile << ""


        when:
        runGradleCotsReportTask()
        def reportFile = defaultReportFile


        then:
        buildResult.task(":cotsReport").outcome == SUCCESS
        buildResult.output.contains("See the report at: " + formatToClickableUrl(reportFile))
        reportFile.exists()
        extractConfigurations(reportFile) == List.of("runtimeClasspath")
        extractDependencies(reportFile) == List.of("org.junit.platform:junit-platform-commons:1.10.2", "org.junit:junit-bom:5.10.2")
    }

    private def runGradleCotsReportTask() {
        buildResult= GradleRunner.create()
                .withGradleVersion("8.4")
                .withProjectDir(testProjectDir)
                .withPluginClasspath()
                .withArguments('cotsReport')
                .build()
    }


    def "can execute cotsReport task with empty cotsReporting extension"() {
        given:
        buildFile << baseBuildGradle+"""
            cotsReporting {}
            """
        settingsFile << ""

        when:
        runGradleCotsReportTask()
        def reportFile = defaultReportFile


        then:
        buildResult.task(":cotsReport").outcome == SUCCESS
        buildResult.output.contains("See the report at: " + formatToClickableUrl(reportFile))
        reportFile.exists()
        extractConfigurations(reportFile) == List.of("runtimeClasspath")
        extractDependencies(reportFile) == List.of("org.junit.platform:junit-platform-commons:1.10.2", "org.junit:junit-bom:5.10.2")
    }




    def "can execute cotsReport task with reportFile property set"() {
        given:
        buildFile << baseBuildGradle+"""
            cotsReporting {
                reportFile = layout.buildDirectory.file("sample.txt")
            }
            """
        settingsFile << ""

        when:
        runGradleCotsReportTask()
        def reportFile = new File(testProjectDir, "build/sample.txt")


        then:
        buildResult.task(":cotsReport").outcome == SUCCESS
        buildResult.output.contains("See the report at: " + formatToClickableUrl(reportFile))
        reportFile.exists()
        extractConfigurations(reportFile) == List.of("runtimeClasspath")
        extractDependencies(reportFile) == List.of("org.junit.platform:junit-platform-commons:1.10.2", "org.junit:junit-bom:5.10.2")
    }


    def "can execute cotsReport task with ignorableGroupIds property set"() {
        given:
        buildFile << baseBuildGradle+"""
            cotsReporting {
                ignorableGroupIds = ["org.junit"]
            }
            """
        settingsFile << ""

        when:
        runGradleCotsReportTask()
        def reportFile = defaultReportFile


        then:
        buildResult.task(":cotsReport").outcome == SUCCESS
        buildResult.output.contains("See the report at: " + formatToClickableUrl(reportFile))
        reportFile.exists()
        extractConfigurations(reportFile) == List.of("runtimeClasspath")
        extractDependencies(reportFile) == List.of("org.junit.platform:junit-platform-commons:1.10.2")
    }


    def "can execute cotsReport task with configurations property set"() {
        given:
        buildFile << baseBuildGradle+"""
            cotsReporting {
                configurations = ["testRuntimeClasspath"]
            }
            """
        settingsFile << ""

        when:
        runGradleCotsReportTask()
        def reportFile = defaultReportFile


        then:
        buildResult.task(":cotsReport").outcome == SUCCESS
        buildResult.output.contains("See the report at: " + formatToClickableUrl(reportFile))
        reportFile.exists()
        extractConfigurations(reportFile) == List.of("testRuntimeClasspath")
        extractDependencies(reportFile) == List.of("commons-io:commons-io:2.16.0","org.junit.platform:junit-platform-commons:1.10.2", "org.junit:junit-bom:5.10.2")
    }

}
