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

/**
 * Some common, re-usable predicates
 */
public final class Arrays
{
  private Arrays()
  {
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
}
