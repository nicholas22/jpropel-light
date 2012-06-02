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

import propel.core.common.CONSTANT;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Provides utility functionality for networking stuff 
 */
public final class NetworkUtils
{
  // TODO: implement missing methods
  
  private NetworkUtils()
  {
  }

  /**
   * Returns the IP address from an endpoint. If this could not be done, null is returned.
   * 
   * @throws NullPointerException An argument is null
   */
  public static String getIPFromEndpoint(SocketAddress endPoint)
  {
    if (endPoint == null)
      throw new NullPointerException("endPoint");

    InetSocketAddress ipEndpoint;
    try
    {
      ipEndpoint = (InetSocketAddress) endPoint;
    }
    catch(ClassCastException e)
    {
      return null;
    }

    String result = ipEndpoint.getAddress().toString();
    if (!StringUtils.isNullOrEmpty(result))
    {
      result = StringUtils.trim(result, new char[] {CONSTANT.FORWARD_SLASH_CHAR, CONSTANT.BACK_SLASH_CHAR});
      return result;
    }

    return null;
  }
}
