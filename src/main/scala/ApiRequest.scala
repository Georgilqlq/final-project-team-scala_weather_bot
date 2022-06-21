import requests.Response

import scala.concurrent.Future
import scala.util.Properties

object ApiRequest:
  val weather_api = "http://api.weatherapi.com/v1"

  def makeApiRequest(command: String, commandArguments: List[String]): Response =
    requests.get(
      createUrlString(command),
      params = createRequestParameters(command, Properties.envOrElse("API_KEY", "") +: commandArguments),
      check = false
    )

  private def createUrlString(command: String): String = command match
    case "football" => s"$weather_api/sports.json"
    case _ => s"$weather_api/$command.json"

  private def createRequestParameters(command: String, commandArguments: List[String]): Map[String, String] =
    val listToZip: List[String] = "key" :: "q" :: Nil

    command match
      case "current" =>
        listToZip.zip(commandArguments).toMap
      case "forecast" =>
        listToZip.zip(commandArguments).toMap
      case "astronomy" =>
        val listToZip: List[String] = "key" :: "q" :: "dt" :: Nil
        listToZip.zip(commandArguments).toMap
      case "timezone" =>
        listToZip.zip(commandArguments).toMap
      case "football" =>
        val listToZip: List[String] = "key" :: "q" :: Nil
        listToZip.zip(commandArguments).toMap
      case _ => throw new IllegalStateException("Invalid command")
