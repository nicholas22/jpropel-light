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

import org.joda.time.LocalDateTime;
import propel.core.validation.ValidationException;

/**
 * Class aiding in validation of LocalDateTime classes
 */
public class LocalDateTimePropertyMetadata
    extends NullablePropertyMetadata<LocalDateTime>
{
  private LocalDateTime minValue;
  private LocalDateTime maxValue;

  /**
   * Default constructor
   */
  protected LocalDateTimePropertyMetadata()
  {
  }

  /**
   * Constructor initializing with a min and a max value
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public LocalDateTimePropertyMetadata(String name, LocalDateTime minValue, LocalDateTime maxValue, boolean notNull)
  {
    super(name, notNull);
    if (minValue == null)
      throw new IllegalArgumentException("minValue is null");
    if (maxValue == null)
      throw new IllegalArgumentException("maxValue is null");

    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  public LocalDateTime getMinValue()
  {
    return minValue;
  }

  public void setMinValue(LocalDateTime minValue)
  {
    this.minValue = minValue;
  }

  public LocalDateTime getMaxValue()
  {
    return maxValue;
  }

  public void setMaxValue(LocalDateTime maxValue)
  {
    this.maxValue = maxValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LocalDateTime validate(LocalDateTime value)
      throws ValidationException
  {
    value = super.validate(value);

    // only check bounds if not null
    if (value != null)
      checkBounds(value);

    return value;
  }

  protected void checkBounds(LocalDateTime value)
      throws ValidationException
  {
    // check conditions
    if (value.compareTo(getMaxValue()) > 0)
      throw new ValidationException(String.format(BoundedValueTypePropertyMetadata.SHOULD_NOT_BE_GREATER_THAN, getName()) + getMaxValue());

    if (value.compareTo(getMinValue()) < 0)
      throw new ValidationException(String.format(BoundedValueTypePropertyMetadata.SHOULD_NOT_BE_LESS_THAN, getName()) + getMinValue());
  }
}
