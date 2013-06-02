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
package propel.core.validation.propertyMetadata;

import propel.core.userTypes.SignedByte;

/**
 * Class aiding in validation of Int8s
 */
public class Int8PropertyMetadata
    extends BoundedValueTypePropertyMetadata<SignedByte>
{
  /**
   * Default constructor
   */
  protected Int8PropertyMetadata()
  {
  }

  /**
   * Constructor initializing with a min and a max value
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public Int8PropertyMetadata(String name, byte minValue, byte maxValue)
  {
    super(name, new SignedByte(minValue), new SignedByte(maxValue));
  }

  /**
   * Constructor initializing with a min and a max value
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public Int8PropertyMetadata(String name, SignedByte minValue, SignedByte maxValue, boolean notNull)
  {
    super(name, minValue, maxValue, notNull);
  }
}
