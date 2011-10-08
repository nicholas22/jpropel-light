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
package propel.core.collections.maps;

import propel.core.collections.KeyNotFoundException;
import propel.core.collections.ReifiedIterable;
import propel.core.model.IShared;

/**
 * The interface of a thread-safe hashtable
 */
public interface ISharedHashtable<TKey extends Comparable<TKey>, TValue>
		extends ReifiedMap<TKey, TValue>, IShared
{
	/**
	 * Adds a new key/value pair.
	 * Returns true if successful, false if another object with same key exists.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	boolean add(TKey key, TValue value);

	/**
	 * Bulk add key/value pairs to avoid locking/unlocking continuously.
	 * Returns true for elements that were successfully added, false for those that were not.
	 * You are advised not to use null keys, as these are not allowed and failing will leave the collection in an inconsistent state.
	 *
	 * @throws NullPointerException	 When the keys or the values argument is null.
	 * @throws IllegalArgumentException When the keys and values collections do not have the same size.
	 */
	Iterable<Boolean> addRange(Iterable<? extends TKey> keys, Iterable<? extends TValue> values);

	/**
	 * Removes all keys and values.
	 */
	void clear();

	/**
	 * Returns the value of a key, if found.
	 * Otherwise a null value is returned.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	boolean contains(TKey key);

	/**
	 * Returns the key's value if it exists, otherwise throws KeyNotFoundException.
	 *
	 * @throws NullPointerException When the key is null.
	 * @throws KeyNotFoundException When the key was not found.
	 */
	TValue get(TKey key);

	/**
	 * Returns the value of a key, if found.
	 * Otherwise the specified nullValue is returned.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	TValue get(TKey key, TValue nullValue);

	/**
	 * Returns a copy of all keys
	 */
	ReifiedIterable<TKey> getKeys();

	/**
	 * Returns a copy of all values
	 */
	ReifiedIterable<TValue> getValues();

	/**
	 * Removes a value by its key.
	 * Returns true if successfully done, false if no such key.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	boolean remove(TKey key);

	/**
	 * Removes a value by its key and returns it.
	 * If no such key, null is returned.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	TValue removeAndGet(TKey key, TValue nullValue);

	/**
	 * Removes a number of elements by key.
	 * Returns true for those successfully removed, false for those that there was no such key.
	 * You are advised not to use null keys, as these are not allowed and failing will leave the collection in an inconsistent state.
	 *
	 * @throws NullPointerException When the keys is null.
	 */
	Iterable<Boolean> removeRange(Iterable<? extends TKey> keys);

	/**
	 * Replaces a key's value, returning true if this was successful.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	boolean replace(TKey key, TValue newValue);

	/**
	 * Clears the hashtable and bulk adds key/value pairs to avoid locking/unlocking continuously.
	 * Returns true for elements that were successfully added, false for those that were not.
	 * You are advised not to use null keys, as these are not allowed and failing will leave the collection in an inconsistent state.
	 *
	 * @throws NullPointerException	 When the keys or the values argument is null.
	 * @throws IllegalArgumentException When the keys and values collections do not have the same size.
	 */
	Iterable<Boolean> replaceAll(Iterable<? extends TKey> keys, Iterable<? extends TValue> values);

	/**
	 * Returns size of hashtable
	 */
	int size();
}
