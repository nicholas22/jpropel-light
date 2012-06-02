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
import java.util.NoSuchElementException;

/**
 * Some common, re-usable predicates for Iterables
 */
public final class Iterables
{
  /**
   * Predicate returning true when the function argument contains an item
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean contains(final Iterable element, final Object _item)
  {
    return Linq.contains(element, _item);
  }

  /**
   * Predicate returning true when the function argument contains all items
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean containsAll(final Iterable element, final Iterable _items)
  {
    return Linq.containsAll(element, _items);
  }

  /**
   * Predicate returning true when the function argument contains any of the given items
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean containsAny(final Iterable element, final Iterable _items)
  {
    return Linq.containsAny(element, _items);
  }

  /**
   * Predicate returning true when the function argument is equal to another array
   */
  @Predicate
  public static boolean equal(final Iterable element, final Iterable _value)
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
        return Linq.sequenceEqual(element, _value);
    }
  }

  /**
   * Predicate returning true when the function argument is empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isEmpty(final Iterable element)
  {
    try
    {
      Linq.first(element);
      return false;
    }
    catch(NoSuchElementException e)
    {
      return true;
    }
  }

  /**
   * Predicate returning true when the function argument is not empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isNotEmpty(final Iterable element)
  {
    try
    {
      Linq.first(element);
      return false;
    }
    catch(NoSuchElementException e)
    {
      return true;
    }
  }

  /**
   * Predicate returning true when the length equals to specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthEquals(final Iterable element, final int _len)
  {
    return _len >= 0 && Linq.count(element) == _len;
  }

  /**
   * Predicate returning true when the length is greater than a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthGreaterThan(final Iterable element, final int _len)
  {
    return _len >= 0 && Linq.count(element) > _len;
  }

  /**
   * Predicate returning true when the length is greater than or equal to a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthGreaterThanOrEqual(final Iterable element, final int _len)
  {
    return _len >= 0 && Linq.count(element) >= _len;
  }

  /**
   * Predicate returning true when the length is less than a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthLessThan(final Iterable element, final int _len)
  {
    return _len >= 0 && Linq.count(element) < _len;
  }

  /**
   * Predicate returning true when the length is less than or equal to a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthLessThanOrEqual(final Iterable element, final int _len)
  {
    return _len >= 0 && Linq.count(element) <= _len;
  }

  /**
   * Predicate returning true when the length is not equal to a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthNotEqual(final Iterable element, final int _len)
  {
    return _len >= 0 && Linq.count(element) != _len;
  }

  /**
   * Predicate returning true when the function argument is not equal to another array
   */
  @Predicate
  public static boolean notEqual(final Iterable element, final Iterable _value)
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
        return !Linq.sequenceEqual(element, _value);
    }
  }

  /**
   * Predicate returning true when the function argument does not contain an item
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notContains(final Iterable element, final Object _item)
  {
    return !Linq.contains(element, _item);
  }

  /**
   * Predicate returning true when the function argument does not contain all items
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notContainsAll(final Iterable element, final Iterable _items)
  {
    return !Linq.containsAll(element, _items);
  }

  /**
   * Predicate returning true when the function argument does not contain any of the given items
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notContainsAny(final Iterable element, final Iterable _items)
  {
    return !Linq.containsAny(element, _items);
  }

  private Iterables()
  {
  }
}
