package net.coacoas.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

/**
 * The Ensime plugin creates an ensime task that allows the build script
 * to create a .ensime project file that can be used for building 
 * Scala projects in emacs, Sublime Text 2, or jEdit. 
 *
 * &copy; Bill Carlson 2012
 */
class Ensime implements Plugin<Project> {
	public static final String TASK_NAME="ensime"
	public Project project
	private Logger logger

	List<String> getClasspath(String configurationName) {
		logger.debug("Getting classpath from configuration ${configurationName}")
		project.configurations.each {
			logger.debug("Found configuration: ${it}")
		}
		return project.configurations.getByName(configurationName).collect{
			logger.debug(it.absolutePath)
			it.absolutePath
		}
	}

	EnsimeSetting classpathSetting(String setting, String config) {
		new EnsimeListSetting(keyword: setting, values: getClasspath(config))
	}

    List<String> getProjectDependencies() {
        project.configurations.testRuntime.getAllDependencies().findAll { it instanceof ProjectDependency }.dependencyProject.collect { it.name }
    }

	List<String> getSourceSets() {
		List<String> sets = project.sourceSets?.main?.java?.srcDirs.collect{it.absolutePath} +
		project.sourceSets?.main?.resources?.srcDirs.collect{it.absolutePath} +
		project.sourceSets?.test?.resources?.srcDirs.collect{it.absolutePath} +
		project.sourceSets?.test?.java?.srcDirs.collect{it.absolutePath}

		if (project.sourceSets.main.hasProperty('scala')) {
			sets += project.sourceSets?.main?.scala?.srcDirs.collect{it.absolutePath} +
			project.sourceSets?.test?.scala?.srcDirs.collect{it.absolutePath}
		}

		sets
	}

	String settings() {
		Map<String, String> props = [
			'module-name': 'name',
			'version': 'version',
			'name': 'name',
			'package': 'groupId',
			'target': 'buildDir',
			'root-dir': 'rootDir',
		]
		List<EnsimeSetting> settings = props.collect {
			new EnsimeStringSetting(keyword: it.key, value: project.properties.get(it.value))
		} +
		classpathSetting('compile-deps', 'compile') +
		classpathSetting('runtime-deps', 'runtime') +
		classpathSetting('test-deps', 'testRuntime') +
		new EnsimeListSetting(keyword: 'depends-on-modules', values: getProjectDependencies()) +
		new EnsimeListSetting(keyword: 'source-roots', values: getSourceSets())
		return settings.collect{it.toSExp()}.join("\n")
	}

	void createEnsimeSettings(EnsimeModel config, File outputFile) {
		outputFile.parentFile.mkdirs()
		String data = "(:subprojects\n((" +
				settings() +
				")))"
		logger.info(data)
		outputFile.write(data)
	}

	String getEnsimeTargetFile(File outputFile) {
		String fileName = (project.extensions.ensime.target ?: project.projectDir.absolutePath) + "/.ensime"
		logger.debug("Writing ensime configuration to ${fileName}")
		return fileName
	}


	@Override
	public void apply(Project project) {
		this.project = project
		logger = Logging.getLogger(getClass())
		project.extensions.create(TASK_NAME, EnsimeModel)
		File outputFile = project.file(getEnsimeTargetFile())
		Task task = project.task(TASK_NAME) {
			inputs.file project.buildFile
			outputs.file outputFile
		}
		task.doFirst {
			createEnsimeSettings(project.extensions.ensime, outputFile)
		}
	}
}

class EnsimeModel {
	// Allow to override final file location
	String target
}