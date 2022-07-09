import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import api.ApiRequest.*

import java.lang

class ApiTest extends AnyFlatSpec with Matchers:
  "formingRequestParameters" should "create a Map with the correct arguments for current" in {
    createRequestParameters("current", "123" :: "Ruse" :: Nil) shouldBe Map(
      "key" -> "123",
      "q" -> "Ruse"
    )
  }

  it should "create a correct Map for astronomy" in {
    createRequestParameters("astronomy", "123" :: "Ruse" :: "Random" :: Nil) shouldBe Map(
      "key" -> "123",
      "q" -> "Ruse",
      "dt" -> "Random"
    )
  }

  it should "create a Map for astronomy even when the input has more arguments" in {
    createRequestParameters("astronomy", "123" :: "Ruse" :: "Random" :: "NotWanted" :: Nil) shouldBe Map(
      "key" -> "123",
      "q" -> "Ruse",
      "dt" -> "Random"
    )
  }

  it should "create a Map with the correct arguments even when the input has more arguments" in {
    createRequestParameters("current", "123" :: "Ruse" :: "Random" :: Nil) shouldBe Map(
      "key" -> "123",
      "q" -> "Ruse"
    )
  }

  it should "throw an error if the command is not supported" in {
    the[IllegalStateException] thrownBy createRequestParameters(
      "cricket",
      "123" :: "Plovdiv" :: Nil
    ) should have message "Invalid command"
  }

  "createUrlString" should "append the command football to the url" in {
    createUrlString("football") shouldBe weather_api ++ "/sports.json"
  }

  it should "append any command to the url" in {
    val command = "something"
    createUrlString(command) shouldBe weather_api ++ s"/$command.json"
  }

  "makeApiRequest" should "create a failed Request - invalid command" in {
    the[IllegalStateException] thrownBy makeApiRequest("invalidCommand", Nil) should have message "Invalid command"
  }

  it should "create a invalid Request - invalid arguments" in {
    val testRequest = makeApiRequest("current", Nil)
    testRequest.statusCode shouldBe 401
  }
