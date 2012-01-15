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
 * Thread-safe class using Monitors to provide modulo counter functionality. This class should be preferred over SharedModuloCounterLight
 * for medium/high contention scenarios, as it does not use spin-waiting.
 */
public final class SharedModuloCounter
    extends ModuloCounter
    implements IShared
{
  private final ReentrantLock lockObject;

  /**
   * Initializes a new instance of the class.
   * 
   * @param maxValue The maximum counter value.
   * 
   * @throws IllegalArgumentException When the maximum is less than or equal to minimum, or equal to Long.MAX_VALUE
   */
  public SharedModuloCounter(long maxValue)
  {
    this(maxValue, DEFAULT_MIN_VALUE);
  }

  /**
   * Initializes a new instance of the class.
   * 
   * @param maxValue The maximum counter value.
   * @param minValue The minimum counter value.
   * 
   * @throws IllegalArgumentException When the maximum is less than or equal to minimum, or equal to Long.MAX_VALUE
   */
  public SharedModuloCounter(long maxValue, long minValue)
  {
    super(maxValue, minValue);

    lockObject = new ReentrantLock();
  }

  /**
   * Returns the counter value
   */
  @Override
  public long getValue()
  {
    lock();
    try
    {
      return super.getValue();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Locks the object
   */
  public void lock()
  {
    lockObject.lock();
  }

  /**
   * Unlocks the object.
   * 
   * @throws IllegalMonitorStateException if the current thread does not hold this lock
   */
  public void unlock()
  {
    lockObject.unlock();
  }

  /**
   * Increments the counter and returns the next value
   */
  @Override
  public long next()
  {
    lock();
    try
    {
      return super.next();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Resets the counter to the minimum value
   */
  public void reset()
  {
    lock();
    try
    {
      super.reset();
    }
    finally
    {
      unlock();
    }
  }
}
