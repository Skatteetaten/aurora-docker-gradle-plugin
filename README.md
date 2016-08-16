# Aurora Gradle Docker Plugin

Aurora Gradle Docker Plugin er et gradle plugin som gjør det lettere å bygge docker images via gradle.

Pluginet bruker docker CLI for å utføre bygg.

Dersom Aurora Gradle Plugin også brukes, vil versjon og evt. revision (git commit hash) settes automatisk. Disse 
brukes av pluginet for å generere tags.


## Kom i gang

Legg følgende i toppen av din `build.gradle` fil

    buildscript {
    
        ...
    
        dependencies {
            'ske.aurora.gradle.plugins:aurora-docker-plugin:1.0.0'
        }
    }
    
    apply plugin: 'ske.plugins.aurora-docker'

    auroradocker {
      imageName = 'tilhørighet/image'
      registry = "docker.skead.no:5000" // Registryet du vil pushe til. Eksempelet er ikke nødvendigvis riktig.
      workingDir = "$buildDir/docker" // Mappen Dockefile ligger
    }



## Funksjonalitet

### Oppretter tasker for å bygge, tagge og pushe image 

Følgende tasker blir opprettet

 * buildImage - Bygger imaget med `docker build`
 * tagImage - Tagger det byggede image med tags som beskrevet under
 * pushImage - Pusher tags


## Generering av tags

Under tagging av imaget blir det generert flere tagger basert på projektets versjon dersom denne versjonen
følger `Semantic Versioning`. Følgende tagger blir pushet basert på versjon:

| Versjon         | Tagger                                                                       |
|-----------------|------------------------------------------------------------------------------|
| 1.0.0           | `1`, `1.0`, `1.0.0`, `latest`                                                |
| master-SNAPSHOT | `master-SNAPSHOT` og dersom `revision` er satt `master-SNAPSHOT-${revision}` |


## Release notes

### 1.0.0 (2016.08.16)

* Første release med feature set som dokumentert