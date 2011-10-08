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
package propel.core.collections.maps.combinational;

import propel.core.collections.KeyNotFoundException;
import propel.core.collections.KeyValuePair;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.maps.ReifiedMap;
import propel.core.utils.Linq;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.Functions.Function1;

/**
 * A type-aware list-backed map holding values which are accessible by key as well as a list-style index.
 * This map does not allow null keys to be inserted.
 * 
 * Instantiate using e.g.:
 * new ListMap&lt;String, Object&gt;(){}; 
 * -OR-
 * new ListMap&lt;String, Object&gt;(String.class, Object.class);
 */
public class ListMap<TKey extends Comparable<TKey>, TValue>
		implements IListMap<TKey, TValue>
{
	// keys stored here
	private final ReifiedArrayList<TKey> keyStore;
	// values stored here
	private final ReifiedArrayList<TValue> valueStore;

	/**
	 * Default constructor.
	 *
	 * @throws SuperTypeTokenException When called without using anonymous class semantics.
	 */
	public ListMap()
	{
		this(ReifiedArrayList.DEFAULT_SIZE);
	}

	/**
	 * Constructor for initializing with the key/value generic type parameters
	 *
	 * @throws NullPointerException When a generic type parameter is null.
	 */
	public ListMap(Class<?> genericTypeParameterKey, Class<?> genericTypeParameterValue)
	{
		this(ReifiedArrayList.DEFAULT_SIZE, genericTypeParameterKey, genericTypeParameterValue);
	}

	/**
	 * Initializes the list-map with an initial size.
	 *
	 * @throws IllegalArgumentException When the buffer size is non positive.
	 * @throws SuperTypeTokenException  When called without using anonymous class semantics.
	 */
	public ListMap(int initialSize)
	{
		keyStore = new ReifiedArrayList<TKey>(initialSize, SuperTypeToken.getClazz(this.getClass(), 0));
		valueStore = new ReifiedArrayList<TValue>(initialSize, SuperTypeToken.getClazz(this.getClass(), 1));
	}

	/**
	 * Constructor initializes with an initial collection size and the key/value generic type parameters.
	 *
	 * @throws IllegalArgumentException When the buffer size is non positive.
	 * @throws NullPointerException	 When a generic type parameter is null.
	 */
	public ListMap(int initialSize, Class<?> genericTypeParameterKey, Class<?> genericTypeParameterValue)
	{
		keyStore = new ReifiedArrayList<TKey>(initialSize, genericTypeParameterKey);
		valueStore = new ReifiedArrayList<TValue>(initialSize, genericTypeParameterValue);
	}

	/**
	 * Constructor initializes with another reified map
	 *
	 * @throws NullPointerException When the argument is null.
	 */
	public ListMap(ReifiedMap<TKey, TValue> map)
	{
		if(map == null)
			throw new NullPointerException("map");

		keyStore = new ReifiedArrayList<TKey>(Linq.select(map,
				new Function1<KeyValuePair<TKey, TValue>, TKey>()
				{
					@Override
					public TKey apply(KeyValuePair<TKey, TValue> arg)
					{
						return arg.getKey();
					}
				}), map.getGenericTypeParameterKey());

		valueStore = new ReifiedArrayList<TValue>(Linq.select(map,
				new Function1<KeyValuePair<TKey, TValue>, TValue>()
				{
					@Override
					public TValue apply(KeyValuePair<TKey, TValue> arg)
					{
						return arg.getValue();
					}
				}), map.getGenericTypeParameterValue());
	}

	/**
	 * Constructor initializes from another map
	 *
	 * @throws SuperTypeTokenException When called without using anonymous class semantics.
	 * @throws NullPointerException	When the argument is null.
	 */
	public ListMap(Map<? extends TKey, ? extends TValue> map)
	{
		if(map == null)
			throw new NullPointerException("map");

		int size = map.size();

		keyStore = new ReifiedArrayList<TKey>(size, SuperTypeToken.getClazz(this.getClass(), 0));
		valueStore = new ReifiedArrayList<TValue>(size, SuperTypeToken.getClazz(this.getClass(), 1));

		for(Map.Entry<? extends TKey, ? extends TValue> entry : map.entrySet())
		{
			keyStore.add(entry.getKey());
			valueStore.add(entry.getValue());
		}
	}

	/**
	 * Constructor initializes from another map and the list-map's key/value generic type parameters
	 *
	 * @throws NullPointerException When an argument is null.
	 */
	public ListMap(Map<? extends TKey, ? extends TValue> map, Class<?> genericTypeParameterKey, Class<?> genericTypeParameterValue)
	{
		if(map == null)
			throw new NullPointerException("map");
		if(genericTypeParameterKey == null)
			throw new NullPointerException("genericTypeParameterKey");
		if(genericTypeParameterValue == null)
			throw new NullPointerException("genericTypeParameterValue");

		int size = map.size();

		keyStore = new ReifiedArrayList<TKey>(size, genericTypeParameterKey);
		valueStore = new ReifiedArrayList<TValue>(size, genericTypeParameterValue);

		for(Map.Entry<? extends TKey, ? extends TValue> entry : map.entrySet())
		{
			keyStore.add(entry.getKey());
			valueStore.add(entry.getValue());
		}
	}

	/**
	 * Adds an item to the collection.
	 * Returns true always.
	 * This is an O(1) operation.
	 *
	 * @throws NullPointerException If the key is null.
	 */
	@Override
	public boolean add(TKey key, TValue value)
	{
		if(key == null)
			throw new NullPointerException("key");

		keyStore.add(key);
		valueStore.add(value);

		return true;
	}

	/**
	 * Adds an item to the collection.
	 * Returns true always.
	 * This is an O(1) operation.
	 *
	 * @throws NullPointerException If the key/value pair or the key is null.
	 */
	@Override
	public boolean add(KeyValuePair<? extends TKey, ? extends TValue> kvp)
	{
		if(kvp == null)
			throw new NullPointerException("kvp");

		TKey key = kvp.getKey();
		if(key == null)
			throw new NullPointerException("key");

		keyStore.add(key);
		valueStore.add(kvp.getValue());

		return true;
	}

	/**
	 * Clears the collection.
	 * This is an O(1) operation.
	 */
	@Override
	public void clear()
	{
		keyStore.clear();
		valueStore.clear();
	}

	/**
	 * Returns true if the given key is contained in the collection.
	 * This is an O(n) operation.
	 *
	 * @throws NullPointerException If the key is null.
	 */
	@Override
	public boolean contains(TKey key)
	{
		if(key == null)
			throw new NullPointerException("key");

		int size = keyStore.size();
		for(int i = 0; i < size; i++)
			if(keyStore.get(i).equals(key))
				return true;

		return false;
	}

	/**
	 * Returns the key's value if it exists, otherwise throws KeyNotFoundException.
	 * This is an O(n) operation.
	 *
	 * @throws NullPointerException The argument provided was null.
	 * @throws KeyNotFoundException The key was not found.
	 */
	@Override
	public TValue get(TKey key)
	{
		if(key == null)
			throw new NullPointerException("key");

		int size = keyStore.size();
		for(int i = 0; i < size; i++)
			if(keyStore.get(i).equals(key))
				return valueStore.get(i);

		throw new KeyNotFoundException("The given key was not found: " + key);
	}

	/**
	 * Returns the key's value if the position exists.
	 * This is an O(1) operation.
	 *
	 * @throws IndexOutOfBoundsException The argument provided was out of range.
	 */
	@Override
	public TValue getAt(int index)
	{
		if(index < 0 || index >= keyStore.size())
			throw new IndexOutOfBoundsException("index=" + index + " size=" + keyStore.size());

		return valueStore.get(index);
	}

	/**
	 * Puts all keys in a list and returns them.
	 * Result order matches key/value pair insertion order.
	 * This is an O(1) operation.
	 */
	@Override
	public Iterable<TKey> getKeys()
	{
		return keyStore;
	}

	/**
	 * Puts all keys in a list and returns them.
	 * Result order matches key/value pair insertion order.
	 * This is an O(1) operation.
	 */
	@Override
	public Iterable<TValue> getValues()
	{
		return valueStore;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getGenericTypeParameterKey()
	{
		return keyStore.getGenericTypeParameter();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getGenericTypeParameterValue()
	{
		return valueStore.getGenericTypeParameter();
	}

	/**
	 * Returns the index of the given key, if it is contained in the collection.
	 * Otherwise returns -1 as it is not contained in the collection.
	 * This is an O(n) operation.
	 *
	 * @throws NullPointerException If the key is null.
	 */
	@Override
	public int indexOf(TKey key)
	{
		if(key == null)
			throw new NullPointerException("key");

		int size = keyStore.size();
		for(int i = 0; i < size; i++)
			if(keyStore.get(i).equals(key))
				return i;

		return -1;
	}

	/**
	 * Returns an iterator of the collection.
	 * This is an O(1) operation.
	 */
	@Override
	public Iterator<KeyValuePair<TKey, TValue>> iterator()
	{
		return new ListMapIterator<TKey, TValue>(keyStore, valueStore);
	}

	/**
	 * Removes an item from the collection.
	 * Returns true if item was found and removed.
	 * This is an O(n) operation.
	 *
	 * @throws NullPointerException If the key is null.
	 */
	@Override
	public boolean remove(TKey key)
	{
		if(key == null)
			throw new NullPointerException("key");

		int size = keyStore.size();
		for(int i = 0; i < size; i++)
			if(keyStore.get(i).equals(key))
			{
				keyStore.remove(i);
				valueStore.remove(i);
				return true;
			}

		return false;
	}

	/**
	 * Removes an item from the collection.
	 * Returns true if item was found and removed.
	 * This is an O(n) operation.
	 *
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 */
	@Override
	public void removeAt(int index)
	{
		if(index < 0 || index >= keyStore.size())
			throw new IndexOutOfBoundsException("index=" + index + " size=" + keyStore.size());

		keyStore.remove(index);
		valueStore.remove(index);
	}

	/**
	 * Replaces a key's value with the specified new value.
	 * Returns true if the key was found and replaced.
	 * This is an O(n) operation.
	 *
	 * @throws NullPointerException If the key is null.
	 */
	@Override
	public boolean replace(TKey key, TValue newValue)
	{
		if(key == null)
			throw new NullPointerException("key");

		int size = keyStore.size();
		for(int i = 0; i < size; i++)
			if(keyStore.get(i).equals(key))
			{
				valueStore.set(i, newValue);
				return true;
			}

		return false;
	}

	/**
	 * Sets a value at the specified index.
	 * This is an O(1) operation.
	 *
	 * @throws IndexOutOfBoundsException The index provided was out of range.
	 */
	@Override
	public void replaceAt(int index, TValue newValue)
	{
		if(index < 0 || index >= keyStore.size())
			throw new IndexOutOfBoundsException("index=" + index + " size=" + keyStore.size());

		valueStore.set(index, newValue);
	}

	/**
	 * Returns the collection size.
	 * This is an O(1) operation.
	 */
	@Override
	public int size()
	{
		return valueStore.size();
	}

	/**
	 * Iterates over the two backing lists
	 */
	private class ListMapIterator<LMTKey, LMTValue>
			implements Iterator<KeyValuePair<LMTKey, LMTValue>>
	{
		private final List<LMTKey> keys;
		private final List<LMTValue> values;
		private int index;

		public ListMapIterator(List<LMTKey> keys, List<LMTValue> values)
		{
			this.keys = keys;
			this.values = values;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext()
		{
			return index < keys.size();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public KeyValuePair<LMTKey, LMTValue> next()
		{
			KeyValuePair<LMTKey, LMTValue> kvp = new KeyValuePair<LMTKey, LMTValue>(keys.get(index), values.get(index));
			index++;
			return kvp;
		}

		/**
		 * @throws UnsupportedOperationException Always.
		 * @deprecated
		 */
		@Deprecated
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("This method is not supported.");
		}
	}

}
