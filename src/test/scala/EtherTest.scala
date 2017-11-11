import com.micronautics.web3j.Ether
import com.micronautics.web3j.Ether.bigDecimal
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._
import org.scalatest.Matchers._

@RunWith(classOf[JUnitRunner])
class EtherTest extends WordSpec {
  "Ether" should {
    "convert properly" in {
      Ether.fromWei(BigInt(1))    shouldBe Ether(BigInt(1))
      Ether.fromWei(BigInt(1))    shouldBe Ether(1)
      Ether.fromWei(1)            shouldBe Ether(1)
      Ether.fromWei(1.0)          shouldBe Ether(1)
      Ether.fromWei(1.0)          shouldBe Ether(1.0)
      Ether.fromWei(1)            shouldBe Ether(1.0)

      Ether.fromKWei(BigInt(1))   shouldBe Ether(BigInt(1000))
      Ether.fromKWei(BigInt(1))   shouldBe Ether(1000)
      Ether.fromKWei(1)           shouldBe Ether(1000)
      Ether.fromKWei(1.0)         shouldBe Ether(1000)
      Ether.fromKWei(1.0)         shouldBe Ether(1000.0)
      Ether.fromKWei(1)           shouldBe Ether(1000.0)

      Ether.fromMWei(BigInt(1))   shouldBe Ether(BigInt(1000 * 1000))
      Ether.fromMWei(BigInt(1))   shouldBe Ether(1000 * 1000)
      Ether.fromMWei(1)           shouldBe Ether(1000 * 1000)
      Ether.fromMWei(1.0)         shouldBe Ether(1000 * 1000)
      Ether.fromMWei(1.0)         shouldBe Ether(1000.0 * 1000.0)
      Ether.fromMWei(1)           shouldBe Ether(1000.0 * 1000.0)

      Ether.fromGWei(BigInt(1))   shouldBe Ether(BigInt(1000 * 1000 * 1000))
      Ether.fromGWei(BigInt(1))   shouldBe Ether(1000 * 1000 * 1000)
      Ether.fromGWei(1)           shouldBe Ether(1000 * 1000 * 1000)
      Ether.fromGWei(1.0)         shouldBe Ether(1000 * 1000 * 1000)
      Ether.fromGWei(1.0)         shouldBe Ether(1000.0 * 1000.0 * 1000.0)
      Ether.fromGWei(1)           shouldBe Ether(1000.0 * 1000.0 * 1000.0)

      Ether.fromSzabo(BigInt(1))  shouldBe Ether(BigInt(1000L) * 1000L * 1000L * 1000L)
      Ether.fromSzabo(BigInt(1))  shouldBe Ether(1000L * 1000L * 1000L * 1000L)
      Ether.fromSzabo(1)          shouldBe Ether(1000L * 1000L * 1000L * 1000L)
      Ether.fromSzabo(1.0)        shouldBe Ether(1000L * 1000L * 1000L * 1000L)
      Ether.fromSzabo(1.0)        shouldBe Ether(1000.0 * 1000.0 * 1000.0 * 1000.0)
      Ether.fromSzabo(1)          shouldBe Ether(1000.0 * 1000.0 * 1000.0 * 1000.0)

      Ether.fromFinney(BigInt(1)) shouldBe Ether(BigInt(1000L) * 1000L * 1000L * 1000L * 1000L)
      Ether.fromFinney(BigInt(1)) shouldBe Ether(1000L * 1000L * 1000L * 1000L * 1000L)
      Ether.fromFinney(1)         shouldBe Ether(1000L * 1000L * 1000L * 1000L * 1000L)
      Ether.fromFinney(1.0)       shouldBe Ether(1000L * 1000L * 1000L * 1000L * 1000L)
      Ether.fromFinney(1.0)       shouldBe Ether(1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0)
      Ether.fromFinney(1)         shouldBe Ether(1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0)

      Ether.fromEther(BigInt(1))  shouldBe Ether(BigInt(1000L) * 1000L * 1000L * 1000L * 1000L * 1000L)
      Ether.fromEther(BigInt(1))  shouldBe Ether(1000L * 1000L * 1000L * 1000L * 1000L * 1000L)
      Ether.fromEther(1)          shouldBe Ether(1000L * 1000L * 1000L * 1000L * 1000L * 1000L)
      Ether.fromEther(1.0)        shouldBe Ether(1000L * 1000L * 1000L * 1000L * 1000L * 1000L)
      Ether.fromEther(1.0)        shouldBe Ether(1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0)
      Ether.fromEther(1)          shouldBe Ether(1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0)

      Ether.fromMEther(BigInt(1)) shouldBe Ether(BigInt(1000L) * 1000L * 1000L * 1000L * 1000L * 1000L * 1000L)
      Ether.fromMEther(BigInt(1)) shouldBe Ether(1000L * 1000L * 1000L * 1000L * 1000L * 1000L * 1000L)
      Ether.fromMEther(1)         shouldBe Ether(1000L * 1000L * 1000L * 1000L * 1000L * 1000L * 1000L)
      Ether.fromMEther(1.0)       shouldBe Ether(1000L * 1000L * 1000L * 1000L * 1000L * 1000L * 1000L)
      Ether.fromMEther(1.0)       shouldBe Ether(1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0)
      Ether.fromMEther(1)         shouldBe Ether(1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0)

      Ether.fromGEther(BigInt(1)) shouldBe Ether(BigInt(1000L) * 1000L * 1000L * 1000L * 1000L * 1000L * 1000L * 1000L)
      Ether.fromGEther(BigInt(1)) shouldBe Ether(1000L * 1000L * 1000L * 1000L * 1000L * 1000L * 1000L * 1000L)
      Ether.fromGEther(1)         shouldBe Ether(1000L * 1000L * 1000L * 1000L * 1000L * 1000L * 1000L * 1000L)
      Ether.fromGEther(1.0)       shouldBe Ether(1000L * 1000L * 1000L * 1000L * 1000L * 1000L * 1000L * 1000L)
      Ether.fromGEther(1.0)       shouldBe Ether(1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0)
      Ether.fromGEther(1)         shouldBe Ether(1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0 * 1000.0)
    }
  }
}
