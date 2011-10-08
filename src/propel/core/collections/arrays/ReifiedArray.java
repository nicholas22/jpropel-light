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
package propel.core.collections.arrays;

import propel.core.utils.ArrayUtils;
import propel.core.utils.PrimitiveArrayType;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * Encapsulates a primitive or object array.
 * For primitive arrays, you must specify System.Object as generic type parameter, to allow for boxing.
 */
public class ReifiedArray<T>
		implements Iterable<T>
{
	private Object originalArray;
	private PrimitiveArrayType primitiveArrayType;
	private Class<?> componentType;
	private int dimensions;
	private int length;

	public ReifiedArray(Object originalArray)
	{
		if(originalArray == null)
			throw new NullPointerException("originalArray");

		this.originalArray = originalArray;
		primitiveArrayType = ArrayUtils.getType(originalArray);
		dimensions = ArrayUtils.getDimensions(originalArray);
		componentType = originalArray.getClass().getComponentType();
		length = ArrayUtils.count(originalArray);

		if(dimensions > 1)
			throw new UnsupportedOperationException("Only one-dimensional arrays are currently supported.");
	}

	/**
	 * Returns the element at a specified position.
	 *
	 * @throws IndexOutOfBoundsException An index is out of bounds.
	 * @throws ClassCastException		The encapsulated array is not of specified generic type, hence casting fails.
	 */
  @SuppressWarnings("unchecked")
	public T get(int index)
	{
		if(index < 0 || index >= length)
			throw new IndexOutOfBoundsException("index=" + index + " length=" + length);

		return (T) Array.get(originalArray, index);
	}

	/**
	 * Sets an element at a specified position.
	 *
	 * @throws IllegalArgumentException  An illegal element was given
	 * @throws IndexOutOfBoundsException An index is out of bounds.
	 */
	public void set(int index, T element)
	{
		if(index < 0 || index >= length)
			throw new IndexOutOfBoundsException("index=" + index + " length=" + length);

		Array.set(originalArray, index, element);
	}

	/**
	 * Returns the length of the array
	 */
	public int length()
	{
		return length;
	}

	/**
	 * Returns the dimensions of the array
	 */
	public int getDimensions()
	{
		return dimensions;
	}

	/**
	 * Returns the array component class type.
	 */
	public Class<?> getComponentType()
	{
		return componentType;
	}

	/**
	 * Returns the primitive array type for primitive arrays,
	 * or NotPrimitive for object arrays.
	 */
	public PrimitiveArrayType getPrimitiveArrayType()
	{
		return primitiveArrayType;
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<T> iterator()
	{
		return new ReifiedArrayIterator<T>(originalArray, length);
	}

	/**
	 * Returns the array used to initialize this class
	 */
	public Object getOriginalArray()
	{
		return originalArray;
	}

}
