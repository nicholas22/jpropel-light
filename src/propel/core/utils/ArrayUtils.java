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

import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.lists.ReifiedLinkedList;
import propel.core.collections.lists.ReifiedList;
import propel.core.collections.lists.SortedList;
import propel.core.common.CONSTANT;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import lombok.Validate;
import lombok.Validate.NotNull;
import lombok.val;

/**
 * Provides utility functionality for array manipulations
 */
public final class ArrayUtils
{
  /**
   * Adds an element to the given array
   * 
   * @throws NullPointerException Array is null.
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> T[] add(@NotNull final T[] array, T element)
  {
    val result = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
    System.arraycopy(array, 0, result, 0, array.length);
    result[array.length] = element;
    return result;
  }

  /**
   * Converts a boolean[] to a Boolean array.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Boolean[] box(@NotNull final boolean[] booleans)
  {
    val result = new ReifiedArrayList<Boolean>(booleans.length, Boolean.class);
    for (boolean val : booleans)
      result.add(val);

    return result.toArray();
  }

  /**
   * Converts a byte[] to a Byte array.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Byte[] box(@NotNull final byte[] bytes)
  {
    val result = new ReifiedArrayList<Byte>(bytes.length, Byte.class);
    for (byte val : bytes)
      result.add(val);

    return result.toArray();
  }

  /**
   * Converts a char[] to a Character array.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Character[] box(@NotNull final char[] chars)
  {
    val result = new ReifiedArrayList<Character>(chars.length, Character.class);
    for (char val : chars)
      result.add(val);

    return result.toArray();
  }

  /**
   * Converts a short[] to a Short array.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Short[] box(@NotNull final short[] shorts)
  {
    val result = new ReifiedArrayList<Short>(shorts.length, Short.class);
    for (short val : shorts)
      result.add(val);

    return result.toArray();
  }

  /**
   * Converts a int[] to an Integer array.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Integer[] box(@NotNull final int[] ints)
  {
    val result = new ReifiedArrayList<Integer>(ints.length, Integer.class);
    for (int val : ints)
      result.add(val);

    return result.toArray();
  }

  /**
   * Converts a long[] to a Long array.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Long[] box(@NotNull final long[] longs)
  {
    val result = new ReifiedArrayList<Long>(longs.length, Long.class);
    for (long val : longs)
      result.add(val);

    return result.toArray();
  }

  /**
   * Converts a float[] to a Float array.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Float[] box(@NotNull final float[] floats)
  {
    val result = new ReifiedArrayList<Float>(floats.length, Float.class);
    for (float val : floats)
      result.add(val);

    return result.toArray();
  }

  /**
   * Converts a double[] to a Double array.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Double[] box(@NotNull final double[] doubles)
  {
    val result = new ReifiedArrayList<Double>(doubles.length, Double.class);
    for (double val : doubles)
      result.add(val);

    return result.toArray();
  }

  /**
   * Converts a boolean[] to a Boolean list.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static List<Boolean> boxBools(@NotNull final boolean[] booleans)
  {
    val result = new ArrayList<Boolean>(booleans.length);
    for (boolean val : booleans)
      result.add(val);

    return result;
  }

  /**
   * Converts a byte[] to a Byte list.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static List<Byte> boxBytes(@NotNull final byte[] bytes)
  {
    val result = new ArrayList<Byte>(bytes.length);
    for (byte val : bytes)
      result.add(val);

    return result;
  }

  /**
   * Converts a char[] to a Character list.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static List<Character> boxChars(@NotNull final char[] chars)
  {
    val result = new ArrayList<Character>(chars.length);
    for (char val : chars)
      result.add(val);

    return result;
  }

  /**
   * Converts a short[] to a Short list.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static List<Short> boxShorts(@NotNull final short[] shorts)
  {
    val result = new ArrayList<Short>(shorts.length);
    for (short val : shorts)
      result.add(val);

    return result;
  }

  /**
   * Converts a int[] to an Integer list.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static List<Integer> boxInts(@NotNull final int[] ints)
  {
    val result = new ArrayList<Integer>(ints.length);
    for (int val : ints)
      result.add(val);

    return result;
  }

  /**
   * Converts a long[] to a Long list.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static List<Long> boxLongs(@NotNull final long[] longs)
  {
    val result = new ArrayList<Long>(longs.length);
    for (long val : longs)
      result.add(val);

    return result;
  }

  /**
   * Converts a float[] to a Float list.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static List<Float> boxFloats(@NotNull final float[] floats)
  {
    val result = new ArrayList<Float>(floats.length);
    for (float val : floats)
      result.add(val);

    return result;
  }

  /**
   * Converts a double[] to a Double list.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static List<Double> boxDoubles(@NotNull final double[] doubles)
  {
    val result = new ArrayList<Double>(doubles.length);
    for (double val : doubles)
      result.add(val);

    return result;
  }

  /**
   * Clears an array with the default Type T value, i.e. null.
   * 
   * @throws NullPointerException Array is null.
   */
  public static <T> void clear(T[] array)
  {
    clear(array, null);
  }

