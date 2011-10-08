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
import propel.core.collections.lists.ReifiedList;

/**
 * The interface that buffers follow.
 * Buffers are LIFO-type containers, very much like queues, but distinctively they have a set size.
 */
public interface IBuffer<T>
		extends ReifiedIterable<T>
{
	/**
	 * Empties the buffer.
	 */
	void clear();

	/**
	 * Checks if an object is contained in the buffer.
	 *
	 * @param obj The object to find.
	 *
	 * @return True, if the specified object is found.
	 *
	 * @throws NullPointerException When the object is null.
	 */
	boolean contains(T obj);

	/**
	 * Returns the maximum size of the buffer
	 */
	int getMaxSize();

	/**
	 * Returns true if the buffer is empty.
	 */
	boolean isEmpty();

	/**
	 * Return true if the buffer is full.
	 */
	boolean isFull();

	/**
	 * Retrieves an object from the buffer.
	 *
	 * @return The object.
	 */
	T get();

	/**
	 * Puts an object in the buffer.
	 *
	 * @param obj The object to put.
	 *
	 * @return True, if the object was put successfully.
	 *
	 * @throws NullPointerException When the object is null.
	 */
	boolean put(T obj);

	/**
	 * Returns the size of the buffer
	 */
	int size();

	/**
	 * Puts all elements in an array and returns them.
	 *
	 * @return All elements.
	 */
	T[] toArray();

	/**
	 * Puts all elements in a list and returns them.
	 *
	 * @return All elements.
	 */
	ReifiedList<T> toList();
}
