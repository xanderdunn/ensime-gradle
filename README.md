# Gradle ENSIME Plugin

[![Join the chat at https://gitter.im/ensime/ensime-gradle](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/ensime/ensime-gradle?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://travis-ci.org/ensime/ensime-gradle.svg)](https://travis-ci.org/ensime/ensime-gradle)

## Purposes

The gradle-ensime plugin creates [.ensime project files](https://github.com/ensime/ensime-server/wiki/Example-Configuration-File) for the ENhanced Scala Integration Mode for Emacs ([ENSIME](https://github.com/ensime)), written by Aemon Cannon.

## Usage

For it to work the build.gradle file either needs to have the [`scala`](http://www.gradle.org/docs/current/userguide/scala_plugin.html) plugin or the [`gradle-android-scala-plugin`](https://github.com/saturday06/gradle-android-scala-plugin) to be configured (the later is WIP).

A working android example can be found [here](https://github.com/rolandtritsch/scala-android-ui-samples).

To use the plugin, make sure that the jar file is loaded in the buildscript classpath and the plugin is applied:

    buildscript {
      repositories { mavenCentral() }

      dependencies {
        classpath group: 'net.coacoas.gradle', name: 'gradle-ensime', version: '0.1.10'
      }
    }
    apply plugin: 'ensime'

You will also need to specify the version of Scala that you're using:

    ext {
        scalaLibraryVersion = "2.11.7"
    }

The plugin adds the 'ensime' task to the project to create a .ensime file in the project directory.

    ./gradlew ensime

Each time the task is executed, the .ensime file will be regenerated.

To see the plugin in action you can also clone this repo and then run `gradle build` to build the plugin and then `cd src/test/sample/scala` and run `gradle clean build ensime`.

## Next Steps

At this point, I think it would be good to clean this up and get it added to the Gradle Plugin Repository.  If an issue exists keeping this from a 1.0 version, let me know so it can get fixed. 

## Updates

### 0.11

*  Thanks to rolandtritsch for this update!
*  Fixes Issue #6: Generates .ensime files for Android development
*  Back to supporting Java 6
