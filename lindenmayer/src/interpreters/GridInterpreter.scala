package lindenmayer.interpreters

import lindenmayer.interpreters.Interpreter
import lindenmayer.RuleTranslator._

case class GridInterpreter(
    x: Int,
    y: Int,
    angle: Int = 0,
    vectorFromAngle: Int => (Int, Int) = GridInterpreter.getVector
) extends Interpreter[Seq[(Int, Int)]] {

  override def interpret(
      shape: String,
      translator: RuleTranslator
  ): Seq[(Int, Int)] =
    shape
      .map(translator.get(_))
      .flatten
      .foldLeft((List((x, y)), angle))(
        (tuple, trans) => {
          val travelled = tuple._1
          val position = travelled.last
          val angle = tuple._2
          trans match {
            case Turn(degrees) =>
              (travelled, math.floorMod(angle + degrees, 360))
            case Forward => {
              val nxtVector = vectorFromAngle(angle)
              (
                travelled :+ (position._1 + nxtVector._1, position._2 + nxtVector._2),
                angle
              )
            }
          }
        }
      )
      ._1
}

object GridInterpreter {
  val getVector: Int => (Int, Int) = angle => {
    val normalisedAngle = Math.floorMod(angle, 360)
    normalisedAngle match {
      case a if (a >= 337.5 || a < 22.5)  => (1, 0)
      case a if (22.5 <= a && a < 67.5)   => (1, 1)
      case a if (67.5 <= a && a < 112.5)  => (0, 1)
      case a if (112.5 <= a && a < 157.5) => (-1, 1)
      case a if (157.5 <= a && a < 202.5) => (-1, 0)
      case a if (202.5 <= a && a < 247.5) => (-1, -1)
      case a if (247.5 <= a && a < 292.5) => (0, -1)
      case a if (292.5 <= a && a < 337.5) => (1, -1)
    }
  }
}