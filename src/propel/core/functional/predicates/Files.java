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
import propel.core.functional.Predicates.Predicate1;

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
  public static Predicate1<File> canExecute()
  {
    return CAN_EXECUTE;
  }
  private static final Predicate1<File> CAN_EXECUTE = new Predicate1<File>() {
    @Override
    public boolean evaluate(final File file)
    {
      return file.canExecute();
    }
  };

  /**
   * Returns true if the file can be read
   * 
   * @throws NullPointerException An argument is null
   */
  public static Predicate1<File> canRead()
  {
    return CAN_READ;
  }
  private static final Predicate1<File> CAN_READ = new Predicate1<File>() {
    @Override
    public boolean evaluate(final File file)
    {
      return file.canRead();
    }
  };

  /**
   * Returns true if the file can be written to
   * 
   * @throws NullPointerException An argument is null
   */
  public static Predicate1<File> canWrite()
  {
    return CAN_WRITE;
  }
  private static final Predicate1<File> CAN_WRITE = new Predicate1<File>() {
    @Override
    public boolean evaluate(final File file)
    {
      return file.canWrite();
    }
  };

  /**
   * Returns true if the file exists
   * 
   * @throws NullPointerException An argument is null
   */
  public static Predicate1<File> exists()
  {
    return EXISTS;
  }
  private static final Predicate1<File> EXISTS = new Predicate1<File>() {
    @Override
    public boolean evaluate(final File file)
    {
      return file.exists();
    }
  };

  /**
   * Returns true if the file is a directory
   * 
   * @throws NullPointerException An argument is null
   */
  public static Predicate1<File> isDirectory()
  {
    return IS_DIRECTORY;
  }
  private static final Predicate1<File> IS_DIRECTORY = new Predicate1<File>() {
    @Override
    public boolean evaluate(final File file)
    {
      return file.isDirectory();
    }
  };

  /**
   * Returns true if the file is not a directory
   * 
   * @throws NullPointerException An argument is null
   */
  public static Predicate1<File> isFile()
  {
    return IS_FILE;
  }
  private static final Predicate1<File> IS_FILE = new Predicate1<File>() {
    @Override
    public boolean evaluate(final File file)
    {
      return file.isFile();
    }
  };

  /**
   * Returns true if the file is a hidden file
   * 
   * @throws NullPointerException An argument is null
   */
  public static Predicate1<File> isHidden()
  {
    return IS_HIDDEN;
  }
  private static final Predicate1<File> IS_HIDDEN = new Predicate1<File>() {
    @Override
    public boolean evaluate(final File file)
    {
      return file.isHidden();
    }
  };

  private Files()
  {
  }
}
