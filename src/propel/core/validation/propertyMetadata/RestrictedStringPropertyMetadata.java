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
 * Class aiding in validation of strings, preventing certain characters from appearing in the string
 */
public class RestrictedStringPropertyMetadata
    extends StringPropertyMetadata
{
  /**
   * Error message when a string cannot contain a character
   */
  public static final String CANNOT_CONTAIN = "%s is not allowed to contain this character: ";
  /**
   * The characters that are not allowed.
   */
  private Iterable<Character> disallowedChars;

  /**
   * Default constructor
   */
  protected RestrictedStringPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with specified constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public RestrictedStringPropertyMetadata(String name, Iterable<Character> disallowedChars, int minLength, int maxLength, boolean notNull,
                                          boolean notEmpty, boolean noNullChars)
  {
    super(name, minLength, maxLength, notNull, notEmpty, noNullChars);
    if (disallowedChars == null)
      throw new IllegalArgumentException("disallowedChars is null");

    this.disallowedChars = disallowedChars;
  }

  public Iterable<Character> getDisallowedChars()
  {
    return disallowedChars;
  }

  public void setDisallowedChars(Iterable<Character> disallowedChars)
  {
    this.disallowedChars = disallowedChars;
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
      checkNoDisallowedChars(value.toCharArray());

    return value;
  }

  protected void checkNoDisallowedChars(char[] value)
      throws ValidationException
  {
    for (char ch : value)
      if (Linq.contains(getDisallowedChars(), ch))
        throw new ValidationException(String.format(CANNOT_CONTAIN, getName()) + "'" + ch + "'");
  }
}
