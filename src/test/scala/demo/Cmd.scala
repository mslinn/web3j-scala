package demo

import java.io.File
import java.nio.file.{Files, Path, Paths}
import java.util.regex.Pattern
import org.slf4j.Logger
import scala.sys.process._

object Cmd {
  val logger: Logger = org.slf4j.LoggerFactory.getLogger("gitStats")

  protected lazy val os: String = sys.props("os.name").toLowerCase

  lazy val isWindows: Boolean = os.indexOf("win") >= 0
  lazy val isMac: Boolean     = os.toLowerCase.startsWith("mac os x")
  lazy val isLinux: Boolean   = os.toLowerCase.startsWith("linux")

  protected def which(program: String): Option[Path] = {
    val path = if (isWindows) sys.env("Path") else sys.env("PATH")
    path
      .split(Pattern.quote(File.pathSeparator))
      .map(Paths.get(_))
      .find(path => Files.exists(path.resolve(program)))
      .map(_.resolve(program))
  }

  @inline protected def whichOrThrow(program: String): Path =
    which(program) match {
      case None => throw new Exception(program + " not found on path")
      case Some(programPath) => programPath
    }
}

class Cmd(cwd: File = new File("."), showOutput: Boolean=true, verbose: Boolean=false) {
  import demo.Cmd._

  @inline def apply(cmd: String*): ProcessBuilder = {
    val command: List[String] = whichOrThrow(cmd(0)).toString :: cmd.tail.toList
    if (verbose) println(s"[${ cwd.getAbsolutePath }] " + command.mkString(" "))
    Process(command=command, cwd=cwd)
  }

  @inline def getOutputFrom(cmd: String*): String =
    try {
      val result = apply(cmd:_*).!!.trim
      if (showOutput) println(result)
      result
    } catch {
      case e: Exception =>
        Console.err.println(e.getMessage)
        if (e.getCause.toString.nonEmpty) Console.err.println(e.getCause)
        Console.err.println(e.getStackTrace.mkString("\n"))
        sys.exit(-1)
    }
}
