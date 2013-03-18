package net.coacoas.gradle.plugins;

import org.gradle.BuildResult
import org.gradle.GradleLauncher
import org.gradle.StartParameter
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * Gradle ENSIME Plugin test
 * 
 * &copy; Bill Carlson 2012
 */
public class EnsimeTest {
	
	@Test
	public void testTaskGetsAdded() { 
		Project project = ProjectBuilder.builder().build()
		project.apply plugin: 'ensime'
		Assert.assertNotNull(project.tasks['ensime'])
	}
	
//	@Test  - Fix this later, add some good functional tests. 
	// See https://github.com/eriwen/gradle-js-plugin/blob/master/src/test/groovy/com/eriwen/gradle/js/JsPluginFunctionalTest.groovy 
	// for good example.
	public void testProject() {
		Project project = ProjectBuilder.builder().build()
		project.apply plugin: 'ensime'
		project.tasks[Ensime.TASK_NAME].execute()
		
		File ensimeFile = new File(project.projectDir, ".ensime")
		assert(ensimeFile.exists())
		ensimeFile.deleteOnExit();

		List<String> data = ensimeFile.text.split("\n")
		assert(data.size > 0)
	}
}
