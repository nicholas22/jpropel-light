package propel.core.functional.projections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.Functions.Function0;
import lombok.SneakyThrows;
import lombok.Validate;
import lombok.Validate.NotNull;

public final class InvokeNoArgs
{

  /**
   * Returns a no-argument action which invokes the method specified on the given object.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static Function0<Void> invokeMethod(@NotNull final Object obj, @NotNull final Method method)
  {
    return new Function0<Void>() {

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
   * Returns a no-argument function which invokes the method name specified on the given object.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException There is no method found having the specified method name on the given object, or more than one
   *           methods have the specified name
   */
  @Validate
  public static <T> Function0<T> invokeFunction(@NotNull final Object obj, @NotNull final Method method)
  {
    return new Function0<T>() {

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


  private InvokeNoArgs()
  {

  }
}