  /**
   * Clears an array with the provided value
   * 
   * @throws NullPointerException Array is null.
   */
  @Validate
  public static <T> void clear(@NotNull final T[] array, T element)
  {
    // set to default value
    for (int i = 0; i < array.length; i++)
      array[i] = element;
  }

  /**
   * Clones the given array
   * 
   * @throws NullPointerException Array is null.
   */
  @Validate
  public static <T> T[] clone(@NotNull final T[] array)
  {
    return Arrays.copyOf(array, array.length);
  }

  /**
   * Clones the given array
   * 
   * @throws NullPointerException Array is null.
   */
  @Validate
  public static <T> T[] copy(@NotNull final T[] array)
  {
    return Arrays.copyOf(array, array.length);
  }

  /**
   * Returns the length of any array (primitive or object array). This works for one-dimensional arrays.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException The argument is not a primitive or object array
   */
  @Validate
  public static int count(@NotNull final Object array)
  {
    // get the array type
    PrimitiveArrayType arrayType = getType(array);

    switch(arrayType)
    {
      case NotPrimitive:
        return ((Object[]) array).length;
      case Char:
        return ((char[]) array).length;
      case Short:
        return ((short[]) array).length;
      case Int:
        return ((int[]) array).length;
      case Long:
        return ((long[]) array).length;
      case Float:
        return ((float[]) array).length;
      case Double:
        return ((double[]) array).length;
      case Boolean:
        return ((boolean[]) array).length;
      case Byte:
        return ((byte[]) array).length;
      default:
        throw new IllegalArgumentException("Unrecognized array type: " + arrayType);
    }
  }

