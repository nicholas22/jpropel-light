package propel.core.utils;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import lombok.Validate;
import lombok.Validate.NotNull;
import lombok.val;
import propel.core.common.CONSTANT;

/**
 * Class aiding in environment property manipulation
 */
public final class EnvironmentUtils
{
  /**
   * Returns the string value of the variable, or null if the variable is not defined in the system environment
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static String getEnv(@NotNull final String name)
  {
    return System.getenv(name);
  }

  /**
   * Overwrites an environment variable for this JVM, returning the old value
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static String setEnv(@NotNull final String name, String value)
  {
    val env = System.getenv();

    // property tree to replace existing one
    val myNewEnv = new TreeMap<String, String>();
    for (val entry : env.entrySet())
      myNewEnv.put(entry.getKey(), entry.getValue());

    // replace
    val oldValue = myNewEnv.put(name, value);
    setEnv(myNewEnv);

    // return old value
    return oldValue == null ? CONSTANT.EMPTY_STRING : oldValue;
  }

  /**
   * Replaces the environment variables map with the provided one
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Validate
  public static boolean setEnv(@NotNull final Map<String, String> newenv)
  {
    try
    {
      val processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
      val theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
      theEnvironmentField.setAccessible(true);
      val env = (Map<String, String>) theEnvironmentField.get(null);
      env.putAll(newenv);

      val theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
      theCaseInsensitiveEnvironmentField.setAccessible(true);
      val cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
      cienv.putAll(newenv);
      return true;
    }
    catch(Throwable e)
    {
      try
      {
        val classes = Collections.class.getDeclaredClasses();
        val env = System.getenv();
        for (val cl : classes)
          if ("java.util.Collections$UnmodifiableMap".equals(cl.getName()))
            for (val field : cl.getDeclaredFields())
              if (ReflectionUtils.isImplementing(field.getType(), Map.class))
              {
                field.setAccessible(true);
                val obj = field.get(env);
                val map = (Map<String, String>) obj;
                map.clear();
                map.putAll(newenv);
                return true;
              }
      }
      catch(Throwable ex)
      {
        // ignore, we return false
      }
    }

    return false;
  }
}
