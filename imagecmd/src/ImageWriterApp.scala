package imagecmd

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import lindenmayer.ProductionRules.ProductionRules
import lindenmayer.Production.recurse
import lindenmayer.interpreters.Interpreter
import lindenmayer.interpreters.SetInterpreter
import lindenmayer.RuleTranslator.{Forward, Turn}
import lindenmayer.Recipes._
import zio._
import java.io.IOException
import java.io.FileNotFoundException

object ImageWriterApp extends zio.App {

  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    val width = args.lift(1).map(_.toInt).getOrElse(3200)
    val height = args.lift(2).map(_.toInt).getOrElse(1800)
    val iterations = args.lift(3).map(_.toInt).getOrElse(21)

    val pr: ProductionRules = dragonCurve
    val shape: String = recurse(pr, iterations, dragonCurveInit)
    val interpreter: Interpreter[Iterable[(Int, Int)]] = SetInterpreter(
      width / 2,
      height / 2,
      90
    )
    val cellsToColour: Set[(Int, Int)] = interpreter
      .interpret(
        shape,
        dragonCurveTranslator
      )
      .toSet
    val img = getCenteredImage(cellsToColour, width, height)
    val name: String = args.headOption.getOrElse("test.jpg")

    val ret: ZIO[console.Console with ImageWriter.ImageWriter, Nothing, Unit] =
      ZIO
        .accessM[ImageWriter.ImageWriter](_.get.writeImage(img, name))
        .foldM( // Folds to other effects, Fold() folds to values
          e => console.putStrLn(s"Error writing to $name: $e"),
          _ => console.putStrLn(s"Wrote to $name")
        )

    // Provide environment
    val layer: ZLayer[Any, Nothing, ImageWriter.ImageWriter] =
      ZLayer.succeed(
        new ImageWriterServiceImpl()
      )
    val program: ZIO[Any, Nothing, Unit] =
      ret.provideLayer(layer ++ console.Console.live)
    program.exitCode
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
