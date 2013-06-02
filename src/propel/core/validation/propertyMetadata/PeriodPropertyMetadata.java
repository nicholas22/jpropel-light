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

import org.joda.time.Period;
import propel.core.validation.ValidationException;
import propel.core.validation.propertyMetadata.BoundedValueTypePropertyMetadata;
import propel.core.validation.propertyMetadata.NullablePropertyMetadata;

/**
 * Class aiding in validation of Period classes
 */
public class PeriodPropertyMetadata
    extends NullablePropertyMetadata<Period>
{
  private Period minValue;
  private Period maxValue;

  /**
   * Default constructor
   */
  protected PeriodPropertyMetadata()
  {
  }

  /**
   * Constructor initializing with a min and a max value
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public PeriodPropertyMetadata(String name, Period minValue, Period maxValue, boolean notNull)
  {
    super(name, notNull);
    if (minValue == null)
      throw new IllegalArgumentException("minValue is null");
    if (maxValue == null)
      throw new IllegalArgumentException("maxValue is null");

    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  public Period getMinValue()
  {
    return minValue;
  }

  public void setMinValue(Period minValue)
  {
    this.minValue = minValue;
  }

  public Period getMaxValue()
  {
    return maxValue;
  }

  public void setMaxValue(Period maxValue)
  {
    this.maxValue = maxValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Period validate(Period value)
      throws ValidationException
  {
    value = super.validate(value);

    // only check bounds if not null
    if (value != null)
      checkBounds(value);

    return value;
  }

  protected void checkBounds(Period value)
      throws ValidationException
  {
    // check conditions    
    if (value.toStandardSeconds().getSeconds() > getMaxValue().toStandardSeconds().getSeconds())
      throw new ValidationException(String.format(BoundedValueTypePropertyMetadata.SHOULD_NOT_BE_GREATER_THAN, getName()) + getMaxValue());

    if (value.toStandardSeconds().getSeconds() < getMinValue().toStandardSeconds().getSeconds())
      throw new ValidationException(String.format(BoundedValueTypePropertyMetadata.SHOULD_NOT_BE_LESS_THAN, getName()) + getMinValue());
  }
}
