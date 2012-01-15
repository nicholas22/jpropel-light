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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe class using AtomicInteger to provide array indexing functionality. Best used in very low contention scenarios (medium/high
 * contention scenarios use too much CPU), this is because it uses spin waiting instead of kernel mode sleep transitions.
 */
public final class SharedModuloIndexerLight
    implements IModuloIndexer
{
  private final int startingIndex;
  private final int length;
  private final AtomicInteger atomicValue;

  /**
   * Initializes indexer with a specified length
   * 
   * @param length The array length
   * 
   * @throws IllegalArgumentException When the maximum is less than or equal to minimum, or equal to Integer.MAX_VALUE
   */
  public SharedModuloIndexerLight(int length)
  {
    this(0, length);
  }

  /**
   * Initializes indexer with a starting and the length
   * 
   * @param startingIndex The starting index
   * @param length The array length
   * 
   * @throws IllegalArgumentException When the maximum is less than or equal to minimum, or equal to INTEGER.MAX_VALUE
   */
  public SharedModuloIndexerLight(int startingIndex, int length)
  {
    if (startingIndex < 0)
      throw new IllegalArgumentException("startingIndex=" + startingIndex);
    if (startingIndex >= length)
      throw new IllegalArgumentException("startingIndex=" + startingIndex + " length=" + length);
    if (length == Integer.MAX_VALUE)
      throw new IllegalArgumentException("length=" + length);

    this.startingIndex = startingIndex;
    this.atomicValue = new AtomicInteger(startingIndex - 1);
    this.length = length;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int next()
  {
    int newValue;
    int oldValue;

    do
    {
      oldValue = atomicValue.intValue();
      newValue = oldValue + 1;
      if (newValue >= length)
        newValue = 0;
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
    int newValue;
    int oldValue;

    do
    {
      oldValue = atomicValue.intValue();
      newValue = startingIndex - 1;
    }
    while (!atomicValue.compareAndSet(oldValue, newValue));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getValue()
  {
    return atomicValue.intValue();
  }
}
