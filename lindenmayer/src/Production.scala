package lindenmayer

import ProductionRules.ProductionRules

object Production {
  def recurse(rules: ProductionRules, iterations: Int, state: String): String =
    if (iterations == 0) state
    else
      recurse(
        rules,
        iterations - 1,
        state.flatMap(c => rules.getOrElse(c, c.toString))
      )
}
