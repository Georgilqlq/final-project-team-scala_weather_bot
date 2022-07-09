package parsing

import enums.Location
import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.read

import javax.swing.text.DefaultFormatter

class JsonParsedTimeZone(override val rawValue: String, override val header: Map[String, Seq[String]])
    extends JsonParser[Location]:
  val parsedValue: Location =
    implicit lazy val formats: Formats = DefaultFormats

    val json = parse(rawValue) \ "location"
    json.extract[Location]
  val command: String = "timezone"
