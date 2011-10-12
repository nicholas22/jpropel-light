/*
 ///////////////////////////////////////////////////////////
 //  This file is part of Propel.
 //
 //  Propel is free software: you can redistribute it and/or modify
 //  it under the terms of the GNU Lesser General Public License as published by
 //  the Free Software Foundation, either version 3 of the License, or
 //  (at your option) any later version.
 //
 //  Propel is distributed in the hope that it will be useful,
 //  but WITHOUT ANY WARRANTY; without even the implied warranty of
 //  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 //  GNU Lesser General Public License for more details.
 //
 //  You should have received a copy of the GNU Lesser General Public License
 //  along with Propel.  If not, see <http://www.gnu.org/licenses/>.
 ///////////////////////////////////////////////////////////
 //  Authored by: Nikolaos Tountas -> salam.kaser-at-gmail.com
 ///////////////////////////////////////////////////////////
 */
package propel.core.functional.projections;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import lombok.Actions.Action0;
import lombok.Function;
import lombok.Functions.Function0;
import lombok.Functions.Function1;
import lombok.SneakyThrows;
import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.collections.KeyValuePair;
import propel.core.utils.FileUtils;
import propel.core.utils.ReflectionUtils;

/**
 * Some common, re-usable projections
 */
public final class Projections
{
  /**
   * Identity function
   */
  @Function
  public static <T> T argumentToResult(T arg)
  {
    return arg;
  }

  /**
   * Calls toString() on function arguments
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String toStringify(Object object)
  {
    return object.toString();
  }

  /**
   * Null coalescing function, similar to ?? operator in C#
   */
  @Function
  public static <T> T orElse(T value, T _elseValue)
  {
    if (value != null)
      return value;

    return _elseValue;
  }

  /**
   * Calls toLowerCase() on function arguments
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String toLowerCase(String value)
  {
    return value.toLowerCase();
  }

  /**
   * Calls toUpperCase() on function arguments
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String toUpperCase(String value)
  {
    return value.toUpperCase();
  }

  /**
   * Calls getKey() on key/value pair function arguments
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static <K, V> K keySelector(KeyValuePair<K, V> kvp)
  {
    return kvp.getKey();
  }

  /**
   * Calls getValue() on key/value pair function arguments
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static <K, V> V valueSelector(KeyValuePair<K, V> kvp)
  {
    return kvp.getValue();
  }

  /**
   * Calls System.out.println() passing the function argument to be printed
   */
  @Function
  public static <T> boolean println(T element)
  {
    System.out.println(element);
    return true;
  }

  /**
   * Action that appends some text data to a file. No EOL character is appended, just the given text.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException An argument is out of range
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, or it does not exist and cannot be created, or cannot be appended to.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Function
  @SneakyThrows
  public static void appendToFile(final String _absPath, final String _data)
  {
    FileUtils.appendText(_absPath, _data);
  }

  /**
   * Action that moves a file from one absolute file path to another
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, it does not exist or cannot be read/created.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Function
  @SneakyThrows
  public static void moveFile(final String _fromAbsPath, final String _toAbsPath)
  {
    FileUtils.moveFile(_fromAbsPath, _toAbsPath);
  }

  /**
   * Action that moves a file from one absolute file path to another. No exception is thrown under any circumstance, but the file is not
   * moved upon an exception occurring.
   */
  @Function
  public static void tryMoveFile(final String _fromAbsPath, final String _toAbsPath)
  {
    FileUtils.tryMoveFile(_fromAbsPath, _toAbsPath);
  }

  /**
   * Action that copies a file from one absolute file path to another
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, it does not exist or cannot be read/created.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Function
  @SneakyThrows
  public static void copyFile(final String _fromAbsPath, final String _toAbsPath)
  {
    FileUtils.copyFile(_fromAbsPath, _toAbsPath);
  }

  /**
   * Action that copies a file from one absolute file path to another. No exception is thrown under any circumstance, but the file is not
   * copied upon an exception occurring.
   */
  @Function
  public static void tryCopyFile(final String _fromAbsPath, final String _toAbsPath)
  {
    FileUtils.tryCopyFile(_fromAbsPath, _toAbsPath);
  }

  /**
   * Action that deletes a file from an absolute file path.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Function
  @SneakyThrows
  public static void deleteFile(final String _absPath)
  {
    FileUtils.deleteFile(_absPath);
  }

  /**
   * Action that deletes a file from one absolute file path. No exception is thrown under any circumstance, but the file is not copied upon
   * an exception occurring.
   */
  @Function
  public static void tryDeleteFile(final String _absPath)
  {
    FileUtils.tryDeleteFile(_absPath);
  }

