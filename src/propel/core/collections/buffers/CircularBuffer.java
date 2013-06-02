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
import propel.core.collections.arrays.ReadOnlyArrayIterator;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.lists.ReifiedList;
import propel.core.counters.ModuloCounter;
import propel.core.utils.Linq;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;
import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * A type-aware circular buffer of limited size. This collection does not allow nulls to be inserted.
 * 
 * Instantiate using e.g.: new CircularBuffer&lt;String&gt;(){}; -OR- new CircularBuffer&lt;String&gt;(String.class);
 */
public class CircularBuffer<T>
    implements IBuffer<T>
{
  /**
   * The default size of the buffer, if none is specified.
   */
  public static final int DEFAULT_SIZE = 1024;
  protected T[] buffer;
  private final Class<?> genericTypeParameter;
  private final int maxSize;
  private ModuloCounter inPos;
  private ModuloCounter outPos;
  private int size;

  /**
   * Default constructor.
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public CircularBuffer()
  {
    this(DEFAULT_SIZE);
  }

  /**
   * Constructor for initializing with the generic type parameter
   * 
   * @throws NullPointerException When the generic type parameter is null.
   */
  public CircularBuffer(Class<?> genericTypeParameter)
  {
    this(DEFAULT_SIZE, genericTypeParameter);
  }

  /**
   * Initializes the buffer with the buffer size.
   * 
   * @throws IllegalArgumentException When the buffer size is non positive.
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public CircularBuffer(int bufferSize)
  {
    if (bufferSize <= 0)
      throw new IllegalArgumentException("bufferSize=" + bufferSize);
    maxSize = bufferSize;

    // retrieves first generic parameter
    genericTypeParameter = SuperTypeToken.getClazz(this.getClass());

    clearBufferInternal(this);
  }

  /**
   * Constructor initializes with the buffer size and a generic type parameter.
   * 
   * @throws IllegalArgumentException When the buffer size is non positive.
   * @throws NullPointerException When the generic type parameter is null.
   */
  public CircularBuffer(int bufferSize, Class<?> genericTypeParameter)
  {
    if (bufferSize <= 0)
      throw new IllegalArgumentException("bufferSize=" + bufferSize);

    maxSize = bufferSize;

    if (genericTypeParameter == null)
      throw new NullPointerException("genericTypeParameter");
    this.genericTypeParameter = genericTypeParameter;

    clearBufferInternal(this);
  }

  /**
   * Constructor initializes from another reified collection
   * 
   * @throws NullPointerException When the argument is null.
   * @throws IllegalArgumentException When the values is an empty iterable.
   */
  public CircularBuffer(ReifiedIterable<T> values)
  {
    if (values == null)
      throw new NullPointerException("values");

    maxSize = Linq.count(values);
    if (maxSize <= 0)
      throw new IllegalArgumentException("values");

    this.genericTypeParameter = values.getGenericTypeParameter();

    Iterator<T> iterator = values.iterator();
    for (int i = 0; i < maxSize; i++)
      buffer[i] = iterator.next();
    size = maxSize;

    clearBufferInternal(this);
  }

  /**
   * Constructor initializes from another collection
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   * @throws IllegalArgumentException When the values is an empty iterable.
   * @throws NullPointerException When the argument is null
   */
  public CircularBuffer(Iterable<? extends T> values)
  {
    if (values == null)
      throw new NullPointerException("values");

    maxSize = Linq.count(values);
    if (maxSize <= 0)
      throw new IllegalArgumentException("values");
    this.genericTypeParameter = SuperTypeToken.getClazz(this.getClass());

    Iterator<? extends T> iterator = values.iterator();
    for (int i = 0; i < maxSize; i++)
      buffer[i] = iterator.next();
    size = maxSize;

    clearBufferInternal(this);
  }

  /**
   * Constructor initializes from another collection and a generic type parameter
   * 
   * @throws NullPointerException When an argument is null.
   * @throws IllegalArgumentException When the values is an empty iterable.
   */
  public CircularBuffer(Iterable<? extends T> values, Class<?> genericTypeParameter)
  {
    if (values == null)
      throw new NullPointerException("values");
    if (genericTypeParameter == null)
      throw new NullPointerException("genericTypeParameter");

    maxSize = Linq.count(values);
    if (maxSize <= 0)
      throw new IllegalArgumentException("values");
    this.genericTypeParameter = genericTypeParameter;

    Iterator<? extends T> iterator = values.iterator();
    for (int i = 0; i < maxSize; i++)
      buffer[i] = iterator.next();
    size = maxSize;

    clearBufferInternal(this);
  }

  /**
   * Empties the buffer. This is an O(1) operation.
   */
  @Override
  public void clear()
  {
    clearBufferInternal(this);
  }

  /**
   * Checks if an object is contained in the buffer. This is an O(n) operation.
   * 
   * @param obj The object to find.
   * 
   * @return True, if the specified object is found.
   * 
   * @throws NullPointerException When the object is null.
   */
  @Override
  public boolean contains(T obj)
  {
    if (obj == null)
      throw new NullPointerException("obj");

    for (int i = 0; i < buffer.length; i++)
      if (buffer[i] != null)
        if (buffer[i].equals(obj))
          return true;

    return false;
  }

  @Override
  public Class<?> getGenericTypeParameter()
  {
    return genericTypeParameter;
  }

  /**
   * Maximum size method. This is an O(1) operation.
   * 
   * @return The maximum size of the buffer
   */
  @Override
  public int getMaxSize()
  {
    return maxSize;
  }

  /**
   * Is empty method. This is an O(1) operation.
   * 
   * @return True, if the buffer is empty.
   */
  @Override
  public boolean isEmpty()
  {
    return size == 0;
  }

  /**
   * Is full method. This is an O(1) operation.
   * 
   * @return True, if the buffer is full.
   */
  @Override
  public boolean isFull()
  {
    return size == maxSize;
  }

  /**
   * Retrieves an object from the buffer if one is available. This is an O(1) operation.
   * 
   * @return The object, or null if the buffer is empty.
   */
  @Override
  public T get()
  {
    int outCounter = (int) outPos.getValue(); // safe to cast to int as long as maxSize is integer too
    T obj = buffer[outCounter];
    buffer[outCounter] = null;
    if (obj != null)
    {
      outPos.next();
      size--;
    }

    return obj;
  }

  /**
   * Returns an iterator over the buffer. This is an O(1) operation.
   */
  @Override
  public Iterator<T> iterator()
  {
    return new ReadOnlyArrayIterator<T>(buffer);
  }

  /**
   * Puts an object in the buffer. This is an O(1) operation.
   * 
   * @param obj The object to put.
   * 
   * @return True, if the object was put successfully, false if the buffer is full.
   * 
   * @throws NullPointerException When the object is null.
   */
  @Override
  public boolean put(T obj)
  {
    if (obj == null)
      throw new NullPointerException("obj");

    // safe to cast to int as long as maxSize is integer too
    int inCounter = (int) inPos.getValue();
    if (buffer[inCounter] == null)
    {
      buffer[inCounter] = obj;
      inPos.next();
      size++;
      return true;
    }

    return false;
  }

  /**
   * Returns the size of the Buffer. This is an O(1) operation.
   */
  @Override
  public int size()
  {
    return size;
  }

  /**
   * Puts all elements in an array copy and returns them. This is an O(n) operation.
   * 
   * @return All elements.
   */
  @SuppressWarnings("unchecked")
  @Override
  public T[] toArray()
  {
    T[] result = (T[]) Array.newInstance(getGenericTypeParameter(), buffer.length);
    System.arraycopy(buffer, 0, result, 0, buffer.length);

    return result;
  }

  /**
   * Puts all elements in a list copy and returns them. This is an O(n) operation.
   * 
   * @return All elements.
   */
  @Override
  public ReifiedList<T> toList()
  {
    return new ReifiedArrayList<T>(buffer);
  }

  /**
   * Clears the buffer.
   * 
   * @param cb The buffer to clear.
   */
  @SuppressWarnings("unchecked")
  private static <T> void clearBufferInternal(CircularBuffer<T> cb)
  {
    cb.size = 0;
    cb.buffer = (T[]) Array.newInstance(cb.genericTypeParameter, cb.maxSize);

    cb.inPos = new ModuloCounter(cb.maxSize - 1);
    cb.outPos = new ModuloCounter(cb.maxSize - 1);
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
