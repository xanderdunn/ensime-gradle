package net.coacoas.gradle.plugins;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;

public class EnsimeTest {
	@Test
	public void testProject() {
		Project project = ProjectBuilder.builder().build()
		project.with {
			apply plugin: 'scala'
			apply plugin: 'ensime'
			
			version = '0.1.0-SNAPSHOT'
			groupId = 'net.coacoas'
			
			dependencies {
			}
			tasks.getByName(Ensime.TASK_NAME).execute()
		}
		
		File ensimeFile = new File(project.projectDir, ".ensime")
		assert(ensimeFile.exists())
		ensimeFile.deleteOnExit();

		List<String> data = ensimeFile.text.split("\n")
		assert(data.size > 0)
//		assert(data.findAll {it.contains("runtime.jar")}.size == 2)
	}
}
