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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import lombok.Predicates.Predicate1;
import lombok.Functions.Function1;
import lombok.Functions.Function2;
import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.collections.ReifiedIterable;
import propel.core.collections.arrays.ReifiedArray;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.lists.ReifiedList;
import propel.core.collections.maps.ReifiedMap;
import propel.core.collections.maps.avl.AvlHashtable;
import java.lang.SuppressWarnings;
import static lombok.Yield.yield;

/**
 * Provides similar functionality to .NET language integrated queries (LINQ). There are usually two versions of each function, one for
 * operating on Iterables (such as collections) and one with arrays. This is because in Java, arrays are subclasses of Object and not of
 * Iterable, Collection, List, etc.
 */
@SuppressWarnings({"unchecked"})
public final class Linq
{
  /**
   * The default list size to use for collecting results when the result size is unknown
   */
  public static final int DEFAULT_LIST_SIZE = 64;

  /**
   * Private constructor prevents instantiation.
   */
  private Linq()
  {
  }

  /**
   * Applies an accumulator function over a sequence. The specified seed value is used as the initial accumulator value.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static <TSource, TAccumulate> TAccumulate aggregate(@NotNull final Iterable<TSource> values, @NotNull final TAccumulate seed,
                                                             @NotNull final Function2<TAccumulate, ? super TSource, TAccumulate> function)
  {
    return aggregate(values, seed, function, new Function1<TAccumulate, TAccumulate>() {

      @Override
      public TAccumulate apply(TAccumulate arg0)
      {
        return arg0;
      }

    });
  }

  /**
   * Applies an accumulator function over a sequence. The specified seed value is used as the initial accumulator value.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static <TSource, TAccumulate> TAccumulate aggregate(@NotNull final TSource[] values, @NotNull final TAccumulate seed,
                                                             @NotNull final Function2<TAccumulate, ? super TSource, TAccumulate> function)
  {
    return aggregate(values, seed, function, new Function1<TAccumulate, TAccumulate>() {

      @Override
      public TAccumulate apply(TAccumulate arg0)
      {
        return arg0;
      }

    });
  }

  /**
   * Applies an accumulator function over a sequence. The specified seed value is used as the initial accumulator value, and the specified
   * function is used to select the result value from the accumulator's type.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static <TSource, TAccumulate, TResult> TResult
      aggregate(@NotNull final Iterable<TSource> values, @NotNull final TAccumulate seed,
                @NotNull final Function2<TAccumulate, ? super TSource, TAccumulate> function,
                @NotNull final Function1<TAccumulate, TResult> resultSelector)
  {
    TAccumulate result = seed;

    for (TSource item : values)
      result = function.apply(result, item);

    return resultSelector.apply(result);
  }

  /**
   * Applies an accumulator function over a sequence. The specified seed value is used as the initial accumulator value, and the specified
   * function is used to select the result value from the accumulator's type.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static <TSource, TAccumulate, TResult> TResult
      aggregate(@NotNull final TSource[] values, @NotNull final TAccumulate seed,
                @NotNull final Function2<TAccumulate, ? super TSource, TAccumulate> function,
                @NotNull final Function1<TAccumulate, TResult> resultSelector)
  {
    TAccumulate result = seed;

    for (int i = 0; i < values.length; i++)
      result = function.apply(result, values[i]);

    return resultSelector.apply(result);
  }

  /**
   * Returns true if a condition is true for all items in a sequence. Otherwise returns false.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> boolean all(@NotNull final Iterable<T> values, @NotNull final Predicate1<? super T> predicate)
  {
    for (T val : values)
      if (!predicate.apply(val))
        return false;

    return true;
  }

  /**
   * Returns true if a condition is true for all items in a sequence. Otherwise returns false.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> boolean all(@NotNull T[] values, @NotNull final Predicate1<? super T> predicate)
  {
    for (int i = 0; i < values.length; i++)
      if (!predicate.apply(values[i]))
        return false;

    return true;
  }

  /**
   * Returns true if a condition is true for any of the items in a sequence. Otherwise returns false.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static <T> boolean any(@NotNull final Iterable<T> values, @NotNull final Predicate1<? super T> predicate)
  {
    for (T val : values)
      if (predicate.apply(val))
        return true;

    return false;
  }

  /**
   * Returns true if a condition is true for any of the items in a sequence. Otherwise returns false.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static <T> boolean any(@NotNull final T[] values, @NotNull final Predicate1<? super T> predicate)
  {
    int count = values.length;
    for (int i = 0; i < count; i++)
      if (predicate.apply(values[i]))
        return true;

    return false;
  }

  /**
   * Casts a sequence of values of a certain type to a sequence of values of another type. Uses InvalidCastBehaviour.Remove i.e. excluding
   * any elements that do not successfully cast, without throwing exceptions. This operates differently to OfType, in that it forces a cast
   * rather than checking if a TSource is of TDest type.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public static <TSource, TDest> Iterable<TDest> cast(final Iterable<TSource> values, final Class<TDest> destinationClass)
  {
    return cast(values, destinationClass, InvalidCastBehaviour.Remove);
  }

  /**
   * Casts an array of values of a certain type to an array of values of another type. Uses InvalidCastBehaviour.Remove i.e. excluding any
   * elements that do not successfully cast, without throwing exceptions. This operates differently to OfType, in that it forces a cast
   * rather than checking if a TSource is of TDest type.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public static <TSource, TDest> TDest[] cast(TSource[] values, Class<TDest> destClass)
  {
    return (TDest[]) cast(values, destClass, InvalidCastBehaviour.Remove);
  }

  /**
   * Casts a sequence of values of a certain type to a sequence of values of another type, using the specified behaviour upon the event of a
   * cast failure. This operates differently to OfType, in that it forces a cast rather than checking if a TSource is of TDest type.
   * 
   * @throws NullPointerException When an argument is null.
   * @throws IllegalArgumentException Unrecognized cast behaviour.
   */
  @Validate
  public static <TSource, TDest> Iterable<TDest> cast(@NotNull final Iterable<TSource> values,
                                                      @NotNull final Class<TDest> destinationClass, InvalidCastBehaviour castBehaviour)
  {
    switch(castBehaviour)
    {
      case Throw:
        return castThrow(values, destinationClass);
      case Remove:
        return castRemove(values, destinationClass);
      case UseDefault:
        return castUseDefault(values, destinationClass);
      default:
        throw new IllegalArgumentException("Unrecognised cast behaviour: " + castBehaviour);
    }
  }

  /**
   * Casts an array of values of a certain type to an array of values of another type, using the specified behaviour upon the event of a
   * cast failure. This operates differently to OfType, in that it forces a cast rather than checking if a TSource is of TDest type.
   * 
   * @throws NullPointerException When an argument is null.
   * @throws IllegalArgumentException Unrecognized cast behaviour.
   */
  public static <TSource, TDest> TDest[] cast(TSource[] values, Class<TDest> destClass, InvalidCastBehaviour castBehaviour)
  {
    if (values == null)
      throw new NullPointerException("values");

    ReifiedArrayList<TDest> list = new ReifiedArrayList<TDest>(values.length, destClass);

    switch(castBehaviour)
    {
      case Throw:
        castThrow(values, list);
        break;
      case Remove:
        castRemove(values, list);
        break;
      case UseDefault:
        castUseDefault(values, list);
        break;
      default:
        throw new IllegalArgumentException("Unrecognised cast behaviour: " + castBehaviour);
    }

    return toArray(list);
  }

  /**
   * Concatenates two or more sequences
   * 
   * @throws NullPointerException When the values or one of its (Iterable&lt;T&gt;) elements is null.
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> Iterable<T> concat(@NotNull final Iterable<? extends T>... values)
  {
    ReifiedArray<? extends T> array = new ReifiedArray<T>(values);

    for (Iterable<? extends T> vals : array)
    {
      if (vals == null)
        throw new NullPointerException("Item of values");

      for (final T item : vals)
        yield(item);
    }
  }

  /**
   * Concatenates two or more arrays
   * 
   * @throws NullPointerException When the values or one of its elements is null.
   */
  public static <T> T[] concat(T[]... values)
  {
    if (values == null)
      throw new NullPointerException("values");

    Class<?> componentType = null;
    List<T> result = new ArrayList<T>(DEFAULT_LIST_SIZE);

    for (T[] vals : values)
    {
      if (vals == null)
        throw new NullPointerException("Item of values");
      if (componentType == null)
        componentType = vals.getClass().getComponentType();

      for (T item : vals)
        result.add(item);
    }

    return toArray(result, componentType);
  }

  /**
   * Returns true if an item is contained in a sequence. Scans the sequence linearly. You may scan for nulls.
   * 
   * @throws NullPointerException When the values argument is null.
   */
  public static <T> boolean contains(Iterable<T> values, T item)
  {
    if (values == null)
      throw new NullPointerException("values");

    if (item == null)
      return containsNull(values);
    else
      return containsNonNull(values, item);
  }

  /**
   * Returns true if an item is contained in a sequence. Scans the sequence linearly. You may scan for nulls.
   * 
   * @throws NullPointerException When the values argument or the comparer is null.
   */
  @Validate
  public static <T> boolean contains(@NotNull final Iterable<T> values, T item, @NotNull final Comparator<? super T> comparer)
  {
    if (item == null)
      return containsNull(values);
    else
      return containsNonNull(values, item, comparer);
  }

  /**
   * Returns true if an item is contained in a sequence. Scans the sequence linearly. You may scan for nulls.
   * 
   * @throws NullPointerException When the values argument is null.
   */
  @Validate
  public static <T> boolean contains(@NotNull final T[] values, T item)
  {
    if (item == null)
      return containsNull(values);
    else
      return containsNonNull(values, item);
  }

  /**
   * Returns true if an item is contained in a sequence. Scans the sequence linearly. You may scan for nulls.
   * 
   * @throws NullPointerException When the array or the comparer is null.
   */
  @Validate
  public static <T> boolean contains(@NotNull final T[] values, T item, @NotNull final Comparator<? super T> comparer)
  {
    if (item == null)
      return containsNull(values);
    else
      return containsNonNull(values, item, comparer);
  }

