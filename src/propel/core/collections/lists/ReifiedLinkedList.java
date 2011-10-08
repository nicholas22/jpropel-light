/*
 ///////////////////////////////////////////////////////////
 //  This file is part of Propel.
 //
 //  Propel is free software: you can redistribute it and/or modify
 //  it under the terms of the GNU Lesser General Public License as published by
 //  the Free Software Foundation, either version 3 of the License, or
 //  (at your option) any later version.
 //
 //  Propel is distributed in the hope that it will be useful,
 //  but WITHOUT ANY WARRANTY; without even the implied warranty of
 //  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 //  GNU Lesser General Public License for more details.
 //
 //  You should have received a copy of the GNU Lesser General Public License
 //  along with Propel.  If not, see <http://www.gnu.org/licenses/>.
 ///////////////////////////////////////////////////////////
 //  Authored by: Nikolaos Tountas -> salam.kaser-at-gmail.com
 ///////////////////////////////////////////////////////////
 */
package propel.core.collections.lists;

import propel.core.collections.ReifiedIterable;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * A type-aware linked list.
 * This collection allows nulls to be inserted.
 * <p/>
 * Note: if you override this class make sure that the add() method is not overridden in
 * such a way that would prevent the iterable-initialization constructors from adding
 * elements in the linked list in a correct way. Overridden method calls from constructors
 * can be quite dangerous if you're not aware of the execution order.
 * 
 * Instantiate using e.g.:
 * new ReifiedLinkedList&lt;String&gt;(){}; 
 * -OR-
 * new ReifiedLinkedList&lt;String&gt;(String.class);
 */
public class ReifiedLinkedList<T>
		extends LinkedList<T>
		implements ReifiedList<T>
{
  private static final long serialVersionUID = 3034006258243537794L;
  private final Class<?> genericTypeParameter;

	/**
	 * Default constructor.
	 *
	 * @throws SuperTypeTokenException When called without using anonymous class semantics.
	 */
	public ReifiedLinkedList()
	{
		super();
		this.genericTypeParameter = SuperTypeToken.getClazz(this.getClass());
	}

	/**
	 * Constructor for initializing with the generic type parameter
	 *
	 * @throws NullPointerException When the generic type parameter is null.
	 */
	public ReifiedLinkedList(Class<?> genericTypeParameter)
	{
		super();
		this.genericTypeParameter = genericTypeParameter;
	}

	/**
	 * Constructor initializes from another reified collection
	 *
	 * @throws NullPointerException When the argument is null.
	 */
	public ReifiedLinkedList(ReifiedIterable<T> values)
	{
		if(values == null)
			throw new NullPointerException("values");

		this.genericTypeParameter = values.getGenericTypeParameter();
		for(T item : values)
			add(item);
	}

	/**
	 * Constructor initializes from another collection
	 *
	 * @throws SuperTypeTokenException When called without using anonymous class semantics.
	 * @throws NullPointerException	When the argument is null
	 */
	public ReifiedLinkedList(Iterable<? extends T> values)
	{
		if(values == null)
			throw new NullPointerException("values");

		this.genericTypeParameter = SuperTypeToken.getClazz(this.getClass());
		for(T item : values)
			add(item);
	}

	/**
	 * Constructor initializes from another collection and a generic type parameter
	 *
	 * @throws NullPointerException When an argument is null.
	 */
	public ReifiedLinkedList(Iterable<? extends T> values, Class<?> genericTypeParameter)
	{
		if(values == null)
			throw new NullPointerException("values");

		this.genericTypeParameter = genericTypeParameter;
		for(T item : values)
			add(item);
	}

	/**
	 * Constructor initializes from an array
	 *
	 * @throws NullPointerException When an argument is null.
	 */
	public ReifiedLinkedList(T[] array)
	{
		if(array == null)
			throw new NullPointerException("array");

		this.genericTypeParameter = array.getClass().getComponentType();
		for(T item : array)
			add(item);
	}

	/**
   * Appends all of the elements in the specified array to the end of
   * this list, in the order that they are ordered.
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
    if(array==null)
      throw new NullPointerException("array");
    
    if(array.length == 0)
      return false;
    
    for(int i = 0; i < array.length; i++)
      add(array[i]);

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
	 * {@inheritDoc}
	 */
	@Override
	public ReifiedList<T> subList(int fromIndex, int toIndex)
	{
		if(fromIndex < 0)
			throw new IndexOutOfBoundsException("fromIndex=" + fromIndex);

		int size = size();
		if(toIndex < 0 || toIndex > size)
			throw new IndexOutOfBoundsException("toIndex=" + toIndex + " size=" + size);

		int count = toIndex - fromIndex;

		Iterator<T> iterator = iterator();
		ReifiedList<T> result = new ReifiedArrayList<T>(count, getGenericTypeParameter());

		// skip
		for(int i = 0; i < fromIndex; i++)
			iterator.next();

		// take
		for(int i = 0; i < count; i++)
			result.add(iterator.next());

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] toArray()
	{
		ReifiedArrayList<T> result = new ReifiedArrayList<T>(this, getGenericTypeParameter());
		return result.toArray();
	}

	/**
	 * Returns a copy of this list.
	 * This is an O(n) operation.
	 */
	public ReifiedList<T> toList()
	{
		return new ReifiedArrayList<T>(this, getGenericTypeParameter());
	}

}
