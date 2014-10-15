package net.coacoas.gradle.plugins

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.tooling.GradleConnector
import org.junit.Assert
import org.junit.Test

import static org.hamcrest.core.Is.is
import static org.junit.Assert.assertThat

/**
 * Gradle ENSIME Plugin test
 * 
 * &copy; Bill Carlson 2012
 */
public class EnsimeTest {
	
	@Test
	public void testTaskGetsAdded() { 
		Project project = ProjectBuilder.builder().build()
		project.plugins.apply(Ensime)
		Assert.assertNotNull(project.tasks['ensime'])
	}

    @Test
    public void testProjectWithEnsime() throws Exception {
        def build = GradleConnector.
                newConnector().
                forProjectDirectory(new File("src/test/sample")).
                connect().newBuild()
        build.forTasks('clean', 'ensime').run()

        def ensime = new File('src/test/sample/build/ensime/.ensime')
        assertThat('an ensime file should be created at ' + ensime.absolutePath, ensime.exists(), is(true))
    }
}
