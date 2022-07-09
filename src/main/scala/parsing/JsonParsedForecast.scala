package parsing

import enums.Hour
import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.read

import javax.swing.text.DefaultFormatter

class JsonParsedForecast(override val rawValue: String, override val header: Map[String, Seq[String]])
    extends JsonParser[List[Hour]]:
  val parsedValue: List[Hour] =
    implicit lazy val formats: Formats = DefaultFormats

    val json = parse(rawValue) \\ "hour"
    json.extract[List[Hour]]
  val command: String = "forecast"

  def temperatures: List[Double] = parsedValue.map(_.temp_c)

  def hours: List[String] = parsedValue.map(_.time)
