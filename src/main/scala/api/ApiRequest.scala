package api

import requests.Response
import utils.Utils

object ApiRequest:
  val weather_api = "http://api.weatherapi.com/v1"

  def makeApiRequest(command: String, commandArguments: List[String]): Response =
    requests.get(
      createUrlString(command),
      params = createRequestParameters(command, Utils.API_KEY +: commandArguments),
      check = false
    )

  def createUrlString(command: String): String = command match
    case "football" => s"$weather_api/sports.json"
    case _ => s"$weather_api/$command.json"

  def createRequestParameters(command: String, commandArguments: List[String]): Map[String, String] =
    command match
      case "current" | "forecast" | "timezone" | "football" =>
        val listToZip: List[String] = "key" :: "q" :: Nil
        listToZip.zip(commandArguments).toMap
      case "astronomy" =>
        val listToZip: List[String] = "key" :: "q" :: "dt" :: Nil
        listToZip.zip(commandArguments).toMap
      case _ => throw new IllegalStateException("Invalid command")
