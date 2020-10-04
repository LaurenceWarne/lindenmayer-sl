package lindenmayer

import enumeratum._

sealed trait RuleTranslation extends EnumEntry

object RuleTranslation extends Enum[RuleTranslation] {

  override val values = findValues

  case object Forward extends RuleTranslation
  case class Turn(degrees: Int) extends RuleTranslation
}

object RuleTranslator {

  type RuleTranslator = Map[Char, RuleTranslation]
}
