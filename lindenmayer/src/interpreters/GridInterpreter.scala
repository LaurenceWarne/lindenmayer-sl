package lindenmayer.interpreters

import lindenmayer.interpreters.Interpreter
import lindenmayer.RuleTranslator.RuleTranslator
import scala.collection.immutable.TreeSet
import lindenmayer.RuleTranslation
import lindenmayer.RuleTranslator.{Forward, Turn}

trait BaseInterpreter[C <: collection.immutable.Iterable[(Int, Int)]]
    extends Interpreter[C] {

  def getInit: (C, (Int, Int), Int)

  def updatePosition(travelled: C, newPosition: (Int, Int)): C

  def vectorFromAngleFn(angle: Int): (Int, Int)

  override def interpret(
      shape: String,
      translator: RuleTranslator
  ): C =
    shape
      .map(translator.get(_))
      .flatten
      .foldLeft(getInit)(
        (tuple, trans) => {
          val travelled: C = tuple._1
          val position: (Int, Int) = tuple._2
          val angle: Int = tuple._3
            trans match {
              case Turn(degrees) =>
                (travelled, position, math.floorMod(angle + degrees, 360))
              case Forward => {
                val nxtVector = vectorFromAngleFn(angle)
                val nxtPos =
                  Tuple2(position._1 + nxtVector._1, position._2 + nxtVector._2)
                (updatePosition(travelled, nxtPos), nxtPos, angle)
              }
            }
        }
      )
      ._1
}

case class ListInterpreter(
    x: Int,
    y: Int,
    angle: Int = 0,
    vectorFn: Int => (Int, Int) = GridInterpreter.getVector
) extends BaseInterpreter[List[(Int, Int)]] {

  override def getInit: (List[(Int, Int)], (Int, Int), Int) =
    (List((x, y)), (x, y), angle)

  override def updatePosition(travelled: List[(Int, Int)], newPosition: (Int, Int)) =
    travelled :+ newPosition

  override def vectorFromAngleFn(angle: Int) = vectorFn(angle)
}

case class SetInterpreter(
    x: Int,
    y: Int,
    angle: Int = 0,
    vectorFn: Int => (Int, Int) = GridInterpreter.getVector
) extends BaseInterpreter[Set[(Int, Int)]] {

  def getInit: (Set[(Int, Int)], (Int, Int), Int) =
    (Set((x, y)), (x, y), angle)

  override def updatePosition(travelled: Set[(Int, Int)], newPosition: (Int, Int)) =
    travelled + newPosition

  override def vectorFromAngleFn(angle: Int) = vectorFn(angle)
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