  /**
   * Creates a generic array
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException If componentType is {@link Void#TYPE}
   * @throws ClassCastException An invalid type parameter was specified
   * @throws NegativeArraySizeException If the specified size is negative
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> T[] create(@NotNull final Class<?> componentType, final int size)
  {
    return (T[]) Array.newInstance(componentType, size);
  }

  /**
   * Creates a 1-dimensional array populated with the specified element in all places
   * 
   * @throws NullPointerException Value is null.
   * @throws IllegalArgumentException Value is null, or length is out of range.
   * @throws OutOfMemoryError No enough memory to allocate array
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> T[] create(@NotNull final T value, int length)
  {
    if (length < 0)
      throw new IllegalArgumentException("length=" + length);

    val result = (T[]) Array.newInstance(value.getClass(), length);
    for (int i = 0; i < length; i++)
      result[i] = value;

    return result;
  }

  /**
   * Creates a 2-dimensional array populated with the specified element's reference in all places
   * 
   * @throws NullPointerException The value is null
   * @throws IllegalArgumentException A dimension is out of range.
   * @throws OutOfMemoryError No enough memory to allocate array
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> T[][] create(@NotNull final T value, int lengthX, int lengthY)
  {
    if (lengthX < 0)
      throw new IllegalArgumentException("lengthX=" + lengthX);
    if (lengthY < 0)
      throw new IllegalArgumentException("lengthY=" + lengthY);

    val valueType = value.getClass();
    val array = (T[]) Array.newInstance(valueType, 0);
    val result = (T[][]) Array.newInstance(array.getClass(), lengthX);

    for (int x = 0; x < lengthX; x++)
    {
      result[x] = (T[]) Array.newInstance(valueType, lengthY);
      for (int y = 0; y < lengthY; y++)
        result[x][y] = value;
    }

    return result;
  }

  /**
   * Clones the given array, by invoking clone on all non-null elements of the array. The elements are responsible for performing a
   * successful clone operation of themselves. If the element cloning operation faults, an IllegalArgumentException will be thrown,
   * encapsulating the underlying exception.
   * 
   * @throws IllegalArgumentException The underlying clone() method is inaccessible due to Java access control, or an invocation error
   *           occurred
   * @throws NullPointerException Array is null.
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> T[] deepClone(@NotNull final T[] array)
  {
    try
    {
      val result = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);

      for (int i = 0; i < array.length; i++)
        if (array[i] != null)
          result[i] = (T) ReflectionUtils.getMethod(array[i].getClass(), "clone", true).invoke(array[i], (Object[]) null);

      return result;
    }
    catch(Exception e)
    {
      throw new IllegalArgumentException("Could not perform a deep-clone operation", e);
    }
  }

  /**
   * Returns true if the provided object refers to a primitive array, e.g. int[]. This returns false for object arrays.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException The argument is not a primitive or object array
   */
  @Validate
  public static PrimitiveArrayType getType(@NotNull final Object array)
  {
    String className = array.getClass().getName();
    if (className.charAt(0) != CONSTANT.BRACKET_OPEN_CHAR)
      throw new IllegalArgumentException("The provided object is not an array: " + array.getClass());

    // trim [ from class name
    className = StringUtils.trimStart(className, new char[] {CONSTANT.BRACKET_OPEN_CHAR});

    if (className.length() < 1)
      throw new IllegalArgumentException("The provided object is not an array: " + array.getClass());

    switch(className.charAt(0))
    {
      case 'C':
        return PrimitiveArrayType.Char;
      case 'I':
        return PrimitiveArrayType.Int;
      case 'J':
        return PrimitiveArrayType.Long;
      case 'S':
        return PrimitiveArrayType.Short;
      case 'B':
        return PrimitiveArrayType.Byte;
      case 'F':
        return PrimitiveArrayType.Float;
      case 'D':
        return PrimitiveArrayType.Double;
      case 'Z':
        return PrimitiveArrayType.Boolean;
      default:
        return PrimitiveArrayType.NotPrimitive;
    }
  }

  /**
   * Returns the dimensions of the provided object. The array can be a primitive array, e.g. int[] or an object array e.g. Integer[]
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException The argument is not a primitive or object array
   */
  @Validate
  public static int getDimensions(@NotNull final Object array)
  {
    String className = array.getClass().getName();
    if (className.charAt(0) != CONSTANT.BRACKET_OPEN_CHAR)
      throw new IllegalArgumentException("The provided object is not an array: " + array.getClass());

    // trim [ from class name
    int result = 0;
    while (className.startsWith(CONSTANT.BRACKET_OPEN))
    {
      result++;
      className = className.substring(1);
    }

    return result;
  }

  /**
   * Joins two arrays in the order they were provided.
   * 
   * @throws NullPointerException Array collection is null
   * @throws IllegalStateException Component type cannot be determined.
   */
  @Validate
  public static <T> T[] join(@NotNull T[] first, @NotNull final T[] second)
  {
    if (second.length == 0)
      return first;
    else if (first.length == 0)
      return second;

    int firstLen = first.length;
    int secondLen = second.length;
    int totalLen = firstLen + secondLen;
    first = ArrayUtils.resize(first, totalLen);

    System.arraycopy(second, 0, first, firstLen, secondLen);

    return first;
  }

