package lindenmayer.json.core

import lindenmayer.json.decoders.LindenmayerRecipe
import lindenmayer.json.decoders.LindenmayerRecipeDecoder._
import io.circe._
import io.circe.parser._
import lindenmayer.json.decoders.LindenmayerRecipeDecoder
import scala.io.Source
import scala.util.Try
import zio.ZIO

class RecipeReaderJson extends RecipeReader.Service {

  override def readFile(
      file: String
  ): zio.Task[Map[String, LindenmayerRecipe]] =
    for {
      fileString <- ZIO.fromTry(Try(Source.fromFile(file).mkString))
      recipeList <-
        ZIO
          .fromEither(
            parser.decode[List[LindenmayerRecipe]](fileString)
          )
    } yield recipeList.map(recipe => (recipe.name, recipe)).toMap
}
