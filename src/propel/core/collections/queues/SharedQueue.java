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
package propel.core.collections.queues;

import propel.core.collections.ReifiedIterable;
import propel.core.collections.lists.ReifiedLinkedList;
import propel.core.collections.lists.ReifiedList;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A type-aware thread-safe queue implementation.
 * This collection allows null items to be inserted.
 *
 * Instantiate using e.g.:
 * new SharedQueue&lt;String&gt;(){}; 
 * -OR-
 * new SharedQueue&lt;String&gt;(String.class);
 */
public class SharedQueue<T>
		implements ISharedQueue<T>
{
	protected final Lock lockObject;
	protected final ReifiedLinkedList<T> queue;
	protected final Condition notEmpty;

	/**
	 * Default constructor.
	 *
	 * @throws SuperTypeTokenException When called without using anonymous class semantics.
	 */
	public SharedQueue()
	{
		queue = new ReifiedLinkedList<T>(SuperTypeToken.getClazz(this.getClass()));
		lockObject = new ReentrantLock();
		notEmpty= lockObject.newCondition();
	}

	/**
	 * Constructor for initializing with the generic type parameter
	 *
	 * @throws NullPointerException When the generic type parameter is null.
	 */
	public SharedQueue(Class<?> genericTypeParameter)
	{
		if(genericTypeParameter == null)
			throw new NullPointerException("genericTypeParameter");

		queue = new ReifiedLinkedList<T>(genericTypeParameter);
		lockObject = new ReentrantLock();
		notEmpty= lockObject.newCondition();
	}

	/**
	 * Constructor initializes from another reified collection
	 *
	 * @throws NullPointerException When the argument is null
	 */
	public SharedQueue(ReifiedIterable<T> iterable)
	{
		if(iterable == null)
			throw new NullPointerException("iterable");

		queue = new ReifiedLinkedList<T>(iterable.getGenericTypeParameter());
		lockObject = new ReentrantLock();
		notEmpty= lockObject.newCondition();

		for(T item : iterable)
			queue.add(item);
	}

	/**
	 * Constructor initializes with an initial collection and this class's generic type parameter
	 *
	 * @throws NullPointerException	When the argument is null
	 * @throws SuperTypeTokenException When called without using anonymous class semantics.
	 */
	public SharedQueue(Iterable<? extends T> iterable)
	{
		if(iterable == null)
			throw new NullPointerException("iterable");

		queue = new ReifiedLinkedList<T>(SuperTypeToken.getClazz(this.getClass()));
		lockObject = new ReentrantLock();
		notEmpty= lockObject.newCondition();

		for(T item : iterable)
			queue.add(item);
	}

	/**
	 * Constructor initializes with an initial collection and this class's generic type parameter
	 *
	 * @throws NullPointerException When an argument is null
	 */
	public SharedQueue(Iterable<? extends T> iterable, Class<?> genericTypeParameter)
	{
		if(iterable == null)
			throw new NullPointerException("iterable");
		if(genericTypeParameter == null)
			throw new NullPointerException("genericTypeParameter");

		queue = new ReifiedLinkedList<T>(genericTypeParameter);
		lockObject = new ReentrantLock();
		notEmpty= lockObject.newCondition();

		for(T item : iterable)
			queue.add(item);
	}

	/**
	 * Initializes with an array.
	 *
	 * @throws NullPointerException When the argument is null
	 */
	public SharedQueue(T[] array)
	{
		if(array == null)
			throw new NullPointerException("array");

		queue = new ReifiedLinkedList<T>(array.getClass().getComponentType());
		lockObject = new ReentrantLock();
		notEmpty= lockObject.newCondition();

		for(T item : array)
			queue.add(item);
	}

	/**
	 * Clears the queue.
	 * This is an O(1) operation.
	 */
	@Override
	public void clear()
	{
		lock();
		try
		{
			queue.clear();
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * De-queues an object, otherwise blocks until one becomes available.
	 * This is an O(1) operation.
	 */
	@Override
	public T get()
	{
		lock();
		try
		{
			while(queue.size() <= 0)
			{
				try
				{
					notEmpty.await();
				}
				catch(InterruptedException e)
				{
					continue;
				}
			}

			return queue.removeFirst();
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * De-queues a number of objects, blocking if not all are available.
	 * This is an O(n) operation where n is the count.
	 *
	 * @throws IllegalArgumentException Count is out of range.
	 */
	@Override
	public Iterable<T> getRange(int count)
	{
		if(count < 0)
			throw new IllegalArgumentException("count=" + count);

		List<T> result = new ArrayList<T>();

		lock();
		try
		{
			for(int i = 0; i < count; i++)
			{
				while(queue.size() <= 0)
				{
					try
					{
						notEmpty.await();
					}
					catch(InterruptedException e)
					{
					}
				}

				result.add(queue.removeFirst());
			}
		}
		finally
		{
			unlock();
		}

		return result;
	}

	/**
	 * Returns an iterator over a copy of the backing list.
	 * This is an O(n) operation.
	 */
	@Override
	public Iterator<T> iterator()
	{
		lock();
		try
		{
			return queue.toList().iterator();
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getGenericTypeParameter()
	{
		return queue.getGenericTypeParameter();
	}

	/**
	 * Returns the next element without removing it.
	 * If there are no elements, an exception will be thrown.
	 * This is an O(1) operation.
	 *
	 * @throws NoSuchElementException When the queue is empty.
	 */
	@Override
	public T peek()
	{
		lock();
		try
		{
			if(queue.size() <= 0)
				throw new NoSuchElementException("Cannot perform a peek operation when the queue is empty.");

			return queue.peekFirst();
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * En-queues an object.
	 * This is an O(1) operation.
	 */
	@Override
	public void put(T item)
	{
		lock();
		try
		{
			queue.addLast(item);
			notEmpty.signalAll();
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * En-queues a number of objects.
	 * This is an O(n) operation where n is the item count.
	 *
	 * @throws NullPointerException When the argument is null.
	 */
	@Override
	public void putRange(Iterable<? extends T> items)
	{
		if(items == null)
			throw new NullPointerException("items");

		lock();
		try
		{
			for(T item : items)
				queue.addLast(item);

			notEmpty.signalAll();
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Returns the queue length.
	 * This is an O(1) operation.
	 */
	@Override
	public int size()
	{
		lock();
		try
		{
			return queue.size();
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Puts all elements in an array and returns them.
	 * This is an O(n) operation.
	 */
	@Override
	public T[] toArray()
	{
		lock();
		try
		{
			return queue.toArray();
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Puts all elements in a list and returns them.
	 * This is an O(n) operation.
	 */
	@Override
	public ReifiedList<T> toList()
	{

		lock();
		try
		{
			return queue.toList();
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Locks the queue
	 */
	@Override
	public void lock()
	{
		lockObject.lock();
	}

	/**
	 * Unlocks the queue
	 *
	 * @throws IllegalMonitorStateException If the current thread does not own the lock.
	 */
	@Override
	public void unlock()
	{
		lockObject.unlock();
	}
}
