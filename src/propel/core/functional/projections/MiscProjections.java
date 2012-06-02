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

import propel.core.utils.ReflectionUtils;
import lombok.Action;
import lombok.Function;
import lombok.SneakyThrows;

/**
 * Some common, re-usable projections (miscellaneous)
 */
public final class MiscProjections
{
  /**
   * Identity function
   */
  @Function
  public static Object argToResult(final Object arg)
  {
    return arg;
  }

  /**
   * Identity function
   */
  @Function
  public static <T> T argumentToResult(final T arg)
  {
    return arg;
  }

  /**
   * Returns an action with an empty body
   */
  @Action
  public static void empty()
  {
    // does nothing
  }

  /**
   * Returns a no-argument function with an empty body
   */
  @Function
  public static Void emptyFunc()
  {
    return null;
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
  @Action
  @SneakyThrows
  public static void throwException(final Throwable _e)
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
  @Action
  @SneakyThrows
  public static void throwDetailed(final Object obj, final Throwable _e)
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
  public static Void throwExceptionFunc(final Throwable _e)
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
  public static Void throwDetailedFunc(final Object obj, final Throwable _e)
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

  private MiscProjections()
  {
  }
}
