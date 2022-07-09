package service

import utils.Utils.mkRegex
import enums.CommandEnum
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFTable, XSSFWorkbook}
import requests.RequestFailedException
import service.Service

import java.io.{File, FileInputStream, FileNotFoundException}
import java.util.regex.Pattern
import scala.beans.BeanProperty
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.StdIn.readLine
import scala.util.matching.Regex

class Console(
  @BeanProperty
  val myService: Service
):

  def continue(commandLine: String): Boolean =
    val helpR: Regex = mkRegex(CommandEnum.Help.value)
    val exitR: Regex = mkRegex(CommandEnum.Exit.value)
    val currentR: Regex = mkRegex(CommandEnum.Current.value)
    val forecastR: Regex = mkRegex(CommandEnum.Forecast.value)
    val astronomyR: Regex = mkRegex(CommandEnum.Astronomy.value)
    val timezoneR: Regex = mkRegex(CommandEnum.Timezone.value)
    val footballR: Regex = mkRegex(CommandEnum.Football.value)
    val readSheetR: Regex = mkRegex(CommandEnum.READ_SHEET.value)
    val readRowR: Regex = mkRegex(CommandEnum.READ_ROW.value)
    val deleteR: Regex = mkRegex(CommandEnum.DELETE.value)

    commandLine match
      case exitR(_) => {
        println("The programme ends!")
        CommandEnum.Exit.continue
      }
      case readSheetR(_) => {
        caller(commandLine, CommandEnum.READ_SHEET.value, myService.printSheet, true)
        CommandEnum.READ_SHEET.continue
      }
      case readRowR(_) => {
        caller(commandLine, CommandEnum.READ_ROW.value, myService.printRow, true)
        CommandEnum.READ_ROW.continue
      }
      case helpR(_) => {
        caller(commandLine, CommandEnum.Help.value, myService.help)
        CommandEnum.Help.continue
      }
      case currentR(_) => {
        caller(commandLine, CommandEnum.Current.value, myService.current)
        CommandEnum.Current.continue
      }
      case forecastR(_) => {
        caller(commandLine, CommandEnum.Forecast.value, myService.forecast)
        CommandEnum.Forecast.continue
      }
      case astronomyR(_) => {
        caller(commandLine, CommandEnum.Astronomy.value, myService.astronomy)
        CommandEnum.Astronomy.continue
      }
      case timezoneR(_) => {
        caller(commandLine, CommandEnum.Timezone.value, myService.timezone)
        CommandEnum.Timezone.continue
      }
      case footballR(_) => {
        caller(commandLine, CommandEnum.Football.value, myService.football)
        CommandEnum.Football.continue
      }
      case deleteR(_) => {
        caller(commandLine, CommandEnum.DELETE.value, myService.delete)
        CommandEnum.DELETE.continue
      }
      case _ => {
        println(
          "Wrong command! Please enter 'help' in order to see detailed information about the supported operations."
        )
        true
      }

  def extractArguments(commandLine: String, command: String): List[String] =
    val index = (commandLine indexOf command) + command.size
    val args = commandLine.substring(index)
    args.split(' ').toList.filter(!_.isBlank)

  def caller(
    commandLine: String,
    command: String,
    executioner: List[String] => Future[Unit],
    acceptsTwoCommands: Boolean = false
  ): Future[Unit] =
    myService.checkArgs(extractArguments(commandLine, command), executioner, acceptsTwoCommands).recoverWith {
      case e: IllegalStateException =>
        Future.successful(
          println(
            e.getMessage() + "Please enter 'help' in order to see detailed information about the supported operations."
          )
        )
      case e: RequestFailedException =>
        Future.successful(println("There was an error with the request!" + e.getMessage))
      case e: NoSuchElementException =>
        Future.successful(println("The requested element was not found!" + e.getMessage))
      case e: FileNotFoundException =>
        Future.successful(println("There was an error with the logging file!" + e.getMessage))
      case e: Exception =>
        Future.successful(
          println(
            "Unknown error! Please enter 'help' in order to see detailed information about the supported operations." + e
          )
        )
    }
