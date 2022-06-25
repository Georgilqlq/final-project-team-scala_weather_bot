import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.read
import requests.Response

import java.io.{FileInputStream, FileOutputStream}

//import org.apache.spark.sql.SparkSession

import javax.swing.text.DefaultFormatter

case class Location(
                     name: String,
                     region: String,
                     country: String,
                     lat: Double,
                     lon: Double,
                     tz_id: String,
                     localtime_epoch: Int,
                     localtime: String
                   ):
  def myArgs: List[(String, Object)] =
    this.productElementNames.toList
      .map(i => i -> this.getClass.getDeclaredField(i).get(this))

case class Current(
                    last_updated_epoch: Int,
                    last_updated: String,
                    temp_c: Double,
                    temp_f: Double,
                    is_day: Int,
                    condition: Condition,
                    wind_mph: Double,
                    wind_kph: Double,
                    wind_degree: Int,
                    wind_dir: String,
                    pressure_mb: Double,
                    pressure_in: Double,
                    precip_mm: Double,
                    precip_in: Double,
                    humidity: Int,
                    cloud: Int,
                    feelslike_c: Double,
                    feelslike_f: Double,
                    vis_km: Double,
                    vis_miles: Double,
                    uv: Double,
                    gust_mph: Double,
                    gust_kph: Double
                  ):
  def myArgs: List[(String, Object)] =
    this.condition.myArgs ++ this.productElementNames.toList
      .filter(!_.equals("condition"))
      .map(i => i -> this.getClass.getDeclaredField(i).get(this))

case class Condition(text: String, icon: String, code: Int):
  def myArgs: List[(String, Object)] =
    this.productElementNames.toList
      .map(i => i -> this.getClass.getDeclaredField(i).get(this))

case class CurrentResult(location: Location, current: Current):
  def myArgs: List[(String, Object)] = this.current.myArgs ++ this.location.myArgs

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

class JsonParsedCurrent(override val rawValue: String,
                        override val header: Map[String, Seq[String]]) extends JsonParser[CurrentResult] :
  val parsedValue: CurrentResult =
    implicit lazy val formats: Formats = DefaultFormats

    val json = parse(rawValue)
    json.extract[CurrentResult]
  val command: String = "current"

case class Astro(
                  sunrise: String,
                  sunset: String,
                  moonrise: String,
                  moonset: String,
                  moon_phase: String,
                  moon_illumination: String
                ):
  def myArgs: List[(String, Object)] =
    this.productElementNames.toList
      .map(i => i -> this.getClass.getDeclaredField(i).get(this))

case class Astronomy(astro: Astro):
  def myArgs: List[(String, Object)] = this.astro.myArgs

case class AstronomyResult(location: Location, astronomy: Astronomy):
  def myArgs: List[(String, Object)] = this.location.myArgs ++ this.astronomy.myArgs

class JsonParsedAstronomy(override val rawValue: String,
                          override val header: Map[String, Seq[String]]) extends JsonParser[AstronomyResult] :
  val parsedValue: AstronomyResult =
    implicit lazy val formats: Formats = DefaultFormats

    val json = parse(rawValue)
    json.extract[AstronomyResult]
  val command: String = "astronomy"

class JsonParsedTimeZone(override val rawValue: String,
                         override val header: Map[String, Seq[String]]) extends JsonParser[Location] :
  val parsedValue: Location =
    implicit lazy val formats: Formats = DefaultFormats

    val json = parse(rawValue)
    json.extract[Location]
  val command: String = "timezone"

case class Matches(
                    stadium: String,
                    country: String,
                    region: String,
                    tournament: String,
                    start: String,
                    `match`: String
                  ):
  def myArgs: List[(String, Object)] =
    this.productElementNames.toList
      .map(i => i -> this.getClass.getDeclaredField(i).get(this))

class JsonParsedFootball(override val rawValue: String,
                         override val header: Map[String, Seq[String]]) extends JsonParser[List[Matches]] :
  val parsedValue: List[Matches] =
    implicit lazy val formats: Formats = DefaultFormats

    val json = parse(rawValue) \ "football"
    json.extract[List[Matches]]
  val command: String = "football"

case class Hour(
                 time_epoch: Int,
                 time: String,
                 temp_c: Double,
                 temp_f: Double,
                 is_day: Int,
                 condition: Condition,
                 wind_mph: Double,
                 wind_kph: Double,
                 wind_degree: Int,
                 wind_dir: String,
                 pressure_mb: Double,
                 pressure_in: Double,
                 precip_mm: Double,
                 precip_in: Double,
                 humidity: Int,
                 cloud: Int,
                 feelslike_c: Double,
                 feelslike_f: Double,
                 windchill_c: Double,
                 windchill_f: Double,
                 heatindex_c: Double,
                 heatindex_f: Double,
                 dewpoint_c: Double,
                 dewpoint_f: Double,
                 will_it_rain: Int,
                 chance_of_rain: Int,
                 will_it_snow: Int,
                 chance_of_snow: Int,
                 vis_km: Double,
                 vis_miles: Double,
                 gust_mph: Double,
                 gust_kph: Double,
                 uv: Double
               ):
  def myArgs: List[(String, Object)] =
    this.condition.myArgs ++ this.productElementNames.toList
      .filter(!_.equals("condition"))
      .map(i => i -> this.getClass.getDeclaredField(i).get(this))

class JsonParsedForecast(override val rawValue: String,
                         override val header: Map[String, Seq[String]]) extends JsonParser[List[Hour]] :
  val parsedValue: List[Hour] =
    implicit lazy val formats: Formats = DefaultFormats

    val json = parse(rawValue) \\ "hour"
    json.extract[List[Hour]]
  val command: String = "forecast"

  def temperatures: List[Double] = parsedValue.map(_.temp_c)

  def hours: List[String] = parsedValue.map(_.time)
