/*
 ///////////////////////////////////////////////////////////
 //  This file is part of Propel.
 //
 //  Propel is free software: you can redistribute it and/or modify
 //  it under the terms of the GNU Lesser General Public License as published by
 //  the Free Software Foundation, either version 3 of the License, or
 //  (at your option) any later version.
 //
 //  Propel is distributed in the hope that it will be useful,
 //  but WITHOUT ANY WARRANTY; without even the implied warranty of
 //  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 //  GNU Lesser General Public License for more details.
 //
 //  You should have received a copy of the GNU Lesser General Public License
 //  along with Propel.  If not, see <http://www.gnu.org/licenses/>.
 ///////////////////////////////////////////////////////////
 //  Authored by: Nikolaos Tountas -> salam.kaser-at-gmail.com
 ///////////////////////////////////////////////////////////
 */
package propel.core.userTypes;

import java.math.BigInteger;

/**
 * Encapsulates an unsigned integer.
 */
public final class UnsignedInteger
		extends NumberType
		implements Comparable<UnsignedInteger>
{
  private static final long serialVersionUID = -70339380774199424L;
  public static final UnsignedInteger MIN_VALUE = new UnsignedInteger(0, true);
	public static final UnsignedInteger MAX_VALUE = new UnsignedInteger(4294967295L, true);
	private final long value;

	/**
	 * Initializes with the value 0
	 */
	public UnsignedInteger()
	{
		value = 0;
	}

	/**
	 * Initializes with a string value.
	 *
	 * @throws NumberFormatException The argument is out of range
	 */
	public UnsignedInteger(String value)
	{
		this.value = Long.parseLong(value);

		if(this.value < MIN_VALUE.value)
			throw new NumberFormatException("The value is too small for be an unsigned integer: " + value);
		if(this.value > MAX_VALUE.value)
			throw new NumberFormatException("The value is too large for be an unsigned integer: " + value);
	}

	/**
	 * Initializes with a BigInteger type
	 *
	 * @throws NumberFormatException The argument is out of range
	 */
	public UnsignedInteger(BigInteger value)
	{
		this(value.toString(10));
	}

	/**
	 * Initializes with a primitive number type
	 *
	 * @throws NumberFormatException The argument is out of range
	 */
	public UnsignedInteger(long value)
	{
		this.value = value;

		if(this.value < MIN_VALUE.value)
			throw new NumberFormatException("The value is too small for be an unsigned integer: " + value);
		if(this.value > MAX_VALUE.value)
			throw new NumberFormatException("The value is too large for be an unsigned integer: " + value);
	}

	/**
	 * Only used internally to initialize static final fields.
	 */
	private UnsignedInteger(long value, boolean dummy)
	{
		this.value = value;
	}

	/**
	 * Initializes with another unsigned integer
	 *
	 * @throws NullPointerException When the argument is null.
	 */
	public UnsignedInteger(UnsignedInteger other)
	{
		if(other == null)
			throw new NullPointerException("other");
		this.value = other.value;
	}

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
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float floatValue()
	{
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte byteValue()
	{
		return (new Long(value)).byteValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public short shortValue()
	{
		return (new Long(value)).shortValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int intValue()
	{
		return (new Long(value)).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long longValue()
	{
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return Long.valueOf(value).toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other)
	{
		if(other == null)
			return false;

		if(other == this)
			return true;

		if(!(other instanceof UnsignedInteger))
			return false;

		UnsignedInteger ui = (UnsignedInteger) other;
		return value == ui.value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return (int) (value ^ (value >>> 32)); // recommended by Joshua Block
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(UnsignedInteger other)
	{
		if(other == null)
			throw new NullPointerException("other");

		if(value < other.value)
			return -1;
		if(value > other.value)
			return 1;

		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object clone()
	{
		return new UnsignedInteger(this);
	}
}
