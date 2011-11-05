package propel.core.functional.projections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.Functions.Function1;
import lombok.SneakyThrows;
import lombok.Validate;
import lombok.Validate.NotNull;

public final class InvokeOneArg
{

  /**
   * Returns a one-argument action which invokes the method specified on the given object, passing the function argument to the invoked
   * method.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static <T> Function1<Object, Void> invokeMethod(@NotNull final Object obj, @NotNull final Method method)
  {
    return new Function1<Object, Void>() {

      @SneakyThrows
      public Void apply(Object arg)
      {
        try
        {
          method.invoke(obj, arg);
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
   * Returns a one-argument function which invokes the method specified on the given object, passing the function argument to the invoked
   * method.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static <T> Function1<Object, T> invokeFunction(@NotNull final Object obj, @NotNull final Method method)
  {
    return new Function1<Object, T>() {

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

  private InvokeOneArg()
  {
  }
}
