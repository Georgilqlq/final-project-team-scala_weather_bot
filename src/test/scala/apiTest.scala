import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ApiRequest.*

import java.lang


class apiTest extends AnyFlatSpec with Matchers :
  "formingRequestParameters" should "create a Map with the correct arguments for current" in {
    createRequestParameters("current", "123" :: "Ruse" :: Nil) shouldBe Map(
      "key" -> "123", "q" -> "Ruse"
    )
  }

  it should "create a correct Map for astronomy" in {
    createRequestParameters("astronomy", "123" :: "Ruse" :: "Random" :: Nil) shouldBe Map(
      "key" -> "123", "q" -> "Ruse", "dt" -> "Random"
    )
  }


  it should "create a Map for astronomy even when the input has more arguments" in {
    createRequestParameters("astronomy", "123" :: "Ruse" :: "Random" :: "NotWanted" :: Nil) shouldBe Map(
      "key" -> "123", "q" -> "Ruse", "dt" -> "Random"
    )
  }

  it should "create a Map with the correct arguments even when the input has more arguments" in {
    createRequestParameters("current", "123" :: "Ruse" :: "Random" :: Nil) shouldBe Map(
      "key" -> "123", "q" -> "Ruse"
    )
  }

  it should "throw an error if the command is not supported" in{
    the [IllegalStateException] thrownBy createRequestParameters("cricket","123" :: "Plovdiv" :: Nil) should have message "Invalid command"
  }