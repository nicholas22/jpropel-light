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
import propel.core.collections.maps.avl.AvlHashtable;
import propel.core.utils.Linq;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.Functions.Function1;

/**
 * A type-aware AVL-tree-backed map holding values which are accessible by key as well as a list-style index. This map does not allow null
 * keys to be inserted.
 * 
 * Instantiate using e.g.: new AvlTreeList&lt;String, Object&gt;(){}; -OR- new AvlTreeList&lt;String, Object&gt;(String.class,
 * Object.class);
 */
public class AvlTreeList<TKey extends Comparable<TKey>, TValue>
    implements IListMap<TKey, TValue>
{
  // holds key->value
  private final AvlHashtable<TKey, TValue> hashtable;
  // holds values, to allow int->value access
  private final List<KeyValuePair<TKey, TValue>> list;

  /**
   * Default constructor
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public AvlTreeList()
  {
    hashtable = new AvlHashtable<TKey, TValue>(SuperTypeToken.getClazz(this.getClass(), 0), SuperTypeToken.getClazz(this.getClass(), 1));
    list = new ArrayList<KeyValuePair<TKey, TValue>>();
  }

  /**
   * Constructor for initializing with the key/value generic type parameters
   * 
   * @throws NullPointerException When a generic type parameter is null.
   */
  public AvlTreeList(Class<?> genericTypeParameterKey, Class<?> genericTypeParameterValue)
  {
    hashtable = new AvlHashtable<TKey, TValue>(genericTypeParameterKey, genericTypeParameterValue);
    list = new ArrayList<KeyValuePair<TKey, TValue>>();
  }

  /**
   * Constructor initializes with another reified map
   * 
   * @throws NullPointerException When the argument is null.
   */
  public AvlTreeList(ReifiedMap<TKey, TValue> map)
  {
    if (map == null)
      throw new NullPointerException("map");

    hashtable = new AvlHashtable<TKey, TValue>(map.getGenericTypeParameterKey(), map.getGenericTypeParameterValue());
    list = new ArrayList<KeyValuePair<TKey, TValue>>();

    for (KeyValuePair<TKey, TValue> entry : map)
      if (hashtable.add(entry.getKey(), entry.getValue()))
        list.add(entry);
  }

  /**
   * Constructor initializes from another map
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   * @throws NullPointerException When the argument is null.
   */
  public AvlTreeList(Map<? extends TKey, ? extends TValue> map)
  {
    if (map == null)
      throw new NullPointerException("map");

    hashtable = new AvlHashtable<TKey, TValue>(SuperTypeToken.getClazz(this.getClass(), 0), SuperTypeToken.getClazz(this.getClass(), 1));
    list = new ArrayList<KeyValuePair<TKey, TValue>>();

    for (Map.Entry<? extends TKey, ? extends TValue> entry : map.entrySet())
    {
      TKey key = entry.getKey();
      TValue value = entry.getValue();
      if (hashtable.add(key, value))
        list.add(new KeyValuePair<TKey, TValue>(key, value));
    }
  }

  /**
   * Constructor initializes from another map and the list-map's key/value generic type parameters
   * 
   * @throws NullPointerException When an argument is null.
   */
  public AvlTreeList(Map<? extends TKey, ? extends TValue> map, Class<?> genericTypeParameterKey, Class<?> genericTypeParameterValue)
  {
    if (map == null)
      throw new NullPointerException("map");
    if (genericTypeParameterKey == null)
      throw new NullPointerException("genericTypeParameterKey");
    if (genericTypeParameterValue == null)
      throw new NullPointerException("genericTypeParameterValue");

    hashtable = new AvlHashtable<TKey, TValue>(genericTypeParameterKey, genericTypeParameterValue);
    list = new ArrayList<KeyValuePair<TKey, TValue>>();

    for (Map.Entry<? extends TKey, ? extends TValue> entry : map.entrySet())
    {
      TKey key = entry.getKey();
      TValue value = entry.getValue();
      if (hashtable.add(key, value))
        list.add(new KeyValuePair<TKey, TValue>(key, value));
    }
  }

  /**
   * Adds a new key/value pair. Returns true if successful, false if another object with same key exists. This is an O(log2(n)) operation.
   * 
   * @return True if added
   * 
   * @throws NullPointerException When the key is null.
   */
  @Override
  public boolean add(TKey key, TValue value)
  {
    if (key == null)
      throw new NullPointerException("key");

    if (hashtable.add(key, value))
    {
      list.add(new KeyValuePair<TKey, TValue>(key, value));
      return true;
    }

    return false;
  }

  /**
   * Adds a new key/value pair. Returns true if successful, false if another object with same key exists. This is an O(log2(n)) operation.
   * 
   * @return True if added.
   * 
   * @throws NullPointerException When the key or the key/value pair is null.
   */
  @Override
  public boolean add(KeyValuePair<? extends TKey, ? extends TValue> kvp)
  {
    return add(kvp.getKey(), kvp.getValue());
  }

  /**
   * Removes all keys and values. This is an O(1) operation.
   */
  @Override
  public void clear()
  {
    list.clear();
    hashtable.clear();
  }

  /**
   * Returns true if a key is found. This is an O(log2(n)) operation.
   * 
   * @return True if contained.
   */
  @Override
  public boolean contains(TKey key)
  {
    if (key == null)
      throw new NullPointerException("key");

    return hashtable.containsKey(key);
  }

  /**
   * Returns the key's value if it exists, otherwise throws KeyNotFoundException. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException The argument provided was null.
   * @throws KeyNotFoundException The key was not found.
   */
  @Override
  public TValue get(TKey key)
  {
    if (key == null)
      throw new NullPointerException("key");

    return hashtable.get(key);
  }

  /**
   * Returns the key's value if the position exists. This is an O(1) operation.
   * 
   * @throws IndexOutOfBoundsException The index provided was out of range.
   */
  @Override
  public TValue getAt(int index)
  {
    if (index < 0 || index >= list.size())
      throw new IndexOutOfBoundsException("index=" + index + " size=" + list.size());

    return list.get(index).getValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameterKey()
  {
    return hashtable.getGenericTypeParameterKey();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameterValue()
  {
    return hashtable.getGenericTypeParameterValue();
  }

  /**
   * Returns all keys in ascending key order. This is an O(1) operation.
   */
  @Override
  public Iterable<TKey> getKeys()
  {
    return hashtable.getKeys();
  }

  /**
   * Returns all values in the order they were input. This is an O(n) operation.
   */
  @Override
  public Iterable<TValue> getValues()
  {
    return Linq.select(list, new Function1<KeyValuePair<TKey, TValue>, TValue>() {

      @Override
      public TValue apply(KeyValuePair<TKey, TValue> arg0)
      {
        return arg0.getValue();
      }
    });
  }

  /**
   * Returns the index of the given key, if it is contained in the collection. Otherwise returns -1 as it is not contained in the
   * collection. This is an O(n) operation.
   * 
   * @throws NullPointerException If the key is null
   */
  @Override
  public int indexOf(TKey key)
  {
    if (key == null)
      throw new NullPointerException("key");

    if (!hashtable.containsKey(key))
      return -1;

    TValue value = hashtable.get(key);
    return list.indexOf(new KeyValuePair<TKey, TValue>(key, value));
  }

  /**
   * Returns an ascending key order iterator. This is an O(1) operation.
   */
  @Override
  public Iterator<KeyValuePair<TKey, TValue>> iterator()
  {
    return hashtable.iterator();
  }

  /**
   * Removes an item from the collection. Returns true if item was found and removed. This is an O(n) operation.
   * 
   * @throws NullPointerException If the key is null
   */
  @Override
  public boolean remove(TKey key)
  {
    if (key == null)
      throw new NullPointerException("key");

    if (!hashtable.containsKey(key))
      return false;

    if (hashtable.remove(key))
    {
      // find key in list
      int index = 0;
      for (int i = 0; i < list.size(); i++)
        if (list.get(i).getKey().equals(key))
        {
          index = i;
          break;
        }
      list.remove(index);
      return true;
    }

    return false;
  }

  /**
   * Removes an item from the collection. Returns true if item was found and removed. This is an O(n) operation.
   * 
   * @throws IndexOutOfBoundsException If the index is out of range
   */
  @Override
  public void removeAt(int index)
  {
    if (index < 0 || index >= list.size())
      throw new IndexOutOfBoundsException("index=" + index + " size=" + size());

    KeyValuePair<TKey, TValue> kvp = list.get(index);
    if (hashtable.remove(kvp.getKey()))
      list.remove(index);
  }

  /**
   * Replaces a key's value with the specified new value. Returns true if the key was found and replaced. This is an O(n) operation.
   * 
   * @throws NullPointerException If the key is null.
   */
  @Override
  public boolean replace(TKey key, TValue newValue)
  {
    if (key == null)
      throw new NullPointerException("key");

    TValue value;
    try
    {
      value = hashtable.get(key);
    }
    catch(KeyNotFoundException e)
    {
      return false;
    }

    int index = list.indexOf(new KeyValuePair<TKey, TValue>(key, value));
    list.set(index, new KeyValuePair<TKey, TValue>(key, newValue));

    hashtable.replace(key, newValue);

    return false;
  }

  /**
   * Sets a value at the specified index. This is an O(log2(n)) operation.
   * 
   * @throws IndexOutOfBoundsException The index provided was out of range.
   */
  @Override
  public void replaceAt(int index, TValue newValue)
  {
    if (index < 0 || index >= list.size())
      throw new IndexOutOfBoundsException("index=" + index + " size=" + list.size());

    KeyValuePair<TKey, TValue> kvp = list.get(index);
    list.set(index, new KeyValuePair<TKey, TValue>(kvp.getKey(), newValue));

    hashtable.replace(kvp.getKey(), newValue);
  }

  /**
   * Returns the number of elements in the collection. # This is an O(1) operation.
   */
  @Override
  public int size()
  {
    return list.size();
  }

}
