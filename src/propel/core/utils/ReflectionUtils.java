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
package propel.core.utils;

import static propel.core.functional.predicates.Reflection.fieldEqual;
import static propel.core.functional.predicates.Reflection.fieldNameEquals;
import static propel.core.functional.predicates.Reflection.memberEqual;
import static propel.core.functional.predicates.Reflection.memberNameEquals;
import static propel.core.functional.predicates.Reflection.methodEqual;
import static propel.core.functional.predicates.Reflection.methodNameEquals;
import static propel.core.functional.predicates.Reflection.propertyEqual;
import static propel.core.functional.predicates.Reflection.propertyNameEquals;
import static propel.core.functional.projections.Objects.toStringify;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarFile;
import lombok.Validate;
import lombok.Validate.NotNull;
import lombok.val;
import propel.core.collections.KeyValuePair;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.maps.avl.AvlHashtable;
import propel.core.collections.sets.AvlTreeSet;
import propel.core.common.CONSTANT;
import propel.core.functional.Functions.Function1;
import propel.core.functional.Predicates.Predicate1;
import propel.core.functional.predicates.Reflection;

/**
 * Provides utility functionality for reflective manipulations
 */
public final class ReflectionUtils
{
  /**
   * Activates an object from the type.
   * 
   * @throws NullPointerException An argument is null.
   * @throws InstantiationException When instantiation fails.
   * @throws IllegalAccessException When a member is illegally accessed.
   */
  @Validate
  public static <T> T activate(@NotNull final Class<T> clazz)
      throws InstantiationException, IllegalAccessException
  {
    return clazz.newInstance();
  }

  /**
   * Activates an object from the class given, using the specified constructor arguments.
   * 
   * @throws NullPointerException The class argument is null.
   * @throws InstantiationException When instantiation fails.
   * @throws IllegalAccessException When a member is illegally accessed.
   * @throws InvocationTargetException When the constructor called throws an exception.
   * @throws IllegalArgumentException When no constructor accepting this many arguments is found, or there are more than 1 constructors
   *           found accepting the arguments given.
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> T activate(@NotNull final Class<T> clazz, @NotNull final Object[] constructorArgs)
      throws InstantiationException, IllegalAccessException, InvocationTargetException
  {
    if (constructorArgs == null || constructorArgs.length == 0)
      return activate(clazz);
    val constructorArgsLength = constructorArgs.length;

    // find relevant constructors
    val constructors = Linq.where(clazz.getConstructors(), constructorParametersEqual(constructorArgsLength));
    if (constructors.length <= 0)
      throw new IllegalArgumentException("A constructor with " + constructorArgsLength + " arguments was not found: " + clazz.getName());

    val constructor = constructors.length == 1 ? constructors[0] : matchConstructor(constructors, constructorArgs);

    return (T) constructor.newInstance(constructorArgs);
  }

  private static Predicate1<Constructor<?>> constructorParametersEqual(final int _len)
  {
    return new Predicate1<Constructor<?>>() {
      @Override
      public boolean evaluate(final Constructor<?> element)
      {
        return element.getParameterTypes().length == _len;
      }
    };
  }

  private static Constructor<?> matchConstructor(Constructor<?>[] constructors, Object[] constructorArgs)
  {
    // get constructor argument types
    val argTypes = Linq.select(constructorArgs, getClassIfNotNull);

    // not found, throw ambiguous call exception
    for (val constructor : constructors)
    {
      // get parameter types
      val parameterTypes = constructor.getParameterTypes();

      // combine into one class
      val list = new ArrayList<KeyValuePair<Class<?>, Class<?>>>();
      for (int i = 0; i < parameterTypes.length; i++)
        list.add(new KeyValuePair<Class<?>, Class<?>>(argTypes[i], parameterTypes[i]));

      // see if all args types are assignable to constructor parameter types
      if (Linq.all(list, isParameterAssignable))
        return constructor;
    }

    // failed to find match, log an informative message
    val constructorSignatures = Linq.select(constructors, toStringify());
    val argTypeNames = Linq.select(argTypes, toStringify());

    throw new IllegalArgumentException("There are " + constructorSignatures.length + " constructors ("
        + StringUtils.delimit(constructorSignatures, CONSTANT.COMMA) + ") accepting the arguments given: "
        + StringUtils.delimit(argTypeNames, CONSTANT.COMMA));
  }

  private static Predicate1<KeyValuePair<Class<?>, Class<?>>> isParameterAssignable = new Predicate1<KeyValuePair<Class<?>, Class<?>>>() {
    @Override
    public boolean evaluate(final KeyValuePair<Class<?>, Class<?>> element)
    {
      Class<?> argType = element.getKey();
      Class<?> parameterType = element.getValue();

      // check if assignable
      if (parameterType.isAssignableFrom(argType))
        return true;

      return false;
    }
  };

  private static Function1<Object, Class<?>> getClassIfNotNull = new Function1<Object, Class<?>>() {
    @Override
    public Class<?> apply(final Object element)
    {
      return element != null ? element.getClass() : null;
    }
  };

  /**
   * Activates an object from the class name given.
   * 
   * @throws InstantiationException When instantiation fails.
   * @throws IllegalAccessException When a member is illegally accessed.
   * @throws InvocationTargetException When the constructor called throws an exception.
   * @throws ClassNotFoundException The specified class name does not correspond to a class.
   */
  @Validate
  public static Object activate(@NotNull final String className)
      throws InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException
  {
    return activate(className, new Object[0]);
  }

