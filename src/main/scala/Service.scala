import Service.findsSheetByCommand
import Utils.mkRegex
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.status.StatusLogger
import org.apache.poi.ss.usermodel.{Cell, CellType}
import org.apache.poi.xssf.usermodel.{XSSFRow, XSSFSheet, XSSFWorkbook}
import requests.Response

import java.io.{File, FileInputStream, FileOutputStream}
import scala.::
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.postfixOps
import scala.util.matching.Regex
import scala.util.{Failure, Success}

object Service:

  StatusLogger.getLogger.setLevel(Level.OFF)

  def help(args: List[String]): Future[Unit] = Future.successful(
    Table.plotTable(
      List(
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
    )
  )

  def current(commandArguments: List[String]): Future[Unit] =
    if commandArguments.isEmpty
    then Future.failed(new IllegalStateException("City is a mandatory field."))
    else if !commandArguments(0).matches("[A-Z]+")
    then Future.failed(new IllegalStateException("City cannot contains special symbols and digits."))
    else
      ResponseHandler
        .createParsedCurrent(CommandEnum.Current.value.toLowerCase, commandArguments)
        .map(value =>
          value.writeDataInTable()
          val args = value.parsedValue.myArgs
          Table.plotTable(List(args.map(_._1), args.map(_._2)))
        )

  def forecast(commandArguments: List[String]): Future[Unit] =
    if commandArguments.isEmpty
    then Future.failed(new IllegalStateException("City is a mandatory field."))
    else if !commandArguments(0).matches("[A-Z]+")
    then Future.failed(new IllegalStateException("City cannot contains special symbols and digits."))
    else
      ResponseHandler
        .createParsedForecast(CommandEnum.Forecast.value.toLowerCase, commandArguments)
        .map(value =>
          value.writeDataInTable()
          if value.parsedValue.isEmpty then println("There is no forecast.")
          else
            val args = value.parsedValue(0).myArgs
            Table.plotTable(args.map(_._1) :: value.parsedValue.map(_.myArgs.map(_._2)))
        )

  def astronomy(commandArguments: List[String]): Future[Unit] =
    if commandArguments.isEmpty
    then Future.failed(new IllegalStateException("City is a mandatory field."))
    else if !commandArguments(0).matches("[A-Z]+")
    then Future.failed(new IllegalStateException("City cannot contains special symbols and digits."))
    else
      ResponseHandler
        .createParsedAstronomy(CommandEnum.Astronomy.value.toLowerCase, commandArguments)
        .map(value =>
          value.writeDataInTable()
          val args = value.parsedValue.myArgs
          Table.plotTable(List(args.map(_._1), args.map(_._2)))
        )

  def timezone(commandArguments: List[String]): Future[Unit] =
    if commandArguments.isEmpty
    then Future.failed(new IllegalStateException("City is a mandatory field."))
    else if !commandArguments(0).matches("[A-Z]+")
    then Future.failed(new IllegalStateException("City cannot contains special symbols and digits."))
    else
      ResponseHandler
        .createParsedTimeZone(CommandEnum.Timezone.value.toLowerCase, commandArguments)
        .map(value =>
          value.writeDataInTable()
          val args = value.parsedValue.myArgs
          Table.plotTable(List(args.map(_._1), args.map(_._2)))
        )

  def football(commandArguments: List[String]): Future[Unit] =
    if commandArguments.isEmpty
    then Future.failed(new IllegalStateException("City is a mandatory field."))
    else if !commandArguments(0).matches("[A-Z]+")
    then Future.failed(new IllegalStateException("City cannot contains special symbols and digits."))
    else
      ResponseHandler
        .createParsedFootball(CommandEnum.Football.value.toLowerCase, commandArguments)
        .map(value =>
          if value.parsedValue.isEmpty then println("There are no matches.")
          else
            value.writeDataInTable()
            val args = value.parsedValue(0).myArgs
            Table.plotTable(args.map(_._1) :: value.parsedValue.map(_.myArgs.map(_._2)))
        )

  def printSheet(commandArguments: List[String]): Future[Unit] =
    if commandArguments.isEmpty
    then Future.failed(new IllegalStateException("Command is a mandatory field."))
    else if !commandArguments(0).matches("[A-Z]+")
    then Future.failed(new IllegalStateException("Command cannot contains special symbols and digits."))
    else
      try
        val tuple: (String, List[String] => List[List[Object]]) = findsSheetByCommand(commandArguments(0))
        val sheet = readSheet(tuple._1)
        Future.successful(visualizeSheet(sheet, tuple._2))
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
        val tuple: (String, List[String] => List[List[Object]]) = findsSheetByCommand(commandArguments(0))
        val row = readRow(commandArguments(1).toInt, tuple._1)
        Future.successful(visualizeRow(row, tuple._2))
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

  def readSheet(sheetName: String, workbook: XSSFWorkbook): List[List[String]] =
    val mySheet = workbook.getSheet(sheetName)
    if mySheet == null then throw new NoSuchElementException("There is no history for this command.")
    else List.range(1, mySheet.getLastRowNum + 1).map(readRow(_, mySheet))

  def readSheet(sheetName: String): List[List[String]] =
    val myFile = new File(Utils.FILE_NAME)
    val fis = new FileInputStream(myFile)
    val myWorkbook = new XSSFWorkbook(fis)
    readSheet(sheetName, myWorkbook)

  def readRow(rowNumber: Int, sheet: XSSFSheet): List[String] =
    if sheet.getLastRowNum == -1
    then throw new NoSuchElementException("There is no history for this command!")
    else if sheet.getLastRowNum < rowNumber || rowNumber < 1
    then
      throw new IllegalStateException(
        "Wrong row number! The number must be positive and not higher than the number of the last row."
      )
    else
      val row = sheet.getRow(rowNumber)
      List(
        row.getCell(0).getNumericCellValue.toString,
        row.getCell(1).getStringCellValue,
        row.getCell(2).getStringCellValue,
        row.getCell(3).getStringCellValue
      )

  def readRow(rowNumber: Int, shееtName: String): List[String] =
    val myFile = new File(Utils.FILE_NAME)

    val fis = new FileInputStream(myFile)

    val myWorkbook = new XSSFWorkbook(fis)

    val mySheet = myWorkbook.getSheet(shееtName)
    if mySheet == null then throw new NoSuchElementException("There is no history for this command.")
    else readRow(rowNumber, mySheet)

  def findsSheetByCommand(command: String): (String, List[String] => List[List[Object]]) =
    command match
      case CommandEnum.Current.value => (CommandEnum.Current.value.toLowerCase, jsonToCurrent)
      case CommandEnum.Forecast.value => (CommandEnum.Forecast.value.toLowerCase, jsonToForecast)
      case CommandEnum.Astronomy.value => (CommandEnum.Astronomy.value.toLowerCase, jsonToAstronomy)
      case CommandEnum.Timezone.value => (CommandEnum.Timezone.value.toLowerCase, jsonToTimeZone)
      case CommandEnum.Football.value => (CommandEnum.Football.value.toLowerCase, jsonToFootball) // TODO change
      case _ => throw new IllegalStateException("Wrong command for searching in sheet!")

  def visualizeSheet(sheet: List[List[String]], converter: List[String] => List[List[Object]]): Unit =
    if sheet.isEmpty
    then println("There is no history for this command!")
    else
      val arg: List[List[Object]] = converter(sheet(0))
      Table.plotTable(arg ++ sheet.tail.map(converter).flatMap(_.tail))

  def visualizeRow(row: List[String], converter: List[String] => List[List[Object]]): Unit =
    val args = converter(row)
    Table.plotTable( /*args.map(_._1) +: args.map(_._2)*/ args)

  def jsonToCurrent(row: List[String]): List[List[Object]] =
    val argsList: List[(String, Object)] =
      new JsonParsedCurrent(row(2), Map[String, Seq[String]]()).parsedValue.myArgs
    if argsList.isEmpty
    then throw new NoSuchElementException("There are no matches!")
    else List(argsList.map(_._1), argsList.map(_._2))

  def jsonToAstronomy(row: List[String]): List[List[Object]] =
    val argsList: List[(String, Object)] =
      new JsonParsedAstronomy(row(2), Map[String, Seq[String]]()).parsedValue.myArgs
    if argsList.isEmpty
    then throw new NoSuchElementException("There are no matches!")
    else List(argsList.map(_._1), argsList.map(_._2))

  def jsonToFootball(row: List[String]): List[List[Object]] =
    val argsList: List[List[(String, Object)]] =
      new JsonParsedFootball(row(2), Map[String, Seq[String]]()).parsedValue.map(_.myArgs)
    if argsList.isEmpty
    then throw new NoSuchElementException("There are no matches!")
    else argsList(0).map(_._1) +: argsList.map(_.map(_._2))

  def jsonToForecast(row: List[String]): List[List[Object]] =
    val argsList: List[List[(String, Object)]] =
      new JsonParsedForecast(row(2), Map[String, Seq[String]]()).parsedValue.map(_.myArgs)
    if argsList.isEmpty
    then throw new NoSuchElementException("There are no days in the forecast!")
    else
      println(argsList(0).map(_._1))
      argsList(0).map(_._1) +: argsList.map(_.map(_._2))

  def jsonToTimeZone(row: List[String]): List[List[Object]] =
    val argsList: List[(String, Object)] =
      new JsonParsedTimeZone(row(2), Map[String, Seq[String]]()).parsedValue.myArgs
    if argsList.isEmpty
    then throw new NoSuchElementException("There are no matches!")
    else List(argsList.map(_._1), argsList.map(_._2))
