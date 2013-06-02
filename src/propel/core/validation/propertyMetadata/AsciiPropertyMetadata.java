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

import propel.core.utils.ArrayUtils;
import propel.core.utils.StringUtils;

/**
 * Class aiding in validation of ASCII values [0..127]. Useful for detecting 7bit encodings.
 */
public class AsciiPropertyMetadata
    extends EncodedStringPropertyMetadata
{
  /**
   * Default constructor
   */
  protected AsciiPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with specified constraints.
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public AsciiPropertyMetadata(String name, int minLength, int maxLength, boolean notNull, boolean notEmpty, boolean noNullChars)
  {
    super(name, ArrayUtils.boxChars(StringUtils.charRange(0, 128)), minLength, maxLength, notNull, notEmpty, noNullChars);
  }
}
