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
package propel.core.collections.sets;

import propel.core.collections.IValueStore;
import java.util.Set;

/**
 * The interface of a type-aware set
 * 
 * @param <T> The component type
 */
public interface ReifiedSet<T extends Comparable<T>>
    extends IValueStore<T>
{
  /**
   * This operation combines this set with another set i.e. this set will add to this collection all non-preexisting items found in the
   * other set.
   */
  void union(Set<? extends T> otherSet);

  /**
   * This operation removes from this set all items that are common between this set and the other set.
   */
  void intersect(Set<? extends T> otherSet);

  /**
   * This operation removes all items from this set, only keeping items that are not common between this set and the other set.
   */
  void difference(Set<? extends T> otherSet);
}
