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

/**
 * A wrapper over Java's BigInteger, used as for compatibility when using C# BigInteger
 */
public final class BigInteger
{
  /**
   * Unsigned integers describing the BigInteger
   */
  private String currentValue;
  /**
   * Used for actual arithmetic
   */
  private java.math.BigInteger bi;

  /**
   * Default constructor
   */
  public BigInteger()
  {
    bi = new java.math.BigInteger("0");
    currentValue = bi.toString(10);
  }

  /**
   * Constructor initializes with a java.math.BigInteger
   */
  public BigInteger(java.math.BigInteger other)
  {
    if (other == null)
      throw new NullPointerException("other");

    bi = other;
    currentValue = bi.toString(10);
  }

  public String getCurrentValue()
  {
    return currentValue;
  }

  public void setCurrentValue(String newValue)
  {
    if (newValue == null)
      throw new NullPointerException("newValue");
    currentValue = newValue;
    bi = new java.math.BigInteger(newValue, 10);
  }

}