  /**
   * Returns true if any of the items are contained in the given values. Scans the values sequence linearly, up to items.Length number of
   * times. You may scan for null.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> boolean containsAny(@NotNull final Iterable<T> values, @NotNull final Iterable<T> items)
  {
    for (T item : items)
      if (contains(values, item))
        return true;

    return false;
  }

  /**
   * Returns true if any of the items are contained in the given values. Scans the values sequence linearly, up to items.Length number of
   * times. You may scan for null.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> boolean
      containsAny(@NotNull final Iterable<T> values, @NotNull final Iterable<T> items, Comparator<? super T> comparer)
  {
    for (T item : items)
      if (contains(values, item, comparer))
        return true;

    return false;
  }

  /**
   * Returns true if any of the items are contained in the given values. Scans the values sequence linearly, up to items.Length number of
   * times. You may scan for null.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> boolean containsAny(@NotNull final T[] values, @NotNull final T[] items)
  {
    for (T item : items)
      if (contains(values, item))
        return true;

    return false;
  }

  /**
   * Returns true if any of the items are contained in the given values. Scans the values sequence linearly, up to items.Length number of
   * times. You may scan for null.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> boolean containsAny(@NotNull final T[] values, @NotNull final T[] items, Comparator<? super T> comparer)
  {
    for (T item : items)
      if (contains(values, item, comparer))
        return true;

    return false;
  }

  /**
   * Returns true if all of the items are contained in the given values. Scans the values sequence linearly, up to items.Length number of
   * times. You may scan for null.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> boolean containsAll(@NotNull final Iterable<T> values, @NotNull final Iterable<T> items)
  {
    for (T item : items)
      if (!contains(values, item))
        return false;

    return true;
  }

  /**
   * Returns true if all of the items are contained in the given values. Scans the values sequence linearly, up to items.Length number of
   * times. You may scan for null.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> boolean
      containsAll(@NotNull final Iterable<T> values, @NotNull final Iterable<T> items, Comparator<? super T> comparer)
  {
    for (T item : items)
      if (!contains(values, item, comparer))
        return false;

    return true;
  }

  /**
   * Returns true if all of the items are contained in the given values. Scans the values sequence linearly, up to items.Length number of
   * times. You may scan for null.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> boolean containsAll(@NotNull final T[] values, @NotNull final T[] items)
  {
    for (T item : items)
      if (!contains(values, item))
        return false;

    return true;
  }

  /**
   * Returns true if all of the items are contained in the given values. Scans the values sequence linearly, up to items.Length number of
   * times. You may scan for null.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> boolean containsAll(@NotNull final T[] values, @NotNull final T[] items, Comparator<? super T> comparer)
  {
    for (T item : items)
      if (!contains(values, item, comparer))
        return false;

    return true;
  }

  // TODO: test this works as expected, use breakpoints

  /**
   * Counts an Iterable in the most efficient manner possible.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> int count(@NotNull final Iterable<T> values)
  {
    if (values instanceof ReifiedList<?>)
    {
      ReifiedList<?> list = (ReifiedList<?>) values;
      return list.size();
    }
    if (values instanceof ReifiedMap<?, ?>)
    {
      ReifiedMap<?, ?> map = (ReifiedMap<?, ?>) values;
      return map.size();
    }
    if (values instanceof Collection<?>)
    {
      Collection<?> list = (Collection<?>) values;
      return list.size();
    }
    if (values instanceof Map<?, ?>)
    {
      Map<?, ?> map = (Map<?, ?>) values;
      return map.size();
    }

    // resort to counting elements one by one
    int result = 0;
    for (T item : values)
      result++;

    return result;
  }

  /**
   * Returns the array length.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> int count(@NotNull final T[] array)
  {
    return array.length;
  }

  /**
   * Returns the number of occurrences of an object in a sequence. It is possible to search for null objects.
   * 
   * @throws NullPointerException When the values argument is null.
   */
  @Validate
  public static <T> int count(@NotNull final Iterable<T> values, T item)
  {
    if (item == null)
      return countNulls(values);
    else
      return countNonNull(values, item);
  }

  /**
   * Returns the number of occurrences of an object in a sequence. It is possible to search for null objects.
   * 
   * @throws NullPointerException When the values argument or the comparer is null.
   */
  @Validate
  public static <T> int count(@NotNull final Iterable<T> values, T item, @NotNull final Comparator<? super T> comparer)
  {
    if (item == null)
      return countNulls(values);
    else
      return countNonNull(values, item, comparer);
  }

  /**
   * Returns the number of occurrences of an object in a sequence. It is possible to search for null objects.
   * 
   * @throws NullPointerException When the values argument is null.
   */
  @Validate
  public static <T> int count(@NotNull final T[] values, T item)
  {
    if (item == null)
      return countNulls(values);
    else
      return countNonNull(values, item);
  }

  /**
   * Returns the number of occurrences of an object in a sequence. It is possible to search for null objects.
   * 
   * @throws NullPointerException When the values argument is null.
   */
  @Validate
  public static <T> int count(@NotNull final T[] values, T item, @NotNull final Comparator<? super T> comparer)
  {
    if (item == null)
      return countNulls(values);
    else
      return countNonNull(values, item, comparer);
  }

  /**
   * Returns the number of occurrences that satisfy the given condition.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> int countWhere(@NotNull final Iterable<? extends T> values, @NotNull final Predicate1<? super T> predicate)
  {
    int counter = 0;
    for (T item : values)
    {
      if (predicate.apply(item))
        counter++;
    }

    return counter;
  }

  /**
   * Returns the number of occurrences that satisfy the given condition.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> int countWhere(@NotNull final T[] values, @NotNull final Predicate1<? super T> predicate)
  {
    int counter = 0;
    for (T item : values)
    {
      if (predicate.apply(item))
        counter++;
    }

    return counter;
  }

  /**
   * Returns the sequence if at least one element exists in it, otherwise returns a collection consisting of a single element which has a
   * null value.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> Iterable<T> defaultIfEmpty(@NotNull final Iterable<T> values)
  {
    boolean empty = true;
    for (T item : values)
    {
      yield(item);
      empty = false;
    }

    if (empty)
      yield(null);
  }

  /**
   * Returns the sequence if at least one element exists in it, otherwise returns a collection consisting of a single element which has a
   * null value.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> T[] defaultIfEmpty(@NotNull final T[] values)
  {
    List<T> result = new ArrayList<T>(DEFAULT_LIST_SIZE);
    for (T item : values)
      result.add(item);

    if (result.size() == 0)
      result.add(null);

    return toArray(result, values.getClass().getComponentType());
  }

  /**
   * Returns distinct (i.e. no duplicate) elements from a sequence.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> Iterable<T> distinct(@NotNull final Iterable<T> values)
  {
    return distinct(values, null);
  }

  /**
   * Returns distinct (i.e. no duplicate) elements from a sequence
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> T[] distinct(@NotNull final T[] values)
  {
    return distinct(values, null);
  }

  /**
   * Returns distinct (i.e. no duplicate) elements from a sequence. Uses the specified comparer to identify duplicates.
   * 
   * @throws NullPointerException When the values argument is null.
   */
  @Validate
  public static <T> Iterable<T> distinct(@NotNull final Iterable<T> values, final Comparator<? super T> comparer)
  {
    Set<T> set;
    if (comparer != null)
      set = new TreeSet<T>(comparer);
    else
      set = new TreeSet<T>();

    for (T item : values)
      if (!set.contains(item))
      {
        set.add(item);
        yield(item);
      }
  }

  /**
   * Returns distinct (i.e. no duplicate) elements from a sequence. Uses the specified comparer to identify duplicates.
   * 
   * @throws NullPointerException When the values argument is null.
   */
  @Validate
  public static <T> T[] distinct(@NotNull final T[] values, Comparator<? super T> comparer)
  {
    ReifiedList<T> list = new ReifiedArrayList<T>(DEFAULT_LIST_SIZE, values.getClass().getComponentType());
    Set<T> set;
    if (comparer != null)
      set = new TreeSet<T>(comparer);
    else
      set = new TreeSet<T>();

    for (T item : values)
      if (!set.contains(item))
      {
        set.add(item);
        list.add(item);
      }

    return list.toArray();
  }

  /**
   * Returns the element at the given position in the provided sequence.
   * 
   * @throws NullPointerException When the values argument is null.
   * @throws IndexOutOfBoundsException When the index is out of range.
   */
  @Validate
  public static <T> T elementAt(@NotNull final Iterable<T> values, int index)
  {
    if (index < 0)
      throw new IndexOutOfBoundsException("index=" + index);

    int i = 0;
    for (T item : values)
    {
      if (i == index)
        return item;
      i++;
    }

    throw new IndexOutOfBoundsException("max=" + i + " index=" + index);
  }

  /**
   * Returns the element at the given position in the provided sequence. It will return null if there is no such index.
   * 
   * @throws NullPointerException When the values argument is null.
   */
  @Validate
  public static <T> T elementAtOrDefault(@NotNull final Iterable<T> values, int index)
  {
    if (index >= 0)
    {
      int i = 0;
      for (T item : values)
      {
        if (i == index)
          return item;
        i++;
      }
    }

    return null;
  }

  /**
   * Returns the element at the given position in the provided sequence. It will return null if there is no such index.
   * 
   * @throws NullPointerException When the values argument is null.
   */
  @Validate
  public static <T> T elementAtOrDefault(@NotNull final T[] values, int index)
  {
    if (index >= 0 && index < values.length)
      return values[index];

    return null;
  }

  /**
   * Returns all distinct values except the specified removed values.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> Iterable<T> except(@NotNull final Iterable<T> values, @NotNull final Iterable<T> removedValues)
  {
    return except(values, removedValues, null);
  }

  /**
   * Returns all distinct values except the specified removed values.
   * 
   * @throws NullPointerException When the values or removedValues argument is null.
   */
  @Validate
  public static <T> Iterable<T> except(@NotNull final Iterable<T> values, @NotNull final Iterable<T> removedValues,
                                       final Comparator<? super T> comparer)
  {
    Iterable<T> distinctValues;
    Iterable<T> distinctRemovedValues;

    if (comparer == null)
    {
      distinctValues = distinct(values);
      distinctRemovedValues = distinct(removedValues);
    } else
    {
      distinctValues = distinct(values, comparer);
      distinctRemovedValues = distinct(removedValues, comparer);
    }

    for (T item : distinctValues)
      if (!contains(distinctRemovedValues, item))
        yield(item);
  }

