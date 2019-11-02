package ir.easazade.androidutils.classes

import java.io.Serializable

/**
 * Represents a generic tuple of four values.
 *
 * There is no meaning attached to values in this class, it can be used for any purpose.
 * Quadruple exhibits value semantics, i.e. two quadruples are equal if all four components are equal.
 */
data class Quadruple<out A, out B, out C, out D>(
  val first: A,
  val second: B,
  val third: C,
  val fourth: D
) : Serializable