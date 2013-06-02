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
import propel.core.functional.Functions.Function0;
import propel.core.functional.Functions.Function1;
import lombok.SneakyThrows;
import lombok.Validate;
import lombok.Validate.NotNull;

/**
 * Some common, re-usable dynamic method invocation projections (uses Function0<Void> instead of Action0)
 */
public final class InvokeDynamicFunc
{

  /**
   * A no-argument action which invokes the method name specified on the given object.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static Function0<Void> invokeMethodWithNoArgsOn(@NotNull final Object obj, @NotNull final String methodName)
  {
    return new Function0<Void>() {

      private final Method method;

      // initialiser block
      {
        method = InvokeDynamic.getSingleMethodIfExists(obj, methodName);
      }

      @SneakyThrows
      public Void apply()
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
        return null;
      }
    };
  }

  /**
   * A one-argument action which invokes the method name specified on the given object, passing the function argument to the invoked method.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static Function1<Object, Void> invokeMethodWithOneArgOn(@NotNull final Object obj, @NotNull final String methodName)
  {
    return new Function1<Object, Void>() {

      private final Method method;

      // initialiser block
      {
        method = InvokeDynamic.getSingleMethodIfExists(obj, methodName);
      }

      @SneakyThrows
      public Void apply(Object arg)
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
        return null;
      }
    };
  }

  /**
   * A one-argument function which invokes the method name specified on the given object, passing the function argument to the invoked
   * method.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static Function1<Object, Object> invokeFunctionWithOneArgOn(@NotNull final Object obj, @NotNull final String methodName)
  {
    return new Function1<Object, Object>() {

      private final Method method;

      // initialiser block
      {
        method = InvokeDynamic.getSingleMethodIfExists(obj, methodName);
      }

      @SneakyThrows
      public Object apply(Object arg)
      {
        try
        {
          return method.invoke(obj, arg);
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
   * A one-argument action which invokes the no-arg method on this argument
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static Function1<Object, Void> invokeMethod(@NotNull final String methodName)
  {
    return invokeMethod(methodName, null);
  }

  /**
   * A one-argument action which invokes the one-arg method on this argument
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static Function1<Object, Void> invokeMethod(@NotNull final String methodName, final Object[] obj)
  {
    return new Function1<Object, Void>() {
      @SneakyThrows
      public Void apply(Object arg)
      {
        try
        {
          Method method = InvokeDynamic.getSingleMethodIfExists(arg, methodName);
          method.invoke(arg, obj);
        }
        catch(InvocationTargetException e)
        {
          // get rid of wrapper exception
          throw e.getCause();
        }
        return null;
      }
    };
  }

  /**
   * A one-argument function which invokes the no-arg method on this argument
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static Function1<Object, Object> invokeFunction(@NotNull final String methodName)
  {
    return invokeFunction(methodName, null);
  }

  /**
   * A one-argument action which invokes the one-arg method on this argument
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static Function1<Object, Object> invokeFunction(@NotNull final String methodName, final Object[] obj)
  {
    return new Function1<Object, Object>() {

      @SneakyThrows
      public Object apply(Object arg)
      {
        try
        {
          Method method = InvokeDynamic.getSingleMethodIfExists(arg, methodName);
          return method.invoke(arg, obj);
        }
        catch(InvocationTargetException e)
        {
          // get rid of wrapper exception
          throw e.getCause();
        }
      }
    };
  }

  private InvokeDynamicFunc()
  {
  }
}
