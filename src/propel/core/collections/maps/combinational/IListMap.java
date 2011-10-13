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
package propel.core.collections.maps.combinational;

import propel.core.collections.KeyNotFoundException;
import propel.core.collections.KeyValuePair;
import propel.core.collections.maps.ReifiedMap;

/**
 * A type-aware collection of values, accessible by a list-style index as well as key.
 * 
 * @param <TKey> The key type
 * @param <TValue> The value type
 */
public interface IListMap<TKey extends Comparable<TKey>, TValue>
    extends ReifiedMap<TKey, TValue>
{
  /**
   * Adds a new key/value pair. Returns true if successful, false if another entry with the same key exists.
   * 
   * @throws NullPointerException If the key is null.
   */
  boolean add(TKey key, TValue value);

  /**
   * Adds a new key/value pair. Returns true if successful, false if another object with same key exists.
   * 
   * @throws NullPointerException If the key/value pair or the key is null.
   */
  boolean add(KeyValuePair<? extends TKey, ? extends TValue> kvp);

  /**
   * Removes all keys and values.
   */
  void clear();

  /**
   * Returns true if the given key is contained in the collection.
   * 
   * @throws NullPointerException If the key is null.
   */
  boolean contains(TKey key);

  /**
   * Returns the key's value if it exists, otherwise throws KeyNotFoundException.
   * 
   * @throws NullPointerException The argument provided was null.
   * @throws KeyNotFoundException The key was not found.
   */
  TValue get(TKey key);

  /**
   * Returns the key's value if the position exists.
   * 
   * @throws IndexOutOfBoundsException The argument provided was out of range.
   */
  TValue getAt(int index);

  /**
   * Returns all keys in ascending key order.
   */
  Iterable<TKey> getKeys();

  /**
   * Returns all values. The order that they were input is used.
   */
  Iterable<TValue> getValues();

  /**
   * Returns the index of the given key, if it is contained in the collection. Otherwise returns -1 as it is not contained in the
   * collection.
   * 
   * @throws NullPointerException If the key is null.
   */
  int indexOf(TKey key);

  /**
   * Removes an item from the collection. Returns true if item was found and removed.
   * 
   * @throws NullPointerException If the key is null.
   */
  boolean remove(TKey key);

  /**
   * Removes an item from the collection. Returns true if item was found and removed.
   * 
   * @throws NullPointerException If the index is out of range.
   */
  void removeAt(int index);

  /**
   * Replaces a key's value with the specified new value. Returns true if the key was found and replaced.
   * 
   * @throws NullPointerException If the key is null.
   */
  boolean replace(TKey key, TValue newValue);

  /**
   * Sets a value at the specified index.
   * 
   * @throws IndexOutOfBoundsException The index provided was out of range.
   */
  void replaceAt(int index, TValue value);

  /**
   * Returns the number of elements in the collection.
   */
  int size();

}
