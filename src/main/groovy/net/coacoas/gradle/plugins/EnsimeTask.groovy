package net.coacoas.gradle.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.TaskAction

class EnsimeTask extends DefaultTask {

    String scalaLibraryVersion() {
        if (project.hasProperty('scalaLibraryVersion')) {
            return project.property('scalaLibraryVersion')
        } else {
            String version = project.extensions.ensime.scalaLibraryVersion
            if (version == null) throw new GradleException('scalaLibraryVersion not specified')
            return version
        }
    }

    @TaskAction
    void writeFile(){
        String fileName = (project.extensions.ensime.target ?: project.projectDir.absolutePath) + "/.ensime"
        project.logger.debug("Writing ensime configuration to ${fileName}")
        File outputFile = new File(fileName)

        outputFile.parentFile.mkdirs()

        Map<String, Object> properties = [
                'root-dir': project.rootDir.absolutePath,
                'cache-dir': new File(project.buildDir, 'ensime_cache').absolutePath,
                'name': project.name,
                'scala-version': scalaLibraryVersion()
        ]

        def modules = project.allprojects
                .grep { it.plugins.hasPlugin(JavaPlugin) }
                .collect { new EnsimeModule(it).settings() }

        properties.put("subprojects", modules)

        def data = SExp.format(properties)
        outputFile.write(data)
    }

}
