package no.skatteetaten.aurora.gradle.plugins.docker

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class AuroraDockerPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {

    project.with {
      extensions.create('auroradocker', AuroraDockerPluginExtension)

      task('buildImage') << {

        String workDir = new File(project.auroradocker.workingDir)

        String imageTag = "$auroradocker.imageName:${version}"
        String buildCmd = DockerCommands.createBuildCommand(imageTag, auroradocker.buildArgs)

        ProcessTools.Result result = ProcessTools.runCommand(buildCmd, workDir)

        if (result.process.exitValue() != 0) {
          throw new GradleException("An error occurred while building the image. Inspect output for more details.")
        }
        String imageId = DockerTools.getImageIdFromOutput(result.output)
        if (!imageId) {
          throw new GradleException("Unable to find id of built image in output.")
        }
        ext.imageId = imageId
      }

      task('tagImage') {
        dependsOn buildImage
        doLast {
          String imageTag = "$auroradocker.imageName:${version}"
          String imageNameWithRegistry = "$auroradocker.registry/$auroradocker.imageName"
          // If the revision property has been set, we include a tag with it included.
          // The aurora plugin, by default, sets the revision property to the current git hash.
          Set<String> versions = DockerTagTools.createVersionTagsFromVersionAndRevision(project.version, project.revision)
          List<String> tags = versions.collect { "$imageNameWithRegistry:$it" }

          tags.each { tag ->
            ProcessTools.runCommand("docker tag $imageTag $tag")
          }
        }
      }

      task('pushImage') {
        dependsOn tagImage
        doLast {
          String imageNameWithRegistry = "$auroradocker.registry/$auroradocker.imageName"
          Set<String> versions = DockerTagTools.createVersionTagsFromVersionAndRevision(project.version, project.revision)
          List<String> tags = versions.collect { "$imageNameWithRegistry:$it" }
          tags.each { tag ->
            ProcessTools.runCommand("docker push $tag")
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