package lindenmayer.imagecmd

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import lindenmayer.ProductionRules.ProductionRules
import lindenmayer.Production.recurse
import lindenmayer.interpreters.Interpreter
import lindenmayer.interpreters.SetInterpreter
import lindenmayer.RuleTranslation.{Forward, Turn}
import lindenmayer.Recipes._
import lindenmayer.json.core.RecipeReader
import lindenmayer.json.core.RecipeReader._
import lindenmayer.imagecmd.ImageWriter._
import zio._
import java.io.IOException
import java.io.FileNotFoundException
import lindenmayer.json.core.RecipeReaderJson

object ImageWriterApp extends zio.App {

  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    val recipeName = args.lift(1).getOrElse("dragon-curve")
    val width = args.lift(2).map(_.toInt).getOrElse(3200)
    val height = args.lift(3).map(_.toInt).getOrElse(1800)
    val iterations = args.lift(4).map(_.toInt).getOrElse(21)

    val prog: ZIO[
      console.Console with system.System with ImageWriter with RecipeReader,
      Nothing,
      ExitCode
    ] =
      (for {
        configFileOp <-
          system
            .env("HOME")
            .map(op => op.map(_ + "/.config/lindenmayer-sl/recipes.json"))
            .mapError(e => "Error reading HOME environment variable")
        configFile <-
          ZIO
            .fromOption(configFileOp)
            .mapError(e => "Error reading HOME environment variable")
        recipes <-
          ZIO
            .accessM[RecipeReader](
              _.get.readFile(configFile)
            )
            .mapError(e => s"Error reading recipes: $e")
        recipe <-
          ZIO
            .fromOption(recipes.get(recipeName))
            .mapError(e => s"Recipe '$recipeName' not found")
        shape = recurse(recipe.rules, iterations, recipe.axiom)
        interpreter = SetInterpreter(width / 2, height / 2, 90)
        cellsToColour = interpreter.interpret(shape, recipe.translator)
        img = getCenteredImage(cellsToColour, width, height)
        name = args.headOption.getOrElse("test.jpg")
        res <-
          ZIO
            .accessM[ImageWriter](_.get.writeImage(img, name))
            .map(_ => s"Wrote to $name")
            .mapError(e => s"Error writing to $name: $e")
      } yield res)
        .foldM(
          e => console.putStrLn(e),
          s => console.putStrLn(s)
        )
        .exitCode

    // Provide environment
    val layer: ZLayer[
      Any,
      Nothing,
      ImageWriter with RecipeReader with console.Console with system.System
    ] = ZLayer.succeed[ImageWriter.Service](
      new ImageWriterServiceImpl()
    ) ++ ZLayer.succeed[RecipeReader.Service](
      new RecipeReaderJson()
    ) ++ console.Console.live ++ system.System.live

    val program: ZIO[Any, Nothing, ExitCode] = prog.provideLayer(layer)
    program
  }

  def getCenteredImage(
      cellsToColour: Iterable[(Int, Int)],
      width: Int,
      height: Int
  ): BufferedImage = {
    val img: BufferedImage =
      new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val (maxX, minX, maxY, minY) = cellsToColour.foldLeft(
      (Int.MinValue, Int.MaxValue, Int.MinValue, Int.MaxValue)
    )((tuple, newCell) => {
      val (maxX, minX, maxY, minY) = tuple
      val (newX, newY) = newCell
      (
        if (newX > maxX) newX else maxX,
        if (newX < minX) newX else minX,
        if (newY > maxY) newY else maxY,
        if (newY < minY) newY else minY
      )
    })
    val centreX = (maxX + minX) / 2
    val centreY = (maxY + minY) / 2
    cellsToColour
      .foreach(tuple => {
        val x: Int = tuple._1 - (centreX - width / 2)
        val y: Int = tuple._2 - (centreY - height / 2)
        if (0 <= x && x < width && 0 <= y && y < height) img.setRGB(x, y, 0xff)
      })
    img
  }

  // Side effects!
  def writeImage(
      img: BufferedImage,
      fileName: String
  ): Task[Unit] =
    Task.effect(ImageIO.write(img, "jpg", new File(fileName)))
}
