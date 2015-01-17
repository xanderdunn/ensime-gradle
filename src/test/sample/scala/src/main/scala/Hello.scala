object HelloScala {
  val text = "Scala"

  def main(args: Array[String]): Unit = {
    println(s"Hello ${text}")
    println(s"Hello ${HelloJava.text}")

    println(s"Hello ${HelloCommonJava.text}")
    println(s"Hello ${HelloCommonScala.text}")
  }
}
