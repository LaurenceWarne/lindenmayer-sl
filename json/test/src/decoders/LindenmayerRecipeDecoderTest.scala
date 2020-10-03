package json

import weaver.SimpleIOSuite
import json.LindenmayerRecipeDecoder._
import lindenmayer.ProductionRules._
import lindenmayer.RuleTranslator._
import io.circe._
import io.circe.parser._
import io.circe.Json
import lindenmayer.RuleTranslation

object LindenmayerRecipeDecoderSuite extends SimpleIOSuite {

  pureTest("test can decode string of length one to char") {
    val json: String = "a"
    expect('a' == charKeyDecoder.apply(json).get)
  }

  pureTest("test cannot decode empty string") {
    val json: String = ""
    expect(charKeyDecoder.apply(json).isEmpty)
  }

  pureTest("test cannot decode string of length > 1") {
    val json: String = "ab"
    expect(charKeyDecoder.apply(json).isEmpty)
  }

  pureTest("test can decode int into Turn translation") {
    val json = "180"
    expect(parser.decode[RuleTranslation](json) == Turn(180))
  }
}
