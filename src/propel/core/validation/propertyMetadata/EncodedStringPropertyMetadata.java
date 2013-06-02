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

import propel.core.utils.Linq;
import propel.core.validation.ValidationException;

/**
 * Class aiding in validation of strings, limiting the content to certain character sets
 */
public class EncodedStringPropertyMetadata
    extends StringPropertyMetadata
{
  /**
   * Error message when an encoded string contains a character it should not
   */
  public static final String CANNOT_CONTAIN = "%s is not allowed to contain this character: ";
  /**
   * The characters that are allowed.
   */
  private Iterable<Character> allowedChars;

  /**
   * Default constructor
   */
  protected EncodedStringPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with specified constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public EncodedStringPropertyMetadata(String name, Iterable<Character> allowedCharacters, int minLength, int maxLength, boolean notNull,
                                       boolean notEmpty, boolean noNullChars)
  {
    super(name, minLength, maxLength, notNull, notEmpty, noNullChars);
    if (allowedCharacters == null)
      throw new IllegalArgumentException("allowedCharacters is null");

    this.allowedChars = allowedCharacters;
  }

  public Iterable<Character> getAllowedChars()
  {
    return allowedChars;
  }

  public void setAllowedChars(Iterable<Character> allowedChars)
  {
    this.allowedChars = allowedChars;
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
      checkAllCharsAllowed(value.toCharArray());

    return value;
  }

  protected void checkAllCharsAllowed(char[] value)
      throws ValidationException
  {
    for (char ch : value)
      if (!Linq.contains(getAllowedChars(), ch))
        throw new ValidationException(String.format(CANNOT_CONTAIN, getName()) + "'" + ch + "'");
  }
}
