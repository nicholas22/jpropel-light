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
 * Class aiding in validation of bounded structs, e.g. integers
 */
public class BoundedValueTypePropertyMetadata<T extends Comparable<T>>
    extends NullablePropertyMetadata<T>
{
  /**
   * Error message when a maximum value is less than the minimum value
   */
  public static final String PROPERTY_ERROR_MAX_LESS_THAN_MIN = "%s maximum value cannot be less than allowed minimum!";
  /**
   * Error message when a value is too high
   */
  public static final String SHOULD_NOT_BE_GREATER_THAN = "%s should not be greater than ";
  /**
   * Error message when a value is too low
   */
  public static final String SHOULD_NOT_BE_LESS_THAN = "%s should not be less than ";
  /**
   * The minimum inclusive value allowed
   */
  private T minValue;
  /**
   * The maximum inclusive value allowed
   */
  private T maxValue;

  /**
   * Default constructor
   */
  protected BoundedValueTypePropertyMetadata()
  {
  }

  /**
   * Initializes with the property name and a pair of a min and max values (inclusive)
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public BoundedValueTypePropertyMetadata(String name, T minValue, T maxValue)
  {
    this(name, minValue, maxValue, true);
  }

  /**
   * Initializes with the property name and a pair of a min and max values (inclusive)
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public BoundedValueTypePropertyMetadata(String name, T minValue, T maxValue, boolean notNull)
  {
    super(name, notNull);

    if (minValue == null)
      throw new IllegalArgumentException("minValue is null");
    if (maxValue == null)
      throw new IllegalArgumentException("maxValue is null");

    this.minValue = minValue;
    this.maxValue = maxValue;
    if (minValue.compareTo(maxValue) > 0)
      throw new IllegalArgumentException(String.format(PROPERTY_ERROR_MAX_LESS_THAN_MIN, name));
  }

  public T getMinValue()
  {
    return minValue;
  }

  public void setMinValue(T minValue)
  {
    this.minValue = minValue;
  }

  public T getMaxValue()
  {
    return maxValue;
  }

  public void setMaxValue(T maxValue)
  {
    this.maxValue = maxValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T validate(T value)
      throws ValidationException
  {
    value = super.validate(value);

    // only check bounds if not null
    if (value != null)
      checkBounds(value);

    return value;
  }

  protected void checkBounds(T value)
      throws ValidationException
  {
    // check conditions
    if (value.compareTo(getMaxValue()) > 0)
      throw new ValidationException(String.format(SHOULD_NOT_BE_GREATER_THAN, getName()) + getMaxValue());

    if (value.compareTo(getMinValue()) < 0)
      throw new ValidationException(String.format(SHOULD_NOT_BE_LESS_THAN, getName()) + getMinValue());
  }
}
