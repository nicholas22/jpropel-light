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
import propel.core.functional.Functions.Function1;
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
  public static <T> Function1<T[], List<T>> asList()
  {
    return new Function1<T[], List<T>>() {
      @Override
      public List<T> apply(final T[] element)
      {
        return java.util.Arrays.asList(element);
      }
    };
  }

  /**
   * Returns the array as a reified list
   * 
   * @throws NullPointerException An argument is null
   * @throws ArrayIndexOutOfBoundsException The index is out of bounds
   */
  public static <T> Function1<T[], ReifiedList<T>> asReifiedList()
  {
    return new Function1<T[], ReifiedList<T>>() {
      @Override
      public ReifiedList<T> apply(final T[] element)
      {
        return new ReifiedArrayList<T>(element);
      }
    };
  }

  /**
   * Returns the element at specified index
   * 
   * @throws NullPointerException An argument is null
   * @throws ArrayIndexOutOfBoundsException The index is out of bounds
   */
  public static <T> Function1<T[], T> getElement(final int _index)
  {
    return new Function1<T[], T>() {
      @Override
      public T apply(final T[] element)
      {
        return element[_index];
      }
    };
  }

  /**
   * Returns the element at specified index, but if this is not possible, it returns null
   * 
   * @throws NullPointerException An argument is null
   * @throws ArrayIndexOutOfBoundsException The index is out of bounds
   */
  public static <T> Function1<T[], T> getElementNulling(final int _index)
  {
    return new Function1<T[], T>() {
      @Override
      public T apply(final T[] element)
      {
        try
        {
          return element[_index];
        }
        catch(Exception e)
        {
          return null;
        }
      }
    };
  }

  /**
   * Returns the element at specified index, but if this is not possible, it returns the specified default value
   */
  public static <T> Function1<T[], T> getElementSafe(final int _index, final T _defaultValue)
  {
    return new Function1<T[], T>() {
      @Override
      public T apply(final T[] element)
      {
        try
        {
          return element[_index];
        }
        catch(Exception e)
        {
          return _defaultValue;
        }
      }
    };
  }

  /**
   * Returns the length of the array
   * 
   * @throws NullPointerException An argument is null
   */
  public static <T> Function1<T[], Integer> length()
  {
    return new Function1<T[], Integer>() {
      @Override
      public Integer apply(final T[] element)
      {
        return element.length;
      }
    };
  }

  /**
   * Returns a portion of the array
   * 
   * @throws NullPointerException An argument is null
   * @throws ArrayIndexOutOfBoundsException An argument is null
   */
  public static <T> Function1<T[], T[]> subArray(final int _startIndex, final int _endIndex)
  {
    return new Function1<T[], T[]>() {
      @Override
      public T[] apply(final T[] element)
      {
        return ArrayUtils.subArray(element, _startIndex, _endIndex);
      }
    };
  }

  /**
   * Returns the string representation of the array
   * 
   * @throws NullPointerException An argument is null
   * @throws ArrayIndexOutOfBoundsException The index is out of bounds
   */
  public static <T> Function1<T[], String> toStringify()
  {
    return new Function1<T[], String>() {
      @Override
      public String apply(final T[] element)
      {
        return java.util.Arrays.toString(element);
      }
    };
  }

  /**
   * Returns the deep string representation of the array
   * 
   * @throws NullPointerException An argument is null
   * @throws ArrayIndexOutOfBoundsException The index is out of bounds
   */
  public static <T> Function1<T[], String> toStringifyDeep()
  {
    return new Function1<T[], String>() {
      @Override
      public String apply(final T[] element)
      {
        return java.util.Arrays.deepToString(element);
      }
    };
  }

  private Arrays()
  {
  }
}
