package lindenmayer

import weaver.SimpleIOSuite
import scala.collection.Map

import ProductionRules.ProductionRules
import Production.recurse

object ProductionSuite extends SimpleIOSuite {

  pureTest("test recurse returns state on zero iterations"){
    val pr: ProductionRules = Map('A' -> "AA")
    expect(recurse(pr, 0, "A") == "A")
  }

  pureTest("test recurse returns correct state on multiple iterations"){
    val pr: ProductionRules = Map('A' -> "AB", 'B' -> "A")
    expect(recurse(pr, 7, "A") == "ABAABABAABAABABAABABAABAABABAABAAB")
  }

  pureTest("test unmapped values are ignored by recurse"){
    val pr: ProductionRules = Map('1' -> "11", '0' -> "1[0]0")
    expect(recurse(pr, 3, "0") == "1111[11[1[0]0]1[0]0]11[1[0]0]1[0]0")
  }
  
}

