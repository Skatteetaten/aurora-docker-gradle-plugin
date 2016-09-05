package ske.aurora.gradle.plugins.docker

class DockerCommands {

  public static String createBuildCommand(String imageTag, Map<String, String> buildArgs) {

    String buildArgsString = createBuildArgsString(buildArgs)
    "docker build $buildArgsString --rm -t $imageTag ."
  }

  protected static String createBuildArgsString(Map<String, String> buildArgs) {

    buildArgs.collect { k, v -> "--build-arg $k=$v" }.join(' ').trim()
  }
}
