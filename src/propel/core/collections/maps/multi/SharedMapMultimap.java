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
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.functional.tuples.Triple;
import propel.core.utils.Linq;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;
import static lombok.Yield.yield;

/**
 * A type-aware thread-safe implementation of a Map of Maps. The SharedMapMultimap has a key, subkeys (i.e. key values) and value (subkey
 * values). This map implementation does not allow null keys/subkeys to be inserted.
 */
public class SharedMapMultimap<T extends Comparable<? super T>, K extends Comparable<? super K>, V>
    implements ISharedMapMultimap<T, K, V>
{
  private final ConcurrentNavigableMap<T, ConcurrentNavigableMap<K, V>> map;
  private final Class<?> keyType;
  private final Class<?> subkeyType;
  private final Class<?> valueType;

  /**
   * Default constructor
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public SharedMapMultimap()
  {
    keyType = SuperTypeToken.getClazz(this.getClass(), 0);
    subkeyType = SuperTypeToken.getClazz(this.getClass(), 1);
    valueType = SuperTypeToken.getClazz(this.getClass(), 2);

    map = new ConcurrentSkipListMap<T, ConcurrentNavigableMap<K, V>>();
  }

  /**
   * Constructor initializes with generic type parameters.
   * 
   * @throws NullPointerException When the generic type parameter is null.
   */
  @Validate
  public SharedMapMultimap(@NotNull final Class<?> keyType, @NotNull final Class<?> subkeyType, @NotNull final Class<?> valueType)
  {
    this.keyType = keyType;
    this.subkeyType = subkeyType;
    this.valueType = valueType;

    map = new ConcurrentSkipListMap<T, ConcurrentNavigableMap<K, V>>();
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
   * {@inheritDoc}
   */
  @Override
  @Validate
  public int size(@NotNull final T key)
  {
    ConcurrentNavigableMap<K, V> m = map.get(key);
    if (m != null)
      return m.size();

    return 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEmpty()
  {
    return map.isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Validate
  public boolean contains(@NotNull final T key, @NotNull final K subkey)
  {
    final ConcurrentNavigableMap<K, V> m = map.get(key);
    return m != null && m.containsKey(subkey);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Validate
  public boolean containsKey(@NotNull final T key)
  {
    return map.containsKey(key);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Validate
  public boolean containsSubkey(@NotNull final K key)
  {
    for (ConcurrentNavigableMap<K, V> m : map.values())
      if (m.containsKey(key))
        return true;

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Validate
  public boolean containsValue(@NotNull final V value)
  {
    for (ConcurrentNavigableMap<K, V> m : map.values())
      if (m.containsValue(value))
        return true;

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Validate
  public V get(@NotNull final T key, @NotNull final K subkey)
  {
    final ConcurrentNavigableMap<K, V> m = map.get(key);
    if (m != null)
      return m.get(subkey);

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Validate
  public Map<K, V> getKey(@NotNull final T key)
  {
    return map.get(key);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Validate
  public Iterable<V> getAllValues(@NotNull final T key)
  {
    ConcurrentNavigableMap<K, V> m = map.get(key);
    if (m != null)
      return m.values();

    return new ArrayList<V>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Validate
  public V getValueBySubkey(@NotNull final K subkey)
  {
    for (ConcurrentNavigableMap<K, V> m : map.values())
    {
      V value = m.get(subkey);
      if (value != null)
        return value;
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Validate
  public Iterable<V> getValuesBySubkey(@NotNull final K subkey)
  {
    for (ConcurrentNavigableMap<K, V> m : map.values())
    {
      V value = m.get(subkey);
      if (value != null)
        yield(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Validate
  public V put(@NotNull final T key, @NotNull final K subkey, V value)
  {
    ConcurrentSkipListMap<K, V> newer = new ConcurrentSkipListMap<K, V>();
    ConcurrentNavigableMap<K, V> m = map.putIfAbsent(key, newer);
    if (m == null)
      m = newer;
    return m.put(subkey, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Validate
  public V putIfAbsent(@NotNull final T key, @NotNull final K subkey, V value)
  {
    ConcurrentSkipListMap<K, V> newer = new ConcurrentSkipListMap<K, V>();
    ConcurrentNavigableMap<K, V> m = map.putIfAbsent(key, newer);
    if (m == null)
      m = newer;
    return m.putIfAbsent(subkey, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Validate
  public V remove(@NotNull final T key, @NotNull final K subkey)
  {
    ConcurrentNavigableMap<K, V> m = map.get(key);
    if (m != null)
      return m.remove(subkey);

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Validate
  public ConcurrentNavigableMap<K, V> removeKey(@NotNull final T key)
  {
    return map.remove(key);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Validate
  public V removeSubKey(@NotNull final K subkey)
  {
    for (ConcurrentNavigableMap<K, V> m : map.values())
    {
      V value = m.get(subkey);
      if (value != null)
        return m.remove(subkey);
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Validate
  public V[] removeSubKeys(@NotNull final K subkey)
  {
    List<V> list = new ArrayList<V>();
    for (ConcurrentNavigableMap<K, V> m : map.values())
    {
      V value = m.remove(subkey);
      if (value != null)
        list.add(value);
    }

    V[] result = Linq.toArray(list, getGenericTypeParameterValue());
    if (result.length == 0)
      result = null;

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear()
  {
    map.clear();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NavigableSet<T> keySet()
  {
    return map.keySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<K> subkeys()
  {
    for (ConcurrentNavigableMap<K, V> m : map.values())
      for (K subkey : m.keySet())
        yield(subkey);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<V> values()
  {
    for (ConcurrentNavigableMap<K, V> m : map.values())
      for (V value : m.values())
        yield(value);
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
      if (subMap != null)
      {
        for (K subkey : subMap.keySet())
        {
          V value = subMap.get(subkey);
          if (value != null)
            result.add(new Triple<T, K, V>(key, subkey, value));
        }
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
    return subkeyType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameterValue()
  {
    return valueType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return Linq.toString(this);
  }
}
