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
package propel.core.collections.buffers;

import propel.core.collections.ReifiedIterable;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.lists.ReifiedList;
import propel.core.model.IShared;
import propel.core.utils.Linq;
import propel.core.utils.SuperTypeTokenException;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A type-aware thread-safe limited size circular buffer. This collection does not allow nulls to be inserted.
 * 
 * Instantiate using e.g.: new SharedBuffer&lt;String&gt;(){}; -OR- new SharedBuffer&lt;String&gt;(String.class);
 */
public class SharedBuffer<T>
    extends CircularBuffer<T>
    implements IShared
{
  private final ReentrantLock lockObject;
  protected final Condition notFull;
  protected final Condition notEmpty;

  /**
   * Default constructor.
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public SharedBuffer()
  {
    super();
    lockObject = new ReentrantLock();
    notFull = lockObject.newCondition();
    notEmpty = lockObject.newCondition();
  }

  /**
   * Constructor for initializing with the generic type parameter
   * 
   * @throws NullPointerException When the generic type parameter is null.
   */
  public SharedBuffer(Class<?> genericTypeParameter)
  {
    super(genericTypeParameter);
    lockObject = new ReentrantLock();
    notFull = lockObject.newCondition();
    notEmpty = lockObject.newCondition();
  }

  /**
   * Initializes the buffer with a buffer size.
   * 
   * @throws IllegalArgumentException When the buffer size is non positive.
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public SharedBuffer(int bufferSize)
  {
    super(bufferSize);
    lockObject = new ReentrantLock();
    notFull = lockObject.newCondition();
    notEmpty = lockObject.newCondition();
  }

  /**
   * Constructor initializes with a buffer size and a generic type parameter.
   * 
   * @throws IllegalArgumentException When the buffer size is non positive.
   * @throws NullPointerException When the generic type parameter is null.
   */
  public SharedBuffer(int bufferSize, Class<?> genericTypeParameter)
  {
    super(bufferSize, genericTypeParameter);
    lockObject = new ReentrantLock();
    notFull = lockObject.newCondition();
    notEmpty = lockObject.newCondition();
  }

  /**
   * Constructor initializes from another reified collection
   * 
   * @throws NullPointerException When the argument is null.
   * @throws IllegalArgumentException When the values is an empty iterable.
   */
  public SharedBuffer(ReifiedIterable<T> values)
  {
    super(values);
    lockObject = new ReentrantLock();
    notFull = lockObject.newCondition();
    notEmpty = lockObject.newCondition();
  }

  /**
   * Constructor initializes from another collection
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   * @throws IllegalArgumentException When the values is an empty iterable.
   * @throws NullPointerException When the argument is null
   */
  public SharedBuffer(Iterable<? extends T> values)
  {
    super(values);
    lockObject = new ReentrantLock();
    notFull = lockObject.newCondition();
    notEmpty = lockObject.newCondition();
  }

  /**
   * Constructor initializes from another collection and a generic type parameter
   * 
   * @throws NullPointerException When an argument is null.
   * @throws IllegalArgumentException When the values is an empty iterable.
   */
  public SharedBuffer(Iterable<? extends T> values, Class<?> genericTypeParameter)
  {
    super(values, genericTypeParameter);
    lockObject = new ReentrantLock();
    notFull = lockObject.newCondition();
    notEmpty = lockObject.newCondition();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean contains(T obj)
  {
    lock();
    try
    {
      return super.contains(obj);
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
  public void clear()
  {
    lock();
    try
    {
      super.clear();
      notFull.signalAll(); // Monitor.PulseAll()
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Retrieves an object from the buffer. If no object is available, blocks until one is and returns it. This is an O(1) operation.
   */
  @Override
  public T get()
  {
    T result;

    lock();
    try
    {
      while (isEmpty())
      {
        try
        {
          notEmpty.await(); // Monitor.Wait(), wait in "until notEmpty" queue
        }
        catch(InterruptedException e)
        {
          continue;
        }
      }

      result = super.get();
      notFull.signalAll(); // Monitor.PulseAll()
    }
    finally
    {
      unlock();
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEmpty()
  {
    lock();
    try
    {
      return super.isEmpty();
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
  public boolean isFull()
  {
    lock();
    try
    {
      return super.isFull();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Returns an iterator over a copy of the backing array. This is an O(n) operation.
   */
  @Override
  public Iterator<T> iterator()
  {
    lock();
    try
    {
      return new ReifiedArrayList<T>(buffer).iterator();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Puts an object in the Buffer. If there is no space, it blocks until space is available. This is an O(1) operation.
   * 
   * @throws NullPointerException When the object is null.
   */
  @Override
  public boolean put(T obj)
  {
    lock();
    try
    {
      while (isFull())
      {
        try
        {
          notFull.await(); // Monitor.wait(), wait in "until notFull" queue
        }
        catch(InterruptedException e)
        {
          continue;
        }
      }

      super.put(obj);
      notEmpty.signalAll(); // Monitor.PulseAll()
    }
    finally
    {
      unlock();
    }

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int size()
  {
    lock();
    try
    {
      return super.size();
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
  public T[] toArray()
  {
    lock();
    try
    {
      return super.toArray();
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
  public ReifiedList<T> toList()
  {
    lock();
    try
    {
      return super.toList();
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
  public String toString()
  {
    lock();
    try
    {
      return Linq.toString(this);
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Locks the object
   */
  @Override
  public void lock()
  {
    lockObject.lock();
  }

  /**
   * Unlocks the object.
   * 
   * @throws IllegalMonitorStateException if the current thread does not hold this lock
   */
  @Override
  public void unlock()
  {
    lockObject.unlock();
  }
}