  /**
   * Returns all distinct values except the specified removed values.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> T[] except(@NotNull final T[] values, @NotNull final T[] removedValues)
  {
    return except(values, removedValues, null);
  }

  /**
   * Returns all distinct values except the specified removed values.
   * 
   * @throws NullPointerException When the values or removedValues argument is null.
   */
  @Validate
  public static <T> T[] except(@NotNull final T[] values, @NotNull final T[] removedValues, Comparator<? super T> comparer)
  {
    T[] distinctValues;
    T[] distinctRemovedValues;

    if (comparer == null)
    {
      distinctValues = distinct(values);
      distinctRemovedValues = distinct(removedValues);
    } else
    {
      distinctValues = distinct(values, comparer);
      distinctRemovedValues = distinct(removedValues, comparer);
    }

    List<T> result = new ArrayList<T>(DEFAULT_LIST_SIZE);

    for (T item : distinctValues)
      if (!contains(distinctRemovedValues, item))
        result.add(item);

    return toArray(result, values.getClass().getComponentType());
  }

  /**
   * Returns the first element in the provided sequence.
   * 
   * @throws NullPointerException When an argument is null.
   * @throws NoSuchElementException There is no first element.
   */
  @Validate
  public static <T> T first(@NotNull final Iterable<T> values)
  {
    for (T item : values)
      return item;

    throw new NoSuchElementException("The iterable is empty.");
  }

  /**
   * Returns the first element in the provided sequence.
   * 
   * @throws NullPointerException When an argument is null.
   * @throws NoSuchElementException There is no first element.
   */
  @Validate
  public static <T> T first(@NotNull final T[] values)
  {
    if (values.length <= 0)
      throw new NoSuchElementException("The array is empty.");

    return values[0];
  }

  /**
   * Returns the first element in the provided sequence that matches a condition.
   * 
   * @throws NullPointerException When an argument is null.
   * @throws NoSuchElementException There is no match to the given predicate.
   */
  @Validate
  public static <T> T first(@NotNull final Iterable<T> values, Predicate1<? super T> predicate)
  {
    for (T element : values)
      if (predicate.apply(element))
        return element;

    throw new NoSuchElementException("There is no match to the given predicate.");
  }

  /**
   * Returns the first element in the provided sequence that matches a condition.
   * 
   * @throws NullPointerException When an argument is null.
   * @throws NoSuchElementException There is no match to the given predicate.
   */
  @Validate
  public static <T> T first(@NotNull final T[] values, @NotNull final Predicate1<? super T> predicate)
  {
    int count = values.length;
    for (int i = 0; i < count; i++)
    {
      T element = values[i];
      if (predicate.apply(element))
        return element;
    }

    throw new NoSuchElementException("There is no match to the given predicate.");
  }

  /**
   * Returns the first element in the provided sequence. If no element is found, then null is returned.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> T firstOrDefault(@NotNull final Iterable<T> values)
  {
    for (T item : values)
      return item;

    return null;
  }

  /**
   * Returns the first element in the provided sequence that matches a condition. It will return the default value of T if there is no
   * match.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> T firstOrDefault(@NotNull final Iterable<T> values, @NotNull final Predicate1<? super T> predicate)
  {
    for (T element : values)
      if (predicate.apply(element))
        return element;

    return null;
  }

  /**
   * Returns the first element in the provided array. If no element is found, then null is returned.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> T firstOrDefault(@NotNull final T[] values)
  {
    if (values.length == 0)
      return null;

    return values[0];
  }

  /**
   * Returns the first element in the provided sequence that matches a condition. It will return the default value of T if there is no
   * match.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> T firstOrDefault(@NotNull final T[] values, Predicate1<? super T> predicate)
  {
    int count = values.length;
    for (int i = 0; i < count; i++)
    {
      T element = values[i];
      if (predicate.apply(element))
        return element;
    }

    return null;
  }

  /**
   * Groups elements by a specified key.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <TKey, TResult> Iterable<TResult> groupBy(@NotNull final Iterable<TResult> values,
                                                          @NotNull final Function1<TResult, TKey> keySelector)
  {
    return groupBy(values, keySelector, null);
  }

  /**
   * Groups elements by a specified key.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <TKey, TResult> TResult[] groupBy(@NotNull final TResult[] values, @NotNull final Function1<TResult, TKey> keySelector)
  {
    return groupBy(values, keySelector, null);
  }

  /**
   * Groups elements by a specified key and comparer.
   * 
   * @throws NullPointerException When the values argument or the key selector is null.
   */
  @Validate
  public static <TKey, TResult> Iterable<TResult> groupBy(@NotNull final Iterable<TResult> values,
                                                          @NotNull final Function1<TResult, TKey> keySelector,
                                                          final Comparator<? super TKey> comparer)
  {
    TreeMap<TKey, TResult> lookup;
    if (comparer == null)
      lookup = new TreeMap<TKey, TResult>();
    else
      lookup = new TreeMap<TKey, TResult>(comparer);

    for (TResult item : values)
    {
      TKey key = keySelector.apply(item);

      if (!lookup.containsKey(key))
        lookup.put(key, item);
    }

    for (TResult result : lookup.values())
      yield(result);
  }

  /**
   * Groups elements by a specified key and comparer.
   * 
   * @throws NullPointerException When the values argument or the key selector is null.
   */
  @Validate
  public static <TKey, TResult> TResult[] groupBy(@NotNull final TResult[] values, @NotNull final Function1<TResult, TKey> keySelector,
                                                  final Comparator<? super TKey> comparer)
  {
    TreeMap<TKey, TResult> lookup;
    if (comparer == null)
      lookup = new TreeMap<TKey, TResult>();
    else
      lookup = new TreeMap<TKey, TResult>(comparer);

    for (TResult item : values)
    {
      TKey key = keySelector.apply(item);

      if (!lookup.containsKey(key))
        lookup.put(key, item);
    }

    return toArray(lookup.values(), values.getClass().getComponentType());
  }

  /**
   * Returns the index where the specified element is first found. You may search for nulls. If the element is not found, this returns -1.
   * 
   * @throws NullPointerException When the values argument is null.
   */
  @Validate
  public static <T> int indexOf(@NotNull final Iterable<T> values, T element)
  {
    return element == null ? indexOfNull(values) : indexOfNotNull(values, element);
  }

  /**
   * Returns the index where the specified element is first found. You may search for nulls. If the element is not found, this returns -1.
   * 
   * @throws NullPointerException When the values or the comparer argument is null.
   */
  @Validate
  public static <T> int indexOf(@NotNull final Iterable<T> values, T element, final Comparator<? super T> comparer)
  {
    return element == null ? indexOfNull(values) : indexOfNotNull(values, element, comparer);
  }

  /**
   * Returns the index where the specified element is first found. You may search for nulls. If the element is not found, this returns -1.
   * 
   * @throws NullPointerException When the values argument is null.
   */
  @Validate
  public static <T> int indexOf(@NotNull final T[] values, T element)
  {
    return element == null ? indexOfNull(values) : indexOfNotNull(values, element);
  }

  /**
   * Returns the index where the specified element is first found. You may search for nulls. If the element is not found, this returns -1.
   * 
   * @throws NullPointerException When the values argument is null.
   */
  @Validate
  public static <T> int indexOf(@NotNull final T[] values, T element, final Comparator<? super T> comparer)
  {
    return element == null ? indexOfNull(values) : indexOfNotNull(values, element, comparer);
  }

  /**
   * Returns the intersection of the distinct elements of two sequences.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> Iterable<T> intersect(@NotNull final Iterable<T> first, @NotNull final Iterable<T> second)
  {
    return intersect(first, second, null);
  }

  /**
   * Returns the intersection of the distinct elements of two sequences.
   * 
   * @throws NullPointerException When the first or second argument is null.
   */
  @Validate
  public static <T> Iterable<T> intersect(@NotNull final Iterable<T> first, @NotNull final Iterable<T> second,
                                          final Comparator<? super T> comparer)
  {
    Iterable<T> distinctFirst;
    Iterable<T> distinctSecond;

    if (comparer == null)
    {
      distinctFirst = distinct(first);
      distinctSecond = distinct(second);
    } else
    {
      distinctFirst = distinct(first, comparer);
      distinctSecond = distinct(second, comparer);
    }

    for (T item : distinctFirst)
      if (contains(distinctSecond, item))
        yield(item);
  }

  /**
   * Returns the intersection of the distinct elements of two sequences.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> T[] intersect(@NotNull final T[] first, @NotNull final T[] second)
  {
    return intersect(first, second, null);
  }

  /**
   * Returns the intersection of the distinct elements of two sequences.
   * 
   * @throws NullPointerException When the first or second argument is null.
   */
  @Validate
  public static <T> T[] intersect(@NotNull final T[] first, @NotNull final T[] second, final Comparator<? super T> comparer)
  {
    List<T> result = new ArrayList<T>(DEFAULT_LIST_SIZE);

    T[] distinctFirst;
    T[] distinctSecond;

    if (comparer == null)
    {
      distinctFirst = distinct(first);
      distinctSecond = distinct(second);
    } else
    {
      distinctFirst = distinct(first, comparer);
      distinctSecond = distinct(second, comparer);
    }

    for (T item : distinctFirst)
      if (contains(distinctSecond, item))
        result.add(item);

    return toArray(result, first.getClass().getComponentType());
  }

  /**
   * Returns true if the sequence is empty.
   * 
   * @throws NullPointerException Is the argument is null.
   */
  @Validate
  public static <T> boolean isEmpty(@NotNull final Iterable<T> values)
  {
    for (T val : values)
      return false;

    return true;
  }

  /**
   * Returns true if the array is empty.
   * 
   * @throws NullPointerException Is the argument is null.
   */
  @Validate
  public static <T> boolean isEmpty(@NotNull final T[] values)
  {
    return values.length == 0;
  }

  /**
   * Returns the last element in the iterable.
   * 
   * @throws NullPointerException If the array is null
   * @throws NoSuchElementException If the iterable is empty
   */
  @Validate
  public static <T> T last(@NotNull final Iterable<T> values)
  {
    // check if any items present
    T last;
    try
    {
      last = first(values);
    }
    catch(NoSuchElementException e)
    {
      // no last element
      throw new NoSuchElementException("The iterable is empty.");
    }

    // find last
    for (T item : values)
      last = item;

    return last;
  }

  /**
   * Returns the last element in the array.
   * 
   * @throws NullPointerException If the array is null
   * @throws NoSuchElementException If the array is empty
   */
  @Validate
  public static <T> T last(@NotNull final T[] array)
  {
    if (array.length <= 0)
      throw new NoSuchElementException("The array is empty.");

    return array[array.length - 1];
  }

