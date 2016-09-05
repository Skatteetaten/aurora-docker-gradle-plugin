package ske.aurora.gradle.plugins.docker

class AuroraDockerPluginExtension {
  String imageName
  String registry
  String workingDir
  Map<String, String> buildArgs = [:]
}