package parsing

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.status.StatusLogger
import org.apache.poi.sl.usermodel.VerticalAlignment
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import java.io.{FileInputStream, FileOutputStream}

abstract class JsonParser[A]:
  val rawValue: String
  val parsedValue: A
  val command: String
  val header: Map[String, Seq[String]]

  StatusLogger.getLogger.setLevel(Level.OFF)

  def writeDataInTable(fileName: String = Utils.FILE_PATH): Unit =
    val inputStream = new FileInputStream(fileName)
    val workbook = new XSSFWorkbook(inputStream)
    val sheet =
      workbook.getSheet(command) match
        case null => workbook.createSheet(command)
        case existingSheet => existingSheet
    sheet.setColumnWidth(1, Utils.DATE_WIDTH)
    sheet.setColumnWidth(2, Utils.JSON_WIDTH)

    if sheet.getLastRowNum == -1 then
      val firstRow = sheet.createRow(0)
      val idCellName: Unit = firstRow.createCell(0).setCellValue("ID")
      val dateCellName: Unit = firstRow.createCell(1).setCellValue("Date")
      val jsonCellName: Unit = firstRow.createCell(2).setCellValue("Json Response")
      val commandCellName: Unit = firstRow.createCell(3).setCellValue("Command")

    val lastRowNum = sheet.getLastRowNum + 1
    val row = sheet.createRow(lastRowNum)

    val getDateResponse = header.getOrElse("date", "unidentified")

    val idCell: Unit = row.createCell(0).setCellValue(lastRowNum)
    val dateCell: Unit = row.createCell(1).setCellValue(getDateResponse.toString.substring(4))
    val jsonCell: Unit = row.createCell(2).setCellValue(rawValue)
    val commandCell: Unit = row.createCell(3).setCellValue(command)

    val fileOutput = new FileOutputStream(fileName)
    workbook.write(fileOutput)

    fileOutput.flush()
    fileOutput.close()
    inputStream.close()
    workbook.close()
