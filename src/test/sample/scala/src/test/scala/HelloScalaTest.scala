import org.scalatest._

class HelloScalaTest extends FlatSpec with Matchers {
  "HelloScala" should "have >Scala< as text" in {
    HelloScala.text should be ("Scala")
  }

  "HelloCommonScala" should "have >Common Scala< as text" in {
    HelloCommonScala.text should be ("Common Scala")
  }
}
