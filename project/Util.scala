package com.micronautics.sbt

object Util {
  import java.io.IOException
  import java.nio.file.{Files, Paths, Path, SimpleFileVisitor, FileVisitResult}
  import java.nio.file.attribute.BasicFileAttributes
  import sbt.Logger

  /** Adapted from https://stackoverflow.com/a/45703150/553865 */
  def remove(root: Path, deleteRoot: Boolean = true)
            (implicit log: Logger): Unit = {
    log.debug(
      if (deleteRoot) s"Nuking $root"
      else s"Clearing files and directories under $root (${ root.toFile.listFiles.mkString(", ") })"
    )

    Files.walkFileTree(root, new SimpleFileVisitor[Path] {
      override def visitFile(file: Path, attributes: BasicFileAttributes): FileVisitResult = {
        Files.delete(file)
        FileVisitResult.CONTINUE
      }

      override def postVisitDirectory(dir: Path, exception: IOException): FileVisitResult = {
        if (deleteRoot) Files.delete(dir)
        FileVisitResult.CONTINUE
      }
    })
  }

  def removeUnder(string: String)
                 (implicit log: Logger): Unit = remove(Paths.get(string), deleteRoot=false)

  def removeAll(string: String)
               (implicit log: Logger): Unit = remove(Paths.get(string))

  def removeUnder(file: java.io.File)
                 (implicit log: Logger): Unit = remove(file.toPath, deleteRoot=false)

  def removeAll(file: java.io.File)
               (implicit log: Logger): Unit = remove(file.toPath)
}
