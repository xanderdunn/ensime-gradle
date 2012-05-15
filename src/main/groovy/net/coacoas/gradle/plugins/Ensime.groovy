package net.coacoas.gradle.plugins

import org.gradle.api.Project;
import org.gradle.api.Plugin;
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging


interface EnsimeSetting {
	def toSExp()
}

class EnsimeListSetting implements EnsimeSetting {
	String keyword
	List<String> values

	@Override
	def toSExp() {
		return ":${keyword}\n(" +
		values.collect{"\"${it}\""}.join("\n") + ")"
	}
}

class EnsimeStringSetting implements EnsimeSetting {
	String keyword
	String value

	@Override
	def toSExp() {
		return ":${keyword}\n\"${value}\""
	}
}

class NestedEnsimeSetting implements EnsimeSetting {
	String keyword
	EnsimeSetting settings

	@Override def toSExp() {
		return "(:${keyword} ${settings.toSExpr()})"
	}
}

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
	
	List<String> getSourceSets() { 
		List<String> sets = project.sourceSets?.main.java?.srcDirs.collect{it.absolutePath} +
		  project.sourceSets?.main.scala?.srcDirs.collect{it.absolutePath} +
		  project.sourceSets?.main.resources.srcDirs.collect{it.absolutePath} +
		  project.sourceSets?.test.resources.srcDirs.collect{it.absolutePath} +
		  project.sourceSets?.test?.java?.srcDirs.collect{it.absolutePath} +
		  project.sourceSets?.test?.scala?.srcDirs.collect{it.absolutePath}
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
		new EnsimeListSetting(keyword: 'depends-on-modules', values: project.dependsOnProjects.collect { it.name }) +
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
		project.task(TASK_NAME) << {
			inputs.file project.buildFile
			def outputFile = project.file(getEnsimeTargetFile())
			outputs.file outputFile
			createEnsimeSettings(project.extensions.ensime, outputFile) 
		}
	}
}

class EnsimeModel {
	// Allow to override final file location 
	String target
}