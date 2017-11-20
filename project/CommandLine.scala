package com.micronautics.sbt

import java.io.File
import java.nio.file.{Files, Path, Paths}
import java.util.regex.Pattern
import sbt.{File, Logger, file}

object CommandLine {
  protected lazy val os: String = sys.props("os.name").toLowerCase

  lazy val isWindows: Boolean = os.indexOf("win") >= 0

  protected def resolve(path: Path, program: String): Option[Path] = {
    val x = path.resolve(program)
    if (x.toFile.exists) Some(x) else None
  }

  protected def which(program: String): Option[Path] = {
    val _path = sys.env.getOrElse("PATH", sys.env.getOrElse("Path", sys.env("path")))

    val paths =
      _path
        .split(Pattern.quote(File.pathSeparator))
        .map(Paths.get(_))

    val result: Option[Path] = paths.collectFirst {
      case path if resolve(path, program).exists(_.toFile.exists) => resolve(path, program)

      case path if isWindows && resolve(path, s"$program.cmd").exists(_.toFile.exists) => resolve(path, s"$program.cmd")

      case path if isWindows && resolve(path, s"$program.bat").exists(_.toFile.exists) => resolve(path, s"$program.bat")
    }.flatten
    result
  }

  @inline protected def whichOrThrow(program: String): Path =
    which(program) match {
      case None => throw new Exception(s"Error: $program not found on ${ if (isWindows) "Path" else "PATH" }")
      case Some(programPath) => programPath
    }

  def run(cmd: String, cwd: File = file(sys.props("user.dir")))
         (implicit log: Logger): String = {
    import scala.sys.process._

    val tokens: Array[String] = cmd.split(" ")
    val command: List[String] = whichOrThrow(tokens(0)).toString :: tokens.tail.toList
    log.debug(s"Running $cmd from '$cwd'") //, which translates to ${ command.mkString("\"", "\", \"", "\"") }")
    Process(command=command, cwd=cwd).!!.trim
  }

  @inline def run(cmd: String)
         (implicit log: Logger): String =
    run(cmd, file(sys.props("user.dir")))
}

