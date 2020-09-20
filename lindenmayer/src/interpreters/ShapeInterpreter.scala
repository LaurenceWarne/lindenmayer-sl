package lindenmayer.interpreters

trait Interpreter[O] {
  def Interpret(shape: String): O
}
