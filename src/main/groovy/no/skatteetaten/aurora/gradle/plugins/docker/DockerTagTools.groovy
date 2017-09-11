package no.skatteetaten.aurora.gradle.plugins.docker

import com.github.zafarkhaja.semver.Version

class DockerTagTools {
  static Set<String> createVersionTagsFromVersionAndRevision(String version, String revision = null) {
    Optional<Version> versionOption = getSemanticVersionFromVersionString(version)

    versionOption.map { v ->
      if (v.preReleaseVersion) {
        [version]
      } else {
        [
            'latest',
            "${v.majorVersion}" as String,
            "${v.majorVersion}.${v.minorVersion}" as String,
            "${v.majorVersion}.${v.minorVersion}.${v.patchVersion}" as String
        ]
      }
    }.orElseGet {
      def versions = [version]
      if (revision) {
        versions.add("$version-$revision" as String)
      }
      versions as Set
    }
  }

  static Optional<Version> getSemanticVersionFromVersionString(String versionString) {
    try {
      return Optional.of(Version.valueOf(versionString))
    } catch (Exception e) {
      return Optional.empty()
    }
  }
}
