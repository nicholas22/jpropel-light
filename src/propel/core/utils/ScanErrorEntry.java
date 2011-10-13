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
 * POCO used for collection of errors during scanning
 */
public class ScanErrorEntry
{
  /**
   * The path that caused the error.
   */
  private String path;
  /**
   * The exception that occurred. The exception's stack trace has been already preserved.
   */
  private Throwable error;

  /**
   * Default constructor
   */
  public ScanErrorEntry()
  {
  }

  /**
   * Conversion constructor
   */
  public ScanErrorEntry(String path, Throwable error)
  {
    this.path = path;
    this.error = error;
  }

  public String getPath()
  {
    return path;
  }

  public void setPath(String path)
  {
    this.path = path;
  }

  public Throwable getError()
  {
    return error;
  }

  public void setError(Throwable error)
  {
    this.error = error;
  }
}
