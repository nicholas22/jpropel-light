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
 * Some common, re-usable predicates for Strings
 */
public final class Strings
{
  /**
   * Predicate returning true when the function argument contains some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean contains(final String element, final String _part)
  {
    return StringUtils.contains(element, _part, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument contains some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean contains(final String element, final String _part, final StringComparison _comparison)
  {
    return StringUtils.contains(element, _part, _comparison);
  }

  /**
   * Predicate returning true when the function argument ends with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean endsWith(final String element, final String _suffix)
  {
    return StringUtils.endsWith(element, _suffix, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument ends with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean endsWith(final String element, final String _suffix, final StringComparison _comparison)
  {
    return StringUtils.endsWith(element, _suffix, _comparison);
  }

  /**
   * Predicate returning true when the function argument is empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isEmpty(final String element)
  {
    return element.isEmpty();
  }

  /**
   * Predicate returning true when the function argument is equal to some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isEqual(final String element, final String _other)
  {
    return StringUtils.equal(element, _other, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument is equal to some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isEqual(final String element, final String _other, final StringComparison _comparison)
  {
    return StringUtils.equal(element, _other, _comparison);
  }

  /**
   * Predicate returning true when the function argument is null or empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isNullOrEmpty(final String element)
  {
    return StringUtils.isNullOrEmpty(element);
  }

  /**
   * Predicate returning true when the function argument is not null or empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isNotNullOrEmpty(final String element)
  {
    return !StringUtils.isNullOrEmpty(element);
  }

  /**
   * Predicate returning true when the function argument is null, empty or blank.
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isNullOrBlank(final String element)
  {
    return StringUtils.isNullOrBlank(element);
  }

  /**
   * Predicate returning true when the function argument is not null, empty or blank.
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isNotNullOrBlank(final String element)
  {
    return !StringUtils.isNullOrBlank(element);
  }

  /**
   * Predicate returning true when the function argument has a specified length
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthEquals(final String element, final int _len)
  {
    return element.length() == _len;
  }

  /**
   * Predicate returning true when the function argument has a length greater than specified
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthGreaterThan(final String element, final int _len)
  {
    return element.length() > _len;
  }

  /**
   * Predicate returning true when the function argument has a length greater than or equal to specified
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthGreaterThanOrEqual(final String element, final int _len)
  {
    return element.length() >= _len;
  }

  /**
   * Predicate returning true when the function argument has a length less than specified
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthLessThan(final String element, final int _len)
  {
    return element.length() < _len;
  }

  /**
   * Predicate returning true when the function argument has a length less than or equal to specified
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthLessThanOrEqual(final String element, final int _len)
  {
    return element.length() <= _len;
  }

  /**
   * Predicate returning true when the function argument does not have a specified length
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean lengthNotEqual(final String element, final int _len)
  {
    return element.length() != _len;
  }

  /**
   * Predicate returning true when the function argument does not contain some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notContains(final String element, final String _part)
  {
    return !StringUtils.contains(element, _part, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument does not contain some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notContains(final String element, final String _part, final StringComparison _comparison)
  {
    return !StringUtils.contains(element, _part, _comparison);
  }

  /**
   * Predicate returning true when the function argument does not end with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notEndsWith(final String element, final String _suffix)
  {
    return !StringUtils.endsWith(element, _suffix, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument does not end with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notEndsWith(final String element, final String _suffix, final StringComparison _comparison)
  {
    return !StringUtils.endsWith(element, _suffix, _comparison);
  }

  /**
   * Predicate returning true when the function argument does not start with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notStartsWith(final String element, final String _prefix)
  {
    return !StringUtils.startsWith(element, _prefix, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument does not start with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean notStartsWith(final String element, final String _prefix, final StringComparison _comparison)
  {
    return !StringUtils.startsWith(element, _prefix, _comparison);
  }

  /**
   * Predicate returning true when the function argument starts with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean startsWith(final String element, final String _prefix)
  {
    return StringUtils.startsWith(element, _prefix, StringComparison.Ordinal);
  }

  /**
   * Predicate returning true when the function argument starts with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean startsWith(final String element, final String _prefix, final StringComparison _comparison)
  {
    return StringUtils.startsWith(element, _prefix, _comparison);
  }

  private Strings()
  {
  }
}
