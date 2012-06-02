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
import lombok.Action;
import lombok.Function;
import lombok.SneakyThrows;

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
  @Action
  @SneakyThrows
  public static void invokeMethod(final Object obj, final Method _method)
  {
    try
    {
      _method.invoke(obj, (Object[]) null);
    }
    catch(InvocationTargetException e)
    {
      // get rid of wrapper exception
      throw e.getCause();
    }
  }

  /**
   * A no-argument action which invokes the method specified on the given object.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalAccessException The method is inaccessible
   * @throws Throwable Some exception thrown during the given method's execution
   */
  @Function
  @SneakyThrows
  public static Void invokeMethodFunc(final Object obj, final Method _method)
  {
    try
    {
      _method.invoke(obj, (Object[]) null);
      return null;
    }
    catch(InvocationTargetException e)
    {
      // get rid of wrapper exception
      throw e.getCause();
    }
  }

  /**
   * A no-argument function which invokes the method name specified on the given object.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalAccessException The method is inaccessible
   * @throws Throwable Some exception thrown during the given function's execution
   */
  @Function
  @SneakyThrows
  public static <T> T invokeFunction(final Object obj, final Method _method)
  {
    try
    {
      return (T) _method.invoke(obj, (Object[]) null);
    }
    catch(InvocationTargetException e)
    {
      // get rid of wrapper exception
      throw e.getCause();
    }
  }

  private InvokeNoArgs()
  {
  }
}
