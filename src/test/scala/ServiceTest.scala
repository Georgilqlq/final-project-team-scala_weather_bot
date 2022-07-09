import api.ResponseHandler
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{atLeastOnce, times, verify, when}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar.mock
import service.{Console, Service}
import table.{TableManager, TableVisualizer}
import parsing.*

import scala.concurrent.Future

class ServiceTest extends AnyFlatSpec with Matchers:
  val tableManagerMock: TableManager = mock[TableManager]
  val tableVisualizerMock: TableVisualizer = mock[TableVisualizer]
  val responseHandlerMock: ResponseHandler = mock[ResponseHandler]
  val consoleMock: Console = mock[Console]
  val service: Service = Service(tableManagerMock, tableVisualizerMock, responseHandlerMock)

  "checkArgs" should "invoke the given function" in {
    def f(l: List[String]): List[List[Object]] = List(l)
    def g(l: List[String]): Future[Unit] = Future.successful(tableVisualizerMock.visualizeRow(l, f))

    service.checkArgs(
      List("Sofia", "3"),
      g,
      false
    )
    verify(tableVisualizerMock, times(1)).visualizeRow(any(), any())
  }

  "checkArgs" should "invoke the given function when with 2 commands and acceptsTwoCommands is true" in {
    def f(l: List[String]): List[List[Object]] = List(l)
    def g(l: List[String]): Future[Unit] = Future.successful(tableVisualizerMock.visualizeSheet(List(l), f))

    service.checkArgs(
      List("CURRENT", "3", "Sofia"),
      g,
      true
    )
    verify(tableVisualizerMock, times(1)).visualizeSheet(any(), any())
  }

  "current" should "invoke createParsedCurrent" in {
    when(responseHandlerMock.createParsedCurrent(any(), any())).thenReturn(Future.successful(Nil))
    service.current(List("SOFIA", "4"))
    verify(responseHandlerMock, times(1))
      .createParsedCurrent(ArgumentMatchers.eq("current"), ArgumentMatchers.eq(List("SOFIA", "4")))
  }

  "timezone" should "invoke createParsedTimeZone" in {
    when(responseHandlerMock.createParsedTimeZone(any(), any())).thenReturn(Future.successful(Nil))
    service.timezone(List("SOFIA", "4"))
    verify(responseHandlerMock, times(1))
      .createParsedTimeZone(ArgumentMatchers.eq("timezone"), ArgumentMatchers.eq(List("SOFIA", "4")))
  }

  "astronomy" should "invoke createParsedAstronomy" in {
    when(responseHandlerMock.createParsedAstronomy(any(), any())).thenReturn(Future.successful(Nil))
    service.astronomy(List("SOFIA", "4"))
    verify(responseHandlerMock, times(1))
      .createParsedAstronomy(ArgumentMatchers.eq("astronomy"), ArgumentMatchers.eq(List("SOFIA", "4")))
  }

  "football" should "invoke createParsedFootball" in {
    when(responseHandlerMock.createParsedFootball(any(), any())).thenReturn(Future.successful(Nil))
    service.football(List("SOFIA", "4"))
    verify(responseHandlerMock, times(1))
      .createParsedFootball(ArgumentMatchers.eq("football"), ArgumentMatchers.eq(List("SOFIA", "4")))
  }

  "forecast" should "invoke createParsedForecast" in {
    when(responseHandlerMock.createParsedForecast(any(), any())).thenReturn(Future.successful(Nil))
    service.forecast(List("SOFIA", "4"))
    verify(responseHandlerMock, times(1))
      .createParsedForecast(ArgumentMatchers.eq("forecast"), ArgumentMatchers.eq(List("SOFIA", "4")))
  }

  "printSheet" should "invoke findsSheetByCommand" in {
    service.printSheet(List("CURRENT", "4"))
    verify(tableManagerMock)
      .findsSheetByCommand(ArgumentMatchers.eq("CURRENT"))
  }

  "printRow" should "invoke findsSheetByCommand" in {
    service.printRow(List("CURRENT", "4"))
    verify(tableManagerMock, atLeastOnce())
      .findsSheetByCommand(ArgumentMatchers.eq("CURRENT"))
  }

  "delete" should "invoke createParsedForecast" in {
    service.delete(List("SOFIA", "4"))
    verify(tableManagerMock, times(1)).deleteAllSheets
  }
