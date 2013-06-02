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
package propel.core.threading.primitives;

import propel.core.functional.Predicates.Predicate1;
import propel.core.model.IShared;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Generic class allowing for easy reader/writer access to an object. Uses Monitors to disallowed concurrent read/write access.
 */
public class SharedData<T>
    implements IShared
{
  /**
   * The lock we use to protect an object from concurrent accesses
   */
  private final ReentrantLock lockObject = new ReentrantLock();
  /**
   * The object we are protecting from concurrent accesses
   */
  private T obj;

  /**
   * Initializes the class with the protected object.
   */
  public SharedData(T obj)
  {
    this.obj = obj;
  }

  /**
   * Locks the object
   */
  public void lock()
  {
    lockObject.lock();
  }

  /**
   * Unlocks the object
   * 
   * @throws IllegalMonitorStateException The current thread does not own the lock for the specified object.
   */
  public void unlock()
  {
    lockObject.unlock();
  }

  /**
   * Gets the value of the protected object
   */
  public T get()
  {
    lock();
    try
    {
      return obj;
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Sets the value of the protected object
   */
  public void set(T newValue)
  {
    lock();
    try
    {
      obj = newValue;
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Compares and replaces the encapsulated (old) value with the newValue if it is equal to the comparand. Returns the old value.
   */
  public T compareExchange(T newValue, T comparand)
  {
    lock();
    try
    {
      T result = obj;
      if (obj.equals(comparand))
        obj = newValue;

      return result;
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Compares and replaces the encapsulated (old) value with the newValue if it is not equal to the comparand. Returns the old value.
   */
  public T compareExchangeNotEqual(T newValue, T comparand)
  {
    lock();
    try
    {
      T result = obj;
      if (!obj.equals(comparand))
        obj = newValue;

      return result;
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Compares and replaces the encapsulated (old) value with the newValueif the given predicate returns true. Returns the old value. For the
   * duration of the predicate the object is locked, so you're advised to keep the predicate short.
   */
  public T compareExchangePredicate(T newValue, Predicate1<T> predicate)
  {
    lock();
    try
    {
      T result = obj;
      if (predicate.apply(obj))
        obj = newValue;

      return result;
    }
    finally
    {
      unlock();
    }
  }
}
