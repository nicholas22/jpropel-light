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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.SneakyThrows;
import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.functional.Actions.Action1;
import propel.core.functional.Functions.Function1;

/**
 * Some common, re-usable method invocation projections; methods accept an argument to be specified.
 */
public final class InvokeOneArg
{

  /**
   * A one-argument action which invokes the method specified on the given object, passing the function argument to the invoked method.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static Action1<Object> invokeMethod(@NotNull final Object _obj, @NotNull final Method _method)
  {
    return new Action1<Object>() {
      @Override
      @SneakyThrows
      public void apply(final Object element)
      {
        try
        {
          _method.invoke(_obj, element);
        }
        catch(InvocationTargetException e)
        {
          // get rid of wrapper exception
          throw e.getCause();
        }
      }
    };
  }

  /**
   * A one-argument action which invokes the method specified on the given object, passing the function argument to the invoked method.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static Function1<Object, Void> invokeMethodFunc(@NotNull final Object _obj, @NotNull final Method _method)
  {
    return new Function1<Object, Void>() {
      @Override
      @SneakyThrows
      public Void apply(final Object element)
      {
        try
        {
          _method.invoke(_obj, element);
          return null;
        }
        catch(InvocationTargetException e)
        {
          // get rid of wrapper exception
          throw e.getCause();
        }
      }
    };
  }

  /**
   * A one-argument function which invokes the method specified on the given object, passing the function argument to the invoked method.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static <T> Function1<Object, T> invokeFunction(@NotNull final Object _obj, @NotNull final Method _method)
  {
    return new Function1<Object, T>() {
      @Override
      @SneakyThrows
      public T apply(final Object element)
      {
        try
        {
          return (T) _method.invoke(_obj, element);
        }
        catch(InvocationTargetException e)
        {
          // get rid of wrapper exception
          throw e.getCause();
        }
      }
    };
  }

  private InvokeOneArg()
  {
  }
}
