package com.micronautics.web3j

object Ether {
  lazy val zero: Ether = Ether(0)

  /** @return a BigInt containing 1 with the given number of zeros after it, e.g. {{{e(3) == BigInt(1000)}}} */
  @inline def e(howManyZeros: Int): BigInt = BigInt(s"1${ zeros(howManyZeros) }")

  /** @return a String containing the specified number of zeros, e.g. {{{zeros(3) == "000"}}} */
  @inline def zeros(howMany: Int): String = "0" * howMany

  /** @param value unit is wei */
  @inline def apply(value: String):          Ether = new Ether(BigInt(value))
  @inline def apply(value: BigInt):          Ether = new Ether(value)
  @inline def apply(value: Int):             Ether = new Ether(BigInt(value))
  @inline def apply(value: Double):          Ether = new Ether(BigDecimal(value).bigDecimal.toBigInteger)
  @inline def apply(value: BigDecimal):      Ether = new Ether(value.bigDecimal.toBigInteger)

  /** @param scale used when constructing the [[java.math.BigDecimal]] return value
    * @return Amount of Ether corresponding to the given wei value */
  @inline def bigDecimal(wei: BigInt, scale: Int=16): java.math.BigDecimal =
    new java.math.BigDecimal(wei.bigInteger)
      .setScale(scale, java.math.BigDecimal.ROUND_DOWN)

  @inline def fromWei(value: String):        Ether = Ether(value)
  @inline def fromWei(value: Double):        Ether = Ether(value)
  @inline def fromWei(value: BigDecimal):    Ether = Ether(value)
  @inline def fromWei(value: Int):           Ether = Ether(value)
  @inline def fromWei(value: BigInt):        Ether = Ether(value)

  @inline def fromKWei(value: String):       Ether = Ether(value + zeros(3))
  @inline def fromKWei(value: Double):       Ether = Ether(BigDecimal(value) * BigDecimal(e(3)))
  @inline def fromKWei(value: BigDecimal):   Ether = Ether(value * BigDecimal(e(3)))
  @inline def fromKWei(value: Int):          Ether = Ether(value * e(3))
  @inline def fromKWei(value: BigInt):       Ether = Ether(value * e(3))

  @inline def fromMWei(value: String):       Ether = Ether(value + zeros(6))
  @inline def fromMWei(value: Double):       Ether = Ether(BigDecimal(value) * BigDecimal(e(6)))
  @inline def fromMWei(value: BigDecimal):   Ether = Ether(value * BigDecimal(e(6)))
  @inline def fromMWei(value: Int):          Ether = Ether(value * e(6))
  @inline def fromMWei(value: BigInt):       Ether = Ether(value * e(6))

  @inline def fromGWei(value: String):       Ether = Ether(value + zeros(9))
  @inline def fromGWei(value: Double):       Ether = Ether(BigDecimal(value) * BigDecimal(e(9)))
  @inline def fromGWei(value: BigDecimal):   Ether = Ether(value * BigDecimal(e(9)))
  @inline def fromGWei(value: Int):          Ether = Ether(value * e(9))
  @inline def fromGWei(value: BigInt):       Ether = Ether(value * e(9))

  @inline def fromSzabo(value: String):      Ether = Ether(value + zeros(12))
  @inline def fromSzabo(value: Double):      Ether = Ether(BigDecimal(value) * BigDecimal(e(12)))
  @inline def fromSzabo(value: BigDecimal):  Ether = Ether(value * BigDecimal(e(12)))
  @inline def fromSzabo(value: Int):         Ether = Ether(value * e(12))
  @inline def fromSzabo(value: BigInt):      Ether = Ether(value * e(12))

  @inline def fromFinney(value: String):     Ether = Ether(value + zeros(15))
  @inline def fromFinney(value: Double):     Ether = Ether(BigDecimal(value) * BigDecimal(e(15)))
  @inline def fromFinney(value: BigDecimal): Ether = Ether(value * BigDecimal(e(15)))
  @inline def fromFinney(value: Int):        Ether = Ether(value * e(15))
  @inline def fromFinney(value: BigInt):     Ether = Ether(value * e(15))

