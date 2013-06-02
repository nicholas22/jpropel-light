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

import lombok.val;
import propel.core.collections.arrays.ReifiedArray;
import propel.core.validation.ValidationException;

/**
 * Class aiding in validation of arrays.
 */
public class ArrayPropertyMetadata
    extends EnumerablePropertyMetadata
{
  /**
   * Default constructor
   */
  protected ArrayPropertyMetadata()
  {
    super();
  }

  /**
   * Initializes with the property name and a pair of a min and max sizes (inclusive)
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public ArrayPropertyMetadata(String name, int minSize, int maxSize, boolean notNull, boolean noNullElements)
  {
    super(name, notNull);

    if (minSize < 0)
      throw new IllegalArgumentException(String.format(SHOULD_NOT_BE_NEGATIVE, "minSize"));
    if (maxSize < 0)
      throw new IllegalArgumentException(String.format(SHOULD_NOT_BE_NEGATIVE, "maxSize"));

    this.minSize = minSize;
    this.maxSize = maxSize;
    this.noNullElements = noNullElements;
    if (minSize > maxSize)
      throw new IllegalArgumentException(String.format(PROPERTY_ERROR_MAX_LESS_THAN_MIN, name));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object validate(Object value)
      throws ValidationException
  {
    super.validate(value);

    // only check further properties if not null
    if (value != null)
    {
      ReifiedArray<Object> array = new ReifiedArray<Object>(value);

      checkBounds(array);
      if (noNullElements)
        checkNoNullElements(array);
    }

    return value;
  }

  protected void checkBounds(ReifiedArray<Object> array)
      throws ValidationException
  {
    // check conditions
    if (getMaxSize() == getMinSize())
      if (array.length() != getMaxSize())
        throw new ValidationException(String.format(SHOULD_BE_EXACTLY, getName()) + getMaxSize());

    if (array.length() > getMaxSize())
      throw new ValidationException(String.format(SHOULD_NOT_BE_GREATER_THAN, getName()) + getMaxSize());

    if (array.length() < getMinSize())
      throw new ValidationException(String.format(SHOULD_NOT_BE_LESS_THAN, getName()) + getMinSize());
  }

  protected void checkNoNullElements(ReifiedArray<Object> array)
      throws ValidationException
  {
    val iterator = array.iterator();
    while (iterator.hasNext())
    {
      val elem = iterator.next();
      if (elem == null)
        throw new ValidationException(String.format(SHOULD_NOT_CONTAIN_NULL_ELEMENTS, getName()));
    }
  }
}
