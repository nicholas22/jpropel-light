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
 * Class aiding in validation of internationalised (i18n) strings, stripping from content certain characters and disallowing certain
 * characters from being at the beginning or end of the string.
 */
public class RestrictedEdgesI18NStrippedStringPropertyMetadata
    extends RestrictedEdgesI18NStringPropertyMetadata
{
  /**
   * Default constructor
   */
  protected RestrictedEdgesI18NStrippedStringPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with custom constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public RestrictedEdgesI18NStrippedStringPropertyMetadata(String name, Iterable<Character> strippedChars,
                                                           Iterable<Character> disallowedStartChars,
                                                           Iterable<Character> disallowedEndChars, int minLength, int maxLength,
                                                           boolean notNull, boolean notEmpty, boolean noNullChars, boolean noUnicodeChars)
  {
    super(name, strippedChars, disallowedStartChars, disallowedEndChars, minLength, maxLength, notNull, notEmpty, noNullChars,
        noUnicodeChars);
  }

  /**
   * Validates the given value with the current constraints. Strips disallowed characters and returns validated result.
   * 
   * @throws ValidationException A validation error occurred.
   */
  public String validate(String value)
      throws ValidationException
  {
    // strip characters first
    if (value != null)
      value = stripNonAllowedChars(value);

    // then perform base validation
    value = super.validate(value);

    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void checkNoDisallowedChars(char[] value)
  {
    // does nothing, to override failing upon encountering a disallowed char, because these are going to be stripped
  }

  /**
   * Removes all characters that are not in the allowed characters set
   */
  protected String stripNonAllowedChars(String value)
  {
    StringBuilder result = new StringBuilder(value.length());

    for (char ch : value.toCharArray())
      if (!Linq.contains(getDisallowedChars(), ch))
        result.append(ch);

    return result.toString();
  }
}
