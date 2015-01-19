# Gradle ENSIME Plugin

## Purposes

The gradle-ensime plugin creates .ensime project files for the ENhanced Scala Integration Mode for Emacs, written by Aemon Cannon (https://github.com/ensime/ensime-server). 

## Usage

To use the plugin, make sure that the jar file is loaded in the buildscript classpath and the plugin is applied: 

    buildscript {
      repositories { mavenCentral() }

      dependencies {
        classpath group: 'net.coacoas.gradle', name: 'gradle-ensime', version: '0.1.10'
      }
    }
    apply plugin: 'ensime'

The plugin adds the 'ensime' task to the project to create a .ensime file in the project directory.  

    ./gradlew ensime

Each time the task is executed, the .ensime file will be regenerated.

## Next Steps

Unknown.  Let me know if you are interested in anything else!
