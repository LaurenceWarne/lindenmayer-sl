// build.sc
import mill._, scalalib._, scalafmt._

trait lindenmayerTest extends TestModule {
    def ivyDeps = Agg(
      ivy"com.disneystreaming::weaver-framework:0.4.3",
    )
    // https://github.com/disneystreaming/weaver-test
    def testFrameworks = Seq("weaver.framework.TestFramework")
}

object lindenmayer extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.1"

  object test extends Tests with lindenmayerTest
}

object imagecmd extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.1"
  def moduleDeps = Seq(lindenmayer)

  object test extends Tests with lindenmayerTest {
    def ivyDeps = super.ivyDeps() ++ Agg(
      ivy"com.disneystreaming::weaver-zio:0.4.3"
    )
  }
}
