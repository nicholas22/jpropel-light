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

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.collections.arrays.ReifiedArray;
import propel.core.collections.lists.ISharedList;
import propel.core.collections.lists.ReifiedList;
import propel.core.collections.maps.IHashtable;
import propel.core.collections.maps.ISharedHashtable;
import propel.core.collections.maps.combinational.IListMap;
import propel.core.collections.maps.multi.IMapMultimap;
import propel.core.collections.maps.multi.ISharedMapMultimap;
import propel.core.collections.maps.primitive.ILongHashMap;
import propel.core.collections.sets.ReifiedSet;

/**
 * Class contains immutability-related utilities
 */
public final class ImmutableUtils
{
  // per-interface suppressed methods (public API)
  private static final String[] collectionSuppressed = {"add", "addAll", "clear", "remove", "removeAll", "retainAll"};
  private static final String[] reifiedListSuppressed = ArrayUtils.add(collectionSuppressed, "set");
  private static final String[] sharedListSuppressed = ArrayUtils.add(reifiedListSuppressed, "addIfAbsent");
  private static final String[] listSuppressed = ArrayUtils.add(reifiedListSuppressed, "listIterator");
  private static final String[] queueSuppressed = ArrayUtils.join(collectionSuppressed, new String[] {"offer", "poll"});
  private static final String[] dequeSuppressed = ArrayUtils.join(queueSuppressed, new String[] {"addFirst", "addLast", "offerFirst",
    "offerLast", "removeFirst", "removeLast", "pollFirst", "pollLast", "removeFirstOccurrence", "removeLastOccurrence", "push", "pop"});
  private static final String[] mapSuppressed = {"put", "remove", "putAll", "clear"};
  private static final String[] concurrentMapSuppressed = ArrayUtils.join(mapSuppressed, new String[] {"putIfAbsent", "replace"});
  private static final String[] hashtableSuppressed = {"add", "clear", "remove", "replace"};
  private static final String[] sharedHashtableSuppressed = ArrayUtils.join(hashtableSuppressed, new String[] {"addRange", "removeRange",
    "replaceAll"});
  private static final String[] listMapSuppressed = {"add", "clear", "remove", "removeAt", "replace", "replaceAt"};
  private static final String[] mapMultimapSuppressed = {"put", "remove", "removeKey", "removeSubKey", "removeSubKeys", "clear"};
  private static final String[] sharedMapMultimapSuppressed = ArrayUtils.join(mapMultimapSuppressed, new String[] {"putIfAbsent"});
  private static final String[] longHashMapSuppressed = {"put"};
  private static final String[] setSuppressed = collectionSuppressed;
  private static final String[] reifiedSetSuppressed = {"add", "clear", "remove", "union", "intersect", "difference"};
  private static final String[] reifiedArraySuppressed = {"set", "getOriginalArray"};

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> List<T> toReadOnly(@NotNull final List<T> list)
  {
    return ReflectionUtils.proxy(list, List.class, listSuppressed);
  }

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> Set<T> toReadOnly(@NotNull final Set<T> set)
  {
    return ReflectionUtils.proxy(set, Set.class, setSuppressed);
  }

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> Queue<T> toReadOnly(@NotNull final Queue<T> queue)
  {
    return ReflectionUtils.proxy(queue, Queue.class, queueSuppressed);
  }

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> Deque<T> toReadOnly(@NotNull final Deque<T> deque)
  {
    return ReflectionUtils.proxy(deque, Deque.class, dequeSuppressed);
  }

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, V> toReadOnly(@NotNull final Map<K, V> map)
  {
    return ReflectionUtils.proxy(map, Map.class, mapSuppressed);
  }

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <K, V> ConcurrentMap<K, V> toReadOnly(@NotNull final ConcurrentMap<K, V> map)
  {
    return ReflectionUtils.proxy(map, ConcurrentMap.class, concurrentMapSuppressed);
  }

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> ReifiedArray<T> toReadOnly(@NotNull final ReifiedArray<T> array)
  {
    return ReflectionUtils.proxy(array, ReifiedArray.class, reifiedArraySuppressed);
  }

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> ReifiedList<T> toReadOnly(@NotNull final ReifiedList<T> list)
  {
    return ReflectionUtils.proxy(list, ReifiedList.class, reifiedListSuppressed);
  }

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> ISharedList<T> toReadOnly(@NotNull final ISharedList<T> list)
  {
    return ReflectionUtils.proxy(list, ISharedList.class, sharedListSuppressed);
  }

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <K extends Comparable<K>, V> IHashtable<K, V> toReadOnly(@NotNull final IHashtable<K, V> map)
  {
    return ReflectionUtils.proxy(map, IHashtable.class, hashtableSuppressed);
  }

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <K extends Comparable<K>, V> ISharedHashtable<K, V> toReadOnly(@NotNull final ISharedHashtable<K, V> map)
  {
    return ReflectionUtils.proxy(map, ISharedHashtable.class, sharedHashtableSuppressed);
  }

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <K extends Comparable<K>, V> IListMap<K, V> toReadOnly(@NotNull final IListMap<K, V> map)
  {
    return ReflectionUtils.proxy(map, IListMap.class, listMapSuppressed);
  }

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T extends Comparable<? super T>, K extends Comparable<? super K>, V> IMapMultimap<T, K, V>
      toReadOnly(@NotNull final IMapMultimap<T, K, V> map)
  {
    return ReflectionUtils.proxy(map, IMapMultimap.class, mapMultimapSuppressed);
  }

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T extends Comparable<? super T>, K extends Comparable<? super K>, V> ISharedMapMultimap<T, K, V>
      toReadOnly(@NotNull final ISharedMapMultimap<T, K, V> map)
  {
    return ReflectionUtils.proxy(map, ISharedMapMultimap.class, sharedMapMultimapSuppressed);
  }

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <V> ILongHashMap<V> toReadOnly(@NotNull final ILongHashMap<V> map)
  {
    return ReflectionUtils.proxy(map, ILongHashMap.class, longHashMapSuppressed);
  }

  /**
   * Returns a read-only version of the given collection
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T extends Comparable<T>> ReifiedSet<T> toReadOnly(@NotNull final ReifiedSet<T> set)
  {
    return ReflectionUtils.proxy(set, ReifiedSet.class, reifiedSetSuppressed);
  }

  /**
   * Private constructor
   */
  private ImmutableUtils()
  {

  }
}
