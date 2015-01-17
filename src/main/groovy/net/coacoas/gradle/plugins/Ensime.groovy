package net.coacoas.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPlugin

/**
 * The Ensime plugin creates an ensime task that allows the build script
 * to create a .ensime project file that can be used for building
 * Scala projects in emacs, Sublime Text 2, or jEdit.
 *
 * &copy; Bill Carlson 2012
 */
class Ensime implements Plugin<Project> {
  public static final String TASK_NAME="ensime"

  @Override
  public void apply(Project project) {
    if (!project.plugins.hasPlugin(Ensime)) {
      project.extensions.create(TASK_NAME, EnsimeModel)
      project.tasks.create(TASK_NAME, EnsimeTask)
    }
  }
}

/**
 * Define all the extension for the plugin.
 */
class EnsimeModel {
  // e.g. "<absolutePath>/.ensime"
  public String targetFile = ""

  // can be >t< or >nil< (TODO - make this work)
  // public String useSbt = ""

  // allow to set the vars in the .ensime file
  // (https://github.com/ensime/ensime-server/wiki/Example-Configuration-File)
  // that cannot be set/configured through the project conf
  public String cacheDir = ""
  public String javaHome = ""
  public List<String> javaFlags = []
  public List<String> referenceSourceRoots = []
  public String scalaVersion = ""
  public List<String> compilerArgs = []
  // public formatingPrefs = [:]
}
