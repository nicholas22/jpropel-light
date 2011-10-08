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
import propel.core.collections.lists.ReifiedList;
import propel.core.model.IShared;

import java.util.NoSuchElementException;

/**
 * The interface of a type-aware thread-safe queue
 *
 * @param <T> The component type of the queue.
 */
public interface ISharedQueue<T>
		extends ReifiedIterable<T>, IShared
{
	/**
	 * Clears the queue
	 */
	void clear();

	/**
	 * Dequeues an object, otherwise blocks until one becomes available.
	 */
	T get();

	/**
	 * Dequeues a number of objects, blocking if not all are available.
	 *
	 * @throws IllegalArgumentException The count is out of range.
	 */
	Iterable<T> getRange(int count);

	/**
	 * Returns the next element without removing it.
	 * If there are no elements, an exception will be thrown.
	 *
	 * @throws NoSuchElementException When the queue is empty.
	 */
	T peek();

	/**
	 * En-queues an object
	 */
	void put(T item);

	/**
	 * En-queues a number of objects
	 *
	 * @throws NullPointerException When the argument is null.
	 */
	void putRange(Iterable<? extends T> items);

	/**
	 * Returns the queue length
	 */
	int size();

	/**
	 * Puts all elements in an array and returns them.
	 */
	T[] toArray();

	/**
	 * Puts all elements in a list and returns them.
	 */
	ReifiedList<T> toList();
}
