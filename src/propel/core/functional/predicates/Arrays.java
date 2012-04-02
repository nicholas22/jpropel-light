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
package propel.core.functional.predicates;

import lombok.Predicate;
import propel.core.utils.Linq;

/**
 * Some common, re-usable predicates
 */
public final class Arrays
{

  /**
   * Predicate returning true when the function argument contains an item
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static <T> boolean contains(T[] element, T _item)
  {
    return Linq.contains(element, _item);
  }

  /**
   * Predicate returning true when the function argument contains all items
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static <T> boolean containsAll(T[] element, T[] _items)
  {
    return Linq.containsAll(element, _items);
  }

  /**
   * Predicate returning true when the function argument contains any of the given items
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static <T> boolean containsAny(T[] element, T[] _items)
  {
    return Linq.containsAny(element, _items);
  }

  /**
   * Predicate returning true when the function argument is equal to another array
   */
  @Predicate
  public static <T> boolean equal(T[] element, T[] _value)
  {
    if (element == null)
    {
      if (_value == null)
        return true;
      else
        return false;
    } else
    {
      if (_value == null)
        return false;
      else
        return java.util.Arrays.equals(element, _value);
    }
  }
  
  /**
   * Predicate returning true when the function argument is empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static <T> boolean isEmpty(T[] element)
  {
    return element.length == 0;
  }

  /**
   * Predicate returning true when the function argument is not empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static <T> boolean isNotEmpty(T[] element)
  {
    return element.length > 0;
  }
  
  /**
   * Predicate returning true when the length equals to specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static <T> boolean lengthEquals(T[] element, int _len)
  {
    return element.length == _len;
  }

  /**
   * Predicate returning true when the length is greater than a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static <T> boolean lengthGreaterThan(T[] element, int _len)
  {
    return element.length > _len;
  }

  /**
   * Predicate returning true when the length is greater than or equal to a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static <T> boolean lengthGreaterThanOrEqual(T[] element, int _len)
  {
    return element.length >= _len;
  }

  /**
   * Predicate returning true when the length is less than a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static <T> boolean lengthLessThan(T[] element, int _len)
  {
    return element.length < _len;
  }

  /**
   * Predicate returning true when the length is less than or equal to a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static <T> boolean lengthLessThanOrEqual(T[] element, int _len)
  {
    return element.length <= _len;
  }

  /**
   * Predicate returning true when the length is not equal to a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static <T> boolean lengthNotEqual(T[] element, int _len)
  {
    return element.length != _len;
  }

  /**
   * Predicate returning true when the function argument is not equal to another array
   */
  @Predicate
  public static <T> boolean notEqual(T[] element, T[] _value)
  {
    if (element == null)
    {
      if (_value == null)
        return false;
      else
        return true;
    } else
    {
      if (_value == null)
        return true;
      else
        return !java.util.Arrays.equals(element, _value);
    }
  }

  private Arrays()
  {
  }
}
