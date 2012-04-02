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
import propel.core.utils.StringComparison;
import propel.core.utils.StringUtils;

/**
 * Some common, re-usable predicates
 */
public final class Strings
{
  private Strings()
  {
  }

  /**
   * Predicate returning true when the function argument contains some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean contains(String element, String _part)
  {
    return StringUtils.contains(element, _part, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument contains some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean contains(String element, String _part, StringComparison _comparison)
  {
    return StringUtils.contains(element, _part, _comparison);
  }

  /**
   * Predicate returning true when the function argument ends with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean endsWith(String element, String _suffix)
  {
    return StringUtils.endsWith(element, _suffix, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument ends with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean endsWith(String element, String _suffix, StringComparison _comparison)
  {
    return StringUtils.endsWith(element, _suffix, _comparison);
  }

  /**
   * Predicate returning true when the function argument is empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isEmpty(String element)
  {
    return element.isEmpty();
  }

  /**
   * Predicate returning true when the function argument is equal to some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isEqual(String element, String _other)
  {
    return StringUtils.equal(element, _other, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument is equal to some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isEqual(String element, String _other, StringComparison _comparison)
  {
    return StringUtils.equal(element, _other, _comparison);
  }

  /**
   * Predicate returning true when the function argument is null or empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isNullOrEmpty(String element)
  {
    return StringUtils.isNullOrEmpty(element);
  }

  /**
   * Predicate returning true when the function argument is not null or empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isNotNullOrEmpty(String element)
  {
    return !StringUtils.isNullOrEmpty(element);
  }

  /**
   * Predicate returning true when the function argument is null, empty or blank.
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isNullOrBlank(String element)
  {
    return StringUtils.isNullOrBlank(element);
  }

  /**
   * Predicate returning true when the function argument is not null, empty or blank.
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isNotNullOrBlank(String element)
  {
    return !StringUtils.isNullOrBlank(element);
  }

  /**
   * Predicate returning true when the function argument has a specified length
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthEquals(String element, int _len)
  {
    return element.length() == _len;
  }

  /**
   * Predicate returning true when the function argument has a length greater than specified
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthGreaterThan(String element, int _len)
  {
    return element.length() > _len;
  }

  /**
   * Predicate returning true when the function argument has a length greater than or equal to specified
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthGreaterThanOrEqual(String element, int _len)
  {
    return element.length() >= _len;
  }

  /**
   * Predicate returning true when the function argument has a length less than specified
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthLessThan(String element, int _len)
  {
    return element.length() < _len;
  }

  /**
   * Predicate returning true when the function argument has a length less than or equal to specified
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthLessThanOrEqual(String element, int _len)
  {
    return element.length() <= _len;
  }

  /**
   * Predicate returning true when the function argument does not have a specified length
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthNotEqual(String element, int _len)
  {
    return element.length() != _len;
  }

  /**
   * Predicate returning true when the function argument does not contain some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notContains(String element, String _part)
  {
    return !StringUtils.contains(element, _part, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument does not contain some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notContains(String element, String _part, StringComparison _comparison)
  {
    return !StringUtils.contains(element, _part, _comparison);
  }

  /**
   * Predicate returning true when the function argument does not end with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notEndsWith(String element, String _suffix)
  {
    return !StringUtils.endsWith(element, _suffix, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument does not end with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notEndsWith(String element, String _suffix, StringComparison _comparison)
  {
    return !StringUtils.endsWith(element, _suffix, _comparison);
  }

  /**
   * Predicate returning true when the function argument does not start with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notStartsWith(String element, String _prefix)
  {
    return !StringUtils.startsWith(element, _prefix, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument does not start with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notStartsWith(String element, String _prefix, StringComparison _comparison)
  {
    return !StringUtils.startsWith(element, _prefix, _comparison);
  }

  /**
   * Predicate returning true when the function argument starts with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean startsWith(String element, String _prefix)
  {
    return StringUtils.startsWith(element, _prefix, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument starts with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean startsWith(String element, String _prefix, StringComparison _comparison)
  {
    return StringUtils.startsWith(element, _prefix, _comparison);
  }

  /**
   * Predicate returning true always, can be used to print out all elements in an all() Linq statement
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean println(String element)
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
  public static boolean print(String element)
  {
    System.out.print(element);
    return true;
  }
}
