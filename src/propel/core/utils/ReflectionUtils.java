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
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import lombok.Function;
import lombok.Predicate;
import lombok.Validate;
import lombok.Validate.NotNull;
import lombok.val;
import propel.core.collections.KeyValuePair;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.lists.ReifiedList;
import propel.core.collections.maps.avl.AvlHashtable;
import propel.core.collections.sets.AvlTreeSet;
import propel.core.common.CONSTANT;
import propel.core.functional.projections.Projections;

/**
 * Provides utility functionality for reflective manipulations
 */
public final class ReflectionUtils
{

  /**
   * Private constructor
   */
  private ReflectionUtils()
  {
  }

  /**
   * Activates an object from the type.
   * 
   * @throws NullPointerException An argument is null.
   * @throws InstantiationException When instantiation fails.
   * @throws IllegalAccessException When a member is illegally accessed.
   */
  public static <T> T activate(Class<T> clazz)
      throws InstantiationException, IllegalAccessException
  {
    if (clazz == null)
      throw new NullPointerException("clazz");

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
  @SuppressWarnings("unchecked")
  public static <T> T activate(Class<T> clazz, Object[] constructorArgs)
      throws InstantiationException, IllegalAccessException, InvocationTargetException
  {
    if (clazz == null)
      throw new NullPointerException("clazz");
    if (constructorArgs == null || constructorArgs.length == 0)
      return activate(clazz);
    final int constructorArgsLength = constructorArgs.length;

    // find relevant constructors
    Constructor<?>[] constructors = Linq.where(clazz.getConstructors(), constructorParametersEqual(constructorArgsLength));
    if (constructors.length <= 0)
      throw new IllegalArgumentException("A constructor with " + constructorArgsLength + " arguments was not found: " + clazz.getName());

    Constructor<?> constructor = constructors.length == 1 ? constructors[0] : matchConstructor(constructors, constructorArgs);

    return (T) constructor.newInstance(constructorArgs);
  }

  @Predicate
  private static boolean constructorParametersEqual(final Constructor<?> element, final int _len)
  {
    return element.getParameterTypes().length == _len;
  }

  private static Constructor<?> matchConstructor(Constructor<?>[] constructors, Object[] constructorArgs)
  {
    // get constructor argument types
    Class<?>[] argTypes = Linq.select(constructorArgs, getClassIfNotNull());

    // not found, throw ambiguous call exception
    for (Constructor<?> constructor : constructors)
    {
      // get parameter types
      Class<?>[] parameterTypes = constructor.getParameterTypes();

      // combine into one class
      List<KeyValuePair<Class<?>, Class<?>>> list = new ArrayList<KeyValuePair<Class<?>, Class<?>>>();
      for (int i = 0; i < parameterTypes.length; i++)
        list.add(new KeyValuePair<Class<?>, Class<?>>(argTypes[i], parameterTypes[i]));

      // see if all args types are assignable to constructor parameter types
      if (Linq.all(list, isParameterAssignable()))
        return constructor;
    }

    // failed to find match, log an informative message
    String[] constructorSignatures = Linq.select(constructors, Projections.toStringify());
    String[] argTypeNames = Linq.select(argTypes, Projections.toStringify());

    throw new IllegalArgumentException("There are " + constructorSignatures.length + " constructors ("
        + StringUtils.delimit(constructorSignatures, CONSTANT.COMMA) + ") accepting the arguments given: "
        + StringUtils.delimit(argTypeNames, CONSTANT.COMMA));
  }

  @Function
  static String getSimpleNameIfNotNull(final Class<?> arg)
  {
    return arg != null ? arg.getSimpleName() : "[NULL]";
  }

  @Predicate
  private static boolean isParameterAssignable(final KeyValuePair<Class<?>, Class<?>> element)
  {
    Class<?> argType = element.getKey();
    Class<?> parameterType = element.getValue();

    // check if assignable
    if (parameterType.isAssignableFrom(argType))
      return true;

    return false;
  }

  @Function
  private static Class<?> getClassIfNotNull(final Object arg)
  {
    return arg != null ? arg.getClass() : null;
  }

  /**
   * Activates an object from the class name given.
   * 
   * @throws InstantiationException When instantiation fails.
   * @throws IllegalAccessException When a member is illegally accessed.
   * @throws InvocationTargetException When the constructor called throws an exception.
   * @throws ClassNotFoundException The specified class name does not correspond to a class.
   */
  public static Object activate(String className)
      throws InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException
  {
    if (className == null)
      throw new NullPointerException("className");

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
  public static Object activate(String className, Object[] constructorArgs)
      throws InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException
  {
    if (className == null)
      throw new NullPointerException("className");

    Class<?> clazz = parse(className);
    return activate(clazz, constructorArgs);
  }

  /**
   * Compares two Method classes and returns true if they appear to be referring to the same signature of a method.
   * 
   * @throws NullPointerException An argument is null
   */
  public static boolean equal(Method methodA, Method methodB)
  {
    if (methodA == null)
      throw new NullPointerException("methodA");
    if (methodB == null)
      throw new NullPointerException("methodB");

    if (methodA.getName().equals(methodB.getName()))
      if (methodA.getReturnType().equals(methodB.getReturnType()))
      {
        Class<?>[] firstMethodArgs = methodA.getParameterTypes();
        Class<?>[] secondMethodArgs = methodB.getParameterTypes();

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
  public static boolean equal(Field fieldA, Field fieldB)
  {
    if (fieldA == null)
      throw new NullPointerException("fieldA");
    if (fieldB == null)
      throw new NullPointerException("fieldB");

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
  public static boolean equal(Constructor<?> constrA, Constructor<?> constrB)
  {
    if (constrA == null)
      throw new NullPointerException("constrA");
    if (constrB == null)
      throw new NullPointerException("constrB");

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
  public static boolean equal(MemberInfo membA, MemberInfo membB)
  {
    if (membA == null)
      throw new NullPointerException("membA");
    if (membB == null)
      throw new NullPointerException("membB");

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
  public static boolean equal(PropertyInfo propA, PropertyInfo propB)
  {
    if (propA == null)
      throw new NullPointerException("propA");
    if (propB == null)
      throw new NullPointerException("propB");

    if (propA.getName().equals(propB.getName()))
      if (propA.getPropertyType().equals(propB.getPropertyType()))
      {
        // compare getters
        Method getterA = propA.getGetter();
        Method getterB = propB.getGetter();

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
        Method setterA = propA.getSetter();
        Method setterB = propB.getSetter();

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
   * Lists all classes inside a package. TODO: fix bug where having a whitespace in the contained folder causes %20 to be used (when this
   * used as part of a JAR)
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException A package is invalid.
   * @throws ClassNotFoundException When finding a class from a class name fails while enumerating.
   */
  public static List<Class<?>> getClasses(Package pkg)
      throws ClassNotFoundException
  {
    if (pkg == null)
      throw new NullPointerException("pkg");

    List<Class<?>> result = new ArrayList<Class<?>>();

    // Get a File object for the package
    String pkgName = pkg.getName();
    File directory = null;
    String fullPath;
    String relPath = pkgName.replace(CONSTANT.DOT_CHAR, CONSTANT.FORWARD_SLASH_CHAR);
    URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);
    if (resource == null)
    {
      // try other slashes
      relPath = pkgName.replace(CONSTANT.DOT_CHAR, CONSTANT.BACK_SLASH_CHAR);
      resource = ClassLoader.getSystemClassLoader().getResource(relPath);

      // not found
      if (resource == null)
        throw new IllegalArgumentException("No resource found for " + relPath);
    }

    fullPath = resource.getFile();

    String normalizedFullPath = fullPath;
    if (StringUtils.contains(fullPath, '/'))
      normalizedFullPath = StringUtils.replace(StringUtils.replace(fullPath, "%5c", "/", StringComparison.OrdinalIgnoreCase), "%2f", "/",
          StringComparison.OrdinalIgnoreCase);
    else if (StringUtils.contains(fullPath, '\\'))
      normalizedFullPath = StringUtils.replace(StringUtils.replace(fullPath, "%5c", "\\", StringComparison.OrdinalIgnoreCase), "%2f", "\\",
          StringComparison.OrdinalIgnoreCase);

    directory = new File(normalizedFullPath);
    final String classExt = ".class";

    if (directory.exists())
    {
      // Get the list of the files contained in the package
      String[] files = directory.list();
      for (int i = 0; i < files.length; i++)
        // we are only interested in .class files
        if (StringUtils.match(files[i], MatchType.EndsWith, classExt, StringComparison.Ordinal))
        {
          // removes the .class extension
          String className = pkgName + CONSTANT.DOT_CHAR + files[i].substring(0, files[i].length() - classExt.length());
          try
          {
            result.add(Class.forName(className));
          }
          catch(ClassNotFoundException e)
          {
            throw new ClassNotFoundException("Could not find class '" + className + "' in package '" + pkgName + "'.");
          }
        }
    } else
    {
      // directory does not exist, find JAR file
      String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");

      try
      {
        // enumerate entries
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements())
        {
          // get next entry
          JarEntry entry = entries.nextElement();
          String entryName = entry.getName();

          // must be a .class entry, starting with package name
          if (entryName.endsWith(classExt) && entryName.startsWith(relPath)
              && entryName.length() > (relPath.length() + CONSTANT.FORWARD_SLASH.length()))
          {
            String className = entryName.replace(CONSTANT.FORWARD_SLASH_CHAR, CONSTANT.DOT_CHAR)
                .replace(CONSTANT.BACK_SLASH_CHAR, CONSTANT.DOT_CHAR).replace(classExt, CONSTANT.EMPTY_STRING);

            // attempt to get the class by its name
            try
            {
              result.add(Class.forName(className));
            }
            catch(ClassNotFoundException e)
            {
              throw new ClassNotFoundException("Could not find class '" + className + "' in JAR file '" + jarPath + "'.");
            }
          }
        }
      }
      catch(IOException e)
      {
        throw new IllegalArgumentException("Package " + pkgName + " (for JAR " + jarPath + ") does not appear to be valid.", e);
      }
    }
    return result;
  }

  /**
   * Returns all annotations from the annotated class (and its subclasses if required)
   * 
   * @throws NullPointerException An argument is null.
   */
  public static <T extends Annotation> List<T> getAnnotations(Class<?> annotatedClass, Class<T> annotationType, boolean includeInherited)
  {
    if (annotatedClass == null)
      throw new NullPointerException("annotatedClass");
    if (annotationType == null)
      throw new NullPointerException("annotationType");

    List<T> result = new ArrayList<T>(16);

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
      for (Class<?> iface : annotatedClass.getInterfaces())
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
  public static List<Annotation> getAnnotations(Class<?> annotatedClass, boolean includeInherited)
  {
    if (annotatedClass == null)
      throw new NullPointerException("annotatedClass");

    List<Annotation> result = new ArrayList<Annotation>(16);

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
      for (Class<?> iface : annotatedClass.getInterfaces())
        result.addAll(ArrayUtils.toList(iface.getDeclaredAnnotations()));
    }

    return result;
  }

  /**
   * Returns all annotations from the annotated method which are of specified annotation class type (or any its subclasses, if required)
   * 
   * @throws NullPointerException An argument is null.
   */
  public static List<Annotation> getMethodAnnotations(Method annotatedMethod, boolean includeInherited)
  {
    if (annotatedMethod == null)
      throw new NullPointerException("annotatedMethod");

    List<Annotation> result = new ArrayList<Annotation>(16);

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
      for (Class<?> iface : annotatedMethod.getDeclaringClass().getInterfaces())
      {
        Method ifaceMethod = findDeclaredMethod(iface, annotatedMethod);
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
  public static <T extends Annotation> List<T> getMethodAnnotations(Method annotatedMethod, Class<T> annotationType,
                                                                    boolean includeInherited)
  {
    if (annotatedMethod == null)
      throw new NullPointerException("annotatedMethod");

    List<T> result = new ArrayList<T>(16);

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
        Method superClassMethod = findDeclaredMethod(superClass, annotatedMethod);
        if (superClassMethod != null)
        {
          annotations = ArrayUtils.toList(superClassMethod.getDeclaredAnnotations());
          result.addAll(Linq.toList(Linq.ofType(annotations, annotationType)));
        }
      }

      // now get all the super-interfaces' annotations
      for (Class<?> iface : annotatedMethod.getDeclaringClass().getInterfaces())
      {
        Method ifaceMethod = findDeclaredMethod(iface, annotatedMethod);
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
  public static Class<?>[] getInterfaces(Class<?> type, boolean includeInherited)
  {
    if (type == null)
      throw new NullPointerException("type");

    Set<Class<?>> result = new HashSet<Class<?>>();
    LinkedList<Class<?>> typeStack = new LinkedList<Class<?>>();

    // populate initially with type's implementing interfaces
    for (Class<?> iface : type.getInterfaces())
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
  public static Class<?>[] getInterfacesExcept(Class<?> type, Class<?> excludedInterface)
  {
    if (type == null)
      throw new NullPointerException("type");
    if (excludedInterface == null)
      throw new NullPointerException("excludedInterface");

    // get all interfaces
    ReifiedList<Class<?>> interfaces = new ReifiedArrayList<Class<?>>(16, Class.class);

    // exclude the specified one
    for (Class<?> iface : type.getInterfaces())
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
  public static Constructor<?>[] getConstructors(Class<?> type, boolean includeSuperClass)
  {
    if (type == null)
      throw new NullPointerException("type");

    ReifiedList<Constructor<?>> result = new ReifiedArrayList<Constructor<?>>() {};

    for (Constructor<?> constr : type.getDeclaredConstructors())
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
  public static Constructor<?> getConstructorDefault(Class<?> type)
  {
    if (type == null)
      throw new NullPointerException("type");

    // iterate constructors
    for (Constructor<?> constructor : type.getConstructors())
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
  public static Field[] getFields(Class<?> type, boolean includeInherited)
  {
    if (type == null)
      throw new NullPointerException("type");

    ReifiedList<Field> result = new ReifiedArrayList<Field>(Field.class);

    for (Field field : type.getDeclaredFields())
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
  public static Field[] getFields(Class<?> type, final String name, boolean includeInherited)
  {
    return Linq.where(getFields(type, includeInherited), fieldNameEquals(name));
  }

  /**
   * Returns the constructor information for an object's property, if found, otherwise null.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  public static Field getField(Class<?> type, final String name, boolean includeInherited)
  {
    if (type == null)
      throw new NullPointerException("type");
    if (name == null)
      throw new NullPointerException("name");

    return Linq.firstOrDefault(getFields(type, includeInherited), fieldNameEquals(name));
  }

  @Predicate
  private static boolean fieldNameEquals(final Field element, final String _name)
  {
    return element.getName().equals(_name);
  }

  /**
   * Gets all properties found in a type. Optionally returns inherited ones too, rather than just declared ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  public static PropertyInfo[] getProperties(Class<?> type, boolean includeInherited)
  {
    if (type == null)
      throw new NullPointerException("type");

    ReifiedList<PropertyInfo> result = new ReifiedArrayList<PropertyInfo>(PropertyInfo.class);
    AvlHashtable<String, Method> methods = new AvlHashtable<String, Method>(String.class, Method.class);

    // get declared method names
    for (Method method : type.getDeclaredMethods())
    {
      String methodName = method.getName();

      if (isGetter(method) || isSetter(method))
        if (!methods.containsKey(methodName))
          methods.add(method.getName(), method);
    }

    // get inherited, if required
    if (includeInherited)
    {
      Class<?> superClass = type;
      while ((superClass = superClass.getSuperclass()) != null)
        for (Method method : superClass.getDeclaredMethods())
        {
          String methodName = method.getName();

          if (!methods.containsKey(methodName))
            if (isGetter(method) || isSetter(method))
              methods.add(method.getName(), method);
        }
    }

    // find which methods exist in pairs of getters/setters
    AvlTreeSet<String> propertyNames = new AvlTreeSet<String>(String.class);
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
  public static PropertyInfo getProperty(Class<?> type, final String name, boolean includeInherited)
  {
    if (type == null)
      throw new NullPointerException("type");
    if (name == null)
      throw new NullPointerException("name");

    return Linq.firstOrDefault(getProperties(type, includeInherited), propertyNameEquals(name));
  }

  @Predicate
  private static boolean propertyNameEquals(final PropertyInfo element, final String _name)
  {
    return element.getName().equals(_name);
  }

  /**
   * Returns all getters found in a class type. Optionally returns inherited ones too, rather than just declared ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  public static Method[] getGetters(Class<?> type, boolean includeInherited)
  {
    Method[] methods = getMethods(type, includeInherited);
    return Linq.where(methods, methodIsGetter());
  }

  @Predicate
  private static boolean methodIsGetter(final Method element)
  {
    return isGetter(element);
  }

  /**
   * Returns all setters found in a class type. Optionally returns inherited ones too, rather than just declared ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  public static Method[] getSetters(Class<?> type, boolean includeInherited)
  {
    Method[] methods = getMethods(type, includeInherited);
    return Linq.where(methods, methodIsSetter());
  }

  @Predicate
  private static boolean methodIsSetter(final Method element)
  {
    return isSetter(element);
  }

  /**
   * Returns all methods found in a class type. Optionally returns inherited ones too, rather than just declared ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  public static Method[] getMethods(Class<?> type, boolean includeInherited)
  {
    if (type == null)
      throw new NullPointerException("type");

    ReifiedList<Method> result = new ReifiedArrayList<Method>(Method.class);

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
  public static Method getMethod(Class<?> type, final String name, boolean includeInherited)
  {
    if (type == null)
      throw new NullPointerException("type");
    if (name == null)
      throw new NullPointerException("name");

    return Linq.firstOrDefault(getMethods(type, includeInherited), methodNameEquals(name));
  }

  @Predicate
  private static boolean methodNameEquals(final Method element, final String _name)
  {
    return element.getName().equals(_name);
  }

  /**
   * Returns all methods found in a class type. Optionally returns inherited ones too, rather than just declared ones.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  public static MemberInfo[] getMembers(Class<?> type, boolean includeInherited)
  {
    if (type == null)
      throw new NullPointerException("type");

    ReifiedList<MemberInfo> result = new ReifiedArrayList<MemberInfo>(MemberInfo.class);

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

  @Function
  private static MemberInfo constructorToMemberInfo(final Constructor<?> arg)
  {
    return new MemberInfo(arg);
  }

  @Function
  private static MemberInfo propertyToMemberInfo(final PropertyInfo arg)
  {
    return new MemberInfo(arg);
  }

  @Function
  private static MemberInfo methodToMemberInfo(final Method arg)
  {
    return new MemberInfo(arg);
  }

  @Function
  private static MemberInfo fieldToMemberInfo(final Field arg)
  {
    return new MemberInfo(arg);
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
  public static MemberInfo getMember(Class<?> type, final String name, boolean includeInherited)
  {
    if (type == null)
      throw new NullPointerException("type");
    if (name == null)
      throw new NullPointerException("name");

    return Linq.firstOrDefault(getMembers(type, includeInherited), memberNameEquals(name));
  }

  @Predicate
  private static <T> boolean memberNameEquals(MemberInfo element, String _name)
  {
    return element.getName().equals(_name);
  }

  /**
   * Returns true if a method appears to be part of a type.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean hasMethod(Class<?> type, final Method method, boolean includeInherited)
  {
    if (type == null)
      throw new NullPointerException("type");
    if (method == null)
      throw new NullPointerException("method");

    return Linq.firstOrDefault(getMethods(type, includeInherited), methodsAreEqual(method)) != null;
  }

  @Predicate
  private static boolean methodsAreEqual(final Method element, final Method _method)
  {
    return equal(element, _method);
  }

  /**
   * Returns true if a method appears to be part of a type.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean hasProperty(Class<?> type, final PropertyInfo property, boolean includeInherited)
  {
    if (type == null)
      throw new NullPointerException("type");
    if (property == null)
      throw new NullPointerException("property");

    return Linq.firstOrDefault(getProperties(type, includeInherited), propertiesAreEqual(property)) != null;
  }

  @Predicate
  private static boolean propertiesAreEqual(final PropertyInfo element, final PropertyInfo _pi)
  {
    return equal(element, _pi);
  }

  /**
   * Returns true if a field appears to be part of a type.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean hasField(Class<?> type, final Field field, boolean includeInherited)
  {
    if (type == null)
      throw new NullPointerException("type");
    if (field == null)
      throw new NullPointerException("field");

    return Linq.firstOrDefault(getFields(type, includeInherited), fieldsAreEqual(field)) != null;
  }

  @Predicate
  private static boolean fieldsAreEqual(final Field element, final Field _field)
  {
    return equal(element, _field);
  }

  /**
   * Returns true if a member appears to be part of a type.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean hasMember(Class<?> type, final MemberInfo member, boolean includeInherited)
  {
    if (type == null)
      throw new NullPointerException("type");
    if (member == null)
      throw new NullPointerException("member");

    return Linq.firstOrDefault(getMembers(type, includeInherited), membersAreEqual(member)) != null;
  }

  @Predicate
  private static boolean membersAreEqual(final MemberInfo element, final MemberInfo _member)
  {
    return equal(element, _member);
  }

  /**
   * Returns true if a class is generic
   * 
   * @throws NullPointerException An argument is null
   */
  public static boolean isGeneric(Class<?> type)
  {
    if (type == null)
      throw new NullPointerException("type");

    return type.getTypeParameters().length > 0;
  }

  /**
   * Returns true if the method appears to be a POJO getter.
   * 
   * @throws NullPointerException An argument is null
   */
  public static boolean isGetter(Method method)
  {
    if (method == null)
      throw new NullPointerException("method");

    int mod = method.getModifiers();
    if (Modifier.isAbstract(mod) || Modifier.isFinal(mod) || Modifier.isNative(mod) || Modifier.isStatic(mod))
      return false;

    String methodName = method.getName();
    if (methodName.length() >= 4)
      // bean syntax for getters
      if (methodName.startsWith("get"))
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
  public static boolean isReturnTypeVoid(Method method)
  {
    if (method == null)
      throw new NullPointerException("method");

    return method.getReturnType().getSimpleName().equals("void");
  }

  /**
   * Returns true if the method appears to be a POJO setter.
   * 
   * @throws NullPointerException An argument is null
   */
  public static boolean isSetter(Method method)
  {
    if (method == null)
      throw new NullPointerException("method");

    int mod = method.getModifiers();
    if (Modifier.isAbstract(mod) || Modifier.isFinal(mod) || Modifier.isNative(mod) || Modifier.isStatic(mod))
      return false;

    String methodName = method.getName();
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
  public static void injectProperty(Object obj, String propertyName, Object propertyValue, boolean includeInherited)
  {
    if (obj == null)
      throw new NullPointerException("obj");
    if (propertyName == null)
      throw new NullPointerException("propertyName");

    // find property
    PropertyInfo pi = getProperty(obj.getClass(), propertyName, includeInherited);
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
  public static boolean instanceOf(Class<?> type, Class<?> baseClassOrInterfaceType)
  {
    if (type == null)
      throw new NullPointerException("type");
    if (baseClassOrInterfaceType == null)
      throw new NullPointerException("baseClassOrInterfaceType");

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
  public static boolean isExtending(Class<?> type, Class<?> baseClassType)
  {
    if (type == null)
      throw new NullPointerException("type");
    if (baseClassType == null)
      throw new NullPointerException("baseClassType");
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
  public static boolean isLookingLike(final Class<?> type, Class<?> interfaceType, final boolean includeTypeInherited,
                                      boolean includeInterfaceTypeInherited)
  {
    if (type == null)
      throw new NullPointerException("type");
    if (interfaceType == null)
      throw new NullPointerException("interfaceType");

    // get methods we are looking for
    Method[] methods = getMethods(interfaceType, includeInterfaceTypeInherited);

    // must find all these methods in the given type
    return Linq.all(methods, methodHasMethod(type, includeTypeInherited));
  }

  @Predicate
  private static boolean methodHasMethod(final Method element, final Class<?> _type, final boolean _includeTypeInherited)
  {
    return hasMethod(_type, element, _includeTypeInherited);
  }

  /**
   * Returns true if the given type implements the interface type specified. The method scans all implementing interfaces as well as their
   * parents all the way up. No classes are checked (only interfaces).
   * 
   * @throws NullPointerException When an argument is null
   */
  public static boolean isImplementing(Class<?> type, Class<?> interfaceType)
  {
    if (type == null)
      throw new NullPointerException("type");
    if (interfaceType == null)
      throw new NullPointerException("interfaceType");
    if (!interfaceType.isInterface())
      throw new IllegalArgumentException("The type '" + interfaceType.getName() + "' is not an interface.");

    List<Class<?>> processed = new ArrayList<Class<?>>();
    LinkedList<Class<?>> typeStack = new LinkedList<Class<?>>();

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
  public static boolean isNested(Class<?> type)
  {
    if (type == null)
      throw new NullPointerException("type");

    return type.getName().contains(CONSTANT.DOLLAR_SIGN);
  }

  /**
   * Returns true if a method can be overridden.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean isOverridable(Method methodInfo)
  {
    if (methodInfo == null)
      throw new NullPointerException("methodInfo");

    int mod = methodInfo.getModifiers();

    return Modifier.isAbstract(mod) || !(Modifier.isFinal(mod) || Modifier.isStatic(mod));
  }

  /**
   * Checks that a class is overridable, i.e. has at least one overridable method and is non final, primitive, etc.
   * 
   * @throws NullPointerException An argument is null.
   * @throws SecurityException Cannot perform reflection operations in this context.
   */
  public static boolean isOverridable(Class<?> type)
  {
    if (type == null)
      throw new NullPointerException("type");

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
  public static PrimitiveType getPrimitiveType(Class<?> type)
  {
    if (type == null)
      throw new NullPointerException("type");

    String name = type.getName();
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
  public static Class<?> parse(String value)
      throws ClassNotFoundException
  {
    if (value == null)
      throw new NullPointerException("value");

    // parse
    Class<?> clazz = Class.forName(value);

    return clazz;
  }

  /**
   * Parses a class from a string. The created class type must be of specified type (or a subtype of it)
   * 
   * @throws NullPointerException An argument is null.
   * @throws ClassNotFoundException The class was not found.
   */
  @SuppressWarnings("unchecked")
  public static <T> Class<T> parse(String value, Class<T> baseType)
      throws ClassNotFoundException
  {
    if (value == null)
      throw new NullPointerException("value");
    if (baseType == null)
      throw new NullPointerException("baseType");

    Class<?> result = parse(value);
    if (!isExtending(result, baseType))
      throw new ClassCastException("The type " + value + " is not derived from " + baseType.getName());

    return (Class<T>) result;
  }

  /**
   * Quick-and-dirty proxy-ing utility method, allows dynamic dispatch on any object using a specified interface. The run-time object and
   * the interface given need not have an inheritance relationship. Method calls to interface methods are routed to the given run-time
   * object. TODO: support overloaded methods.
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
   * in a fashion similar to e.g. UnmodifiableMap and such classes. TODO: support overloaded methods.
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
   * Internal-use function, matches a method in the declared methods of a class, returning the method found in the class instead of the
   * method given. Returns null if the method is not found.
   * 
   * @throws NullPointerException An argument is null.
   */
  private static Method findDeclaredMethod(Class<?> type, Method method)
  {
    if (type == null)
      throw new NullPointerException("type");
    if (method == null)
      throw new NullPointerException("method");

    for (Method m : type.getDeclaredMethods())
      if (equal(m, method))
        return m;

    return null;
  }
}