  /**
   * Action that throws the specified exception when being invoked. For the correct stack trace to be used, a new exception needs to be
   * created. As such, the action will attempt to create one and re-use the error message of the exception provided. For best results, the
   * exception class type should have a single-argument constructor accepting the exception message as argument. Alternatively a no-argument
   * constructor will be attempted to be instantiated. If both of these strategies fail, an Exception object is created and thrown,
   * containing the error message.
   * 
   * @throws Throwable When the action is called
   */
  @Function
  @SneakyThrows
  public static Void throwException(Throwable _e)
  {
    Throwable actual;
    try
    {
      actual = ReflectionUtils.activate(_e.getClass(), new Object[] {_e.getMessage()});
    }
    catch(Throwable e)
    {
      try
      {
        actual = ReflectionUtils.activate(_e.getClass());
      }
      catch(Throwable ex)
      {
        actual = new Exception(_e.getMessage());
      }
    }

    throw actual;
  }

  /**
   * Action that throws the specified exception when being invoked. For the correct stack trace to be used, a new exception needs to be
   * created. As such, the action will attempt to create one and re-use the error message of the exception provided, concatenating it with
   * the object that caused the exception. For best results, the exception class type should have a single-argument constructor accepting
   * the exception message as argument. Alternatively a no-argument constructor will be attempted to be instantiated. If both of these
   * strategies fail, an Exception object is created and thrown, containing the error message.
   * 
   * @throws Throwable When the action is called
   */
  @Function
  @SneakyThrows
  public static Void throwDetailed(Object obj, Throwable _e)
  {
    Throwable actual;
    try
    {
      actual = ReflectionUtils.activate(_e.getClass(), new Object[] {_e.getMessage() + obj});
    }
    catch(Throwable e)
    {
      actual = new Exception(_e.getMessage() + obj);
    }

    throw actual;
  }

  /**
   * Returns a no-argument action which invokes the method name specified on the given object.
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
        method = getSingleMethodIfExists(obj,  methodName);
      }

      @SneakyThrows
      public Void apply()
      {
        try
        {
          method.invoke(obj, null);
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
   * Returns a one-argument action which invokes the method name specified on the given object, passing the function argument to the
   * invoked method.
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
        method = getSingleMethodIfExists(obj,  methodName);
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
   * Returns a no-argument function which invokes the method name specified on the given object.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static <T> Function0<T> invokeFunctionWithNoArgsOn(@NotNull final Object obj, @NotNull final String methodName)
  {
    return new Function0<T>() {

      private final Method method;

      // initialiser block
      {
        method = getSingleMethodIfExists(obj,  methodName);
      }

      @SneakyThrows
      public T apply()
      {
        try
        {
          return (T) method.invoke(obj, null);
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
   * Returns a one-argument function which invokes the method name specified on the given object, passing the function argument to the
   * invoked method.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static <T> Function1<Object, T> invokeFunctionWithOneArgOn(@NotNull final Object obj, @NotNull final String methodName)
  {
    return new Function1<Object, T>() {

      private final Method method;

      // initialiser block
      {
        method = getSingleMethodIfExists(obj,  methodName);
      }

      @SneakyThrows
      public T apply(Object arg)
      {
        try
        {
          return (T) method.invoke(obj, arg);
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
  public static <T> Function1<Object, Void> invokeMethod(@NotNull final String methodName)
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
  public static <T> Function1<Object, Void> invokeMethod(@NotNull final String methodName, final Object[] obj)
  {
    return new Function1<Object, Void>() {
      @SneakyThrows
      public Void apply(Object arg)
      {
        try
        {
          Method method = getSingleMethodIfExists(arg,  methodName);
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
   * Returns a one-argument function which invokes the no-arg method on this argument
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static <T> Function1<Object, T> invokeFunction(@NotNull final String methodName)
  {
    return invokeFunction(methodName, null);
  }
  
  /**
   * Returns a one-argument action which invokes the one-arg method on this argument
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static <T> Function1<Object, T> invokeFunction(@NotNull final String methodName, final Object[] obj)
  {
    return new Function1<Object, T>() {
      @SneakyThrows
      public T apply(Object arg)
      {
        try
        {
          Method method = getSingleMethodIfExists(arg,  methodName);
          return (T) method.invoke(arg, obj);
        }
        catch(InvocationTargetException e)
        {
          // get rid of wrapper exception
          throw e.getCause();
        }
      }
    };
  }
  
  private static Method getSingleMethodIfExists(Object obj, String methodName) {
    Method[] methods = ReflectionUtils.getMethods(obj.getClass(), methodName, false);
    if (methods.length > 1)
      throw new IllegalArgumentException("Found multiple method overloads for " + obj.getClass() + "." + methodName);
    if (methods.length <= 0)
      throw new IllegalArgumentException("Could not find method " + obj.getClass() + "." + methodName);
    return methods[0];
  }

  /**
   * Returns an action with an empty body
   */
  public static Action0 empty()
  {
    return new Action0() {

      @Override
      public void apply()
      {
        // does nothing
      }
    };
  }

  /**
   * Returns a no-argument function with an empty body
   */
  public static Function0<Void> emptyFunc()
  {
    return new Function0<Void>() {

      @Override
      public Void apply()
      {
        // does nothing
        return null;
      }
    };
  }

  private Projections()
  {
  }
}
