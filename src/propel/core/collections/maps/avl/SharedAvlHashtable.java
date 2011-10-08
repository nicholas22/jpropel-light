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
package propel.core.collections.maps.avl;

import propel.core.collections.KeyValuePair;
import propel.core.collections.ReifiedIterable;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.maps.ISharedHashtable;
import propel.core.collections.maps.ReifiedMap;
import propel.core.utils.Linq;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A type-aware thread-safe AVL-tree-backed hashtable.
 * This map does not allow null keys to be inserted.
 * 
 * Instantiate using e.g.:
 * new SharedAvlHashtable&lt;String, Object&gt;(){}; 
 * -OR-
 * new SharedAvlHashtable&lt;String, Object&gt;(String.class, Object.class);
 */
public class SharedAvlHashtable<TKey extends Comparable<TKey>, TValue>
		implements ISharedHashtable<TKey, TValue>
{
	private final AvlHashtable<TKey, TValue> hashtable;
	private final ReentrantLock reEntrantLock;

	/**
	 * Default constructor
	 *
	 * @throws SuperTypeTokenException When called without using anonymous class semantics.
	 */
	public SharedAvlHashtable()
	{
		Class<?> keyClass = SuperTypeToken.getClazz(this.getClass(), 0);
		Class<?> valueClass = SuperTypeToken.getClazz(this.getClass(), 1);

		hashtable = new AvlHashtable<TKey, TValue>(keyClass, valueClass);
		reEntrantLock = new ReentrantLock();
	}

	/**
	 * Constructor for initializing with the key/value generic type parameters
	 *
	 * @throws NullPointerException When a generic type parameter is null.
	 */
	public SharedAvlHashtable(Class<?> genericTypeParameterKey, Class<?> genericTypeParameterValue)
	{
		if(genericTypeParameterKey == null)
			throw new NullPointerException("genericTypeParameterKey");
		if(genericTypeParameterValue == null)
			throw new NullPointerException("genericTypeParameterValue");

		hashtable = new AvlHashtable<TKey, TValue>(genericTypeParameterKey, genericTypeParameterValue);
		reEntrantLock = new ReentrantLock();
	}

	/**
	 * Constructor initializes with another reified map
	 *
	 * @throws NullPointerException When the argument is null, or a key in the map provided is null.
	 */
	public SharedAvlHashtable(ReifiedMap<TKey, TValue> map)
	{
		if(map == null)
			throw new NullPointerException("map");

		hashtable = new AvlHashtable<TKey, TValue>(map);
		reEntrantLock = new ReentrantLock();
	}

	/**
	 * Constructor initializes from another map
	 *
	 * @throws SuperTypeTokenException When called without using anonymous class semantics.
	 * @throws NullPointerException	When the argument is null.
	 */
	public SharedAvlHashtable(Map<? extends TKey, ? extends TValue> map)
	{
		if(map == null)
			throw new NullPointerException("map");

		Class<?> keyClass = SuperTypeToken.getClazz(this.getClass(), 0);
		Class<?> valueClass = SuperTypeToken.getClazz(this.getClass(), 1);

		hashtable = new AvlHashtable<TKey, TValue>(map, keyClass, valueClass);
		reEntrantLock = new ReentrantLock();
	}

	/**
	 * Constructor initializes from another map and the list-map's key/value generic type parameters
	 *
	 * @throws NullPointerException When an argument is null.
	 */
	public SharedAvlHashtable(Map<? extends TKey, ? extends TValue> map, Class<?> genericTypeParameterKey, Class<?> genericTypeParameterValue)
	{
		if(map == null)
			throw new NullPointerException("map");
		if(genericTypeParameterKey == null)
			throw new NullPointerException("genericTypeParameterKey");
		if(genericTypeParameterValue == null)
			throw new NullPointerException("genericTypeParameterValue");

		hashtable = new AvlHashtable<TKey, TValue>(map, genericTypeParameterKey, genericTypeParameterValue);
		reEntrantLock = new ReentrantLock();
	}

	/**
	 * Adds a new key/value pair.
	 * Returns true if successful, false if another object with same key exists.
	 * This is an O(log2(n)) operation.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	@Override
	public boolean add(TKey key, TValue value)
	{
		if(key == null)
			throw new NullPointerException("key");

		lock();
		try
		{
			return hashtable.add(key, value);
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Bulk add key/value pairs to avoid locking/unlocking continuously.
	 * Returns true for elements that were successfully added, false for those that were not.
	 * You are advised not to use null keys, as these are not allowed and failing will leave the collection in an inconsistent state.
	 * This is an O(mlog2(n)) operation where m is the provided element count, n is the hashtables's element Count.
	 *
	 * @throws NullPointerException	 When the keys or values argument is null.
	 * @throws IllegalArgumentException When the key count is not equal to the value count in the provided sequences.
	 */
	@Override
	public Iterable<Boolean> addRange(Iterable<? extends TKey> keys, Iterable<? extends TValue> values)
	{
		if(keys == null)
			throw new NullPointerException("keys");
		if(values == null)
			throw new NullPointerException("values");

		int keyCount = Linq.count(keys);
		int valueCount = Linq.count(values);
		if(Linq.count(keys) != Linq.count(values))
			throw new IllegalArgumentException("keys=" + keyCount + " values=" + valueCount);

		// where results are put
		List<Boolean> result = new ArrayList<Boolean>(keyCount);

		lock();
		try
		{
			// iterate both
			Iterator<? extends TKey> keyIterator = keys.iterator();
			Iterator<? extends TValue> valueIterator = values.iterator();

			while(keyIterator.hasNext())
			{
				// get key/value pair
				TKey key = keyIterator.next();
				TValue value = valueIterator.next();

				// add
				boolean inserted = hashtable.add(key, value);
				result.add(inserted);
			}

			return result;
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Removes all keys and values.
	 * This is an O(1) operation.
	 */
	@Override
	public void clear()
	{
		lock();
		try
		{
			hashtable.clear();
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Returns the value of a key, if found.
	 * Otherwise a null value is returned.
	 * This is an O(log2(n)) operation.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	@Override
	public boolean contains(TKey key)
	{
		if(key == null)
			throw new NullPointerException("key");

		lock();
		try
		{
			return hashtable.keys.contains(key);
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Attempts to get the given key's value.
	 * If not found, then null is returned.
	 * This is an O(log2(n)) operation.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	@Override
	public TValue get(TKey key)
	{
		return get(key, null);
	}

	/**
	 * Returns the value of a key, if found.
	 * Otherwise the specified defaultValue (E.g. null) is returned.
	 * This is an O(log2(n)) operation.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	@Override
	public TValue get(TKey key, TValue defaultValue)
	{
		if(key == null)
			throw new NullPointerException("key");

		lock();
		try
		{
			return hashtable.keys.contains(key) ? hashtable.get(key) : defaultValue;
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Returns a copy of all keys
	 */
	@Override
	public ReifiedIterable<TKey> getKeys()
	{
		lock();
		try
		{
			return new ReifiedArrayList<TKey>(hashtable.getKeys(), hashtable.getGenericTypeParameterKey());
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Returns a copy of all values
	 */
	@Override
	public ReifiedIterable<TValue> getValues()
	{
		lock();
		try
		{
			return new ReifiedArrayList<TValue>(hashtable.getValues(), hashtable.getGenericTypeParameterValue());
		}
		finally
		{
			unlock();
		}
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
	 * Returns all key/value pairs in a list copy of key/value pairs.
	 * This is an O(n) operation.
	 */
	@Override
	public Iterator<KeyValuePair<TKey, TValue>> iterator()
	{
		lock();
		try
		{
			List<KeyValuePair<TKey, TValue>> result = new ArrayList<KeyValuePair<TKey, TValue>>(hashtable.size());

			for(KeyValuePair<TKey, TValue> kvp : hashtable)
				result.add(kvp);

			return result.iterator();
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Removes a value by its key.
	 * Returns true if successfully done, false if no such key.
	 * This is an O(log2(n)) operation.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	@Override
	public boolean remove(TKey key)
	{
		if(key == null)
			throw new NullPointerException("key");

		lock();
		try
		{
			return hashtable.remove(key);
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Removes a number of elements by key.
	 * Returns true for those successfully removed, false for those that there was no such key.
	 * You are advised not to use null keys, as these are not allowed and failing will leave the collection in an inconsistent state.
	 * This is an O(mlog2(n)) operation where m is the provided element count, n is the hashtables's element Count.
	 *
	 * @throws NullPointerException When the keys argument is null.
	 */
	@Override
	public Iterable<Boolean> removeRange(Iterable<? extends TKey> keys)
	{
		if(keys == null)
			throw new NullPointerException("keys");

		// where results are put
		List<Boolean> result = new ArrayList<Boolean>(64);

		lock();
		try
		{
			for(TKey key : keys)
				result.add(hashtable.remove(key));

			return result;
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * If the key is contained, the key/value pair is removed and the value returned.
	 * Otherwise null is returned.
	 * This is an O(log2(n)) operation.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	public TValue removeAndGet(TKey key)
	{
		if(key == null)
			throw new NullPointerException("key");

		return removeAndGet(key, null);
	}

	/**
	 * If the key is contained, the key/value pair is removed and the value returned.
	 * Otherwise the specified defaultValue (E.g. null) is returned.
	 * This is an O(log2(n)) operation.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	@Override
	public TValue removeAndGet(TKey key, TValue defaultValue)
	{
		if(key == null)
			throw new NullPointerException("key");

		TValue result = defaultValue;

		lock();
		try
		{
			if(hashtable.getKeys().contains(key))
			{
				result = hashtable.get(key);
				hashtable.remove(key);
			}
		}
		finally
		{
			unlock();
		}

		return result;
	}

	/**
	 * Replaces a key's value, returning true if this was successful.
	 *
	 * @throws NullPointerException When the key is null.
	 */
	@Override
	public boolean replace(TKey key, TValue newValue)
	{
		lock();
		try
		{
			return hashtable.replace(key, newValue);
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Clears the hashtable and bulk adds key/value pairs to avoid locking/unlocking continuously.
	 * Returns true for elements that were successfully added, false for those that were not.
	 * You are advised not to use null keys, as these are not allowed and failing will leave the collection in an inconsistent state.
	 * This is an O(mlog2(n)) operation where m is the provided element count, n is the hashtables's element Count.
	 *
	 * @throws NullPointerException	 When the keys or values argument is null.
	 * @throws IllegalArgumentException When the key count is not equal to the value count in the given collections.
	 */
	@Override
	public Iterable<Boolean> replaceAll(Iterable<? extends TKey> keys, Iterable<? extends TValue> values)
	{
		lock();
		try
		{
			clear();
			return addRange(keys, values);
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Returns the size of the AVL hashtable.
	 * This is an O(1) operation.
	 */
	@Override
	public int size()
	{
		lock();
		try
		{
			return hashtable.size();
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Locks the collection
	 */
	@Override
	public void lock()
	{
		reEntrantLock.lock();
	}

	/**
	 * Unlocks the collection.
	 *
	 * @throws IllegalMonitorStateException When the current thread does not own the lock.
	 */
	@Override
	public void unlock()
	{
		reEntrantLock.unlock();
	}

}
