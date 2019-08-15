import com.micronautics.web3j.Ether
import com.micronautics.web3j.Ether._
import org.scalatest._
import org.scalatest.Matchers._

class EtherTest extends WordSpec {
  val wei: Ether    = Ether(e(0))
  val kWei: Ether   = Ether(e(3))
  val mWei: Ether   = Ether(e(6))
  val gWei: Ether   = Ether(e(9))
  val szabo: Ether  = Ether(e(12))
  val finney: Ether = Ether(e(15))
  val ether: Ether  = Ether(e(18))
  val kEther: Ether = Ether(e(21))
  val mEther: Ether = Ether(e(24))
  val gEther: Ether = Ether(e(27))

  "Ether" should {
    "compare" in {
      wei  shouldBe wei
      kWei should be >  wei
      kWei should be >= wei
      wei  should be <  kWei
      wei  should be <= kWei
    }

    "support math" in {
      wei + wei shouldBe Ether(2)
      wei - 1 shouldBe Ether.zero
      wei - 2 shouldBe Ether(-1)
      wei + 2 shouldBe Ether(3)
      wei * 4 shouldBe Ether(4)

      kWei + kWei shouldBe Ether(e(3) * 2)
      kWei - 1000 shouldBe Ether.zero
      kWei - 2000 shouldBe Ether(e(3) * -1)
      kWei + 2000 shouldBe Ether(e(3) * 3)
      kWei * 4    shouldBe Ether(e(3) * 4)
    }

    "convert properly" in {
      Ether.fromWei(BigInt(1))        shouldBe wei
      Ether.fromWei(BigDecimal(1))    shouldBe wei
      Ether.fromWei(1)                shouldBe wei
      Ether.fromWei(1.0)              shouldBe wei
      Ether.fromWei(1.0)              shouldBe wei
      Ether.fromWei(1)                shouldBe wei

      Ether.fromKWei(BigInt(1))       shouldBe kWei
      Ether.fromKWei(BigDecimal(1))   shouldBe kWei
      Ether.fromKWei(1)               shouldBe kWei
      Ether.fromKWei(1.0)             shouldBe kWei
      Ether.fromKWei(1.0)             shouldBe kWei
      Ether.fromKWei(1)               shouldBe kWei

      Ether.fromMWei(BigInt(1))       shouldBe mWei
      Ether.fromMWei(BigDecimal(1))   shouldBe mWei
      Ether.fromMWei(1)               shouldBe mWei
      Ether.fromMWei(1.0)             shouldBe mWei
      Ether.fromMWei(1.0)             shouldBe mWei
      Ether.fromMWei(1)               shouldBe mWei

      Ether.fromGWei(BigInt(1))       shouldBe gWei
      Ether.fromGWei(BigDecimal(1))   shouldBe gWei
      Ether.fromGWei(1)               shouldBe gWei
      Ether.fromGWei(1.0)             shouldBe gWei
      Ether.fromGWei(1.0)             shouldBe gWei
      Ether.fromGWei(1)               shouldBe gWei

      Ether.fromSzabo(BigInt(1))      shouldBe szabo
      Ether.fromSzabo(BigDecimal(1))  shouldBe szabo
      Ether.fromSzabo(1)              shouldBe szabo
      Ether.fromSzabo(1.0)            shouldBe szabo
      Ether.fromSzabo(1.0)            shouldBe szabo
      Ether.fromSzabo(1)              shouldBe szabo

      Ether.fromFinney(BigInt(1))     shouldBe finney
      Ether.fromFinney(BigDecimal(1)) shouldBe finney
      Ether.fromFinney(1)             shouldBe finney
      Ether.fromFinney(1.0)           shouldBe finney
      Ether.fromFinney(1.0)           shouldBe finney
      Ether.fromFinney(1)             shouldBe finney

      Ether.fromEther(BigInt(1))      shouldBe ether
      Ether.fromEther(BigDecimal(1))  shouldBe ether
      Ether.fromEther(1)              shouldBe ether
      Ether.fromEther(1.0)            shouldBe ether
      Ether.fromEther(1.0)            shouldBe ether
      Ether.fromEther(1)              shouldBe ether

      Ether.fromKEther(BigInt(1))     shouldBe kEther
      Ether.fromKEther(BigDecimal(1)) shouldBe kEther
      Ether.fromKEther(1)             shouldBe kEther
      Ether.fromKEther(1.0)           shouldBe kEther
      Ether.fromKEther(1.0)           shouldBe kEther
      Ether.fromKEther(1)             shouldBe kEther

      Ether.fromMEther(BigInt(1))     shouldBe mEther
      Ether.fromMEther(BigDecimal(1)) shouldBe mEther
      Ether.fromMEther(1)             shouldBe mEther
      Ether.fromMEther(1.0)           shouldBe mEther
      Ether.fromMEther(1.0)           shouldBe mEther
      Ether.fromMEther(1)             shouldBe mEther

      Ether.fromGEther(BigInt(1))     shouldBe gEther
      Ether.fromGEther(BigDecimal(1)) shouldBe gEther
      Ether.fromGEther(1)             shouldBe gEther
      Ether.fromGEther(1.0)           shouldBe gEther
      Ether.fromGEther(1.0)           shouldBe gEther
      Ether.fromGEther(1)             shouldBe gEther
    }
  }
}
