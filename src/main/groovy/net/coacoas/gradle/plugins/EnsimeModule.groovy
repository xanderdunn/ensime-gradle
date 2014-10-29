package net.coacoas.gradle.plugins

import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

/**
 * Converts a Project into a collection of settings representing a sub-module in Ensime.
 */
class EnsimeModule {

    private Project project

    EnsimeModule(Project project) {
        this.project = project
    }

    List<String> getClasspath(String configurationName) {
        project.logger.debug("Getting classpath from configuration ${configurationName}")
        return project.configurations.getByName(configurationName).collect {
            project.logger.debug(it.absolutePath)
            it.absolutePath
        }
    }

    List<String> getProjectDependencies() {
        project.configurations.testRuntime.getAllDependencies().findAll {
            it instanceof ProjectDependency
        }.dependencyProject.collect { it.name }
    }

    List<String> getSourceSets() {
        List<String> sets = project.sourceSets.main.java.srcDirs.collect { it.absolutePath } +
                project.sourceSets.main.resources.srcDirs.collect { it.absolutePath } +
                project.sourceSets.test.resources.srcDirs.collect { it.absolutePath } +
                project.sourceSets.test.java.srcDirs.collect { it.absolutePath }

        if (project.sourceSets.main.hasProperty('scala')) {
            sets += project.sourceSets.main.scala.srcDirs.collect { it.absolutePath } +
                    project.sourceSets.test.scala.srcDirs.collect { it.absolutePath }
        }

        sets
    }

    Map<String, Object> settings() {
        return [
                'name'              : project.name,
                'target'            : project.sourceSets.main.output.classesDir.absolutePath,
                'test-target'       : project.sourceSets.test.output.classesDir.absolutePath,
                'compile-deps'      : getClasspath('compile'),
                'runtime-deps'      : getClasspath('runtime'),
                'test-deps'         : getClasspath('testRuntime'),
                'depends-on-modules': getProjectDependencies(),
                'source-roots'      : getSourceSets()
        ]
    }

}
