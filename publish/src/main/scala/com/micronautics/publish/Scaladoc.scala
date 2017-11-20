package com.micronautics.publish

import java.io.File
import java.nio.file.{Path, Paths}

/** @param classPath Specify where to find user class files (on Unix-based systems a colon-separated list of paths, on Windows-based systems, a semicolon-separate list  of
  *            paths). This does not override the built-in ("boot") search path.
  *            The default class path is the current directory. Setting the CLASSPATH variable or using the -classpath command-line option overrides that default, so
  *            if you want to include the current directory in the search path, you must include "." in the new settings.
  * @param deprecation Indicate whether source should be compiled with deprecation information; defaults to off (accepted values are: on, off, yes and no)
  * @param diagrams Create inheritance diagrams for classes, traits and packages.
  *                 Ignored if the `dot` program from the `graphviz` package is not installed
  * @param encoding Specify character encoding used by source files.
  *           The default value is platform-specific (Linux: "UTF8", Windows: "Cp1252").
  *           Executing the following code in the Scala interpreter will return the default value on your system:
  *           {{{new java.io.InputStreamReader(System.in).getEncoding}}}
  * @param externalDoc Comma-separated list of classpath_entry_path#doc_URL pairs describing external dependencies.
  * @param footer  A footer on every Scaladoc page, by default set to non-blank space. Can be overridden with a custom footer.
  * @param implicits Document members inherited by implicit conversions.
  * @param sourcePath Location(s) of source files.
  * @param sourceUrl A URL pattern used to link to the source file; the following variables are available:
  *                  €{TPL_NAME}, €{TPL_OWNER} and respectively €{FILE_PATH}.
  *                  For example, for `scala.collection.Seq`, the variables will be expanded to `Seq`, `scala.collection`
  *                  and respectively `scala/collection/Seq`.
  *                  To obtain a relative path for €{FILE_PATH} instead of an absolute path, use the `-sourcepath` setting.
  * @param outputDirectory directory to generate documentation into.
  * @param title overall title of the documentation, typically the name of the library being documented.
  * @param verbose Output messages about what the compiler is doing
  * @param version An optional version number to be appended to the title.
  */
case class Scaladoc(
  classPath: String = ".", // is this the best default value?
  deprecation: String = "on",
  diagrams: Boolean = true,
  encoding: String = "",
  externalDoc: String = "",
  footer: String = "&nbsp;",
  implicits: Boolean = true,
  outputDirectory: Path = Paths.get(""),
  sourcePath: String = ".", // todo this may not be a good default value
  sourceUrl: String = "",
  title: String = "",
  verbose: Boolean = false,
  version: String = ""
) {
  import Documenter._

  @inline protected def option(name: String, value: String): List[String] =
    if (value.nonEmpty) List(name, value) else Nil

  @inline protected def option(name: String, value: Boolean): List[String] =
    if (value) List(name) else Nil

  def run(cwd: File): String = {
    val dotIsInstalled = CommandLine.which("dot").nonEmpty
    if (diagrams && !dotIsInstalled)
      log.warn("Inheritance diagrams were requested, but the 'dot' program from the 'graphviz' package is not installed.")

    val options =
      option("-classpath",        classPath) :::
      option("-d",                outputDirectory.toString) :::
      option("-deprecation",      deprecation) :::
      option("-diagrams",         diagrams && dotIsInstalled) :::
      option("-doc-external-doc", externalDoc) :::
      option("-doc-footer",       footer) :::
      option("-doc-source-url",   sourceUrl) :::
      option("-doc-title",        title) :::
      option("-doc-version",      version) :::
      option("-implicits",        implicits) :::
      option("-sourcepath",       sourcePath) :::
      option("-verbose",          verbose)

    CommandLine.run(cwd, "scaladoc" :: options: _*)
  }
}
