package parsing

import enums.AstronomyResult
import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.read

import javax.swing.text.DefaultFormatter

class JsonParsedAstronomy(override val rawValue: String, override val header: Map[String, Seq[String]])
    extends JsonParser[AstronomyResult]:
  val parsedValue: AstronomyResult =
    implicit lazy val formats: Formats = DefaultFormats

    val json = parse(rawValue)
    json.extract[AstronomyResult]
  val command: String = "astronomy"
