package no.skatteetaten.aurora.gradle.plugins.docker

import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonException

class BuildCommands {

  public static String createDockerBuildCommand(String imageTag, Map<String, String> buildArgs) {
    String buildArgsString = createBuildArgsString(buildArgs)
    "docker build $buildArgsString --rm -t $imageTag ."
  }

  public static String createBuildahBuildCommand(String imageName, Map<String, String> buildArgs) {
    String buildContext = "./Dockerfile"
    String buildArgsString = createBuildArgsString(buildArgs)
    "buildah --storage-driver vfs bud --quiet --isolation chroot $buildArgsString -t $imageName -f $buildContext ."
  }

  public static String createDockerLoginCommand(PushCredentials creds) {
    "docker login -u ${creds.username} -p ${creds.password} ${creds.serveraddress}"
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

  public static String createBuildahPushCommand(PushCredentials creds) {
    String credsOption = createBuildahCredsString(creds)
    "buildah --storage-driver vfs push --quiet $credsOption"
  }

  protected static String createBuildArgsString(Map<String, String> buildArgs) {
    buildArgs.collect { k, v -> "--build-arg $k=$v" }.join(' ').trim()
  }

  protected static String createBuildahCredsString(PushCredentials creds) {
    if (creds != null) {
      return "--creds=${creds.username}:${creds.password}"
    } else {
      return ""
    }
  }

  public static PushCredentials getPushCredentials (String pushregistry) {
    String secretPath = "/var/run/secrets/openshift.io/push/.dockercfg"
    def secretfile = new File(secretPath)

    if (secretfile.exists() && secretfile.canRead()) {
      def jsonSlurper = new JsonSlurper()
      def dockerconfig
      try {
        dockerconfig = jsonSlurper.parse(secretfile)
      } catch (JsonException je) {
        println "credentials not valid json"
        throw je
      }

      def foundcreds = dockerconfig.get(pushregistry)
      if (foundcreds != null) {
        def pushCredentials = new PushCredentials()

        pushCredentials.username = foundcreds.username
        pushCredentials.password = foundcreds.password
        pushCredentials.serveraddress = pushregistry

        return pushCredentials
      } else {
        println "found no credentials for $pushregistry"
      }
    } else {
      println "found no secret file"
      throw new FileNotFoundException("Could not find $secretPath")
    }

    return null

  }
}
