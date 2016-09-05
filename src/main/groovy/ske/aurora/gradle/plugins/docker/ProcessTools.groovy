package ske.aurora.gradle.plugins.docker

import org.apache.commons.io.output.TeeOutputStream
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ProcessTools {

  private static Logger logger = LoggerFactory.getLogger(ProcessTools)

  static class Result {
    Process process
    String output
  }

  public static Result runCommand(String cmd, File workingDir = null) {

    String[] env = []
    logger.info("Executing command [$cmd]")
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
