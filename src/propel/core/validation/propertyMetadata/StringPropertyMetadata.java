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
import propel.core.utils.StringUtils;
import propel.core.validation.ValidationException;

/**
 * Class aiding in validation of sequences, e.g. strings, arrays, etc.
 */
public class StringPropertyMetadata
    extends NullablePropertyMetadata<String>
{
  /**
   * Error message when minimum is less than 0
   */
  public static final String PROPERTY_ERROR_MIN_LEN_LESS_THAN_ZERO = "%s minimum length cannot be less than zero!";
  /**
   * Error message when maximum is less than 0
   */
  public static final String PROPERTY_ERROR_MAX_LEN_LESS_THAN_ZERO = "%s maximum length cannot be less than zero!";
  /**
   * Error message when maximum is less than minimum
   */
  public static final String PROPERTY_ERROR_MAX_LEN_LESS_THAN_MIN_LEN = "%s length maximum cannot be less than allowed minimum!";
  /**
   * Error message when sequence is too long
   */
  public static final String SHOULD_BE_EXACTLY = "%s should be %d characters in length.";
  /**
   * Error message when sequence is too short
   */
  public static final String SHOULD_BE_LONGER = "%s should be %d or more characters in length.";
  /**
   * Error message when sequence is too long
   */
  public static final String SHOULD_BE_SHORTER = "%s should be %d or fewer characters in length.";
  /**
   * Error message when sequence is empty
   */
  public static final String SHOULD_NOT_BE_EMPTY = "%s should not be empty.";
  /**
   * Error message when null chars are contained
   */
  public static final String SHOULD_NOT_CONTAIN_NULL_CHARS = "%s is not allowed to contain null characters.";
  /**
   * The minimum allowed value length
   */
  private int minLength;
  /**
   * The maximum allowed value length
   */
  private int maxLength;
  /**
   * True if the sequence is not allowed to be empty
   */
  private boolean notEmpty;
  /**
   * True if '\0' characters are not allowed to be contained in the string
   */
  private boolean noNullChars;

  /**
   * Default constructor
   */
  protected StringPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with specified constraints
   * 
   * @throws IllegalArgumentException Indicates a problem with a specified argument.
   */
  public StringPropertyMetadata(String name, int minLength, int maxLength, boolean notNull, boolean notEmpty, boolean noNullChars)
  {
    super(name, notNull);

    if (minLength < 0)
      throw new IllegalArgumentException(String.format(PROPERTY_ERROR_MIN_LEN_LESS_THAN_ZERO, name));
    this.minLength = minLength;
    if (maxLength < 0)
      throw new IllegalArgumentException(String.format(PROPERTY_ERROR_MAX_LEN_LESS_THAN_ZERO, name));
    if (maxLength < minLength)
      throw new IllegalArgumentException(String.format(PROPERTY_ERROR_MAX_LEN_LESS_THAN_MIN_LEN, name));
    this.maxLength = maxLength;
    this.notEmpty = notEmpty;
    this.noNullChars = noNullChars;
  }

  public int getMinLength()
  {
    return minLength;
  }

  public void setMinLength(int minLength)
  {
    this.minLength = minLength;
  }

  public int getMaxLength()
  {
    return maxLength;
  }

  public void setMaxLength(int maxLength)
  {
    this.maxLength = maxLength;
  }

  public boolean getNotEmpty()
  {
    return notEmpty;
  }

  public void setNotEmpty(boolean notEmpty)
  {
    this.notEmpty = notEmpty;
  }

  public boolean getNoNullChars()
  {
    return noNullChars;
  }

  public void setNoNullChars(boolean noNullChars)
  {
    this.noNullChars = noNullChars;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String validate(String value)
      throws ValidationException
  {
    value = super.validate(value);

    // only check further if not null
    if (value != null)
      // check if empty first
      if (!checkEmpty(value))
      {
        // check length only if not empty
        checkLength(value);
        // check no null chars only if not empty
        checkNoNullChars(value);
      }

    return value;
  }

  protected boolean checkEmpty(String value)
      throws ValidationException
  {
    boolean empty = value.equals(CONSTANT.EMPTY_STRING);

    if (getNotEmpty())
      if (empty)
        throw new ValidationException(String.format(SHOULD_NOT_BE_EMPTY, getName()));

    return empty;
  }

  protected void checkLength(String value)
      throws ValidationException
  {
    // check lengths
    int length = value.length();

    if (getMinLength() == getMaxLength())
      if (length != getMinLength())
        throw new ValidationException(String.format(SHOULD_BE_EXACTLY, getName(), getMaxLength()));
    if (length < getMinLength())
      throw new ValidationException(String.format(SHOULD_BE_LONGER, getName(), getMinLength()));
    if (length > getMaxLength())
      throw new ValidationException(String.format(SHOULD_BE_SHORTER, getName(), getMaxLength()));
  }

  protected void checkNoNullChars(String value)
      throws ValidationException
  {
    if (getNoNullChars())
      if (StringUtils.contains(value, CONSTANT.NULL_CHAR))
        throw new ValidationException(String.format(SHOULD_NOT_CONTAIN_NULL_CHARS, getName()));
  }
}
