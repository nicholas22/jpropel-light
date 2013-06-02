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
import propel.core.utils.ReflectionUtils;
import propel.core.utils.StringComparison;
import propel.core.utils.StringUtils;

/**
 * Some common, re-usable predicates for objects
 */
public final class Objects
{
  /**
   * Predicate returning true when the function argument's toString() contains some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> contains(@NotNull final String _part)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return StringUtils.contains(element.toString(), _part, StringComparison.Ordinal);
      }
    };
  }

  /**
   * Predicate returning true when the function argument's toString() contains some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> contains(@NotNull final String _part, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return StringUtils.contains(element.toString(), _part, _comparison);
      }
    };
  }

  /**
   * Predicate returning true if an element is contained in the function argument (Array)
   */
  @Validate
  public static <T> Predicate1<T> containedIn(@NotNull final T[] _elements)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return Linq.contains(_elements, element);
      }
    };
  }

  /**
   * Predicate returning true if an element is contained in the function argument (Iterable)
   */
  @Validate
  @SuppressWarnings("rawtypes")
  public static <T> Predicate1<T> containedBy(@NotNull final Iterable _elements)
  {
    return new Predicate1<T>() {
      @Override
      @SuppressWarnings("unchecked")
      public boolean evaluate(final T element)
      {
        return Linq.contains(_elements, element);
      }
    };
  }

  /**
   * Predicate returning true when the function argument's toString() ends with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> endsWith(@NotNull final String _suffix)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return StringUtils.endsWith(element.toString(), _suffix, StringComparison.Ordinal);
      }
    };
  }

  /**
   * Predicate returning true when the function argument's toString() ends with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> endsWith(@NotNull final String _suffix, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return StringUtils.endsWith(element.toString(), _suffix, _comparison);
      }
    };
  }

  /**
   * Predicate returning true when the function argument is equal to a value (nulls are allowed)
   */
  public static <T> Predicate1<T> equal(final Object _value)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
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
            return element.equals(_value);
        }
      }
    };
  }

  /**
   * Predicate returning true when the function argument is greater than a value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T extends Comparable<T>> Predicate1<T> greaterThan(@NotNull final T _value)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return element.compareTo(_value) > 0;
      }
    };
  }

  /**
   * Predicate returning true when the function argument is greater than or equal to a value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T extends Comparable<T>> Predicate1<T> greaterThanOrEqual(@NotNull final T _value)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return element.compareTo(_value) >= 0;
      }
    };
  }

  /**
   * Predicate returning true when the function argument's toString() is equal to some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> isEqual(@NotNull final String _other)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return StringUtils.equal(element.toString(), _other, StringComparison.Ordinal);
      }
    };
  }

  /**
   * Predicate returning true when the function argument's toString() is equal to some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> isEqual(@NotNull final String _other, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return StringUtils.equal(element.toString(), _other, _comparison);
      }
    };
  }

  /**
   * Predicate that returns true if the function argument is a subclass of the class specified
   * 
   * @throws NullPointerException When an argument is null
   * @throws IllegalArgumentException When a non-class (e.g. interface) was provided
   */
  @Validate
  public static <T> Predicate1<T> isExtending(final Class<?> _class)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return ReflectionUtils.isExtending(element.getClass(), _class);
      }
    };
  }

  /**
   * Predicate that returns true if the function argument is an instance of the interface specified
   * 
   * @throws NullPointerException When an argument is null
   * @throws IllegalArgumentException When a non-interface (e.g. class) was provided
   */
  @Validate
  public static <T> Predicate1<T> isImplementing(@NotNull final Class<?> _class)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return ReflectionUtils.isImplementing(element.getClass(), _class);
      }
    };
  }

  /**
   * Predicate that returns true if the function argument is not a subclass of the class specified
   * 
   * @throws NullPointerException When an argument is null
   * @throws IllegalArgumentException When a non-class (e.g. interface) was provided
   */
  @Validate
  public static <T> Predicate1<T> isNotExtending(@NotNull final Class<?> _class)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return !ReflectionUtils.isExtending(element.getClass(), _class);
      }
    };
  }

  /**
   * Predicate that returns true if the function argument is not an instance of the interface specified
   * 
   * @throws NullPointerException When an argument is null
   * @throws IllegalArgumentException When a non-interface (e.g. class) was provided
   */
  @Validate
  public static <T> Predicate1<T> isNotImplementing(@NotNull final Class<?> _class)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return !ReflectionUtils.isImplementing(element.getClass(), _class);
      }
    };
  }

  /**
   * Predicate that returns true if the function argument is an instance of the class specified
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> instanceOf(@NotNull final Class<?> _class)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return ReflectionUtils.instanceOf(element.getClass(), _class);
      }
    };
  }

  /**
   * Predicate returning true when the function argument is null
   */
  @SuppressWarnings("unchecked")
  public static <T> Predicate1<T> isNull()
  {
    return IS_NULL;
  }
  @SuppressWarnings("rawtypes")
  private static final Predicate1 IS_NULL = new Predicate1() {
    @Override
    public boolean evaluate(final Object element)
    {
      return element == null;
    }
  };

  /**
   * Predicate returning true when the function argument is not null
   */
  @SuppressWarnings("unchecked")
  public static <T> Predicate1<T> isNotNull()
  {
    return IS_NOT_NULL;
  }
  @SuppressWarnings("rawtypes")
  private static final Predicate1 IS_NOT_NULL = new Predicate1() {
    @Override
    public boolean evaluate(final Object element)
    {
      return element == null;
    }
  };

  /**
   * Predicate returning true always, can be used to print out all elements in an all() Linq statement
   * 
   * @throws NullPointerException When an argument is null
   */
  @SuppressWarnings("unchecked")
  public static <T> Predicate1<T> println()
  {
    return PRINTLN;
  }
  @SuppressWarnings("rawtypes")
  private static final Predicate1 PRINTLN = new Predicate1() {
    @Override
    public boolean evaluate(final Object element)
    {
      System.out.println(element);
      return true;
    }
  };

  /**
   * Predicate returning true always, can be used to print out all elements in an all() Linq statement
   * 
   * @throws NullPointerException When an argument is null
   */
  @SuppressWarnings("unchecked")
  public static <T> Predicate1<T> print()
  {
    return PRINT;
  }
  @SuppressWarnings("rawtypes")
  private static final Predicate1 PRINT =new Predicate1() {
    @Override
    public boolean evaluate(final Object element)
    {
      System.out.print(element);
      return true;
    }
  };

  /**
   * Predicate returning true when the function argument is less than a value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T extends Comparable<T>> Predicate1<T> lessThan(@NotNull final T _value)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return element.compareTo(_value) < 0;
      }
    };
  }

  /**
   * Predicate returning true when the function argument is less than or equal to a value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T extends Comparable<T>> Predicate1<T> lessThanOrEqual(@NotNull final T _value)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return element.compareTo(_value) <= 0;
      }
    };
  }

  /**
   * Predicate returning true when the function argument's toString() does not contain some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> notContains(@NotNull final String _part)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return !StringUtils.contains(element.toString(), _part, StringComparison.Ordinal);
      }
    };
  }

  /**
   * Predicate returning true when the function argument's toString() does not contain some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> notContains(@NotNull final String _part, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return !StringUtils.contains(element.toString(), _part, _comparison);
      }
    };
  }

  /**
   * Predicate returning true if an element is not contained in the function argument (Array)
   */
  @Validate
  public static <T> Predicate1<T> notContainedIn(@NotNull final T[] _elements)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return !Linq.contains(_elements, element);
      }
    };
  }

  /**
   * Predicate returning true if an element is not contained in the function argument (Iterable)
   */
  @Validate
  public static <T> Predicate1<T> notContainedBy(@NotNull final Iterable _elements)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return !Linq.contains(_elements, element);
      }
    };
  }

  /**
   * Predicate returning true when the function argument's toString() does not end with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> notEndsWith(@NotNull final String _suffix)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return !StringUtils.endsWith(element.toString(), _suffix, StringComparison.Ordinal);
      }
    };
  }

  /**
   * Predicate returning true when the function argument's toString() does not end with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> notEndsWith(@NotNull final String _suffix, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return !StringUtils.endsWith(element.toString(), _suffix, _comparison);
      }
    };
  }

  /**
   * Predicate returning true when the function argument is not equal to a value
   */
  public static <T> Predicate1<T> notEqual(final Object _value)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
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
            return !element.equals(_value);
        }
      }
    };
  }

  /**
   * Predicate that returns true if the function argument is not an instance of the class specified
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> notInstanceOf(@NotNull final Class<?> _class)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return !ReflectionUtils.instanceOf(element.getClass(), _class);
      }
    };
  }

  /**
   * Predicate returning true when the function argument's toString() does not start with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> notStartsWith(@NotNull final String _prefix)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return !StringUtils.startsWith(element.toString(), _prefix, StringComparison.Ordinal);
      }
    };
  }

  /**
   * Predicate returning true when the function argument's toString() does not start with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> notStartsWith(@NotNull final String _prefix, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return !StringUtils.startsWith(element.toString(), _prefix, _comparison);
      }
    };
  }

  /**
   * Predicate returning true when the function argument's toString() starts with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> startsWith(@NotNull final String _prefix)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return StringUtils.startsWith(element.toString(), _prefix, StringComparison.Ordinal);
      }
    };
  }

  /**
   * Predicate returning true when the function argument's toString() starts with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Predicate1<T> startsWith(@NotNull final String _prefix, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<T>() {
      @Override
      public boolean evaluate(final T element)
      {
        return StringUtils.startsWith(element.toString(), _prefix, _comparison);
      }
    };
  }

  private Objects()
  {
  }
}
