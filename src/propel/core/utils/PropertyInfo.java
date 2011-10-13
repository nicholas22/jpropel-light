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
package propel.core.utils;

import java.lang.reflect.Method;

/**
 * A class encapsulating a property, i.e. a property name and a setter and getter method for a backing field.
 */
public final class PropertyInfo
    implements Comparable<PropertyInfo>
{

  private String name;
  private Method getter;
  private Method setter;
  private Class<?> declaringType;
  private Class<?> propertyType;

  /**
   * Initializes with property information.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException The name is empty
   */
  public PropertyInfo(Class<?> declaringType, String name, Method getter, Method setter, Class<?> propertyType)
  {
    if (declaringType == null)
      throw new NullPointerException("declaringType");
    if (StringUtils.isNullOrEmpty(name))
      throw new NullPointerException("name");
    if (getter == null && setter == null)
      throw new NullPointerException("Both getter and setter are null");

    this.declaringType = declaringType;
    this.name = name;
    this.getter = getter;
    this.setter = setter;
    this.propertyType = propertyType;
  }

  /**
   * Returns the name of the property, e.g. for getName() and setName(), the property is Name
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the property getter method
   */
  public Method getGetter()
  {
    return getter;
  }

  /**
   * Returns the property setter method
   */
  public Method getSetter()
  {
    return setter;
  }

  /**
   * Returns the class which declares this property
   */
  public Class<?> getDeclaringType()
  {
    return declaringType;
  }

  /**
   * Returns the property type
   */
  public Class<?> getPropertyType()
  {
    return propertyType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(PropertyInfo other)
  {
    return name.compareTo(other.getName());
  }
}
