// /////////////////////////////////////////////////////////
// This file is part of Propel.
//
// Propel is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Propel is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with Propel. If not, see <http://www.gnu.org/licenses/>.
// /////////////////////////////////////////////////////////
// Authored by: Nikolaos Tountas -> salam.kaser-at-gmail.com
// /////////////////////////////////////////////////////////
package propel.core.userTypes;

import java.math.BigInteger;

/**
 * Encapsulates an unsigned long.
 */
public final class UnsignedLong
    extends NumberType
    implements Comparable<UnsignedLong>
{
  private static final long serialVersionUID = -5620866038306834473L;
  public static final UnsignedLong MIN_VALUE = new UnsignedLong(new BigInteger("0"), true);
  public static final UnsignedLong MAX_VALUE = new UnsignedLong(new BigInteger("18446744073709551615"), true);
  private final BigInteger value;

  /**
   * Initializes with the value 0
   */
  public UnsignedLong()
  {
    value = new BigInteger("0");
  }

  /**
   * Initializes with a string value.
   * 
   * @throws NumberFormatException The argument is out of range
   */
  public UnsignedLong(String value)
  {
    this.value = new BigInteger(value);

    if (this.value.compareTo(MIN_VALUE.value) < 0)
      throw new NumberFormatException("The value is too small for be an unsigned long: " + value);
    if (this.value.compareTo(MAX_VALUE.value) > 0)
      throw new NumberFormatException("The value is too large for be an unsigned long: " + value);
  }

  /**
   * Initializes with a BigInteger type
   * 
   * @throws NumberFormatException The argument is out of range
   */
  public UnsignedLong(BigInteger value)
  {
    this.value = value;

    if (this.value.compareTo(MIN_VALUE.value) < 0)
      throw new NumberFormatException("The value is too small for be an unsigned long: " + value);
    if (this.value.compareTo(MAX_VALUE.value) > 0)
      throw new NumberFormatException("The value is too large for be an unsigned long: " + value);
  }

  /**
   * Only used internally to initialize static final fields.
   */
  private UnsignedLong(BigInteger value, boolean dummy)
  {
    this.value = value;
  }

  /**
   * Initializes with another unsigned long
   * 
   * @throws NullPointerException When the argument is null.
   */
  public UnsignedLong(UnsignedLong other)
  {
    if (other == null)
      throw new NullPointerException("other");
    this.value = other.value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigInteger bigIntegerValue()
  {
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double doubleValue()
  {
    return value.doubleValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float floatValue()
  {
    return value.floatValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte byteValue()
  {
    return value.byteValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public short shortValue()
  {
    return value.shortValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int intValue()
  {
    return value.intValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long longValue()
  {
    return value.longValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return value.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object other)
  {
    if (other == null)
      return false;

    if (other == this)
      return true;

    if (!(other instanceof UnsignedLong))
      return false;

    UnsignedLong ul = (UnsignedLong) other;
    return value.equals(ul.value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return value.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(UnsignedLong other)
  {
    if (other == null)
      throw new NullPointerException("other");

    return value.compareTo(other.value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone()
  {
    return new UnsignedLong(this);
  }
}
