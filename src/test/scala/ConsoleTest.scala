import org.mockito.ArgumentMatchers.any
import org.mockito.{ArgumentCaptor, ArgumentMatchers, Mockito}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar.mock
import org.mockito.Mockito.{times, verify, when}
import requests.{RequestFailedException, Response}
import service.{Console, Service}

import java.io.FileNotFoundException
import scala.concurrent.Future
import scala.language.postfixOps

class ConsoleTest extends AnyFlatSpec with Matchers:
  val serviceMock = mock[Service]
  val console: Console = new Console(serviceMock)

  "extractArguments" should "extract the arguments after the command 1" in {
    console.extractArguments("current Sofia 3", "current") shouldBe List("Sofia", "3")
  }

  "extractArguments" should "extract the arguments after the command 2" in {
    console.extractArguments("forecast current Sofia 3", "current") shouldBe List("Sofia", "3")
  }

  "extractArguments" should "extract the arguments after the command 3" in {
    console
      .extractArguments("forecast current Sofia 3 4 5", "forecast") shouldBe List("current", "Sofia", "3", "4", "5")
  }

  "continue" should "should return true with current" in {
    val serviceMock = mock[Service]
    val console: Console = new Console(serviceMock)

    when(serviceMock.checkArgs(any(), any(), any())).thenReturn(Future.successful {})

    console.continue("CURRENT SOFIA PARIS 13") shouldBe true

    verify(serviceMock, times(1)).checkArgs(
      ArgumentMatchers.eq(List("SOFIA", "PARIS", "13")),
      any(),
      ArgumentMatchers.eq(false)
    )
  }

  "continue" should "should return true with forecast" in {
    val serviceMock = mock[Service]
    val console: Console = new Console(serviceMock)

    when(serviceMock.checkArgs(any(), any(), any())).thenReturn(Future.successful {})

    console.continue("FORECAST SOFIA PARIS 13") shouldBe true

    verify(serviceMock, times(1)).checkArgs(
      ArgumentMatchers.eq(List("SOFIA", "PARIS", "13")),
      any(),
      ArgumentMatchers.eq(false)
    )
  }

  "continue" should "should return true with astronomy" in {
    val serviceMock = mock[Service]
    val console: Console = new Console(serviceMock)

    when(serviceMock.checkArgs(any(), any(), any())).thenReturn(Future.successful {})

    console.continue("ASTRONOMY SOFIA PARIS 13") shouldBe true

    verify(serviceMock, times(1)).checkArgs(
      ArgumentMatchers.eq(List("SOFIA", "PARIS", "13")),
      any(),
      ArgumentMatchers.eq(false)
    )
  }

  "continue" should "should return true with timezone" in {
    val serviceMock = mock[Service]
    val console: Console = new Console(serviceMock)

    when(serviceMock.checkArgs(any(), any(), any())).thenReturn(Future.successful {})

    console.continue("TIMEZONE SOFIA PARIS 13") shouldBe true

    verify(serviceMock, times(1)).checkArgs(
      ArgumentMatchers.eq(List("SOFIA", "PARIS", "13")),
      any(),
      ArgumentMatchers.eq(false)
    )
  }

  "continue" should "should return true with football" in {
    val serviceMock = mock[Service]
    val console: Console = new Console(serviceMock)

    when(serviceMock.checkArgs(any(), any(), any())).thenReturn(Future.successful {})

    console.continue("FOOTBALL SOFIA PARIS 13") shouldBe true

    verify(serviceMock, times(1)).checkArgs(
      ArgumentMatchers.eq(List("SOFIA", "PARIS", "13")),
      any(),
      ArgumentMatchers.eq(false)
    )
  }

  "continue" should "should return true with read-sheet" in {
    val serviceMock = mock[Service]
    val console: Console = new Console(serviceMock)

    when(serviceMock.checkArgs(any(), any(), any())).thenReturn(Future.successful {})

    console.continue("READ-SHEET CURRENT") shouldBe true

    verify(serviceMock, times(1)).checkArgs(
      ArgumentMatchers.eq(List("CURRENT")),
      any(),
      ArgumentMatchers.eq(true)
    )
  }

  "continue" should "should return true with delete" in {
    val serviceMock = mock[Service]
    val console: Console = new Console(serviceMock)

    when(serviceMock.checkArgs(any(), any(), any())).thenReturn(Future.successful {})

    console.continue("DELETE CURRENT") shouldBe true

    verify(serviceMock, times(1)).checkArgs(
      ArgumentMatchers.eq(List()),
      any(),
      ArgumentMatchers.eq(false)
    )
  }

  "continue" should "should return true with read-row" in {
    val serviceMock = mock[Service]
    val console: Console = new Console(serviceMock)

    when(serviceMock.checkArgs(any(), any(), any())).thenReturn(Future.successful {})

    console.continue("READ-ROW FORECAST 3") shouldBe true

    verify(serviceMock, times(1)).checkArgs(
      ArgumentMatchers.eq(List("FORECAST", "3")),
      any(),
      ArgumentMatchers.eq(true)
    )
  }

  "continue" should "should return false with exit" in {
    val serviceMock = mock[Service]
    val console: Console = new Console(serviceMock)

    when(serviceMock.checkArgs(any(), any(), any())).thenReturn(Future.successful {})

    console.continue("CURRENT EXIT PARIS 13") shouldBe false

    verify(serviceMock, times(0)).checkArgs(any(), any(), any())
  }

  "continue" should "should return true with help" in {
    val serviceMock = mock[Service]
    val console: Console = new Console(serviceMock)

    when(serviceMock.checkArgs(any(), any(), any())).thenReturn(Future.successful {})

    console.continue("CURRENT HELP PARIS 13") shouldBe true

    verify(serviceMock, times(1)).checkArgs(
      ArgumentMatchers.eq(List("PARIS", "13")),
      any(),
      ArgumentMatchers.eq(false)
    )
  }

  "continue" should "should return true with other" in {
    val serviceMock = mock[Service]
    val console: Console = new Console(serviceMock)

    when(serviceMock.checkArgs(any(), any(), any())).thenReturn(Future.successful {})

    console.continue("OTHERCOMMAND SOFIA PARIS 13") shouldBe true

    verify(serviceMock, times(0)).checkArgs(any(), any(), any())
  }

  "caller" should "should return future successful" in {
    val serviceMock = mock[Service]
    val console: Console = new Console(serviceMock)

    when(serviceMock.checkArgs(any(), any(), any())).thenReturn(Future.successful {})

    console.caller("CURRENT SOFIA", "CURRENT", serviceMock.current, false)

    verify(serviceMock, times(1)).checkArgs(
      ArgumentMatchers.eq(List("SOFIA")),
      any(),
      ArgumentMatchers.eq(false)
    )
  }

  "caller" should "should return future successful when checkArgs returns NoSuchElementException" in {
    val serviceMock = mock[Service]
    val console: Console = new Console(serviceMock)

    when(serviceMock.checkArgs(any(), any(), any())).thenReturn(Future.failed(new NoSuchElementException("Exception")))

    console.caller("CURRENT SOFIA", "CURRENT", serviceMock.current, false)

    verify(serviceMock, times(1)).checkArgs(
      ArgumentMatchers.eq(List("SOFIA")),
      any(),
      ArgumentMatchers.eq(false)
    )
  }

  "caller" should "should return future successful when checkArgs returns FileNotFoundException" in {
    val serviceMock = mock[Service]
    val console: Console = new Console(serviceMock)

    when(serviceMock.checkArgs(any(), any(), any())).thenReturn(Future.failed(new FileNotFoundException("Exception")))

    console.caller("CURRENT SOFIA", "CURRENT", serviceMock.current, false)

    verify(serviceMock, times(1)).checkArgs(
      ArgumentMatchers.eq(List("SOFIA")),
      any(),
      ArgumentMatchers.eq(false)
    )
  }

  "caller" should "should return future successful when checkArgs returns Exception" in {
    val serviceMock = mock[Service]
    val console: Console = new Console(serviceMock)

    when(serviceMock.checkArgs(any(), any(), any())).thenReturn(Future.failed(new Exception("Exception")))

    console.caller("CURRENT SOFIA", "CURRENT", serviceMock.current, false)

    verify(serviceMock, times(1)).checkArgs(
      ArgumentMatchers.eq(List("SOFIA")),
      any(),
      ArgumentMatchers.eq(false)
    )
  }
