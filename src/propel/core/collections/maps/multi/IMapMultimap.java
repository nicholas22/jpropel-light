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
package propel.core.collections.maps.multi;

import java.util.Map;
import java.util.Set;
import propel.core.collections.maps.multi.ReifiedMultimap;

/**
 * Interface of a MapMultimap (a map of maps). TODO: MapMultimap to inherit from this and use @Override on overridden methods!
 */
public interface IMapMultimap<T extends Comparable<? super T>, K extends Comparable<? super K>, V>
    extends ReifiedMultimap<T, K, V>
{
  /**
   * Returns true if the map is empty, false otherwise
   */
  boolean isEmpty();

  /**
   * Returns the size of a sub-map
   */
  int size(T key);

  /**
   * Returns true if the map contains a key
   * 
   * @throws NullPointerException An argument is null
   */
  boolean contains(T key, K subkey);

  /**
   * Returns true if the map contains a key
   * 
   * @throws NullPointerException An argument is null
   */
  boolean containsKey(T key);

  /**
   * Returns true if the map contains a sub-key
   * 
   * @throws NullPointerException An argument is null
   */
  boolean containsSubkey(K key);

  /**
   * Returns true if the map contains a value
   * 
   * @throws NullPointerException An argument is null
   */
  boolean containsValue(V value);

  /**
   * Returns the first occurrence of a key/sub-key tuple. If no such key/sub-key tuple is found, null is returned.
   * 
   * @throws NullPointerException An argument is null
   */
  V get(T key, K subkey);

  /**
   * Returns the sub-map of a key which stores sub-keys -> values. If no such key is found, null is returned.
   * 
   * @throws NullPointerException An argument is null
   */
  Map<K, V> getKey(T key);

  /**
   * Returns all values held under a key's sub-keys. If no such key is found, an empty iterable is returned.
   * 
   * @throws NullPointerException An argument is null
   */
  Iterable<V> getAllValues(T key);

  /**
   * Returns the first value of a key's sub-key. If no such sub-key is found, null is returned.
   * 
   * @throws NullPointerException An argument is null
   */
  V getValueBySubkey(K subkey);

  /**
   * Returns all values under a key's sub-key. If no such sub-key is found, an empty iterable is returned.
   * 
   * @throws NullPointerException An argument is null
   */
  Iterable<V> getValuesBySubkey(K subkey);

  /**
   * Inserts a key/subkey/value tuple. Returns the old value. If no old value existed, null is returned.
   * 
   * @throws NullPointerException A key or sub-key is null
   */
  V put(T key, K subKey, V value);

  /**
   * Removes a key/sub-key tuple value. Returns the removed value, or null if no such key/sub-key tuple existed.
   * 
   * @throws NullPointerException An argument is null
   */
  V remove(T key, K subkey);

  /**
   * Removes a key's sub-key->value map. Returns the removed value, or null if no such key existed.
   * 
   * @throws NullPointerException An argument is null
   */
  Map<K, V> removeKey(T key);

  /**
   * Removes the first occurrence of a sub-key. Returns the removed value, or null if no such sub-key existed.
   * 
   * @throws NullPointerException An argument is null
   */
  V removeSubKey(K subkey);

  /**
   * Removes all occurrences of a sub-key. Returns the removed values, or null if no such sub-key existed.
   * 
   * @throws NullPointerException An argument is null
   */
  V[] removeSubKeys(K subkey);

  /**
   * Clears the entire multimap
   */
  void clear();

  /**
   * Returns the key set
   */
  Set<T> keySet();

  /**
   * Returns all sub-keys, or an empty iterable if no sub-keys exist
   */
  Iterable<K> subkeys();

  /**
   * Returns all values, or an empty iterable if no values exist
   */
  Iterable<V> values();
}
