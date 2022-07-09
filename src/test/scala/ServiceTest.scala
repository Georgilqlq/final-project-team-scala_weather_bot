import api.ResponseHandler
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar.mock
import service.Service
import table.{TableManager, TableVisualizer}

class ServiceTest extends AnyFlatSpec with Matchers:
  val tableManagerMock: TableManager = mock[TableManager]
  val tableVisualizerMock: TableVisualizer = mock[TableVisualizer]
  val responseHandlerMock: ResponseHandler = mock[ResponseHandler]
  val service: Service = Service(tableManagerMock, tableVisualizerMock, responseHandlerMock)

//  "extractArguments" should "extract the arguments after the command 1" in {
//    service.checkArgs(List("current Sofia 3", "current"), service.current, false) shouldBe Fu
//  }
