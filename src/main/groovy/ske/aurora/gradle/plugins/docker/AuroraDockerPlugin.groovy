package ske.aurora.gradle.plugins.docker

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

import com.github.zafarkhaja.semver.Version

class AuroraDockerPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {

    project.with {
      extensions.create('auroradocker', AuroraDockerPluginExtension)

      task('buildImage') << {
        String imageTag = "$auroradocker.imageName:${version}"
        String workDir = new File(project.auroradocker.workingDir)
        ProcessTools.Result result = ProcessTools.runCommand("docker build --rm -t $imageTag .", workDir)
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
            ProcessTools.runCommand("docker tag -f $imageTag $tag")
          }
        }
      }

      task('pushImage') {
        dependsOn tagImage
        doLast {
          List<String> tags = createImageTags(project)
          tags.each { tag ->
            ProcessTools.runCommand("docker push $tag")
          }
        }
      }

      build.dependsOn tagImage
      pushImage.mustRunAfter build
    }
  }
}
