# Scala Project Template

[![License](https://licensebuttons.net/p/zero/1.0/88x31.png)](https://creativecommons.org/share-your-work/public-domain/cc0/)
[![Build Status](https://travis-ci.org/mslinn/sbtTemplate.svg?branch=master)](https://travis-ci.org/mslinn/sbtTemplate)
[![GitHub version](https://badge.fury.io/gh/mslinn%2FsbtTemplate.svg)](https://badge.fury.io/gh/mslinn%2FsbtTemplate)

This is a handy starting point for Scala/Java console apps built with SBT.
Projects are built with Scala 2.12, which requires Java 8+.
The `scala_2_11` branch builds projects for Scala 2.11.

For more information about this project, and SBT, see the [SBT Project Setup lecture](https://scalacourses.com/student/showLecture/135) on [ScalaCourses.com](https://www.ScalaCourses.com).

## sbtTemplate Bash command

Copy this to a directory on the path (like `/usr/local/bin/`), and call it `sbtTemplate`:

```
#!/bin/bash

# Clones sbtTemplate and starts a new SBT project
# Optional argument specifies name of directory to place the new project into

DIR=sbtTemplate
if [ "$1" ]; then DIR="$1"; fi
git clone https://github.com/mslinn/sbtTemplate.git "$DIR"
cd "$DIR"
rm -rf .git
git init
echo "Please edit README.md, build.sbt and publish.sbt right away"
echo "After you create the remote repository, type this:"
echo "git branch -u origin/master"
echo "git add -A"
echo "git commit -m "Initial checkin""
echo "git push"
```

Make the bash script executable:

    $ chmod a+x /usr/local/bin/sbtTemplate

To create a new SBT project, run the script.

    $ sbtTemplate my-new-project

## Using GitHub?

### GitHub Pages
`sbtTemplate` sets up the GitHub pages branch for your new project.
Before you can use it, edit `build.sbt` and change this line so your GitHub user id and project name are substituted
for the placeholders `yourGithubId` and `my-new-project`:

    git.remoteRepo := "git@github.com:yourGithubId/my-new-project.git"

Now you can publish the Scaladoc for your project with this command:

    sbt ";doc ;ghpagesPushSite"

The Scaladoc will be available at a URL of the form:

    http://yourGithubId.github.io/my-new-project/latest/api/index.html

### Try Hub!
With `hub` and `sbtTemplate` you can create a new SBT project and a matching GitHub project with only two commands.
The setup documented below will supply your GitHub username and password,
and will only prompt your for your 2-factor-authentication (2FA) token each time
you run it if you set up your GitHub account to use 2FA.

#### Install Hub
Install Hub on Mac OS:

    $ brew install hub

Install Hub on Linux:

    $ sudo -H pip install hub

Put your GitHub login credentials in `~/.bash_profile` or `~/.profile`.
Also alias `hub` as `git` (`hub` also executes `git` commands):

    export GITHUB_USER=yourGithubUserName
    export GITHUB_PASSWORD=yourPassword
    alias git=hub

Reload `~/.bash_profile`

    $ source `~/.bash_profile`

... or reload `~/.profile`

    $ source `~/.profile`

#### Using sbtTemplate with Hub
Create a new SBT project and create a new GitHub project, which `hub` automatically adds as a `git` `remote`:

    $ sbtTemplate bigBadProject
    $ git create -d "Project description"
    two-factor authentication code: 881078
    Updating origin
    created repository: mslinn/bigBadProject

Now check in the new project:

    $ git add -A && git commit -m "Initial checkin" && git push -u origin master

## Running the Program
The `bin/run` Bash script assembles this project into a fat jar and runs it.
Sample usage, which runs the `Hello` entry point in `src/main/scala/Hello.scala`:

```
$ bin/run Hello
```

The `-j` option forces a rebuild of the fat jar. 
Use it after modifying the source code.

```
$ bin/run -j Hello
```
