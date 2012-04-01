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

/**
 * Interface of a MapMultimap (a map of maps). TODO: MapMultimap to inherit from this and use @Override on overridden methods!
 */
public interface ISharedMapMultimap<T extends Comparable<? super T>, K extends Comparable<? super K>, V>
    extends IMapMultimap<T, K, V>
{
  /**
   * Inserts a key/subkey/value tuple, if it is absent, returning null. Otherwise returns the existing value, without altering it.
   * 
   * @throws NullPointerException A key or sub-key is null
   */
  V putIfAbsent(T key, K subkey, V value);
}
