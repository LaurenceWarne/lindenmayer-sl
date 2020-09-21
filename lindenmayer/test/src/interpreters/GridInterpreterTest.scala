package lindenmayer.interpreters

import weaver.SimpleIOSuite
import lindenmayer.RuleTranslator.{Forward, Turn}
import lindenmayer.RuleTranslator

object GridInterpreterSuite extends SimpleIOSuite {

  val basicGi = ListInterpreter(0, 0, vectorFromAngleFn = _ => (1, 0))
  val listGi = ListInterpreter(0, 0)
  val setGi = SetInterpreter(0, 0)

  pureTest("test ListInterpreter returns correct coords on simple pattern"){
    val shape = "FFFF"
    expect(List((0, 0), (1, 0), (2, 0), (3, 0), (4, 0))
      == basicGi.interpret(shape, Map('F' -> Forward)))
  }

  pureTest("test ListInterpreter ignores unmapped chars"){
    val shape = "[F]X"
    expect(List((0, 0), (1, 0)) == basicGi.interpret(shape, Map('F' -> Forward)))
  }

  pureTest("test ListInterpreter can handle complex pattern"){
    val dcGi = ListInterpreter(0, 0, 90)
    val dcTranslator = Map('F' -> Forward, '+' -> Turn(270), '-' -> Turn(90))
    val shape = "FX+YF++-FX-YF++-FX+YF+--FX-YF+"
    expect(List((0, 0), (0, 1), (1, 1), (1, 0), (2, 0), (2, -1), (1, -1), (1, -2), (2, -2))
      == dcGi.interpret(shape, dcTranslator))
  }

  pureTest("test SetInterpreter returns correct coords on simple pattern"){
    val shape = "FFFF"
    expect(Set((0, 0), (1, 0), (2, 0), (3, 0), (4, 0))
      == setGi.interpret(shape, Map('F' -> Forward)))
  }

  pureTest("test SetInterpreter ignores unmapped chars"){
    val shape = "[F]X"
    expect(Set((0, 0), (1, 0)) == setGi.interpret(shape, Map('F' -> Forward)))
  }

  pureTest("test SetInterpreter can handle complex pattern"){
    val dcGi = SetInterpreter(0, 0, 90)
    val dcTranslator = Map('F' -> Forward, '+' -> Turn(270), '-' -> Turn(90))
    val shape = "FX+YF++-FX-YF++-FX+YF+--FX-YF+"
    expect(Set((0, 0), (0, 1), (1, 1), (1, 0), (2, 0), (2, -1), (1, -1), (1, -2), (2, -2))
      == dcGi.interpret(shape, dcTranslator))
  }
}
