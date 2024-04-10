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

class CotsReportHelper {

    static def extractConfigurations(File reportFile) {
        return extractSectionElements(reportFile, "--- COTS configurations")
    }

    private static def extractSectionElements(File reportFile, String sectionTitle) {
        def inside = false
        def configurationList = new ArrayList<String>()
        for (def line : reportFile.text.lines()) {
            if (line == sectionTitle) {
                inside = true
            } else if (inside) {
                if (line.isEmpty())
                    break
                configurationList.add(line)
            }
        }
        return configurationList
    }

    static def extractDependencies(File reportFile) {
        return extractSectionElements(reportFile, "--- COTS dependencies")
    }


    static def formatToClickableUrl(File file) {
        def url = file.toURI().toString()
        if (url.startsWith("file:///"))
            return url
        if (url.startsWith("file:/"))
            return url.replace("file:/", "file:///")
        return "file:///" + url
    }

}
