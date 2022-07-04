import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class tests extends AnyFlatSpec with Matchers:
    "basic" should "return true" in {
      1 + 1 shouldBe 2
    }