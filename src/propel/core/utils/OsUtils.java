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

/**
 * Facilitates OS type polling
 */
public final class OsUtils
{
  private static final String OS_NAME_PROPERTY = "os.name";
  private static final boolean IS_WINDOWS = System.getProperty(OS_NAME_PROPERTY).contains("Windows");
  private static final boolean IS_OSX = System.getProperty(OS_NAME_PROPERTY).contains("Mac");
  private static final boolean IS_LINUX = System.getProperty(OS_NAME_PROPERTY).contains("Linux");
  private static final boolean IS_BSD = System.getProperty(OS_NAME_PROPERTY).contains("BSD");

  /**
   * Private constructor prevents instantiation
   */
  private OsUtils()
  {
  }

  /**
   * OS determination method
   * 
   * @return true if likely to be running on a Windows box
   */
  public static boolean isWindows()
  {
    return IS_WINDOWS;
  }

  /**
   * OS determination method
   * 
   * @return true if likely to be running on an OS/X box
   */
  public static boolean isOSX()
  {
    return IS_OSX;
  }

  /**
   * OS determination method
   * 
   * @return true if likely to be running on a Linux box
   */
  public static boolean isLinux()
  {
    return IS_LINUX;
  }

  /**
   * OS determination method
   * 
   * @return true if likely to be running on a BSD box
   */
  public static boolean isBsd()
  {
    return IS_BSD;
  }

  /**
   * Returns a simple version of the OS name
   */
  public static String getSimpleName()
  {
    if (isBsd())
      return "BSD";
    if (isLinux())
      return "Linux";
    if (isOSX())
      return "OSX";
    if (isWindows())
      return "Windows";

    return "OtherOS";
  }

  /**
   * Returns the os.name property
   */
  public static String getName()
  {
    return System.getProperty(OS_NAME_PROPERTY);
  }
}
