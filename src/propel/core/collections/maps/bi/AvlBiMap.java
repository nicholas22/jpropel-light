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
package propel.core.collections.maps.bi;

import propel.core.collections.KeyNotFoundException;
import propel.core.collections.KeyValuePair;
import propel.core.collections.maps.ReifiedMap;
import propel.core.collections.maps.avl.AvlHashtable;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;
import java.util.Iterator;
import java.util.Map;

/**
 * A type-aware AVL-tree-backed bi-directional map. This map does not allow null keys or values to be inserted.
 * 
 * Instantiate using e.g.: new AvlBiMap&lt;String, Object&gt;(){}; -OR- new AvlBiMap&lt;String, Object&gt;(String.class, Object.class);
 */
public class AvlBiMap<TKey extends Comparable<TKey>, TValue extends Comparable<TValue>>
    implements IBiMap<TKey, TValue>
{
  /**
   * The inverse mapping (value->key)
   */
  protected AvlHashtable<TValue, TKey> inverseMap;
  /**
   * The normal mapping (key->value)
   */
  protected AvlHashtable<TKey, TValue> normalMap;

  /**
   * Default constructor
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public AvlBiMap()
  {
    Class<?> keyType = SuperTypeToken.getClazz(this.getClass(), 0);
    Class<?> valueType = SuperTypeToken.getClazz(this.getClass(), 1);

    normalMap = new AvlHashtable<TKey, TValue>(keyType, valueType);
    inverseMap = new AvlHashtable<TValue, TKey>(valueType, keyType);
  }

  /**
   * Constructor for initializing with the key/value generic type parameters
   * 
   * @throws NullPointerException When a generic type parameter is null.
   */
  public AvlBiMap(Class<?> genericTypeParameterKey, Class<?> genericTypeParameterValue)
  {
    if (genericTypeParameterKey == null)
      throw new NullPointerException("genericTypeParameterKey");
    if (genericTypeParameterValue == null)
      throw new NullPointerException("genericTypeParameterValue");

    normalMap = new AvlHashtable<TKey, TValue>(genericTypeParameterKey, genericTypeParameterValue);
    inverseMap = new AvlHashtable<TValue, TKey>(genericTypeParameterValue, genericTypeParameterKey);
  }

  /**
   * Constructor initializes with another reified map
   * 
   * @throws NullPointerException When the argument is null, or a key or value in the map provided is null.
   */
  public AvlBiMap(ReifiedMap<TKey, TValue> map)
  {
    if (map == null)
      throw new NullPointerException("map");

    Class<?> keyType = map.getGenericTypeParameterKey();
    Class<?> valueType = map.getGenericTypeParameterValue();

    normalMap = new AvlHashtable<TKey, TValue>(keyType, valueType);
    inverseMap = new AvlHashtable<TValue, TKey>(valueType, keyType);

    // add elements in a consistent fashion
    for (KeyValuePair<TKey, TValue> entry : map)
    {
      TKey key = entry.getKey();
      TValue value = entry.getValue();
      if (key == null)
        throw new NullPointerException("An entry key was null.");
      if (value == null)
        throw new NullPointerException("An entry value was null.");

      if (!normalMap.containsKey(key))
        if (!inverseMap.containsKey(value))
        {
          normalMap.add(key, value);
          inverseMap.add(value, key);
        }
    }
  }

  /**
   * Constructor initializes from another map
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   * @throws NullPointerException When the argument is null.
   */
  public AvlBiMap(Map<? extends TKey, ? extends TValue> map)
  {
    if (map == null)
      throw new NullPointerException("map");

    Class<?> keyType = SuperTypeToken.getClazz(this.getClass(), 0);
    Class<?> valueType = SuperTypeToken.getClazz(this.getClass(), 1);

    normalMap = new AvlHashtable<TKey, TValue>(keyType, valueType);
    inverseMap = new AvlHashtable<TValue, TKey>(valueType, keyType);

    // add elements in a consistent fashion
    for (Map.Entry<? extends TKey, ? extends TValue> entry : map.entrySet())
    {
      TKey key = entry.getKey();
      TValue value = entry.getValue();
      if (key == null)
        throw new NullPointerException("An entry key was null.");
      if (value == null)
        throw new NullPointerException("An entry value was null.");

      if (!normalMap.containsKey(key))
        if (!inverseMap.containsKey(value))
        {
          normalMap.add(key, value);
          inverseMap.add(value, key);
        }
    }
  }

  /**
   * Constructor initializes from another map and the list-map's key/value generic type parameters
   * 
   * @throws NullPointerException When an argument is null.
   */
  public AvlBiMap(Map<? extends TKey, ? extends TValue> map, Class<?> genericTypeParameterKey, Class<?> genericTypeParameterValue)
  {
    if (map == null)
      throw new NullPointerException("map");

    Class<?> keyType = genericTypeParameterKey;
    Class<?> valueType = genericTypeParameterValue;

    normalMap = new AvlHashtable<TKey, TValue>(keyType, valueType);
    inverseMap = new AvlHashtable<TValue, TKey>(valueType, keyType);

    // add elements in a consistent fashion
    for (Map.Entry<? extends TKey, ? extends TValue> entry : map.entrySet())
    {
      TKey key = entry.getKey();
      TValue value = entry.getValue();
      if (key == null)
        throw new NullPointerException("An entry key was null.");
      if (value == null)
        throw new NullPointerException("An entry value was null.");

      if (!normalMap.containsKey(key))
        if (!inverseMap.containsKey(value))
        {
          normalMap.add(key, value);
          inverseMap.add(value, key);
        }
    }
  }

  /**
   * Adds a key/value pair in the BiMap, returning true if succeeded. Null keys/values are not allowed. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException When the key or the value is null.
   */
  @Override
  public boolean add(TKey key, TValue value)
  {
    if (key == null)
      throw new NullPointerException("key");
    else if (value == null)
      throw new NullPointerException("value");
    else
    {
      if ((!normalMap.getKeys().contains(key)) && !inverseMap.getKeys().contains(value))
      {
        normalMap.add(key, value);
        inverseMap.add(value, key);
        return true;
      }
      return false;
    }
  }

  /**
   * Removes all elements. This is an O(1) operation.
   */
  @Override
  public void clear()
  {
    normalMap.clear();
    inverseMap.clear();
  }

  /**
   * Returns true if a given key exists, false otherwise. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException When the key is null.
   */
  @Override
  public boolean containsKey(TKey key)
  {
    if (key == null)
      throw new NullPointerException("key");
    else if (normalMap.getKeys().contains(key))
      return true;

    return false;
  }

  /**
   * Returns true if a given value exists, false otherwise This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException When the value is null.
   */
  @Override
  public boolean containsValue(TValue value)
  {
    if (value == null)
      throw new NullPointerException("value");
    else if (inverseMap.getKeys().contains(value))
      return true;

    return false;
  }

  /**
   * Returns all keys in ascending key order. This is an O(1) operation.
   */
  @Override
  public Iterable<TKey> getKeys()
  {
    return normalMap.getKeys();
  }

  /**
   * Returns all values in ascending value order. This is an O(1) operation.
   */
  @Override
  public Iterable<TValue> getValues()
  {
    return inverseMap.getKeys();
  }

  /**
   * Returns the value associated with a key, if existent. Otherwise returns null. This is an O(log2(n)) operation.
   * 
   * @throws KeyNotFoundException When the key is not found.
   */
  @Override
  public TValue getByKey(TKey key)
  {
    return normalMap.get(key);
  }

  /**
   * Returns the key associated with a value, if existent. Otherwise returns null. This is an O(log2(n)) operation.
   * 
   * @throws KeyNotFoundException When the key is not found.
   */
  @Override
  public TKey getByValue(TValue value)
  {
    return inverseMap.get(value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameterKey()
  {
    return normalMap.getGenericTypeParameterKey();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameterValue()
  {
    return normalMap.getGenericTypeParameterValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<KeyValuePair<TKey, TValue>> iterator()
  {
    return normalMap.iterator();
  }

  /**
   * Removes an element by its key, returns true if successful. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException When the key is null.
   */
  @Override
  public boolean removeByKey(TKey key)
  {
    if (key == null)
      throw new NullPointerException("key");
    else if (normalMap.getKeys().contains(key))
    {
      TValue value = normalMap.get(key);
      normalMap.remove(key);
      inverseMap.remove(value);

      return true;
    }

    return false;
  }

  /**
   * Removes an element by its value, returns true if successful. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException When the value is null
   */
  @Override
  public boolean removeByValue(TValue value)
  {
    if (value == null)
      throw new NullPointerException("value");
    else if (inverseMap.getKeys().contains(value))
    {
      TKey key = inverseMap.get(value);
      inverseMap.remove(value);
      normalMap.remove(key);

      return true;
    }

    return false;
  }

  /**
   * Replaces an element's value, searching for it by key, returns true if successful. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException When the key or the value is null.
   */
  @Override
  public boolean replaceByKey(TKey key, TValue newValue)
  {
    if (key == null)
      throw new NullPointerException("key");
    else if (normalMap.getKeys().contains(key))
      if (!inverseMap.getKeys().contains(newValue))
      {
        normalMap.replace(key, newValue);
        inverseMap.add(newValue, key);

        return true;
      }

    return false;
  }

  /**
   * Replaces an element's key, searching for it by value, returns true if successful. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException When the value or the key is null.
   */
  @Override
  public boolean replaceByValue(TValue value, TKey newKey)
  {
    if (value == null)
      throw new NullPointerException("value");
    else if (inverseMap.getKeys().contains(value))
      if (!normalMap.getKeys().contains(newKey))
      {
        inverseMap.replace(value, newKey);
        normalMap.add(newKey, value);

        return true;
      }

    return false;
  }

  /**
   * Returns the number of items. This is an O(1) operation.
   */
  @Override
  public int size()
  {
    return normalMap.size();
  }

}
