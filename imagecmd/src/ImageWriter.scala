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
    val width = if (args.length > 1) args(1).toInt else 3200
    val height = if (args.length > 2) args(2).toInt else 1800

    val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val pr: ProductionRules = dragonCurve
    val shape: String = recurse(pr, 21, dragonCurveInit)
    val interpreter: Interpreter[Iterable[(Int, Int)]] = SetInterpreter(width/2, height/2, 90)
    val tilesToColour: Set[(Int, Int)] = interpreter.interpret(
      shape, dragonCurveTranslator
    ).toSet
    for (i <- 0 until width) {
      for (j <- 0 until height) {
        img.setRGB(i, j, 0xFFFFFF)
      }
    }
    val maxX = tilesToColour.map(_._1).max
    val minX = tilesToColour.map(_._1).min
    val maxY = tilesToColour.map(_._2).max
    val minY = tilesToColour.map(_._2).min
    val centreX = (maxX + minX) / 2
    val centreY = (maxY + minY) / 2
    println(tilesToColour.size)
    tilesToColour
      .foreach(tuple => {
      val x: Int = tuple._1 - (centreX - width/2)
      val y: Int = tuple._2 - (centreY - height/2)
      if (0 <= x && x < width && 0 <= y && y < height) img.setRGB(x, y, 0xFF)
    })
    val imageName: String = args.head
    ImageIO.write(img, "jpg", new File(imageName))
  }
}
