package lindenmayer.json.core

import lindenmayer.json.decoders.LindenmayerRecipe
import lindenmayer.json.decoders.LindenmayerRecipeDecoder._
import io.circe._
import io.circe.parser._
import lindenmayer.json.decoders.LindenmayerRecipeDecoder
import scala.io.Source
import zio.ZIO

class RecipeReaderJson extends RecipeReader.Service {

  override def readFile(
      file: String
  ): zio.Task[Map[String, LindenmayerRecipe]] =
    ZIO
      .fromEither(
        parser.decode[List[LindenmayerRecipe]](Source.fromFile(file).mkString)
      )
      .map(recipeList => recipeList.map(recipe => (recipe.name, recipe)).toMap)
}
