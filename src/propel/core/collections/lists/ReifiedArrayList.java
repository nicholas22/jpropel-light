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
import propel.core.collections.arrays.ReadOnlyArrayIterator;
import propel.core.utils.Linq;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * A type-aware array-backed list that uses similar semantics and strategy to the java.util.ArrayList. This collection allows nulls to be
 * inserted.
 * 
 * Instantiate using e.g.: new ReifiedArrayList&lt;String&gt;(){}; -OR- new ReifiedArrayList&lt;String&gt;(String.class);
 */
public class ReifiedArrayList<T>
    implements ReifiedList<T>
{
  public static final int DEFAULT_SIZE = 64;
  private final Class<?> genericTypeParameter;
  private T[] buffer;
  private int realListSize;

  /**
   * Default constructor
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public ReifiedArrayList()
  {
    this(DEFAULT_SIZE);
  }

  /**
   * Constructor for initializing with the generic type parameter
   * 
   * @throws NullPointerException When the generic type parameter is null.
   */
  public ReifiedArrayList(Class<?> genericTypeParameter)
  {
    this(DEFAULT_SIZE, genericTypeParameter);
  }

  /**
   * Constructor initializes with an initial collection size.
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   * @throws IllegalArgumentException When the size is non positive.
   */
  @SuppressWarnings("unchecked")
  public ReifiedArrayList(int initialSize)
  {
    if (initialSize < 0)
      throw new IllegalArgumentException("initialSize=" + initialSize);

    // retrieves first generic parameter
    genericTypeParameter = SuperTypeToken.getClazz(this.getClass());

    // init array
    buffer = (T[]) Array.newInstance(genericTypeParameter, initialSize);
  }

  /**
   * Constructor initializes with an initial collection size and a generic type parameter.
   * 
   * @throws IllegalArgumentException When the buffer size is non positive.
   * @throws NullPointerException When the generic type parameter is null.
   */
  @SuppressWarnings("unchecked")
  public ReifiedArrayList(int initialSize, Class<?> genericTypeParameter)
  {
    if (initialSize < 0)
      throw new IllegalArgumentException("initialSize=" + initialSize);
    if (genericTypeParameter == null)
      throw new NullPointerException("genericTypeParameter");
    this.genericTypeParameter = genericTypeParameter;

    // init array
    buffer = (T[]) Array.newInstance(genericTypeParameter, initialSize);
  }

  /**
   * Constructor initializes from another reified collection
   * 
   * @throws NullPointerException When the argument is null
   */
  @SuppressWarnings("unchecked")
  public ReifiedArrayList(ReifiedIterable<T> iterable)
  {
    if (iterable == null)
      throw new NullPointerException("iterable");

    int size = Linq.count(iterable);
    genericTypeParameter = iterable.getGenericTypeParameter();

    // init array
    buffer = (T[]) Array.newInstance(genericTypeParameter, Math.max(size, DEFAULT_SIZE));

    Iterator<T> iterator = iterable.iterator();

    for (int i = 0; i < size; i++)
      buffer[i] = iterator.next();
    realListSize = size;
  }

  /**
   * Constructor initializes with an initial collection and this class's generic type parameter
   * 
   * @throws NullPointerException When the argument is null
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  @SuppressWarnings("unchecked")
  public ReifiedArrayList(Iterable<? extends T> iterable)
  {
    if (iterable == null)
      throw new NullPointerException("iterable");

    int size = Linq.count(iterable);

    // retrieves first generic parameter
    genericTypeParameter = SuperTypeToken.getClazz(this.getClass());

    // init array
    buffer = (T[]) Array.newInstance(genericTypeParameter, Math.max(size, DEFAULT_SIZE));

    Iterator<? extends T> iterator = iterable.iterator();

    for (int i = 0; i < size; i++)
      buffer[i] = iterator.next();
    realListSize = size;
  }

  /**
   * Constructor initializes with an initial collection and this class's generic type parameter
   * 
   * @throws NullPointerException When an argument is null
   */
  @SuppressWarnings("unchecked")
  public ReifiedArrayList(Iterable<? extends T> iterable, Class<?> genericTypeParameter)
  {
    if (iterable == null)
      throw new NullPointerException("iterable");
    if (genericTypeParameter == null)
      throw new NullPointerException("genericTypeParameter");

    int size = Linq.count(iterable);

    // retrieves first generic parameter
    this.genericTypeParameter = genericTypeParameter;

    // init array
    buffer = (T[]) Array.newInstance(genericTypeParameter, Math.max(size, DEFAULT_SIZE));

    Iterator<? extends T> iterator = iterable.iterator();

    for (int i = 0; i < size; i++)
      buffer[i] = iterator.next();
    realListSize = size;
  }

  /**
   * Initializes with an array.
   * 
   * @throws NullPointerException When the argument is null
   */
  public ReifiedArrayList(T[] array)
  {
    if (array == null)
      throw new NullPointerException("array");

    buffer = array;
    genericTypeParameter = array.getClass().getComponentType();
    realListSize = array.length;
  }

  /**
   * Adds an element to the collection. This is an O(1) operation.
   * 
   * @return True always.
   */
  @Override
  public boolean add(T t)
  {
    ensureCapacity(realListSize + 1);

    buffer[realListSize++] = t;
    return true;
  }

  /**
   * Inserts the specified element at the specified position in this list. Shifts the element currently at that position and any subsequent
   * elements to the right (adds one to their indices). This is an O(1) operation.
   * 
   * @throws IndexOutOfBoundsException If the index is out of range
   */
  @Override
  public void add(int index, T element)
  {
    if (index < 0 || index >= realListSize)
      throw new IndexOutOfBoundsException("index=" + index + " realListSize=" + realListSize);

    ensureCapacity(realListSize + 1);

    System.arraycopy(buffer, index, buffer, index + 1, realListSize - index);
    buffer[index] = element;

    realListSize++;
  }

  /**
   * Appends all of the elements in the specified collection to the end of this list, in the order that they are returned by the specified
   * collection's Iterator. This is an O(n) operation where n is the size of the given collection.
   * 
   * @return True, if this list changed as a result of the call
   * 
   * @throws NullPointerException If the collection is null.
   */
  @Override
  public boolean addAll(Collection<? extends T> c)
  {
    if (c == null)
      throw new NullPointerException("c");

    int size = c.size();
    if (size <= 0)
      return false;

    ensureCapacity(realListSize + size);

    Iterator<? extends T> iterator = c.iterator();
    for (int i = 0; i < size; i++)
      buffer[i + realListSize] = iterator.next();

    realListSize += size;
    return true;
  }

  /**
   * Appends all of the elements in the specified array to the end of this list, in the order that they are ordered.
   * 
   * This is an O(n) operation where n is the size of the given array.
   * 
   * @return True, if this list changed as a result of the call
   * 
   * @throws NullPointerException If the array is null.
   */
  @Override
  public boolean addAll(T[] array)
  {
    if (array == null)
      throw new NullPointerException("array");

    int size = array.length;
    if (size <= 0)
      return false;

    ensureCapacity(realListSize + size);

    System.arraycopy(array, 0, buffer, realListSize, size);

    realListSize += size;
    return true;
  }

  /**
   * Inserts the specified elements at the specified position in this list. Shifts the element currently at that position and any subsequent
   * elements to the right (adds to their indices). This is an O(n) operation where n is the size of the given collection.
   * 
   * @throws IndexOutOfBoundsException If the index is out of range
   * @throws NullPointerException The collection provided is null.
   */
  @Override
  public boolean addAll(int index, Collection<? extends T> collection)
  {
    if (collection == null)
      throw new NullPointerException("collection");
    if (index < 0 || index >= realListSize)
      throw new IndexOutOfBoundsException("index=" + index + " realListSize=" + realListSize);

    int size = collection.size();
    if (size <= 0)
      return false;

    ensureCapacity(realListSize + size);

    System.arraycopy(buffer, index, buffer, index + size, realListSize - index);

    Iterator<? extends T> iterator = collection.iterator();
    for (int i = 0; i < size; i++)
      buffer[index + i] = iterator.next();

    realListSize += size;
    return true;
  }

  /**
   * Empties the list and re-creates the internal buffer with a default size. This is an O(1) operation.
   */
  @SuppressWarnings("unchecked")
  @Override
  public void clear()
  {
    buffer = (T[]) Array.newInstance(getGenericTypeParameter(), DEFAULT_SIZE);
    realListSize = 0;
  }

  /**
   * Scans the collection linearly and returns true if the specified element is found. This is an O(n) operation.
   */
  @Override
  public boolean contains(Object o)
  {
    int index = Linq.indexOf(buffer, o);
    if (index >= 0 && index < realListSize)
      return true;

    return false;
  }

  /**
   * Returns true if all elements in the provided collection are contained in this collection. Note: also returns true if the collection
   * given is empty. This is an O(n^2) operation
   * 
   * @throws NullPointerException The argument is null.
   */
  @Override
  public boolean containsAll(Collection<?> collection)
  {
    if (collection == null)
      throw new NullPointerException("collection");

    for (Object o : collection)
      if (!contains(o))
        return false;

    return true;
  }

  /**
   * Returns the element at the specified index. This is an O(1) operation.
   * 
   * @throws IndexOutOfBoundsException When the index is out of range
   */
  @Override
  public T get(int index)
  {
    if (index < 0 || index >= realListSize)
      throw new IndexOutOfBoundsException("index=" + index + " realListSize=" + realListSize);

    return buffer[index];
  }

  /**
   * Returns the formal type parameter as a class
   */
  @Override
  public Class<?> getGenericTypeParameter()
  {
    return genericTypeParameter;
  }

  /**
   * Scans the collection linearly from start to end and returns the index of the element specified. Returns -1 if the element is not found.
   * This is an O(n) operation.
   */
  @Override
  public int indexOf(Object o)
  {
    return Linq.indexOf(buffer, o);
  }

  /**
   * Returns true if the list is empty. This is an O(1) operation.
   */
  @Override
  public boolean isEmpty()
  {
    return realListSize == 0;
  }

  /**
   * Returns an iterator over the backing array. This is an O(1) operation.
   */
  @Override
  public Iterator<T> iterator()
  {
    return new ReadOnlyArrayIterator<T>(buffer, 0, realListSize);
  }

  /**
   * Scans the collection linearly from end to start returns the index of the element specified. Returns -1 if the element is not found.
   * This is an O(n) operation.
   */
  @Override
  public int lastIndexOf(Object o)
  {
    return Linq.lastIndexOf(buffer, o);
  }

  /**
   * Returns a list iterator over the backing array. This is an O(1) operation.
   */
  @Override
  public ListIterator<T> listIterator()
  {
    return new ReadOnlyArrayIterator<T>(buffer);
  }

  /**
   * Returns a list iterator over the backing array, starting at the specified index. This is an O(1) operation.
   * 
   * @throws IndexOutOfBoundsException When the index is out of range
   */
  @Override
  public ListIterator<T> listIterator(int index)
  {
    if (index > realListSize)
      throw new IndexOutOfBoundsException("index=" + index + " realListSize=" + realListSize);

    return new ReadOnlyArrayIterator<T>(buffer, index, realListSize);

  }

  /**
   * Removes the first occurrence of an element. Elements to the right of the removed element are shifted to the left by one position (their
   * indices decrease by one). This is an O(n) operation.
   * 
   * @return True if an element was removed, false otherwise.
   */
  @Override
  public boolean remove(Object o)
  {
    int index = indexOf(o);
    if (index >= 0)
    {
      System.arraycopy(buffer, index + 1, buffer, index, realListSize - index - 1);
      buffer[--realListSize] = null;
      return true;
    }

    return false;
  }

  /**
   * Removes the element at the specified position. Elements to the right of the removed element are shifted to the left by one position
   * (their indices decrease by one). This is an O(n) operation.
   * 
   * @return The element removed.
   * 
   * @throws IndexOutOfBoundsException When the index is out of range.
   */
  @Override
  public T remove(int index)
  {
    if (index < 0 || index >= realListSize)
      throw new IndexOutOfBoundsException("index=" + index + " realListSize=" + realListSize);

    T result = buffer[index];
    System.arraycopy(buffer, index + 1, buffer, index, realListSize - index - 1);
    buffer[--realListSize] = null;
    return result;
  }

  /**
   * Removes all items in the provided collection from this list. This is an O(n^2) operation.
   * 
   * @return True if the list was modified
   * 
   * @throws NullPointerException If the collection is null
   */
  @Override
  public boolean removeAll(Collection<?> collection)
  {
    if (collection == null)
      throw new NullPointerException("collection");

    int size = collection.size();
    if (size <= 0)
      return false;

    boolean modified = false;
    for (Object item : collection)
      if (remove(item))
        modified = true;

    return modified;
  }

  /**
   * Removes from this list all items not contained in the given collection. This is an O(n^2) operation.
   * 
   * @return True if the list was modified.
   * 
   * @throws NullPointerException If the collection is null.
   */
  @Override
  public boolean retainAll(Collection<?> collection)
  {
    if (collection == null)
      throw new NullPointerException("collection");

    int size = collection.size();
    if (size <= 0)
    {
      clear();
      return true;
    }

    boolean modified = false;
    for (int i = 0; i < this.size(); i++)
      if (!collection.contains(get(i)))
      {
        remove(i);
        i--;
        modified = true;
      }
    return modified;
  }

  /**
   * Replaces the element at the specified position in this list with the specified element. This is an O(1) operation.
   * 
   * @return The old element
   * 
   * @throws IndexOutOfBoundsException When the index is out of range
   */
  @Override
  public T set(int index, T element)
  {
    if (index < 0 || index >= realListSize)
      throw new IndexOutOfBoundsException("index=" + index + " realListSize=" + realListSize);

    T result = buffer[index];
    buffer[index] = element;
    return result;
  }

  @Override
  public int size()
  {
    return realListSize;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ReifiedList<T> subList(int fromIndex, int toIndex)
  {
    if (fromIndex < 0 || fromIndex > toIndex)
      throw new IndexOutOfBoundsException("fromIndex=" + fromIndex);
    if (toIndex < 0 || toIndex > realListSize)
      throw new IndexOutOfBoundsException("toIndex=" + toIndex);

    int size = toIndex - fromIndex;
    ReifiedArrayList<T> result = new ReifiedArrayList<T>(size, getGenericTypeParameter());

    System.arraycopy(buffer, fromIndex, result.buffer, 0, size);
    result.realListSize = size;

    return result;
  }

  /**
   * Returns a copy of this list.
   */
  public ReifiedList<T> toList()
  {
    return subList(0, size());
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public T[] toArray()
  {
    T[] result = (T[]) Array.newInstance(getGenericTypeParameter(), realListSize);

    System.arraycopy(buffer, 0, result, 0, realListSize);

    return result;
  }

  /**
   * Returns all elements in an array. Attempts to use the provided array, but creates a new one if the length of the given array is not
   * sufficient to fit all elements (or if it is null). This is an O(n) operation.
   */
  @SuppressWarnings({"unchecked", "hiding"})
  @Override
  public <T> T[] toArray(T[] a)
  {
    if (a == null || a.length < realListSize)
      a = (T[]) Array.newInstance(getGenericTypeParameter(), realListSize);

    System.arraycopy(buffer, 0, a, 0, realListSize);

    return a;
  }

  /**
   * Ensures that the buffer has enough places available for the number of required
   */
  @SuppressWarnings("unchecked")
  private void ensureCapacity(int positionsRequired)
  {
    if (buffer.length < positionsRequired)
    {
      // double it
      T[] newBuffer = (T[]) Array.newInstance(getGenericTypeParameter(), Math.max(buffer.length * 2, positionsRequired));
      System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
      buffer = newBuffer;
    }
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