  /**
   * Returns the last element in the provided sequence that matches a condition.
   * 
   * @throws NullPointerException The values argument is null.
   * @throws NoSuchElementException There is no match to the given predicate
   */
  @Validate
  public static <T> T last(@NotNull final Iterable<T> values, @NotNull final Predicate1<? super T> predicate)
  {
    T result = null;
    boolean found = false;

    for (T element : values)
      if (predicate.apply(element))
      {
        found = true;
        result = element;
      }

    if (found)
      return result;

    throw new NoSuchElementException("There is no match to the given predicate.");
  }

  /**
   * Returns the last element in the provided sequence that matches a condition.
   * 
   * @throws NullPointerException The values argument is null.
   * @throws NoSuchElementException There is no match to the given predicate
   */
  @Validate
  public static <T> T last(@NotNull final T[] values, @NotNull final Predicate1<? super T> predicate)
  {
    T result = null;
    boolean found = false;

    for (T element : values)
      if (predicate.apply(element))
      {
        found = true;
        result = element;
      }

    if (found)
      return result;

    throw new NoSuchElementException("There is no match to the given predicate.");
  }

  /**
   * Returns the last element in the provided sequence. It will return null if there is no match.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> T lastOrDefault(@NotNull final Iterable<T> values)
  {
    T result = null;
    boolean found = false;

    for (T item : values)
    {
      result = item;
      found = true;
    }

    return found ? result : (T) null;
  }

  /**
   * Returns the last element in the provided array. It will return null if there is no match.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> T lastOrDefault(@NotNull final T[] values)
  {
    if (values.length <= 0)
      return null;

    return values[values.length - 1];
  }

  /**
   * Returns the last element in the provided sequence that matches a condition. It will return null if there is no match.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> T lastOrDefault(@NotNull final Iterable<T> values, @NotNull final Predicate1<? super T> predicate)
  {
    T result = null;

    for (T element : values)
      if (predicate.apply(element))
        result = element;

    return result;
  }

  /**
   * Returns the last element in the provided sequence that matches a condition. It will return null if there is no match.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> T lastOrDefault(@NotNull final T[] values, @NotNull final Predicate1<? super T> predicate)
  {
    T result = null;

    int count = values.length;
    for (int i = 0; i < count; i++)
    {
      T element = values[i];
      if (predicate.apply(element))
        result = element;
    }

    return result;
  }

  /**
   * Returns the last index where the specified element is found. You may search for nulls. If the element is not found, this returns -1.
   * 
   * @throws NullPointerException If the values argument is null.
   */
  @Validate
  public static <T> int lastIndexOf(@NotNull final Iterable<? super T> values, T element)
  {
    return element == null ? lastIndexOfNull(values) : lastIndexOfNotNull(values, element);
  }

  /**
   * Returns the last index where the specified element is found. You may search for nulls. If the element is not found, this returns -1.
   * 
   * @throws NullPointerException If the values or comparer argument is null.
   */
  @Validate
  public static <T> int lastIndexOf(@NotNull final Iterable<T> values, final T element, final Comparator<? super T> comparer)
  {
    return element == null ? lastIndexOfNull(values) : lastIndexOfNotNull(values, element, comparer);
  }

  /**
   * Returns the last index where the specified element is first found. You may search for nulls. If the element is not found, this returns
   * -1.
   * 
   * @throws NullPointerException If the values argument is null.
   */
  @Validate
  public static <T> int lastIndexOf(@NotNull final T[] values, final T element)
  {
    return element == null ? lastIndexOfNull(values) : lastIndexOfNotNull(values, element);
  }

  /**
   * Returns the last index where the specified element is first found. You may search for nulls. If the element is not found, this returns
   * -1.
   * 
   * @throws NullPointerException If the values or comparer argument is null.
   */
  @Validate
  public static <T> int lastIndexOf(@NotNull final T[] values, final T element, final Comparator<? super T> comparer)
  {
    return element == null ? lastIndexOfNull(values) : lastIndexOfNotNull(values, element, comparer);
  }

  /**
   * Performs an inner join (more specifically an equi-join) over two sequences. The outer values are filtered for key uniqueness (first
   * encountered key kept), whereas inner values may be non-unique.
   * 
   * @throws NullPointerException If an argument is null.
   */
  @Validate
  public static <TOuter, TInner, TKey extends Comparable<TKey>, TResult> Iterable<TResult>
      join(@NotNull final Iterable<TOuter> outerValues, @NotNull final Iterable<TInner> innerValues,
           @NotNull final Function1<TOuter, TKey> outerKeySelector, @NotNull final Function1<TInner, TKey> innerKeySelector,
           @NotNull final Function2<TOuter, TInner, TResult> resultSelector)
  {
    AvlHashtable<TKey, TOuter> lookup = new AvlHashtable<TKey, TOuter>(outerKeySelector.getReturnType(),
        outerKeySelector.getParameterType1());

    for (TOuter value : outerValues)
    {
      TKey outerKey = outerKeySelector.apply(value);
      lookup.add(outerKey, value);
    }

    for (TInner inner : innerValues)
    {
      TKey innerKey = innerKeySelector.apply(inner);

      if (lookup.containsKey(innerKey))
      {
        TOuter outer = lookup.get(innerKey);
        TResult res = resultSelector.apply(outer, inner);
        yield(res);
      }
    }
  }

  /**
   * Performs an inner join (more specifically an equi-join) over two sequences. The outer values are filtered for key uniqueness (first
   * encountered key kept), whereas inner values may be non-unique.
   * 
   * @throws NullPointerException If an argument is null.
   */
  @Validate
  public static <TOuter, TInner, TKey extends Comparable<TKey>, TResult> TResult[]
      join(@NotNull final TOuter[] outerValues, @NotNull final TInner[] innerValues,
           @NotNull final Function1<TOuter, TKey> outerKeySelector, @NotNull final Function1<TInner, TKey> innerKeySelector,
           @NotNull final Function2<TOuter, TInner, TResult> resultSelector)
  {
    List<TResult> result = new ArrayList<TResult>(DEFAULT_LIST_SIZE);
    AvlHashtable<TKey, TOuter> lookup = new AvlHashtable<TKey, TOuter>(outerKeySelector.getReturnType(), outerKeySelector.getReturnType());

    for (TOuter value : outerValues)
    {
      TKey outerKey = outerKeySelector.apply(value);
      lookup.add(outerKey, value);
    }

    for (TInner inner : innerValues)
    {
      TKey innerKey = innerKeySelector.apply(inner);

      if (lookup.containsKey(innerKey))
      {
        TOuter outer = lookup.get(innerKey);
        TResult res = resultSelector.apply(outer, inner);
        result.add(res);
      }
    }

    return toArray(result, resultSelector.getReturnType());
  }

  /**
   * Returns all values in a sequence that are of a particular type. This operates differently to Cast, in that it does not force a cast; it
   * rather checks if a TSource is of TDest type.
   * 
   * @throws NullPointerException When the argument is null.
   */
  @Validate
  public static <TSource, TDest> Iterable<TDest> ofType(@NotNull final Iterable<TSource> values,
                                                        @NotNull final Class<TDest> destinationClass)
  {
    for (TSource item : values)
    {
      TDest temp = null;
      try
      {
        temp = destinationClass.cast(item);
      }
      catch(ClassCastException e)
      {}

      yield(temp); 
    }
  }

  /**
   * Returns all values in a sequence that are of a particular type. This operates differently to Cast, in that it does not force a cast; it
   * rather checks if a TSource is of TDest type.
   * 
   * @throws NullPointerException When the argument is null.
   */
  @Validate
  public static <TSource, TDest> TDest[] ofType(@NotNull final TSource[] values, @NotNull final Class<TDest> destinationClass)
  {
    ReifiedList<TDest> result = new ReifiedArrayList<TDest>(DEFAULT_LIST_SIZE, destinationClass);

    for (TSource item : values)
    {
      try
      {
        TDest temp = destinationClass.cast(item);
        result.add(temp);
      }
      catch(ClassCastException e)
      {
        continue;
      }
    }

    return result.toArray();
  }

  /**
   * Orders a sequence by a specified key.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <TKey extends Comparable<TKey>, TResult> Iterable<TResult> orderBy(@NotNull final Iterable<TResult> values,
                                                                                   @NotNull final Function1<TResult, TKey> keySelector)
  {
    return orderBy(values, keySelector, null);
  }

  /**
   * Orders a sequence by a specified key.
   * 
   * @throws NullPointerException When the values or keySelector argument is null.
   */
  @Validate
  public static <TKey extends Comparable<TKey>, TResult> ReifiedList<TResult> orderBy(@NotNull final Iterable<TResult> values,
                                                                                      @NotNull final Function1<TResult, TKey> keySelector,
                                                                                      @NotNull final Comparator<? super TKey> comparer)
  {
    TreeMap<TKey, List<TResult>> dict;
    if (comparer == null)
      dict = new TreeMap<TKey, List<TResult>>();
    else
      dict = new TreeMap<TKey, List<TResult>>(comparer);

    for (TResult item : values)
    {
      TKey key = keySelector.apply(item);
      if (dict.containsKey(key))
        dict.get(key).add(item);
      else
      {
        List<TResult> val = new ArrayList<TResult>();
        val.add(item);
        dict.put(key, val);
      }
    }

    ReifiedList<TResult> result = new ReifiedArrayList<TResult>(DEFAULT_LIST_SIZE, keySelector.getParameterType1());
    for (List<TResult> list : dict.values())
      result.addAll(list);

    return result;
  }

  /**
   * Orders a sequence by a specified key.
   * 
   * @throws NullPointerException When the values or keySelector argument is null.
   */
  @Validate
  public static <TKey, TResult> TResult[] orderBy(@NotNull final TResult[] values, @NotNull final Function1<TResult, TKey> keySelector,
                                                  @NotNull final Comparator<? super TKey> comparer)
  {
    TreeMap<TKey, List<TResult>> dict;
    if (comparer == null)
      dict = new TreeMap<TKey, List<TResult>>();
    else
      dict = new TreeMap<TKey, List<TResult>>(comparer);

    for (TResult item : values)
    {
      TKey key = keySelector.apply(item);
      if (dict.containsKey(key))
        dict.get(key).add(item);
      else
      {
        List<TResult> val = new ArrayList<TResult>();
        val.add(item);
        dict.put(key, val);
      }
    }

    ReifiedList<TResult> result = new ReifiedArrayList<TResult>(DEFAULT_LIST_SIZE, keySelector.getParameterType1());
    for (List<TResult> list : dict.values())
      result.addAll(list);

    return result.toArray();
  }