  @inline def fromEther(value: String):      Ether = Ether(value + zeros(18))
  @inline def fromEther(value: Double):      Ether = Ether(BigDecimal(value) * BigDecimal(e(18)))
  @inline def fromEther(value: BigDecimal):  Ether = Ether(value * BigDecimal(e(18)))
  @inline def fromEther(value: Int):         Ether = Ether(value * e(18))
  @inline def fromEther(value: BigInt):      Ether = Ether(value * e(18))

  @inline def fromKEther(value: String):     Ether = Ether(value + zeros(21))
  @inline def fromKEther(value: Double):     Ether = Ether(BigDecimal(value) * BigDecimal(e(21)))
  @inline def fromKEther(value: BigDecimal): Ether = Ether(value * BigDecimal(e(21)))
  @inline def fromKEther(value: Int):        Ether = Ether(value * e(21))
  @inline def fromKEther(value: BigInt):     Ether = Ether(value * e(21))

  @inline def fromMEther(value: String):     Ether = Ether(value + zeros(24))
  @inline def fromMEther(value: Double):     Ether = Ether(BigDecimal(value) * BigDecimal(e(24)))
  @inline def fromMEther(value: BigDecimal): Ether = Ether(value * BigDecimal(e(24)))
  @inline def fromMEther(value: Int):        Ether = Ether(value * e(24))
  @inline def fromMEther(value: BigInt):     Ether = Ether(value * e(24))

  @inline def fromGEther(value: String):     Ether = Ether(value + zeros(27))
  @inline def fromGEther(value: Double):     Ether = Ether(BigDecimal(value) * BigDecimal(e(27)))
  @inline def fromGEther(value: BigDecimal): Ether = Ether(value * BigDecimal(e(27)))
  @inline def fromGEther(value: Int):        Ether = Ether(value * e(27))
  @inline def fromGEther(value: BigInt):     Ether = Ether(value * e(27))
}

/** Wei are the smallest unit of currency and are always integers, never fractional quantities.
  * An `Ether` is 10^18^ Wei ... a huge dynamic range.
  * But that is not all, because Ether actually has 54 orders of magnitude (+/- 10^27^).
  *
  * This class stores and computes in Wei (like cents, they are the smallest currency denomination), using [[scala.BigInt]].
  * Unless special care is taken, [[scala.BigDecimal]] should not be used for Ether computations, just for formatting of output.
  *
  * Common arithmetic operators used in financial applications are directly implemented; if you need an operator that is not provided,
  * the underlying [[scala.BigInt]] probably supports it. */
class Ether(val wei: BigInt) extends Ordered[Ether] {
  import Ether._

  @inline def *(value: Ether): Ether      = Ether(wei * value.wei)
  @inline def *(value: Int): Ether        = Ether(wei * value)
  @inline def *(value: Double): Ether     = Ether(BigDecimal(wei) * value)
  @inline def *(value: BigInt): Ether     = Ether(value) * wei
  @inline def *(value: BigDecimal): Ether = Ether(value) * wei

  @inline def /(value: Ether): Ether      = Ether(wei / value.wei)
  @inline def /(value: Int): Ether        = Ether(wei / value)
  @inline def /(value: Double): Ether     = Ether(BigDecimal(wei) / value)
  @inline def /(value: BigInt): Ether     = Ether(value) / wei
  @inline def /(value: BigDecimal): Ether = Ether(value) / wei

  @inline def +(value: Ether): Ether      = Ether(wei + value.wei)
  @inline def +(value: Int): Ether        = Ether(wei + value)
  @inline def +(value: Double): Ether     = Ether(BigDecimal(wei) + value)
  @inline def +(value: BigInt): Ether     = Ether(value) + wei
  @inline def +(value: BigDecimal): Ether = Ether(value) + wei

  @inline def -(value: Ether): Ether      = Ether(wei - value.wei)
  @inline def -(value: Int): Ether        = Ether(wei - value)
  @inline def -(value: Double): Ether     = Ether(BigDecimal(wei) - value)
  @inline def -(value: BigInt): Ether     = Ether(value) - wei
  @inline def -(value: BigDecimal): Ether = Ether(value) - wei

  /** @return the underlying [[java.math.BigInteger]] */
  @inline def bigInteger: java.math.BigInteger = wei.bigInteger

