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
package propel.core.collections;

import propel.core.collections.lists.ReifiedList;

/**
 * The interface of a value store.
 * 
 * @param <T> The component type of the value store.
 */
public interface IValueStore<T extends Comparable<T>>
    extends ReifiedIterable<T>
{
  /**
   * Adds an item to the collection.
   * 
   * @param item The item to add.
   * 
   * @return True if successful.
   */
  boolean add(T item);

  /**
   * Clears the collection
   */
  void clear();

  /**
   * Checks if an item exists.
   * 
   * @param item The item to find.
   * 
   * @return True if the item is found.
   */
  boolean contains(T item);

  /**
   * Removes an item from the collection.
   * 
   * @param item The item to find.
   * 
   * @return True if item was found and removed.
   * 
   * @throws NullPointerException When the key is null.
   */
  boolean remove(T item);

  /**
   * Returns the collection size
   */
  int size();

  /**
   * Puts all elements in an array and returns them.
   * 
   * @return All elements
   */
  T[] toArray();

  /**
   * Puts all elements in an array and returns them.
   * 
   * @return All elements
   */
  ReifiedList<T> toList();
}
