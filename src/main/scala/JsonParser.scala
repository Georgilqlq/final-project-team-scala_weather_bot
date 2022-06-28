import org.apache.poi.xssf.usermodel.XSSFWorkbook

import java.io.{FileInputStream, FileOutputStream}

abstract class JsonParser[A]:
  val rawValue: String
  val parsedValue: A
  val command: String
  val header: Map[String, Seq[String]]

  def writeDataInTable(): Unit =
    val inputStream = new FileInputStream(" ..\\..\\test.xlsx")
    val workbook = new XSSFWorkbook(inputStream)
    val sheet =
      workbook.getSheet(command) match
        case null => workbook.createSheet(command)
        case existingSheet => existingSheet
    val lastRowNum = sheet.getLastRowNum + 1
    val row = sheet.createRow(lastRowNum)

    val getDateResponse = header.getOrElse("date", "unidentified")

    val idCell: Unit = row.createCell(0).setCellValue(lastRowNum)
    val dateCell: Unit = row.createCell(1).setCellValue(getDateResponse.toString)
    val jsonCell: Unit = row.createCell(2).setCellValue(rawValue)
    val commandCell: Unit = row.createCell(3).setCellValue(command)

    val fileOutput = new FileOutputStream(" ..\\..\\test.xlsx")
    workbook.write(fileOutput)
    fileOutput.flush()
    fileOutput.close()
    inputStream.close()
    workbook.close()
