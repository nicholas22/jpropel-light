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

import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.threading.primitives.SharedFlag;

/**
 * Thread-safe lock-free guard, allows components to check whether they have initialised prior to being used.
 */
public final class MultiInitGuard
    implements InitGuard
{
  // used for maintaining the state
  private final SharedFlag flag;
  // used to identify the owner of this guard
  private final String className;

  /**
   * Constructor initialises with the owner of this guard
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public MultiInitGuard(@NotNull final Class<?> owner)
  {
    this.className = owner.getSimpleName();
    this.flag = new SharedFlag();
  }

  /**
   * Constructor initialises with the owner of this guard
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public MultiInitGuard(@NotNull final String className)
  {
    this.className = className;
    this.flag = new SharedFlag();
  }

  /**
   * Call to initialise, can be called multiple times
   */
  @Override
  public void initialise()
  {
    flag.set();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void uninitialise()
  {
    if (!flag.unSet())
      throw new IllegalStateException("Cannot proceed with un-initialisation of " + className
          + " instance as it is not in an initialised state");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void assertInitialised()
  {
    if (flag.isNotSet())
      throw new IllegalStateException("Instance of " + className + " is not in an initialised state");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void assertNotInitialised()
  {
    if (flag.isSet())
      throw new IllegalStateException("Instance of " + className + " is in an initialised state");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInitialised()
  {
    return flag.isSet();
  }

}
