package ske.aurora.gradle.plugins.docker

import spock.lang.Specification

class DockerTagToolsTest extends Specification {

  def "Creates tags from version and revision"() {

    when:
      Set<String> tags = DockerTagTools.createVersionTagsFromVersionAndRevision(version, revision)

    then:
      tags.size() == expectedTags.size()
      tags.containsAll(expectedTags)

    where:
      version           | revision                                   | expectedTags
      "1.0.0"           | null                                       | ['1', '1.0', '1.0.0', 'latest']
      "1.0.0-rc.1"      | null                                       | ['1.0.0-rc.1']
      "1.0.0-GA"        | null                                       | ['1.0.0-GA']
      "master-SNAPSHOT" | null                                       | ['master-SNAPSHOT']
      "master-SNAPSHOT" | '833398dea27b7cabc1d6a9e188ad395d17ba9163' | ['master-SNAPSHOT', 'master-SNAPSHOT-833398dea27b7cabc1d6a9e188ad395d17ba9163']
  }
}
