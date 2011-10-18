package propel.core.collections.maps.multi;

import propel.core.functional.tuples.Triple;

public interface ReifiedMultimap<T, K, V>
    extends Iterable<Triple<T, K, V>>
{
  /**
   * Returns the T generic type parameter's class. This never returns null.
   */
  Class<?> getGenericTypeParameterKey();

  /**
   * Returns the K generic type parameter's class. This never returns null.
   */
  Class<?> getGenericTypeParameterSubKey();

  /**
   * Returns the V generic type parameter's class. This never returns null.
   */
  Class<?> getGenericTypeParameterValue();

  /**
   * Returns the T,K,V tuple size that is returned when iterating over this multimap
   */
  int size();
}
