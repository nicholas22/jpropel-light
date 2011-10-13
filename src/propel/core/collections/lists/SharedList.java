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
package propel.core.collections.lists;

import propel.core.collections.ReifiedIterable;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import propel.core.utils.SuperTypeTokenException;

/**
 * An type-aware array-backed thread-safe list. This collection allows nulls to be inserted.
 * 
 * Instantiate using e.g.: new SharedList&lt;String&gt;(){}; -OR- new SharedList&lt;String&gt;(String.class);
 */
public class SharedList<T>
    extends ReifiedArrayList<T>
    implements ISharedList<T>
{
  private final ReentrantLock reEntrantLock;

  /**
   * Default constructor
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public SharedList()
  {
    super();
    reEntrantLock = new ReentrantLock();
  }

  /**
   * Constructor for initializing with the generic type parameter
   * 
   * @throws NullPointerException When the generic type parameter is null.
   */
  public SharedList(Class<?> genericTypeParameter)
  {
    super(genericTypeParameter);
    reEntrantLock = new ReentrantLock();
  }

  /**
   * Constructor initializes with an initial collection size.
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   * @throws IllegalArgumentException When the size is non positive.
   */
  public SharedList(int initialSize)
  {
    super(initialSize);
    reEntrantLock = new ReentrantLock();
  }

  /**
   * Constructor initializes with an initial collection size and a generic type parameter.
   * 
   * @throws IllegalArgumentException When the buffer size is non positive.
   * @throws NullPointerException When the generic type parameter is null.
   */
  public SharedList(int initialSize, Class<?> genericTypeParameter)
  {
    super(initialSize, genericTypeParameter);
    reEntrantLock = new ReentrantLock();
  }

  /**
   * Constructor initializes from another reified collection
   * 
   * @throws NullPointerException When the argument is null
   */
  public SharedList(ReifiedIterable<T> iterable)
  {
    super(iterable);
    reEntrantLock = new ReentrantLock();
  }

  /**
   * Constructor initializes with an initial collection and this class's generic type parameter
   * 
   * @throws NullPointerException When the argument is null
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public SharedList(Iterable<? extends T> iterable)
  {
    super(iterable);
    reEntrantLock = new ReentrantLock();
  }

  /**
   * Constructor initializes with an initial collection and this class's generic type parameter
   * 
   * @throws NullPointerException When an argument is null
   */
  public SharedList(Iterable<? extends T> iterable, Class<?> genericTypeParameter)
  {
    super(iterable, genericTypeParameter);
    reEntrantLock = new ReentrantLock();
  }

  /**
   * Initializes with an array.
   * 
   * @throws NullPointerException When the argument is null
   */
  public SharedList(T[] array)
  {
    super(array);
    reEntrantLock = new ReentrantLock();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean add(T t)
  {
    lock();
    try
    {
      return super.add(t);
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
  public void add(int index, T element)
  {
    lock();
    try
    {
      super.add(index, element);
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
  public boolean addAll(Collection<? extends T> c)
  {
    lock();
    try
    {
      return super.addAll(c);
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
  public boolean addAll(int index, Collection<? extends T> c)
  {
    lock();
    try
    {
      return super.addAll(index, c);
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
  public boolean addIfAbsent(T item)
  {
    lock();
    try
    {
      if (!super.contains(item))
      {
        super.add(item);
        return true;
      }

      return false;
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
  public boolean contains(Object o)
  {
    lock();
    try
    {
      return super.contains(o);
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
  public boolean containsAll(Collection<?> c)
  {
    lock();
    try
    {
      return super.containsAll(c);
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
  public boolean isEmpty()
  {
    lock();
    try
    {
      return super.size() == 0;
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
  public int indexOf(Object o)
  {
    lock();
    try
    {
      return super.indexOf(o);
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Returns an iterator over a copy of the collection. This is an O(n) operation.
   */
  @Override
  public Iterator<T> iterator()
  {
    lock();
    try
    {
      return (new ArrayList<T>(this)).iterator();
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
  public int lastIndexOf(Object o)
  {
    lock();
    try
    {
      return super.lastIndexOf(o);
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
  public ListIterator<T> listIterator()
  {
    lock();
    try
    {
      return (new ArrayList<T>(this)).listIterator();
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
  public ListIterator<T> listIterator(int index)
  {
    lock();
    try
    {
      return (new ArrayList<T>(this)).listIterator(index);
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
  public boolean remove(Object o)
  {
    lock();
    try
    {
      return super.remove(o);
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
  public boolean removeAll(Collection<?> c)
  {
    lock();
    try
    {
      return super.removeAll(c);
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
  public boolean retainAll(Collection<?> c)
  {
    lock();
    try
    {
      return super.retainAll(c);
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
  @SuppressWarnings("hiding")
  @Override
  public <T> T[] toArray(T[] a)
  {
    lock();
    try
    {
      return super.toArray(a);
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
      return new ReifiedArrayList<T>(this, getGenericTypeParameter()) {};
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
  public T get(int index)
  {
    lock();
    try
    {
      return super.get(index);
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
  public T set(int index, T element)
  {
    lock();
    try
    {
      return super.set(index, element);
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Clears the list and puts all given elements. This is an O(n) operation.
   */
  public void replaceAll(Collection<? extends T> c)
  {
    lock();
    try
    {
      super.clear();
      super.addAll(c);
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Removes a number of items from the collection. Returns true for those successfully removed, false for those that were not found. This
   * is an O(n) operation.
   */
  public Iterable<Boolean> removeAll(Iterable<? extends T> items)
  {
    lock();
    try
    {
      List<Boolean> result = new ArrayList<Boolean>();
      for (T item : items)
        result.add(super.remove(item));

      return result;
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
  public T remove(int index)
  {
    lock();
    try
    {
      return super.remove(index);
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
  public ReifiedList<T> subList(int fromIndex, int toIndex)
  {
    lock();
    try
    {
      return super.subList(fromIndex, toIndex);
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
    reEntrantLock.lock();
  }

  /**
   * Unlocks the collection
   * 
   * @throws IllegalMonitorStateException if the current thread does not hold this lock
   */
  @Override
  public void unlock()
  {
    reEntrantLock.unlock();
  }
}
