package no.skatteetaten.aurora.gradle.plugins.docker

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class AuroraDockerPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {

    project.with {
      extensions.create('auroradocker', AuroraDockerPluginExtension)

      task('buildImage') {
        doLast {
          String workDir = new File(project.auroradocker.workingDir)
          String imageTag = "$auroradocker.imageName:${version}"
          String buildCmd
          ProcessTools.Result result
          if (auroradocker.buildStrategy == "buildah") {
            def imageUUID = UUID.randomUUID().toString()
            buildCmd = BuildCommands.createBuildahBuildCommand(imageUUID, auroradocker.buildArgs)
            result = ProcessTools.runCommand(buildCmd, null, new File(workDir))
            if (result.process.exitValue() != 0) {
              throw new GradleException("An error occurred while building the image. Inspect output for more details.")
            }
            ext.imageId = imageUUID;
          } else { // docker
            buildCmd = BuildCommands.createDockerBuildCommand(imageTag, auroradocker.buildArgs)
            result = ProcessTools.runCommand(buildCmd, workDir)
            if (result.process.exitValue() != 0) {
              throw new GradleException("An error occurred while building the image. Inspect output for more details.")
            }
            String imageId = DockerTools.getImageIdFromOutput(result.output)
            if (!imageId) {
              throw new GradleException("Unable to find id of built image in output.")
            }
            ext.imageId = imageId
          }
        }
      }

      task('tagImage') {
        dependsOn buildImage
        doLast {
          String imageNameWithRegistry = "$auroradocker.registry/$auroradocker.imageName"
          // If the revision property has been set, we include a tag with it included.
          // The aurora plugin, by default, sets the revision property to the current git hash.
          Set<String> versions = DockerTagTools.
              createVersionTagsFromVersionAndRevision(project.version, project.revision)
          List<String> tags = versions.collect { "$imageNameWithRegistry:$it" }
          String tagCmd
          if (auroradocker.buildStrategy == "buildah") {
            String imageUUID = buildImage.imageId
            tagCmd = BuildCommands.createBuildahTagCommand()
            tags.each { tag ->
              ProcessTools.runCommand("$tagCmd $imageUUID $tag")
            }
          } else { // docker
            String imageTag = "$auroradocker.imageName:${version}"
            tagCmd = BuildCommands.createDockerTagCommand()
            tags.each { tag ->
              ProcessTools.runCommand("$tagCmd $imageTag $tag")
            }
          }
        }
      }

      task('pushImage') {
        dependsOn tagImage
        doLast {
          String imageNameWithRegistry = "$auroradocker.registry/$auroradocker.imageName"
          Set<String> versions = DockerTagTools.
              createVersionTagsFromVersionAndRevision(project.version, project.revision)
          List<String> tags = versions.collect { "$imageNameWithRegistry:$it" }
          String pushCmd
          if (auroradocker.buildStrategy == "buildah") {
            pushCmd = BuildCommands.createBuildahPushCommand()
          } else { // docker
            pushCmd = BuildCommands.createDockerPushCommand()
          }
          tags.each { tag ->
            ProcessTools.runCommand(pushCmd + " $tag", null, null)
          }
        }
      }

      if (project.hasProperty('build')) {
        build.dependsOn tagImage
        pushImage.mustRunAfter build
      }
    }
  }
}