  /**
   * Joins two or more arrays in the order they were provided. Null arrays are ignored.
   * 
   * @throws NullPointerException Array collection is null
   * @throws IllegalStateException Component type cannot be determined.
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> T[] join(@NotNull final Iterable<T[]> arrays)
  {
    Class<?> componentType = null;

    // determine full length
    int length = 0;
    for (T[] array : arrays)
      if (array != null)
      {
        length += array.length;
        // determine array component type
        if (componentType == null)
          componentType = arrays.getClass().getComponentType();
      }

    if (componentType == null)
      throw new IllegalStateException("Cannot determine component type from array collection.");

    // create big array
    T[] result = (T[]) Array.newInstance(componentType, length);

    // copy all elements over
    int index = 0;
    for (T[] array : arrays)
      if (array != null)
        for (T item : array)
        {
          result[index] = item;
          index++;
        }

    return result;
  }

  /**
   * Adds an element to the given array, at position 0
   * 
   * @throws NullPointerException Array is null.
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> T[] prepend(@NotNull final T[] array, T element)
  {
    val result = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
    System.arraycopy(array, 0, result, 1, array.length);
    result[0] = element;
    return result;
  }

  /**
   * Removes the first encounter of element from the given array, returning an new array if found. Otherwise returns a copy of the array
   * containing all original items.
   * 
   * @throws NullPointerException Array is null.
   */
  @Validate
  public static <T> T[] remove(@NotNull final T[] array, T element)
  {
    val list = new ReifiedArrayList<T>(array);
    list.remove(element);
    return list.toArray();
  }

  /**
   * Removes all occurences of element from the given array, returning an new array if found. Otherwise returns a copy of the array
   * containing all original items.
   * 
   * @throws NullPointerException Array is null.
   */
  @Validate
  public static <T> T[] removeAll(@NotNull final T[] array, T element)
  {
    val list = new ReifiedArrayList<T>(array.length, array.getClass().getComponentType());

    int i = 0;
    if (element == null)
      for (i = 0; i < array.length; i++)
        if (array[i] != null)
          list.add(array[i]);
        else
          for (i = 0; i < array.length; i++)
            if (array[i] == null)
              list.add(array[i]);
            else if (!array[i].equals(element))
              list.add(array[i]);

    return list.toArray();
  }

  /**
   * Removes the element at the given position from the given array.
   * 
   * @throws NullPointerException Array is null
   * @throws IndexOutOfBoundsException Index is out of range
   */
  @Validate
  public static <T> T[] remove(@NotNull final T[] array, int index)
  {
    if (index < 0 || index >= array.length)
      throw new IndexOutOfBoundsException("index=" + index + " length=" + array.length);

    val result = (T[]) ArrayUtils.create(array.getClass().getComponentType(), array.length - 1);
    if (index > 0)
      System.arraycopy(array, 0, result, 0, index);
    if (index != array.length - 1)
      System.arraycopy(array, index + 1, result, index, array.length - index - 1);
    return result;
  }

  /**
   * Re-sizes an array to the specified size. If the new size is smaller, elements get truncated. If the new size if bigger, the new array
   * will have several null-valued elements near its end (i.e. newSize-oldSize in count). If the sizes are equal, the same array is
   * returned.
   * 
   * @throws NullPointerException Array is null
   * @throws IllegalArgumentException Length is out of range
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> T[] resize(@NotNull final T[] array, int length)
  {
    // validate new size
    if (length < 0)
      throw new IllegalArgumentException("length=" + length);

    int oldLength = array.length;

    // check if the sizes match
    if (length == oldLength)
      return array;

    // create new array
    T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), length);

    // select strategy based on given scenario
    if (length > oldLength)
      // newer is larger, use old size as upper bound
      System.arraycopy(array, 0, newArray, 0, oldLength);
    else
      // newer is smaller, use new size as upper bound
      System.arraycopy(array, 0, newArray, 0, length);

    return newArray;
  }

  /**
   * Reverses an array (in place)
   * 
   * @throws NullPointerException Array is null.
   */
  @Validate
  public static <T> T[] reverse(@NotNull final T[] array)
  {
    if (array.length > 1)
    {
      int left = 0; // index of leftmost element
      int right = array.length - 1; // index of rightmost element

      while (left < right)
      {
        // exchange the left and right elements
        val temp = array[left];
        array[left] = array[right];
        array[right] = temp;

        // move the bounds toward the center
        left++;
        right--;
      }
    }

    return array;
  }

