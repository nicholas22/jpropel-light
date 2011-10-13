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
package propel.core.collections.arrays;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Reified list iterator, does not allow modifications.
 */
public final class ReadOnlyArrayIterator<T>
    implements ListIterator<T>
{
  private final T[] array;
  private final int max;
  private final int min;
  private int current;

  /**
   * Initializes with the array and the starting and ending position. The starting position is inclusive, the ending position is exclusive.
   * 
   * @throws NullPointerException The array is null.
   */
  public ReadOnlyArrayIterator(T[] array)
  {
    if (array == null)
      throw new NullPointerException("array");

    this.array = array;
    this.max = array.length;
    this.min = 0;
    current = min;
  }

  /**
   * Initializes with the array and the starting and ending position. The starting position is inclusive, the ending position is exclusive.
   * 
   * @throws NullPointerException The array is null.
   * @throws IndexOutOfBoundsException An index is out of bounds.
   */
  public ReadOnlyArrayIterator(T[] array, int min, int max)
  {
    if (array == null)
      throw new NullPointerException("array");
    if (min < 0)
      throw new IndexOutOfBoundsException("min=" + min);
    if (min > max)
      throw new IndexOutOfBoundsException("min=" + min + " max=" + max);
    if (max > array.length)
      throw new IndexOutOfBoundsException("max=" + min + " length=" + array.length);

    this.array = array;
    this.max = max;
    this.min = min;
    current = min;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasNext()
  {
    return current < max;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T next()
  {
    try
    {
      return array[current++];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      throw new NoSuchElementException(
          "There is no next element. Either a concurrent modification has occurred or you didn't check for the existence of a next element.");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasPrevious()
  {
    return current > min;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T previous()
  {
    try
    {
      return array[--current];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      throw new NoSuchElementException(
          "There is no previous element. Either a concurrent modification has occurred or you didn't check for the existence of a previous element.");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int nextIndex()
  {
    return current;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int previousIndex()
  {
    return current - 1;
  }

  /**
   * @throws UnsupportedOperationException Always.
   */
  @Override
  @Deprecated
  public void remove()
  {
    throw new UnsupportedOperationException();
  }

  /**
   * @throws UnsupportedOperationException Always.
   */
  @Override
  @Deprecated
  public void set(T t)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * @throws UnsupportedOperationException Always.
   */
  @Override
  @Deprecated
  public void add(T t)
  {
    throw new UnsupportedOperationException();
  }
}
