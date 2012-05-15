package net.coacoas.gradle.plugins

 /**
  * This file contains the generic beans for translating from Gradle/ENSIME settigns
  * to S-Expressions for storing in the .ensime file. 
  * 
  * &copy; Bill Carlson 2012
  */
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