  /**
   * Orders a sequence by a specified key.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <TKey extends Comparable<TKey>, TResult> TResult[] orderBy(@NotNull final TResult[] values,
                                                                           @NotNull final Function1<TResult, TKey> keySelector)
  {
    return orderBy(values, keySelector, null);
  }

  /**
   * Orders a sequence by a specified key and matching key results get sorted by a second key.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <TKey extends Comparable<TKey>, TKey2 extends Comparable<TKey>, TResult> Iterable<TResult>
      orderByThenBy(@NotNull final Iterable<TResult> values, @NotNull final Function1<TResult, TKey> keySelector,
                    @NotNull final Function1<TResult, TKey2> keySelector2)
  {
    return orderByThenBy(values, keySelector, null, keySelector2, null);
  }

  /**
   * Orders a sequence by a specified key and matching key results get sorted by a second key.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <TKey extends Comparable<TKey>, TKey2 extends Comparable<TKey>, TResult> TResult[]
      orderByThenBy(@NotNull final TResult[] values, @NotNull final Function1<TResult, TKey> keySelector,
                    @NotNull final Function1<TResult, TKey2> keySelector2)
  {
    return orderByThenBy(values, keySelector, null, keySelector2, null);
  }

  /**
   * Orders a sequence by a specified key and matching key results get sorted by a second key.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <TKey, TKey2, TResult> Iterable<TResult> orderByThenBy(@NotNull final Iterable<TResult> values,
                                                                       @NotNull final Function1<TResult, TKey> keySelector,
                                                                       @NotNull final Comparator<? super TKey> comparer,
                                                                       @NotNull final Function1<TResult, TKey2> keySelector2,
                                                                       @NotNull final Comparator<? super TKey2> comparer2)
  {
    TreeMap<TKey, TreeMap<TKey2, List<TResult>>> dict;
    if (comparer == null)
      dict = new TreeMap<TKey, TreeMap<TKey2, List<TResult>>>();
    else
      dict = new TreeMap<TKey, TreeMap<TKey2, List<TResult>>>(comparer);

    for (TResult item : values)
    {
      TKey key = keySelector.apply(item);
      TKey2 key2 = keySelector2.apply(item);

      if (dict.containsKey(key))
      {
        if (dict.get(key).containsKey(key2))
          dict.get(key).get(key2).add(item);
        else
        {
          List<TResult> lst = new ArrayList<TResult>();
          lst.add(item);
          dict.get(key).put(key2, lst);
        }
      } else
      {
        TreeMap<TKey2, List<TResult>> secondDictionary;

        if (comparer2 == null)
          secondDictionary = new TreeMap<TKey2, List<TResult>>();
        else
          secondDictionary = new TreeMap<TKey2, List<TResult>>(comparer2);

        List<TResult> lst = new ArrayList<TResult>();
        lst.add(item);
        secondDictionary.put(key2, lst);
        dict.put(key, secondDictionary);
      }
    }

    // get all lists and combine into one resultant list
    ReifiedList<TResult> result = new ReifiedArrayList<TResult>(DEFAULT_LIST_SIZE, keySelector2.getReturnType());
    // get all secondary dictionaries
    for (TreeMap<TKey2, List<TResult>> tree : dict.values())
      for (List<TResult> list : tree.values())
        result.addAll(list);

    return result;
  }

  /**
   * Orders a sequence by a specified key and matching key results get sorted by a second key.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <TKey, TKey2, TResult> TResult[] orderByThenBy(@NotNull final TResult[] values,
                                                               @NotNull final Function1<TResult, TKey> keySelector,
                                                               @NotNull final Comparator<? super TKey> comparer,
                                                               @NotNull final Function1<TResult, TKey2> keySelector2,
                                                               @NotNull final Comparator<? super TKey2> comparer2)
  {
    TreeMap<TKey, TreeMap<TKey2, List<TResult>>> dict;
    if (comparer == null)
      dict = new TreeMap<TKey, TreeMap<TKey2, List<TResult>>>();
    else
      dict = new TreeMap<TKey, TreeMap<TKey2, List<TResult>>>(comparer);

    for (TResult item : values)
    {
      TKey key = keySelector.apply(item);
      TKey2 key2 = keySelector2.apply(item);

      if (dict.containsKey(key))
      {
        if (dict.get(key).containsKey(key2))
          dict.get(key).get(key2).add(item);
        else
        {
          List<TResult> lst = new ArrayList<TResult>();
          lst.add(item);
          dict.get(key).put(key2, lst);
        }
      } else
      {
        TreeMap<TKey2, List<TResult>> secondDictionary;

        if (comparer2 == null)
          secondDictionary = new TreeMap<TKey2, List<TResult>>();
        else
          secondDictionary = new TreeMap<TKey2, List<TResult>>(comparer2);

        List<TResult> lst = new ArrayList<TResult>();
        lst.add(item);
        secondDictionary.put(key2, lst);
        dict.put(key, secondDictionary);
      }
    }

    // get all lists and combine into one resultant list
    ReifiedList<TResult> result = new ReifiedArrayList<TResult>(DEFAULT_LIST_SIZE, keySelector2.getReturnType());
    // get all secondary dictionaries
    for (TreeMap<TKey2, List<TResult>> tree : dict.values())
      for (List<TResult> list : tree.values())
        result.addAll(list);

    return result.toArray();
  }

  /**
   * Returns a range from the provided sequence. Inclusiveness is [start, finish) i.e. as in a For loop.
   * 
   * @throws NullPointerException The values argument is null.
   * @throws IndexOutOfBoundsException An index is out of range.
   */
  @Validate
  public static <T> Iterable<T> range(@NotNull final Iterable<T> values, final int start, final int finish)
  {
    if (values == null)
      throw new NullPointerException("values");
    if (start < 0)
      throw new IndexOutOfBoundsException("start=" + start);
    if (finish < start)
      throw new IndexOutOfBoundsException("start=" + start + " finish=" + finish);

    int index = 0;
    for (T item : values)
    {
      if (index >= finish)
        break;
      if (index >= start)
        yield(item);

      index++;
    }

    if (index < finish)
      throw new IndexOutOfBoundsException("max=" + index + " finish=" + finish);
  }

  /**
   * Returns a range from the provided sequence. Inclusiveness is [start, finish) i.e. as in a For loop.
   * 
   * @throws NullPointerException The values argument is null.
   * @throws IndexOutOfBoundsException An index is out of range.
   */
  @Validate
  public static <T> T[] range(@NotNull final T[] values, final int start, final int finish)
  {
    if (start < 0)
      throw new IndexOutOfBoundsException("start=" + start);
    if (finish < start || finish > values.length)
      throw new IndexOutOfBoundsException("start=" + start + " finish=" + finish + " length=" + values.length);

    List<T> result = new ArrayList<T>(finish - start);

    for (int i = start; i < finish; i++)
      result.add(values[i]);

    return toArray(result, values.getClass().getComponentType());
  }

  /**
   * Returns a range of values, from start to end (exclusive). The value is incremented using the specified stepping function.
   * 
   * @throws NullPointerException The step function argument is null.
   */
  @Validate
  public static <T> Iterable<T> range(@NotNull final T start, @NotNull final T end, @NotNull final Function1<T, T> stepFunction)
  {
    T current = start;

    if (start == null || end == null)
      while (current != end)
      {
        yield(current);
        current = stepFunction.apply(current);
      }
    else
      while (!current.equals(end))
      {
        yield(current);
        current = stepFunction.apply(current);
      }
  }

  /**
   * Returns a range of values, by using a step function, until the predicate returns false
   * 
   * @throws NullPointerException The predicate or step function argument is null.
   */
  @Validate
  public static <T> Iterable<T> range(@NotNull final T start, @NotNull final Predicate1<? super T> predicate,
                                      @NotNull final Function1<T, T> stepFunction)
  {
    T current = start;
    while (predicate.apply(current))
    {
      yield(current);
      current = stepFunction.apply(current);
    }
  }

  /**
   * Returns a collection of specified size
   * 
   * @throws IllegalArgumentException The count is out of range.
   * @throws NullPointerException When the destination class is null.
   */
  @Validate
  public static <T> Iterable<T> repeat(@NotNull final T value, final int count)
  {
    if (count < 0)
      throw new IllegalArgumentException("count=" + count);

    for (int i = 0; i < count; i++)
      yield(value);
  }

  /**
   * Returns a reversed version of the provided sequence
   * 
   * @throws NullPointerException When the argument is null.
   */
  @Validate
  public static <T> List<T> reverse(@NotNull final List<T> values)
  {
    Collections.reverse(values);

    return values;
  }

  /**
   * Returns a reversed version of the provided sequence
   * 
   * @throws NullPointerException When the argument is null.
   */
  @Validate
  public static <T> Iterable<T> reverse(@NotNull final Iterable<T> values)
  {
    List<T> result = new ArrayList<T>(DEFAULT_LIST_SIZE);
    for (T item : values)
      result.add(item);

    Collections.reverse(result);

    return result;
  }

  /**
   * Returns a reversed version of the provided sequence
   * 
   * @throws NullPointerException When the argument is null.
   */
  @Validate
  public static <T> ReifiedList<T> reverse(@NotNull final ReifiedIterable<T> values)
  {
    ReifiedList<T> result = new ReifiedArrayList<T>(values);
    Collections.reverse(result);

    return result;
  }

  /**
   * Returns a reversed version of the provided array
   * 
   * @throws NullPointerException When the argument is null.
   */
  @Validate
  public static <T> T[] reverse(@NotNull final T[] values)
  {
    List<T> result = Arrays.asList(values);
    Collections.reverse(result);

    return toArray(result, values.getClass().getComponentType());
  }

