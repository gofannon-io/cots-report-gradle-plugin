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

import spock.lang.Specification

class CotsContextImplTest extends Specification {

    def "shall ignore inner projects"() {
        given:
        def cotsContext = new CotsContextImpl()

        expect:
        !cotsContext.accept("project :another")
    }


    def "shall add single ignored groupId"() {
        given:
        def cotsContext = new CotsContextImpl()

        when:
        cotsContext.addIgnoredGroupId("org.sample")

        then:
        !cotsContext.accept("org.sample:lol:1.0")
        cotsContext.accept("org.other:lol:1.0")
    }

    def "shall add several ignored groupIds"() {
        given:
        def cotsContext = new CotsContextImpl()

        when:
        cotsContext.addIgnoredGroupIds(Arrays.asList("org.sample", "com.test"))

        then:
        !cotsContext.accept("org.sample:app1:3.0")
        !cotsContext.accept("com.test:app2:2.1")
        cotsContext.accept("org.other:app3:4.2")
    }
}
