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

import propel.core.validation.ValidationException;

/**
 * Class aiding in validation of internationalised (i18n) strings, limiting the content from containing certain characters and disallowing
 * certain characters from being at the beginning or end of the string.
 */
public class RestrictedEdgesI18NStringPropertyMetadata
    extends RestrictedEdgesStringPropertyMetadata
{
  /**
   * Error message when a string cannot contain unicode characters
   */
  public static final String UNICODE_CHARACTERS_NOT_ALLOWED = "%s is not allowed to contain Unicode characters such as this: ";
  /**
   * True if no Unicode characters are allowed.
   */
  private boolean noUnicodeChars;

  /**
   * Default constructor
   */
  protected RestrictedEdgesI18NStringPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with custom constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public RestrictedEdgesI18NStringPropertyMetadata(String name, Iterable<Character> disallowedChars,
                                                   Iterable<Character> disallowedStartChars, Iterable<Character> disallowedEndChars,
                                                   int minLength, int maxLength, boolean notNull, boolean notEmpty, boolean noNullChars,
                                                   boolean noUnicodeChars)
  {
    super(name, disallowedChars, disallowedStartChars, disallowedEndChars, minLength, maxLength, notNull, notEmpty, noNullChars);
    this.noUnicodeChars = noUnicodeChars;
  }

  public boolean getNoUnicodeChars()
  {
    return noUnicodeChars;
  }

  public void setNoUnicodeChars(boolean noUnicodeChars)
  {
    this.noUnicodeChars = noUnicodeChars;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String validate(String value)
      throws ValidationException
  {
    value = super.validate(value);

    if (value != null)
      checkNoUnicodeChars(value.toCharArray());

    return value;
  }

  protected void checkNoUnicodeChars(char[] value)
      throws ValidationException
  {
    if (getNoUnicodeChars())
      for (char ch : value)
        if (ch >= 128)
          throw new ValidationException(String.format(UNICODE_CHARACTERS_NOT_ALLOWED, getName()) + "'" + ch + "'");
  }
}
