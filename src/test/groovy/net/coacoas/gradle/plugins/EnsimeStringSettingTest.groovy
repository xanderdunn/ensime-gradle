package net.coacoas.gradle.plugins

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.core.Is
import org.junit.Assert
import org.junit.Test

import static org.hamcrest.CoreMatchers.both
import static org.hamcrest.CoreMatchers.containsString
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

public class EnsimeSettingTest {
	
	@Test
	public void testWindowsBackslashesAreEscaped() {
		def setting = new EnsimeListSetting()
        setting.keyword = "bob"
        setting.values = ["c:\\a.jar", "c:\\b.jar" ]
        assertThat(setting.toSExp().toString(), both(containsString("c:\\\\a.jar")).and(containsString("c:\\\\b.jar")))
	}

}