  /** Do not use the return value for computations unless special care is token.
    * @param scale used when constructing the [[java.math.BigDecimal]] return value; defaults to 16.
    * This class is intended for output formatting only. */
  @inline def asWei(scale: Int = 16): BigDecimal   = bigDecimal(wei, scale)

  /** This class is intended for output formatting only.
    * Do not use the return value for computations unless special care is token.
    * @param scale used when constructing the [[java.math.BigDecimal]] return value; defaults to 16. */
  @inline def asKWei(scale: Int = 16): BigDecimal   = bigDecimal(wei / e(3), scale)

  /** This class is intended for output formatting only.
    * Do not use the return value for computations unless special care is token.
    * @param scale used when constructing the [[java.math.BigDecimal]] return value; defaults to 16. */
  @inline def asMWei(scale: Int = 16): BigDecimal   = bigDecimal(wei / e(6))

  /** This class is intended for output formatting only.
    * Do not use the return value for computations unless special care is token.
    * @param scale used when constructing the [[java.math.BigDecimal]] return value; defaults to 16. */
  @inline def asGWei(scale: Int = 16): BigDecimal   = bigDecimal(wei / e(9))

  /** This class is intended for output formatting only.
    * Do not use the return value for computations unless special care is token.
    * @param scale used when constructing the [[java.math.BigDecimal]] return value; defaults to 16. */
  @inline def asSzabo(scale: Int = 16): BigDecimal  = bigDecimal(wei / e(12))

  /** This class is intended for output formatting only.
    * Do not use the return value for computations unless special care is token.
    * @param scale used when constructing the [[java.math.BigDecimal]] return value; defaults to 16. */
  @inline def asFinney(scale: Int = 16): BigDecimal = bigDecimal(wei / e(15))

  /** This class is intended for output formatting only.
    * Do not use the return value for computations unless special care is token.
    * @param scale used when constructing the [[java.math.BigDecimal]] return value; defaults to 16. */
  @inline def asEther(scale: Int = 16): BigDecimal  = bigDecimal(wei / e(18))

  /** This class is intended for output formatting only.
    * Do not use the return value for computations unless special care is token.
    * @param scale used when constructing the [[java.math.BigDecimal]] return value; defaults to 16. */
  @inline def asKEther(scale: Int = 16): BigDecimal = bigDecimal(wei / e(21))

  /** This class is intended for output formatting only.
    * Do not use the return value for computations unless special care is token.
    * @param scale used when constructing the [[java.math.BigDecimal]] return value; defaults to 16. */
  @inline def asMEther(scale: Int = 16): BigDecimal = bigDecimal(wei / e(24))

  /** This class is intended for output formatting only.
    * Do not use the return value for computations unless special care is token.
    * @param scale used when constructing the [[java.math.BigDecimal]] return value; defaults to 16. */
  @inline def asGEther(scale: Int = 16): BigDecimal = bigDecimal(wei / e(27))

  @inline def compare(that: Ether): Int = this.wei compare that.wei

  @inline override def equals(that: Any): Boolean =
    that match {
      case that: Ether =>
        this.hashCode == that.hashCode

      case _ => false
    }

  @inline override def hashCode: Int = wei.hashCode

  @inline def isNegative: Boolean = wei < zero.wei

  /** Zero is not considered to be a positive value */
  @inline def isPositive: Boolean = wei > zero.wei

  @inline def isZero: Boolean = wei == zero.wei

  @inline def toHex: String = s"0x${ wei.toString(16) }"

  override def toString: String = wei.bitLength match {
    case length if length <=3  => s"${ asWei() } Wei"
    case length if length <=6  => s"${ asKWei() } KWei"
    case length if length <=9  => s"${ asMWei() } MWei"
    case length if length <=12 => s"${ asGWei() } GWei"
    case length if length <=15 => s"${ asSzabo() } Szabo"
    case length if length <=18 => s"${ asFinney() } Finney"
    case length if length <=21 => s"${ asEther() } Ether"
    case length if length <=24 => s"${ asKEther() } KEther"
    case length if length <=27 => s"${ asMEther() } MEther"
    case _                     => s"${ asGEther() } GEther"
  }
}
