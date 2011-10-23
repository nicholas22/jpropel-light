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
 * Some common, re-usable predicates
 */
public final class Iterables
{
  private Iterables()
  {
  }

  /**
   * Predicate returning true when the function argument is empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isEmpty(Iterable<?> element)
  {
    return Linq.firstOrDefault(element) == null;
  }

  /**
   * Predicate returning true when the function argument is not empty
   * 
   * @throws NullPointerException When an argument is null
   */
  @Predicate
  public static boolean isNotEmpty(Iterable<?> element)
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
   * Predicate returning true when the function argument is equal to another array
   */
  @Predicate
  public static <T> boolean equal(Iterable<T> element, Iterable<T> _value)
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
   * Predicate returning true when the function argument is not equal to another array
   */
  @Predicate
  public static <T> boolean notEqual(Iterable<T> element, Iterable<T> _value)
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
}
