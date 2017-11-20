package com.micronautics.publish

import org.slf4j.{Logger, LoggerFactory}
import org.slf4j.event.Level

object LogMessage {
  implicit val empty: LogMessage = LogMessage(Level.INFO, "")(LoggerFactory.getLogger(""))
}

case class LogMessage(level: Level, message: String)
                     (implicit logger: Logger) {
  lazy val isEmpty: Boolean = message.isEmpty

  /** Conditionally displays the message, according to SLF4J's logging precedence rules */
  def display(): Unit = level match {
    case Level.DEBUG => logger.debug(message)
    case Level.ERROR => logger.error(message)
    case Level.INFO  => logger.info(message)
    case Level.TRACE => logger.trace(message)
    case Level.WARN  => logger.warn(message)
  }

  lazy val nonEmpty: Boolean = message.nonEmpty
}

class CommandLine(config: Config = Config.default) {
  import java.io.File
  import java.nio.file.{Path, Paths}
  import java.util.regex.Pattern
  import scala.sys.process._
  import scala.util.Properties.isWin

  @inline def run(cmd: String)
                 (implicit logMessage: LogMessage, log: Logger): String =
    run(new File(sys.props("user.dir")), cmd)


  @inline def run(cmd: String*)
                 (implicit logMessage: LogMessage, log: Logger): String =
    run(new File(sys.props("user.dir")), cmd: _*)


  def run(cwd: File = new File(sys.props("user.dir")), cmd: String)
         (implicit logMessage: LogMessage, log: Logger): String = {
    val tokens: Array[String] = cmd.split(" ")
    val command: List[String] = whichOrThrow(tokens(0)).toString :: tokens.tail.toList
    if (logMessage.nonEmpty) logMessage.display()
    log.debug(s"# Running $cmd from '$cwd'") //, which translates to ${ command.mkString("\"", "\", \"", "\"") }")
    if (config.dryRun) {
      log.debug(command.mkString(" "))
      ""
    } else
      Process(command=command, cwd=cwd).!!.trim
  }


  def run(cwd: File, cmd: String*)
         (implicit logMessage: LogMessage, log: Logger): String = {
    val command: List[String] = whichOrThrow(cmd(0)).toString :: cmd.tail.toList
    if (logMessage.nonEmpty) logMessage.display()
    log.debug(s"Running ${ cmd.mkString(" ") } from '$cwd'")
    if (config.dryRun) {
      log.debug(command.mkString(" "))
      ""
    } else
      Process(command=command, cwd=cwd).!!.trim
  }


  def run(cwd: Path, cmd: String)
         (implicit logMessage: LogMessage, log: Logger): String =
    run(cwd.toFile, cmd)


  def run(cwd: Path, cmd: String*)
         (implicit logMessage: LogMessage, log: Logger): String =
    run(cwd.toFile, cmd: _*)


  def which(program: String): Option[Path] = {
    val pathEnv = sys.env.getOrElse("PATH", sys.env.getOrElse("Path", sys.env("path")))

    val paths =
      pathEnv
        .split(Pattern.quote(File.pathSeparator))
        .map(Paths.get(_))

    paths.collectFirst {
      case path if resolve(path, program).exists(_.toFile.exists) => resolve(path, program)

      case path if isWin && resolve(path, s"$program.cmd").exists(_.toFile.exists) => resolve(path, s"$program.cmd")

      case path if isWin && resolve(path, s"$program.bat").exists(_.toFile.exists) => resolve(path, s"$program.bat")
    }.flatten
  }


  @inline protected def resolve(path: Path, program: String): Option[Path] = {
    val x = path.resolve(program)
    if (x.toFile.exists) Some(x) else None
  }

  @inline protected def whichOrThrow(program: String): Path =
    which(program) match {
      case None =>
        Console.err.println(s"Error: $program not found on ${ if (isWin) "Path" else "PATH" }")
        System.exit(0)
        Paths.get("Bogus, dude, just making the compiler happy.")

      case Some(programPath) => programPath
    }
}
