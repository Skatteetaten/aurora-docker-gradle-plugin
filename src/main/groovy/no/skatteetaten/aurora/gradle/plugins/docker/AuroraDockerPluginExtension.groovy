package no.skatteetaten.aurora.gradle.plugins.docker

class AuroraDockerPluginExtension {
  String imageName
  String registry
  String workingDir
  String buildStrategy = "docker"
  Map<String, String> buildArgs = [:]
}
