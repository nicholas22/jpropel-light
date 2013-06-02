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

import propel.core.common.CONSTANT;
import propel.core.utils.ArrayUtils;

/**
 * Class aiding in validation of Hex values
 */
public class HexPropertyMetadata
    extends EncodedStringPropertyMetadata
{
  /**
   * Default constructor
   */
  protected HexPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with specified constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public HexPropertyMetadata(String name, int minLength, int maxLength, boolean notNull, boolean notEmpty)
  {
    super(name, ArrayUtils.boxChars(CONSTANT.HEX_DIGITS), minLength, maxLength, notNull, notEmpty, true);
  }
}
