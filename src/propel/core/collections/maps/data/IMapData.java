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
package propel.core.collections.maps.data;

import propel.core.collections.maps.ReifiedMap;

/**
 * The interface of a class encapsulating Keys and Values as separate arrays.
 * 
 * @param <TKey> The key type
 * @param <TValue> The value type
 */
public interface IMapData<TKey, TValue>
    extends ReifiedMap<TKey, TValue>
{
  /**
   * The keys
   */
  TKey[] getKeys();

  /**
   * The values
   */
  TValue[] getValues();

  /**
   * The key/value count
   */
  int size();
}
