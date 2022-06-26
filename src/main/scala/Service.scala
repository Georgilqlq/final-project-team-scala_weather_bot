import Utils.mkRegex
import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}
import requests.Response

import java.io.{File, FileInputStream}
import scala.::
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.matching.Regex
import scala.util.{Failure, Success}

object Service:
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
        val sheet = readSheet(findsSheetByCommand(commandArguments(0)))
        Future.successful(visualizeSheet(sheet))
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
        val row = readRow(findsSheetByCommand(commandArguments(0)), commandArguments(1).toInt)
        Future.successful(visualizeRow(row))
      catch
        case e: Exception =>
          Future.failed(e)

  def delete(commandArguments: List[String]): Future[Unit] = Future.successful(deleteAllSheets)

  def deleteAllSheets: Unit =
    val myFile = new File("test.xlsx")
    val fis = new FileInputStream(myFile)
    val myWorkbook = new XSSFWorkbook(fis)
    List
      .range(0, 2)
      .foreach(n =>
        List
          .range(1, myWorkbook.getSheetAt(n).getLastRowNum)
          .foreach(x =>
            myWorkbook.getSheetAt(n).removeRow(myWorkbook.getSheetAt(n).getRow(x))
          ) // TODO fix - cannot delete sheet
      )
    println("History was successfully deleted!")

  def checkArgs(args: List[String], executioner: List[String] => Future[Unit]): Future[Unit] =
    if args.filter(CommandEnum.isCommand(_)).size != 0 then
      Future { throw new IllegalStateException("You can enter only one command per time!") }
    else executioner(args)

  def readAllSheets(): List[List[List[String]]] =
    val myFile = new File("test.xlsx")

    val fis = new FileInputStream(myFile)

    val myWorkbook = new XSSFWorkbook(fis)

    List.range(1, 3).map(readSheet(_, myWorkbook)) // TODO change sheets count

  def readSheet(sheetNumber: Int, workbook: XSSFWorkbook): List[List[String]] =
    val mySheet = workbook.getSheetAt(sheetNumber)
    List.range(1, mySheet.getLastRowNum).map(readRow(_, mySheet))

  def readSheet(sheetNumber: Int): List[List[String]] =
    val myFile = new File("test.xlsx")
    val fis = new FileInputStream(myFile)
    val myWorkbook = new XSSFWorkbook(fis)
    readSheet(sheetNumber, myWorkbook)

  def readRow(rowNumber: Int, sheet: XSSFSheet): List[String] =
    val row = sheet.getRow(rowNumber)
    List(
      row.getCell(0),
      row.getCell(1),
      row.getCell(2),
      row.getCell(3)
    ).map(_.getStringCellValue)

  def readRow(rowNumber: Int, shееtNumber: Int): List[String] =
    val myFile = new File("test.xlsx")

    val fis = new FileInputStream(myFile)

    val myWorkbook = new XSSFWorkbook(fis)

    val mySheet = myWorkbook.getSheetAt(shееtNumber)

    readRow(rowNumber, mySheet)

  def findsSheetByCommand(command: String): Int = command match
    case "current" => 1
    case "forecast" => 2
    case "astronomyR" => 3
    case "timezone" => 4
    case "football" => 5
    case _ => throw new IllegalStateException("Wrong command for searching in sheet!")

  def visualizeSheet(sheet: List[List[String]]): Unit = ???
  def visualizeRow(row: List[String]): Unit = ???
//    List(List("id", row(0)), List("command", row(1))) :: row.last

//  def stringToCurrentResult(json: String): CurrentResult = JsonParsedCurrent(
//    Response("", 0, "", null, null, Some(json))
//  )
