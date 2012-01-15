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
package propel.core.collections.maps.primitive;

/**
 * Interface of a hash table of primitive long -> V
 */
public interface ILongHashMap<V>
{
  /**
   * Puts the given key in the map, returning any previously associated value of the given key
   */
  V put(long key, V value);

  /**
   * Returns true if the given key exists
   */
  boolean containsKey(long key);

  /**
   * Returns the key/value pair, if the given value exists, otherwise returns null
   * 
   * @throws NullPointerException An argument is null
   */
  LongEntry<V> containsValue(V value);

  /**
   * Returns the set of key/value entries contained
   */
  LongEntry<V>[] entries();

  /**
   * Returns the value associated with a key, or null if no such key exists
   */
  V get(long key);

  /**
   * Returns true if the map is empty
   */
  boolean isEmpty();

  /**
   * Returns all values
   */
  V[] values();

  /**
   * Returns all keys
   */
  long[] keySet();

  /**
   * Returns the number of key/value pairs
   */
  int size();

  /**
   * Returns the map capacity
   */
  int capacity();

}
