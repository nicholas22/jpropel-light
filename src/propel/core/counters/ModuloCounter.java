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
package propel.core.counters;

/**
 * Class providing modulo counter functionality. In other words, this counter wraps around when maximum is reached.
 */
public class ModuloCounter
    implements ICounter
{
  /**
   * The default smallest number that can be held by the counter
   */
  public static final long DEFAULT_MIN_VALUE = 0;
  /**
   * The default largest number that can be held by the counter
   */
  public static final long DEFAULT_MAX_VALUE = 1024;
  /**
   * The max value
   */
  protected final long maxValue;
  /**
   * The min value
   */
  protected final long minValue;
  /**
   * The value of the counter
   */
  protected long value;

  /**
   * Default constructor, initializes with default minimum and maximum bounds.
   */
  public ModuloCounter()
  {
    this(DEFAULT_MAX_VALUE);
  }

  /**
   * Initializes counter with a specified maximum value.
   * 
   * @param maxValue The maximum counter value.
   * 
   * @throws IllegalArgumentException When the maximum is less than or equal to minimum, or equal to Long.MAX_VALUE
   */
  public ModuloCounter(long maxValue)
  {
    this(maxValue, DEFAULT_MIN_VALUE);
  }

  /**
   * Initializes counter with a specified maximum value and minimum value.
   * 
   * @param maxValue The maximum counter value.
   * @param minValue The minimum counter value.
   * 
   * @throws IllegalArgumentException When the maximum is less than or equal to minimum, or equal to Long.MAX_VALUE
   */
  public ModuloCounter(long maxValue, long minValue)
  {
    this.minValue = minValue;
    this.maxValue = maxValue;
    value = minValue;

    if (minValue == Long.MAX_VALUE)
      throw new IllegalArgumentException("Parameter must be greater than " + Long.MIN_VALUE);
    if (maxValue <= minValue)
      throw new IllegalArgumentException("Parameter must be greater than " + minValue);
    if (maxValue == Long.MAX_VALUE)
      throw new IllegalArgumentException("Parameter must be less than " + Long.MAX_VALUE);
  }

  /**
   * Returns the counter value
   */
  public long getValue()
  {
    return value;
  }

  /**
   * Returns the minimum value of the counter
   */
  public long getMinValue()
  {
    return minValue;
  }

  /**
   * Returns the maximum value of the counter
   */
  public long getMaxValue()
  {
    return maxValue;
  }

  /**
   * Increments the counter and returns the next value
   */
  public long next()
  {
    value++;
    if (value > maxValue)
      value = minValue;

    return value;
  }

  /**
   * Resets the counter to the minimum value
   */
  public void reset()
  {
    value = minValue;
  }
}
