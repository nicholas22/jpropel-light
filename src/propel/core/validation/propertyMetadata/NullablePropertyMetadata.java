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
 * Class aiding in validation of reference classes
 */
public class NullablePropertyMetadata<T>
    extends AbstractPropertyMetadata<T>
{
  /**
   * Error message for null
   */
  public static final String SHOULD_NOT_BE_NULL = "%s should not be null.";
  /**
   * True if nulls are not allowed as valid values
   */
  private boolean notNull;

  /**
   * Default constructor
   */
  protected NullablePropertyMetadata()
  {
  }

  /**
   * Initializes with the property name and whether null values are not allowed
   * 
   * @throws IllegalArgumentException Property metadata name cannot be null or empty!
   */
  public NullablePropertyMetadata(String name, boolean notNull)
  {
    super(name);
    this.notNull = notNull;
  }

  public boolean getNotNull()
  {
    return notNull;
  }

  public void setNotNull(boolean notNull)
  {
    this.notNull = notNull;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T validate(T value)
      throws ValidationException
  {
    checkNotNull(value);

    return value;
  }

  protected void checkNotNull(T value)
      throws ValidationException
  {
    if (getNotNull())
      if (value == null)
        throw new ValidationException(String.format(SHOULD_NOT_BE_NULL, getName()));
  }
}
