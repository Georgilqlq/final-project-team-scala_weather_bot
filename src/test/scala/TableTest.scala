import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import table.*

class TableTest extends AnyFlatSpec with Matchers:

  "format" should "return formatted response" in {
    Table
      .format(
        List(
          List("1", "2", "3", "4"),
          List("A1", "A2", "A3", "A4"),
          List("B1", "B2", "B3", "B4"),
          List("C1", "C2", "C3", "C4"),
          List("D1", "D2", "D3", "D4")
        )
      ) shouldBe "+--+--+--+--+\n| 1| 2| 3| 4|\n+--+--+--+--+\n|A1|A2|A3|A4|\n|B1|B2|B3|B4|\n|C1|C2|C3|C4|\n|D1|D2|D3|D4|\n+--+--+--+--+"
  }
