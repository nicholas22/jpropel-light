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

import java.util.NoSuchElementException;
import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.functional.Predicates.Predicate1;
import propel.core.utils.Linq;

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
  @Validate
  public static Predicate1<Iterable> contains(@NotNull final Object _item)
  {
    return new Predicate1<Iterable>() {
      @Override
      public boolean evaluate(final Iterable element)
      {
        return Linq.contains(element, _item);
      }
    };
  }

  /**
   * Predicate returning true when the function argument contains all items
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<Iterable> containsAll(@NotNull final Iterable _items)
  {
    return new Predicate1<Iterable>() {
      @Override
      public boolean evaluate(final Iterable element)
      {
        return Linq.containsAll(element, _items);
      }
    };
  }

  /**
   * Predicate returning true when the function argument contains any of the given items
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<Iterable> containsAny(@NotNull final Iterable _items)
  {
    return new Predicate1<Iterable>() {
      @Override
      public boolean evaluate(final Iterable element)
      {
        return Linq.containsAny(element, _items);
      }
    };
  }

  /**
   * Predicate returning true when the function argument is equal to another array
   */
  @Validate
  public static Predicate1<Iterable> equal(@NotNull final Iterable _value)
  {
    return new Predicate1<Iterable>() {
      @Override
      public boolean evaluate(final Iterable element)
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
    };
  }

  /**
   * Predicate returning true when the function argument is empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @SuppressWarnings("rawtypes")
  public static Predicate1<Iterable> isEmpty()
  {
    return IS_EMPTY;
  }
  @SuppressWarnings("rawtypes")
  private static final Predicate1<Iterable> IS_EMPTY = new Predicate1<Iterable>() {
    @Override
    @SuppressWarnings("unchecked")
    public boolean evaluate(final Iterable element)
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
  };

  /**
   * Predicate returning true when the function argument is not empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @SuppressWarnings("rawtypes")
  public static Predicate1<Iterable> isNotEmpty()
  {
    return IS_NOT_EMPTY;
  }
  @SuppressWarnings("rawtypes")
  private static final Predicate1<Iterable> IS_NOT_EMPTY = new Predicate1<Iterable>() {
    @Override
    @SuppressWarnings("unchecked")
    public boolean evaluate(final Iterable element)
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
  };

  /**
   * Predicate returning true when the length equals to specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @SuppressWarnings("rawtypes")
  public static Predicate1<Iterable> lengthEquals(final int _len)
  {
    return new Predicate1<Iterable>() {
      @Override
      @SuppressWarnings("unchecked")
      public boolean evaluate(final Iterable element)
      {
        return _len >= 0 && Linq.count(element) == _len;
      }
    };
  }

  /**
   * Predicate returning true when the length is greater than a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @SuppressWarnings("rawtypes")
  public static Predicate1<Iterable> lengthGreaterThan(final int _len)
  {
    return new Predicate1<Iterable>() {
      @Override
      @SuppressWarnings("unchecked")
      public boolean evaluate(final Iterable element)
      {
        return _len >= 0 && Linq.count(element) > _len;
      }
    };
  }

  /**
   * Predicate returning true when the length is greater than or equal to a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @SuppressWarnings("rawtypes")
  public static Predicate1<Iterable> lengthGreaterThanOrEqual(final int _len)
  {
    return new Predicate1<Iterable>() {
      @Override
      @SuppressWarnings("unchecked")
      public boolean evaluate(final Iterable element)
      {
        return _len >= 0 && Linq.count(element) >= _len;
      }
    };
  }

  /**
   * Predicate returning true when the length is less than a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @SuppressWarnings("rawtypes")
  public static Predicate1<Iterable> lengthLessThan(final int _len)
  {
    return new Predicate1<Iterable>() {
      @Override
      @SuppressWarnings("unchecked")
      public boolean evaluate(final Iterable element)
      {
        return _len >= 0 && Linq.count(element) < _len;
      }
    };
  }

  /**
   * Predicate returning true when the length is less than or equal to a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @SuppressWarnings("rawtypes")
  public static Predicate1<Iterable> lengthLessThanOrEqual(final int _len)
  {
    return new Predicate1<Iterable>() {
      @Override
      @SuppressWarnings("unchecked")
      public boolean evaluate(final Iterable element)
      {
        return _len >= 0 && Linq.count(element) <= _len;
      }
    };
  }

  /**
   * Predicate returning true when the length is not equal to a specified value
   * 
   * @throws NullPointerException When an argument is null
   */
  @SuppressWarnings("rawtypes")
  public static Predicate1<Iterable> lengthNotEqual(final int _len)
  {
    return new Predicate1<Iterable>() {
      @Override
      @SuppressWarnings("unchecked")
      public boolean evaluate(final Iterable element)
      {
        return _len >= 0 && Linq.count(element) != _len;
      }
    };
  }

  /**
   * Predicate returning true when the function argument is not equal to another array
   */
  @Validate
  public static Predicate1<Iterable> notEqual(@NotNull final Iterable _value)
  {
    return new Predicate1<Iterable>() {
      @Override
      public boolean evaluate(final Iterable element)
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
    };
  }

  /**
   * Predicate returning true when the function argument does not contain an item
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<Iterable> notContains(@NotNull final Object _item)
  {
    return new Predicate1<Iterable>() {
      @Override
      public boolean evaluate(final Iterable element)
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
  public static Predicate1<Iterable> notContainsAll(@NotNull final Iterable _items)
  {
    return new Predicate1<Iterable>() {
      @Override
      public boolean evaluate(final Iterable element)
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
  public static Predicate1<Iterable> notContainsAny(@NotNull final Iterable _items)
  {
    return new Predicate1<Iterable>() {
      @Override
      public boolean evaluate(final Iterable element)
      {
        return !Linq.containsAny(element, _items);
      }
    };
  }

  private Iterables()
  {
  }
}
