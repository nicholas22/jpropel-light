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

/**
 * A 128-bit integer. Implemented with BigInteger backing.
 */
public class Int128
		extends NumberType
		implements Comparable<Int128>
{
  private static final long serialVersionUID = -7970478545846258719L;
  public static final Int128 MIN_VALUE = new Int128(new java.math.BigInteger("-170141183460469231731687303715884105728"), true);
	public static final Int128 MAX_VALUE = new Int128(new java.math.BigInteger("170141183460469231731687303715884105727"), true);
	private java.math.BigInteger value;

	/**
	 * Initializes with the value 0
	 */
	public Int128()
	{
		value = new java.math.BigInteger("0");
	}

	/**
	 * Initializes with a string value.
	 *
	 * @throws NumberFormatException The argument is out of range
	 */
	public Int128(String str)
	{
		this.value = new java.math.BigInteger(str);

		if(this.value.compareTo(MIN_VALUE.value) < 0)
			throw new NumberFormatException("The value is too small for be an Int128: " + value);
		if(this.value.compareTo(MAX_VALUE.value) > 0)
			throw new NumberFormatException("The value is too large for be an Int128: " + value);
	}

	/**
	 * Initializes with a BigInteger type
	 *
	 * @throws NumberFormatException The argument is out of range
	 */
	public Int128(java.math.BigInteger bi)
	{
		this.value = bi;

		if(this.value.compareTo(MIN_VALUE.value) < 0)
			throw new NumberFormatException("The value is too small for be an Int128: " + value);
		if(this.value.compareTo(MAX_VALUE.value) > 0)
			throw new NumberFormatException("The value is too large for be an Int128: " + value);
	}

	/**
	 * Only used internally to initialize static final fields.
	 */
	private Int128(java.math.BigInteger bi, boolean dummy) {
		this.value = bi;
	}

	/**
	 * Initializes with another Int128
	 *
	 * @throws NullPointerException When the argument is null.
	 */
	public Int128(Int128 other)
	{
		if(other == null)
			throw new NullPointerException("other");
		this.value = other.value;
	}

	/**
	 * Gets the current value in base 10
	 */
	public String getCurrentValue()
	{
		return this.value.toString(10);
	}

	/**
	 * Sets the current value in base 10
	 */
	public void setCurrentValue(String newValue)
	{
		this.value = new java.math.BigInteger(newValue, 10);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.math.BigInteger bigIntegerValue()
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
		if(other == null)
			return false;

		if(other == this)
			return true;

		if(!(other instanceof Int128))
			return false;

		Int128 num = (Int128) other;

		return value.equals(num.value);
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
	public int compareTo(Int128 other)
	{
		if(other == null)
			throw new NullPointerException("other");

		return value.compareTo(other.value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object clone()
	{
		return new Int128(this);
	}
}
