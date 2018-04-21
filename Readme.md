[![Maven Central](https://img.shields.io/maven-central/v/com.thoughtworks.gauge.gradle/gauge-gradle-plugin.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22gauge-gradle-plugin%22)
[![Download](https://api.bintray.com/packages/manupsunny/maven/gauge-gradle-plugin/images/download.svg) ](https://bintray.com/manupsunny/maven/gauge-gradle-plugin/_latestVersion)
[![Build Status](https://snap-ci.com/manupsunny/gauge-gradle-plugin/branch/master/build_image)](https://snap-ci.com/manupsunny/gauge-gradle-plugin/branch/master)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/d4d3e7d6c4ce4fa3a79f2790167fd511)](https://www.codacy.com/app/manupsunny/gauge-gradle-plugin)
[![License](http://img.shields.io/:license-gpl3-blue.svg)](https://www.gnu.org/licenses/gpl.txt)


# Gauge Gradle Plugin

Use the gauge-gradle-plugin to execute specifications in your [Gauge](http://getgauge.io) java project and manage dependencies using [Gradle](http://gradle.org//).

### Using plugin in project

Apply plugin ***gauge*** and add classpath to your ***build.gradle***. Here is a sample gradle file,

````groovy
apply plugin: 'java'
apply plugin: 'gauge'
apply plugin: 'application'

group = "my-gauge-tests"
version = "1.1.0"

description = "My Gauge Tests"

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'com.thoughtworks.gauge.gradle:gauge-gradle-plugin:+'
    }
}

repositories {
    mavenCentral()
}

dependencies {
    'com.thoughtworks.gauge:gauge-java:+',
}

// configure gauge task here (optional)
gauge {
    specsDir = 'specs'
    inParallel = true
    nodes = 2
    env = 'dev'
    tags = 'tag1'
    additionalFlags = '--verbose'
    gaugeRoot = '/opt/gauge'
}

````

The plugin is also available at [Gradle Plugin Portal](https://plugins.gradle.org/). Find more details [here](https://plugins.gradle.org/plugin/com.thoughtworks.gauge)..

### Executing specs using gradle
To execute gauge specs,

````
gradle gauge
````

#### Execute list of specs
```
gradle gauge -PspecsDir="specs/first.spec specs/second.spec"
```
#### Execute specs in parallel
```
gradle gauge -PinParallel=true -PspecsDir=specs
```
#### Execute specs by tags
```
gradle gauge -Ptags="!in-progress" -PspecsDir=specs
```
#### Specifying execution environment
```
gradle gauge -Penv="dev" -PspecsDir=specs
```

Note : Pass specsDir parameter as the last one.

### All additional Properties
The following plugin properties can be additionally set:

|Property name|Usage|Description|
|-------------|-----|-----------|
|specsDir| -PspecsDir=specs| Gauge specs directory path. Required for executing specs|
|tags    | -Ptags="tag1 & tag2" |Filter specs by specified tags expression|
|inParallel| -PinParallel=true | Execute specs in parallel|
|nodes    | -Pnodes=3 | Number of parallel execution streams. Use with ```parallel```|
|env      | -Penv=qa  | gauge env to run against  |
|additionalFlags| -PadditionalFlags="--verbose" | Add additional gauge flags to execution|
|gaugeRoot| -PgaugeRoot="/opt/gauge" | Path to gauge installation root|


See gauge's [command line interface](http://getgauge.io/documentation/user/current/cli/index.html) for list of all flags that be used with **-PadditionalFlags** option.

### Adding/configuring custom Gauge tasks
It is possible to define new custom Gauge tasks by extending `GaugePlugin` class. It can be used to create/configure tasks specific for different environments. For example,

````groovy
task gaugeDev(type: GaugeTask) {
    doFirst {
        gauge {
            specsDir = 'specs'
            inParallel = true
            nodes = 2
            env = 'dev'
            additionalFlags = '--verbose'
        }
    }
}

task gaugeTest(type: GaugeTask) {
    doFirst {
        gauge {
            specsDir = 'specs'
            inParallel = true
            nodes = 4
            env = 'test'
            additionalFlags = '--verbose'
        }
    }
}
````

### License

![GNU Public License version 3.0](http://www.gnu.org/graphics/gplv3-127x51.png)

Gauge Gradle Plugin is released under [GNU Public License Version 3.0](http://www.gnu.org/licenses/gpl-3.0.txt)
