package lindenmayer.json.decoders

import weaver.SimpleIOSuite
import lindenmayer.json.decoders.LindenmayerRecipeCodec._
import lindenmayer.ProductionRules._
import lindenmayer.RuleTranslator._
import lindenmayer.RuleTranslation._
import io.circe._
import io.circe.parser._
import io.circe.generic.auto._
import io.circe.syntax._
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

  pureTest("test can decode valid LindenmayerRecipe") {
    val json =
      """
        |{
        |    "name": "dragon-curve",
        |    "axiom": "FX",
        |    "rules": {
        |        "X": "X+YF+",
        |        "Y": "-FX-Y"
        |    },
        |    "translator": {
        |        "F": {"Forward": {}},
        |        "+": {"Turn": {"degrees": 270}},
        |        "-": {"Turn": {"degrees": 90}}
        |    }
        |}
    """.stripMargin
    val expected = LindenmayerRecipe(
      "dragon-curve",
      "FX",
      Map('X' -> "X+YF+", 'Y' -> "-FX-Y"),
      Map('F' -> Forward, '+' -> Turn(270), '-' -> Turn(90))
    )
    expect(
      parser
        .decode[LindenmayerRecipe](json)
        .right
        .exists(
          _.equals(
            LindenmayerRecipe(
              "dragon-curve",
              "FX",
              Map('X' -> "X+YF+", 'Y' -> "-FX-Y"),
              Map('F' -> Forward, '+' -> Turn(270), '-' -> Turn(90))
            )
          )
        )
    )
  }

  pureTest("test cannot decode incomplete LindenmayerRecipe") {
    val json =
      """
        |{
        |    "name": "dragon-curve",
        |    "axiom": "FX",
        |    "rules": {
        |        "X": "X+YF+",
        |        "Y": "-FX-Y"
        |    },
        |}
    """.stripMargin
    expect(parser.decode[LindenmayerRecipe](json).isLeft)
  }

}
