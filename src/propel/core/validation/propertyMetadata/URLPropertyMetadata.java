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

import java.net.URL;

/**
 * Class aiding in validation of URL classes
 */
public class URLPropertyMetadata
    extends NullablePropertyMetadata<URL>
{
  /**
   * Default constructor
   */
  protected URLPropertyMetadata()
  {
  }

  /**
   * Initializes with the property name and whether null values are not allowed
   * 
   * @throws IllegalArgumentException Property metadata name cannot be null or empty!
   */
  public URLPropertyMetadata(String name, boolean notNull)
  {
    super(name, notNull);
  }

}
