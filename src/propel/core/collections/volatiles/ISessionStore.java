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
package propel.core.collections.volatiles;

import propel.core.model.IShared;

/**
 * The interface of a Session store.
 */
public interface ISessionStore<T>
    extends IShared
{
  /**
   * Returns the duration of a session in milliseconds. If the value is Timeout.Infinite then there is no expiration.
   */
  int getExpirationMillis();

  /**
   * The polling interval in milliseconds. If the value is Timeout.Infinite then there is no polling.
   */
  int getPollingIntervalMillis();

  /**
   * Renews the session expiration time for an item. Returns true if the item was found and refreshed.
   * 
   * @throws NullPointerException When the item is null.
   */
  boolean refresh(T item);

  /**
   * Expires the time of a session item. Item will be removed the next time the timer fires.
   * 
   * @throws NullPointerException When the item is null.
   */
  boolean expire(T item);
}
