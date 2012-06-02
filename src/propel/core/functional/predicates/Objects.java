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

import propel.core.utils.Linq;
import propel.core.utils.StringUtils;
import propel.core.utils.StringComparison;
import propel.core.utils.ReflectionUtils;
import lombok.Predicate;

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
  @Predicate
  public static boolean contains(final Object element, final String _part)
  {
    return StringUtils.contains(element.toString(), _part, StringComparison.Ordinal);
  }
  
  /**
   * Predicate returning true when the function argument's toString() contains some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean contains(final Object element, final String _part, final StringComparison _comparison)
  {
    return StringUtils.contains(element.toString(), _part, _comparison);
  }

  /**
   * Predicate returning true if an element is contained in the function argument (Array)
   */
  @Predicate
  public static <T> boolean containedIn(final T element, final T[] _elements)
  {
    return Linq.contains(_elements, element);
  }
  
  /**
   * Predicate returning true if an element is contained in the function argument (Iterable)
   */
  @Predicate
  public static boolean containedBy(final Object element, final Iterable _elements)
  {
    return Linq.contains(_elements, element);
  }
  
  /**
   * Predicate returning true when the function argument's toString() ends with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean endsWith(final Object element, final String _suffix)
  {
    return StringUtils.endsWith(element.toString(), _suffix, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument's toString() ends with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean endsWith(final Object element, final String _suffix, final StringComparison _comparison)
  {
    return StringUtils.endsWith(element.toString(), _suffix, _comparison);
  }

  /**
   * Predicate returning true when the function argument is equal to a value
   */
  @Predicate
  public static boolean equal(final Object element, final Object _value)
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

  /**
   * Predicate returning true when the function argument is greater than a value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static <T extends Comparable<T>> boolean greaterThan(final T element, final T _value)
  {
    return element.compareTo(_value) > 0;
  }

  /**
   * Predicate returning true when the function argument is greater than or equal to a value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static <T extends Comparable<T>> boolean greaterThanOrEqual(final T element, final T _value)
  {
    return element.compareTo(_value) >= 0;
  }

  /**
   * Predicate returning true when the function argument's toString() is equal to some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isEqual(final Object element, final String _other)
  {
    return StringUtils.equal(element.toString(), _other, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument's toString() is equal to some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isEqual(final Object element, final String _other, final StringComparison _comparison)
  {
    return StringUtils.equal(element.toString(), _other, _comparison);
  }

  /**
   * Predicate that returns true if the function argument is a subclass of the class specified
   * 
   * @throws NullPointerException When an argument is null
   * @throws IllegalArgumentException When a non-class (e.g. interface) was provided
   */
  @Predicate
  public static boolean isExtending(final Object obj, final Class<?> _class)
  {
    return ReflectionUtils.isExtending(obj.getClass(), _class);
  }

  /**
   * Predicate that returns true if the function argument is an instance of the interface specified
   * 
   * @throws NullPointerException When an argument is null
   * @throws IllegalArgumentException When a non-interface (e.g. class) was provided
   */
  @Predicate
  public static boolean isImplementing(final Object obj, final Class<?> _class)
  {
    return ReflectionUtils.isImplementing(obj.getClass(), _class);
  }

  /**
   * Predicate that returns true if the function argument is not a subclass of the class specified
   * 
   * @throws NullPointerException When an argument is null
   * @throws IllegalArgumentException When a non-class (e.g. interface) was provided
   */
  @Predicate
  public static boolean isNotExtending(final Object obj, final Class<?> _class)
  {
    return !ReflectionUtils.isExtending(obj.getClass(), _class);
  }

  /**
   * Predicate that returns true if the function argument is not an instance of the interface specified
   * 
   * @throws NullPointerException When an argument is null
   * @throws IllegalArgumentException When a non-interface (e.g. class) was provided
   */
  @Predicate
  public static boolean isNotImplementing(final Object obj, final Class<?> _class)
  {
    return !ReflectionUtils.isImplementing(obj.getClass(), _class);
  }

  /**
   * Predicate that returns true if the function argument is an instance of the class specified
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean instanceOf(final Object obj, final Class<?> _class)
  {
    return ReflectionUtils.instanceOf(obj.getClass(), _class);
  }

  /**
   * Predicate returning true when the function argument is null
   */
  @Predicate
  public static boolean isNull(final Object element)
  {
    return element == null;
  }

  /**
   * Predicate returning true when the function argument is not null
   */
  @Predicate
  public static boolean isNotNull(final Object element)
  {
    return element != null;
  }

  /**
   * Predicate returning true always, can be used to print out all elements in an all() Linq statement
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean println(final Object element)
  {
    System.out.println(element);
    return true;
  }

  /**
   * Predicate returning true always, can be used to print out all elements in an all() Linq statement
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean print(final Object element)
  {
    System.out.print(element);
    return true;
  }
  
  /**
   * Predicate returning true when the function argument is less than a value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static <T extends Comparable<T>> boolean lessThan(final T element, final T _value)
  {
    return element.compareTo(_value) < 0;
  }

  /**
   * Predicate returning true when the function argument is less than or equal to a value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static <T extends Comparable<T>> boolean lessThanOrEqual(final T element, final T _value)
  {
    return element.compareTo(_value) <= 0;
  }

  /**
   * Predicate returning true when the function argument's toString() does not contain some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notContains(final Object element, final String _part)
  {
    return !StringUtils.contains(element.toString(), _part, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument's toString() does not contain some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notContains(final Object element, final String _part, final StringComparison _comparison)
  {
    return !StringUtils.contains(element.toString(), _part, _comparison);
  }

  /**
   * Predicate returning true if an element is not contained in the function argument (Array)
   */
  @Predicate
  public static <T> boolean notContainedIn(final T element, final T[] _elements)
  {
    return !Linq.contains(_elements, element);
  }

  /**
   * Predicate returning true if an element is not contained in the function argument (Iterable)
   */
  @Predicate
  public static boolean notContainedBy(final Object element, final Iterable _elements)
  {
    return !Linq.contains(_elements, element);
  }

  /**
   * Predicate returning true when the function argument's toString() does not end with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notEndsWith(final Object element, final String _suffix)
  {
    return !StringUtils.endsWith(element.toString(), _suffix, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument's toString() does not end with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notEndsWith(final Object element, final String _suffix, final StringComparison _comparison)
  {
    return !StringUtils.endsWith(element.toString(), _suffix, _comparison);
  }

  /**
   * Predicate returning true when the function argument is not equal to a value
   */
  @Predicate
  public static boolean notEqual(final Object element, final Object _value)
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

  /**
   * Predicate that returns true if the function argument is not an instance of the class specified
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notInstanceOf(final Object obj, final Class<?> _class)
  {
    return !ReflectionUtils.instanceOf(obj.getClass(), _class);
  }

  /**
   * Predicate returning true when the function argument's toString() does not start with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notStartsWith(final Object element, final String _prefix)
  {
    return !StringUtils.startsWith(element.toString(), _prefix, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument's toString() does not start with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notStartsWith(final Object element, final String _prefix, final StringComparison _comparison)
  {
    return !StringUtils.startsWith(element.toString(), _prefix, _comparison);
  }

  /**
   * Predicate returning true when the function argument's toString() starts with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean startsWith(final Object element, final String _prefix)
  {
    return StringUtils.startsWith(element.toString(), _prefix, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument's toString() starts with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean startsWith(final Object element, final String _prefix, final StringComparison _comparison)
  {
    return StringUtils.startsWith(element.toString(), _prefix, _comparison);
  }

  private Objects()
  {
  }
}
