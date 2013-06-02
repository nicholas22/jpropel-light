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
 * Class aiding in validation of strings, stripping any disallowed characters from the value before validating.
 */
public class EncodedStrippedStringPropertyMetadata
    extends EncodedStringPropertyMetadata
{
  /**
   * Default constructor
   */
  protected EncodedStrippedStringPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with specified constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public EncodedStrippedStringPropertyMetadata(String name, Iterable<Character> allowedCharacters, int minLength, int maxLength,
                                               boolean notNull, boolean notEmpty, boolean noNullChars)
  {
    super(name, allowedCharacters, minLength, maxLength, notNull, notEmpty, noNullChars);
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
      value = stripNonAllowedChars(value.toCharArray());

    // call base validation methods
    value = super.validate(value);

    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void checkAllCharsAllowed(char[] value)
  {
    // does nothing, to override failing upon encountering a disallowed char, because these are going to be stripped
  }

  /**
   * Removes all characters that are not in the allowed characters set
   */
  protected String stripNonAllowedChars(char[] value)
  {
    StringBuilder sb = new StringBuilder(value.length);
    for (char ch : value)
      if (Linq.contains(getAllowedChars(), ch))
        sb.append(ch);

    return sb.toString();
  }
}
