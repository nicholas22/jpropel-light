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
import propel.core.functional.Actions.Action1;
import propel.core.functional.Functions.Function1;
import lombok.Validate;
import lombok.Validate.NotNull;

/**
 * Some common, re-usable method invocation projections
 */
public final class InvokeNoArgs
{

  /**
   * A no-argument action which invokes the method specified on the given object.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalAccessException The method is inaccessible
   * @throws Throwable Some exception thrown during the given method's execution
   */
  @Validate
  public static Action1<Object> invokeMethod(@NotNull final Method _method)
  {
    return new Action1<Object>() {
      @Override
      @SneakyThrows
      public void apply(final Object element)
      {
        try
        {
          _method.invoke(element, (Object[]) null);
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
   * A no-argument action which invokes the method specified on the given object.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalAccessException The method is inaccessible
   * @throws Throwable Some exception thrown during the given method's execution
   */
  @Validate
  public static Function1<Object, Void> invokeMethodFunc(@NotNull final Method _method)
  {
    return new Function1<Object, Void>() {
      @Override
      @SneakyThrows
      public Void apply(final Object element)
      {
        try
        {
          _method.invoke(element, (Object[]) null);
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
   * A no-argument function which invokes the method name specified on the given object.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalAccessException The method is inaccessible
   * @throws Throwable Some exception thrown during the given function's execution
   */
  @Validate
  public static <T> Function1<Object, T> invokeFunction(@NotNull final Method _method)
  {
    return new Function1<Object, T>() {
      @SuppressWarnings("unchecked")
      @Override
      @SneakyThrows
      public T apply(final Object element)
      {
        try
        {
          return (T) _method.invoke(element, (Object[]) null);
        }
        catch(InvocationTargetException e)
        {
          // get rid of wrapper exception
          throw e.getCause();
        }
      }
    };
  }

  private InvokeNoArgs()
  {
  }
}
