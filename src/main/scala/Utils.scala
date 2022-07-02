import scala.util.Properties
import scala.util.matching.Regex

object Utils:

  def mkRegex(str1: String): Regex = ("(.*" + str1 + ".*)").r

  val FILE_NAME: String = "file.xlsx"
  val API_KEY: String = Properties.envOrElse("API_KEY", "")
