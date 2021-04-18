import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import ModelSelection._

class ResultSpec extends AnyFlatSpec with Matchers{

  behavior of "best model"
  it should "work for accuracy > 70%" in {
    testAccuracy > 0.7
  }
}
