import scala.concurrent.Future
import scala.util.Properties
import scala.util.matching.Regex

object Utils:

  def mkRegex(str1: String): Regex = ("(.*" + str1 + ".*)").r

  def validateCity(commandArguments: List[String], action: List[String] => Future[Unit]): Future[Unit] =
    if commandArguments.isEmpty
    then Future.failed(new IllegalStateException(CITY_IS_MANDATORY_MESSAGE))
    else if !commandArguments(0).matches("[A-Z]+")
    then Future.failed(new IllegalStateException(CITY_CONSTRAINTS_MESSAGE))
    else action(commandArguments)

  val FILE_NAME: String = "file.xlsx"
  val DATE_WIDTH:Int = 8000
  val JSON_WIDTH:Int = 12000
  val API_KEY: String = Properties.envOrElse("API_KEY", "")
  val NO_HISTORY_MESSAGE: String = "There is no history for this command!"

  val CITY_IS_MANDATORY_MESSAGE: String = "City is a mandatory field."
  val CITY_CONSTRAINTS_MESSAGE: String = "City cannot contains special symbols and digits."

  val HELP_INSTRUCTIONS: List[List[String]] = List(
    List("command", "description"),
    List("help", "This command prints out this information."),
    List("exit", "This command stops the programme."),
    List("current <city>", "This command shows detailed information about the weather in the chosen city(<city>)."),
    List(
      "forecast <city>",
      "This command shows the forecast for the next day for the chosen city(<city). It also plots the temperature."
    ),
    List(
      "astronomy <city> <date>",
      "This command shows astronomy details about the chosen city(<city>) on the chosen date(<date>). If date is not entered, the date is today by default."
    ),
    List("timezone <city>", "This command prints out information about the timezone of the chosen city(<city>)."),
    List("football <city>", "This command prints out information about the football matches for the day."),
    List("read-sheet <command>", "This command prints out history about all invocations of this command."),
    List("read-row <command> <number>", "This command prints out information about specific command by id."),
    List("delete", "This deletes the history of the used commands.")
  )
