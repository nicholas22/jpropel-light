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
package propel.core.initialisation;

import propel.core.common.CONSTANT;
import propel.core.threading.primitives.SharedFlag;

/**
 * Simulates a final object by implementing a generic container of any kind of reference, which can only be set once. This is either upon
 * construction or using by a setter. Any attempt to overwrite it will throw an IllegalStateException. Attempts to get the value without
 * having set it beforehand also throw an IllegalStateException.
 */
public final class Frozen<T>
{
  private static final String ERROR_NOT_SET = "The frozen object has not been set";
  private static final String ERROR_ALREADY_SET = "The frozen object%s has already been set";

  // the protected object
  private T object;
  // whether the object has been set
  private final SharedFlag flag;

  /**
   * Default constructor, if using this then the object must be set explicitly before getting.
   */
  public Frozen()
  {
    // set flag to false
    flag = new SharedFlag();
  }

  /**
   * Initialises with an object, preventing subsequent overwriting.
   */
  public Frozen(T object)
  {
    // set flag to true
    flag = new SharedFlag(true);
    this.object = object;
  }

  /**
   * Getter for the frozen object
   * 
   * @throws IllegalStateException The frozen object has not been set
   */
  public T get()
  {
    if (flag.isNotSet())
      throwNotSetException();

    return object;
  }

  /**
   * Sets the object, if it has not already been set.
   * 
   * @throws IllegalStateException A frozen object cannot be set more than once
   */
  public void set(T object)
  {
    if (flag.isNotSet())
    {
      synchronized(flag)
      {
        if (flag.isSet())
          throwAlreadySetException();

        this.object = object;
        flag.set();
      }
    } else
      throwAlreadySetException();
  }

  /**
   * Returns true if a value has been set
   */
  public boolean isSet()
  {
    return flag.isSet();
  }

  private void throwNotSetException()
  {
    throw new IllegalStateException(ERROR_NOT_SET);
  }

  private void throwAlreadySetException()
  {
    Object copyRef = object;
    throw new IllegalStateException(String.format(ERROR_ALREADY_SET, (copyRef == null ? CONSTANT.EMPTY_STRING : " "
        + copyRef.getClass().getSimpleName())));
  }
}
