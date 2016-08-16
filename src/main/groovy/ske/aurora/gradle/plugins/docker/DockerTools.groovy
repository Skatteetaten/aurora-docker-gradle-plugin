package ske.aurora.gradle.plugins.docker

class DockerTools {
  static String getImageIdFromOutput(String output) {
    String imageId = output.split(System.getProperty('line.separator')).reverse().findResult {
      def match = (it =~ /Successfully built (.*)$/)
      if (match.matches()) {
        return match[0][1]
      }
      return null
    }
    imageId
  }
}