  /**
   * Acts as a Select LINQ function. It will never return null.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <TSource, TResult> Iterable<TResult> select(@NotNull final Iterable<TSource> values,
                                                            @NotNull final Function1<TSource, TResult> selector)
  {
    for (TSource item : values)
      yield(selector.apply(item));
  }

  /**
   * Acts as a Select LINQ function. It will never return null.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <TSource, TResult> TResult[] select(@NotNull final TSource[] values, @NotNull final Function1<TSource, TResult> selector)
  {
    List<TResult> result = new ArrayList<TResult>(values.length);

    for (TSource item : values)
      result.add(selector.apply(item));

    return toArray(result, selector.getReturnType());
  }

  /**
   * Acts as a SelectMany LINQ function, to allow selection of iterables and return all their sub-items.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <TSource, TResult> Iterable<TResult> selectMany(@NotNull final Iterable<TSource> values,
                                                                @NotNull final Function1<TSource, ReifiedList<TResult>> selector)
  {
    for (TSource item : values)
    {
      ReifiedList<TResult> subItems = selector.apply(item);
      if (subItems != null)
      {
        for (TResult subItem : subItems)
          yield(subItem);
      }
    }
  }

  /**
   * Acts as a SelectMany LINQ function, to allow selection of iterables and return all their sub-items.
   * 
   * @throws NullPointerException When an argument is null.
   * @throws IllegalArgumentException When the run-time type of the resulting array cannot be determined.
   */
  @Validate
  public static <TSource, TResult> TResult[] selectMany(@NotNull final TSource[] values, Function1<TSource, ReifiedList<TResult>> selector)
  {
    List<TResult> result = new ArrayList<TResult>(DEFAULT_LIST_SIZE);
    Class<?> resultClass = null;

    for (TSource item : values)
    {
      ReifiedList<TResult> subItems = selector.apply(item);
      if (subItems != null)
      {
        for (TResult subItem : subItems)
          result.add(subItem);
        if (resultClass == null)
          resultClass = subItems.getGenericTypeParameter();
      }
    }

    if (resultClass == null)
      throw new IllegalArgumentException("Could not determine run-time type because all selection results were null.");

    return toArray(result, resultClass);
  }

  /**
   * Returns true if both iterables have the same values in the exact same positions.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static <T> boolean sequenceEqual(@NotNull final Iterable<? super T> values1, @NotNull final Iterable<T> values2)
  {
    if (count(values1) != count(values2))
      return false;
    else
    {
      Iterator<? super T> i1 = values1.iterator();
      Iterator<T> i2 = values2.iterator();

      // enumerate both
      while (i1.hasNext())
      {
        Object v1 = i1.next();
        T v2 = i2.next();

        // compare using Equals if both not null
        if (v1 != null && v2 != null)
        {
          if (!v1.equals(v2))
            return false;
        } else
        // check if one is null and the other is not
        if (v1 != null || v2 != null)
          return false;
      }

      return true;
    }
  }

  /**
   * Returns true if both iterables have the same values in the exact same positions.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static <T> boolean sequenceEqual(@NotNull final T[] values1, @NotNull final T[] values2)
  {
    if (values1.length != values2.length)
      return false;
    else
    {
      int count = values1.length;
      for (int i = 0; i < count; i++)
      {
        T v1 = values1[i];
        T v2 = values2[i];

        // compare using Equals if both not null
        if (v1 != null && v2 != null)
        {
          if (!v1.equals(v2))
            return false;
        } else
        // check if one is null and the other is not
        if (v1 != null || v2 != null)
          return false;
      }

      return true;
    }
  }

  /**
   * Skips up to the specified number of elements in the given sequence.
   * 
   * @throws NullPointerException When the values argument is null
   * @throws IllegalArgumentException When count is out of range.
   */
  @Validate
  public static <T> Iterable<T> skip(@NotNull final Iterable<T> values, final int count)
  {
    if (count < 0)
      throw new IllegalArgumentException("count=" + count);

    // skip phase
    int skipped = 0;
    Iterator<T> iterator = values.iterator();
    for (T item : values)
    {
      if (iterator.hasNext() && skipped < count)
      {
        iterator.next();
        skipped++;
      }
    }

    // return remaining phase
    while (iterator.hasNext() && skipped < count)
    {
      yield(iterator.next());
      skipped++;
    }
  }

  /**
   * Skips up to the specified number of elements in the given sequence.
   * 
   * @throws NullPointerException When the values argument is null
   * @throws IllegalArgumentException When count is out of range.
   */
  @Validate
  public static <T> T[] skip(@NotNull final T[] values, final int count)
  {
    if (count < 0)
      throw new IllegalArgumentException("count=" + count);

    List<T> result = new ArrayList<T>(DEFAULT_LIST_SIZE);

    int i = 0;
    for (T item : values)
    {
      if (i++ < count)
        continue;

      result.add(item);
    }

    return toArray(result, values.getClass().getComponentType());
  }

  /**
   * Skips items in the sequence for which a predicate is true, returning the rest.
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Iterable<T> skipWhile(@NotNull final Iterable<T> values, @NotNull final Predicate1<? super T> predicate)
  {
    boolean skipping = true;

    for (T item : values)
    {
      if (skipping)
      {
        if (!predicate.apply(item))
        {
          skipping = false;
          yield(item);
        }
      } else
        yield(item);
    }
  }

  /**
   * Skips items in the sequence for which a predicate is true, returning the rest.
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> T[] skipWhile(@NotNull final T[] values, @NotNull final Predicate1<? super T> predicate)
  {
    List<T> result = new ArrayList<T>(DEFAULT_LIST_SIZE);
    boolean skipping = true;

    for (T item : values)
    {
      if (skipping)
      {
        if (!predicate.apply(item))
        {
          skipping = false;
          result.add(item);
        }
      } else
        result.add(item);
    }

    return toArray(result, values.getClass().getComponentType());
  }

  /**
   * Sorts a list
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T extends Comparable<T>> List<T> sort(@NotNull final List<T> values)
  {
    Collections.sort(values);
    return values;
  }

  /**
   * Sorts a list
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T extends Comparable<T>> List<T> sort(@NotNull final List<T> values, @NotNull final Comparator<? super T> comparator)
  {
    Collections.sort(values, comparator);
    return values;
  }

  /**
   * Sorts a sequence.
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T extends Comparable<T>> List<T> sort(@NotNull final Iterable<T> values)
  {
    List<T> result = new ArrayList<T>(DEFAULT_LIST_SIZE);

    for (T item : values)
      result.add(item);

    Collections.sort(result);
    return result;
  }

  /**
   * Sorts a sequence.
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T extends Comparable<T>> ReifiedList<T> sort(@NotNull final ReifiedIterable<T> values)
  {
    ReifiedList<T> result = new ReifiedArrayList<T>(values);
    Collections.sort(result);
    return result;
  }

  /**
   * Sorts a sequence.
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> List<T> sort(@NotNull final Iterable<T> values, @NotNull final Comparator<? super T> comparator)
  {
    List<T> result = new ArrayList<T>(DEFAULT_LIST_SIZE);
    for (T item : values)
      result.add(item);

    Collections.sort(result, comparator);

    return result;
  }

  /**
   * Splits a sequence into parts delimited by the specified delimited. Empty entries between delimiters are removed.
   * 
   * @throws NullPointerException When an argument is null, or an item in the iterable is null.
   */
  @Validate
  public static <T> List<T>[] split(@NotNull final Iterable<? extends T> values, @NotNull final T delimiter)
  {
    ReifiedList<List<T>> parts = new ReifiedArrayList<List<T>>() {};
    parts.add(new ArrayList<T>(DEFAULT_LIST_SIZE));

    for (T item : values)
    {
      if (!item.equals(delimiter))
        parts.get(parts.size() - 1).add(item);
      else
        parts.add(new ArrayList<T>(DEFAULT_LIST_SIZE));
    }

    return where(parts.toArray(), elementsExist());
  }

  /**
   * Splits a sequence into parts delimited by the specified delimited. Empty entries between delimiters are removed.
   * 
   * @throws NullPointerException When an argument is null, or an item in the iterable is null.
   */
  @Validate
  public static <T> List<T>[] split(@NotNull final T[] values, @NotNull final T delimiter)
  {
    ReifiedList<List<T>> parts = new ReifiedArrayList<List<T>>() {};
    parts.add(new ArrayList<T>(DEFAULT_LIST_SIZE));

    for (T item : values)
    {
      if (!item.equals(delimiter))
        parts.get(parts.size() - 1).add(item);
      else
        parts.add(new ArrayList<T>(DEFAULT_LIST_SIZE));
    }

    return where(parts.toArray(), elementsExist());
  }

  /**
   * Splits a sequence into parts delimited by the specified delimited. Empty entries between delimiters are removed.
   * 
   * @throws NullPointerException When an argument is null, or an item in the iterable is null and the comparer does not handle this case.
   */
  @Validate
  public static <T> List<T>[] split(@NotNull final Iterable<T> values, @NotNull final T delimiter, Comparator<? super T> comparer)
  {
    ReifiedList<List<T>> parts = new ReifiedArrayList<List<T>>() {};
    parts.add(new ArrayList<T>(DEFAULT_LIST_SIZE));

    for (T item : values)
    {
      if (comparer.compare(item, delimiter) != 0)
        // not a delimiter, add to parts
        parts.get(parts.size() - 1).add(item);
      else
        parts.add(new ArrayList<T>(DEFAULT_LIST_SIZE));
    }

    return where(parts.toArray(), elementsExist());
  }

  /**
   * Splits a sequence into parts delimited by the specified delimited. Empty entries between delimiters are removed.
   * 
   * @throws NullPointerException When an argument is null, or an item in the iterable is null and the comparer does not handle this case.
   */
  @Validate
  public static <T> List<T>[] split(@NotNull final T[] values, @NotNull final T delimiter, Comparator<? super T> comparer)
  {
    ReifiedList<List<T>> parts = new ReifiedArrayList<List<T>>() {};
    parts.add(new ArrayList<T>(DEFAULT_LIST_SIZE));

    for (T item : values)
    {
      if (comparer.compare(item, delimiter) != 0)
        parts.get(parts.size() - 1).add(item);
      else
        parts.add(new ArrayList<T>(DEFAULT_LIST_SIZE));
    }

    List<T> result = new ArrayList<T>(DEFAULT_LIST_SIZE);

    return where(parts.toArray(), elementsExist());
  }

  /**
   * Returns up to the specified number of elements from the given sequence.
   * 
   * @throws NullPointerException The values argument is null.
   */
  @Validate
  public static <T> Iterable<T> take(@NotNull final Iterable<T> values, final int count)
  {
    Iterator<T> iterator = values.iterator();

    int index = 0;
    while (index++ < count && iterator.hasNext())
      yield(iterator.next());
  }

