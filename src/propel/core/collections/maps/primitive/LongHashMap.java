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

import lombok.Validate;
import lombok.Validate.NotNull;
import lombok.val;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.utils.ArrayUtils;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;

/**
 * Implementation of a long -> V hash map with an emphasis on lookup speed and reduction of memory footprint.
 */
public class LongHashMap<V>
    implements ILongHashMap<V>
{
  /**
   * The maximum capacity, used if a higher value is implicitly specified by either of the constructors with arguments
   */
  private static final int MAXIMUM_CAPACITY = 1 << 30;
  private final Class<?> genericTypeParameterValue;
  private BucketEntry[] table;
  private int size;

  /**
   * Constructs an empty map with the specified max capacity
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException An argument is invalid
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public LongHashMap(final int maxCapacity)
  {
    this.genericTypeParameterValue = SuperTypeToken.getClazz(getClass());
    init(maxCapacity);
  }

  /**
   * Constructs an empty map with the specified max capacity.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException An argument is invalid
   */
  @Validate
  public LongHashMap(final int maxCapacity, @NotNull final Class<?> genericTypeParameterValue)
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

    this.table = ArrayUtils.create(BucketEntry.class, capacity);
  }

  /**
   * Clears the contents. This is an O(n) operation
   */
  public void clear()
  {
    // clear all bucket entries
    for (int i = 0; i < table.length; i++)
      table[i] = null;

    size = 0;
  }

  /**
   * {@inheritDoc}
   * 
   * This is an O(1) operation
   */
  @Override
  public V put(final long key, final V value)
  {
    // find bucket entry
    int index = indexFor(key);
    if (table[index] == null)
      table[index] = new BucketEntry();

    // the new long->V entry
    val inserted = new LongEntry<V>(key, value);

    // attempt to find old long->V entry, if existent, to replace it
    BucketEntry bucket = table[index];
    int existingIndex = bucket.find(key);

    // if existent, replace
    if (existingIndex >= 0)
    {
      // this does not increase the hash map size
      val oldItem = bucket.set(existingIndex, inserted);
      return oldItem.value;
    }

    // otherwise insert new, this increases the size
    bucket.add(inserted);
    size++;
    return null;
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
    if (table[index] != null)
      return table[index].find(key) >= 0;

    return false;
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
    for (int i = 0; i < table.length; i++)
      if (table[i] != null)
        for (int j = 0; j < table[i].position; j++)
        {
          val item = table[i].entries[j];
          if (item.value != null)
            if (item.value.equals(value))
              return item;
        }

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
    for (int i = 0; i < table.length; i++)
      if (table[i] != null)
        result.addAll(table[i].entries);

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
    BucketEntry bucket = table[indexFor(key)];
    if (bucket != null)
      for (int i = 0; i < bucket.position; i++)
      {
        val entry = bucket.entries[i];
        if (entry.key == key)
          return entry.value;
      }

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
    for (int i = 0; i < table.length; i++)
      if (table[i] != null)
        for (int j = 0; j < table[i].position; j++)
        {
          val item = table[i].entries[j];
          if (item != null)
            result.add(item.value);
        }

    return result.toArray();
  }

  /**
   * {@inheritDoc}
   * 
   * This is an O(n) operation
   */
  @Override
  public long[] keySet()
  {
    int index = 0;
    long[] result = new long[size];
    for (int i = 0; i < table.length; i++)
      if (table[i] != null)
        for (int j = 0; j < table[i].position; j++)
        {
          val item = table[i].entries[j];
          result[index++] = item.key;
        }

    return result;
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
   * Resizes the hash map.
   * 
   * This is an O(n) operation, note that it more than doubles the space used for a short while
   */
  public void resize(int capacity)
  {
    BucketEntry[] tableCopy = ArrayUtils.clone(table);
    init(capacity);
    for (BucketEntry bucket : tableCopy)
      if (bucket != null)
        for (val item : bucket.entries)
          put(item.key, item.value);
  }

  /**
   * Returns index for hash code h.
   */
  private int indexFor(long key)
  {
    return ((int) key / 2) & (table.length - 1);
  }

  /**
   * The hash map contains multiple bucket entry classes such as this
   */
  final class BucketEntry
  {
    long[] keys;
    LongEntry<V>[] entries;
    int position;

    BucketEntry()
    {
      keys = new long[8];
      entries = ArrayUtils.create(LongEntry.class, 8);
      position = 0;
    }

    void add(LongEntry<V> entry)
    {
      keys[position] = entry.key;
      entries[position] = entry;
      position++;

      // ensure capacity
      if (position >= entries.length)
      {
        val newKeys = new long[(int) (keys.length * 0.75f)];
        System.arraycopy(keys, 0, newKeys, 0, entries.length);
        keys = newKeys;
        entries = ArrayUtils.resize(entries, entries.length * 2);
      }
    }

    int find(long key)
    {
      for (int i = 0; i < position; i++)
        if (keys[i] == key)
          return i;

      return -1;
    }

    LongEntry<V> set(int index, LongEntry<V> entry)
    {
      keys[index] = entry.key;
      val old = entries[index];
      entries[index] = entry;
      return old;
    }
  }
  
}
