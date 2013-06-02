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

import lombok.SneakyThrows;
import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.functional.Actions.Action0;
import propel.core.functional.Actions.Action1;
import propel.core.functional.Functions.Function0;
import propel.core.functional.Functions.Function1;
import propel.core.utils.ReflectionUtils;

/**
 * Some common, re-usable projections (miscellaneous)
 */
public final class MiscProjections
{
  /**
   * Identity function
   */
  public static Function1<Object, Object> argToResult()
  {
    return ARG_TO_RESULT;
  }
  private static final Function1<Object, Object> ARG_TO_RESULT = new Function1<Object, Object>() {
    @Override
    public Object apply(final Object element)
    {
      return element;
    }
  };

  /**
   * Identity function
   */
  @SuppressWarnings("unchecked")
  public static <T> Function1<T, T> argumentToResult()
  {
    return (Function1<T, T>) ARG_TO_RESULT;
  }

  /**
   * Returns an action with an empty body
   */
  public static Action0 empty()
  {
    return EMPTY;
  }
  private static final Action0 EMPTY = new Action0() {
    @Override
    public void apply()
    {
    }
  };

  /**
   * Returns a no-argument function with an empty body
   */
  public static Function0<Void> emptyFunc()
  {
    return EMPTY_FUNC;
  }
  private static final Function0<Void> EMPTY_FUNC = new Function0<Void>() {
    @Override
    public Void apply()
    {
      return null;
    }
  };

  /**
   * Action that throws the specified exception when being invoked. For the correct stack trace to be used, a new exception needs to be
   * created. As such, the action will attempt to create one and re-use the error message of the exception provided. For best results, the
   * exception class type should have a single-argument constructor accepting the exception message as argument. Alternatively a no-argument
   * constructor will be attempted to be instantiated. If both of these strategies fail, an Exception object is created and thrown,
   * containing the error message.
   * 
   * @throws Throwable When the action is called
   */
  @Validate
  public static Action0 throwException(@NotNull final Throwable _e)
  {
    return new Action0() {
      @Override
      @SneakyThrows
      public void apply()
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
    };
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
  @Validate
  public static Action1<Object> throwDetailed(@NotNull final Throwable _e)
  {
    return new Action1<Object>() {
      @Override
      @SneakyThrows
      public void apply(final Object element)
      {
        Throwable actual;
        try
        {
          actual = ReflectionUtils.activate(_e.getClass(), new Object[] {_e.getMessage() + element});
        }
        catch(Throwable e)
        {
          actual = new Exception(_e.getMessage() + element);
        }

        throw actual;
      }
    };
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
  @Validate
  public static Function0<Void> throwExceptionFunc(@NotNull final Throwable _e)
  {
    return new Function0<Void>() {
      @Override
      @SneakyThrows
      public Void apply()
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
    };
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
  @Validate
  public static Function1<Object, Void> throwDetailedFunc(@NotNull final Throwable _e)
  {
    return new Function1<Object, Void>() {
      @Override
      @SneakyThrows
      public Void apply(final Object element)
      {
        Throwable actual;
        try
        {
          actual = ReflectionUtils.activate(_e.getClass(), new Object[] {_e.getMessage() + element});
        }
        catch(Throwable e)
        {
          actual = new Exception(_e.getMessage() + element);
        }

        throw actual;
      }
     
    };
  }

  private MiscProjections()
  {
  }
}
