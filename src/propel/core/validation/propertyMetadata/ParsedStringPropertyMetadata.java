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
 * Class aiding in validation of parsed strings, trimming whitespace around the value before validating
 */
public class ParsedStringPropertyMetadata
    extends StringPropertyMetadata
{
  // true if only ASCII printable chars are allowed

  private boolean onlyAsciiPrintableCharsAllowed;

  /**
   * Constructor
   */
  public ParsedStringPropertyMetadata(String name, int minLength, int maxLength, boolean notNull, boolean notEmpty, boolean noNullChars,
                                      boolean onlyAsciiPrintableCharsAllowed)
  {
    super(name, minLength, maxLength, notNull, notEmpty, noNullChars);
    this.onlyAsciiPrintableCharsAllowed = onlyAsciiPrintableCharsAllowed;
  }

  /**
   * Validates the given value with the current constraints. Strips disallowed characters and returns validated result.
   * 
   * @throws ValidationException A validation error occurred.
   */
  @Override
  public String validate(String value)
      throws ValidationException
  {
    // strip first, to avoid logical problems with length
    if (value != null)
    {
      value = trimWhitespaceChars(value);

      // then check if only ASCII printable allowed e.g. for when parsing ASCII text files
      if (getOnlyAsciiPrintableCharsAllowed())
        checkOnlyAsciiPrintableChars(value);
    }

    // call base validation methods
    value = super.validate(value);

    return value;
  }

  public boolean getOnlyAsciiPrintableCharsAllowed()
  {
    return onlyAsciiPrintableCharsAllowed;
  }

  public void setOnlyAsciiPrintableCharsAllowed(boolean value)
  {
    onlyAsciiPrintableCharsAllowed = value;
  }

  /**
   * Override this method to make it trim starting and ending with trim-able chars (e.g. space, tab, CRLF)
   */
  protected String trimWhitespaceChars(String value)
  {
    if (value.length() == 0)
      return CONSTANT.EMPTY_STRING;

    int start = 0;
    int end = value.length() - 1;

    while (isTrimmable(value.charAt(start)) && start < value.length() - 1)
      start++;
    while (isTrimmable(value.charAt(end)) && end > start)
      end--;

    int length = end + 1 - start;

    return StringUtils.substring(value, start, length);
  }

  /**
   * Returns true if the given character is a whitespace character
   */
  protected boolean isTrimmable(char ch)
  {
    return StringUtils.contains(CONSTANT.WHITESPACE_CHARS, ch);
  }

  /**
   * Throws an exception if a non-ASCII (printable) char is found
   */
  protected void checkOnlyAsciiPrintableChars(String value)
      throws ValidationException
  {
    for (int i = 0; i < value.length(); i++)
    {
      char ch = value.charAt(i);

      if (ch < 32 || ch > 126)
        throw new ValidationException(String.format(EncodedStringPropertyMetadata.CANNOT_CONTAIN, getName()) + "'" + ch + "'");
    }
  }
}
