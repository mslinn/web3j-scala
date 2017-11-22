# Scaladoc for SBT MultiProjects

[![Build Status](https://travis-ci.org/mslinn/scaladoc.svg?branch=master)](https://travis-ci.org/mslinn/scaladoc)
[![GitHub version](https://badge.fury.io/gh/mslinn%2Fscaladoc.svg)](https://badge.fury.io/gh/mslinn%2Fscaladoc)
[![Contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/dwyl/esta/issues)

## Publishing
1. Update the version string in the project's `build.sbt` and in this `README.md` before attempting to publish to Bintray.
2. Commit changes with a descriptive comment:
   ```
   $ git add -a && git commit -m "Comment here"
   ```
3. Tell the `sbt-git` SBT plugin where the `.git` directory is:
   ```
   export GIT_DIR="$(pwd)/.git"
   ```
4. Publish a new version of this library, including committing changes and updating the Scaladoc with this command:
   ```
   $ sbt publishAndTag
   ```

### Updating Scaladoc
The documentation for this project is generated separately for each subprojects.
For usage, simply type:
```
$ bin/doc
Publish 0.2.1
Usage: bin/doc [options]

  -a, --autoCheckIn <value>
                           Stop program if any files need to be committed or pushed
  -c, --copyright <value>  Scaladoc footer
  -d, --deleteAfterUse <value>
                           remove the GhPages temporary directory when the program ends
  -n, --gitHubName <value>
                           Github ID for project
  -o, --overWriteIndex <value>
                           Do not preserve any pre-existing index.html in the Scaladoc root
  -r, --dryRun <value>     Show the commands that would be run
  -s, --subProjectNames <value>
                           Comma-delimited names of subprojects to generate Scaladoc for
```

## Sponsor
<img src='https://www.micronauticsresearch.com/images/robotCircle400shadow.png' align='right' width='15%'>

This project is sponsored by [Micronautics Research Corporation](http://www.micronauticsresearch.com/),
the company that delivers online Scala training via [ScalaCourses.com](http://www.ScalaCourses.com).
You can learn Scala by taking the [Introduction to Scala](http://www.ScalaCourses.com/showCourse/40),
and [Intermediate Scala](http://www.ScalaCourses.com/showCourse/45) courses.

Micronautics Research also offers Ethereum and Scala consulting.
Please [contact us](mailto:sales@micronauticsresearch.com) to discuss your organization&rsquo;s needs.

## License
This software is published under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).