  /**
   * Reverses an array (creates a new copy)
   * 
   * @throws NullPointerException Array is null.
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> T[] reverseCopy(@NotNull final T[] array)
  {
    val length = array.length;
    val result = (T[]) Array.newInstance(array.getClass().getComponentType(), length);

    for (int i = 0; i < length; i++)
      result[i] = array[length - 1 - i];

    return result;
  }

  /**
   * Returns a sub-array of the given array
   * 
   * @throws NullPointerException Array is null.
   * @throws IndexOutOfBoundsException Start index / end index are out of range.
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> T[] subArray(@NotNull final T[] array, int startIndex, int endIndex)
  {
    if (startIndex < 0 || startIndex > endIndex)
      throw new IndexOutOfBoundsException("startIndex=" + startIndex + " endIndex=" + endIndex);
    if (endIndex > array.length)
      throw new IndexOutOfBoundsException("endIndex=" + endIndex + " length=" + array.length);

    val result = (T[]) Array.newInstance(array.getClass().getComponentType(), endIndex - startIndex);
    System.arraycopy(array, startIndex, result, 0, endIndex - startIndex);

    return result;
  }

  /**
   * Puts all elements from an array to a collection
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static <T> void toCollection(@NotNull final T[] from, @NotNull final Collection<T> to)
  {
    for (T item : from)
      to.add(item);
  }

  /**
   * Converts an array to a list.
   * 
   * @throws NullPointerException The array is null.
   */
  @Validate
  public static <T> ReifiedList<T> toList(@NotNull final T[] array)
  {
    return new ReifiedArrayList<T>(array);
  }

  /**
   * Returns the given array as a PriorityQueue. Elements are sorted based on their Comparable implementation.
   * 
   * @throws NullPointerException The array is null.
   */
  @Validate
  public static <T extends Comparable<T>> SortedList<T> toSortedList(@NotNull final T[] array)
  {
    val result = new SortedList<T>(array.getClass().getComponentType());
    for (T element : array)
      result.add(element);

    return result;
  }

  /**
   * Returns the given array as a PriorityQueue. Elements are sorted based on their Comparable implementation.
   * 
   * @throws NullPointerException The array is null.
   */
  @Validate
  public static <T extends Comparable<T>> SortedList<T> toSortedList(@NotNull final T[] array, Comparator<? super T> comparator)
  {
    val result = new SortedList<T>(comparator, array.getClass().getComponentType());
    for (T element : array)
      result.add(element);

    return result;
  }

  /**
   * Returns the given array as a LinkedList (a class operating as a Queue or Stack).
   * 
   * @throws NullPointerException The array is null.
   */
  @Validate
  public static <T> ReifiedLinkedList<T> toLinkedList(@NotNull final T[] array)
  {
    val result = new ReifiedLinkedList<T>(array);

    for (T item : array)
      result.addLast(item);

    return result;
  }

