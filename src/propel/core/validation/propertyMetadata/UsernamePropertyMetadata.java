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
 * Class aiding in validation of Active Directory usernames
 */
public class UsernamePropertyMetadata
    extends RestrictedEdgesI18NStringPropertyMetadata
{
  /**
   * The default minimum username length
   */
  public static final int DEFAULT_MIN_USERNAME_LENGTH = ConfigurableParameters
      .getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MIN_WIN_USERNAME_LENGTH);
  /**
   * The default maximum username length
   */
  public static final int DEFAULT_MAX_USERNAME_LENGTH = ConfigurableParameters
      .getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MAX_WIN_USERNAME_LENGTH);

  /**
   * Default constructor
   */
  protected UsernamePropertyMetadata()
  {
  }

  /**
   * Constructor initializes with Active Directory constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public UsernamePropertyMetadata(String name, boolean notNull, boolean notEmpty)
  {
    this(name, DEFAULT_MIN_USERNAME_LENGTH, DEFAULT_MAX_USERNAME_LENGTH, notNull, notEmpty);
  }

  /**
   * Constructor initializes with Active Directory constraints allowing for length customisation
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public UsernamePropertyMetadata(String name, int minLength, int maxLength, boolean notNull, boolean notEmpty)
  {
    this(name, ConfigurableParameters.tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_AD_USERNAME_CHARS), ConfigurableParameters
        .tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_AD_USERNAME_START_CHARS), ConfigurableParameters
        .tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_AD_USERNAME_END_CHARS), minLength, maxLength, notNull, notEmpty, true,
        false);
  }

  /**
   * Constructor initializes allowing custom constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public UsernamePropertyMetadata(String name, Iterable<Character> disallowedChars, Iterable<Character> disallowedStartChars,
                                  Iterable<Character> disallowedEndChars, int minLength, int maxLength, boolean notNull, boolean notEmpty,
                                  boolean noNullChars, boolean noUnicodeChars)
  {
    super(name, disallowedChars, disallowedStartChars, disallowedEndChars, minLength, maxLength, notNull, notEmpty, noNullChars,
        noUnicodeChars);
  }
}
