package lindenmayer

import weaver.SimpleIOSuite

import ProductionRules.ProductionRules

object ProductionRulesSuite extends SimpleIOSuite {

  pureTest("test can get production rule from ProductionRules") {
    val pr: ProductionRules = Map('A' -> "AA")
    expect(pr.get('A').get == "AA")
  }
}
