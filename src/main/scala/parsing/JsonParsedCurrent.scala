package parsing

import enums.CurrentResult
import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.read

import javax.swing.text.DefaultFormatter

class JsonParsedCurrent(override val rawValue: String, override val header: Map[String, Seq[String]])
    extends JsonParser[CurrentResult]:
  val parsedValue: CurrentResult =
    implicit lazy val formats: Formats = DefaultFormats

    val json = parse(rawValue)
    json.extract[CurrentResult]
  val command: String = "current"
