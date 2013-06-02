package propel.core.functional.predicates;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.functional.Predicates.Predicate1;
import propel.core.utils.MemberInfo;
import propel.core.utils.PropertyInfo;
import propel.core.utils.ReflectionUtils;
import propel.core.utils.StringComparison;
import propel.core.utils.StringUtils;

/**
 * Some common, re-usable predicates for run-time reflection
 */
public final class Reflection
{
  /**
   * Predicate evaluating to true if given method is getter
   */
  public static Predicate1<Method> isGetter()
  {
    return IS_GETTER;
  }
  private static final Predicate1<Method> IS_GETTER = new Predicate1<Method>() {
    @Override
    public boolean evaluate(final Method element)
    {
      return ReflectionUtils.isGetter(element);
    }
  };

  /**
   * Predicate evaluating to true if given method is setter
   */
  public static Predicate1<Method> isSetter()
  {
    return IS_SETTER;
  }
  private static final Predicate1<Method> IS_SETTER = new Predicate1<Method>() {
    @Override
    public boolean evaluate(final Method element)
    {
      return ReflectionUtils.isSetter(element);
    }
  };

  /**
   * Predicate evaluating to true if given constructor equals the processed value
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Predicate1<Constructor<?>> constructorEqual(@NotNull final Constructor<?> _constructor)
  {
    return new Predicate1<Constructor<?>>() {
      @Override
      public boolean evaluate(final Constructor<?> element)
      {
        return ReflectionUtils.equal(element, _constructor);
      }
    };
  }

  /**
   * Predicate evaluating to true if given field equals the processed value
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Predicate1<Field> fieldEqual(@NotNull final Field _field)
  {
    return new Predicate1<Field>() {
      @Override
      public boolean evaluate(final Field element)
      {
        return ReflectionUtils.equal(element, _field);
      }
    };
  }

  /**
   * Predicate evaluating to true if given member equals the processed value
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Predicate1<MemberInfo> memberEqual(@NotNull final MemberInfo _member)
  {
    return new Predicate1<MemberInfo>() {
      @Override
      public boolean evaluate(final MemberInfo element)
      {
        return ReflectionUtils.equal(element, _member);
      }
    };
  }

  /**
   * Predicate evaluating to true if given method equals the processed value
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Predicate1<Method> methodEqual(@NotNull final Method _method)
  {
    return new Predicate1<Method>() {
      @Override
      public boolean evaluate(final Method element)
      {
        return ReflectionUtils.equal(element, _method);
      }
    };
  }

  /**
   * Predicate evaluating to true if given property equals the processed value
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Predicate1<PropertyInfo> propertyEqual(@NotNull final PropertyInfo _member)
  {
    return new Predicate1<PropertyInfo>() {
      @Override
      public boolean evaluate(final PropertyInfo element)
      {
        return ReflectionUtils.equal(element, _member);
      }
    };
  }

  /**
   * Predicate evaluating to true if a field name matches the given name
   * 
   * @throws NullPointerException An argument is null
   */
  public static Predicate1<Field> fieldNameEquals(final String _name)
  {
    return fieldNameEquals(_name, StringComparison.Ordinal);
  }

  /**
   * Predicate evaluating to true if a field name matches the given name
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Predicate1<Field> fieldNameEquals(@NotNull final String _name, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<Field>() {
      @Override
      public boolean evaluate(final Field element)
      {
        return StringUtils.equal(element.getName(), _name, _comparison);
      }
    };
  }

  /**
   * Predicate evaluating to true if a method name matches the given name
   * 
   * @throws NullPointerException An argument is null
   */
  public static Predicate1<MemberInfo> memberNameEquals(final String _name)
  {
    return memberNameEquals(_name, StringComparison.Ordinal);
  }

  /**
   * Predicate evaluating to true if a method name matches the given name
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Predicate1<MemberInfo> memberNameEquals(@NotNull final String _name, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<MemberInfo>() {
      @Override
      public boolean evaluate(final MemberInfo element)
      {
        return StringUtils.equal(element.getName(), _name, _comparison);
      }
    };
  }

  /**
   * Predicate evaluating to true if a method name matches the given name
   * 
   * @throws NullPointerException An argument is null
   */
  public static Predicate1<Method> methodNameEquals(final String _name)
  {
    return methodNameEquals(_name, StringComparison.Ordinal);
  }

  /**
   * Predicate evaluating to true if a method name matches the given name
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Predicate1<Method> methodNameEquals(@NotNull final String _name, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<Method>() {
      @Override
      public boolean evaluate(final Method element)
      {
        return StringUtils.equal(element.getName(), _name, _comparison);
      }
    };
  }

  /**
   * Predicate evaluating to true if a method name matches the given name
   * 
   * @throws NullPointerException An argument is null
   */
  public static Predicate1<PropertyInfo> propertyNameEquals(final String _name)
  {
    return propertyNameEquals(_name, StringComparison.Ordinal);
  }

  /**
   * Predicate evaluating to true if a property name matches the given name
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Predicate1<PropertyInfo> propertyNameEquals(@NotNull final String _name, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<PropertyInfo>() {
      @Override
      public boolean evaluate(final PropertyInfo element)
      {
        return element.getName().equals(_name);
      }
    };
  }

  /**
   * Predicate that returns true if the function argument is a subclass of the class specified
   * 
   * @throws NullPointerException When an argument is null
   * @throws IllegalArgumentException When a non-class (e.g. interface) was provided
   */
  public static <T> Predicate1<T> isExtending(final Class<?> _class)
  {
    return Objects.isExtending(_class);
  }

  /**
   * Predicate that returns true if the function argument is an instance of the interface specified
   * 
   * @throws NullPointerException When an argument is null
   * @throws IllegalArgumentException When a non-interface (e.g. class) was provided
   */
  public static <T> Predicate1<T> isImplementing(final Class<?> _class)
  {
    return Objects.isImplementing(_class);
  }

  /**
   * Predicate that returns true if the function argument is not a subclass of the class specified
   * 
   * @throws NullPointerException When an argument is null
   * @throws IllegalArgumentException When a non-class (e.g. interface) was provided
   */
  public static <T> Predicate1<T> isNotExtending(final Class<?> _class)
  {
    return Objects.isNotExtending(_class);
  }

  /**
   * Predicate that returns true if the function argument is not an instance of the interface specified
   * 
   * @throws NullPointerException When an argument is null
   * @throws IllegalArgumentException When a non-interface (e.g. class) was provided
   */
  public static <T> Predicate1<T> isNotImplementing(final Class<?> _class)
  {
    return Objects.isNotImplementing(_class);
  }

  /**
   * Predicate that returns true if the function argument is an instance of the class specified
   * 
   * @throws NullPointerException When an argument is null
   */
  public static <T> Predicate1<T> instanceOf(final Class<?> _class)
  {
    return Objects.instanceOf(_class);
  }

  private Reflection()
  {
  };
}
