package com.micronautics.publish

import java.io.File
import java.io.IOException
import java.nio.file.{Files, Paths, Path, SimpleFileVisitor, FileVisitResult}
import java.nio.file.attribute.BasicFileAttributes
import org.slf4j.Logger

object Nuke {
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

  def removeAll(string: String)
               (implicit log: Logger): Unit = remove(Paths.get(string))

  def removeAll(file: File)
               (implicit log: Logger): Unit = remove(file.toPath)

  def removeAll(path: Path)
               (implicit log: Logger): Unit = remove(path)

  def removeUnder(string: String)
                 (implicit log: Logger): Unit = remove(Paths.get(string), deleteRoot=false)

  def removeUnder(file: File)
                 (implicit log: Logger): Unit = remove(file.toPath, deleteRoot=false)

  def removeUnder(path: Path)
                 (implicit log: Logger): Unit = remove(path, deleteRoot=false)
}
