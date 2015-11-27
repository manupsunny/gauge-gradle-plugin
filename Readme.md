# Gauge-gradle-plugin

Use the gauge-gradle-plugin to execute specifications in your gauge java project and manage dependencies using [gradle](http://gradle.org//).

### Using plugin in project

Apply plugin ***gauge*** and add classpath to your ***build.gradle***. Here is a sample gradle file,

````groovy
apply plugin: 'java'
apply plugin: 'gauge'
apply plugin: 'application'

group = "my-gauge-tests"
version = "1.0.0"

description = "My Gauge Tests"

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'com.thoughtworks.gauge.gradle:gauge-gradle-plugin:1.0-SNAPSHOT'
        classpath files("$projectDir/libs")
    }
}

repositories {
    mavenCentral()
}

dependencies {
}

// configure gauge task here (optional)
gauge {
    specsDir = 'specs'
    inParallel = true
    nodes = 2
    env = 'dev'
    tags = 'tag1'
    additionalFlags = '--verbose'
}

````

### Executing specs using gradle
To execute gauge specs,
````
gradle gauge
````

#### Execute specs in parallel
```
gradle gauge -PspecsDir=specs -PinParallel=true
```
#### Execute specs by tags
```
gradle gauge -PspecsDir=specs -Ptags="!in-progress"
```
#### Specifying execution environment
```
gradle gauge -PspecsDir=specs -Penv="dev"
```
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


See gauge's [command line interface](http://getgauge.io/documentation/user/current/cli/index.html) for list of all flags that be used with **-PadditionalFlags** option.


