import requests.RequestFailedException

import java.io.FileNotFoundException
import java.util.regex.Pattern
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.StdIn.readLine
import scala.util.matching.Regex

object Console:
  def main(args: Array[String]): Unit =
    print("$ ")

    while continue(readLine().toUpperCase()) do print("$ ")

  def mkRegex(str1: String): Regex = ("(.*" + str1 + ".*)").r

  def continue(commandLine: String): Boolean =
    val helpR: Regex = mkRegex(CommandEnum.Help.value)
    val exitR: Regex = mkRegex(CommandEnum.Exit.value)
    val currentR: Regex = mkRegex(CommandEnum.Current.value)
    val forecastR: Regex = mkRegex(CommandEnum.Forecast.value)
    val astronomyR: Regex = mkRegex(CommandEnum.Astronomy.value)
    val timezoneR: Regex = mkRegex(CommandEnum.Timezone.value)
    val footballR: Regex = mkRegex(CommandEnum.Football.value)

    commandLine match
      case exitR(_) => {
        println("The programme ends!")
        CommandEnum.Exit.continue
      }
      case helpR(_) => {
        caller(commandLine, CommandEnum.Help.value, Service.help)
        CommandEnum.Help.continue
      }
      case currentR(_) => {
        caller(commandLine, CommandEnum.Current.value, Service.current)
        CommandEnum.Current.continue
      }
      case forecastR(_) => {
        caller(commandLine, CommandEnum.Forecast.value, Service.forecast)
        CommandEnum.Forecast.continue
      }
      case astronomyR(_) => {
        caller(commandLine, CommandEnum.Astronomy.value, Service.astronomy)
        CommandEnum.Astronomy.continue
      }
      case timezoneR(_) => {
        caller(commandLine, CommandEnum.Timezone.value, Service.timezone)
        CommandEnum.Timezone.continue
      }
      case footballR(_) => {
        caller(commandLine, CommandEnum.Football.value, Service.football)
        CommandEnum.Football.continue
      }
      case _ => {
        println(
          "Wrong command! Please enter 'help' in order to see detailed information about the supported operations."
        )
        true
      }

  def extractArguments(commandLine: String, command: String): List[String] =
    val index = (commandLine indexOf command) + command.size
    val args = commandLine.substring(0, index - command.size) + commandLine.substring(index)
    args.split(' ').toList.filter(!_.isBlank)

  def caller(commandLine: String, command: String, executioner: List[String] => Future[Unit]): Future[Unit] =
    Service.checkArgs(extractArguments(commandLine, command), executioner).recoverWith {
      case e: IllegalStateException =>
        Future.successful(
          println(
            e.getMessage() + "Please enter 'help' in order to see detailed information about the supported operations."
          )
        )
      case e: RequestFailedException =>
        Future.successful(println("There was an error with the request!" + e.getMessage))
      case e:FileNotFoundException =>
        Future.successful(println("There was an error with the logging file!" + e.getMessage))
      case e: Exception =>
        Future.successful(
          println(
            "Unknown error! Please enter 'help' in order to see detailed information about the supported operations." + e
              .getMessage()
          )
        )
    }
