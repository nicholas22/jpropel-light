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
import lombok.Actions.Action0;
import lombok.Actions.Action1;
import lombok.SneakyThrows;
import lombok.Validate;
import lombok.Validate.NotNull;
import lombok.val;
import propel.core.utils.ReflectionUtils;

/**
 * Some common, re-usable dynamic method invocation projections
 */
public final class InvokeDynamic
{

  /**
   * A no-argument action which invokes the method name specified on the given object.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static Action0 invokeMethodWithNoArgsOn(@NotNull final Object obj, @NotNull final String methodName)
  {
    return new Action0() {

      private final Method method;

      // initialiser block
      {
        method = getSingleMethodIfExists(obj, methodName);
      }

      @SneakyThrows
      public void apply()
      {
        try
        {
          method.invoke(obj, (Object[]) null);
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
   * Returns a one-argument action which invokes the method name specified on the given object, passing the function argument to the invoked
   * method.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static Action1<Object> invokeMethodWithOneArgOn(@NotNull final Object obj, @NotNull final String methodName)
  {
    return new Action1<Object>() {

      private final Method method;

      // initialiser block
      {
        method = getSingleMethodIfExists(obj, methodName);
      }

      @SneakyThrows
      public void apply(Object arg)
      {
        try
        {
          method.invoke(obj, arg);
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
   * Returns a one-argument action which invokes the no-arg method on this argument
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static Action1<Object> invokeMethod(@NotNull final String methodName)
  {
    return invokeMethod(methodName, null);
  }

  /**
   * Returns a one-argument action which invokes the one-arg method on this argument
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static Action1<Object> invokeMethod(@NotNull final String methodName, final Object[] obj)
  {
    return new Action1<Object>() {
      @SneakyThrows
      public void apply(Object arg)
      {
        try
        {
          Method method = getSingleMethodIfExists(arg, methodName);
          method.invoke(arg, obj);
        }
        catch(InvocationTargetException e)
        {
          // get rid of wrapper exception
          throw e.getCause();
        }
      }
    };
  }

  static Method getSingleMethodIfExists(final Object obj, final String methodName)
  {
    val methods = ReflectionUtils.getMethods(obj.getClass(), methodName, false);
    if (methods.length > 1)
      throw new IllegalArgumentException("Found multiple method overloads for " + obj.getClass() + "." + methodName);
    if (methods.length <= 0)
      throw new IllegalArgumentException("Could not find method " + obj.getClass() + "." + methodName);

    return methods[0];
  }

  private InvokeDynamic()
  {
  }
}
