package lindenmayer.interpreters

import lindenmayer.RuleTranslator._

trait Interpreter[O] {
  def interpret(shape: String, translator: RuleTranslator): O
}
