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
package propel.core.collections.sets;

import propel.core.collections.ReifiedIterable;
import propel.core.collections.lists.ReifiedList;
import propel.core.model.IShared;
import propel.core.utils.Linq;
import propel.core.utils.SuperTypeTokenException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A thread-safe type-aware AVL-tree-backed set. This collection does not allow null items to be inserted.
 * 
 * Instantiate using e.g.: new SharedAvlTreeSet&lt;String&gt;(){}; -OR- new SharedAvlTreeSet&lt;String&gt;(String.class);
 */
public class SharedAvlTreeSet<T extends Comparable<T>>
    extends AvlTreeSet<T>
    implements IShared
{
  private final ReentrantLock reEntrantLock;

  /**
   * Default constructor
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public SharedAvlTreeSet()
  {
    super();
    reEntrantLock = new ReentrantLock();
  }

  /**
   * Constructor for initializing with the generic type parameter
   * 
   * @throws NullPointerException When the generic type parameter is null.
   */
  public SharedAvlTreeSet(Class<?> genericTypeParameter)
  {
    super(genericTypeParameter);
    reEntrantLock = new ReentrantLock();
  }

  /**
   * Constructor initializes from another reified collection
   * 
   * @throws NullPointerException When the argument is null
   */
  public SharedAvlTreeSet(ReifiedIterable<T> iterable)
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
  public SharedAvlTreeSet(Iterable<? extends T> iterable)
  {
    super(iterable);
    reEntrantLock = new ReentrantLock();
  }

  /**
   * Constructor initializes with an initial collection and this class's generic type parameter
   * 
   * @throws NullPointerException When an argument is null
   */
  public SharedAvlTreeSet(Iterable<? extends T> iterable, Class<?> genericTypeParameter)
  {
    super(iterable, genericTypeParameter);
    reEntrantLock = new ReentrantLock();
  }

  /**
   * Constructor initializes with an array
   * 
   * @throws NullPointerException When an argument is null
   */
  public SharedAvlTreeSet(T[] array)
  {
    super(array);
    reEntrantLock = new ReentrantLock();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean add(T item)
  {
    lock();
    try
    {
      return super.add(item);
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
  public boolean contains(T item)
  {
    lock();
    try
    {
      return super.contains(item);
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Returns an enumerator of a copy of the collection. This is an O(n) operation
   */
  @Override
  public Iterator<T> iterator()
  {
    lock();
    try
    {
      return super.toList().iterator();
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
  public boolean remove(T item)
  {
    lock();
    try
    {
      return super.remove(item);
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Clears the set and bulk adds the specified items to avoid locking/unlocking continuously. Returns true for elements that were
   * successfully added, false for those that were not. You are advised not to use null keys, as these are not allowed and failing will
   * leave the collection in an inconsistent state. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException When the items is null
   */
  public Iterable<Boolean> replaceAll(Collection<? extends T> items)
  {
    if (items == null)
      throw new NullPointerException("items");

    lock();
    try
    {
      clear();

      List<Boolean> result = new ArrayList<Boolean>(items.size());
      for (T item : items)
        result.add(this.add(item));

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
  public void union(Set<? extends T> otherSet)
  {
    lock();
    try
    {
      super.union(otherSet);
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
  public void intersect(Set<? extends T> otherSet)
  {
    lock();
    try
    {
      super.intersect(otherSet);
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
  public void difference(Set<? extends T> otherSet)
  {
    lock();
    try
    {
      super.difference(otherSet);
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
   * @throws IllegalMonitorStateException If the current thread does not hold this lock
   */
  @Override
  public void unlock()
  {
    reEntrantLock.unlock();
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
}