  /**
   * Returns up to the specified number of elements from the given sequence.
   * 
   * @throws NullPointerException The values argument is null.
   * @throws IllegalArgumentException The count argument is out of range.
   */
  @Validate
  public static <T> T[] take(@NotNull final T[] values, final int count)
  {
    List<T> result = new ArrayList<T>(count);
    for (int i = 0; i < count && i < values.length; i++)
      result.add(values[i]);

    return toArray(result, values.getClass().getComponentType());
  }

  /**
   * Returns items in the sequence while a predicate is true. Breaks when the condition is not satisfied.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static <T> Iterable<T> takeWhile(@NotNull final Iterable<T> values, @NotNull final Predicate1<? super T> predicate)
  {
    for (T item : values)
      if (predicate.apply(item))
        yield(item);
      else
        break;
  }

  /**
   * Returns items in the sequence while a predicate is true. Breaks when the condition is not satisfied.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static <T> T[] takeWhile(@NotNull final T[] values, @NotNull final Predicate1<? super T> predicate)
  {
    List<T> result = new ArrayList<T>(DEFAULT_LIST_SIZE);

    int count = values.length;
    for (int i = 0; i < count; i++)
    {
      T item = values[i];
      if (predicate.apply(item))
        result.add(item);
      else
        break;
    }

    return toArray(result, values.getClass().getComponentType());
  }

  /**
   * Converts a list to an array.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static <T> T[] toArray(@NotNull final ReifiedList<T> list)
  {
    return toArray(list, list.getGenericTypeParameter());
  }

  /**
   * Converts an iterable to an array.
   * 
   * @throws NullPointerException The values argument is null.
   */
  @Validate
  public static <T> T[] toArray(@NotNull final ReifiedIterable<T> values)
  {
    return toArray(values, values.getGenericTypeParameter());
  }

  /**
   * Converts an iterable to an array.
   * 
   * @throws NullPointerException An argument is nul.
   */
  @Validate
  public static <T> T[] toArray(@NotNull final Iterable<T> values, @NotNull final Class<?> componentType)
  {
    // count items
    int count = count(values);

    T[] result = (T[]) Array.newInstance(componentType, count);

    Iterator<T> iterator = values.iterator();

    for (int i = 0; i < count; i++)
      result[i] = iterator.next();

    return result;
  }

  /**
   * Converts a collection to an array.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static <T> T[] toArray(@NotNull final Collection<T> list, @NotNull final Class<?> componentType)
  {
    // count items
    int size = list.size();
    T[] result = (T[]) Array.newInstance(componentType, size);

    Iterator<T> iterator = list.iterator();

    for (int i = 0; i < size; i++)
      result[i] = iterator.next();

    return result;
  }

  /**
   * Converts a sequence of items into a key/value AVL tree. Items from which duplicate keys are derived will be skipped. Null keys are not
   * allowed.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T, TKey extends Comparable<TKey>, TValue> AvlHashtable<TKey, TValue>
      toAvlHashtable(@NotNull final Iterable<T> values, @NotNull final Function1<T, TKey> keySelector,
                     @NotNull final Function1<T, TValue> valueSelector)
  {
    AvlHashtable<TKey, TValue> result = new AvlHashtable<TKey, TValue>(keySelector.getReturnType(), valueSelector.getReturnType());
    for (T item : values)
      result.add(keySelector.apply(item), valueSelector.apply(item));

    return result;
  }

  /**
   * Converts a sequence of items into a key/value AVL hashtable. Items from which duplicate keys are derived will be skipped. Null keys are
   * not allowed by the AVLHashtable.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T, TKey extends Comparable<TKey>, TValue> AvlHashtable<TKey, TValue>
      toAvlHashtable(@NotNull final T[] values, @NotNull final Function1<T, TKey> keySelector,
                     @NotNull final Function1<T, TValue> valueSelector)
  {
    AvlHashtable<TKey, TValue> result = new AvlHashtable<TKey, TValue>(keySelector.getReturnType(), valueSelector.getReturnType());
    int count = values.length;
    for (int i = 0; i < count; i++)
      result.add(keySelector.apply(values[i]), valueSelector.apply(values[i]));

    return result;
  }

  /**
   * Converts an Iterable to an array
   * 
   * @throws NullPointerException The values argument is null.
   */
  @Validate
  public static <T> List<T> toList(@NotNull final Iterable<? extends T> values)
  {
    List<T> result = new ArrayList<T>(DEFAULT_LIST_SIZE);
    for (T item : values)
      result.add(item);

    return result;
  }

  /**
   * Converts an iterable to a list
   * 
   * @throws NullPointerException The values argument is null.
   */
  @Validate
  public static <T> ReifiedList<T> toList(@NotNull final Iterable<T> values, @NotNull final Class<?> genericTypeParameter)
  {
    return new ReifiedArrayList<T>(values, genericTypeParameter);
  }

  /**
   * Converts an iterable to a list
   * 
   * @throws NullPointerException The values argument is null.
   */
  @Validate
  public static <T> ReifiedList<T> toList(@NotNull final ReifiedIterable<T> values)
  {
    return new ReifiedArrayList<T>(values);
  }

  /**
   * Converts an array to a list
   * 
   * @throws NullPointerException The values argument is null.
   */
  @Validate
  public static <T> ReifiedList<T> toList(@NotNull final T[] values)
  {
    return new ReifiedArrayList<T>(values);
  }

  /**
   * Produces the union of two sequences.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> Iterable<T> union(@NotNull final Iterable<T> first, @NotNull final Iterable<T> second)
  {
    return union(first, second, null);
  }

  /**
   * Produces the union of two sequences.
   * 
   * @throws NullPointerException When the first or second argument is null.
   */
  @Validate
  public static <T> Iterable<T> union(@NotNull final Iterable<T> first, @NotNull final Iterable<T> second,
                                      @NotNull final Comparator<? super T> comparer)
  {
    Iterable<T> firstDistinct;
    Iterable<T> secondDistinct;

    if (comparer == null)
    {
      firstDistinct = distinct(first);
      secondDistinct = distinct(second);
      return concat(firstDistinct, secondDistinct);
    } else
    {
      firstDistinct = distinct(first, comparer);
      secondDistinct = distinct(second, comparer);
      return concat(firstDistinct, secondDistinct);
    }
  }

  /**
   * Produces the union of two sequences.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <T> T[] union(@NotNull final T[] first, @NotNull final T[] second)
  {
    return union(first, second, null);
  }

  /**
   * Produces the union of two sequences.
   * 
   * @throws NullPointerException When the first or second argument is null.
   */
  @Validate
  public static <T> T[] union(@NotNull final T[] first, @NotNull final T[] second, @NotNull final Comparator<? super T> comparer)
  {
    T[] firstDistinct;
    T[] secondDistinct;

    if (comparer == null)
    {
      firstDistinct = distinct(first);
      secondDistinct = distinct(second);
      T[] union = concat(firstDistinct, secondDistinct);
      return distinct(union);
    } else
    {
      firstDistinct = distinct(first, comparer);
      secondDistinct = distinct(second, comparer);
      T[] union = concat(firstDistinct, secondDistinct);
      return distinct(union, comparer);
    }
  }

  /**
   * Returns a subset of the provided sequence, which conforms to the given predicate i.e. acts like a Where LINQ function It will never
   * return null.
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> Iterable<T> where(@NotNull final Iterable<T> values, @NotNull final Predicate1<? super T> predicate)
  {
    for (T element : values)
      if (predicate.apply(element))
        yield(element);
  }

  /**
   * Returns a subset of the provided sequence, which conforms to the given predicate i.e. acts like a Where LINQ function It will never
   * return null.
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static <T> T[] where(@NotNull final T[] values, @NotNull final Predicate1<? super T> predicate)
  {
    List<T> result = new ArrayList<T>(DEFAULT_LIST_SIZE);

    for (T element : values)
      if (predicate.apply(element))
        result.add(element);

    return toArray(result, values.getClass().getComponentType());
  }

  /**
   * Merges two sequences by using the specified predicate function.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <TFirst, TSecond, TResult> Iterable<TResult> zip(@NotNull final Iterable<TFirst> first,
                                                                 @NotNull final Iterable<TSecond> second,
                                                                 @NotNull final Function2<TFirst, TSecond, TResult> function)
  {
    Iterator<TFirst> iterator1 = first.iterator();
    Iterator<TSecond> iterator2 = second.iterator();

    while (iterator1.hasNext() && iterator2.hasNext())
      yield(function.apply(iterator1.next(), iterator2.next()));
  }

  /**
   * Merges two sequences by using the specified predicate function.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static <TFirst, TSecond, TResult> TResult[] zip(@NotNull final TFirst[] first, @NotNull final TSecond[] second,
                                                         @NotNull final Function2<TFirst, TSecond, TResult> function)
  {
    List<TResult> result = new ArrayList<TResult>(DEFAULT_LIST_SIZE);

    int count = Math.min(first.length, second.length);

    for (int i = 0; i < count; i++)
      result.add(function.apply(first[i], second[i]));

    return toArray(result, function.getReturnType());
  }

  /**
   * Casts a sequence of values of a certain type to an array of values of another type, throwing an InvalidCastException if any elements
   * are not cast successfully.
   */
  private static <TSource, TDest> Iterable<TDest> castThrow(final Iterable<TSource> values, final Class<TDest> destinationClass)
  {
    for (TSource val : values)
    {
      TDest castVal = (TDest) destinationClass.cast(val);
      yield(castVal);
    }
  }

  /**
   * Casts a sequence of values of a certain type to an array of values of another type, throwing an InvalidCastException if any elements
   * are not cast successfully.
   */
  private static <TSource, TDest> void castThrow(TSource[] values, ReifiedList<TDest> list)
  {
    Class<?> destinationClass = list.getGenericTypeParameter();
    for (TSource val : values)
    {
      TDest castVal = (TDest) destinationClass.cast(val);
      list.add(castVal);
    }
  }

  /**
   * Casts a sequence of values of a certain type to an array of values of another type, discarding elements that are not cast successfully.
   */
  private static <TSource, TDest> Iterable<TDest> castRemove(final Iterable<TSource> values, final Class<TDest> destinationClass)
  {
    for (TSource val : values)
    {
      boolean cce = false;
      TDest castVal;
      try
      {
        castVal = (TDest) destinationClass.cast(val);
      }
      catch(ClassCastException e)
      {
        cce = true;
      }
      if (!cce)
        yield(castVal);
    }
  }

