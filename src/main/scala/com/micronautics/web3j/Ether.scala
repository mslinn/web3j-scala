package com.micronautics.web3j

object Ether {
  /** @param value unit is wei */
  @inline def apply(value: String):          Ether = Ether(BigInt(value).bigInteger)
  @inline def apply(value: BigInt):          Ether = Ether(value.bigInteger)
  @inline def apply(value: Int):             Ether = Ether(BigInt(value).bigInteger)
  @inline def apply(value: Double):          Ether = Ether(BigDecimal(value).bigDecimal.toBigInteger)
  @inline def apply(value: BigDecimal):      Ether = Ether(value.bigDecimal.toBigInteger)

  @inline def fromWei(value: String):        Ether = Ether(BigInt(value))
  @inline def fromWei(value: Double):        Ether = Ether(value)
  @inline def fromWei(value: BigDecimal):    Ether = Ether(value)
  @inline def fromWei(value: Int):           Ether = Ether(bigDecimal(value))
  @inline def fromWei(value: BigInt):        Ether = Ether(bigDecimal(value))

  @inline def fromKWei(value: String):       Ether = Ether(BigInt(value) * 1000)
  @inline def fromKWei(value: Double):       Ether = Ether(value * 1000)
  @inline def fromKWei(value: BigDecimal):   Ether = Ether(value * 1000)
  @inline def fromKWei(value: Int):          Ether = Ether(bigDecimal(value * 1000))
  @inline def fromKWei(value: BigInt):       Ether = Ether(bigDecimal(value * 1000))

  @inline def fromMWei(value: String):       Ether = Ether(BigInt(value) * 1000 * 1000)
  @inline def fromMWei(value: Double):       Ether = Ether(value * 1000 * 1000)
  @inline def fromMWei(value: BigDecimal):   Ether = Ether(value * 1000 * 1000)
  @inline def fromMWei(value: Int):          Ether = Ether(bigDecimal(value * 1000 * 1000))
  @inline def fromMWei(value: BigInt):       Ether = Ether(bigDecimal(value * 1000 * 1000))

  @inline def fromGWei(value: String):       Ether = Ether(BigInt(value) * 1000 * 1000 * 1000)
  @inline def fromGWei(value: Double):       Ether = Ether(value * 1000 * 1000 * 1000)
  @inline def fromGWei(value: BigDecimal):   Ether = Ether(value * 1000 * 1000 * 1000)
  @inline def fromGWei(value: Int):          Ether = Ether(bigDecimal(value * 1000 * 1000 * 1000))
  @inline def fromGWei(value: BigInt):       Ether = Ether(bigDecimal(value * 1000 * 1000 * 1000))

  @inline def fromSzabo(value: String):      Ether = Ether(BigInt(value) * 1000 * 1000 * 1000 * 1000)
  @inline def fromSzabo(value: Double):      Ether = Ether(value * 1000 * 1000 * 1000 * 1000)
  @inline def fromSzabo(value: BigDecimal):  Ether = Ether(value * 1000 * 1000 * 1000 * 1000)
  @inline def fromSzabo(value: Int):         Ether = Ether(bigDecimal(value * 1000 * 1000 * 1000 * 1000))
  @inline def fromSzabo(value: BigInt):      Ether = Ether(bigDecimal(value * 1000 * 1000 * 1000 * 1000))

  @inline def fromFinney(value: String):     Ether = Ether(BigInt(value) * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def fromFinney(value: Double):     Ether = Ether(value * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def fromFinney(value: BigDecimal): Ether = Ether(value * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def fromFinney(value: Int):        Ether = Ether(bigDecimal(value * 1000 * 1000 * 1000 * 1000 * 1000))
  @inline def fromFinney(value: BigInt):     Ether = Ether(bigDecimal(value * 1000 * 1000 * 1000 * 1000 * 1000))

  @inline def fromEther(value: String):      Ether = Ether(BigInt(value) * 1000 * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def fromEther(value: Double):      Ether = Ether(value * 1000 * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def fromEther(value: BigDecimal):  Ether = Ether(value * 1000 * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def fromEther(value: Int):         Ether = Ether(bigDecimal(value * 1000 * 1000 * 1000 * 1000 * 1000 * 1000))
  @inline def fromEther(value: BigInt):      Ether = Ether(bigDecimal(value * 1000 * 1000 * 1000 * 1000 * 1000 * 1000))

  @inline def fromMEther(value: String):     Ether = Ether(BigInt(value) * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def fromMEther(value: Double):     Ether = Ether(value * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def fromMEther(value: BigDecimal): Ether = Ether(value * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def fromMEther(value: Int):        Ether = Ether(bigDecimal(value * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000))
  @inline def fromMEther(value: BigInt):     Ether = Ether(bigDecimal(value * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000))

  @inline def fromGEther(value: String):     Ether = Ether(BigInt(value) * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def fromGEther(value: Double):     Ether = Ether(value * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def fromGEther(value: BigDecimal): Ether = Ether(value * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def fromGEther(value: Int):        Ether = Ether(bigDecimal(value * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000))
  @inline def fromGEther(value: BigInt):     Ether = Ether(bigDecimal(value * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000))

  protected lazy val weiToEthRatio = new java.math.BigInteger("1000000000000000000")

  /** @return Amount of Ether corresponding to the given wei value */
  def bigDecimal(wei: BigInt): java.math.BigDecimal =
    new java.math.BigDecimal(wei.bigInteger)
      .setScale(16, java.math.BigDecimal.ROUND_DOWN)

  protected def ethToWei(amountInEth: java.math.BigDecimal): BigInt =
    amountInEth.multiply(new java.math.BigDecimal(weiToEthRatio)).toBigInteger
}

/** Wei are the smallest unit of currency and are always integers, never fractional quantities */
class Ether(val wei: BigInt) extends AnyVal {
  import Ether.bigDecimal

  @inline def asWei: BigDecimal    = bigDecimal(wei)
  @inline def asKWei: BigDecimal   = bigDecimal(wei * 1000)
  @inline def asMWei: BigDecimal   = bigDecimal(wei * 1000 * 1000)
  @inline def asGWei: BigDecimal   = bigDecimal(wei * 1000 * 1000 * 1000)
  @inline def asSzabo: BigDecimal  = bigDecimal(wei * 1000 * 1000 * 1000 * 1000)
  @inline def asFinney: BigDecimal = bigDecimal(wei * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def asEther: BigDecimal  = bigDecimal(wei * 1000 * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def asKEther: BigDecimal = bigDecimal(wei * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def asMEther: BigDecimal = bigDecimal(wei * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000)
  @inline def asGEther: BigDecimal = bigDecimal(wei * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000)

  /*override def equals(that: Any): Boolean =
    that match {
      case that: Ether => this.hashCode == that.hashCode
      case _ => false
    }

  override def hashCode: Int = wei.hashCode*/

  @inline def toHex: String = s"0x${ wei.toString(16) }"

  @inline override def toString: String = wei.toString.length match {
    case length if length <=3  => s"$wei Wei"
    case length if length <=6  => s"$asKWei KWei"
    case length if length <=9  => s"$asMWei MWei"
    case length if length <=12 => s"$asGWei GWei"
    case length if length <=15 => s"$asSzabo Szabo"
    case length if length <=18 => s"$asFinney Finney"
    case length if length <=21 => s"$asEther Ether"
    case length if length <=24 => s"$asKEther KEther"
    case _                     => s"$asGEther GEther"
  }
}
