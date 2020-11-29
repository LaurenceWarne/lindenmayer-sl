package lindenmayer.interpreters

import doodle.core._
import doodle.syntax._
import doodle.java2d._
import doodle.effect.Writer._
import doodle.image.Image
import lindenmayer.RuleTranslator._

object VectorGraphicInterpreter extends Interpreter[Image] {
  def interpret(shape: String, translator: RuleTranslator): Image = {
    Image.rectangle(1000, 1000).fillColor(Color.gray)

  }
}
