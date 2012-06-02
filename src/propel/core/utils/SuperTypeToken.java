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

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.Validate;
import lombok.Validate.NotNull;
import lombok.val;

/**
 * Super type token implementation, allows for getting the run-time generic type of an anonymously instantiated generic collection.
 * <br/>
 * Example:
 * <pre>
 * public class MyAvlHashtable<TKey extends Comparable<TKey>, TValue> extends AvlHashtable<TKey, TValue> { 
 *   public Class&lt;?&gt; keyClass;
 *   public Class&lt;?&gt; valueClass; 
 *   public Type keyType; 
 *   public Type valueType;
 * 
 *   public A_MyAvlHashtable() { 
 *     // retrieves first generic parameter i.e. class type of TKey
 *     keyClass = SuperTypeToken.getClazz(this.getClass()); 
 *     // retrieves second generic parameter 
 *     valueClass = SuperTypeToken.getClazz(this.getClass(), 1);
 * 
 *     keyType = SuperTypeToken.getType(this.getClass()); 
 *     valueType = SuperTypeToken.getType(this.getClass(), 1); 
 *   }
 * }
 * </pre>
 */
public final class SuperTypeToken
{
  /**
   * Private constructor prevents instantiation.
   */
  private SuperTypeToken()
  {

  }

  /**
   * Returns the Type of a class's first formal type argument Therefore for Blah<T0, T1>, 0 will return T0, and 1 will return T1.
   * 
   * @throws NullPointerException An argument is null
   * @throws SuperTypeTokenException The class is not a concrete class, not a generic class or not instantiated using anonymous class
   *           semantics.
   */
  public static Type getType(final Class<?> clazz)
  {
    return getType(clazz, 0);
  }

  /**
   * Returns the Type of a class's formal type argument, at a given position. The first format type argument is 0, then 1, etc. Therefore
   * for Blah<T0, T1>, 0 will return T0, and 1 will return T1.
   * 
   * @throws NullPointerException An argument is null
   * @throws SuperTypeTokenException The class is not a concrete class, not a generic class or not instantiated using anonymous class
   *           semantics.
   */
  @Validate
  public static Type getType(@NotNull final Class<?> clazz, final int pos)
  {
    val superClass = clazz.getGenericSuperclass();

    // test is superclass found
    if (superClass instanceof Class)
      throw new SuperTypeTokenException(String.format("Could not find generic parameter " + pos + " because this '" + clazz.getName()
          + "' instance should belong to an anonymous class or should be extending a generic class."));

    // get type arguments
    val types = ((ParameterizedType) superClass).getActualTypeArguments();

    // test if enough generic parameters were passed
    if (pos >= types.length)
      throw new SuperTypeTokenException(String.format("Could not find generic parameter #" + pos + " because only " + types.length
          + " parameters were passed."));

    return types[pos];
  }

  /**
   * Returns the Class of a class's first formal type argument. Therefore for Blah<T>, this will return T.
   * 
   * @throws NullPointerException An argument is null
   * @throws SuperTypeTokenException The class is not a concrete class, not a generic class or not instantiated using anonymous class
   *           semantics.
   */
  public static Class<?> getClazz(final Class<?> clazz)
  {
    return getClazz(clazz, 0);
  }

  /**
   * Returns the Class of a class's formal type argument, at a given position. The first format type argument is 0, then 1, etc. Therefore
   * for Blah<T0, T1>, 0 will return T0, and 1 will return T1.
   * 
   * @throws NullPointerException An argument is null
   * @throws SuperTypeTokenException The class is not a concrete class, not a generic class or not instantiated using anonymous class
   *           semantics.
   */
  public static Class<?> getClazz(final Class<?> clazz, final int pos)
  {
    val type = getType(clazz, pos);
    val result = getClass(type);

    if (result == null)
      throw new SuperTypeTokenException(
          "Could not determine run-time generic parameter for "
              + clazz
              + ". You either have to use anonymous class type declaration semantics or explicitly specify a generic type parameter at construction time if this is not possible (e.g when using composition).");

    return result;
  }

  /**
   * Get the underlying class for a type, or null if the type is a variable type.
   * 
   * @param type the type
   * 
   * @return the underlying class
   */
  private static Class<?> getClass(Type type)
  {
    if (type instanceof Class)
    {
      return (Class<?>) type;
    } else if (type instanceof ParameterizedType)
    {
      return getClass(((ParameterizedType) type).getRawType());
    } else if (type instanceof GenericArrayType)
    {
      Type componentType = ((GenericArrayType) type).getGenericComponentType();
      Class<?> componentClass = getClass(componentType);
      if (componentClass != null)
      {
        return Array.newInstance(componentClass, 0).getClass();
      } else
      {
        return null;
      }
    } else
    {
      return null;
    }
  }

  /*
   * // TODO: consider whether deep type retrieval is useful at all public static <T> List<Class<?>> getTypeArguments( Class<T> baseClass,
   * Class<? extends T> childClass) { Map<Type, Type> resolvedTypes = new HashMap<Type, Type>(); Type type = childClass; // start walking up
   * the inheritance hierarchy until we hit baseClass while(!getClass(type).equals(baseClass)) { if(type instanceof Class) { // there is no
   * useful information for us in raw types, so just keep going. type = ((Class) type).getGenericSuperclass(); } else { ParameterizedType
   * parameterizedType = (ParameterizedType) type; Class<?> rawType = (Class) parameterizedType.getRawType();
   * 
   * Type[] actualTypeArguments = parameterizedType.getActualTypeArguments(); TypeVariable<?>[] typeParameters =
   * rawType.getTypeParameters(); for(int i = 0; i < actualTypeArguments.length; i++) { resolvedTypes.put(typeParameters[i],
   * actualTypeArguments[i]); }
   * 
   * if(!rawType.equals(baseClass)) { type = rawType.getGenericSuperclass(); } } }
   * 
   * // finally, for each actual type argument provided to baseClass, determine (if possible) // the raw class for that type argument.
   * Type[] actualTypeArguments; if(type instanceof Class) { actualTypeArguments = ((Class) type).getTypeParameters(); } else {
   * actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments(); } List<Class<?>> typeArgumentsAsClasses = new
   * ArrayList<Class<?>>(); // resolve types by chasing down type variables. for(Type baseType : actualTypeArguments) {
   * while(resolvedTypes.containsKey(baseType)) { baseType = resolvedTypes.get(baseType); } typeArgumentsAsClasses.add(getClass(baseType));
   * } return typeArgumentsAsClasses; }
   */

}
