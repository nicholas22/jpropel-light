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

import propel.core.configuration.ConfigurableConsts;
import propel.core.configuration.ConfigurableParameters;

/**
 * Class aiding in validation of telephone numbers.
 */
public class TelephonePropertyMetadata
    extends EncodedStringPropertyMetadata
{
  /**
   * The default minimum telephone number length
   */
  public static final int DEFAULT_MIN_TEL_LENGTH = ConfigurableParameters.getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MIN_TEL_LENGTH);
  /**
   * The default maximum telephone number length
   */
  public static final int DEFAULT_MAX_TEL_LENGTH = ConfigurableParameters.getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MAX_TEL_LENGTH);

  /**
   * Default constructor
   */
  protected TelephonePropertyMetadata()
  {
  }

  /**
   * Constructor initializes with default constants
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public TelephonePropertyMetadata(String name, boolean notNull, boolean notEmpty)
  {
    this(name, DEFAULT_MIN_TEL_LENGTH, DEFAULT_MAX_TEL_LENGTH, notNull, notEmpty);
  }

  /**
   * Constructor initializes allowing custom constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public TelephonePropertyMetadata(String name, int minLength, int maxLength, boolean notNull, boolean notEmpty)
  {
    this(name, ConfigurableParameters.tryGetIterable(ConfigurableConsts.VALIDATION_ALLOWED_TEL_CHARS), minLength, maxLength, notNull,
        notEmpty);
  }

  /**
   * Constructor initializes allowing custom constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public TelephonePropertyMetadata(String name, Iterable<Character> allowedChars, int minLength, int maxLength, boolean notNull,
                                   boolean notEmpty)
  {
    super(name, allowedChars, minLength, maxLength, notNull, notEmpty, true);
  }
}
