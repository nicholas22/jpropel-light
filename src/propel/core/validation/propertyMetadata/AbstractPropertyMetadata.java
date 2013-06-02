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

import propel.core.utils.StringUtils;
import propel.core.validation.ValidationException;

/**
 * Class aiding in validation
 */
public abstract class AbstractPropertyMetadata<T>
    implements IPropertyMetadata<T>
{
  /**
   * Error message for invalid property name
   */
  public static final String PROPERTY_ERROR_INVALID_NAME = "Property metadata name cannot be null or empty!";
  /**
   * The name of the property
   */
  private String name;

  /**
   * Default constructor
   */
  protected AbstractPropertyMetadata()
  {
  }

  /**
   * Initializes with the property name
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  protected AbstractPropertyMetadata(String name)
  {
    if (StringUtils.isNullOrEmpty(name))
      throw new IllegalArgumentException(PROPERTY_ERROR_INVALID_NAME);

    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Validates the given value with the current constraints.
   * 
   * @throws ValidationException Thrown upon validation errors
   */
  public abstract T validate(T value)
      throws ValidationException;
}
