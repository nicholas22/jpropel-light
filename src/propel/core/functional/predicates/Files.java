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
package propel.core.functional.predicates;

import java.io.File;
import lombok.Predicate;

/**
 * Some common, re-usable predicates for Files
 */
public final class Files
{

  /**
   * Returns true if the file can be executed
   * 
   * @throws NullPointerException An argument is null
   */
  @Predicate
  public static boolean canExecute(final File file)
  {
    return file.canExecute();
  }

  /**
   * Returns true if the file can be read
   * 
   * @throws NullPointerException An argument is null
   */
  @Predicate
  public static boolean canRead(final File file)
  {
    return file.canRead();
  }

  /**
   * Returns true if the file can be written to
   * 
   * @throws NullPointerException An argument is null
   */
  @Predicate
  public static boolean canWrite(final File file)
  {
    return file.canWrite();
  }

  /**
   * Returns true if the file exists
   * 
   * @throws NullPointerException An argument is null
   */
  @Predicate
  public static boolean exists(final File file)
  {
    return file.exists();
  }

  /**
   * Returns true if the file is a directory
   * 
   * @throws NullPointerException An argument is null
   */
  @Predicate
  public static boolean isDirectory(final File file)
  {
    return file.isDirectory();
  }

  /**
   * Returns true if the file is not a directory
   * 
   * @throws NullPointerException An argument is null
   */
  @Predicate
  public static boolean isFile(final File file)
  {
    return file.isFile();
  }

  /**
   * Returns true if the file is a hidden file
   * 
   * @throws NullPointerException An argument is null
   */
  @Predicate
  public static boolean isHidden(final File file)
  {
    return file.isHidden();
  }

  private Files()
  {
  }
}
