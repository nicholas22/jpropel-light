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
import java.util.UUID;

/**
 * Class aiding in validation of GUIDs
 */
public class UUIDPropertyMetadata
    extends NullablePropertyMetadata<UUID>
{
  /**
   * True if the value cannot be the zero-ed GUID
   */
  private boolean nonZero;
  /**
   * Error message for empty UUID
   */
  public static final String SHOULD_NOT_BE_ZERO_GUID = "%s should not be an un-initialized/empty UUID.";

  /**
   * Default constructor
   */
  protected UUIDPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with option of whether to allow zero-valued uuids
   * 
   * @throws IllegalArgumentException Indicates than an argument is invalid
   */
  public UUIDPropertyMetadata(String name, boolean nonZero, boolean notNull)
  {
    super(name, notNull);
    this.nonZero = nonZero;
  }

  public boolean getNonZero()
  {
    return nonZero;
  }

  public void setNonZero(boolean nonZero)
  {
    this.nonZero = nonZero;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID validate(UUID value)
      throws ValidationException
  {
    value = super.validate(value);

    // only check bounds if not null
    if (value != null)
      checkNonZero(value);

    return value;
  }

  /**
   * Throws a ValidationException if the specified value is the empty UUID (all zero digits).
   * 
   * @throws ValidationException When validation errors occur.
   */
  protected void checkNonZero(UUID value)
      throws ValidationException
  {
    if (getNonZero())
      if (value.equals(CONSTANT.EMPTY_UUID))
        throw new ValidationException(String.format(SHOULD_NOT_BE_ZERO_GUID, getName()));
  }
}
