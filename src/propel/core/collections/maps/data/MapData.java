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
package propel.core.collections.maps.data;

import propel.core.collections.KeyValuePair;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.lists.ReifiedList;
import propel.core.utils.Linq;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;
import java.util.Iterator;

/**
 * Encapsulates Keys and Values, usually sourced from Dictionaries, hashtables and other map-type data structures. These values can be used
 * in initializing Dictionaries, hashtables and other map-type data structures. This would typically be done in serialization and
 * de-serialization of these types of more complex data structures.
 */
public class MapData<TKey, TValue>
    implements IMapData<TKey, TValue>
{
  private int count;
  private ReifiedList<TKey> keys;
  private ReifiedList<TValue> values;

  /**
   * Constructor initializes with array sizes.
   * 
   * @throws SuperTypeTokenException If not called using anonymous class semantics
   */
  public MapData(int size)
  {
    count = size;
    keys = new ReifiedArrayList<TKey>(size, SuperTypeToken.getClazz(this.getClass(), 0));
    values = new ReifiedArrayList<TValue>(size, SuperTypeToken.getClazz(this.getClass(), 1));
  }

  /**
   * Constructor initializes with array sizes.
   * 
   * @throws NullPointerException When a generic type parameter is null
   */
  public MapData(int size, Class<?> genericTypeParameterKey, Class<?> genericTypeParameterValue)
  {
    count = size;
    keys = new ReifiedArrayList<TKey>(size, genericTypeParameterKey);
    values = new ReifiedArrayList<TValue>(size, genericTypeParameterValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TKey[] getKeys()
  {
    return keys.toArray();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TValue[] getValues()
  {
    return values.toArray();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int size()
  {
    return count;
  }

  /**
   * Enumerates key/value pairs
   * 
   * @throws IllegalStateException If the keys and values collections are not of equal size
   */
  @Override
  public Iterator<KeyValuePair<TKey, TValue>> iterator()
  {
    if (keys.size() != values.size())
      throw new IllegalStateException("Cannot enumerate if the key count (" + keys.size() + ") is not equal to the value count ("
          + values.size() + ")");

    return new MapDataIterator<TKey, TValue>(keys, values);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameterKey()
  {
    return keys.getGenericTypeParameter();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameterValue()
  {
    return values.getGenericTypeParameter();
  }

  /**
   * Iterates over a MapData key/value pair collection
   */
  private class MapDataIterator<MDTKey, MDTValue>
      implements Iterator<KeyValuePair<MDTKey, MDTValue>>
  {
    private ReifiedList<MDTKey> keys;
    private ReifiedList<MDTValue> values;
    private int index;

    /**
     * Constructor initializes with the map key/value pairs
     */
    public MapDataIterator(ReifiedList<MDTKey> keys, ReifiedList<MDTValue> values)
    {
      index = 0;
      this.keys = keys;
      this.values = values;
    }

    @Override
    public boolean hasNext()
    {
      return index >= keys.size();
    }

    @Override
    public KeyValuePair<MDTKey, MDTValue> next()
    {
      KeyValuePair<MDTKey, MDTValue> kvp = new KeyValuePair<MDTKey, MDTValue>(keys.get(index), values.get(index));
      index++;

      return kvp;
    }

    /**
     * @throws UnsupportedOperationException The remove operation is not supported.
     */
    @Override
    @Deprecated
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
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
