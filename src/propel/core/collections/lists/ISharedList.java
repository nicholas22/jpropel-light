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
package propel.core.collections.lists;

import propel.core.model.IShared;

/**
 * A type-aware thread-safe list interface
 * 
 * @param <T> The component type
 */
public interface ISharedList<T>
    extends ReifiedList<T>, IShared
{
  /**
   * Adds an element if it does not exist.
   * 
   * @return True if added
   */
  boolean addIfAbsent(T item);

  /**
   * Returns all elements as a list. This is an O(n) operation.
   */
  ReifiedList<T> toList();

}
