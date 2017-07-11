# Aurora Gradle Docker Plugin

Aurora Gradle Docker Plugin is a gradle plugin that makes it easier to build docker images with gradle.

The plugin is using the CLI to do the docker build itself


If the  Aurora Gradle Plugin is used aswell version and revision  will be set automatically. These are used by the plugin to generate tags.
 


## Getting started 

Add the following to the top of your gradle file `build.gradle` 

    buildscript {
    
        ...
    
        dependencies {
            'no.skatteetaten.aurora.gradle.plugins:aurora-docker-plugin:1.1.3'
        }
    }
    
    apply plugin: 'no.skatteetaten.plugins.aurora-docker'

    auroradocker {
      imageName = 'group/image'
      registry = //the registry to push to
      workingDir = "$buildDir/docker" // Mappen Dockefile ligger
      buildArgs = [
        arg1: 'value'
      ]
    }



## Functionality

### Create tasks for build tag, push

 * buildImage - Build image with  `docker build`. After the build is done the id for the built image is available in the `imageId` propertyen on the task. 
 * tagImage - tag the build image with tags 
 * pushImage - Push tags


## Generating tags

Several tags are made based up on `Semantic Versioning`. See the table below

| Versjon         | Tagger                                                                       |
|-----------------|------------------------------------------------------------------------------|
| 1.0.0           | `1`, `1.0`, `1.0.0`, `latest`                                                |
| 1.0.0-rc.1      | `1.0.0-rc.1`                                                                 |
| 1.0.0-GA        | `1.0.0-GA`                                                                   |
| master-SNAPSHOT | `master-SNAPSHOT` and if  `revision` is set `master-SNAPSHOT-${revision}`    |

Note that everything after `1.0.0` is considered a prerelease and will not get extra semantic tags. 
