package ske.aurora.gradle.plugins.docker

import spock.lang.Specification

class DockerCommandsTest extends Specification {

  def "Test docker command"() {

    when:
      String cmd = DockerCommands.createBuildCommand(tag, buildArgs)
    then:
      cmd == expectedCmd

    where:
      tag      | buildArgs                         | expectedCmd
      'latest' | [:]                               | 'docker build  --rm -t latest .'
      'latest' | [VERSION: '2.1.4']                | 'docker build --build-arg VERSION=2.1.4 --rm -t latest .'
      'latest' | [VERSION: '2.1.4', HASH: 'ABC34'] | 'docker build --build-arg VERSION=2.1.4 --build-arg HASH=ABC34 --rm -t latest .'
  }
}
