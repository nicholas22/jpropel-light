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
 * Class aiding in validation of file names
 */
public class FileNamePropertyMetadata
    extends RestrictedEdgesI18NStringPropertyMetadata
{
  /**
   * The default minimum filename length
   */
  public static final int DEFAULT_MIN_FILENAME_LENGTH = ConfigurableParameters
      .getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MIN_FILENAME_LENGTH);
  /**
   * The default maximum filename length
   */
  public static final int DEFAULT_MAX_FILENAME_LENGTH = ConfigurableParameters
      .getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MAX_FILENAME_LENGTH);
  /**
   * Whether a windows file name. This is null if not specified, in which case we use the current OS.
   */
  private boolean windowsFilename;

  /**
   * Default constructor
   */
  protected FileNamePropertyMetadata()
  {
  }

  /**
   * Constructor initializes with default constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public FileNamePropertyMetadata(String name, boolean notNull, boolean notEmpty)
  {
    this(name, DEFAULT_MIN_FILENAME_LENGTH, DEFAULT_MAX_FILENAME_LENGTH, notNull, notEmpty, null);
  }

  /**
   * Constructor initializes with default constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public FileNamePropertyMetadata(String name, boolean notNull, boolean notEmpty, Boolean windowsFilename)
  {
    this(name, DEFAULT_MIN_FILENAME_LENGTH, DEFAULT_MAX_FILENAME_LENGTH, notNull, notEmpty, windowsFilename);
  }

  /**
   * Constructor initializes with custom constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public FileNamePropertyMetadata(String name, int minLength, int maxLength, boolean notNull, boolean notEmpty)
  {
    this(name, ConfigurableParameters.tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_FILENAME_CHARS), ConfigurableParameters
        .tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_FILENAME_START_CHARS), ConfigurableParameters
        .tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_FILENAME_END_CHARS), minLength, maxLength, notNull, notEmpty, false, null);
  }

  /**
   * Constructor initializes with custom constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public FileNamePropertyMetadata(String name, int minLength, int maxLength, boolean notNull, boolean notEmpty, Boolean windowsFilename)
  {
    this(name, ConfigurableParameters.tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_FILENAME_CHARS), ConfigurableParameters
        .tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_FILENAME_START_CHARS), ConfigurableParameters
        .tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_FILENAME_END_CHARS), minLength, maxLength, notNull, notEmpty, false,
        windowsFilename);
  }

  /**
   * Constructor initializes with fully custom constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public FileNamePropertyMetadata(String name, Iterable<Character> disallowedChars, Iterable<Character> disallowedStartChars,
                                  Iterable<Character> disallowedEndChars, int minLength, int maxLength, boolean notNull, boolean notEmpty,
                                  boolean noUnicodeChars, Boolean windowsFilename)
  {
    super(name, disallowedChars, disallowedStartChars, disallowedEndChars, minLength, maxLength, notNull, notEmpty, true, noUnicodeChars);

    if (windowsFilename != null)
      this.windowsFilename = windowsFilename;
    else
      this.windowsFilename = OsUtils.isWindows();
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
   * Throws an exception if on a Windows filename we have the name ending with a dot
   */
  protected void checkNoMultipleDots(String value)
      throws ValidationException
  {
    if (windowsFilename)
      if (value.endsWith("."))
        if (!value.equals(".") && !value.equals("..")) // these are the current and parent directory
          throw new ValidationException("Windows file/directory names cannot end with a dot: " + value);
  }

}
