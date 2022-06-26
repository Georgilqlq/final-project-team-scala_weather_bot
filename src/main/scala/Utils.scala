import scala.util.matching.Regex

object Utils:

  def mkRegex(str1: String): Regex = ("(.*" + str1 + ".*)").r
