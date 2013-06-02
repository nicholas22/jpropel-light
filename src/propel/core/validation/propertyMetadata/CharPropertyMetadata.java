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
import propel.core.validation.ValidationException;

/**
 * Class aiding in validation of characters
 */
public class CharPropertyMetadata
    extends BoundedValueTypePropertyMetadata<Character>
{
  /**
   * Error message for null character
   */
  public static final String SHOULD_NOT_BE_NULL_CHAR = "%s should not be the null character.";
  /**
   * True if the value cannot be the special character '\0'
   */
  private boolean notNullChar;

  /**
   * Default constructor
   */
  protected CharPropertyMetadata()
  {
  }

  /**
   * Initializes with the property name and a pair of a min and max values (inclusive), as well as whether the null '\0' char is not
   * allowed.
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public CharPropertyMetadata(String name, char minValue, char maxValue, boolean notNullChar)
  {
    super(name, minValue, maxValue);
    this.notNullChar = notNullChar;
  }

  /**
   * Initializes with the property name and a pair of a min and max values (inclusive), as well as whether the null '\0' char is not
   * allowed.
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public CharPropertyMetadata(String name, Character minValue, Character maxValue, boolean notNullChar, boolean notNull)
  {
    super(name, minValue, maxValue, notNull);
    this.notNullChar = notNullChar;
  }

  public boolean getNotNullChar()
  {
    return notNullChar;
  }

  public void setNotNullChar(boolean notNullChar)
  {
    this.notNullChar = notNullChar;
  }

  /**
   * {@inheritDoc}
   */
  public Character validate(Character value)
      throws ValidationException

  {
    value = super.validate(value);

    // only check for non null character if the reference is not null
    if (value != null)
      checkNotNullChar(value);

    return value;
  }

  protected void checkNotNullChar(char value)
      throws ValidationException
  {
    if (getNotNullChar())
      if (value == CONSTANT.NULL_CHAR)
        throw new ValidationException(String.format(SHOULD_NOT_BE_NULL_CHAR, getName()));
  }
}
