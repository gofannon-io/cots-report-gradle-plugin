import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.23"
    `java-gradle-plugin`
    `java-library`
    `maven-publish`
    groovy
}


group = "io.gofannon.gradle"


gradlePlugin {
    plugins {
        create("cotsReportPlugin") {
            id = "io.gofannon.cots-report"
            implementationClass = "io.gofannon.gradle.cots.report.CotsReportPlugin"
        }
    }
}


repositories {
    maven {
        url = uri("https://repo.gradle.org/gradle/libs-releases/")
    }
    mavenCentral()
}

dependencies {
    compileOnly("org.gradle:gradle-tooling-api:7.4")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")

    testImplementation("org.codehaus.groovy:groovy:3.0.21")
    //testImplementation("commons-io:commons-io:2.16.0")

    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.spockframework:spock-core:2.3-groovy-3.0")
    testImplementation(gradleTestKit())
    //testImplementation(testFixtures("org.gradle:gradle-core:8.4"))
    //testImplementation("org.gradle:gradle-test-kit:8.4")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

tasks.withType<Wrapper> {
    gradleVersion = "8.4"
}