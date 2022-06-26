import ApiRequest.makeApiRequest
import requests.{RequestFailedException, Response}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps
import scala.util.{Failure, Success}

object ResponseHandler:
  def manageResponse[A](
    response: Response,
    command: String
  ): JsonParser[? >: CurrentResult with List[Hour] with AstronomyResult with Location with List[Matches] <: Equals] =
    command match
      case "current" => new JsonParsedCurrent(response.text(),response.headers)
      case "forecast" => new JsonParsedForecast(response.text(),response.headers)
      case "astronomy" => new JsonParsedAstronomy(response.text(),response.headers)
      case "timezone" => new JsonParsedTimeZone(response.text(),response.headers)
      case "football" => new JsonParsedFootball(response.text(),response.headers)

  def createParsedCurrent(command: String = "current", commandArguments: List[String]): Future[JsonParsedCurrent] =
    val response = makeApiRequest(command, commandArguments)

    response.statusCode match
      case x if 200 until 300 contains x => Future.successful(new JsonParsedCurrent(response.text(),response.headers))
      case _ => Future.failed(new RequestFailedException(response))

  def createParsedForecast(command: String = "forecast", commandArguments: List[String]): Future[JsonParsedForecast] =
    val response = makeApiRequest(command, commandArguments)

    response.statusCode match
      case x if 200 until 300 contains x => Future.successful(new JsonParsedForecast(response.text(),response.headers))
      case _ => Future.failed(new RequestFailedException(response))

  def createParsedFootball(command: String = "football", commandArguments: List[String]): Future[JsonParsedFootball] =
    val response = makeApiRequest(command, commandArguments)

    response.statusCode match
      case x if 200 until 300 contains x => Future.successful(new JsonParsedFootball(response.text(),response.headers))
      case _ => Future.failed(new RequestFailedException(response))

  def createParsedTimeZone(command: String = "timezone", commandArguments: List[String]): Future[JsonParsedTimeZone] =
    val response = makeApiRequest(command, commandArguments)

    response.statusCode match
      case x if 200 until 300 contains x => Future.successful(new JsonParsedTimeZone(response.text(),response.headers))
      case _ => Future.failed(new RequestFailedException(response))

  def createParsedAstronomy(
    command: String = "astronomy",
    commandArguments: List[String]
  ): Future[JsonParsedAstronomy] =
    val response = makeApiRequest(command, commandArguments)

    response.statusCode match
      case x if 200 until 300 contains x => Future.successful(new JsonParsedAstronomy(response.text(),response.headers))
      case _ => Future.failed(new RequestFailedException(response))
