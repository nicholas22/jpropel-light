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

import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe class using AtomicLong to provide modulo counter functionality. Should be preferred over SharedModuloCounter in very low
 * contention scenarios (medium/high contention scenarios use too much CPU), this is because it uses spin waiting instead of kernel mode
 * sleep transitions.
 */
public final class SharedModuloCounterLight
    extends ModuloCounter
{
  private final AtomicLong atomicValue;

  /**
   * Initializes counter with a specified maximum value.
   * 
   * @param maxValue The maximum counter value.
   * 
   * @throws IllegalArgumentException When the maximum is less than or equal to minimum, or equal to Long.MAX_VALUE
   */
  public SharedModuloCounterLight(long maxValue)
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
  public SharedModuloCounterLight(long maxValue, long minValue)
  {
    super(maxValue, minValue);
    atomicValue = new AtomicLong(minValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long next()
  {
    long newValue;
    long oldValue;

    do
    {
      oldValue = atomicValue.longValue();
      newValue = oldValue + 1;
      if (newValue > maxValue)
        newValue = minValue;
    }
    while (!atomicValue.compareAndSet(oldValue, newValue));

    return newValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset()
  {
    long newValue;
    long oldValue;

    do
    {
      oldValue = atomicValue.longValue();
      newValue = minValue;
    }
    while (!atomicValue.compareAndSet(oldValue, newValue));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getValue()
  {
    return atomicValue.longValue();
  }
}
