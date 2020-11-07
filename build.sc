// build.sc
import mill._, scalalib._, scalafmt._

trait lindenmayerTest extends TestModule {
  def ivyDeps =
    Agg(
      ivy"com.disneystreaming::weaver-framework:0.4.3"
    )
  // https://github.com/disneystreaming/weaver-test
  def testFrameworks = Seq("weaver.framework.TestFramework")
}

object lindenmayer extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.1"

  def ivyDeps =
    Agg(
      ivy"com.beachape::enumeratum:1.6.1",
      ivy"org.typelevel::cats-core:2.2.0"
    )

  object test extends Tests with lindenmayerTest
}

object json extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.1"
  def moduleDeps = Seq(lindenmayer)
  def ivyDeps =
    Agg(
      ivy"dev.zio::zio:1.0.0",
      ivy"io.circe::circe-core:0.12.3",
      ivy"io.circe::circe-generic:0.12.3",
      ivy"io.circe::circe-parser:0.12.3"
    )

  object test extends Tests with lindenmayerTest
}

object imagecmd extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.1"
  def moduleDeps = Seq(lindenmayer, json)
  def ivyDeps =
    Agg(
      ivy"dev.zio::zio:1.0.0"
    )

  object test extends Tests with lindenmayerTest {
    def ivyDeps =
      super.ivyDeps() ++ Agg(
        ivy"com.disneystreaming::weaver-zio:0.4.3",
        ivy"com.disneystreaming::weaver-zio:0.5.0"
      )
  }
}
