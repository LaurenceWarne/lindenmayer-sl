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

object ImageWriter {

  def main(args: Array[String]): Unit = {
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
    writeImage(img, name)
    // side effect
    println(s"Wrote to $name")
  }

  def getCenteredImage(
      cellsToColour: Iterable[(Int, Int)],
      width: Int,
      height: Int
  ): BufferedImage = {
    val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val maxX = cellsToColour.map(_._1).max
    val minX = cellsToColour.map(_._1).min
    val maxY = cellsToColour.map(_._2).max
    val minY = cellsToColour.map(_._2).min
    val centreX = (maxX + minX) / 2
    val centreY = (maxY + minY) / 2
    cellsToColour
      .foreach(tuple => {
        val x: Int = tuple._1 - (centreX - width / 2)
        val y: Int = tuple._2 - (centreY - height / 2)
        if (0 <= x && x < width && 0 <= y && y < height) img.setRGB(x, y, 0xFF)
      })
    img
  }

  // Side effects!
  def writeImage(img: BufferedImage, fileName: String): Unit =
    ImageIO.write(img, "jpg", new File(fileName))
}
