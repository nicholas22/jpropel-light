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
package propel.core.collections;

import propel.core.collections.maps.ReifiedMap;

/**
 * The interface of a key/value pair store
 *
 * @param <TKey>   The key type
 * @param <TValue> The value type
 */
public interface IKeyValueStore<TKey extends Comparable<TKey>, TValue>
		extends ReifiedMap<TKey, TValue>
{
	/**
	 * Adds an item to the collection.
	 *
	 * @param key   The key to add.
	 * @param value The value to add.
	 *
	 * @return True if successful.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	boolean add(TKey key, TValue value);

	/**
	 * Clears the collection
	 */
	void clear();

	/**
	 * Checks if a key exists.
	 *
	 * @param key The key to find.
	 *
	 * @return True if the key is found.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	boolean containsKey(TKey key);

	/**
	 * Getter for the value associated with a key.
	 *
	 * @throws NullPointerException When the key is null.
	 * @throws propel.core.collections.KeyNotFoundException
	 *                              When the key does not exist.
	 */
	TValue get(TKey key);

	/**
	 * Returns all keys.
	 */
	Iterable<TKey> getKeys();

	/**
	 * Returns all values.
	 */
	Iterable<TValue> getValues();

	/**
	 * Removes an item from the collection.
	 *
	 * @param key The key to find.
	 *
	 * @return True if item was found and removed.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	boolean remove(TKey key);

	/**
	 * Replaces a key's value with the specified new value.
	 *
	 * @param key The key to find.
	 *
	 * @return True if the key was found and replaced.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	boolean replace(TKey key, TValue newValue);

	/**
	 * Returns the collection size
	 */
	int size();
}
