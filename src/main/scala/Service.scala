import Utils.{
  CITY_CONSTRAINTS_MESSAGE,
  CITY_IS_MANDATORY_MESSAGE,
  HELP_INSTRUCTIONS,
  NO_HISTORY_MESSAGE,
  mkRegex,
  validateCity
}
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.status.StatusLogger
import org.apache.poi.ss.usermodel.{Cell, CellType}
import org.apache.poi.xssf.usermodel.{XSSFRow, XSSFSheet, XSSFWorkbook}
import requests.Response

import java.io.{File, FileInputStream, FileOutputStream}
import scala.::
import scala.beans.BeanProperty
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.postfixOps
import scala.util.matching.Regex
import scala.util.{Failure, Success}

class Service(
  @BeanProperty
  val tableManager: TableManager,
  @BeanProperty
  val tableVisualizer: TableVisualizer
):

  StatusLogger.getLogger.setLevel(Level.OFF)

  def help(args: List[String]): Future[Unit] = Future.successful(Table.plotTable(HELP_INSTRUCTIONS))

  def current(commandArguments: List[String]): Future[Unit] =
    validateCity(
      commandArguments,
      myArguments =>
        ResponseHandler
          .createParsedCurrent(CommandEnum.Current.value.toLowerCase, myArguments)
          .map(value =>
            value.writeDataInTable()
            val args = value.parsedValue.myArgs
            Table.plotTable(List(args.map(_._1), args.map(_._2)))
          )
    )

  def forecast(commandArguments: List[String]): Future[Unit] =
    validateCity(
      commandArguments,
      myArguments =>
        ResponseHandler
          .createParsedForecast(CommandEnum.Forecast.value.toLowerCase, myArguments)
          .map(value =>
            value.writeDataInTable()
            if value.parsedValue.isEmpty then println("There is no forecast.")
            else
              val args = value.parsedValue(0).myArgs
              Table.plotTable(args.map(_._1) :: value.parsedValue.map(_.myArgs.map(_._2)))
          )
    )

  def astronomy(commandArguments: List[String]): Future[Unit] =
    validateCity(
      commandArguments,
      myArguments =>
        ResponseHandler
          .createParsedAstronomy(CommandEnum.Astronomy.value.toLowerCase, myArguments)
          .map(value =>
            value.writeDataInTable()
            val args = value.parsedValue.myArgs
            Table.plotTable(List(args.map(_._1), args.map(_._2)))
          )
    )

  def timezone(commandArguments: List[String]): Future[Unit] =
    validateCity(
      commandArguments,
      myArguments =>
        ResponseHandler
          .createParsedTimeZone(CommandEnum.Timezone.value.toLowerCase, myArguments)
          .map(value =>
            value.writeDataInTable()
            val args = value.parsedValue.myArgs
            Table.plotTable(List(args.map(_._1), args.map(_._2)))
          )
    )

  def football(commandArguments: List[String]): Future[Unit] =
    validateCity(
      commandArguments,
      myArguments =>
        ResponseHandler
          .createParsedFootball(CommandEnum.Football.value.toLowerCase, myArguments)
          .map(value =>
            if value.parsedValue.isEmpty then println("There are no matches.")
            else
              value.writeDataInTable()
              val args = value.parsedValue(0).myArgs
              Table.plotTable(args.map(_._1) :: value.parsedValue.map(_.myArgs.map(_._2)))
          )
    )

  def printSheet(commandArguments: List[String]): Future[Unit] =
    if commandArguments.isEmpty
    then Future.failed(new IllegalStateException("Command is a mandatory field."))
    else if !commandArguments(0).matches("[A-Z]+")
    then Future.failed(new IllegalStateException("Command cannot contains special symbols and digits."))
    else
      try
        val tuple: (String, List[String] => List[List[Object]]) =
          tableManager.findsSheetByCommand(commandArguments(0))
        val sheet = tableManager.readSheet(tuple._1)
        Future.successful(tableVisualizer.visualizeSheet(sheet, tuple._2))
      catch
        case e: Exception =>
          Future.failed(e)

  def printRow(commandArguments: List[String]): Future[Unit] =
    if commandArguments.size < 2
    then Future.failed(new IllegalStateException("Command and row number are mandatory fields."))
    else if !commandArguments(0).matches("[A-Z]+")
    then Future.failed(new IllegalStateException("Command cannot contains special symbols and digits."))
    else
      try
        val tuple: (String, List[String] => List[List[Object]]) =
          tableManager.findsSheetByCommand(commandArguments(0))
        val row = tableManager.readRow(commandArguments(1).toInt, tuple._1)
        Future.successful(tableVisualizer.visualizeRow(row, tuple._2))
      catch
        case e: Exception =>
          Future.failed(e)

  def delete(commandArguments: List[String]): Future[Unit] = Future.successful(deleteAllSheets)

  def deleteAllSheets: Unit =
    val myFile = new File(Utils.FILE_NAME)
    val fis = new FileInputStream(myFile)
    val myWorkbook = new XSSFWorkbook(fis)

    List(CommandEnum.Current, CommandEnum.Forecast, CommandEnum.Astronomy, CommandEnum.Timezone, CommandEnum.Football)
      .map(_.value.toLowerCase)
      .filter(myWorkbook.getSheet(_) != null)
      .filter(myWorkbook.getSheet(_).getLastRowNum >= 1)
      .foreach(name =>
        List
          .range(1, myWorkbook.getSheet(name).getLastRowNum + 1)
          .foreach(rowNumber =>
            val currentSheet: XSSFSheet = myWorkbook.getSheet(name)
            val lastCellNumber: Int = currentSheet.getRow(rowNumber).getLastCellNum
            val removingRow: XSSFRow = currentSheet.getRow(rowNumber)
            currentSheet.removeRow(removingRow)
          )
      )
    val fileOut: FileOutputStream = new FileOutputStream(Utils.FILE_NAME)
    myWorkbook.write(fileOut)
    println("History was successfully deleted!")

  def checkArgs(
    args: List[String],
    executioner: List[String] => Future[Unit],
    acceptsTwoCommands: Boolean
  ): Future[Unit] =
    if (args.filter(CommandEnum.isCommand(_)).size != 0) && !acceptsTwoCommands then
      Future { throw new IllegalStateException("You can enter only one command per time!") }
    else executioner(args)
