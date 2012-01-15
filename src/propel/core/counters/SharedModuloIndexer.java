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

import propel.core.model.IShared;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe class using Monitors to provide array indexing functionality. This class should be preferred over SharedModuloIndexerLight in
 * medium to high thread contention scenarios, as it uses kernel mode thread transitions, instead of spin-waiting.
 */
public final class SharedModuloIndexer
    implements IShared, IModuloIndexer
{
  private final ReentrantLock lockObject;
  private final int startingIndex;
  private final int length;
  private int index;

  /**
   * Initializes indexer with a specified length
   * 
   * @param length The array length
   * 
   * @throws IllegalArgumentException When the maximum is less than or equal to minimum, or equal to Integer.MAX_VALUE
   */
  public SharedModuloIndexer(int length)
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
  public SharedModuloIndexer(int startingIndex, int length)
  {
    if (startingIndex < 0)
      throw new IllegalArgumentException("startingIndex=" + startingIndex);
    if (startingIndex >= length)
      throw new IllegalArgumentException("startingIndex=" + startingIndex + " length=" + length);
    if (length == Integer.MAX_VALUE)
      throw new IllegalArgumentException("length=" + length);

    this.lockObject = new ReentrantLock();
    this.startingIndex = startingIndex;
    this.index = startingIndex - 1;
    this.length = length;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getValue()
  {
    lock();
    try
    {
      return index;
    }
    finally
    {
      unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void lock()
  {
    lockObject.lock();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unlock()
  {
    lockObject.unlock();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int next()
  {
    lock();
    try
    {
      index += 1;
      if (index >= length)
        index = 0;

      return index;
    }
    finally
    {
      unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset()
  {
    lock();
    try
    {
      index = startingIndex - 1;
    }
    finally
    {
      unlock();
    }
  }
}
