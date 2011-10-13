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
package propel.core.collections;

/**
 * Encapsulates a key/value pair.
 * 
 * @param <TKey> The key type.
 * @param <TValue> The value type.
 */
public final class KeyValuePair<TKey, TValue>
{
  protected TKey key;
  protected TValue value;

  /**
   * Initializes class with a key and a value.
   */
  public KeyValuePair(TKey key, TValue value)
  {
    this.key = key;
    this.value = value;
  }

  /**
   * @return Returns the key.
   */
  public TKey getKey()
  {
    return key;
  }

  /**
   * Sets the key
   */
  void setKey(TKey key)
  {
    this.key = key;
  }

  /**
   * @return Returns the key.
   */
  public TValue getValue()
  {
    return value;
  }

  /**
   * Sets the key
   */
  void setValue(TValue value)
  {
    this.value = value;
  }

  /**
   * Compares two key/value pairs for equality.
   * 
   * @param other The other key/value pair.
   * 
   * @return True if the key and values match.
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object other)
  {
    if (other == null)
      return false;

    if (other == this)
      return true;

    if (!(other instanceof KeyValuePair))
      return false;

    return equals((KeyValuePair<TKey, TValue>) other);
  }

  /**
   * Compares two key/value pairs for equality.
   * 
   * @param kvp The other key/value pair.
   * 
   * @return True if the key and values match.
   * 
   * @throws NullPointerException When the key/value pair is null.
   */
  public boolean equals(KeyValuePair<? extends TKey, ? extends TValue> kvp)
  {
    if (kvp == null)
      throw new NullPointerException("kvp");

    TKey otherKey = kvp.getKey();
    TValue otherValue = kvp.getValue();

    if (equal(key, otherKey))
      if (equal(value, otherValue))
        return true;

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return key.hashCode() + (value == null ? 0 : value.hashCode());
  }

  /**
   * {@inheritDoc}
   */
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append('[');

    if (key != null)
      sb.append(key.toString());
    sb.append(", ");
    if (value != null)
      sb.append(value.toString());

    sb.append(']');
    return sb.toString();
  }

  private boolean equal(Object a, Object b)
  {
    if (a == null && b == null)
      return true;
    if (a == null || b == null)
      return false;

    return a.equals(b);
  }

}