  /**
   * Activates an object from the class name given.
   * 
   * @throws InstantiationException When instantiation fails.
   * @throws IllegalAccessException When a member is illegally accessed.
   * @throws InvocationTargetException When the constructor called throws an exception.
   * @throws ClassNotFoundException The specified class name does not correspond to a class.
   */
  @Validate
  public static Object activate(@NotNull final String className, Object[] constructorArgs)
      throws InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException
  {
    Class<?> clazz = parse(className);
    return activate(clazz, constructorArgs);
  }

  /**
   * Compares two Method classes and returns true if they appear to be referring to the same signature of a method.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean equal(@NotNull final Method methodA, @NotNull final Method methodB)
  {
    if (methodA.getName().equals(methodB.getName()))
      if (methodA.getReturnType().equals(methodB.getReturnType()))
      {
        val firstMethodArgs = methodA.getParameterTypes();
        val secondMethodArgs = methodB.getParameterTypes();

        if (Linq.sequenceEqual(firstMethodArgs, secondMethodArgs))
          return true;
      }

    return false;
  }

  /**
   * Compares two Field classes and returns true if they appear to be referring to the same signature of a field.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean equal(@NotNull final Field fieldA, @NotNull final Field fieldB)
  {
    if (fieldA.getName().equals(fieldB.getName()))
      if (fieldA.getType().equals(fieldB.getType()))
        return true;

    return false;
  }

  /**
   * Compares two Constructors and returns true if they appear to be referring to the same constructor.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean equal(@NotNull final Constructor<?> constrA, @NotNull final Constructor<?> constrB)
  {
    if (constrA.getName().equals(constrB.getName()))
      if (constrA.getDeclaringClass().equals(constrB.getDeclaringClass()))
        return true;

    return false;
  }

  /**
   * Compares two class members and returns true if they appear to be referring to the same member.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean equal(@NotNull final MemberInfo membA, @NotNull final MemberInfo membB)
  {
    // check if of the same backing member type
    if ((membA.isField() & membB.isField()) != true)
      if ((membA.isMethod() & membB.isMethod()) != true)
        if ((membA.isProperty() & membB.isProperty()) != true)
          if (membA.isConstructor() & membB.isConstructor() != true)
            return false;

    if (membA.isField())
      return equal(membA.getFieldInfo(), membB.getFieldInfo());
    if (membA.isMethod())
      return equal(membA.getMethodInfo(), membB.getMethodInfo());
    if (membA.isProperty())
      return equal(membA.getPropertyInfo(), membB.getPropertyInfo());
    if (membA.isConstructor())
      return equal(membA.getConstructorInfo(), membB.getConstructorInfo());
    return false;
  }

  /**
   * Compares two Method classes and returns true if they appear to be referring to the same signature of a property.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean equal(@NotNull final PropertyInfo propA, @NotNull final PropertyInfo propB)
  {
    if (propA.getName().equals(propB.getName()))
      if (propA.getPropertyType().equals(propB.getPropertyType()))
      {
        // compare getters
        val getterA = propA.getGetter();
        val getterB = propB.getGetter();

        boolean gettersEqual = false;
        if (getterA == null && getterB == null)
          gettersEqual = true;
        if (getterA != null && getterB != null)
          gettersEqual = equal(getterA, getterB);
        if (getterA == null || getterB == null)
          gettersEqual = false;

        if (!gettersEqual)
          return false;

        // compare setters
        val setterA = propA.getSetter();
        val setterB = propB.getSetter();

        boolean settersEqual = false;
        if (setterA == null && setterB == null)
          settersEqual = true;
        if (setterA != null && setterB != null)
          settersEqual = equal(setterA, setterB);
        if (setterA == null || setterB == null)
          settersEqual = false;

        if (!settersEqual)
          return false;

        return true;
      }

    return false;
  }

  /**
   * Returns the stack-trace element above the one of this method. This allows to determine the calling class, method name, etc.
   */
  public static StackTraceElement getCallingStackTraceElement()
  {
    return (new Throwable()).fillInStackTrace().getStackTrace()[1];
  }

  /**
   * Lists all classes inside a package.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException A package is invalid.
   * @throws ClassNotFoundException When finding a class from a class name fails while enumerating.
   */
  @Validate
  public static List<Class<?>> getClasses(@NotNull final Package pkg)
      throws ClassNotFoundException
  {
    val result = new ArrayList<Class<?>>();

    // Get a File object for the package
    File directory = null;
    String fullPath = null;
    val pkgName = pkg.getName();
    String relPath = pkgName.replace(CONSTANT.DOT_CHAR, CONSTANT.FORWARD_SLASH_CHAR);
    URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);

    if (resource == null)
    {
      // try the other slashes
      relPath = pkgName.replace(CONSTANT.DOT_CHAR, CONSTANT.BACK_SLASH_CHAR);
      resource = ClassLoader.getSystemClassLoader().getResource(relPath);

      // not found
      if (resource == null)
        throw new IllegalArgumentException("No resource found for " + relPath);
    }

    fullPath = resource.getFile();

    // un-escape to locate locally
    val normalizedFullPath = EscapingUtils.fromUrl(fullPath);
    directory = new File(normalizedFullPath);
    val classExt = ".class";

