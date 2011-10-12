/*
 ///////////////////////////////////////////////////////////
 //  This file is part of Propel.
 //
 //  Propel is free software: you can redistribute it and/or modify
 //  it under the terms of the GNU Lesser General Public License as published by
 //  the Free Software Foundation, either version 3 of the License, or
 //  (at your option) any later version.
 //
 //  Propel is distributed in the hope that it will be useful,
 //  but WITHOUT ANY WARRANTY; without even the implied warranty of
 //  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 //  GNU Lesser General Public License for more details.
 //
 //  You should have received a copy of the GNU Lesser General Public License
 //  along with Propel.  If not, see <http://www.gnu.org/licenses/>.
 ///////////////////////////////////////////////////////////
 //  Authored by: Nikolaos Tountas -> salam.kaser-at-gmail.com
 ///////////////////////////////////////////////////////////
 */
package propel.core.functional.predicates;

import lombok.Function;
import propel.core.utils.StringComparison;
import propel.core.utils.StringUtils;
import propel.core.utils.ReflectionUtils;

/**
 * Some common, re-usable predicates
 */
public final class Predicates
{  
  /**
   * Predicate returning true when the function argument is equal to a value
   */
  @Function
  public static <T> Boolean equal(T element, T _value)
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
   * Predicate returning true when the function argument is not equal to a value
   */
  @Function
  public static <T> Boolean notEqual(T element, T _value)
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
   * Predicate returning true when the function argument is greater than a value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Function
  public static <T extends Comparable<T>> Boolean greaterThan(T element, T _value)
  {
    return element.compareTo(_value) > 0;
  }

  /**
   * Predicate returning true when the function argument is less than a value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Function
  public static <T extends Comparable<T>> Boolean lessThan(T element, T _value)
  {
    return element.compareTo(_value) < 0;
  }

  /**
   * Predicate returning true when the function argument is greater than or equal to a value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Function
  public static <T extends Comparable<T>> Boolean greaterThanOrEqual(T element, T _value)
  {
    return element.compareTo(_value) >= 0;
  }

  /**
   * Predicate returning true when the function argument is less than or equal to a value
   * 
   * @throws NullPointerException When an argument is null
   */
  @Function
  public static <T extends Comparable<T>> Boolean lessThanOrEqual(T element, T _value)
  {
    return element.compareTo(_value) <= 0;
  }

  /**
   * Predicate returning true when the function argument is null
   */
  @Function
  public static <T> Boolean isNull(T element)
  {
    return element == null;
  }

  /**
   * Predicate returning true when the function argument is not null
   */
  @Function
  public static <T> Boolean isNotNull(T element)
  {
    return element != null;
  }

  /**
   * Predicate returning true when the function argument is empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Function
  public static Boolean isEmpty(String element)
  {
    return element.isEmpty();
  }

  /**
   * Predicate returning true when the function argument is not empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Function
  public static Boolean isNotEmpty(String element)
  {
    return !element.isEmpty();
  }

  /**
   * Predicate returning true when the function argument is null or empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Function
  public static Boolean isNullOrEmpty(String element)
  {
    return StringUtils.isNullOrEmpty(element);
  }

  /**
   * Predicate returning true when the function argument is not null or empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Function
  public static Boolean isNotNullOrEmpty(String element)
  {
    return !StringUtils.isNullOrEmpty(element);
  }
  
  /**
   * Predicate returning true when the function argument is null, empty or blank.
   * 
   * @throws NullPointerException When an argument is null
   */
  @Function
  public static Boolean isNullOrBlank(String element)
  {
    return StringUtils.isNullOrBlank(element);
  }

  /**
   * Predicate returning true when the function argument is not null, empty or blank.
   * 
   * @throws NullPointerException When an argument is null
   */
  @Function
  public static Boolean isNotNullOrBlank(String element)
  {
    return !StringUtils.isNullOrBlank(element);
  }

  /**
   * Predicate returning true when the function argument starts with a prefix
   * 
   * @throws NullPointerException When an argument is null 
   */
  @Function
  public static Boolean startsWith(String element, String _prefix)
  {
    return StringUtils.startsWith(element, _prefix, StringComparison.Ordinal);
  }
  
  /**
   * Predicate returning true when the function argument starts with a prefix
   * 
   * @throws NullPointerException When an argument is null 
   */
  @Function
  public static Boolean startsWith(String element, String _prefix, StringComparison _comparison)
  {
    return StringUtils.startsWith(element, _prefix, _comparison);
  }

  /**
   * Predicate returning true when the function argument ends with a suffix
   * 
   * @throws NullPointerException When an argument is null 
   */
  @Function
  public static Boolean endsWith(String element, String _suffix)
  {
    return StringUtils.endsWith(element, _suffix, StringComparison.Ordinal);
  }
  
  /**
   * Predicate returning true when the function argument ends with a suffix
   * 
   * @throws NullPointerException When an argument is null 
   */
  @Function
  public static Boolean endsWith(String element, String _suffix, StringComparison _comparison)
  {
    return StringUtils.endsWith(element, _suffix, _comparison);
  }
  
  /**
   * Predicate returning true when the function argument contains some string
   * 
   * @throws NullPointerException When an argument is null 
   */
  @Function
  public static Boolean contains(String element, String _part)
  {
    return StringUtils.contains(element, _part, StringComparison.Ordinal);
  }
  
  /**
   * Predicate returning true when the function argument contains some string
   * 
   * @throws NullPointerException When an argument is null 
   */
  @Function
  public static Boolean contains(String element, String _part, StringComparison _comparison)
  {
    return StringUtils.contains(element, _part, _comparison);
  }
  
  /**
   * Predicate returning true when the function argument is equal to some string
   * 
   * @throws NullPointerException When an argument is null 
   */
  @Function
  public static Boolean isEqual(String element, String _other)
  {
    return StringUtils.equal(element, _other, StringComparison.Ordinal);
  }
  
  /**
   * Predicate returning true when the function argument is equal to some string
   * 
   * @throws NullPointerException When an argument is null 
   */
  @Function
  public static Boolean isEqual(String element, String _other, StringComparison _comparison)
  {
    return StringUtils.equal(element, _other, _comparison);
  }
  
  /**
   * Predicate returning true always, can be used to print out all elements in an all() Linq statement
   * 
   * @throws NullPointerException When an argument is null 
   */
  @Function
  public static <T> Boolean println(T element) {
    System.out.println(element);
    return true;
  }
  
  /**
   * Predicate that returns true if the function argument is a subclass of the class specified
   *     
   * @throws NullPointerException When an argument is null
   * @throws IllegalArgumentException When a non-class (e.g. interface) was provided
   */
  @Function
  public static <T> boolean isExtending(T obj, Class<?> _class)
  {
    return ReflectionUtils.isExtending(obj.getClass(), _class);
  }
  
  /**
   * Predicate that returns true if the function argument is an instance of the interface specified
   *     
   * @throws NullPointerException When an argument is null
   * @throws IllegalArgumentException When a non-interface (e.g. class) was provided
   */
  @Function
  public static <T> boolean isImplementing(T obj, Class<?> _class)
  {
    return ReflectionUtils.isImplementing(obj.getClass(), _class);
  }
  
  /**
   * Predicate that returns true if the function argument is an instance of the class specified
   *     
   * @throws NullPointerException When an argument is null
   */
  @Function
  public static <T> boolean instanceOf(T obj, Class<?> _class)
  {
    return ReflectionUtils.instanceOf(obj.getClass(), _class);
  }
  
  private Predicates()
  {
  }
}
