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
package propel.core.utils;

/**
 * Class with null value related utilities
 */
public final class Nullable
{
  /**
   * Throws a NPE if the value is null
   */
  public void check(final Object value)
  {
    if (value == null)
      throw new NullPointerException();
  }

  /**
   * Null coalescing extension method, similar to the ?? operator of C#
   */
  public static <T> T orElse(T value, T _elseValue)
  {
    if (value != null)
      return value;

    return _elseValue;
  }

  /**
   * Null coalescing extension method, similar to the ?? operator of C#
   */
  public static Object orElseObj(Object value, Object _elseValue)
  {
    if (value != null)
      return value;

    return _elseValue;
  }

  private Nullable()
  {
  }
}
