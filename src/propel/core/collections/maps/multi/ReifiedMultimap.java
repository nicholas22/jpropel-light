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
