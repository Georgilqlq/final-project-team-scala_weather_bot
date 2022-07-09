import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import parsing.JsonToObjectParser
import table.TableManager
import utils.Utils

import java.io.{File, FileInputStream}

class TableManagerTests extends AnyFlatSpec with Matchers:
  val myFile = new File("src/test/resources/test1.xlsx")
  val fis = new FileInputStream(myFile)
  val myWorkbook = new XSSFWorkbook(fis)
  val tableManager: TableManager = new TableManager(new JsonToObjectParser)

  "readSheet" should "return the sheet for current" in {
    tableManager.readSheet("current", myWorkbook) shouldBe List(
      List("1.0", "(Sat, 09 Jul 2022 09:58:02 GMT)", "C2", "current"),
      List("2.0", "(Sat, 09 Jul 2022 09:58:02 GMT)", "C1", "current")
    )
  }

  "readSheet" should "return the sheet for timezone" in {
    tableManager.readSheet("timezone", myWorkbook) shouldBe List(
      List("1.0", "D1", "J1", "timezone"),
      List("2.0", "D2", "J2", "timezone")
    )
  }

  "readSheet" should "return the sheet for astronomy" in {
    tableManager.readSheet("astronomy", myWorkbook) shouldBe List(
      List("1.0", "D1", "J1", "astronomy"),
      List("2.0", "D2", "J2", "astronomy")
    )
  }

  "readSheet" should "return the sheet for football" in {
    tableManager.readSheet("football", myWorkbook) shouldBe List(
      List("1.0", "(Sat, 09 Jul 2022 09:58:02 GMT)", "F1", "football"),
      List("2.0", "(Sat, 09 Jul 2022 09:58:02 GMT)", "F2", "football")
    )
  }

  "readSheet" should "return the sheet for forecast" in {
    tableManager.readSheet("forecast", myWorkbook) shouldBe List(
      List("1.0", "(Sat, 09 Jul 2022 09:58:02 GMT)", "F1", "forecast"),
      List("2.0", "(Sat, 09 Jul 2022 09:58:02 GMT)", "F2", "forecast")
    )
  }

  "readRow" should "return the row for current" in {
    val mySheet = myWorkbook.getSheet("current")
    tableManager.readRow(1, mySheet) shouldBe List("1.0", "(Sat, 09 Jul 2022 09:58:02 GMT)", "C2", "current")
  }

  "readRow" should "return the row for timezone" in {
    val mySheet = myWorkbook.getSheet("timezone")
    tableManager.readRow(1, mySheet) shouldBe List("1.0", "D1", "J1", "timezone")
  }

  "readRow" should "return the row for astronomy" in {
    val mySheet = myWorkbook.getSheet("astronomy")
    tableManager.readRow(1, mySheet) shouldBe List("1.0", "D1", "J1", "astronomy")
  }

  "readRow" should "return the row for football" in {
    val mySheet = myWorkbook.getSheet("football")
    tableManager.readRow(1, mySheet) shouldBe List("1.0", "(Sat, 09 Jul 2022 09:58:02 GMT)", "F1", "football")
  }

  "readRow" should "return the row for forecast" in {
    val mySheet = myWorkbook.getSheet("forecast")
    tableManager.readRow(1, mySheet) shouldBe List("1.0", "(Sat, 09 Jul 2022 09:58:02 GMT)", "F1", "forecast")
  }

  "readRow" should "throw Exception whenindex is too big" in {
    val mySheet = myWorkbook.getSheet("forecast")
    the[IllegalStateException] thrownBy tableManager.readRow(
      5,
      mySheet
    ) should have message "Wrong row number! The number must be positive and not higher than the number of the last row."
  }

  "readRow" should "throw Exception when index is negative" in {
    val mySheet = myWorkbook.getSheet("forecast")
    the[IllegalStateException] thrownBy tableManager.readRow(
      -11,
      mySheet
    ) should have message "Wrong row number! The number must be positive and not higher than the number of the last row."
  }

  "findsSheetByCommand" should "return current" in {
    tableManager.findsSheetByCommand("CURRENT")._1 shouldBe "current"
  }
  "findsSheetByCommand" should "return timezone" in {
    tableManager.findsSheetByCommand("TIMEZONE")._1 shouldBe "timezone"
  }
  "findsSheetByCommand" should "return forecast" in {
    tableManager.findsSheetByCommand("FORECAST")._1 shouldBe "forecast"
  }
  "findsSheetByCommand" should "return football" in {
    tableManager.findsSheetByCommand("FOOTBALL")._1 shouldBe "football"
  }
  "findsSheetByCommand" should "return astronomy" in {
    tableManager.findsSheetByCommand("ASTRONOMY")._1 shouldBe "astronomy"
  }

  "findsSheetByCommand" should "throw IllegalStateException when invoked with wrong command" in {
    the[IllegalStateException] thrownBy tableManager.findsSheetByCommand(
      "WRONGCOMMAND"
    ) should have message "Wrong command for searching in sheet!"
  }
