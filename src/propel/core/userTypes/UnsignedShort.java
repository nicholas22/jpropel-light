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
 * Encapsulates an unsigned short.
 */
public final class UnsignedShort
    extends NumberType
    implements Comparable<UnsignedShort>
{
  private static final long serialVersionUID = 2058452473845076723L;
  public static final UnsignedShort MIN_VALUE = new UnsignedShort(0, true);
  public static final UnsignedShort MAX_VALUE = new UnsignedShort(65535, true);
  private final int value;

  /**
   * Initializes with the value 0
   */
  public UnsignedShort()
  {
    value = 0;
  }

  /**
   * Initializes with a string value.
   * 
   * @throws NumberFormatException The argument is out of range
   */
  public UnsignedShort(String value)
  {
    this.value = Short.parseShort(value);

    if (this.value < MIN_VALUE.value)
      throw new NumberFormatException("The value is too small for be an unsigned short: " + value);
    if (this.value > MAX_VALUE.value)
      throw new NumberFormatException("The value is too large for be an unsigned short: " + value);
  }

  /**
   * Initializes with a primitive number type
   * 
   * @throws NumberFormatException The argument is out of range
   */
  public UnsignedShort(int value)
  {
    this.value = value;

    if (this.value < MIN_VALUE.value)
      throw new NumberFormatException("The value is too small for be an unsigned short: " + value);
    if (this.value > MAX_VALUE.value)
      throw new NumberFormatException("The value is too large for be an unsigned short: " + value);
  }

  /**
   * Only used internally to initialize static final fields.
   */
  private UnsignedShort(int value, boolean dummy)
  {
    this.value = value;
  }

  /**
   * Initializes with a BigInteger type
   * 
   * @throws NumberFormatException The argument is out of range
   */
  public UnsignedShort(BigInteger value)
  {
    this(value.toString(10));
  }

  /**
   * Initializes with another unsigned short
   * 
   * @throws NullPointerException When the argument is null.
   */
  public UnsignedShort(UnsignedShort other)
  {
    if (other == null)
      throw new NullPointerException("other");
    this.value = other.value;
  }

  /**
   * {@inheritDoc}
   */
  /**
   * {@inheritDoc}
   */
  @Override
  public BigInteger bigIntegerValue()
  {
    return new BigInteger(toString());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double doubleValue()
  {
    return (new Integer(value)).doubleValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float floatValue()
  {
    return (new Integer(value)).floatValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte byteValue()
  {
    return (new Integer(value)).byteValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public short shortValue()
  {
    return (new Integer(value)).shortValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int intValue()
  {
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long longValue()
  {
    return (new Integer(value)).longValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return Integer.valueOf(value).toString();
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

    if (!(other instanceof UnsignedShort))
      return false;

    UnsignedShort us = (UnsignedShort) other;
    return value == us.value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(UnsignedShort other)
  {
    if (other == null)
      throw new NullPointerException("other");

    if (value < other.value)
      return -1;
    if (value > other.value)
      return 1;

    return 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone()
  {
    return new UnsignedShort(this);
  }
}
