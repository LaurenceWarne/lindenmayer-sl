package lindenmayer.json.decoders

import scala.util.Try
import io.circe.{Decoder, HCursor, Json}
import lindenmayer.ProductionRules.ProductionRules
import lindenmayer.RuleTranslator._
import io.circe.KeyDecoder
import lindenmayer.RuleTranslation

final case class LindenmayerRecipe(
    name: String,
    axiom: String,
    rules: ProductionRules,
    translator: RuleTranslator
)

object LindenmayerRecipeDecoder {

  implicit val charKeyDecoder: KeyDecoder[Char] = new KeyDecoder[Char] {
    override def apply(key: String): Option[Char] = {
      if (key.length == 1) key.lift(0) else None
    }
  }

  implicit val decodeRuleTranslation: Decoder[RuleTranslation] =
    new Decoder[RuleTranslation] {
      final def apply(c: HCursor): Decoder.Result[RuleTranslation] = {
        val st: Decoder.Result[String] = c.downField("move").as[String]
        if (st.getOrElse("").toLowerCase == "forward")
          st.map(st => RuleTranslation.Forward)
        else c.downField("turn").as[Int].map(RuleTranslation.Turn(_))
      }
    }

  implicit val decodeLindenmayerRecipe: Decoder[LindenmayerRecipe] =
    new Decoder[LindenmayerRecipe] {
      final def apply(c: HCursor): Decoder.Result[LindenmayerRecipe] =
        for {
          name <- c.downField("name").as[String]
          axiom <- c.downField("axiom").as[String]
          rules <- c.downField("rules").as[Map[Char, String]]
          translator <- c.downField("translator").as[Map[Char, RuleTranslation]]
        } yield {
          LindenmayerRecipe(name, axiom, rules, translator)
        }
    }
}
