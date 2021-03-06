package net.coacoas.gradle.plugins

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.tooling.GradleConnector
import org.junit.Assert
import org.junit.Test
import org.junit.Ignore

import static org.hamcrest.core.Is.is
import static org.junit.Assert.assertThat

/**
 * Gradle ENSIME Plugin test
 *
 * &copy; Bill Carlson 2012
 */
@Ignore
public class EnsimeTest {
  @Test
  public void testTaskGetsAdded() {
    Project project = ProjectBuilder.builder().build()
    project.plugins.apply(Ensime)
    Assert.assertNotNull(project.tasks['ensime'])
  }

  @Test
  public void testScalaProjectWithEnsime() throws Exception {
    def build = GradleConnector
      .newConnector()
      .forProjectDirectory(new File("src/test/sample/scala"))
      .connect().newBuild()
    build.forTasks("clean", "ensime").run()

    def ensime = new File("src/test/sample/scala/build/ensime_file")
    assertThat(
      "an ensime file should be created at ${ensime.absolutePath}",
      ensime.exists(),
      is(true)
    )
  }

  @Test
  public void testAndroidProjectWithEnsime() throws Exception {
    def build = GradleConnector
      .newConnector()
      .forProjectDirectory(new File("src/test/sample/android/ActionBarCompat-Basic"))
      .connect().newBuild()
    build.forTasks("clean", "ensime").run()

    def ensime = new File("src/test/sample/android/ActionBarCompat-Basic/Application/.ensime")
    assertThat(
      "an ensime file should be created at ${ensime.absolutePath}",
      ensime.exists(),
      is(true)
    )
  }
}
