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

import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.functional.Predicates.Predicate1;
import propel.core.utils.Linq;

/**
 * Some common, re-usable predicates for Arrays
 */
public final class Arrays
{
  /**
   * Predicate returning true when the function argument contains an item
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> contains(@NotNull final T[] _values)
  {
    return new Predicate1<T>() {
      public boolean evaluate(final T element)
      {
        return Linq.contains(_values, element);
      }
    };
  }

  /**
   * Predicate returning true when the function argument contains all items
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T[]> containsAll(@NotNull final T[] _values)
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
      {
        return Linq.containsAll(_values, element);
      }
    };
  }

  /**
   * Predicate returning true when the function argument contains any of the given items
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T[]> containsAny(@NotNull final T[] _values)
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
      {
        return Linq.containsAny(_values, element);
      }
    };
  }

  /**
   * Predicate returning true when the function argument is deeply equal to another array
   */
  @Validate
  public static <T> Predicate1<T[]> deepEqual(@NotNull final T[] _values)
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
      {
        if (element == null)
        {
          if (_values == null)
            return true;
          else
            return false;
        } else
        {
          if (_values == null)
            return false;
          else
            return java.util.Arrays.deepEquals(element, _values);
        }
      }
    };
  }

  /**
   * Predicate returning true when the function argument is equal to another array
   */
  @Validate
  public static <T> Predicate1<T[]> equal(@NotNull final T[] _value)
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
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
    };
  }

  /**
   * Predicate returning true when the function argument is empty
   * 
   * @throws NullPointerException When an argument is null
   */
  public static <T> Predicate1<T[]> isEmpty()
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
      {
        return element.length == 0;
      }
    };
  }

  /**
   * Predicate returning true when the function argument is not empty
   * 
   * @throws NullPointerException When an argument is null
   */
  public static <T> Predicate1<T[]> isNotEmpty()
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
      {
        return element.length > 0;
      }
    };
  }

  /**
   * Predicate returning true when the length equals to specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  public static <T> Predicate1<T[]> lengthEquals(@NotNull final int _len)
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
      {
        return element.length == _len;
      }
    };
  }

  /**
   * Predicate returning true when the length is greater than a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  public static <T> Predicate1<T[]> lengthGreaterThan(@NotNull final int _len)
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
      {
        return element.length > _len;
      }
    };
  }

  /**
   * Predicate returning true when the length is greater than or equal to a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  public static <T> Predicate1<T[]> lengthGreaterThanOrEqual(@NotNull final int _len)
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
      {
        return element.length >= _len;
      }
    };
  }

  /**
   * Predicate returning true when the length is less than a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  public static <T> Predicate1<T[]> lengthLessThan(@NotNull final int _len)
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
      {
        return element.length < _len;
      }
    };
  }

  /**
   * Predicate returning true when the length is less than or equal to a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  public static <T> Predicate1<T[]> lengthLessThanOrEqual(@NotNull final int _len)
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
      {
        return element.length <= _len;
      }
    };
  }

  /**
   * Predicate returning true when the length is not equal to a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  public static <T> Predicate1<T[]> lengthNotEqual(@NotNull final int _len)
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
      {
        return element.length != _len;
      }
    };
  }

  /**
   * Predicate returning true when the function argument is not equal to another array
   */
  @Validate
  public static <T> Predicate1<T[]> notEqual(@NotNull final T[] _value)
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
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
    };
  }

  /**
   * Predicate returning true when the function argument does not contain an item
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T[]> notContains(@NotNull final T _item)
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
      {
        return !Linq.contains(element, _item);
      }
    };
  }

  /**
   * Predicate returning true when the function argument does not contain all items
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T[]> notContainsAll(@NotNull final T[] _items)
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
      {
        return !Linq.containsAll(element, _items);
      }
    };
  }

  /**
   * Predicate returning true when the function argument does not contain any of the given items
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T[]> notContainsAny(@NotNull final T[] _items)
  {
    return new Predicate1<T[]>() {
      public boolean evaluate(final T[] element)
      {
        return !Linq.containsAny(element, _items);
      }
    };
  }

  private Arrays()
  {
  }
}
