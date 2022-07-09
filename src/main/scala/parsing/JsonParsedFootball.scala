package parsing

import enums.Matches
import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.read

import javax.swing.text.DefaultFormatter

class JsonParsedFootball(override val rawValue: String, override val header: Map[String, Seq[String]])
    extends JsonParser[List[Matches]]:
  val parsedValue: List[Matches] =
    implicit lazy val formats: Formats = DefaultFormats

    val json = parse(rawValue) \ "football"
    json.extract[List[Matches]]
  val command: String = "football"
