// Ported to Java by Nikolaos Tountas
//
// GOLETAS COMMUNITY SOURCE CODE LICENSE AGREEMENT
// Version: June 11, 2006
// Copyright © 2005 - 2008 Maksim Goleta. All Rights Reserved.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of this source code and associated documentation files (the
// "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish,
// distribute, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
// following conditions:
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software or
// documentation (and/or other materials) for the binary forms, if any, produced from the Software or derived source code.
// Products derived from the Software may not be called Goletas, nor may Goletas appear in their name without the prior written permission
// of Goletas. This license does not grant you any rights to use Goletas’ logos or trademarks. If you alter the Software in any way, you
// must remove the modified source code from the Goletas code namespace, if applicable, and cause the modified files to carry prominent
// notices stating that you changed the files.
// If you begin patent litigation against Goletas over patents that you think may apply to the Software (including a cross-claim or
// counterclaim in a lawsuit), your license to the Software is terminated automatically.
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS. IN NO EVENT SHALL GOLETAS, THE CONTRIBUTORS
// OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
// Except as contained in this notice, neither the name Goletas nor the names of contributors or copyright holders may be used in
// advertising or otherwise to promote the sale, use or other dealings in the Software or products derived from the Software without the
// specific prior written authorization.

package propel.core.collections.maps.avl;

import propel.core.collections.ReifiedIterable;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.lists.ReifiedList;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents an immutable type-aware collection of keys.
 */
public final class KeyCollection<TKey extends Comparable<TKey>, TValue>
    implements Collection<TKey>, ReifiedIterable<TKey>
{
  private final String READ_ONLY_COLLECTION = "The collection is read-only";
  private final AvlHashtable<TKey, TValue> dictionary;
  private final Class<?> genericTypeParameter;

  /**
   * Initializes with a new key collection. This is an O(1) operation.
   */
  public KeyCollection(AvlHashtable<TKey, TValue> dictionary, Class<?> genericTypeParameterKey)
  {
    this.dictionary = dictionary;
    this.genericTypeParameter = genericTypeParameterKey;
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
  @SuppressWarnings("unchecked")
  @Override
  public boolean contains(Object item)
  {
    if (item == null)
      throw new NullPointerException("item");

    TKey key = (TKey) item;

    return contains(key);
  }

  /**
   * Returns true if this collection contains the specified element. This is an O(log2(n)) operation.
   * 
   * @param item Element whose presence in this collection is to be tested.
   * 
   * @return True if this collection contains the specified element.
   * 
   * @throws NullPointerException The specified element is null.
   */
  public boolean contains(TKey item)
  {
    if (item == null)
      throw new NullPointerException("item");

    AvlNode<TKey, TValue> p = dictionary.root;

    while (p != null)
    {
      int c = item.compareTo(p.item.getKey());

      if (c < 0)
        p = p.left;
      else if (c > 0)
        p = p.right;
      else
        return true;
    }

    return false;
  }

  /**
   * Returns true if this collection contains all the specified elements. This is an O(nlog2(n)) operation.
   * 
   * @param collection Contains all elements to search for.
   * 
   * @return True if this collection contains all the specified elements.
   * 
   * @throws ClassCastException An item is not of correct type.
   * @throws NullPointerException The specified collection is null.
   */
  @Override
  public boolean containsAll(Collection<?> collection)
  {
    if (collection == null)
      throw new NullPointerException("The collection cannot be null.");

    for (Object item : collection)
      if (!contains(item))
        return false;

    return true;
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
   * Returns true if empty. This is an O(1) operation.
   * 
   * @return True if this collection contains no elements
   */
  @Override
  public boolean isEmpty()
  {
    return dictionary.size() == 0;
  }

  /**
   * @return An Iterator over the elements in this collection
   */
  @Override
  public Iterator<TKey> iterator()
  {
    AvlNode<TKey, TValue> p = dictionary.root;

    if (p != null)
      while (p.left != null)
        p = p.left;

    return new AscendingOrderKeyIterator<TKey, TValue>(p);
  }

  /**
   * Returns the size of the collection. This is an O(1) operation.
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
   * 
   * @return An ordered list with all keys.
   */
  public ReifiedList<TKey> toList()
  {
    return new ReifiedArrayList<TKey>(dictionary.getKeys(), getGenericTypeParameter());
  }

  /**
   * Returns the key of all key/value pairs, in ascending key order. This is an O(n) operation.
   */
  @SuppressWarnings("unchecked")
  @Override
  public TKey[] toArray()
  {
    int size = size();
    TKey[] result = (TKey[]) Array.newInstance(getGenericTypeParameter(), size);

    Iterator<TKey> iterator = iterator();
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
   * @throws UnsupportedOperationException Always thrown.
   */
  @Deprecated
  @Override
  public boolean add(TKey tKey)
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
  public boolean addAll(Collection<? extends TKey> c)
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
