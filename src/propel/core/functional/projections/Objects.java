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
package propel.core.functional.projections;

import propel.core.common.CONSTANT;
import lombok.Function;

/**
 * Some common, re-usable projections for objects
 */
public final class Objects
{
  /**
   * Calls getClass() on the function argument
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static Class<?> getClassType(final Object obj)
  {
    return obj.getClass();
  }

  /**
   * Calls getClass().getName() on the function argument
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String getClassName(final Object obj)
  {
    return obj.getClass().getName();
  }

  /**
   * Calls getClass().getSimpleName() on the function argument
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String getClassNameSimple(final Object obj)
  {
    return obj.getClass().getSimpleName();
  }

  /**
   * Returns the object's hashcode
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static int getHashCode(final Object obj)
  {
    return obj.hashCode();
  }

  /**
   * Null coalescing function, similar to ?? operator in C#
   */
  @Function
  public static Object orElse(final Object value, final Object _elseValue)
  {
    return value != null ? value : _elseValue;
  }

  /**
   * Calls toString() on function arguments
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String toStringify(final Object object)
  {
    return object.toString();
  }

  /**
   * Calls toString() on function arguments, null-coalescing (uses empty string)
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String toStringifyNullCoalescing(final Object object)
  {
    return object != null ? object.toString() : CONSTANT.EMPTY_STRING;
  }

  /**
   * Calls toString() on function arguments, using a default value when null is encountered
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String toStringifySafe(final Object object, final String _defaultValue)
  {
    return object != null ? object.toString() : _defaultValue;
  }

  private Objects()
  {
  }
}