  /**
   * Casts a sequence of values of a certain type to an array of values of another type, discarding elements that are not cast successfully.
   */
  private static <TSource, TDest> void castRemove(final TSource[] values, final ReifiedList<TDest> list)
  {
    Class<?> destinationClass = list.getGenericTypeParameter();
    for (TSource val : values)
    {
      TDest castVal;
      try
      {
        castVal = (TDest) destinationClass.cast(val);
      }
      catch(ClassCastException e)
      {
        // remove upon any failure
        continue;
      }

      list.add(castVal);
    }
  }

  /**
   * Casts a sequence of values of a certain type to an array of values of another type, using nulls for elements that are not cast
   * successfully
   */
  private static <TSource, TDest> Iterable<TDest> castUseDefault(final Iterable<TSource> values, final Class<TDest> destinationClass)
  {
    for (TSource val : values)
    {
      TDest castVal = null;
      try
      {
        castVal = (TDest) destinationClass.cast(val);
      }
      catch(ClassCastException e)
      {}

      yield(castVal);
    }
  }

  /**
   * Casts a sequence of values of a certain type to an array of values of another type, using nulls for elements that are not cast
   * successfully
   * 
   * @throws NullPointerException An argument is null.
   */
  private static <TSource, TDest> void castUseDefault(final TSource[] values, final ReifiedList<TDest> list)
  {
    Class<?> destinationClass = list.getGenericTypeParameter();

    int count = values.length;
    for (int i = 0; i < count; i++)
    {
      TDest castVal;
      try
      {
        castVal = (TDest) destinationClass.cast(values[i]);
      }
      catch(ClassCastException e)
      {
        castVal = null;
      }
      // add
      list.add(castVal);
    }
  }

  /**
   * Returns true if a non-null item is contained in the sequence of values
   */
  private static <T> boolean containsNonNull(final Iterable<T> values, final T item)
  {
    for (T val : values)
      // if a value is null, we cannot use equals
      if (val != null)
        if (val.equals(item))
          return true;

    return false;
  }

  /**
   * Returns true if a non-null item is contained in the sequence of values
   */
  private static <T> boolean containsNonNull(final Iterable<T> values, final T item, final Comparator<? super T> comparer)
  {
    for (T val : values)
      // if a value is null, we cannot use equals
      if (val != null)
        if (comparer.compare(val, item) == 0)
          return true;

    return false;
  }

  /**
   * Returns true if null is contained in the sequence of values
   */
  private static <T> boolean containsNull(final Iterable<T> values)
  {
    for (T item : values)
      if (item == null)
        return true;

    return false;
  }

  /**
   * Returns true if a non-null item is contained in the sequence of values
   */
  private static <T> boolean containsNonNull(final T[] values, final T item)
  {
    int count = values.length;
    for (int i = 0; i < count; i++)
    {
      T val = values[i];
      // if a value is null, we cannot use equals
      if (val != null)
        if (val.equals(item))
          return true;
    }

    return false;
  }

  /**
   * Returns true if a non-null item is contained in the sequence of values
   */
  private static <T> boolean containsNonNull(final T[] values, final T item, final Comparator<? super T> comparer)
  {
    int count = values.length;
    for (int i = 0; i < count; i++)
    {
      T val = values[i];
      // if a value is null, we cannot use equals
      if (val != null)
        if (comparer.compare(val, item) == 0)
          return true;
    }

    return false;
  }

  /**
   * Returns true if null is contained in the sequence of values
   */
  private static <T> boolean containsNull(final T[] values)
  {
    int count = values.length;
    for (int i = 0; i < count; i++)
      if (values[i] == null)
        return true;

    return false;
  }

  /**
   * Returns the number of null entries in the sequence
   */
  private static <T> int countNulls(final Iterable<? extends T> values)
  {
    // check if value contained in sequence
    int result = 0;

    // the values is confirmed to be a non-null IEnumerable prior to this
    for (T val : values)
      if (val == null)
        result++;

    return result;
  }

  /**
   * Returns the number of null entries in the sequence
   */
  private static <T> int countNulls(final T[] values)
  {
    // check if value contained in sequence
    int result = 0;

    // the values is confirmed to be a non-null IEnumerable prior to this
    int count = values.length;
    for (int i = 0; i < count; i++)
      if (values[i] == null)
        result++;

    return result;
  }

  /**
   * Returns the number of occurences of a non-null value in the collection
   */
  private static <T> int countNonNull(final Iterable<T> values, final T value)
  {
    // check if value contained collection
    int result = 0;

    // the values is confirmed to be a non-null IEnumerable prior to this
    for (T val : values)
      if (val != null)
        if (val.equals(value))
          result++;

    return result;
  }

  /**
   * Returns the number of occurences of a non-null value in the collection
   */
  private static <T> int countNonNull(final Iterable<T> values, final T value, final Comparator<? super T> comparer)
  {
    // check if value contained collection
    int result = 0;

    // the values is confirmed to be a non-null IEnumerable prior to this
    for (T val : values)
      if (val != null)
        if (comparer.compare(val, value) == 0)
          result++;

    return result;
  }

  /**
   * Returns the number of occurences of a non-null value in the collection
   */
  private static <T> int countNonNull(final T[] values, final T value)
  {
    // check if value contained collection
    int result = 0;

    // the values is confirmed to be a non-null IEnumerable prior to this
    int count = values.length;
    for (int i = 0; i < count; i++)
    {
      T val = values[i];
      if (val != null)
        if (val.equals(value))
          result++;
    }

    return result;
  }

  /**
   * Returns the number of occurences of a non-null value in the collection
   */
  private static <T> int countNonNull(final T[] values, final T value, final Comparator<? super T> comparer)
  {
    // check if value contained collection
    int result = 0;

    // the values is confirmed to be a non-null IEnumerable prior to this
    int count = values.length;
    for (int i = 0; i < count; i++)
    {
      T val = values[i];
      if (val != null)
        if (comparer.compare(val, value) == 0)
          result++;
    }

    return result;
  }

  /**
   * Returns the index where the first null element is found. If no null elements are found, this returns -1.
   */
  private static <T> int indexOfNull(final Iterable<T> values)
  {
    int i = 0;
    for (T item : values)
    {
      if (item == null)
        return i;
      i++;
    }

    return -1;
  }

  /**
   * Returns the index where the first null element is found. If no null elements are found, this returns -1.
   */
  private static <T> int indexOfNull(final T[] values)
  {
    int count = values.length;
    for (int i = 0; i < count; i++)
    {
      if (values[i] == null)
        return i;
    }

    return -1;
  }

  /**
   * Returns the index where the specified not-null element is first found. If the element is not found, this returns -1.
   */
  private static <T> int indexOfNotNull(final Iterable<? super T> values, final T element)
  {
    int i = 0;
    for (Object item : values)
    {
      if (element.equals(item))
        return i;
      i++;
    }

    return -1;
  }

  /**
   * Returns the index where the specified not-null element is first found. If the element is not found, this returns -1.
   */
  private static <T> int indexOfNotNull(final Iterable<T> values, final T element, final Comparator<? super T> comparer)
  {
    int i = 0;
    for (T item : values)
    {
      if (comparer.compare(element, item) == 0)
        return i;
      i++;
    }

    return -1;
  }

  /**
   * Returns the index where the specified not-null element is first found. If the element is not found, this returns -1.
   */
  private static <T> int indexOfNotNull(final T[] values, final T element)
  {
    int count = values.length;
    for (int i = 0; i < count; i++)
    {
      if (element.equals(values[i]))
        return i;
    }

    return -1;
  }

  /**
   * Returns the index where the specified not-null element is first found. If the element is not found, this returns -1.
   */
  private static <T> int indexOfNotNull(final T[] values, final T element, final Comparator<? super T> comparer)
  {
    int count = values.length;
    for (int i = 0; i < count; i++)
    {
      if (comparer.compare(element, values[i]) == 0)
        return i;
    }

    return -1;
  }

  /**
   * Returns the last index where the first null element is found. If no null elements are found, this returns -1.
   */
  private static <T> int lastIndexOfNull(final Iterable<T> values)
  {
    int lastPos = -1;
    int i = 0;
    for (T item : values)
    {
      if (item == null)
        lastPos = i;
      i++;
    }

    return lastPos;
  }

  /**
   * Returns the last index where the first null element is found. If no null elements are found, this returns -1.
   */
  private static <T> int lastIndexOfNull(final T[] values)
  {
    int count = values.length;
    for (int i = count; i >= 0; i--)
    {
      if (values[i] == null)
        return i;
    }

    return -1;
  }

  /**
   * Returns the last index where the specified not-null element is first found. If the element is not found, this returns -1.
   */
  private static <T> int lastIndexOfNotNull(final Iterable<? super T> values, final T element)
  {
    int lastPos = -1;
    int i = 0;
    for (Object item : values)
    {
      if (element.equals(item))
        lastPos = i;
      i++;
    }

    return lastPos;
  }

  /**
   * Returns the last index where the specified not-null element is first found. If the element is not found, this returns -1.
   */
  private static <T> int lastIndexOfNotNull(final Iterable<T> values, final T element, final Comparator<? super T> comparer)
  {
    int lastPos = -1;
    int i = 0;
    for (T item : values)
    {
      if (comparer.compare(element, item) == 0)
        lastPos = i;
      i++;
    }

    return lastPos;
  }

  /**
   * Returns the last index where the specified not-null element is first found. If the element is not found, this returns -1.
   */
  private static <T> int lastIndexOfNotNull(final T[] values, final T element)
  {
    int count = values.length;
    for (int i = count - 1; i >= 0; i--)
    {
      if (element.equals(values[i]))
        return i;
    }

    return -1;
  }

  /**
   * Returns the last index where the specified not-null element is first found. If the element is not found, this returns -1.
   */
  private static <T> int lastIndexOfNotNull(final T[] values, final T element, final Comparator<? super T> comparer)
  {
    int count = values.length;
    for (int i = count - 1; i >= 0; i--)
    {
      if (comparer.compare(element, values[i]) == 0)
        return i;
    }

    return -1;
  }

  @SuppressWarnings("rawtypes")
  private static <T> Predicate1<List> elementsExist()
  {
    return new Predicate1<List>() {
      public boolean evaluate(List list)
      {
        return list.size() > 0;
      }
    };
  }

}
