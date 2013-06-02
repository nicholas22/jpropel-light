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
import propel.core.utils.OsUtils;
import propel.core.validation.ValidationException;

/**
 * Class aiding in validation of file paths
 */
public class PathPropertyMetadata
    extends RestrictedEdgesI18NStringPropertyMetadata
{
  /**
   * The default minimum path length
   */
  public static final int DEFAULT_MIN_PATH_LENGTH = ConfigurableParameters.getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MIN_PATH_LENGTH);
  /**
   * The default maximum path length
   */
  public static final int DEFAULT_MAX_PATH_LENGTH = ConfigurableParameters.getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MAX_PATH_LENGTH);

  /**
   * Default constructor
   */
  protected PathPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with default constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public PathPropertyMetadata(String name, boolean notNull, boolean notEmpty)
  {
    this(name, DEFAULT_MIN_PATH_LENGTH, DEFAULT_MAX_PATH_LENGTH, notNull, notEmpty);
  }

  /**
   * Constructor initializes with custom but platform-specific character constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public PathPropertyMetadata(String name, int minLength, int maxLength, boolean notNull, boolean notEmpty)
  {
    this(name, ConfigurableParameters.tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_PATH_CHARS), ConfigurableParameters
        .tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_PATH_START_CHARS), ConfigurableParameters
        .tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_PATH_END_CHARS), minLength, maxLength, notNull, notEmpty, false);
  }

  /**
   * Constructor initializes with custom but platform-specific character constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public PathPropertyMetadata(String name, Iterable<Character> disallowedChars, Iterable<Character> disallowedStartChars,
                              Iterable<Character> disallowedEndChars, int minLength, int maxLength, boolean notNull, boolean notEmpty,
                              boolean noUnicodeChars)
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
      checkNoMultipleDots(value);

    return value;
  }

  /**
   * Throws an exception if on Windows a path ends with a dot
   */
  protected void checkNoMultipleDots(String value)
      throws ValidationException
  {
    if (OsUtils.isWindows())
      if (value.endsWith("."))
        if (!value.equals(".") && !value.equals("..")) // these are the current and parent directory
          throw new ValidationException("Windows file/directory names cannot end with a dot: " + value);
  }
}
