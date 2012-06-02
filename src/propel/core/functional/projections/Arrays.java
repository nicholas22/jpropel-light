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
package propel.core.functional.projections;

import java.util.List;
import lombok.Function;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.lists.ReifiedList;
import propel.core.utils.ArrayUtils;

/**
 * Some common, re-usable projections for arrays
 */
public final class Arrays
{
  /**
   * Returns the array as a list
   * 
   * @throws NullPointerException An argument is null
   * @throws ArrayIndexOutOfBoundsException The index is out of bounds
   */
  @Function
  public static <T> List<T> asList(final T[] array)
  {
    return java.util.Arrays.asList(array);
  }

  /**
   * Returns the array as a reified list
   * 
   * @throws NullPointerException An argument is null
   * @throws ArrayIndexOutOfBoundsException The index is out of bounds
   */
  @Function
  public static <T> ReifiedList<T> asReifiedList(final T[] array)
  {
    return new ReifiedArrayList(array);
  }

  /**
   * Returns the element at specified index
   * 
   * @throws NullPointerException An argument is null
   * @throws ArrayIndexOutOfBoundsException The index is out of bounds
   */
  @Function
  public static <T> T getElement(final T[] array, final int _index)
  {
    return array[_index];
  }

  /**
   * Returns the element at specified index, but if this is not possible, it returns null
   * 
   * @throws NullPointerException An argument is null
   * @throws ArrayIndexOutOfBoundsException The index is out of bounds
   */
  @Function
  public static <T> T getElementNulling(final T[] array, final int _index)
  {
    try
    {
      return array[_index];
    }
    catch(Exception e)
    {
      return null;
    }
  }

  /**
   * Returns the element at specified index, but if this is not possible, it returns the specified default value
   */
  @Function
  public static <T> T getElementSafe(final T[] array, final int _index, final T _defaultValue)
  {
    try
    {
      return array[_index];
    }
    catch(Exception e)
    {
      return _defaultValue;
    }
  }

  /**
   * Returns the length of the array
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static <T> int length(final T[] array)
  {
    return array.length;
  }

  /**
   * Returns a portion of the array
   * 
   * @throws NullPointerException An argument is null
   * @throws ArrayIndexOutOfBoundsException An argument is null
   */
  @Function
  public static <T> T[] subArray(final T[] array, final int _startIndex, final int _endIndex)
  {
    return ArrayUtils.subArray(array, _startIndex, _endIndex);
  }

  /**
   * Returns the string representation of the array
   * 
   * @throws NullPointerException An argument is null
   * @throws ArrayIndexOutOfBoundsException The index is out of bounds
   */
  @Function
  public static <T> String toStringify(final T[] array)
  {
    return java.util.Arrays.toString(array);
  }

  /**
   * Returns the deep string representation of the array
   * 
   * @throws NullPointerException An argument is null
   * @throws ArrayIndexOutOfBoundsException The index is out of bounds
   */
  @Function
  public static <T> String toStringifyDeep(final T[] array)
  {
    return java.util.Arrays.deepToString(array);
  }

  private Arrays()
  {
  }
}
