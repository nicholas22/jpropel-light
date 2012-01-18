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
package propel.core.collections.lists.primitive;

import propel.core.utils.SuperTypeTokenException;

/**
 * A primitive long array-backed list that uses similar semantics and strategy to the java.util.ArrayList.
 */
public class LongArrayList
    implements ILongArrayList
{
  public static final int DEFAULT_SIZE = 64;
  private long[] buffer;
  private int realListSize;

  /**
   * Default constructor
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public LongArrayList()
  {
    this(DEFAULT_SIZE);
  }

  /**
   * Constructor initializes with an initial collection size.
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   * @throws IllegalArgumentException When the size is non positive.
   */
  public LongArrayList(int initialSize)
  {
    if (initialSize < 0)
      throw new IllegalArgumentException("initialSize=" + initialSize);

    // init array
    buffer = new long[initialSize];
  }

  /**
   * Initializes with an array.
   * 
   * @throws NullPointerException When the argument is null
   */
  public LongArrayList(long[] array)
  {
    if (array == null)
      throw new NullPointerException("array");

    buffer = array;
    realListSize = array.length;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean add(long element)
  {
    ensureCapacity(realListSize + 1);

    buffer[realListSize++] = element;
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add(int index, long element)
  {
    if (index < 0 || index >= realListSize)
      throw new IndexOutOfBoundsException("index=" + index + " realListSize=" + realListSize);

    ensureCapacity(realListSize + 1);

    System.arraycopy(buffer, index, buffer, index + 1, realListSize - index);
    buffer[index] = element;

    realListSize++;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean addAll(long[] array)
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
   * {@inheritDoc}
   */
  @Override
  public void clear()
  {
    buffer = new long[DEFAULT_SIZE];
    realListSize = 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean contains(long value)
  {
    for (int i = 0; i < realListSize; i++)
      if (buffer[i] == value)
        return true;

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean containsAll(long[] array)
  {
    if (array == null)
      throw new NullPointerException("array");

    for (int i = 0; i < array.length; i++)
      if (!contains(array[i]))
        return false;

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long get(int index)
  {
    if (index < 0 || index >= realListSize)
      throw new IndexOutOfBoundsException("index=" + index + " realListSize=" + realListSize);

    return buffer[index];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int indexOf(long value)
  {
    for (int i = 0; i < realListSize; i++)
      if (buffer[i] == value)
        return i;

    return -1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEmpty()
  {
    return realListSize == 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int lastIndexOf(long value)
  {
    for (int i = realListSize - 1; i >= 0; i--)
      if (buffer[i] == value)
        return i;

    return -1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeValue(long value)
  {
    int index = indexOf(value);
    if (index >= 0)
    {
      System.arraycopy(buffer, index + 1, buffer, index, realListSize - index - 1);
      buffer[--realListSize] = 0;
      return true;
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long remove(int index)
  {
    if (index < 0 || index >= realListSize)
      throw new IndexOutOfBoundsException("index=" + index + " realListSize=" + realListSize);

    long result = buffer[index];
    System.arraycopy(buffer, index + 1, buffer, index, realListSize - index - 1);
    buffer[--realListSize] = 0;
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long set(int index, long value)
  {
    if (index < 0 || index >= realListSize)
      throw new IndexOutOfBoundsException("index=" + index + " realListSize=" + realListSize);

    long result = buffer[index];
    buffer[index] = value;
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int size()
  {
    return realListSize;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long[] toArray()
  {
    long[] result = new long[realListSize];

    System.arraycopy(buffer, 0, result, 0, realListSize);

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long[] toArray(long[] a)
  {
    if (a == null || a.length < realListSize)
      a = new long[realListSize];

    System.arraycopy(buffer, 0, a, 0, realListSize);

    return a;
  }

  /**
   * Ensures that the buffer has enough places available for the number of required
   */
  private void ensureCapacity(int positionsRequired)
  {
    if (buffer.length < positionsRequired)
    {
      // double it
      long[] newBuffer = new long[buffer.length * 2];
      System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
      buffer = newBuffer;
    }
  }

}
