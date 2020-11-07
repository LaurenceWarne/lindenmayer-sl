package lindenmayer.json.decoders

import scala.util.Try
import io.circe._
import io.circe.generic.semiauto._
import lindenmayer.ProductionRules.ProductionRules
import lindenmayer.RuleTranslator._
import lindenmayer.RuleTranslation
import io.circe.generic.decoding.DerivedDecoder

final case class LindenmayerRecipe(
    name: String,
    axiom: String,
    rules: ProductionRules,
    translator: RuleTranslator
)

object LindenmayerRecipeCodec {

  // Decoders

  implicit val charKeyDecoder: KeyDecoder[Char] = new KeyDecoder[Char] {
    override def apply(key: String): Option[Char] = {
      if (key.length == 1) key.lift(0) else None
    }
  }

  implicit val productionRulesDecoder: Decoder[ProductionRules] =
    Decoder.decodeMap[Char, String]

  implicit val ruleTranslationDecoder = deriveDecoder[RuleTranslation]

  implicit val ruleTranslatorDecoder = Decoder.decodeMap[Char, RuleTranslation]

  implicit val lindenmayerRecipeDecoder = deriveDecoder[LindenmayerRecipe]

  // Encoders

  implicit val charKeyEncoder = new KeyEncoder[Char] {
    override def apply(key: Char): String = key.toString
  }

  implicit val productionRulesEncoder: Encoder[ProductionRules] =
    Encoder.encodeMap[Char, String]

  implicit val ruleTranslationEncoder = deriveEncoder[RuleTranslation]

  implicit val ruleTranslatorEncoder = Encoder.encodeMap[Char, RuleTranslation]

  implicit val lindenmayerRecipeEncoder = deriveEncoder[LindenmayerRecipe]
}
