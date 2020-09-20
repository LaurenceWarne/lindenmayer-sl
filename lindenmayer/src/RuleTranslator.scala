package lindenmayer

sealed trait RuleTranslation

object RuleTranslator {
  case object Forward extends RuleTranslation
  case class Turn(degrees: Int) extends RuleTranslation
}