  /**
   * Converts a Boolean sequence to primitive boolean[]. Null references use default value false.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean[] unboxBools(@NotNull final Iterable<Boolean> booleans)
  {
    val result = new boolean[Linq.count(booleans)];

    int index = 0;
    for (Boolean val : booleans)
    {
      result[index] = val != null ? val : false;
      index++;
    }

    return result;
  }

  /**
   * Converts a Boolean sequence to primitive boolean[]. Null references use default value 0.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean[] unbox(@NotNull final Boolean[] booleans)
  {
    val result = new boolean[booleans.length];
    for (int i = 0; i < result.length; i++)
      result[i] = booleans[i] != null ? booleans[i] : false;

    return result;
  }

  /**
   * Converts a Byte sequence to primitive byte[]. Null references use default value 0.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static byte[] unboxBytes(@NotNull final Iterable<Byte> bytes)
  {
    val result = new byte[Linq.count(bytes)];

    int index = 0;
    for (Byte val : bytes)
    {
      result[index] = val != null ? val : 0;
      index++;
    }

    return result;
  }

  /**
   * Converts a Byte sequence to primitive byte[]. Null references use default value 0.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static byte[] unbox(@NotNull final Byte[] bytes)
  {
    val result = new byte[bytes.length];
    for (int i = 0; i < result.length; i++)
      result[i] = bytes[i] != null ? bytes[i] : 0;

    return result;
  }

  /**
   * Converts a Character sequence to primitive char[]. Null references use default value 0.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static char[] unboxChars(@NotNull final Iterable<Character> characters)
  {
    val result = new char[Linq.count(characters)];

    int index = 0;
    for (Character val : characters)
    {
      result[index] = val != null ? val : 0;
      index++;
    }

    return result;
  }

  /**
   * Converts a Character sequence to primitive char[]. Null references use default value 0.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static char[] unbox(@NotNull final Character[] characters)
  {
    val result = new char[characters.length];
    for (int i = 0; i < result.length; i++)
      result[i] = characters[i] != null ? characters[i] : 0;

    return result;
  }

  /**
   * Converts a Short sequence to primitive short[]. Null references use default value 0.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static short[] unboxShorts(@NotNull final Iterable<Short> shorts)
  {
    val result = new short[Linq.count(shorts)];

    int index = 0;
    for (Short val : shorts)
    {
      result[index] = val != null ? val : 0;
      index++;
    }

    return result;
  }

  /**
   * Converts a Short sequence to primitive short[]. Null references use default value 0.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static short[] unbox(@NotNull final Short[] shorts)
  {
    val result = new short[shorts.length];
    for (int i = 0; i < result.length; i++)
      result[i] = shorts[i] != null ? shorts[i] : 0;

    return result;
  }

  /**
   * Converts a Integer sequence to primitive int[]. Null references use default value 0.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static int[] unboxInts(@NotNull final Iterable<Integer> ints)
  {
    val result = new int[Linq.count(ints)];

    int index = 0;
    for (Integer val : ints)
    {
      result[index] = val != null ? val : 0;
      index++;
    }

    return result;
  }

  /**
   * Converts a Integer sequence to primitive int[]. Null references use default value 0.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static int[] unbox(@NotNull final Integer[] ints)
  {
    val result = new int[ints.length];
    for (int i = 0; i < result.length; i++)
      result[i] = ints[i] != null ? ints[i] : 0;

    return result;
  }

  /**
   * Converts a Long sequence to primitive long[]. Null references use default value 0.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static long[] unboxLongs(@NotNull final Iterable<Long> longs)
  {
    val result = new long[Linq.count(longs)];

    int index = 0;
    for (Long val : longs)
    {
      result[index] = val != null ? val : 0;
      index++;
    }

    return result;
  }

  /**
   * Converts a Long sequence to primitive long[]. Null references use default value 0.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static long[] unbox(@NotNull final Long[] longs)
  {
    val result = new long[longs.length];
    for (int i = 0; i < result.length; i++)
      result[i] = longs[i] != null ? longs[i] : 0;

    return result;
  }

  /**
   * Converts a Float sequence to primitive float[]. Null references use default value 0.0
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static float[] unboxFloats(@NotNull final Iterable<Float> floats)
  {
    val result = new float[Linq.count(floats)];

    int index = 0;
    for (Float val : floats)
    {
      result[index] = val != null ? val : 0.0f;
      index++;
    }

    return result;
  }

  /**
   * Converts a Float sequence to primitive float[]. Null references use default value 0.0
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static float[] unbox(@NotNull final Float[] longs)
  {
    val result = new float[longs.length];
    for (int i = 0; i < result.length; i++)
      result[i] = longs[i] != null ? longs[i] : 0.0f;

    return result;
  }

  /**
   * Converts a Double sequence to primitive double[]. Null references use default value 0.0
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static double[] unboxDoubles(@NotNull final Iterable<Double> doubles)
  {
    val result = new double[Linq.count(doubles)];

    int index = 0;
    for (Double val : doubles)
    {
      result[index] = val != null ? val : 0.0d;
      index++;
    }

    return result;
  }

  /**
   * Converts a Double sequence to primitive double[]. Null references use default value 0.0
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static double[] unbox(@NotNull final Double[] doubles)
  {
    val result = new double[doubles.length];
    for (int i = 0; i < result.length; i++)
      result[i] = doubles[i] != null ? doubles[i] : 0.0d;

    return result;
  }

  private ArrayUtils()
  {
  }
}
