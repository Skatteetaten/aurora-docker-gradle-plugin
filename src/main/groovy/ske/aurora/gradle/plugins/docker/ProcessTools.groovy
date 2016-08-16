package ske.aurora.gradle.plugins.docker

import org.apache.commons.io.output.TeeOutputStream

class ProcessTools {

  static class Result {
    Process process
    String output
  }

  public static Result runCommand(String cmd, File workingDir = null) {

    String[] env = []
    Process p = Runtime.getRuntime().exec(cmd, env, workingDir)

    def output = new ByteArrayOutputStream()
    def processOutput = new TeeOutputStream(System.out, output)
    p.waitForProcessOutput(processOutput, processOutput)
    new Result(process: p, output: output.toString())
  }

  public static Result runCommand(String cmd, String workingDir) {

    runCommand(cmd, new File(workingDir))
  }
}
