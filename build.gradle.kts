import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.23"
    `java-gradle-plugin`
    `java-library`
    `maven-publish`
    groovy
    id("com.gradle.plugin-publish") version "1.2.1"
}


group = "io.gofannon.gradle"


gradlePlugin {
    website.set("https://github.com/gofannon-io/cots-report-gradle-plugin")
    vcsUrl.set("https://github.com/gofannon-io/cots-report-gradle-plugin")

    plugins {
        create("cotsReportPlugin") {
            id = "io.gofannon.cots-report"
            implementationClass = "io.gofannon.gradle.cots.report.CotsReportPlugin"
            displayName = "Cots Report Plugin"
            description = "Gradle plugin to generate reports of project's COTS (Commercial Off-The Shelve)"
            tags.set(listOf("cots", "report", "dependency"))
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

    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.spockframework:spock-core:2.3-groovy-3.0")
    testImplementation(gradleTestKit())
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

tasks.withType<Jar> {
    from(".") {
        include("LICENSE")
        into("META-INF")
    }
    manifest {
        attributes("License" to "Apache License 2.0")
    }
}