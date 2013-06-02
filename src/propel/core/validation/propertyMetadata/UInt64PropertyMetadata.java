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

import propel.core.userTypes.UnsignedLong;

/**
 * Class aiding in validation of UInt64s
 */
public class UInt64PropertyMetadata
    extends BoundedValueTypePropertyMetadata<UnsignedLong>
{
  /**
   * Default constructor
   */
  protected UInt64PropertyMetadata()
  {
  }

  /**
   * Constructor initializing with a min and a max value
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public UInt64PropertyMetadata(String name, UnsignedLong minValue, UnsignedLong maxValue, boolean notNull)
  {
    super(name, minValue, maxValue, notNull);
  }
}
