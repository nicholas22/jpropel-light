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
import propel.core.collections.maps.avl.AvlHashtable;
import propel.core.utils.Linq;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;
import java.util.Iterator;
import java.util.Set;

/**
 * A type-aware AVL-tree-backed set. This collection does not allow null items to be inserted.
 * 
 * Instantiate using e.g.: new AvlTreeSet&lt;String&gt;(){}; -OR- new AvlTreeSet&lt;String&gt;(String.class);
 */
public class AvlTreeSet<T extends Comparable<T>>
    implements ReifiedSet<T>
{
  private AvlHashtable<T, Object> store;

  /**
   * Default constructor
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public AvlTreeSet()
  {
    store = new AvlHashtable<T, Object>(SuperTypeToken.getClazz(this.getClass()), Object.class);
  }

  /**
   * Constructor for initializing with the generic type parameter
   * 
   * @throws NullPointerException When the generic type parameter is null.
   */
  public AvlTreeSet(Class<?> genericTypeParameter)
  {
    if (genericTypeParameter == null)
      throw new NullPointerException("genericTypeParameter");

    store = new AvlHashtable<T, Object>(genericTypeParameter, Object.class);
  }

  /**
   * Constructor initializes from another reified collection
   * 
   * @throws NullPointerException When the argument is null
   */
  public AvlTreeSet(ReifiedIterable<T> iterable)
  {
    if (iterable == null)
      throw new NullPointerException("iterable");

    store = new AvlHashtable<T, Object>(iterable.getGenericTypeParameter(), Object.class);
    for (T item : iterable)
      store.add(item, null);
  }

  /**
   * Constructor initializes with an initial collection and this class's generic type parameter
   * 
   * @throws NullPointerException When the argument is null
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public AvlTreeSet(Iterable<? extends T> iterable)
  {
    if (iterable == null)
      throw new NullPointerException("iterable");

    store = new AvlHashtable<T, Object>(SuperTypeToken.getClazz(this.getClass()), Object.class);
    for (T item : iterable)
      store.add(item, null);
  }

  /**
   * Constructor initializes with an initial collection and this class's generic type parameter
   * 
   * @throws NullPointerException When an argument is null
   */
  public AvlTreeSet(Iterable<? extends T> iterable, Class<?> genericTypeParameter)
  {
    if (iterable == null)
      throw new NullPointerException("iterable");

    store = new AvlHashtable<T, Object>(genericTypeParameter, Object.class);
    for (T item : iterable)
      store.add(item, null);
  }

  /**
   * Constructor initializes with an array
   * 
   * @throws NullPointerException When an argument is null
   */
  public AvlTreeSet(T[] array)
  {
    if (array == null)
      throw new NullPointerException("array");

    store = new AvlHashtable<T, Object>(array.getClass().getComponentType(), Object.class);
    for (T item : array)
      store.add(item, null);
  }

  /**
   * Clears the collection. This is an O(1) operation.
   */
  @Override
  public void clear()
  {
    store.clear();
  }

  /**
   * Adds an item to the collection This is an O(log2(n)) operation.
   * 
   * @return True if successful.
   * 
   * @throws NullPointerException When the item is null.
   */
  @Override
  public boolean add(T item)
  {
    if (item == null)
      throw new NullPointerException("item");

    return store.add(item, null);
  }

  /**
   * Returns true if an item is contained in the collection. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException When the item is null.
   */
  @Override
  public boolean contains(T item)
  {
    if (item == null)
      throw new NullPointerException("item");

    return store.getKeys().contains(item);
  }

  /**
   * Returns an ascending key order iterator over the set. This is an O(nlog2(n)) operation, taking element traversal into account.
   */
  @Override
  public Iterator<T> iterator()
  {
    return store.getKeys().iterator();
  }

  /**
   * Removes an item from the collection. This is an O(log2(n)) operation.
   * 
   * @return True if item was found and removed.
   * 
   * @throws NullPointerException When the item is null.
   */
  @Override
  public boolean remove(T item)
  {
    if (item == null)
      throw new NullPointerException("item");

    return store.remove(item);
  }

  /**
   * Returns the collection size. This is an O(1) operation.
   */
  @Override
  public int size()
  {
    return store.size();
  }

  /**
   * Puts all elements in an array and returns them. This is an O(n) operation.
   */
  @Override
  public T[] toArray()
  {
    return store.getKeys().toArray();
  }

  /**
   * Puts all elements in a list and returns them. This is an O(n) operation.
   */
  @Override
  public ReifiedList<T> toList()
  {
    return store.getKeys().toList();
  }

  /**
   * This operation combines this set with another set i.e. this set will add to this collection all non-preexisting items found in the
   * other set. This is an O(nlog2(n)) operation.
   * 
   * @throws NullPointerException If the other set is null.
   */
  @Override
  public void union(Set<? extends T> otherSet)
  {
    if (otherSet == null)
      throw new NullPointerException("otherSet");

    for (T item : otherSet)
      if (!contains(item))
        add(item);
  }

  /**
   * This operation only keeps elements that are common between the two sets. This is an O(nlog2(n)) operation.
   * 
   * @throws NullPointerException When the other set is null.
   */
  @Override
  public void intersect(Set<? extends T> otherSet)
  {
    if (otherSet == null)
      throw new NullPointerException("otherSet");

    AvlHashtable<T, Object> newStore = new AvlHashtable<T, Object>();

    if (otherSet.size() > store.size())
    {
      for (T item : otherSet)
        if (store.containsKey(item))
          newStore.add(item, null);
    }

    store = newStore;
  }

  /**
   * This operation removes from this set all items that are common between both sets. This is an O(nlog2(n)) operation.
   * 
   * @throws NullPointerException If the other set is null.
   */
  @Override
  public void difference(Set<? extends T> otherSet)
  {
    if (otherSet == null)
      throw new NullPointerException("otherSet");

    for (T item : otherSet)
      if (contains(item))
        remove(item);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameter()
  {
    return store.getGenericTypeParameterKey();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return Linq.toString(this);
  }
}
