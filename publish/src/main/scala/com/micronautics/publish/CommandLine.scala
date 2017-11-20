package com.micronautics.publish

import java.io.File
import java.nio.file.{Path, Paths}
import java.util.regex.Pattern
import org.slf4j.Logger
import scala.util.Properties.isWin

object CommandLine {
  protected def resolve(path: Path, program: String): Option[Path] = {
    val x = path.resolve(program)
    if (x.toFile.exists) Some(x) else None
  }

  def which(program: String): Option[Path] = {
    val _path = sys.env.getOrElse("PATH", sys.env.getOrElse("Path", sys.env("path")))

    val paths =
      _path
        .split(Pattern.quote(File.pathSeparator))
        .map(Paths.get(_))

    val result: Option[Path] = paths.collectFirst {
      case path if resolve(path, program).exists(_.toFile.exists) => resolve(path, program)

      case path if isWin && resolve(path, s"$program.cmd").exists(_.toFile.exists) => resolve(path, s"$program.cmd")

      case path if isWin && resolve(path, s"$program.bat").exists(_.toFile.exists) => resolve(path, s"$program.bat")
    }.flatten
    result
  }

  @inline protected def whichOrThrow(program: String): Path =
    which(program) match {
      case None => throw new Exception(s"Error: $program not found on ${ if (isWin) "Path" else "PATH" }")
      case Some(programPath) => programPath
    }

  @inline def run(cmd: String)
         (implicit log: Logger): String =
    run(new File(sys.props("user.dir")), cmd)

  @inline def run(cmd: String*)
         (implicit log: Logger): String =
    run(new File(sys.props("user.dir")), cmd: _*)

  def run(cwd: File = new File(sys.props("user.dir")), cmd: String)
         (implicit log: Logger): String = {
    import scala.sys.process._

    val tokens: Array[String] = cmd.split(" ")
    val command: List[String] = whichOrThrow(tokens(0)).toString :: tokens.tail.toList
    log.debug(s"Running $cmd from '$cwd'") //, which translates to ${ command.mkString("\"", "\", \"", "\"") }")
    Process(command=command, cwd=cwd).!!.trim
  }

  def run(cwd: File, cmd: String*)
         (implicit log: Logger): String = {
    import scala.sys.process._

    val command: List[String] = whichOrThrow(cmd(0)).toString :: cmd.tail.toList
    log.debug(s"Running ${ cmd.mkString(" ") } from '$cwd'")
    Process(command=command, cwd=cwd).!!.trim
  }

  def run(cwd: Path, cmd: String)
         (implicit log: Logger): String =
    run(cwd.toFile, cmd)

  def run(cwd: Path, cmd: String*)
         (implicit log: Logger): String = run(cwd.toFile, cmd: _*)
}

