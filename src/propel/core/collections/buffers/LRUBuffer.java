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
package propel.core.collections.buffers;

import propel.core.collections.ReifiedIterable;
import propel.core.collections.lists.ReadOnlyListIterator;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.lists.ReifiedList;
import propel.core.utils.Linq;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;

import java.util.Iterator;

/**
 * A type-aware least recently used (LRU) buffer.
 * This collection does not allow nulls to be inserted.
 * 
 * Instantiate using e.g.:
 * new LRUBuffer&lt;String&gt;(){}; 
 * -OR-
 * new LRUBuffer&lt;String&gt;(String.class);
 */
public class LRUBuffer<T>
		implements IBuffer<T>
{
	/**
	 * The default size of the buffer, if none is specified.
	 */
	public static final int DEFAULT_SIZE = 1024;
	private final int maxSize;
	private final ReifiedArrayList<T> cache;

	/**
	 * Default constructor.
	 *
	 * @throws SuperTypeTokenException When called without using anonymous class semantics.
	 */
	public LRUBuffer()
	{
		this(DEFAULT_SIZE);
	}

	/**
	 * Constructor for initializing with the generic type parameter
	 *
	 * @throws NullPointerException When the generic type parameter is null.
	 */
	public LRUBuffer(Class<?> genericTypeParameter)
	{
		this(DEFAULT_SIZE, genericTypeParameter);
	}

	/**
	 * Initializes the buffer with a buffer size.
	 *
	 * @throws IllegalArgumentException When the buffer size is non positive.
	 * @throws SuperTypeTokenException  When called without using anonymous class semantics.
	 */
	public LRUBuffer(int bufferSize)
	{
		if(bufferSize <= 0)
			throw new IllegalArgumentException("bufferSize=" + bufferSize);

		maxSize = bufferSize;

		// retrieves first generic parameter
		cache = new ReifiedArrayList<T>(bufferSize, SuperTypeToken.getClazz(this.getClass()));
	}

	/**
	 * Constructor initializes with a buffer size and a generic type parameter.
	 *
	 * @throws IllegalArgumentException When the buffer size is non positive.
	 * @throws NullPointerException	 When the generic type parameter is null.
	 */
	public LRUBuffer(int bufferSize, Class<?> genericTypeParameter)
	{
		if(bufferSize <= 0)
			throw new IllegalArgumentException("bufferSize=" + bufferSize);

		maxSize = bufferSize;

		// retrieves first generic parameter
		cache = new ReifiedArrayList<T>(bufferSize, genericTypeParameter);
	}

	/**
	 * Constructor initializes from another reified collection
	 *
	 * @throws NullPointerException	 When the argument is null.
	 * @throws IllegalArgumentException When the values is an empty iterable.
	 */
	public LRUBuffer(ReifiedIterable<T> values)
	{
		if(values == null)
			throw new NullPointerException("values");

		maxSize = Linq.count(values);
		if(maxSize <= 0)
			throw new IllegalArgumentException("values");

		cache = new ReifiedArrayList<T>(values);
	}

	/**
	 * Constructor initializes from another collection
	 *
	 * @throws SuperTypeTokenException  When called without using anonymous class semantics.
	 * @throws IllegalArgumentException When the values is an empty iterable.
	 * @throws NullPointerException	 When the argument is null
	 */
	public LRUBuffer(Iterable<? extends T> values)
	{
		if(values == null)
			throw new NullPointerException("values");

		maxSize = Linq.count(values);
		if(maxSize <= 0)
			throw new IllegalArgumentException("values");

		cache = new ReifiedArrayList<T>(maxSize, SuperTypeToken.getClazz(this.getClass()));
	}

	/**
	 * Constructor initializes from another collection and a generic type parameter
	 *
	 * @throws NullPointerException	 When an argument is null.
	 * @throws IllegalArgumentException When the values is an empty iterable.
	 */
	public LRUBuffer(Iterable<? extends T> values, Class<?> genericTypeParameter)
	{
		if(values == null)
			throw new NullPointerException("values");
		if(genericTypeParameter == null)
			throw new NullPointerException("genericTypeParameter");

		maxSize = Linq.count(values);
		if(maxSize <= 0)
			throw new IllegalArgumentException("values");
		cache = new ReifiedArrayList<T>(maxSize, genericTypeParameter);
	}

	/**
	 * Empties the buffer.
	 * This is an O(1) operation.
	 */
	@Override
	public void clear()
	{
		cache.clear();
	}

	/**
	 * Returns true if an object is contained in the buffer.
	 * This is an O(n) operation.
	 *
	 * @param obj The object to find.
	 *
	 * @throws NullPointerException If the object is null.
	 */
	@Override
	public boolean contains(T obj)
	{
		if(obj == null)
			throw new NullPointerException("obj");

		return cache.contains(obj);
	}

	/**
	 * Returns the maximum size.
	 * This is an O(1) operation.
	 */
	@Override
	public int getMaxSize()
	{
		return maxSize;
	}

	/**
	 * Returns true if the buffer is empty.
	 * This is an O(1) operation.
	 */
	@Override
	public boolean isEmpty()
	{
		return cache.size() == 0;
	}

	/**
	 * Returns true if the buffer is full.
	 * This is an O(1) operation.
	 */
	@Override
	public boolean isFull()
	{
		return cache.size() == maxSize;
	}

	/**
	 * Retrieves an object from the Buffer
	 * This method is not supported. Use get(int) instead.
	 *
	 * @throws UnsupportedOperationException Always.
	 * @deprecated Use get(int) instead.
	 */
	@Override
	@Deprecated
	public T get()
	{
		throw new UnsupportedOperationException("This method is not supported. Use the overloaded get(int index) instead.");
	}

	/**
	 * Retrieves the object with specified index from the list.
	 * This is an O(1) operation.
	 *
	 * @throws IndexOutOfBoundsException When the index provided is a negative number or beyond the buffer size.
	 */
	public T get(int index)
	{
		if(index < 0 || index >= cache.size())
			throw new IndexOutOfBoundsException("index=" + index + " size=" + cache.size());

		return cache.get(index);
	}

	/**
	 * Returns an iterator over the backing list.
	 * This is an O(1) operation.
	 */
	public Iterator<T> iterator()
	{
		return new ReadOnlyListIterator<T>(cache);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getGenericTypeParameter()
	{
		return cache.getGenericTypeParameter();
	}

	/**
	 * Puts an object in the buffer.
	 * This is an O(1) operation.
	 *
	 * @param obj The object to put.
	 *
	 * @return Always true
	 *
	 * @throws NullPointerException If the object is null.
	 */
	@Override
	public boolean put(T obj)
	{
		if(obj == null)
			throw new NullPointerException("obj");

		// remove first if needed
		if(cache.size() >= maxSize)
			cache.remove(0);

		cache.add(obj);

		return true;
	}

	/**
	 * Returns the size of the Buffer.
	 * This is an O(1) operation.
	 */
	@Override
	public int size()
	{
		return cache.size();
	}

	/**
	 * Returns all elements in an array copy.
	 * This is an O(n) operation.
	 */
	@Override
	public T[] toArray()
	{
		return cache.toArray();
	}

	/**
	 * Puts all elements in a list copy and returns them.
	 * This is an O(n) operation.
	 *
	 * @return All elements.
	 */
	@Override
	public ReifiedList<T> toList()
	{
		return new ReifiedArrayList<T>(cache);
	}
}
