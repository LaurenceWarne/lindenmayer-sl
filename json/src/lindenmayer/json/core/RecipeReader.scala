package lindenmayer.json.core

import zio._
import lindenmayer.json.decoders.LindenmayerRecipe

object RecipeReader {

  type RecipeReader = Has[RecipeReader.Service]

  trait Service {
    def readFile(file: String): Task[Map[String, LindenmayerRecipe]]
  }

  def readFile(
      file: String
  ): ZIO[RecipeReader, Throwable, Map[String, LindenmayerRecipe]] =
    ZIO.accessM(_.get.readFile(file))
}
