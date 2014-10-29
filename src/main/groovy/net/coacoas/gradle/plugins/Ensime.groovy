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

class EnsimeModel {
	// Allow to override final file location
	String target
    /**
     * attempts to read a root project property first, but allows a user to define this separately.
     * this is the full scala compiler version e.g. 2.11.2
     */
    String scalaLibraryVersion
}