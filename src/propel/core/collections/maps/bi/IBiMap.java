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
package propel.core.collections.maps.bi;

import propel.core.collections.maps.ReifiedMap;

/**
 * A type-aware bi-directional AVL-tree-backed map.
 * BiMaps require that keys and values are unique and can be accessed by key or by value.
 *
 * @param <TKey>   The key type
 * @param <TValue> The value type
 */
public interface IBiMap<TKey extends Comparable<TKey>, TValue extends Comparable<TValue>>
		extends ReifiedMap<TKey, TValue>
{
	/**
	 * Returns all keys
	 */
	Iterable<TKey> getKeys();

	/**
	 * Returns all values
	 */
	Iterable<TValue> getValues();

	/**
	 * Adds a key/value pair in the AvlBiMap, returning true if succeeded.
	 * Null keys/values are not allowed.
	 *
	 * @throws NullPointerException When the key or the value is null.
	 */
	boolean add(TKey key, TValue value);

	/**
	 * Removes all elements
	 */
	void clear();

	/**
	 * Returns true if a given key exists, false otherwise
	 *
	 * @throws NullPointerException When the key is null
	 */
	boolean containsKey(TKey key);

	/**
	 * Returns true if a given value exists, false otherwise.
	 *
	 * @throws NullPointerException When the value is null.
	 */
	boolean containsValue(TValue value);

	/**
	 * Returns the value associated with a key, if existent.
	 * Otherwise returns null.
	 */
	TValue getByKey(TKey key);

	/**
	 * Returns the key associated with a value, if existent.
	 * Otherwise returns null.
	 */
	TKey getByValue(TValue value);

	/**
	 * Removes an element by its key, returns true if successful.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	boolean removeByKey(TKey key);

	/**
	 * Removes an element by its value, returns true if successful.
	 *
	 * @throws NullPointerException When the value is null.
	 */
	boolean removeByValue(TValue value);

	/**
	 * Replaces an element's value, searching for it by key, returns true if successful.
	 *
	 * @throws NullPointerException When the key or the value is null.
	 */
	boolean replaceByKey(TKey key, TValue newValue);

	/**
	 * Replaces an element's key, searching for it by value, returns true if successful.
	 *
	 * @throws NullPointerException When the key or the value is null.
	 */
	boolean replaceByValue(TValue value, TKey newKey);

	/**
	 * Returns the number of keys
	 */
	int size();
}
