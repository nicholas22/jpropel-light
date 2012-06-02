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

/**
 * Interface of class that implement an initialisation guard. Such classes can be used to enforce checks that a class has been initialised
 * one or more times, by throwing exceptions when such conditions are not met.
 */
public interface InitGuard
{
  /**
   * Call to set the state to initialised. Depending no the implementation this may be a call-once method.
   * 
   * @throws IllegalStateException If the class has already been initialised and multiple initialisations are not allowed
   */
  void initialise();

  /**
   * Call to un-initialise this object
   * 
   * @throws IllegalStateException The class has not been initialised yet
   */
  void uninitialise();

  /**
   * Call to assert that initialisation has taken place prior to this call
   * 
   * @throws IllegalStateException The class has not been initialised
   */
  void assertInitialised();

  /**
   * Call to assert that initialisation has NOT taken place prior to this call
   * 
   * @throws IllegalStateException The class has already been initialised
   */
  void assertNotInitialised();

  /**
   * Returns true if the state is initialised
   */
  boolean isInitialised();

}
