package no.skatteetaten.aurora.gradle.plugins.docker

class BuildCommands {

  public static String createDockerBuildCommand(String imageTag, Map<String, String> buildArgs) {
    String buildArgsString = createBuildArgsString(buildArgs)
    "docker build $buildArgsString --rm -t $imageTag ."
  }

  public static String createBuildahBuildCommand(String imageUUID, Map<String, String> buildArgs) {
    String buildContext = "./Dockerfile"
    String buildArgsString = createBuildArgsString(buildArgs)
    "buildah --storage-driver vfs bud --quiet --isolation chroot $buildArgsString -t $imageUUID -f $buildContext ."
  }

  public static String createDockerTagCommand() {
    "docker tag"
  }

  public static String createBuildahTagCommand() {
    "buildah --storage-driver vfs tag"
  }

  public static String createDockerPushCommand() {
    "docker push"
  }

  public static String createBuildahPushCommand() {
    "buildah --storage-driver vfs push --quiet"
  }

  protected static String createBuildArgsString(Map<String, String> buildArgs) {

    buildArgs.collect { k, v -> "--build-arg $k=$v" }.join(' ').trim()
  }
}
