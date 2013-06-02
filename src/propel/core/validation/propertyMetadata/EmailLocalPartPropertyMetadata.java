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
import propel.core.configuration.ConfigurableConsts;
import propel.core.configuration.ConfigurableParameters;
import propel.core.validation.ValidationException;

/**
 * Class aiding in validation of email local parts (the text before @)
 */
public class EmailLocalPartPropertyMetadata
    extends RestrictedEdgesI18NStringPropertyMetadata
{
  /**
   * Error message when an email local-part contains two dots in succession
   */
  public static final String CANNOT_CONTAIN_DOUBLE_DOT = "%s cannot contain two dots in succession.";
  /**
   * The default maximum email local part length
   */
  public static final int DEFAULT_MAX_LOCALPART_LENGTH = ConfigurableParameters
      .getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MAX_LOCALPART_LENGTH);
  /**
   * The default minimum email username length
   */
  public static final int DEFAULT_MIN_LOCALPART_LENGTH = ConfigurableParameters
      .getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MIN_LOCALPART_LENGTH);

  /**
   * Default constructor
   */
  protected EmailLocalPartPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with RFC-specified email local-part constraints and whether to allow international characters
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public EmailLocalPartPropertyMetadata(String name, boolean notNull, boolean notEmpty, boolean noUnicodeChars)
  {
    this(name, DEFAULT_MIN_LOCALPART_LENGTH, DEFAULT_MAX_LOCALPART_LENGTH, notNull, notEmpty, noUnicodeChars);
  }

  /**
   * Constructor initializes with custom constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public EmailLocalPartPropertyMetadata(String name, int minLength, int maxLength, boolean notNull, boolean notEmpty, boolean noUnicodeChars)
  {
    this(name, ConfigurableParameters.tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_EMAIL_LOCALPART_CHARS),
        ConfigurableParameters.tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_EMAIL_LOCALPART_START_CHARS), ConfigurableParameters
            .tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_EMAIL_LOCALPART_END_CHARS), minLength, maxLength, notNull, notEmpty,
        noUnicodeChars);
  }

  /**
   * Constructor initializes with custom constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public EmailLocalPartPropertyMetadata(String name, Iterable<Character> disallowedChars, Iterable<Character> disallowedStartChars,
                                        Iterable<Character> disallowedEndChars, int minLength, int maxLength, boolean notNull,
                                        boolean notEmpty, boolean noUnicodeChars)
  {
    super(name, disallowedChars, disallowedStartChars, disallowedEndChars, minLength, maxLength, notNull, notEmpty, true, noUnicodeChars);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String validate(String value)
      throws ValidationException
  {
    value = super.validate(value);

    // only check if not null
    if (value != null)
      checkNoConsecutiveDots(value.toCharArray());

    return value;
  }

  protected void checkNoConsecutiveDots(char[] value)
      throws ValidationException
  {
    boolean dotFound = false;

    // scan for double dots
    for (char ch : value)
      if (ch == CONSTANT.DOT_CHAR)
      {
        if (dotFound)
          throw new ValidationException(String.format(CANNOT_CONTAIN_DOUBLE_DOT, getName()));
        else
          dotFound = true;
      } else
        dotFound = false;
  }
}
