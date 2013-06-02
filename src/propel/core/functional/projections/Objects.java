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
import propel.core.functional.Functions.Function1;

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
  public static Function1<Object, Class<?>> getClassType()
  {
    return GET_CLASS_TYPE;
  }
  private static final Function1<Object, Class<?>> GET_CLASS_TYPE = new Function1<Object, Class<?>>() {
    @Override
    public Class<?> apply(final Object element)
    {
      return element.getClass();
    }
  };

  /**
   * Calls getClass().getName() on the function argument
   * 
   * @throws NullPointerException An argument is null
   */
  public static Function1<Object, String> getClassName()
  {
    return GET_CLASS_NAME;
  }
  private static final Function1<Object, String> GET_CLASS_NAME = new Function1<Object, String>() {
    @Override
    public String apply(final Object element)
    {
      return element.getClass().getName();
    }
  };

  /**
   * Calls getClass().getSimpleName() on the function argument
   * 
   * @throws NullPointerException An argument is null
   */
  public static Function1<Object, String> getClassNameSimple()
  {
    return GET_CLASS_NAME_SIMPLE;
  }
  private static final Function1<Object, String> GET_CLASS_NAME_SIMPLE = new Function1<Object, String>() {
    @Override
    public String apply(final Object element)
    {
      return element.getClass().getSimpleName();
    }
  };

  /**
   * Returns the object's hashcode
   * 
   * @throws NullPointerException An argument is null
   */
  public static Function1<Object, Integer> getHashCode()
  {
    return GET_HASHCODE;
  }
  private static final Function1<Object, Integer> GET_HASHCODE = new Function1<Object, Integer>() {
    @Override
    public Integer apply(final Object element)
    {
      return element.hashCode();
    }
  };

  /**
   * Null coalescing function, similar to ?? operator in C#
   */
  public static Object orElse(final Object _elseValue)
  {
    return new Function1<Object, Object>() {
      @Override
      public Object apply(final Object element)
      {
        return element != null ? element : _elseValue;
      }
    };
  }

  /**
   * Calls toString() on function arguments
   * 
   * @throws NullPointerException An argument is null
   */
  public static Function1<Object, String> toStringify()
  {
    return TO_STRINGIFY;
  }
  private static final Function1<Object, String> TO_STRINGIFY = new Function1<Object, String>() {
    @Override
    public String apply(final Object element)
    {
      return element.toString();
    }
  };

  /**
   * Calls toString() on function arguments, null-coalescing (uses empty string)
   * 
   * @throws NullPointerException An argument is null
   */
  public static Function1<Object, String> toStringifyNullCoalescing()
  {
    return TO_STRINGIFY_NULL_COALESCING;
  }
  private static final Function1<Object, String> TO_STRINGIFY_NULL_COALESCING = new Function1<Object, String>() {
    @Override
    public String apply(final Object element)
    {
      return element != null ? element.toString() : CONSTANT.EMPTY_STRING;
    }
  };

  /**
   * Calls toString() on function arguments, using a default value when null is encountered
   * 
   * @throws NullPointerException An argument is null
   */
  public static Function1<Object, String> toStringifySafe(final String _defaultValue)
  {
    return new Function1<Object, String>() {
      @Override
      public String apply(final Object element)
      {
        return element != null ? element.toString() : _defaultValue;
      }
    };
  }

  private Objects()
  {
  }
}
