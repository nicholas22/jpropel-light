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
import java.io.FileNotFoundException;
import java.io.IOException;
import lombok.SneakyThrows;
import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.functional.Actions.Action1;
import propel.core.functional.Functions.Function1;
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
  @Validate
  public static Action1<File> appendToFile(@NotNull final String _text)
  {
    return new Action1<File>() {
      @Override
      @SneakyThrows
      public void apply(final File element)
      {
        FileUtils.appendText(element, _text);
      }
    };
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
  @Validate
  public static Action1<File> appendLineToFile(@NotNull final String _text)
  {
    return new Action1<File>() {
      @Override
      @SneakyThrows
      public void apply(final File element)
      {
        FileUtils.appendTextLine(element, _text);
      }
    };
  }

  /**
   * Action that copies a file from an absolute file path to another
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, it does not exist or cannot be read/created.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static Action1<File> copyFile(@NotNull final String _destFilePath)
  {
    return new Action1<File>() {
      @Override
      @SneakyThrows
      public void apply(final File element)
      {
        FileUtils.copyFile(element, _destFilePath);
      }
    };
  }

  /**
   * Action that deletes a file from an absolute file path.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static Action1<File> deleteFile()
  {
    return DELETE_FILE;
  }
  private static final Action1<File> DELETE_FILE = new Action1<File>() {
    @Override
    @SneakyThrows
    public void apply(final File element)
    {
      FileUtils.deleteFile(element);
    }
  };

  /**
   * Action that deletes a directory from an absolute file path.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was not a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static Action1<File> deleteDirectory()
  {
    return DELETE_DIRECTORY;
  }
  private static final Action1<File> DELETE_DIRECTORY = new Action1<File>() {
    @Override
    @SneakyThrows
    public void apply(final File element)
    {
      FileUtils.deleteDirectory(element);
    }
  };

  /**
   * Action that deletes a directory tree from an absolute file path.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was not a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static Action1<File> deleteDirectoryRecursive()
  {
    return DELETE_DIRECTORY_RECURSIVE;
  }
  private static final Action1<File> DELETE_DIRECTORY_RECURSIVE = new Action1<File>() {
    @Override
    @SneakyThrows
    public void apply(final File element)
    {
      FileUtils.deleteDirectoryRecursive(element);
    }
  };

  /**
   * Returns a File for a given path
   * 
   * @throws NullPointerException An argument is null
   * @throws SecurityException If a required system property value cannot be accessed
   */
  public static Function1<String, File> getFile()
  {
    return GET_FILE;
  }
  private static final Function1<String, File> GET_FILE = new Function1<String, File>() {
    @Override
    public File apply(final String element)
    {
      return new File(element);
    }
  };

  /**
   * Returns the absolute path of the file
   * 
   * @throws NullPointerException An argument is null
   * @throws SecurityException If a required system property value cannot be accessed
   */
  public static Function1<File, String> getAbsolutePath()
  {
    return GET_ABSOLUTE_PATH;
  }
  private static final Function1<File, String> GET_ABSOLUTE_PATH = new Function1<File, String>() {
    @Override
    public String apply(final File element)
    {
      return element.getAbsolutePath();
    }
  };

  /**
   * Returns the extension of the file
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Function1<File, String> getExtension(@NotNull final File file)
  {
    return GET_EXTENSION;
  }
  private static final Function1<File, String> GET_EXTENSION = new Function1<File, String>() {
    @Override
    public String apply(final File element)
    {
      return FileUtils.getFileExtension(element);
    }
  };

  /**
   * Returns the name of the file
   * 
   * @throws NullPointerException An argument is null
   */
  public static Function1<File, String> getName()
  {
    return GET_NAME;
  }
  private static final Function1<File, String> GET_NAME = new Function1<File, String>() {
    @Override
    public String apply(final File element)
    {
      return element.getName();
    }
  };

  /**
   * Returns the parent of the file
   * 
   * @throws NullPointerException An argument is null
   * @throws SecurityException If a required system property value cannot be accessed
   */
  public static Function1<File, File> getParent()
  {
    return GET_PARENT_FILE;
  }
  private static final Function1<File, File> GET_PARENT_FILE = new Function1<File, File>() {
    @Override
    public File apply(final File element)
    {
      return element.getParentFile();
    }
  };

  /**
   * Action that moves a file from an absolute file path to another
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, it does not exist or cannot be read/created.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static Action1<File> moveFile(@NotNull final String _destFilePath)
  {
    return new Action1<File>() {
      @Override
      @SneakyThrows
      public void apply(final File element)
      {
        FileUtils.moveFile(element, _destFilePath);
      }
    };
  }

  /**
   * Action that moves a file from one absolute file path to another. No exception is thrown under any circumstance.
   */
  @Validate
  public static Action1<File> tryMoveFile(@NotNull final String _destFilePath)
  {
    return new Action1<File>() {
      @Override
      @SneakyThrows
      public void apply(final File element)
      {
        FileUtils.tryMoveFile(element, _destFilePath);
      }
    };
  }

  /**
   * Action that copies a file from an absolute file path to another. No exception is thrown under any circumstance.
   */
  @Validate
  public static Action1<File> tryCopyFile(@NotNull final String _destFilePath)
  {
    return new Action1<File>() {
      @Override
      public void apply(final File element)
      {
        FileUtils.tryCopyFile(element, _destFilePath);
      }
    };
  }

  /**
   * Action that deletes a file from an absolute file path. No exception is thrown under any circumstance.
   */
  public static Action1<File> tryDeleteFile()
  {
    return TRY_DELETE_FILE;
  }
  private static final Action1<File> TRY_DELETE_FILE = new Action1<File>() {
    @Override
    @SneakyThrows
    public void apply(final File element)
    {
      FileUtils.tryDeleteFile(element);
    }
  };

  /**
   * Action that deletes a directory from an absolute file path. No exception is thrown under any circumstance.
   */
  public static Action1<File> tryDeleteDirectory()
  {
    return TRY_DELETE_DIRECTORY;
  }
  private static final Action1<File> TRY_DELETE_DIRECTORY = new Action1<File>() {
    @Override
    @SneakyThrows
    public void apply(final File element)
    {
      FileUtils.tryDeleteDirectory(element);
    }
  };

  /**
   * Action that deletes a directory tree from an absolute file path. No exception is thrown under any circumstance.
   */
  public static Action1<File> tryDeleteDirectoryRecursive()
  {
    return TRY_DELETE_DIRECTORY_RECURSIVE;
  }
  private static final Action1<File> TRY_DELETE_DIRECTORY_RECURSIVE = new Action1<File>() {
    @Override
    @SneakyThrows
    public void apply(final File element)
    {
      FileUtils.tryDeleteDirectory(element);
    }
  };

  private Files()
  {
  }
}
