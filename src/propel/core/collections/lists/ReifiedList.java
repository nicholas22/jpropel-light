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

import propel.core.collections.ReifiedIterable;
import java.util.List;

/**
 * A type-aware list interface
 */
public interface ReifiedList<T>
    extends List<T>, ReifiedIterable<T>
{
  /**
   * Appends all of the elements in the specified array to the end of this list, in the order that they are ordered.
   * 
   * This is an O(n) operation where n is the size of the given array.
   * 
   * @return True, if this list changed as a result of the call
   * 
   * @throws NullPointerException If the array is null.
   */
  public boolean addAll(T[] array);

  /**
   * Returns all elements in an array copy. This is an O(n) operation.
   */
  @Override
  T[] toArray();

  /**
   * Creates a sub-list from this list. This is an O(n) operation.
   * 
   * @throws IndexOutOfBoundsException When an index provided is out of range.
   */
  @Override
  ReifiedList<T> subList(int fromIndex, int toIndex);
}
