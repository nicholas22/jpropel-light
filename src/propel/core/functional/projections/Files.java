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
package propel.core.functional.projections;

import java.io.File;
import lombok.Action;
import lombok.Function;
import lombok.SneakyThrows;
import propel.core.utils.FileUtils;

/**
 * Some common, re-usable projections for file manipulation
 */
public final class Files
{
  /**
   * Action that appends some text data to a file. No EOL character is appended, just the given text.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException An argument is out of range
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, or it does not exist and cannot be created, or cannot be appended to.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Action
  @SneakyThrows
  public static void appendToFile(final File _file, final String _text)
  {
    FileUtils.appendText(_file, _text);
  }

  /**
   * Action that appends some text data to a file. The current operating system EOL character is appended at the end of the given text.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException An argument is out of range
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, or it does not exist and cannot be created, or cannot be appended to.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Action
  @SneakyThrows
  public static void appendLineToFile(final File _file, final String _text)
  {
    FileUtils.appendTextLine(_file, _text);
  }

  /**
   * Action that copies a file from an absolute file path to another
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, it does not exist or cannot be read/created.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Action
  @SneakyThrows
  public static void copyFile(final File _srcFile, final String _destFilePath)
  {
    FileUtils.copyFile(_srcFile, _destFilePath);
  }

  /**
   * Action that deletes a file from an absolute file path.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Action
  @SneakyThrows
  public static void deleteFile(final File _file)
  {
    FileUtils.deleteFile(_file);
  }

  /**
   * Action that deletes a directory from an absolute file path.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was not a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Action
  @SneakyThrows
  public static void deleteDirectory(final File _dir)
  {
    FileUtils.deleteDirectory(_dir);
  }

  /**
   * Action that deletes a directory tree from an absolute file path.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was not a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Action
  @SneakyThrows
  public static void deleteDirectoryRecursive(final File _dir)
  {
    FileUtils.deleteDirectoryRecursive(_dir);
  }

  /**
   * Returns the absolute path of the file
   * 
   * @throws NullPointerException An argument is null
   * @throws SecurityException If a required system property value cannot be accessed
   */
  @Function
  public static String getAbsolutePath(final File file)
  {
    return file.getAbsolutePath();
  }

  /**
   * Returns the extension of the file
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String getExtension(final File file)
  {
    return FileUtils.getFileExtension(file);
  }

  /**
   * Returns the name of the file
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String getName(final File file)
  {
    return file.getName();
  }

  /**
   * Returns the parent of the file
   * 
   * @throws NullPointerException An argument is null
   * @throws SecurityException If a required system property value cannot be accessed
   */
  @Function
  public static File getParent(final File file)
  {
    return file.getParentFile();
  }

  /**
   * Action that moves a file from an absolute file path to another
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, it does not exist or cannot be read/created.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Action
  @SneakyThrows
  public static void moveFile(final File _srcFile, final String _destFilePath)
  {
    FileUtils.moveFile(_srcFile, _destFilePath);
  }

  /**
   * Action that copies a file from an absolute file path to another. No exception is thrown under any circumstance.
   */
  @Action
  public static void tryCopyFile(final File _srcFile, final String _destFilePath)
  {
    FileUtils.tryCopyFile(_srcFile, _destFilePath);
  }

  /**
   * Action that deletes a file from an absolute file path. No exception is thrown under any circumstance.
   */
  @Action
  public static void tryDeleteFile(final File _file)
  {
    FileUtils.tryDeleteFile(_file);
  }

  /**
   * Action that deletes a directory from an absolute file path. No exception is thrown under any circumstance.
   */
  @Action
  public static void tryDeleteDirectory(final File _dir)
  {
    FileUtils.tryDeleteDirectory(_dir);
  }

  /**
   * Action that deletes a directory tree from an absolute file path. No exception is thrown under any circumstance.
   */
  @Action
  public static void tryDeleteDirectoryRecursive(final File _dir)
  {
    FileUtils.tryDeleteDirectoryRecursive(_dir);
  }

  /**
   * Action that moves a file from one absolute file path to another. No exception is thrown under any circumstance.
   */
  @Action
  public static void tryMoveFile(final File _srcFile, final String _destFilePath)
  {
    FileUtils.tryMoveFile(_srcFile, _destFilePath);
  }

  private Files()
  {
  }
}
