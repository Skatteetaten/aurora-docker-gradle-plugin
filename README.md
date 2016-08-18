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

 * buildImage - Bygger imaget med `docker build`. Etter bygget er ferfig er id til det byggede imaget tilgjengelig via `imageId` propertyen på tasken. 
 * tagImage - Tagger det byggede image med tags som beskrevet under
 * pushImage - Pusher tags


## Generering av tags

Under tagging av imaget blir det generert flere tagger basert på prosjektets versjon dersom denne versjonen
følger `Semantic Versioning`. Følgende tagger blir pushet basert på versjon:

| Versjon         | Tagger                                                                       |
|-----------------|------------------------------------------------------------------------------|
| 1.0.0           | `1`, `1.0`, `1.0.0`, `latest`                                                |
| 1.0.0-rc.1      | `1.0.0-rc.1`                                                                 |
| 1.0.0-GA        | `1.0.0-GA`                                                                   |
| master-SNAPSHOT | `master-SNAPSHOT` og dersom `revision` er satt `master-SNAPSHOT-${revision}` |

Merk at alt etter `1.0.0` betraktes som prerelease version, og versjoner med prerelease version satt kun får sin versjon som tag. Dette
er for å unngå at f.eks. `1.2.0-rc.1` skal overskrive `1` taggen dersom f.eks. `1.1.0` allerede er releaset.

## Release notes

### 1.0.2 (2016.08.18)

* Ingen reelle endringer. Bare første release fra Jenkins.


### 1.0.1 (2016.08.16)

 * Fikset et problem med generering av tagger for versjoner med prerelease version satt


### 1.0.0 (2016.08.16)

 * Første release med feature set som dokumentert