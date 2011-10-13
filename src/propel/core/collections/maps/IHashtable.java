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
package propel.core.collections.maps;

import propel.core.TryResult;
import propel.core.collections.KeyNotFoundException;
import propel.core.collections.KeyValuePair;
import propel.core.collections.maps.avl.KeyCollection;
import propel.core.collections.maps.avl.ValueCollection;

/**
 * The interface of a hashtable
 * 
 * @param <TKey> The key type
 * @param <TValue> The value type
 */
public interface IHashtable<TKey extends Comparable<TKey>, TValue>
    extends ReifiedMap<TKey, TValue>
{
  /**
   * Adds an element represented by the provided key/value pair if the key is not already present.
   * 
   * @param kvp The key/value pair to add.
   * 
   * @throws NullPointerException If the key value pair or the key is null.
   */
  void add(KeyValuePair<? extends TKey, ? extends TValue> kvp);

  /**
   * Adds the provided key/value pair if the key is not already present.
   * 
   * @param key The key to add.
   * @param value The value to add.
   * 
   * @return True if the key did not exist, therefore the value was added.
   * 
   * @throws NullPointerException If the key is null.
   */
  boolean add(TKey key, TValue value);

  /**
   * Removes all elements from this hash table.
   */
  void clear();

  /**
   * Returns true if the key of the key/value pair exists in the key collection
   * 
   * @param kvp A key value pair (only the key is used).
   * 
   * @return True if the key is found.
   * 
   * @throws NullPointerException If the key is null.
   */
  boolean contains(KeyValuePair<? extends TKey, ? extends TValue> kvp);

  /**
   * Returns true if the key of the key/value pair exists in the key collection
   * 
   * @param key The key to find.
   * 
   * @return True if the key is found.
   * 
   * @throws NullPointerException If the key is null.
   */
  boolean containsKey(TKey key);

  /**
   * Gets the value associated with the specified key.
   * 
   * @param key The key to find.
   * 
   * @return The associated value.
   * 
   * @throws NullPointerException If the key is null.
   * @throws KeyNotFoundException If the key is not found.
   */
  TValue get(TKey key);

  /**
   * Gets a collection containing all the keys.
   * 
   * @return All keys.
   */
  KeyCollection<TKey, TValue> getKeys();

  /**
   * Gets a collection containing all the values.
   * 
   * @return All values.
   */
  ValueCollection<TKey, TValue> getValues();

  /**
   * Removes a key/value pair based on its key.
   * 
   * @param kvp The key/value pair (only the key is used).
   * 
   * @return True if found and removed.
   * 
   * @throws NullPointerException If the key/value pair or the key is null.
   */
  boolean remove(KeyValuePair<? extends TKey, ? extends TValue> kvp);

  /**
   * Removes the value associated with the specified key.
   * 
   * @param key The key to find.
   * 
   * @return True if found and removed.
   * 
   * @throws NullPointerException If the key is null.
   */
  boolean remove(TKey key);

  /**
   * Replaces a key's value with the specified value.
   * 
   * @param key The key to find.
   * @param value The new value.
   * 
   * @return True if the key is found and replaced.
   * 
   * @throws NullPointerException When the key is null.
   */
  boolean replace(TKey key, TValue value);

  /**
   * Gets the number of key/value pairs contained.
   */
  int size();

  /**
   * Attempts to get a value by a given key.
   * 
   * @param key The key to find.
   * 
   * @return Results in success/failure with the key's value in the case of success.
   * 
   * @throws NullPointerException When the key is null.
   */
  TryResult<TValue> tryGetValue(TKey key);

}
