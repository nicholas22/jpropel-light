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

import java.lang.reflect.Array;
import lombok.Validate;
import lombok.Validate.NotNull;
import lombok.val;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.lists.SharedList;
import propel.core.utils.ArrayUtils;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;

/**
 * Thread-safe implementation of a long -> V hash map.
 */
public class SharedLongHashMap<V>
    implements ILongHashMap<V>
{
  /**
   * The maximum capacity, used if a higher value is implicitly specified by either of the constructors with arguments
   */
  private static final int MAXIMUM_CAPACITY = 1 << 30;
  private final Class<?> genericTypeParameterValue;
  private SharedList<LongEntry<V>>[] table;
  private int size;

  /**
   * Constructs an empty map with the specified max capacity
   * 
   * @param maxCapacity The maximum capacity
   * @throws IllegalArgumentException An argument is invalid
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public SharedLongHashMap(final int maxCapacity)
  {
    this.genericTypeParameterValue = SuperTypeToken.getClazz(getClass());
    init(maxCapacity);
  }

  /**
   * Constructs an empty map with the specified max capacity.
   * 
   * @param maxCapacity The maximum capacity
   * @throws IllegalArgumentException An argument is invalid
   */
  @Validate
  public SharedLongHashMap(final int maxCapacity, @NotNull final Class<?> genericTypeParameterValue)
  {
    this.genericTypeParameterValue = genericTypeParameterValue;
    init(maxCapacity);
  }

  /**
   * Initialisation moved here for re-use
   */
  private void init(final int maxCapacity)
  {
    if (maxCapacity < 0)
      throw new IllegalArgumentException("maxCapacity=" + maxCapacity);
    // Find a power of 2 >= maxSize
    int capacity = 1;
    while (capacity < maxCapacity)
      capacity <<= 1;
    // must not be over this maximum
    if (capacity > MAXIMUM_CAPACITY)
      capacity = MAXIMUM_CAPACITY;
    this.table = create(SharedList.class, capacity);
    // eager population
    for (int i = 0; i < this.table.length; i++)
      table[i] = new SharedList<LongEntry<V>>(LongEntry.class);
  }

  /**
   * Creates a generic array
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException If componentType is {@link Void#TYPE}
   * @throws ClassCastException An invalid type parameter was specified
   * @throws NegativeArraySizeException If the specified size is negative
   */
  @SuppressWarnings("unchecked")
  private static <T> T[] create(final Class<?> componentType, final int size)
  {
    return (T[]) Array.newInstance(componentType, size);
  }

  /**
   * {@inheritDoc}
   * 
   * This is an O(1) operation
   */
  @Override
  public V put(final long key, final V value)
  {
    int index = indexFor(key);

    val bucket = table[index];
    bucket.lock();
    try
    {
      val inserted = new LongEntry<V>(key, value);

      // attempt to find old, if existent
      int existingIndex = findInBucket(bucket, key);

      // if existent, replace
      if (existingIndex >= 0)
      {
        // this does not increase size
        val oldItem = bucket.set(existingIndex, inserted);
        return oldItem.value;
      }

      // otherwise insert new
      bucket.add(inserted);
      size++;
      return null;
    }
    finally
    {
      bucket.unlock();
    }
  }

  /**
   * {@inheritDoc}
   * 
   * This is an O(1) operation
   */
  @Override
  public boolean containsKey(final long key)
  {
    int index = indexFor(key);
    val bucket = table[index];
    return findInBucket(bucket, key) >= 0;
  }

  /**
   * {@inheritDoc}
   * 
   * This is an O(n) operation
   */
  @Override
  @Validate
  public LongEntry<V> containsValue(@NotNull final V value)
  {
    for (val bucket : table)
      for (val item : bucket)
          if (item != null)
            if (item.equals(value))
              return item;

    return null;
  }

  /**
   * {@inheritDoc}
   * 
   * This is an O(n) operation
   */
  @Override
  public LongEntry<V>[] entries()
  {
    val result = new ReifiedArrayList<LongEntry<V>>(size, LongEntry.class);
    for (val bucket : table)
        result.addAll(bucket);

    return result.toArray();
  }

  /**
   * {@inheritDoc}
   * 
   * This is an O(1) operation
   */
  @Override
  public V get(long key)
  {
    val bucket = table[indexFor(key)];
      for (val item : bucket)
        if (item.key == key)
          return item.value;

    return null;
  }

  /**
   * {@inheritDoc}
   * 
   * This is an O(1) operation
   */
  @Override
  public boolean isEmpty()
  {
    return size == 0;
  }

  /**
   * {@inheritDoc}
   * 
   * This is an O(n) operation
   */
  @Override
  public V[] values()
  {
    val result = new ReifiedArrayList<V>(size, genericTypeParameterValue);
    for (val bucket : table)
        for (val item : bucket)
          result.add(item.value);

    return result.toArray();
  }

  /**
   * {@inheritDoc}
   * 
   * This is an O(n) operation
   */
  // TODO: optimize
  @Override
  public long[] keySet()
  {
    val result = new ReifiedArrayList<Long>(Long.class);
    
    for (val bucket : table)
        for (val item : bucket)
          result.add(item.key);

    return ArrayUtils.unbox(result.toArray());
  }

  /**
   * {@inheritDoc}
   * 
   * This is an O(1) operation
   */
  @Override
  public int size()
  {
    return size;
  }

  /**
   * {@inheritDoc}
   * 
   * This is an O(1) operation
   */
  public int capacity()
  {
    return table.length;
  }

  /**
   * Returns index for hash code h.
   */
  private int indexFor(long hash)
  {
    return hash64to32(hash) & (table.length - 1);
  }

  private static int hash64to32(long key)
  {
    key = (~key) + (key << 18);
    key = key ^ (key >>> 31);
    key = key * 21;
    key = key ^ (key >>> 11);
    key = key + (key << 6);
    key = key ^ (key >>> 22);
    return (int) key;
  }

  /**
   * Returns the index of a key within the given bucket, or -1 if that's not found
   */
  private int findInBucket(final SharedList<LongEntry<V>> bucket, final long key)
  {
    bucket.lock();
    try
    {
      for (int i = 0; i < bucket.size(); i++)
        if (bucket.get(i).key == key)
          return i;
    }
    finally
    {
      bucket.unlock();
    }

    return -1;
  }
}