    if (directory.exists())
    {
      // Get the list of the files contained in the package
      val files = directory.list();
      for (int i = 0; i < files.length; i++)
        // we are only interested in .class files
        if (StringUtils.match(files[i], MatchType.EndsWith, classExt, StringComparison.Ordinal))
        {
          // removes the .class extension
          val className = pkgName + CONSTANT.DOT_CHAR + files[i].substring(0, files[i].length() - classExt.length());

          try
          {
            result.add(Class.forName(className));
          }
          catch(NoClassDefFoundError e)
          {
            // no class definition present
          }
          catch(ClassNotFoundException e)
          {
            throw new ClassNotFoundException("Could not find class '" + className + "' in package '" + pkgName + "'.");
          }
        }
    } else
    {
      // directory does not exist, find JAR file
      val jarpath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", CONSTANT.EMPTY_STRING);

      // un-escape to locate locally
      val normalizedJarPath = EscapingUtils.fromUrl(jarpath);
      try
      {
        // enumerate entries
        val jarFile = new JarFile(normalizedJarPath);
        val entries = jarFile.entries();
        while (entries.hasMoreElements())
        {
          // get next entry
          val entry = entries.nextElement();
          val entryName = entry.getName();

          // must be a .class entry, starting with the package name
          if (entryName.endsWith(classExt) && entryName.startsWith(relPath))
          {
            // must be in the same package
            val className = entryName.replace(CONSTANT.FORWARD_SLASH_CHAR, CONSTANT.DOT_CHAR)
                .replace(CONSTANT.BACK_SLASH_CHAR, CONSTANT.DOT_CHAR).replace(classExt, CONSTANT.EMPTY_STRING);

            // attempt to get the class by its name
            try
            {
              val clazz = Class.forName(className);
              if (clazz.getPackage().equals(pkg))
                result.add(clazz);
            }
            catch(NoClassDefFoundError e)
            {
              // no class definition present
            }
            catch(ClassNotFoundException e)
            {
              throw new ClassNotFoundException("Could not find class '" + className + "' in JAR file '" + normalizedJarPath + "'.");
            }
          }
        }
      }
      catch(IOException e)
      {
        throw new IllegalArgumentException("Package " + pkgName + " (for JAR " + normalizedJarPath + ") does not appear to be valid.", e);
      }
    }
    return result;
  }

  /**
   * Returns all annotations from the annotated class (and its subclasses if required)
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static <T extends Annotation> List<T> getAnnotations(@NotNull final Class<?> annotatedClass,
                                                              @NotNull final Class<T> annotationType, boolean includeInherited)
  {
    val result = new ArrayList<T>(16);

    // get derived class annotations
    List<Annotation> annotations = ArrayUtils.toList(annotatedClass.getDeclaredAnnotations());
    result.addAll(Linq.toList(Linq.ofType(annotations, annotationType)));

    if (includeInherited)
    {
      // get all annotations from base classes and implemented interfaces
      Class<?> superClass = annotatedClass;

      while ((superClass = superClass.getSuperclass()) != null)
      {
        // get super class annotations
        annotations = ArrayUtils.toList(superClass.getDeclaredAnnotations());
        result.addAll(Linq.toList(Linq.ofType(annotations, annotationType)));
      }

      // now get all the superinterfaces' annotations
      for (val iface : annotatedClass.getInterfaces())
      {
        annotations = ArrayUtils.toList(iface.getDeclaredAnnotations());
        result.addAll(Linq.toList(Linq.ofType(annotations, annotationType)));
      }
    }

    return result;
  }

  /**
   * Returns all annotations from the annotated class (and its subclasses if required)
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static List<Annotation> getAnnotations(@NotNull final Class<?> annotatedClass, boolean includeInherited)
  {
    val result = new ArrayList<Annotation>(16);

    // get derived class annotations
    result.addAll(ArrayUtils.toList(annotatedClass.getDeclaredAnnotations()));

    if (includeInherited)
    {
      // get all annotations from base classes and implemented interfaces
      Class<?> superClass = annotatedClass;

      while ((superClass = superClass.getSuperclass()) != null)
        // get super class annotations
        result.addAll(ArrayUtils.toList(superClass.getDeclaredAnnotations()));

      // now get all the superinterfaces' annotations
      for (val iface : annotatedClass.getInterfaces())
        result.addAll(ArrayUtils.toList(iface.getDeclaredAnnotations()));
    }

    return result;
  }

  /**
   * Returns all annotations from the annotated method which are of specified annotation class type (or any its subclasses, if required)
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static List<Annotation> getMethodAnnotations(@NotNull final Method annotatedMethod, boolean includeInherited)
  {
    val result = new ArrayList<Annotation>(16);

    // get method annotations
    result.addAll(ArrayUtils.toList(annotatedMethod.getDeclaredAnnotations()));

    if (includeInherited)
    {
      // get all annotations from base classes and implemented interfaces
      Class<?> superClass = annotatedMethod.getDeclaringClass();

      while ((superClass = superClass.getSuperclass()) != null)
      {
        // get super class method annotations
        Method superClassMethod = findDeclaredMethod(superClass, annotatedMethod);
        if (superClassMethod != null)
          result.addAll(ArrayUtils.toList(superClassMethod.getDeclaredAnnotations()));
      }

      // now get all the super-interfaces' annotations
      for (val iface : annotatedMethod.getDeclaringClass().getInterfaces())
      {
        val ifaceMethod = findDeclaredMethod(iface, annotatedMethod);
        if (ifaceMethod != null)
          result.addAll(ArrayUtils.toList(ifaceMethod.getDeclaredAnnotations()));
      }

    }

    return result;
  }

  /**
   * Returns all annotations from the annotated method which are of specified annotation class type (or any its subclasses, if required)
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static <T extends Annotation> List<T> getMethodAnnotations(@NotNull final Method annotatedMethod,
                                                                    @NotNull final Class<T> annotationType, boolean includeInherited)
  {
    val result = new ArrayList<T>(16);

    // get method annotations
    List<Annotation> annotations = ArrayUtils.toList(annotatedMethod.getDeclaredAnnotations());
    result.addAll(Linq.toList(Linq.ofType(annotations, annotationType)));

    if (includeInherited)
    {
      // get all annotations from base classes and implemented interfaces
      Class<?> superClass = annotatedMethod.getDeclaringClass();

      while ((superClass = superClass.getSuperclass()) != null)
      {
        // get super class method annotations
        val superClassMethod = findDeclaredMethod(superClass, annotatedMethod);
        if (superClassMethod != null)
        {
          annotations = ArrayUtils.toList(superClassMethod.getDeclaredAnnotations());
          result.addAll(Linq.toList(Linq.ofType(annotations, annotationType)));
        }
      }

      // now get all the super-interfaces' annotations
      for (val iface : annotatedMethod.getDeclaringClass().getInterfaces())
      {
        val ifaceMethod = findDeclaredMethod(iface, annotatedMethod);
        if (ifaceMethod != null)
        {
          annotations = ArrayUtils.toList(ifaceMethod.getDeclaredAnnotations());
          result.addAll(Linq.toList(Linq.ofType(annotations, annotationType)));
        }
      }

    }

    return result;
  }

  /**
   * Returns all interfaces of a type, except the specified as a generic argument one (T).
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static Class<?>[] getInterfaces(@NotNull final Class<?> type, boolean includeInherited)
  {
    val result = new HashSet<Class<?>>();
    val typeStack = new LinkedList<Class<?>>();

    // populate initially with type's implementing interfaces
    for (val iface : type.getInterfaces())
      if (!result.contains(iface))
      {
        typeStack.push(iface);
        result.add(iface);
      }

    if (includeInherited)
    {
      // populate with super-classes' interfaces
      Class<?> superClass = type;
      while ((superClass = superClass.getSuperclass()) != null)
        for (Class<?> iface : superClass.getInterfaces())
          if (!result.contains(iface))
          {
            typeStack.push(iface);
            result.add(iface);
          }

      // iterate through all added interfaces to find any further interfaces
      while (typeStack.size() > 0)
      {
        // get next and check
        Class<?> iType = typeStack.pop();
        for (Class<?> iface : iType.getInterfaces())
          if (!result.contains(iface))
          {
            typeStack.add(iface);
            result.add(iface);
          }
      }
    }

    return result.toArray(new Class<?>[result.size()]);
  }

  /**
   * Returns all interfaces of a type, except the specified as a generic argument one (T). Note that this does not return inherited
   * interfaces.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static Class<?>[] getInterfacesExcept(@NotNull final Class<?> type, @NotNull final Class<?> excludedInterface)
  {
    // get all interfaces
    val interfaces = new ReifiedArrayList<Class<?>>(16, Class.class);

    // exclude the specified one
    for (val iface : type.getInterfaces())
      if (!iface.equals(excludedInterface))
        interfaces.add(iface);

    return interfaces.toArray();
  }

  /**
   * Gets all constructors found in a type. Optionally returns superclass constructors too, rather than just declared ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  @Validate
  public static Constructor<?>[] getConstructors(@NotNull final Class<?> type, boolean includeSuperClass)
  {
    val result = new ReifiedArrayList<Constructor<?>>() {};

    for (val constr : type.getDeclaredConstructors())
      result.add(constr);

    if (includeSuperClass)
    {
      Class<?> superClass = type;
      while ((superClass = superClass.getSuperclass()) != null)
        result.addAll(Arrays.asList(superClass.getDeclaredConstructors()));
    }

    return result.toArray();
  }

  /**
   * Returns null if a type has no default no-argument constructor, otherwise returns the constructor.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  @Validate
  public static Constructor<?> getConstructorDefault(@NotNull final Class<?> type)
  {
    // iterate constructors
    for (val constructor : type.getConstructors())
      if (constructor.getParameterTypes().length == 0)
        return constructor;

    return null;
  }

  /**
   * Gets all fields found in a type. Optionally returns inherited ones too, rather than just declared ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  @Validate
  public static Field[] getFields(@NotNull final Class<?> type, boolean includeInherited)
  {
    val result = new ReifiedArrayList<Field>(Field.class);

    for (val field : type.getDeclaredFields())
      result.add(field);

    if (includeInherited)
    {
      Class<?> superClass = type;
      while ((superClass = superClass.getSuperclass()) != null)
        result.addAll(Arrays.asList(superClass.getDeclaredFields()));
    }

    return result.toArray();
  }

  /**
   * Returns all fields found in a class type matching the given name. Optionally returns inherited ones too, rather than just declared
   * ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  public static Field[] getFields(final Class<?> type, final String name, boolean includeInherited)
  {
    return Linq.where(getFields(type, includeInherited), fieldNameEquals(name));
  }

  /**
   * Returns the constructor information for an object's property, if found, otherwise null.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  @Validate
  public static Field getField(@NotNull final Class<?> type, @NotNull final String name, boolean includeInherited)
  {
    return Linq.firstOrDefault(getFields(type, includeInherited), fieldNameEquals(name));
  }

  /**
   * Gets all properties found in a type. Optionally returns inherited ones too, rather than just declared ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  @Validate
  public static PropertyInfo[] getProperties(@NotNull final Class<?> type, boolean includeInherited)
  {
    val result = new ReifiedArrayList<PropertyInfo>(PropertyInfo.class);
    val methods = new AvlHashtable<String, Method>(String.class, Method.class);

    // get declared method names
    for (val method : type.getDeclaredMethods())
    {
      val methodName = method.getName();

      if (isGetter(method) || isSetter(method))
        if (!methods.containsKey(methodName))
          methods.add(method.getName(), method);
    }

    // get inherited, if required
    if (includeInherited)
    {
      Class<?> superClass = type;
      while ((superClass = superClass.getSuperclass()) != null)
        for (val method : superClass.getDeclaredMethods())
        {
          String methodName = method.getName();

          if (!methods.containsKey(methodName))
            if (isGetter(method) || isSetter(method))
              methods.add(method.getName(), method);
        }
    }

    // find which methods exist in pairs of getters/setters
    val propertyNames = new AvlTreeSet<String>(String.class);
    for (String methodName : methods.getKeys())
    {
      Method getter;
      Method setter;

      // skip "get" or "set" to identify property name
      String propertyName = methodName.substring(3);
      if (!propertyNames.contains(propertyName))
      {

        // find getter if we have setter and vice versa
        if (methodName.startsWith("get"))
        {
          getter = methods.get(methodName);
          setter = methods.containsKey("set" + propertyName) ? methods.get("set" + propertyName) : null;
        } else
        {
          setter = methods.get(methodName);
          getter = methods.containsKey("get" + propertyName) ? methods.get("get" + propertyName) : null;
        }

        // skip if both are not available
        if (getter != null && setter != null)
          // check that the getter returns the setter's first argument type
          if (getter.getReturnType().equals(setter.getParameterTypes()[0]))
            // add if not already found
            if (propertyNames.add(propertyName))
            {
              PropertyInfo pi = new PropertyInfo(type, propertyName, getter, setter, getter.getReturnType());
              result.add(pi);
            }
      }
    }

    return result.toArray();
  }

  /**
   * Returns all properties found in a class type matching the given name. Optionally returns inherited ones too, rather than just declared
   * ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  @Validate
  public static PropertyInfo[] getProperties(Class<?> type, final String name, boolean includeInherited)
  {
    return Linq.where(getProperties(type, includeInherited), propertyNameEquals(name));
  }

  /**
   * Returns the property information for an object's property, if found, otherwise null.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  @Validate
  public static PropertyInfo getProperty(@NotNull final Class<?> type, @NotNull final String name, boolean includeInherited)
  {
    return Linq.firstOrDefault(getProperties(type, includeInherited), propertyNameEquals(name));
  }

  /**
   * Returns all getters found in a class type. Optionally returns inherited ones too, rather than just declared ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  public static Method[] getGetters(Class<?> type, boolean includeInherited)
  {
    val methods = getMethods(type, includeInherited);
    return Linq.where(methods, Reflection.isGetter());
  }

  /**
   * Returns the first encountered getter with the given name, or null if not found
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  public static Method getGetter(Class<?> type, String name, boolean includeInherited)
  {
    return Linq.firstOrDefault(Linq.where(getGetters(type, includeInherited), methodNameEquals(name)));
  }

  /**
   * Returns all setters found in a class type. Optionally returns inherited ones too, rather than just declared ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  public static Method[] getSetters(Class<?> type, boolean includeInherited)
  {
    val methods = getMethods(type, includeInherited);
    return Linq.where(methods, Reflection.isSetter());
  }

  /**
   * Returns the first encountered setter with the given name, or null if not found
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  public static Method getSetter(Class<?> type, String name, boolean includeInherited)
  {
    return Linq.firstOrDefault(Linq.where(getSetters(type, includeInherited), methodNameEquals(name)));
  }

  /**
   * Returns all methods found in a class type. Optionally returns inherited ones too, rather than just declared ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  @Validate
  public static Method[] getMethods(@NotNull final Class<?> type, boolean includeInherited)
  {
    val result = new ReifiedArrayList<Method>(Method.class);

    for (Method method : type.getDeclaredMethods())
      result.add(method);

    if (includeInherited)
    {
      Class<?> superClass = type;
      while ((superClass = superClass.getSuperclass()) != null)
        for (Method method : superClass.getDeclaredMethods())
          result.add(method);
    }

    return result.toArray();
  }

  /**
   * Returns all methods found in a class type matching the given name. Optionally returns inherited ones too, rather than just declared
   * ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  public static Method[] getMethods(Class<?> type, final String name, boolean includeInherited)
  {
    return Linq.where(getMethods(type, includeInherited), methodNameEquals(name));
  }

  /**
   * Returns the method information for an object's method, if found, otherwise null.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  @Validate
  public static Method getMethod(@NotNull final Class<?> type, @NotNull final String name, boolean includeInherited)
  {
    return Linq.firstOrDefault(getMethods(type, includeInherited), methodNameEquals(name));
  }

  /**
   * Returns all methods found in a class type. Optionally returns inherited ones too, rather than just declared ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  @Validate
  public static MemberInfo[] getMembers(@NotNull final Class<?> type, boolean includeInherited)
  {
    val result = new ReifiedArrayList<MemberInfo>(MemberInfo.class);

    // get fields
    result.addAll(Arrays.asList(Linq.select(getFields(type, includeInherited), fieldToMemberInfo())));
    // get methods
    result.addAll(Arrays.asList(Linq.select(getMethods(type, includeInherited), methodToMemberInfo())));
    // get properties
    result.addAll(Arrays.asList(Linq.select(getProperties(type, includeInherited), propertyToMemberInfo())));
    // get constructors
    result.addAll(Arrays.asList(Linq.select(getConstructors(type, includeInherited), constructorToMemberInfo())));

    return result.toArray();
  }

  private static Function1<Constructor<?>, MemberInfo> constructorToMemberInfo()
  {
    return new Function1<Constructor<?>, MemberInfo>() {
      @Override
      public MemberInfo apply(final Constructor<?> element)
      {
        return new MemberInfo(element);
      }
    };
  }

  private static Function1<PropertyInfo, MemberInfo> propertyToMemberInfo()
  {
    return new Function1<PropertyInfo, MemberInfo>() {
      @Override
      public MemberInfo apply(final PropertyInfo element)
      {
        return new MemberInfo(element);
      }
    };
  }

  private static Function1<Method, MemberInfo> methodToMemberInfo()
  {
    return new Function1<Method, MemberInfo>() {
      @Override
      public MemberInfo apply(final Method element)
      {
        return new MemberInfo(element);
      }
    };
  }

  private static Function1<Field, MemberInfo> fieldToMemberInfo()
  {
    return new Function1<Field, MemberInfo>() {
      @Override
      public MemberInfo apply(final Field element)
      {
        return new MemberInfo(element);
      }
    };
  }

  /**
   * Returns all members found in a class type matching the given name. Optionally returns inherited ones too, rather than just declared
   * ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  public static MemberInfo[] getMembers(Class<?> type, final String name, boolean includeInherited)
  {
    return Linq.where(getMembers(type, includeInherited), memberNameEquals(name));
  }

  /**
   * Returns the property information for an object's property, if found, otherwise null.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  @Validate
  public static MemberInfo getMember(@NotNull final Class<?> type, @NotNull final String name, boolean includeInherited)
  {
    return Linq.firstOrDefault(getMembers(type, includeInherited), memberNameEquals(name));
  }

  /**
   * Returns true if a method appears to be part of a type.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static boolean hasMethod(@NotNull final Class<?> type, @NotNull final Method method, boolean includeInherited)
  {
    return Linq.firstOrDefault(getMethods(type, includeInherited), methodEqual(method)) != null;
  }

  /**
   * Returns true if a method appears to be part of a type.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static boolean hasProperty(@NotNull final Class<?> type, @NotNull final PropertyInfo property, boolean includeInherited)
  {
    return Linq.firstOrDefault(getProperties(type, includeInherited), propertyEqual(property)) != null;
  }

  /**
   * Returns true if a field appears to be part of a type.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static boolean hasField(@NotNull final Class<?> type, @NotNull final Field field, boolean includeInherited)
  {
    return Linq.firstOrDefault(getFields(type, includeInherited), fieldEqual(field)) != null;
  }

  /**
   * Returns true if a member appears to be part of a type.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static boolean hasMember(@NotNull final Class<?> type, @NotNull final MemberInfo member, boolean includeInherited)
  {
    return Linq.firstOrDefault(getMembers(type, includeInherited), memberEqual(member)) != null;
  }

  /**
   * Returns true if a class is generic
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean isGeneric(@NotNull final Class<?> type)
  {
    return type.getTypeParameters().length > 0;
  }

  /**
   * Returns true if the method appears to be a POJO getter.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean isGetter(@NotNull final Method method)
  {
    val mod = method.getModifiers();
    if (Modifier.isAbstract(mod) || Modifier.isFinal(mod) || Modifier.isNative(mod) || Modifier.isStatic(mod))
      return false;

    val methodName = method.getName();
    // bean syntax for getters
    if (((methodName.length() >= 4) && methodName.startsWith("get")) || ((methodName.length() >= 3) && methodName.startsWith("is")))
      // must have non-void return type and no arguments
      if (!isReturnTypeVoid(method))
        if (method.getParameterTypes().length == 0)
          return true;

    return false;
  }

  /**
   * Returns true if the method return type is void.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean isReturnTypeVoid(@NotNull final Method method)
  {
    return method.getReturnType() == void.class || method.getReturnType() == Void.class;
  }

  /**
   * Returns true if the method appears to be a POJO setter.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean isSetter(@NotNull final Method method)
  {
    val mod = method.getModifiers();
    if (Modifier.isAbstract(mod) || Modifier.isFinal(mod) || Modifier.isNative(mod) || Modifier.isStatic(mod))
      return false;

    val methodName = method.getName();
    if (methodName.length() >= 4)
      // bean syntax for setters
      if (methodName.startsWith("set"))
        // must have void return type and 1 argument
        if (isReturnTypeVoid(method))
          if (method.getParameterTypes().length == 1)
            return true;

    return false;
  }

  /**
   * Inject a value to a property of an object. You may specify whether to include inherited properties in the search of the property name.
   * 
   * @throws NullPointerException The object or property name is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  @Validate
  public static void injectProperty(@NotNull final Object obj, @NotNull final String propertyName, Object propertyValue,
                                    boolean includeInherited)
  {
    // find property
    val pi = getProperty(obj.getClass(), propertyName, includeInherited);
    if (pi == null)
      throw new IllegalArgumentException("The object of type " + obj.getClass().getName() + " does not have a property named '"
          + propertyName + "'");

    // obtain setter and inject value
    try
    {
      pi.getSetter().invoke(obj, propertyValue);
    }
    catch(Throwable e)
    {
      throw new IllegalArgumentException("Could not inject property value on object of type " + obj.getClass().getName() + " and property "
          + propertyName, e);
    }
  }

  /**
   * Returns true if any of the type's base types/interfaces are equal to the given type, or if the two types are the same. The method scans
   * up until the root of the object hierarchy.
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static boolean instanceOf(@NotNull final Class<?> type, @NotNull final Class<?> baseClassOrInterfaceType)
  {
    if (baseClassOrInterfaceType.isInterface())
      return isImplementing(type, baseClassOrInterfaceType);
    else
      return isExtending(type, baseClassOrInterfaceType);
  }

  /**
   * Returns true if any of the type's BaseTypes are equal to the given type or if the two types are the same. The method scans up until the
   * root of the object hierarchy. Only classes are checked (no interfaces).
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static boolean isExtending(@NotNull Class<?> type, @NotNull final Class<?> baseClassType)
  {
    if (baseClassType.isInterface())
      throw new IllegalArgumentException("The type '" + baseClassType.getName() + "' is an interface.");

    if (type.equals(baseClassType))
      return true;

    Class<?> superClass = type;
    while ((superClass = superClass.getSuperclass()) != null)
    {
      // check
      if (type.getSuperclass().equals(baseClassType))
        return true;

      // move to next
      type = type.getSuperclass();
    }

    return false;
  }

  /**
   * Returns true if the given type appears to look like a given interface (or other class). The method does not check for inheritance
   * relationships. Rather it checks if duck typing would be possible (i.e. if it looks like a duck and quacks like a duck, then it is a
   * duck). This is useful for checking if an annotation "implements" some interface, because in Java annotations cannot extend or implement
   * other types. Note: even if this returns true, it does not mean that you can perform casting. You may specify whether all or only
   * declared methods are to be checked.
   */
  @Validate
  public static boolean isLookingLike(@NotNull final Class<?> type, @NotNull final Class<?> interfaceType, boolean includeTypeInherited,
                                      boolean includeInterfaceTypeInherited)
  {
    // get methods we are looking for
    val methods = getMethods(interfaceType, includeInterfaceTypeInherited);

    // must find all these methods in the given type
    return Linq.all(methods, methodHasMethod(type, includeTypeInherited));
  }

  private static Predicate1<Method> methodHasMethod(final Class<?> _type, final boolean _includeTypeInherited)
  {
    return new Predicate1<Method>() {
      @Override
      public boolean evaluate(final Method element)
      {
        return hasMethod(_type, element, _includeTypeInherited);
      }
    };
  }

  /**
   * Returns true if the given type implements the interface type specified. The method scans all implementing interfaces as well as their
   * parents all the way up. No classes are checked (only interfaces).
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static boolean isImplementing(@NotNull final Class<?> type, @NotNull final Class<?> interfaceType)
  {
    if (!interfaceType.isInterface())
      throw new IllegalArgumentException("The type '" + interfaceType.getName() + "' is not an interface.");

    val processed = new ArrayList<Class<?>>();
    val typeStack = new LinkedList<Class<?>>();

    // populate initially with type's implementing interfaces
    for (Class<?> iface : type.getInterfaces())
    {
      typeStack.push(iface);
      processed.add(iface);
    }

    // populate with superclasses' interfaces
    Class<?> superClass = type;
    while ((superClass = superClass.getSuperclass()) != null)
      for (Class<?> iface : superClass.getInterfaces())
      {
        typeStack.push(iface);
        processed.add(iface);
      }

    // iterate through all added interfaces
    while (typeStack.size() > 0)
    {
      // get next and check if matching
      Class<?> iType = typeStack.pop();
      if (iType.equals(interfaceType))
        return true;
      else
        // otherwise add this type's implementing interfaces for investigation
        for (Class<?> iface : iType.getInterfaces())
          if (!processed.contains(iface))
          {
            typeStack.push(iface);
            processed.add(iface);
          }
    }

    return false;
  }

  /**
   * Returns true if the class given is taking part in a nested class hierarchy.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean isNested(@NotNull final Class<?> type)
  {
    return type.getName().contains(CONSTANT.DOLLAR_SIGN);
  }

  /**
   * Returns true if a method can be overridden.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static boolean isOverridable(@NotNull final Method methodInfo)
  {
    val mod = methodInfo.getModifiers();
    return Modifier.isAbstract(mod) || !(Modifier.isFinal(mod) || Modifier.isStatic(mod));
  }

  /**
   * Checks that a class is overridable, i.e. has at least one overridable method and is non final, primitive, etc.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  @Validate
  public static boolean isOverridable(@NotNull final Class<?> type)
  {
    if (type.isPrimitive())
      return false;
    if (type.isEnum())
      return false;
    if (type.isAnonymousClass())
      return false;
    if (type.isInterface())
      return true;

    int mod = type.getModifiers();

    if (Modifier.isFinal(mod))
      return false;
    if (Modifier.isAbstract(mod))
      return true;
    if (getConstructorDefault(type) == null)
      return false;

    boolean existsOverridableMethod = false;
    for (Method mi : type.getMethods())
      if (isOverridable(mi))
        return true;

    return existsOverridableMethod;
  }

  /**
   * Returns true if the type specified is a primitive type such as int, boolean, etc. This cannot be used for arrays, see the ReifiedArray
   * class for that.
   */
  @Validate
  public static PrimitiveType getPrimitiveType(@NotNull final Class<?> type)
  {
    val name = type.getName();
    if ("byte".equals(name))
      return PrimitiveType.Byte;
    if ("int".equals(name))
      return PrimitiveType.Int;
    if ("double".equals(name))
      return PrimitiveType.Double;
    if ("long".equals(name))
      return PrimitiveType.Long;
    if ("short".equals(name))
      return PrimitiveType.Short;
    if ("boolean".equals(name))
      return PrimitiveType.Boolean;
    if ("float".equals(name))
      return PrimitiveType.Float;
    if ("char".equals(name))
      return PrimitiveType.Char;

    return PrimitiveType.NotPrimitive;
  }

  /**
   * Parses a class from a string.
   * 
   * @throws NullPointerException An argument is null.
   * @throws ClassNotFoundException The class was not found.
   */
  @Validate
  public static Class<?> parse(@NotNull final String value)
      throws ClassNotFoundException
  {
    // parse
    val clazz = Class.forName(value);

    return clazz;
  }

  /**
   * Parses a class from a string. The created class type must be of specified type (or a subtype of it)
   * 
   * @throws NullPointerException An argument is null.
   * @throws ClassNotFoundException The class was not found.
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> Class<T> parse(@NotNull final String value, @NotNull final Class<T> baseType)
      throws ClassNotFoundException
  {
    val result = parse(value);
    if (!isExtending(result, baseType))
      throw new ClassCastException("The type " + value + " is not derived from " + baseType.getName());

    return (Class<T>) result;
  }

  /**
   * Quick-and-dirty proxy-ing utility method, allows dynamic dispatch on any object using a specified interface. The run-time object and
   * the interface given need not have an inheritance relationship. Method calls to interface methods are routed to the given run-time
   * object.
   * 
   * TODO: support overloaded methods.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException The interface class type is not an actual interface type
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> T proxy(@NotNull final Object obj, @NotNull final Class<T> interfaceType)
  {
    if (!interfaceType.isInterface())
      throw new IllegalArgumentException(interfaceType.getName() + " is not an interface!");

    // wrap with proxy, implementing the specified interface and this invocation handler
    return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] {interfaceType}, new InvocationHandler() {

      /**
       * Dispatches method invocations to the original object, using reflection
       */
      @Override
      public Object invoke(Object proxy, Method method, Object[] args)
          throws Throwable
      {
        // convert proxy method invocation to a call to the service
        String methodName = method.getName();

        Method dispatchedMethod = getMethod(obj.getClass(), methodName, true);
        if (dispatchedMethod == null)
          throw new NoSuchMethodException("methodName=" + methodName);

        return dispatchedMethod.invoke(obj, args);
      }
    });
  }

  /**
   * Overloaded variation of the above method, which allows certain methods to be prevented from being called. Useful for "sealing" classes
   * in a fashion similar to e.g. UnmodifiableMap and such classes.
   * 
   * TODO: support overloaded methods.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException The interface class type is not an actual interface type
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> T
      proxy(@NotNull final Object obj, @NotNull final Class<T> interfaceType, @NotNull final String[] suppressedMethodNames)
  {
    if (!interfaceType.isInterface())
      throw new IllegalArgumentException(interfaceType.getName() + " is not an interface!");

    // wrap with proxy, implementing the specified interface and this invocation handler
    return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] {interfaceType}, new InvocationHandler() {

      /**
       * Dispatches method invocations to the original object, using reflection
       */
      @Override
      public Object invoke(Object proxy, Method method, Object[] args)
          throws Throwable
      {
        // convert proxy method invocation to a call to the service
        val methodName = method.getName();
        if (StringUtils.contains(suppressedMethodNames, methodName, StringComparison.Ordinal))
          throw new UnsupportedOperationException("The method '" + methodName + "' is not allowed to be called");

        Method dispatchedMethod = getMethod(obj.getClass(), methodName, true);
        if (dispatchedMethod == null)
          throw new NoSuchMethodException("methodName=" + methodName);

        return dispatchedMethod.invoke(obj, args);
      }
    });
  }

  /**
   * Returns a reflective toString() of the fields of this object
   * 
   * @throws SecurityException if cannot perform reflection operations in this context
   * @throws IllegalAccessException if this Field object is enforcing Java language access control and the underlying field is inaccessible
   * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field
   *           (or a subclass or implementor thereof)
   * @throws ExceptionInInitializerError if the initialization provoked by this method fails
   */
  public static String toString(final Object obj)
      throws IllegalArgumentException, IllegalAccessException
  {
    return toString(obj, true);
  }

  /**
   * Returns a reflective toString() of the fields of this object
   * 
   * @throws SecurityException if cannot perform reflection operations in this context
   * @throws IllegalAccessException if this Field object is enforcing Java language access control and the underlying field is inaccessible
   * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field
   *           (or a subclass or implementor thereof)
   * @throws ExceptionInInitializerError if the initialization provoked by this method fails
   */
  public static String toString(final Object obj, final boolean includeInherited)
      throws IllegalArgumentException, IllegalAccessException
  {
    if (obj == null)
      return "null";

    StringBuilder result = new StringBuilder();
    result.append(obj.getClass().getName());
    result.append(" {");

    // determine fields declared in this class only (no fields of superclass)
    Field[] fields = getFields(obj.getClass(), includeInherited);
    if (fields.length == 0)
    {
      result.append("}");
      return result.toString();
    }
    {
      // print field names paired with their values
      for (Field field : fields)
      {
        result.append(field.getName());
        result.append(": ");
        field.setAccessible(true);
        result.append(field.get(obj));
        result.append(", ");
      }

      result.delete(result.length() - 2, result.length());
      result.append("}");
    }

    return result.toString();
  }

  /**
   * Internal-use function, matches a method in the declared methods of a class, returning the method found in the class instead of the
   * method given. Returns null if the method is not found.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  private static Method findDeclaredMethod(@NotNull final Class<?> type, @NotNull final Method method)
  {
    for (val m : type.getDeclaredMethods())
      if (equal(m, method))
        return m;

    return null;
  }

  private ReflectionUtils()
  {
  }
}
