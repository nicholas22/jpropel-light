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

import propel.core.userTypes.UnsignedShort;
import propel.core.validation.ValidationException;

/**
 * Class aiding in validation of network ports
 */
public class PortPropertyMetadata
    extends UInt16PropertyMetadata
{
  /**
   * Default constructor
   */
  public PortPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with default 0..65535 range
   * 
   * @throws IllegalArgumentException And argument is invalid
   */
  public PortPropertyMetadata(String name)
  {
    super(name, UnsignedShort.MIN_VALUE, UnsignedShort.MAX_VALUE, true);
  }

  /**
   * Constructor initializes with custom range
   * 
   * @throws IllegalArgumentException And argument is invalid
   * @throws NumberFormatException A number is invalid
   */
  public PortPropertyMetadata(String name, int minPort, int maxPort)
  {
    super(name, new UnsignedShort(minPort), new UnsignedShort(maxPort), true);
  }

  /**
   * Constructor initializes with custom range
   * 
   * @throws IllegalArgumentException And argument is invalid
   */
  public PortPropertyMetadata(String name, UnsignedShort minPort, UnsignedShort maxPort, boolean notNull)
  {
    super(name, minPort, maxPort, notNull);
  }

  /**
   * Validates a port number
   * 
   * @throws ValidationException A validation error occurs
   */
  public int validate(int port)
      throws ValidationException
  {
    UnsignedShort us;
    try
    {
      us = new UnsignedShort(port);
    }
    catch(NumberFormatException e)
    {
      throw new ValidationException("Number is not within valid TCP port range: " + port);
    }

    return validate(us).intValue();
  }
}
