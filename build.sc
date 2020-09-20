// build.sc
import mill._, scalalib._

object lindenmayer extends ScalaModule {
  def scalaVersion = "2.13.1"

  object test extends Tests {
    def ivyDeps = Agg(
      ivy"com.disneystreaming::weaver-framework:0.4.3",
      ivy"com.disneystreaming::weaver-zio:0.4.3"
    )
    // https://github.com/disneystreaming/weaver-test
    def testFrameworks = Seq("weaver.framework.TestFramework")
  }
}
