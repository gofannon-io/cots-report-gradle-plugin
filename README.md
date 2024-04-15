# COTS-Report plugin

## Objective
The COTS-Report plugin generates a report containing all COTS (Commercial Off-The Shelve) of the project.

In software development, a COTS is a dependency (artifact) which is used as-is and not developed by the software team.

### A gradle dependency like task
It is very similar to `gradle dependency` task.
But there are some differences:
* the presentation is a list of artifacts
  * *dependency* task display trees
* the displayed artifacts are unique and are sorted by name
  * *dependency* task display the artifacts each time they appear in the dependency tree
* the displayed artifacts are filtered. The inner projects and the dependencies developed locally are hidden.
  * *dependency* task display all the artifacts

### Why ?
In software companies, it is usual for developers to provide a list of COTS to other teams:
* the legal team, to check that the COTS licences are compatible with the software licence,
* the cybersecurity team, to check the vulnerabilities of the COTS,
* the documentation team, to integrate the COTS list into the documentation.


## Usage

### Plugin declaration
First, declares the plugin

__Java__
```groovy
plugins {
//  [...]
    id 'xyz.gofannon.cots-report' version("0.1.0-SNAPSHOT")
}
```

__Kotlin__
```kotlin
plugins {
//  [...]
    id("xyz.gofannon.cots-report") version ("0.1.0-SNAPSHOT")
}
```


### Extension usage
The plugin provides the `cotsReport` extension.
This extension contains 3 properties:
* **reportFile** which is the path to the report. This property is optional. By default, the report file is `build/reports/project/cots-report.txt`.
* **configurations** which contains the list of the configurations to parse. This property is optional. By default, the configuration is `runtimeClasspath`.
* **ignorableGroupIds** which contains the list of the groups to ignore. This property is optional. By default, there is no ignorable group.


### Examples:

__Groovy__
```groovy
cotsReporting {
    reportFile = layout.buildDirectory.file("my-report.txt")
    configurations = ["runtimeClasspath", "testRuntimeClasspath"]
    ignorableGroupIds = ["commons-io", "com.fasterxml.jackson.module", "org.jetbrains.kotlin:kotlin-stdlib"]
}
```

__Kotlin__
```kotlin
cotsReporting {
    reportFile = layout.buildDirectory.file("sample.txt")
    configurations.set(listOf("runtimeClasspath","testRuntimeClasspath"))
    ignorableGroupIds.set(listOf("commons-io", "com.fasterxml.jackson.module", "jackson-module-kotlin"))
}
```


## Run
To run the plugin, just execute the task 

```shell
gradle :cotsReport
```
