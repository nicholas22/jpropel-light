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
 * Class aiding in validation of strings, limiting the content from containing certain characters and disallowing certain characters from
 * being at the beginning or end of the string.
 */
public class RestrictedEdgesStringPropertyMetadata
    extends RestrictedStringPropertyMetadata
{
  /**
   * Error message when a string cannot start with a character
   */
  public static final String CANNOT_START_WITH = "%s is not allowed to start with this character: ";
  /**
   * Error message when a string cannot end with a character
   */
  public static final String CANNOT_END_WITH = "%s is not allowed to end with this character: ";
  /**
   * The property value must not start with these characters.
   */
  private Iterable<Character> disallowedStartChars;
  /**
   * The property value must not end with these characters.
   */
  private Iterable<Character> disallowedEndChars;

  /**
   * Default constructor
   */
  protected RestrictedEdgesStringPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with custom constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public RestrictedEdgesStringPropertyMetadata(String name, Iterable<Character> disallowedChars, Iterable<Character> disallowedStartChars,
                                               Iterable<Character> disallowedEndChars, int minLength, int maxLength, boolean notNull,
                                               boolean notEmpty, boolean noNullChars)
  {
    super(name, disallowedChars, minLength, maxLength, notNull, notEmpty, noNullChars);

    if (disallowedStartChars == null)
      throw new IllegalArgumentException("disallowedStartChars is null");
    if (disallowedEndChars == null)
      throw new IllegalArgumentException("disallowedEndChars is null");

    this.disallowedStartChars = disallowedStartChars;
    this.disallowedEndChars = disallowedEndChars;
  }

  public Iterable<Character> getDisallowedEndChars()
  {
    return disallowedEndChars;
  }

  public void setDisallowedEndChars(Iterable<Character> disallowedEndChars)
  {
    this.disallowedEndChars = disallowedEndChars;
  }

  public Iterable<Character> getDisallowedStartChars()
  {
    return disallowedStartChars;
  }

  public void setDisallowedStartChars(Iterable<Character> disallowedStartChars)
  {
    this.disallowedStartChars = disallowedStartChars;
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
      checkNotAllowedStartOrEnd(value.toCharArray());

    return value;
  }

  protected void checkNotAllowedStartOrEnd(char[] value)
      throws ValidationException
  {
    if (value.length > 0)
    {
      char first = value[0];
      if (Linq.contains(getDisallowedStartChars(), first))
        throw new ValidationException(String.format(CANNOT_START_WITH, getName()) + "'" + first + "'");

      char last = value[value.length - 1];
      if (Linq.contains(getDisallowedEndChars(), last))
        throw new ValidationException(String.format(CANNOT_END_WITH, getName()) + "'" + last + "'");
    }
  }
}
