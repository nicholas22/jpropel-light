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
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * A type-aware sorted list of comparable elements. This collection does not allow nulls to be inserted.
 * 
 * Instantiate using e.g.: new SortedList&lt;String&gt;(){}; -OR- new SortedList&lt;String&gt;(String.class);
 */
public class SortedList<T extends Comparable<T>>
    extends PriorityQueue<T>
    implements ReifiedIterable<T>
{
  private static final long serialVersionUID = 4269411034127983747L;
  private final Class<?> genericTypeParameter;

  /**
   * Default constructor
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public SortedList()
  {
    super();
    this.genericTypeParameter = SuperTypeToken.getClazz(this.getClass());
  }

  /**
   * Constructor for initializing with the generic type parameter
   * 
   * @throws NullPointerException When the generic type parameter is null.
   */
  public SortedList(Class<?> genericTypeParameter)
  {
    super();
    if (genericTypeParameter == null)
      throw new NullPointerException("genericTypeParameter");
    this.genericTypeParameter = genericTypeParameter;
  }

  /**
   * Constructor initializes with an initial collection size.
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   * @throws IllegalArgumentException When the size is non positive.
   */
  public SortedList(int initialSize)
  {
    super(initialSize);
    genericTypeParameter = SuperTypeToken.getClazz(this.getClass());
  }

  /**
   * Constructor initializes with an initial collection size and a generic type parameter.
   * 
   * @throws IllegalArgumentException When the buffer size is non positive.
   * @throws NullPointerException When the generic type parameter is null.
   */
  public SortedList(int initialSize, Class<?> genericTypeParameter)
  {
    super(initialSize);
    if (genericTypeParameter == null)
      throw new NullPointerException("genericTypeParameter");
    this.genericTypeParameter = genericTypeParameter;
  }

  /**
   * Constructor for initializing with the generic type parameter
   * 
   * @throws NullPointerException When the generic type parameter is null.
   */
  public SortedList(Comparator<? super T> comparator, Class<?> genericTypeParameter)
  {
    super(64, comparator);
    if (genericTypeParameter == null)
      throw new NullPointerException("genericTypeParameter");
    this.genericTypeParameter = genericTypeParameter;
  }

  /**
   * Constructor initializes with an initial collection size.
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   * @throws IllegalArgumentException When the size is non positive.
   */
  public SortedList(Comparator<? super T> comparator, int initialSize)
  {
    super(initialSize, comparator);
    genericTypeParameter = SuperTypeToken.getClazz(this.getClass());
  }

  /**
   * Constructor initializes with an initial collection size and a generic type parameter.
   * 
   * @throws IllegalArgumentException When the buffer size is non positive.
   * @throws NullPointerException When the generic type parameter is null.
   */
  public SortedList(int initialSize, Comparator<? super T> comparator, Class<?> genericTypeParameter)
  {
    super(initialSize, comparator);
    if (genericTypeParameter == null)
      throw new NullPointerException("genericTypeParameter");
    this.genericTypeParameter = genericTypeParameter;
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
   * Returns an ordered iterator of elements. This is an O(n) operation.
   */
  @Override
  public Iterator<T> iterator()
  {
    return new ReadOnlyArrayIterator<T>(toArray());
  }

  /**
   * Returns an ordered copy of elements. Note: The other toArray(T[]) method does not return ordered elements. This is an O(n) operation.
   */
  @SuppressWarnings("unchecked")
  @Override
  public T[] toArray()
  {
    int size = size();

    T[] result = (T[]) Array.newInstance(getGenericTypeParameter(), size);

    // make a copy
    PriorityQueue<T> copy;

    if (comparator() != null)
      copy = new PriorityQueue<T>(size, comparator());
    else
      copy = new PriorityQueue<T>(size);

    // add all elements to copy
    copy.addAll(Linq.toList(super.toArray(result)));

    // sort
    for (int i = 0; i < result.length; i++)
      result[i] = copy.remove();

    return result;
  }
}
