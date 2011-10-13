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
package propel.core.collections.stacks;

import propel.core.collections.ReifiedIterable;
import propel.core.collections.lists.ReifiedLinkedList;
import propel.core.collections.lists.ReifiedList;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A shared stack for use by multiple threads. This collection allows null items to be inserted.
 * 
 * Instantiate using e.g.: new SharedStack&lt;String&gt;(){}; -OR- new SharedStack&lt;String&gt;(String.class);
 */
public class SharedStack<T>
    implements ISharedStack<T>
{
  private final ReentrantLock lockObject;
  protected final Condition notEmpty;
  private final ReifiedLinkedList<T> stack;

  /**
   * Default constructor.
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public SharedStack()
  {
    stack = new ReifiedLinkedList<T>(SuperTypeToken.getClazz(this.getClass()));
    lockObject = new ReentrantLock();
    notEmpty = lockObject.newCondition();
  }

  /**
   * Constructor for initializing with the generic type parameter
   * 
   * @throws NullPointerException When the generic type parameter is null.
   */
  public SharedStack(Class<?> genericTypeParameter)
  {
    if (genericTypeParameter == null)
      throw new NullPointerException("genericTypeParameter");

    stack = new ReifiedLinkedList<T>(genericTypeParameter);
    lockObject = new ReentrantLock();
    notEmpty = lockObject.newCondition();
  }

  /**
   * Constructor initializes from another reified collection
   * 
   * @throws NullPointerException When the argument is null
   */
  public SharedStack(ReifiedIterable<T> iterable)
  {
    if (iterable == null)
      throw new NullPointerException("iterable");

    stack = new ReifiedLinkedList<T>(iterable.getGenericTypeParameter());
    lockObject = new ReentrantLock();
    notEmpty = lockObject.newCondition();

    for (T item : iterable)
      stack.add(item);
  }

  /**
   * Constructor initializes with an initial collection and this class's generic type parameter
   * 
   * @throws NullPointerException When the argument is null
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public SharedStack(Iterable<? extends T> iterable)
  {
    if (iterable == null)
      throw new NullPointerException("iterable");

    stack = new ReifiedLinkedList<T>(SuperTypeToken.getClazz(this.getClass()));
    lockObject = new ReentrantLock();
    notEmpty = lockObject.newCondition();

    for (T item : iterable)
      stack.add(item);
  }

  /**
   * Constructor initializes with an initial collection and this class's generic type parameter
   * 
   * @throws NullPointerException When an argument is null
   */
  public SharedStack(Iterable<? extends T> iterable, Class<?> genericTypeParameter)
  {
    if (iterable == null)
      throw new NullPointerException("iterable");
    if (genericTypeParameter == null)
      throw new NullPointerException("genericTypeParameter");

    stack = new ReifiedLinkedList<T>(genericTypeParameter);
    lockObject = new ReentrantLock();
    notEmpty = lockObject.newCondition();

    for (T item : iterable)
      stack.add(item);
  }

  /**
   * Initializes with an array.
   * 
   * @throws NullPointerException When the argument is null
   */
  public SharedStack(T[] array)
  {
    if (array == null)
      throw new NullPointerException("array");

    stack = new ReifiedLinkedList<T>(array.getClass().getComponentType());
    lockObject = new ReentrantLock();
    notEmpty = lockObject.newCondition();

    for (T item : array)
      stack.add(item);
  }

  /**
   * Clears the stack. This is an O(1) operation.
   */
  @Override
  public void clear()
  {
    lock();
    try
    {
      stack.clear();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Pops an object, blocking if none is available
   */
  @Override
  public T get()
  {
    lock();
    try
    {
      while (stack.size() <= 0)
      {
        try
        {
          notEmpty.await(); // Monitor.Wait(lockObject);
        }
        catch(InterruptedException e)
        {
          continue;
        }
      }

      return stack.pop();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Dequeues a number of objects, blocking if not all are available.
   * 
   * @throws IllegalArgumentException When the argument is out of range
   */
  @Override
  public Iterable<T> getRange(int count)
  {
    if (count < 0)
      throw new IllegalArgumentException("count=" + count);

    List<T> result = new ArrayList<T>(count);

    lock();
    try
    {
      for (int i = 0; i < count; i++)
      {
        while (stack.size() <= 0)
        {
          try
          {
            notEmpty.await(); // Monitor.Wait(lockObject);
          }
          catch(InterruptedException e)
          {
            continue;
          }
        }

        result.add(stack.pop());
      }
    }
    finally
    {
      unlock();
    }

    return result;
  }

  /**
   * Returns an iterator of a copy of the collection. This is an O(n) operation.
   */
  @Override
  public Iterator<T> iterator()
  {
    lock();
    try
    {
      return stack.toList().iterator();
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
  public Class<?> getGenericTypeParameter()
  {
    return stack.getGenericTypeParameter();
  }

  /**
   * Returns the next element without removing it. If there are no elements, an exception will be thrown. This is an O(1) operation.
   * 
   * @throws NoSuchElementException When the stack is empty
   */
  @Override
  public T peek()
  {
    lock();
    try
    {
      if (stack.size() <= 0)
        throw new NoSuchElementException("The stack is empty.");

      return stack.peek();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Pushes an object in the stack. This is an O(1) operation.
   */
  @Override
  public void put(T item)
  {
    lock();
    try
    {
      stack.push(item);
      notEmpty.signalAll(); // Monitor.PulseAll(lockObject);
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Pushes a number of objects. This is an O(n) operation where n is the item count.
   * 
   * @throws NullPointerException When the items argument is null.
   */
  @Override
  public void putRange(Iterable<? extends T> items)
  {
    if (items == null)
      throw new NullPointerException("items");

    lock();
    try
    {
      for (T item : items)
        stack.push(item);

      notEmpty.signalAll(); // Monitor.PulseAll(lockObject);
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Returns the queue length. This is an O(1) operation.
   */
  @Override
  public int size()
  {
    lock();
    try
    {
      return stack.size();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Puts all elements in an array and returns them. This is an O(n) operation.
   */
  @Override
  public T[] toArray()
  {
    lock();
    try
    {
      return stack.toArray();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Puts all elements in a list and returns them. This is an O(n) operation.
   */
  @Override
  public ReifiedList<T> toList()
  {
    lock();
    try
    {
      return stack.toList();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Locks the collection
   */
  @Override
  public void lock()
  {
    lockObject.lock();
  }

  /**
   * Unlocks the collection
   * 
   * @throws IllegalMonitorStateException The current thread does not own the lock
   */
  @Override
  public void unlock()
  {
    lockObject.unlock();
  }

}
