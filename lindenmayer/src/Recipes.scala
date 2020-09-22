package lindenmayer

import ProductionRules.ProductionRules
import RuleTranslator.{RuleTranslator, Forward, Turn}

object Recipes {
  // See
  // https://en.wikipedia.org/wiki/L-system
  val dragonCurve: ProductionRules = Map('X' -> "X+YF+", 'Y' → "-FX-Y")
  val dragonCurveTranslator: RuleTranslator =
    Map('F' -> Forward, '+' -> Turn(270), '-' -> Turn(90))
  val dragonCurveInit: String = "FX"

  val sierpinski: ProductionRules = Map('A' -> "B-A-B", 'B' -> "A+B+A")
  val sierpinskiTranslator: RuleTranslator =
    Map('A' -> Forward, 'B' -> Forward, '+' -> Turn(300), '-' -> Turn(60))
  val sierpinskiInit: String = "A"
}
