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

package propel.core.collections.maps.avl;

import propel.core.collections.ReifiedIterable;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.lists.ReifiedList;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents an immutable type-aware collection of values.
 */
public final class ValueCollection<TKey extends Comparable<TKey>, TValue>
    implements Collection<TValue>, ReifiedIterable<TValue>
{
  private final String READ_ONLY_COLLECTION = "The collection is read-only";
  private final AvlHashtable<TKey, TValue> dictionary;
  private final Class<?> genericTypeParameter;

  /**
   * Initializes with a new value collection. This is an O(1) operation.
   */
  public ValueCollection(AvlHashtable<TKey, TValue> dictionary, Class<?> genericTypeParameterValue)
  {
    this.dictionary = dictionary;
    this.genericTypeParameter = genericTypeParameterValue;
  }

  /**
   * Returns true if this collection contains the specified element. This is an O(log2(n)) operation.
   * 
   * @param item Element whose presence in this collection is to be tested.
   * 
   * @return True if this collection contains the specified element.
   * 
   * @throws ClassCastException The item is not of correct type.
   * @throws NullPointerException The specified element is null.
   */
  @Override
  public boolean contains(Object item)
  {
    if (item == null)
      throw new NullPointerException("item");

    AvlNode<TKey, TValue> p = dictionary.root;

    if (p == null)
      return false;

    while (p.left != null)
      p = p.left;

    while (true)
    {
      if (item.equals(p.item.getValue()))
        return true;

      if (p.right == null)
      {
        while (true)
        {
          if (p.parent == null)
            return false;

          if (p != p.parent.right)
            break;

          p = p.parent;
        }

        p = p.parent;
      } else
      {
        p = p.right;

        while (p.left != null)
          p = p.left;
      }
    }

    // // item == null
    // while(true)
    // {
    // if(p.item.getValue() == null)
    // return true;
    //
    // if(p.right == null)
    // {
    // while(true)
    // {
    // if(p.parent == null)
    // return false;
    //
    // if(p != p.parent.right)
    // break;
    //
    // p = p.parent;
    // }
    //
    // p = p.parent;
    // }
    // else
    // {
    // p = p.right;
    //
    // while(p.left != null)
    // p = p.left;
    // }
    // }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameter()
  {
    return genericTypeParameter;
  }

  /**
   * This is an O(1) operation.
   * 
   * @return True if this collection contains no elements
   */
  @Override
  public boolean isEmpty()
  {
    return dictionary.size() == 0;
  }

  /**
   * This is an O(nlog2(n)) operation, taking element traversal into account.
   * 
   * @return An Iterator over the elements in this collection
   */
  @Override
  public Iterator<TValue> iterator()
  {
    AvlNode<TKey, TValue> p = dictionary.root;

    if (p != null)
      while (p.left != null)
        p = p.left;

    return new AscendingOrderValueIterator<TKey, TValue>(p);
  }

  /**
   * This is an O(1) operation.
   * 
   * @return The number of elements in this collection
   */
  @Override
  public int size()
  {
    return dictionary.size();
  }

  /**
   * Returns the key of all key/value pairs, in ascending key order. This is an O(n) operation.
   */
  @SuppressWarnings("unchecked")
  @Override
  public TValue[] toArray()
  {
    int size = size();
    TValue[] result = (TValue[]) Array.newInstance(getGenericTypeParameter(), size);

    Iterator<TValue> iterator = iterator();
    for (int i = 0; i < size; i++)
      result[i] = iterator.next();

    return result;
  }

  /**
   * @throws UnsupportedOperationException Always thrown.
   */
  @Deprecated
  @Override
  public <T> T[] toArray(T[] array)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the key of all key/value pairs, in ascending key order. This is an O(n) operation.
   * 
   * @return An ordered list with all keys.
   */
  public ReifiedList<TValue> toList()
  {
    return new ReifiedArrayList<TValue>(dictionary.getValues(), getGenericTypeParameter());
  }

  /**
   * @throws UnsupportedOperationException Always thrown.
   */
  @Deprecated
  @Override
  public boolean add(TValue tVal)
  {
    throw new UnsupportedOperationException(READ_ONLY_COLLECTION);
  }

  /**
   * @throws UnsupportedOperationException Always thrown.
   */
  @Deprecated
  @Override
  public boolean remove(Object o)
  {
    throw new UnsupportedOperationException(READ_ONLY_COLLECTION);
  }

  /**
   * @throws UnsupportedOperationException Always thrown.
   */
  @Deprecated
  @Override
  public boolean containsAll(Collection<?> c)
  {
    throw new UnsupportedOperationException(READ_ONLY_COLLECTION);
  }

  /**
   * @throws UnsupportedOperationException Always thrown.
   */
  @Deprecated
  @Override
  public boolean addAll(Collection<? extends TValue> c)
  {
    throw new UnsupportedOperationException(READ_ONLY_COLLECTION);
  }

  /**
   * @throws UnsupportedOperationException Always thrown.
   */
  @Deprecated
  @Override
  public boolean removeAll(Collection<?> c)
  {
    throw new UnsupportedOperationException(READ_ONLY_COLLECTION);
  }

  /**
   * @throws UnsupportedOperationException Always thrown.
   */
  @Deprecated
  @Override
  public boolean retainAll(Collection<?> c)
  {
    throw new UnsupportedOperationException(READ_ONLY_COLLECTION);
  }

  /**
   * @throws UnsupportedOperationException Always thrown.
   */
  @Deprecated
  @Override
  public void clear()
  {
    throw new UnsupportedOperationException(READ_ONLY_COLLECTION);
  }
}
