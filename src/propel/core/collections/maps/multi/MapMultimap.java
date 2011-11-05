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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.functional.tuples.Triple;
import propel.core.utils.Linq;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;
import static lombok.Yield.yield;

/**
 * A type-aware implementation of a Map of Maps. The MapMultimap has a key, subkeys (i.e. key values) and value (subkey values). This map
 * implementation does not allow null keys/subkeys to be inserted.
 */
public class MapMultimap<T extends Comparable<? super T>, K extends Comparable<? super K>, V>
    implements ReifiedMultimap<T, K, V>
{
  private final Map<T, Map<K, V>> map;
  private final Class<?> keyType;
  private final Class<?> subKeyType;
  private final Class<?> valueType;

  /**
   * Default constructor
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public MapMultimap()
  {
    keyType = SuperTypeToken.getClazz(this.getClass(), 0);
    subKeyType = SuperTypeToken.getClazz(this.getClass(), 1);
    valueType = SuperTypeToken.getClazz(this.getClass(), 2);

    map = new TreeMap<T, Map<K, V>>();
  }

  /**
   * Constructor initializes with generic type parameters.
   * 
   * @throws NullPointerException When the generic type parameter is null.
   */
  @Validate
  public MapMultimap(@NotNull final Class<?> keyType, @NotNull final Class<?> subKeyType, @NotNull final Class<?> valueType)
  {
    this.keyType = keyType;
    this.subKeyType = subKeyType;
    this.valueType = valueType;

    map = new TreeMap<T, Map<K, V>>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int size()
  {
    return Linq.count(values());
  }
  
  /**
   * Returns the size of a sub-map
   */
  @Validate
  public int size(@NotNull final String key) {
    if(map.containsKey(key))
      return map.get(key).size();
    
    return 0;
  }

  /**
   * Returns true if the map is empty, false otherwise
   */
  public boolean isEmpty()
  {
    return map.isEmpty();
  }

  /**
   * Returns true if the map contains a key
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public boolean contains(@NotNull final T key, @NotNull final K subkey)
  {
    return map.containsKey(key) && map.get(key).containsKey(subkey);
  }

  /**
   * Returns true if the map contains a key
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public boolean containsKey(@NotNull final T key)
  {
    return map.containsKey(key);
  }

  /**
   * Returns true if the map contains a sub-key
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public boolean containsSubkey(@NotNull final K key)
  {
    for (Map<K, V> m : map.values())
      if (m.containsKey(key))
        return true;

    return false;
  }

  /**
   * Returns true if the map contains a value
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public boolean containsValue(@NotNull final V value)
  {
    for (Map<K, V> m : map.values())
      if (m.containsValue(value))
        return true;

    return false;
  }

  /**
   * Returns the first occurrence of a key/sub-key tuple. If no such key/sub-key tuple is found, null is returned.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public V get(@NotNull final T key, @NotNull final K subkey)
  {
    if (map.containsKey(key))
      return map.get(key).get(subkey);

    return null;
  }

  /**
   * Returns the sub-map of a key which stores sub-keys -> values. If no such key is found, null is returned.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public Map<K, V> getKey(@NotNull final T key)
  {
    return map.get(key);
  }

  /**
   * Returns all values held under a key's sub-keys. If no such key is found, an empty iterable is returned.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public Iterable<V> getAllValues(@NotNull final T key)
  {
    if (map.containsKey(key))
      return map.get(key).values();

    return new ArrayList<V>();
  }

  /**
   * Returns the first value of a key's sub-key. If no such sub-key is found, null is returned.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public V getValueBySubkey(@NotNull final K subkey)
  {
    for (Map<K, V> m : map.values())
      if (m.containsKey(subkey))
        return m.get(subkey);

    return null;
  }

  /**
   * Returns all values under a key's sub-key. If no such sub-key is found, an empty iterable is returned.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public Iterable<V> getValuesBySubkey(@NotNull final K subkey)
  {
    for (Map<K, V> m : map.values())
      if (m.containsKey(subkey))
        yield(m.get(subkey));
  }

  /**
   * Inserts a key/subkey/value tuple. Returns the old value. If no old value existed, null is returned.
   * 
   * @throws NullPointerException A key or sub-key is null
   */
  @Validate
  public V put(@NotNull final T key, @NotNull final K subKey, V value)
  {
    if (!map.containsKey(key))
      map.put(key, new TreeMap<K, V>());

    return map.get(key).put(subKey, value);
  }

  /**
   * Removes a key/sub-key tuple value. Returns the removed value, or null if no such key/sub-key tuple existed.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public V remove(@NotNull final T key, @NotNull final K subkey)
  {
    if (map.containsKey(key))
      return map.get(key).remove(subkey);

    return null;
  }

  /**
   * Removes a key's sub-key->value map. Returns the removed value, or null if no such key existed.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public Map<K, V> removeKey(@NotNull final T key)
  {
    return map.remove(key);
  }

  /**
   * Removes the first occurrence of a sub-key. Returns the removed value, or null if no such sub-key existed.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public V removeSubKey(@NotNull final K subkey)
  {
    for (Map<K, V> m : map.values())
      if (m.containsKey(subkey))
        return m.remove(subkey);

    return null;
  }

  /**
   * Removes all occurrences of a sub-key. Returns the removed values, or null if no such sub-key existed.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public V[] removeSubKeys(@NotNull final K subkey)
  {
    List<V> list = new ArrayList<V>();
    for (Map<K, V> m : map.values())
      if (m.containsKey(subkey))
        list.add(m.remove(subkey));

    V[] result = Linq.toArray(list, getGenericTypeParameterValue());
    if (result.length == 0)
      result = null;

    return result;
  }

  /**
   * Clears the entire multimap
   */
  public void clear()
  {
    map.clear();
  }

  /**
   * Returns the key set
   */
  public Set<T> keySet()
  {
    return map.keySet();
  }

  /**
   * Returns all sub-keys, or an empty iterable if no sub-keys exist
   */
  public Iterable<K> subkeys()
  {
    for (Map<K, V> m : map.values())
      for (K subkey : m.keySet())
        yield(subkey);
  }

  /**
   * Returns all values, or an empty iterable if no values exist
   */
  public Iterable<V> values()
  {
    for (T key : map.keySet())
    {
      Map<K, V> subMap = map.get(key);
      for (K subkey : subMap.keySet())
      {
        V value = subMap.get(subkey);
        yield(value);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<Triple<T, K, V>> iterator()
  {
    List<Triple<T, K, V>> result = new ArrayList<Triple<T, K, V>>();
    for (T key : map.keySet())
    {
      Map<K, V> subMap = map.get(key);
      for (K subkey : subMap.keySet())
      {
        V value = subMap.get(subkey);
        result.add(new Triple<T, K, V>(key, subkey, value));
      }
    }

    return result.iterator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameterKey()
  {
    return keyType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameterSubKey()
  {
    return subKeyType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameterValue()
  {
    return valueType;
  }
}
