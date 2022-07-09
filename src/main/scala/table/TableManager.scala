package table

import utils.Utils.NO_HISTORY_MESSAGE
import enums.CommandEnum
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.status.StatusLogger
import org.apache.poi.xssf.usermodel.{XSSFRow, XSSFSheet, XSSFWorkbook}
import parsing.JsonToObjectParser
import utils.Utils

import java.io.{File, FileInputStream, FileOutputStream}
import scala.beans.BeanProperty

class TableManager(
  @BeanProperty
  val jsonToObjectParser: JsonToObjectParser
):
  StatusLogger.getLogger.setLevel(Level.OFF)

  def readSheet(sheetName: String, workbook: XSSFWorkbook): List[List[String]] =
    val mySheet = workbook.getSheet(sheetName)
    if mySheet == null then throw new NoSuchElementException(NO_HISTORY_MESSAGE)
    else List.range(1, mySheet.getLastRowNum + 1).map(readRow(_, mySheet))

  def readSheet(sheetName: String): List[List[String]] =
    val myFile = new File(Utils.FILE_PATH)
    val fis = new FileInputStream(myFile)
    val myWorkbook = new XSSFWorkbook(fis)
    readSheet(sheetName, myWorkbook)

  def readRow(rowNumber: Int, sheet: XSSFSheet): List[String] =
    if sheet.getLastRowNum == -1
    then throw new NoSuchElementException(NO_HISTORY_MESSAGE)
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
    val myFile = new File(Utils.FILE_PATH)

    val fis = new FileInputStream(myFile)

    val myWorkbook = new XSSFWorkbook(fis)

    val mySheet = myWorkbook.getSheet(shееtName)
    if mySheet == null then throw new NoSuchElementException(NO_HISTORY_MESSAGE)
    else readRow(rowNumber, mySheet)

  def findsSheetByCommand(command: String): (String, List[String] => List[List[Object]]) =
    command match
      case CommandEnum.Current.value => (CommandEnum.Current.value.toLowerCase, jsonToObjectParser.jsonToCurrent)
      case CommandEnum.Forecast.value => (CommandEnum.Forecast.value.toLowerCase, jsonToObjectParser.jsonToForecast)
      case CommandEnum.Astronomy.value =>
        (CommandEnum.Astronomy.value.toLowerCase, jsonToObjectParser.jsonToAstronomy)
      case CommandEnum.Timezone.value => (CommandEnum.Timezone.value.toLowerCase, jsonToObjectParser.jsonToTimeZone)
      case CommandEnum.Football.value => (CommandEnum.Football.value.toLowerCase, jsonToObjectParser.jsonToFootball)
      case _ => throw new IllegalStateException("Wrong command for searching in sheet!")

  def deleteAllSheets: Unit =
    val myFile = new File(Utils.FILE_PATH)
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
    val fileOut: FileOutputStream = new FileOutputStream(Utils.FILE_PATH)
    myWorkbook.write(fileOut)
    println("History was successfully deleted!")
