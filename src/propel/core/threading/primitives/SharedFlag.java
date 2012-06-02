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
package propel.core.threading.primitives;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Lightweight synchronisation primitive.
 */
public class SharedFlag
{
  /**
   * Starts off being 0 (not set) by default, becomes 1 if flag is set.
   */
  private AtomicInteger flag;

  /**
   * Default constructor. Atomic flag starts off as not set.
   */
  public SharedFlag()
  {
    this(false);
  }

  /**
   * Constructor allowing for flag to be started of as set or not set.
   */
  public SharedFlag(boolean startSet)
  {
    flag = startSet ? new AtomicInteger(1) : new AtomicInteger(0);
  }

  /**
   * Returns true if the value is not set. Returns false if the value is set.
   */
  public boolean isNotSet()
  {
    return flag.compareAndSet(0, 0);
  }

  /**
   * Returns true if the value is set. Returns false if the value is not set.
   */
  public boolean isSet()
  {
    return flag.compareAndSet(1, 1);
  }

  /**
   * Sets the value of the flag. Returns true if successful, false if already set.
   */
  public boolean set()
  {
    return flag.compareAndSet(0, 1);
  }

  /**
   * Unsets the value of the flag. Returns true if successful, false if already unset.
   */
  public boolean unSet()
  {
    return flag.compareAndSet(1, 0);
  }
}
