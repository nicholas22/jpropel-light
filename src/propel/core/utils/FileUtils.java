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

import static propel.core.functional.projections.Files.getAbsolutePath;
import static lombok.Yield.yield;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.SneakyThrows;
import lombok.Validate;
import lombok.Validate.NotNull;
import lombok.val;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.lists.ReifiedList;
import propel.core.common.CONSTANT;
import propel.core.functional.tuples.Pair;

/**
 * Provides utility functionality for File/Directory related structures
 */
public final class FileUtils
{
  /**
   * Appends some binary data to a file. If the file does not exist, it is created.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException An argument is out of range
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, or it does not exist and cannot be created, or cannot be appended to.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static void appendData(File file, byte[] data, int offset, int count)
      throws IOException
  {
    appendData(file.getAbsolutePath(), data, offset, count);
  }

  /**
   * Appends some binary data to a file in the specified path. If the file does not exist, it is created.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException An argument is out of range
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, or it does not exist and cannot be created, or cannot be appended to.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static void appendData(@NotNull final String fileAbsPath, @NotNull final byte[] data, int offset, int count)
      throws IOException
  {
    if (offset > data.length)
      throw new IllegalArgumentException("offset=" + offset + " dataLen=" + data.length);
    if (count < 0 || count > data.length)
      throw new IllegalArgumentException("count=" + count + " dataLen=" + data.length);
    if (offset + count > data.length || offset + count < 0)
      throw new IllegalArgumentException("offset=" + offset + " count=" + count + " dataLen=" + data.length);

    val bos = new BufferedOutputStream(new FileOutputStream(fileAbsPath, true));
    bos.write(data, offset, count);
    bos.flush();
    bos.close();
  }

  /**
   * Appends some text to a file. If the file does not exist, it is created. No EOL terminator is appended, e.g. Environment.NewLine.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException An argument is out of range
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, or it does not exist and cannot be created, or cannot be appended to.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static void appendText(File file, String text)
      throws IOException
  {
    appendText(file.getAbsolutePath(), text);
  }

  /**
   * Appends some text to a file in the specified path. If the file does not exist, it is created. No EOL terminator is appended, e.g.
   * Environment.NewLine.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException An argument is out of range
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, or it does not exist and cannot be created, or cannot be appended to.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static void appendText(String fileAbsPath, String text)
      throws IOException
  {
    appendText(fileAbsPath, text, null);
  }

  /**
   * Appends some text to a file and then appends Environment.NewLine. If the file does not exist, it is created.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException An argument is out of range
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, or it does not exist and cannot be created, or cannot be appended to.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static void appendTextLine(File file, String text)
      throws IOException
  {
    appendTextLine(file.getAbsolutePath(), text);
  }

  /**
   * Appends some text to a file in the specified path and then appends Environment.NewLine. If the file does not exist, it is created.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException An argument is out of range
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, or it does not exist and cannot be created, or cannot be appended to.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static void appendTextLine(String fileAbsPath, String text)
      throws IOException
  {
    appendText(fileAbsPath, text, CONSTANT.ENVIRONMENT_NEWLINE);
  }

  /**
   * Appends some text to a file in the specified path. If the file does not exist, it is created. If the End-Of-Line terminator is not
   * null, it is appended after the text (this could be e.g. Environment.NewLine)
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException An argument is out of range
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, or it does not exist and cannot be created, or cannot be appended to.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static void appendText(File file, String text, String eolTerminator)
      throws IOException
  {
    appendText(file.getAbsolutePath(), text, eolTerminator);
  }

  /**
   * Appends some text to a file in the specified path. If the file does not exist, it is created. If the End-Of-Line terminator is not
   * null, it is appended after the text (this could be e.g. Environment.NewLine)
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException An argument is out of range
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, or it does not exist and cannot be created, or cannot be appended to.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static void appendText(@NotNull final String fileAbsPath, String text, String eolTerminator)
      throws IOException
  {
    val bw = new BufferedWriter(new FileWriter(fileAbsPath, true));
    bw.write(text);
    if (eolTerminator != null)
      bw.write(eolTerminator);
    bw.flush();
    bw.close();
  }

  /**
   * Copies a file from source to destination while preserving the file's Last Modified date. Note: Creation date is not possible to obtain
   * and set in a cross platform way in Java prior to 1.7.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, it does not exist or cannot be read/created.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static void cloneFile(File originatingFile, String destinationPath)
      throws IOException
  {
    cloneFile(originatingFile.getAbsolutePath(), destinationPath);
  }

  /**
   * Copies a file from source to destination while preserving the file's Last Modified date. Note: Creation date is not possible to obtain
   * and set in a cross platform way in Java prior to 1.7.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, it does not exist or cannot be read/created.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static void cloneFile(String originatingPath, String destinationPath)
      throws IOException
  {
    copyFile(originatingPath, destinationPath);

    val source = new File(originatingPath);
    val dest = new File(destinationPath);

    if (!dest.setLastModified(source.lastModified()))
      throw new IOException("Could not set last modified date/time.");
  }

  /**
   * Combines two paths, appending a directory separator after the first path, if needed.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static String combinePath(@NotNull final String path, @NotNull final String nextPath)
  {
    return path.endsWith(CONSTANT.DIRECTORY_SEPARATOR) ? path + nextPath : path + CONSTANT.DIRECTORY_SEPARATOR + nextPath;
  }

  /**
   * Clones a file from source to destination, returning true if the operation was successful. The file is not copied if any exception
   * occurs. No exception is thrown under any circumstance.
   */
  public static boolean tryCloneFile(File sourceFile, String destAbsPath)
  {
    return tryCloneFile(sourceFile.getAbsolutePath(), destAbsPath);
  }

  /**
   * Clones a file from source to destination, returning true if the operation was successful. The file is not copied if any exception
   * occurs. No exception is thrown under any circumstance.
   */
  public static boolean tryCloneFile(String sourceAbsPath, String destAbsPath)
  {
    try
    {
      cloneFile(sourceAbsPath, destAbsPath);
      return true;
    }
    catch(Throwable e)
    {
      return false;
    }
  }

  /**
   * Copies a file from source to destination
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, it does not exist or cannot be read/created.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static void copyFile(File originatingFile, String destinationPath)
      throws IOException
  {
    copyFile(originatingFile.getAbsolutePath(), destinationPath);
  }

  /**
   * Copies a file from source to destination
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, it does not exist or cannot be read/created.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static void copyFile(@NotNull final String originatingPath, @NotNull final String destinationPath)
      throws IOException
  {
    val source = new File(originatingPath);
    val dest = new File(destinationPath);

    BufferedInputStream fis = null;
    BufferedOutputStream fos = null;

    try
    {
      fis = new BufferedInputStream(new FileInputStream(source));
      fos = new BufferedOutputStream(new FileOutputStream(dest));

      byte[] buf = new byte[64 * 1024];
      int read = 0;
      while ((read = fis.read(buf)) != -1)
        fos.write(buf, 0, read);
    }
    finally
    {
      if (fis != null)
        fis.close();
      if (fos != null)
        fos.close();
    }
  }

  /**
   * Copies a file from source to destination, returning true if the operation was successful. The file is not copied if any exception
   * occurs. No exception is thrown under any circumstance.
   */
  public static boolean tryCopyFile(File sourceFile, String destAbsPath)
  {
    return tryCopyFile(sourceFile, destAbsPath);
  }

  /**
   * Copies a file from source to destination, returning true if the operation was successful. The file is not copied if any exception
   * occurs. No exception is thrown under any circumstance.
   */
  public static boolean tryCopyFile(String sourceAbsPath, String destAbsPath)
  {
    try
    {
      copyFile(sourceAbsPath, destAbsPath);
      return true;
    }
    catch(Throwable e)
    {
      return false;
    }
  }

  /**
   * Deletes a file, if it exists.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static void deleteFile(File file)
      throws IOException
  {
    deleteFile(file.getAbsolutePath());
  }

  /**
   * Deletes a file, if it exists.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static void deleteFile(@NotNull final String fileAbsPath)
      throws IOException
  {
    val file = new File(fileAbsPath);
    if (file.exists())
    {
      // check not dir
      if (!file.isFile())
        throw new FileNotFoundException("The specified path is not referring to a file: " + fileAbsPath);
      // delete
      if (!file.delete())
        throw new IOException("Could not delete file: " + fileAbsPath);
    }
  }

  /**
   * Attempts to delete a file. The file is not deleted if any exception occurs. No exception is thrown under any circumstance.
   */
  public static boolean tryDeleteFile(File file)
  {
    return tryDeleteFile(file.getAbsolutePath());
  }

  /**
   * Attempts to delete a file. The file is not deleted if any exception occurs. No exception is thrown under any circumstance.
   */
  public static boolean tryDeleteFile(String fileAbsPath)
  {
    try
    {
      if (fileAbsPath == null)
        throw new NullPointerException("fileAbsPath");

      File file = new File(fileAbsPath);
      if (file.exists())
        return file.delete();

      return true;
    }
    catch(Throwable e)
    {
      return false;
    }
  }

  /**
   * Returns true if the file exists
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean exists(@NotNull final File file)
  {
    return file.exists();
  }

  /**
   * Returns true if the file exists
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean exists(@NotNull final String file)
  {
    return new File(file).exists();
  }

  /**
   * Returns the specified file's extension, or an empty string if no extension was found
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static String getFileExtension(@NotNull final File file)
  {
    return getFileExtension(file.getAbsolutePath());
  }

  /**
   * Returns the specified file's extension, or an empty string if no extension was found
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static String getFileExtension(@NotNull final String filename)
  {
    val index = StringUtils.lastIndexOf(filename, CONSTANT.DOT, StringComparison.Ordinal);

    if (index < 0)
      return CONSTANT.EMPTY_STRING;

    return filename.substring(index + 1);
  }

  /**
   * Trims the file's extension (if there is one) and returns the file name
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static String getFileName(@NotNull final File file)
  {
    return getFileName(file.getName());
  }

  /**
   * Trims the file's extension (if there is one) and returns the file name
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static String getFileName(@NotNull final String filename)
  {
    val extension = getFileExtension(filename);
    if (extension.length() <= 0)
      return filename;

    return filename.substring(0, filename.length() - extension.length() - 1);
  }

  /**
   * Returns the URL to a resource, by its class path e.g. /java/util/test.xml.
   * 
   * @throws NullPointerException An argument is null
   * @throws FileNotFoundException The resource specified was not found
   */
  @Validate
  public static URL getResourceUrl(@NotNull final String classpath)
      throws FileNotFoundException
  {
    val result = FileUtils.class.getResource(classpath);

    if (result == null)
      throw new FileNotFoundException("Resource not found in specified class path: " + classpath);

    return result;
  }

  /**
   * Returns the URL to a resource, by its package and name e.g. class path e.g. /java/util and test.xml
   * 
   * @throws NullPointerException An argument is null
   * @throws FileNotFoundException The resource specified was not found
   */
  @Validate
  public static URL getResourceUrl(@NotNull final Package pkg, @NotNull final String filename)
      throws FileNotFoundException
  {
    // get the class's package
    val myPackage = pkg.getName();

    // convert package to class path
    String myClasspath = CONSTANT.FORWARD_SLASH + myPackage.replace(CONSTANT.DOT, CONSTANT.FORWARD_SLASH);
    if (!myClasspath.endsWith(CONSTANT.FORWARD_SLASH))
      myClasspath += CONSTANT.FORWARD_SLASH;

    // append filename to class path
    val resourceClasspath = myClasspath + filename;

    // load resource
    return FileUtils.getResourceUrl(resourceClasspath);
  }

  /**
   * Returns a tuple. The first argument is an open input stream to the specified resource, which has to be closed explicitly. The second
   * argument is the length of the data in the input stream.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O exception occurred during loading of the resource
   * @throws FileNotFoundException The resource specified was not found
   */
  @Validate
  private static Pair<InputStream, Long> getResourceStream(@NotNull final URL url)
      throws FileNotFoundException, IOException
  {
    // Thanks to Marcin Lamparski for contribution
    val connection = url.openConnection();
    int length = connection.getContentLength();

    val is = connection.getInputStream();
    return new Pair<InputStream, Long>(is, (long) length);
  }

  /**
   * Returns an input stream to the specified resource. Do not forget to close the stream when done.
   * 
   * @throws IOException An I/O exception occurred during loading of the resource
   */
  @Validate
  public static InputStream getResourceStreamData(@NotNull final URL url)
      throws IOException
  {
    return url.openStream();
  }

  /**
   * Returns the text data of the specified URL. The conversion uses the UTF8 character set
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O exception occurred during loading of the resource
   * @throws FileNotFoundException The resource specified was not found
   */
  public static String getResourceTextData(URL url)
      throws IOException
  {
    return new String(getResourceData(url), CONSTANT.UTF8);
  }

  /**
   * Returns the text data of the specified URL. The conversion uses the specified character set, e.g. UTF8
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O exception occurred during loading of the resource
   * @throws FileNotFoundException The resource specified was not found
   */
  @Validate
  public static String getResourceTextData(URL url, @NotNull final Charset charset)
      throws IOException
  {
    return new String(getResourceData(url), CONSTANT.UTF8);
  }

  /**
   * Returns the data of the specified URL.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O exception occurred during loading of the resource
   * @throws FileNotFoundException The resource specified was not found
   */
  @Validate
  public static byte[] getResourceData(@NotNull final URL url)
      throws IOException
  {
    Pair<InputStream, Long> tuple = getResourceStream(url);

    val input = tuple.getFirst();
    val length = tuple.getSecond();

    byte[] result = null;
    try
    {
      result = StreamUtils.readFully(input, length);
    }
    finally
    {
      // important not to forget to close the opened stream
      input.close();
    }

    return result;
  }

  /**
   * Returns true if the File is a directory
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean isDirectory(@NotNull final File file)
  {
    return file.isDirectory();
  }

  /**
   * Returns true if the File is a directory
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean isDirectory(@NotNull final String file)
  {
    return new File(file).isDirectory();
  }

  /**
   * Returns true if the File is not a directory
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean isFile(@NotNull final File file)
  {
    return file.isFile();
  }

  /**
   * Returns true if the File is not a directory
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static boolean isFile(@NotNull final String file)
  {
    return new File(file).isFile();
  }

  /**
   * Moves a file from source to destination
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, it does not exist or cannot be read/created.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static boolean moveFile(File originatingFile, String destinationPath)
      throws IOException
  {
    return moveFile(originatingFile.getAbsolutePath(), destinationPath);
  }

  /**
   * Moves a file from source to destination
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file specified is a directory, it does not exist or cannot be read/created.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static boolean moveFile(@NotNull final String originatingPath, @NotNull final String destinationPath)
      throws IOException
  {
    val source = new File(originatingPath);
    val dest = new File(destinationPath);
    return source.renameTo(dest);
  }

  /**
   * Moves a file from source to destination, returning true if the operation was successful. The file is not moved if any exception occurs.
   * No exception is thrown under any circumstance.
   */
  public static boolean tryMoveFile(File sourceFile, String destAbsPath)
  {
    return tryMoveFile(sourceFile.getAbsolutePath(), destAbsPath);
  }

  /**
   * Moves a file from source to destination, returning true if the operation was successful. The file is not moved if any exception occurs.
   * No exception is thrown under any circumstance.
   */
  public static boolean tryMoveFile(String sourceAbsPath, String destAbsPath)
  {
    try
    {
      return moveFile(sourceAbsPath, destAbsPath);
    }
    catch(Throwable e)
    {
      return false;
    }
  }

  /**
   * Reads the entire contents of a file to a string and returns it. This method should generally only be used for small files as it is
   * quite inefficient to read everything in memory.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was not found, or it was a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static String readFileToEnd(@NotNull final File file)
      throws IOException
  {
    return readFileToEnd(file.getAbsolutePath());
  }

  /**
   * Reads the entire contents of a file to a string and returns it. This method should generally only be used for small files as it is
   * quite inefficient to read everything in memory.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was not found, or it was a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static String readFileToEnd(@NotNull final String fileAbsPath)
      throws IOException
  {
    val file = new File(fileAbsPath);

    if (!file.exists())
      throw new FileNotFoundException("The file was not found: " + fileAbsPath);
    if (!file.isFile())
      throw new FileNotFoundException("The specified path is not referring to a file: " + fileAbsPath);

    BufferedInputStream stream = null;
    try
    {
      stream = new BufferedInputStream(new FileInputStream(file));
      val result = StreamUtils.readFully(stream, file.length());
      return ConversionUtils.toString(result);
    }
    finally
    {
      if (stream != null)
        stream.close();
    }
  }

  /**
   * Enumerates the contents of a file line by line (reads the entire contents of a file in memory to do so).
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was not found, or it was a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static Iterable<String> readFilePerLine(@NotNull final File file)
      throws IOException
  {
    return readFilePerLine(file.getAbsolutePath());
  }

  /**
   * Enumerates the contents of a file line by line. It does not read the entire contents in memory.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was not found, or it was a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static Iterable<String> readFilePerLine(@NotNull final String fileAbsPath)
      throws IOException
  {
    File file = openFile(fileAbsPath);

    BufferedReader br = openBufferedReader(file);
    String line;
    while ((line = readLine(br)) != null)
      yield(line);

    closeBufferedReader(br);
  }

  @SneakyThrows
  private static File openFile(final String fileAbsPath)
  {
    val file = new File(fileAbsPath);
    if (!file.exists())
      throw new FileNotFoundException("The file was not found: " + fileAbsPath);
    if (!file.isFile())
      throw new FileNotFoundException("The specified path is not referring to a file: " + fileAbsPath);
    return file;
  }

  @SneakyThrows
  private static BufferedReader openBufferedReader(final File file)
  {
    return new BufferedReader(new FileReader(file));
  }

  @SneakyThrows
  private static String readLine(final BufferedReader br)
  {
    return br.readLine();
  }

  @SneakyThrows
  private static void closeBufferedReader(final BufferedReader br)
  {
    br.close();
  }

  /**
   * Reads the entire contents of a file to a byte[] and returns it. This method should generally only be used for small files as it is
   * quite inefficient to read everything in memory.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was not found, or it was a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static byte[] readFileInMemory(@NotNull final File file)
      throws IOException
  {
    return readFileInMemory(file.getAbsolutePath());
  }

  /**
   * Reads the entire contents of a file to a byte[] and returns it. This method should generally only be used for small files as it is
   * quite inefficient to read everything in memory.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was not found, or it was a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static byte[] readFileInMemory(@NotNull final String fileAbsPath)
      throws IOException
  {
    val file = new File(fileAbsPath);
    if (!file.exists())
      throw new FileNotFoundException("The file was not found: " + fileAbsPath);
    if (!file.isFile())
      throw new FileNotFoundException("The specified path is not referring to a file: " + fileAbsPath);

    val bis = new BufferedInputStream(new FileInputStream(file));
    val result = StreamUtils.readFully(bis, file.length());
    bis.close();
    return result;
  }

  /**
   * Enumerates the contents of a file line by line (reads the entire contents of a file in memory to do so).
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was not found, or it was a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static Iterable<byte[]> readFilePerBlock(@NotNull final File file, final int blockSize)
      throws IOException
  {
    return readFilePerBlock(file.getAbsolutePath(), blockSize);
  }

  /**
   * Enumerates the contents of a file line by line (reads the entire contents of a file in memory to do so).
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException The file was not found, or it was a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static Iterable<byte[]> readFilePerBlock(@NotNull final String fileAbsPath, final int blockSize)
      throws IOException
  {
    if (blockSize <= 0)
      throw new IllegalArgumentException("blockSize");
    List<byte[]> result = new ArrayList<byte[]>(100);

    File file = new File(fileAbsPath);
    if (!file.exists())
      throw new FileNotFoundException("The file was not found: " + fileAbsPath);
    if (!file.isFile())
      throw new FileNotFoundException("The specified path is not referring to a file: " + fileAbsPath);

    if (file.length() > Integer.MAX_VALUE)
      throw new IOException("The file is too large, this method supports files of up to 2GB.");

    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

    // read all blocks
    int repeats = (int) (file.length() / blockSize);
    int remainder = (int) (file.length() % blockSize);
    for (int i = 0; i < repeats; i++)
      result.add(StreamUtils.readFully(bis, blockSize));

    // read remainder
    if (remainder > 0)
      result.add(StreamUtils.readFully(bis, remainder));

    bis.close();
    return result;
  }

  /**
   * Touches a file, i.e. creates it if not there, otherwise updates its last write time.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException A directory was specified instead of a file
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static void touchFile(@NotNull final File file)
      throws IOException
  {
    touchFile(file.getAbsolutePath());
  }

  /**
   * Touches a file, i.e. creates it if not there, otherwise updates its last write time.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException A directory was specified instead of a file
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static void touchFile(@NotNull final String fileAbsPath)
      throws IOException
  {
    val file = new File(fileAbsPath);
    if (file.exists())
    {
      if (!file.isFile())
        throw new FileNotFoundException("The specified path is not referring to a file: " + fileAbsPath);

      // set to current date/time (millis since 1/1/1970)
      file.setLastModified(System.currentTimeMillis());
    } else if (!file.createNewFile())
      throw new IOException("Could not touch file, because creation of new file failed.");
  }

  /**
   * Touches a file, i.e. creates it if not there, otherwise updates its last write time. Returns true if succeeded. No exception is thrown
   * under any circumstance.
   */
  public static boolean tryTouchFile(File file)
  {
    return tryTouchFile(file.getAbsolutePath());
  }

  /**
   * Touches a file, i.e. creates it if not there, otherwise updates its last write time. Returns true if succeeded. No exception is thrown
   * under any circumstance.
   */
  public static boolean tryTouchFile(String fileAbsPath)
  {
    try
    {
      touchFile(fileAbsPath);
      return true;
    }
    catch(Throwable e)
    {
      return false;
    }
  }

  // Directory related methods
  /**
   * Creates a directory if it does not already exist (if there are subdirectories there are created too).
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs, or directory creation fails: please note that a partial path may have been created during the
   *           attempt.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static void createDirectory(@NotNull final File dirAbsPath)
      throws IOException
  {
    if (!dirAbsPath.exists())
      if (!dirAbsPath.mkdirs())
        throw new IOException("Directory creation failure: " + dirAbsPath.getAbsolutePath()); // sub-directories may have been created!
  }

  /**
   * Creates a directory if it does not already exist (if there are subdirectories there are created too).
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs, or directory creation fails: please note that a partial path may have been created during the
   *           attempt.
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static void createDirectory(final String dirAbsPath)
      throws IOException
  {
    val dir = new File(dirAbsPath);
    createDirectory(dir);
  }

  /**
   * Attempts to create a directory (if there are subdirectories there are created too). Returns true if succeeded or directory already
   * exists. No exception is thrown under any circumstance.
   */
  public static boolean tryCreateDirectory(File dirAbsPath)
  {
    try
    {
      createDirectory(dirAbsPath);
      return true;
    }
    catch(Throwable e)
    {
      return false;
    }
  }

  /**
   * Attempts to create a directory (if there are subdirectories there are created too). Returns true if succeeded or directory already
   * exists. No exception is thrown under any circumstance.
   */
  public static boolean tryCreateDirectory(String dirAbsPath)
  {
    try
    {
      createDirectory(dirAbsPath);
      return true;
    }
    catch(Throwable e)
    {
      return false;
    }
  }

  /**
   * Deletes an empty directory, if it exists. (if it has contents this will not succeed).
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs, or the directory could not be deleted
   * @throws FileNotFoundException The specified path is not a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static void deleteDirectory(@NotNull final File dirAbsPath)
      throws IOException
  {
    if (dirAbsPath.exists())
    {
      if (!dirAbsPath.isDirectory())
        throw new FileNotFoundException("The specified path is not referring to a directory: " + dirAbsPath.getAbsolutePath());

      if (!dirAbsPath.delete())
        throw new IOException("The directory could not be deleted, because it is not empty.");
    }
  }

  /**
   * Deletes an empty directory, if it exists. (if it has contents this will not succeed).
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs, or the directory could not be deleted
   * @throws FileNotFoundException The specified path is not a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static void deleteDirectory(final String dirAbsPath)
      throws IOException
  {
    val dir = new File(dirAbsPath);
    deleteDirectory(dir);
  }

  /**
   * Attempts to delete an empty directory (if there are contents this will not succeed). Returns true if succeeded. No exception is thrown
   * under any circumstance.
   */
  public static boolean tryDeleteDirectory(File dirAbsPath)
  {
    try
    {
      deleteDirectory(dirAbsPath);
      return true;
    }
    catch(Throwable e)
    {
      return false;
    }
  }

  /**
   * Attempts to delete an empty directory (if there are contents this will not succeed). Returns true if succeeded. No exception is thrown
   * under any circumstance.
   */
  public static boolean tryDeleteDirectory(String dirAbsPath)
  {
    try
    {
      deleteDirectory(dirAbsPath);
      return true;
    }
    catch(Throwable e)
    {
      return false;
    }
  }

  /**
   * Deletes a directory recursively (if there are contents they will be deleted).
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs, or the directory could not be deleted
   * @throws FileNotFoundException The specified path is not a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static void deleteDirectoryRecursive(@NotNull final File dirAbsPath)
      throws IOException
  {
    if (dirAbsPath.exists())
    {
      if (!dirAbsPath.isDirectory())
        throw new FileNotFoundException("The specified path is not referring to a directory: " + dirAbsPath.getAbsolutePath());

      val files = dirAbsPath.listFiles();
      for (int i = 0; i < files.length; i++)
        if (files[i].isDirectory())
          deleteDirectoryRecursive(files[i]);
        else if (!files[i].delete())
          throw new IOException("Could not delete path: " + files[i].getAbsolutePath());
      if (!dirAbsPath.delete())
        throw new IOException("Could not delete path: " + dirAbsPath.getAbsolutePath());

    }
  }

  /**
   * Deletes a directory recursively (if there are contents they will be deleted).
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs, or the directory could not be deleted
   * @throws FileNotFoundException The specified path is not a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static void deleteDirectoryRecursive(final String dirAbsPath)
      throws IOException
  {
    val dir = new File(dirAbsPath);
    deleteDirectoryRecursive(dir);
  }

  /**
   * Attempts to delete a directory (if there are contents they will be deleted). Returns true if succeeded. No exception is thrown under
   * any circumstance.
   */
  public static boolean tryDeleteDirectoryRecursive(File dirAbsPath)
  {
    try
    {
      deleteDirectoryRecursive(dirAbsPath);
      return true;
    }
    catch(Throwable e)
    {
      return false;
    }
  }

  /**
   * Attempts to delete a directory (if there are contents they will be deleted). Returns true if succeeded. No exception is thrown under
   * any circumstance.
   */
  public static boolean tryDeleteDirectoryRecursive(String dirAbsPath)
  {
    try
    {
      deleteDirectoryRecursive(dirAbsPath);
      return true;
    }
    catch(Throwable e)
    {
      return false;
    }
  }

  /**
   * Touches a directory, i.e. creates it if not there, otherwise updates its last write time.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException A file was specified instead of a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static void touchDirectory(@NotNull final File dirAbsPath)
      throws IOException
  {
    if (dirAbsPath.exists())
    {
      if (!dirAbsPath.isDirectory())
        throw new FileNotFoundException("The specified entry was a file, not a directory: " + dirAbsPath.getAbsolutePath());

      // set to current date/time (millis since 1/1/1970)
      dirAbsPath.setLastModified(System.currentTimeMillis());
    } else if (!dirAbsPath.mkdirs())
      throw new IOException("Could not touch directory, because creation failed.");
  }

  /**
   * Touches a directory, i.e. creates it if not there, otherwise updates its last write time.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException A file was specified instead of a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  public static void touchDirectory(final String dirAbsPath)
      throws IOException
  {
    val dir = new File(dirAbsPath);
    touchDirectory(dir);
  }

  /**
   * Touches a directory, i.e. creates it if not there, otherwise updates its last write time. Returns true if succeeded. No exception is
   * thrown under any circumstance.
   */
  public static boolean tryTouchDirectory(File dirAbsPath)
  {
    try
    {
      touchDirectory(dirAbsPath);
      return true;
    }
    catch(Throwable e)
    {
      return false;
    }
  }

  /**
   * Touches a directory, i.e. creates it if not there, otherwise updates its last write time. Returns true if succeeded. No exception is
   * thrown under any circumstance.
   */
  public static boolean tryTouchDirectory(String dirAbsPath)
  {
    try
    {
      touchDirectory(dirAbsPath);
      return true;
    }
    catch(Throwable e)
    {
      return false;
    }
  }

  // FileSystemInfo related methods
  private static boolean matchesAnyAttribute(File file, Set<FileAttribute> fileAttributes)
      throws FileNotFoundException
  {
    if (!file.exists())
      throw new FileNotFoundException("The specified path is not an existing file or folder: " + file.getAbsolutePath());

    if (fileAttributes.contains(FileAttribute.Hidden) && file.isHidden())
      return true;
    if (fileAttributes.contains(FileAttribute.File) && file.isFile())
      return true;
    if (fileAttributes.contains(FileAttribute.Directory) && file.isDirectory())
      return true;
    if (fileAttributes.contains(FileAttribute.Readable) && file.canRead())
      return true;
    if (fileAttributes.contains(FileAttribute.Writeable) && file.canWrite())
      return true;
    if (fileAttributes.contains(FileAttribute.Executable) && file.canExecute())
      return true;

    return false;
  }

  /**
   * Filters out files/directories which have the specified attributes.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException A file was specified instead of a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static File[] filterOut(@NotNull final File[] files, @NotNull final Set<FileAttribute> fileAttributes)
      throws IOException
  {
    val result = new ReifiedArrayList<File>(64, File.class);
    for (File fi : files)
      if (!matchesAnyAttribute(fi, fileAttributes))
        result.add(fi);

    return result.toArray();
  }

  /**
   * Filters out files/directories which do not have the specified attributes.
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   * @throws FileNotFoundException A file was specified instead of a directory
   * @throws SecurityException Access to filesystem is denied by a SecurityManager
   */
  @Validate
  public static File[] filterIn(@NotNull final File[] files, @NotNull final Set<FileAttribute> fileAttributes)
      throws IOException
  {
    val result = new ReifiedArrayList<File>(64, File.class);
    for (File fi : files)
      if (matchesAnyAttribute(fi, fileAttributes))
        result.add(fi);

    return result.toArray();
  }

  // Shortcuts
  /**
   * On Win32/Linux, creates a shortcut to a file or folder (use overloaded version for linking to an application). On OSX, creates a
   * symlink to a file or folder.
   * 
   * @param shortcutAbsPath The destination filename of the shortcut. This will be overwritten if already exists.
   * @param linkedResourceAbsPath The path to the file or folder to link to.
   * 
   * @throws NullPointerException An argument is null
   */
  public static void createApplicationShortcut(String shortcutAbsPath, String linkedResourceAbsPath)
      throws IOException
  {
    createApplicationShortcut(shortcutAbsPath, linkedResourceAbsPath, null);
  }

  /**
   * On Win32/Linux, creates a shortcut to a file, folder or application (if an application working dir is specified). On OSX, creates a
   * symlink to the file/folder/app. Note that if the linked resource is an app, you cannot specify an application working directory and the
   * application must know where it has been installed to access any resource files it needs. Furthermore, launching symlinks from OSX
   * Finder launches a console first, so overall not a great UX. A ".url" file is created in Windows, an extension-less [Desktop Entry] text
   * file on Linux or a symlink on OSX.
   * 
   * @param shortcutAbsPath The destination filename of the shortcut. This will be overwritten if already exists.
   * @param linkedResourceAbsPath The path to the file or folder to link to.
   * @param applicationWorkingDirectory The working directory of the application. Use null for non-applications: i.e. files or for folders.
   *          This is ignored on OSX, as symlinks are used.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException The linked resource does not exist
   * @throws IOException An I/O error occurs
   */
  @Validate
  public static void createApplicationShortcut(@NotNull String shortcutAbsPath, @NotNull final String linkedResourceAbsPath,
                                               String applicationWorkingDirectory)
      throws IOException
  {
    val linkedResourceFile = new File(linkedResourceAbsPath);
    if (!linkedResourceFile.exists())
      throw new IllegalArgumentException("The linked resource does not exist: " + linkedResourceAbsPath);

    // Windows OS
    if (OsUtils.isWindows())
    {
      // ensure correct extension is used
      if (!StringUtils.endsWith(shortcutAbsPath, ".url", StringComparison.OrdinalIgnoreCase))
      {
        if (!StringUtils.endsWith(shortcutAbsPath, CONSTANT.DOT, StringComparison.Ordinal))
          shortcutAbsPath += CONSTANT.DOT;
        shortcutAbsPath += "url";
      }

      // remove old shortcut if it exists
      tryDeleteFile(shortcutAbsPath);

      // windows OS
      val bw = new BufferedWriter(new FileWriter(shortcutAbsPath, false));

      bw.write("[InternetShortcut]");
      bw.newLine();

      // path to file/folder
      bw.write("URL=file:*" + linkedResourceAbsPath);
      bw.newLine();
      // working directory is required, whether specified or not
      if (applicationWorkingDirectory != null)
      {
        bw.write("WorkingDirectory=" + applicationWorkingDirectory);
        bw.newLine();
      } else
      {
        val lrf = new File(linkedResourceAbsPath);
        if (lrf.exists())
        {
          // for files, get their parent directory
          bw.write("WorkingDirectory=" + lrf.getParent());
          bw.newLine();
        } else
        {
          // for directory, use the same directory
          bw.write("WorkingDirectory=" + linkedResourceAbsPath);
          bw.newLine();
        }
      }
      // icon stuff
      bw.write("IconIndex=0");
      bw.newLine();
      /** note that iconfile could also be "%systemroot%\\SYSTEM\\url.dll" */
      bw.write("IconFile=" + linkedResourceAbsPath);
      bw.newLine();

      bw.flush();
      bw.close();
    } else
    // MacOS / Unix
    if (OsUtils.isOSX() || OsUtils.isBsd())
    {
      // delete symlink if it exists
      val p1 = Runtime.getRuntime().exec(new String[] {"rm", "\"" + shortcutAbsPath + "\""});
      BufferedReader br = new BufferedReader(new InputStreamReader(p1.getInputStream()));
      String line = null;
      while ((line = br.readLine()) != null)
      {}
      try
      {
        p1.waitFor();
      }
      catch(InterruptedException e)
      {}

      val p2 = Runtime.getRuntime().exec(new String[] {"ln", "-s", "\"" + linkedResourceAbsPath + "\"", "\"" + shortcutAbsPath + "\""});
      br = new BufferedReader(new InputStreamReader(p2.getInputStream()));

      while ((line = br.readLine()) != null)
        // this step should not fail, if it does, throw exception
        throw new IOException("Failed to create symlink: " + line);
      try
      {
        p2.waitFor();
      }
      catch(InterruptedException e)
      {}

    } else
    {
      // Linux OS

      // remove old shortcut if it exists
      tryDeleteFile(shortcutAbsPath);

      val bw = new BufferedWriter(new FileWriter(shortcutAbsPath, false));
      bw.write("[Desktop Entry]");
      bw.newLine();

      // icon type to use depends on whether the shortcut link is a file or folder
      if (linkedResourceFile.isFile())
      {
        bw.write("Icon=file");
        bw.newLine();
      } else if (linkedResourceFile.isDirectory())
      {
        bw.write("Icon=folder");
        bw.newLine();
      }
      // for applications the type must be Application, we determine that from whether an application working directory was given
      if (applicationWorkingDirectory == null)
      {
        // files and folders
        bw.write("Type=Link");
        bw.newLine();
        // path to file/folder
        bw.write("URL[$e]=file://" + linkedResourceAbsPath);
        bw.newLine();
      } else
      {
        // application executables
        bw.write("Type=Application");
        bw.newLine();
        // specify working directory if shortcut to application
        bw.write("Path=" + applicationWorkingDirectory);
        bw.newLine();
        // executable and arguments
        bw.write("Exec=" + linkedResourceAbsPath);
        bw.newLine();
      }

      bw.flush();
      bw.close();
    }
  }

  /**
   * Creates a URL shortcut. The shortcut will have the .url extension. Ensure that the URL starts with a protocol e.g. http://...
   * 
   * @throws NullPointerException An argument is null
   * @throws IOException An I/O error occurs
   */
  @Validate
  public static void createInternetShortcut(@NotNull String shortcutAbsPath, @NotNull final String url)
      throws IOException
  {
    // ensure correct extension is used
    if (!StringUtils.endsWith(shortcutAbsPath, ".url", StringComparison.OrdinalIgnoreCase))
    {
      if (!StringUtils.endsWith(shortcutAbsPath, CONSTANT.DOT, StringComparison.Ordinal))
        shortcutAbsPath += CONSTANT.DOT;
      shortcutAbsPath += "url";
    }

    // delete any existing file
    tryDeleteFile(shortcutAbsPath);

    val bw = new BufferedWriter(new FileWriter(shortcutAbsPath, false));

    bw.write("[InternetShortcut]");
    bw.newLine();

    // path to file/folder
    bw.write("URL=" + url);
    bw.newLine();

    bw.flush();
    bw.close();
  }

  // File-system access rights
  /**
   * Tests access rights on a folder. Throws exception if not enough rights to perform basic file operations.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static void performCreateReadWriteDeleteAccess(@NotNull final String folderName)
      throws IOException
  {
    // check source is readable/writable
    val folderPath = new File(folderName);
    val randomFilename = new File(folderPath, UUID.randomUUID() + ".crwd");

    if (!randomFilename.createNewFile())
      throw new IOException("Could not create file: " + randomFilename.getAbsolutePath());
    if (!randomFilename.delete())
      throw new IOException("Could not delete file: " + randomFilename.getAbsolutePath());

    try
    {
      val fs = new FileOutputStream(randomFilename);
      fs.write(100);
      fs.flush();
      fs.close();
    }
    catch(Throwable e)
    {
      try
      {
        randomFilename.delete();
      }
      catch(Throwable e2)
      {
        // ignore
      }
      throw new IOException("Could not write to file: " + randomFilename.getAbsolutePath());
    }

    try
    {
      val fis = new FileInputStream(randomFilename);
      fis.read();
      fis.close();
    }
    catch(Throwable e)
    {
      try
      {
        randomFilename.delete();
      }
      catch(Throwable e2)
      {
        // ignore
      }
      throw new IOException("Could not read from file: " + randomFilename.getAbsolutePath());
    }

    if (!randomFilename.delete())
      throw new IOException("Could not delete file: " + randomFilename.getAbsolutePath());
  }

  /**
   * Tests access rights on a folder and returns true if these succeed, otherwise returns false. No exceptions are thrown under any
   * circumstance.
   */
  public static boolean tryPerformCreateReadWriteDeleteAccess(String folderName)
  {
    try
    {
      performCreateReadWriteDeleteAccess(folderName);
      return true;
    }
    catch(Throwable e)
    {
      return false;
    }
  }

  // Scanning
  /**
   * Scans a directory path for files and folders, using the specified configuration.
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException Unrecognized hide, sort or fail mode. Also occurs when fail mode is SkipAndCollect and
   *           collectedScanErrors is null.
   * @throws IOException An I/O error occurs and the scan fail mode is immediate
   * @throws Throwable An error occurs and the scan fail mode is immediate
   */
  public static List<String> scanPath(String path, ScanInclusionMode inclusionMode, ScanHiddenMode hiddenMode, ScanDepthMode depthMode,
                                      ScanSortMode sortMode, ScanFailMode failMode)
      throws Exception
  {
    return scanPath(path, inclusionMode, hiddenMode, depthMode, sortMode, failMode, null);
  }

  /**
   * Scans a directory path for files and folders, using the specified configuration. Allows for collecting caught exceptions (if
   * SkipAndCollection fail mode is selected).
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException Unrecognized hide, sort or fail mode. Also occurs when fail mode is SkipAndCollect and
   *           collectedScanErrors is null.
   * @throws IOException An I/O error occurs and the scan fail mode is immediate
   * @throws Exception An error occurs and the scan fail mode is immediate
   */
  @Validate
  public static List<String> scanPath(@NotNull String path, ScanInclusionMode inclusionMode, ScanHiddenMode hiddenMode,
                                      ScanDepthMode depthMode, ScanSortMode sortMode, ScanFailMode failMode,
                                      List<ScanErrorEntry> collectedScanErrors)
      throws Exception
  {
    if (failMode == ScanFailMode.SkipAndCollect)
      if (collectedScanErrors == null)
        throw new IllegalArgumentException("Cannot use " + ScanFailMode.SkipAndCollect
            + " mode without a specified container for accumulating errors.");

    val dir = new File(path);
    if (!dir.exists())
      throw new IOException("The directory cannot be scanned as it does not exist: " + path);
    if (!dir.isDirectory())
      throw new IOException("The path cannot be scanned as it is not a directory: " + path);

    // get full path
    path = dir.getAbsolutePath();

    switch(depthMode)
    {
      case Shallow:
        return scanPathBreadthFirst(path, (inclusionMode == ScanInclusionMode.Files) || (inclusionMode == ScanInclusionMode.All),
            (inclusionMode == ScanInclusionMode.Directories) || (inclusionMode == ScanInclusionMode.All), hiddenMode, sortMode, failMode,
            collectedScanErrors, false);

      case BreadthFirst:
        return scanPathBreadthFirst(path, (inclusionMode == ScanInclusionMode.Files) || (inclusionMode == ScanInclusionMode.All),
            (inclusionMode == ScanInclusionMode.Directories) || (inclusionMode == ScanInclusionMode.All), hiddenMode, sortMode, failMode,
            collectedScanErrors, true);

      case DepthFirst:
        return scanPathDepthFirst(path, (inclusionMode == ScanInclusionMode.Files) || (inclusionMode == ScanInclusionMode.All),
            (inclusionMode == ScanInclusionMode.Directories) || (inclusionMode == ScanInclusionMode.All), hiddenMode, sortMode, failMode,
            collectedScanErrors);

      default:
        throw new IllegalArgumentException("Unknown depth scan mode: " + depthMode);
    }
  }

  /**
   * Performs breadth-first filesystem path scanning
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException Unrecognized hide, sort or fail mode.
   * @throws IOException An I/O error occurs and the scan fail mode is immediate
   * @throws Exception Some other error occurs and the scan fail mode is immediate
   */
  private static List<String> scanPathBreadthFirst(String path, boolean includeFiles, boolean includeDirectories,
                                                   ScanHiddenMode hiddenMode, ScanSortMode sortMode, ScanFailMode failMode,
                                                   List<ScanErrorEntry> collectedScanErrors, boolean deepScan)
      throws Exception
  {
    val result = new ArrayList<String>(64);

    // maintain a queue of scanned directories
    val directoryQueue = new LinkedList<String>();
    directoryQueue.addLast(path);

    // check if we need to add self
    if (includeDirectories)
      result.addAll(Linq.toList(Linq.select(hideScanned(new File[] {new File(path)}, hiddenMode, failMode, collectedScanErrors),
          getAbsolutePath())));

    // While there are directories to process
    while (directoryQueue.size() > 0)
    {
      String currDirName = directoryQueue.removeFirst();

      try
      {
        File currDir = new File(currDirName);
        if (currDir.exists() && currDir.isDirectory())
        {
          // check if we need to scan for directories at all
          if (includeDirectories || deepScan)
          {
            // get folders
            File[] dis = filterIn(currDir.listFiles(),
                new HashSet<FileAttribute>(Arrays.asList(new FileAttribute[] {FileAttribute.Directory})));
            dis = hideScanned(dis, hiddenMode, failMode, collectedScanErrors);

            List<String> dirDirs = new ArrayList<String>(64);
            for (File di : dis)
              dirDirs.add(di.getAbsolutePath());

            // sort
            dirDirs = sortScanned(dirDirs, sortMode);

            // check if we need to include directories in results
            if (includeDirectories)
              result.addAll(dirDirs);

            // next directories to process
            if (deepScan)
              for (String dirDir : dirDirs)
                directoryQueue.addLast(dirDir);
          }

          // check if we need to include files in results
          if (includeFiles)
          {
            // get files
            File[] fis = filterIn(currDir.listFiles(), new HashSet<FileAttribute>(Arrays.asList(new FileAttribute[] {FileAttribute.File})));
            fis = hideScanned(fis, hiddenMode, failMode, collectedScanErrors);

            List<String> dirFiles = new ArrayList<String>(64);
            for (File fi : fis)
              dirFiles.add(fi.getAbsolutePath());

            // sort
            dirFiles = sortScanned(dirFiles, sortMode);

            // add to result
            result.addAll(dirFiles);
          }
        }
      }
      catch(Exception e)
      {
        switch(failMode)
        {
          case Never:
            continue;
          case Immediate:
            throw e;
          case SkipAndCollect:
            collectedScanErrors.add(new ScanErrorEntry(currDirName, e));
            break;
          default:
            throw new IllegalArgumentException("Unknown fail mode: " + failMode, e);
        }
      }
    }

    return result;
  }

  /**
   * Performs depth-first filesystem path scanning
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException Unrecognized hide, sort or fail mode.
   * @throws IOException An I/O error occurs and the scan fail mode is immediate
   * @throws Exception Some other error occurs and the scan fail mode is immediate
   */
  private static List<String> scanPathDepthFirst(String path, boolean includeFiles, boolean includeDirectories, ScanHiddenMode hiddenMode,
                                                 ScanSortMode sortMode, ScanFailMode failMode, List<ScanErrorEntry> collectedScanErrors)
      throws Exception
  {
    val result = new ArrayList<String>(64);

    // maintain a stack of scanned files/directories
    LinkedList<String> fsEntryStack = new LinkedList<String>();
    fsEntryStack.addFirst(path);

    // While there are directories to process
    while (fsEntryStack.size() > 0)
    {
      String fsEntryName = fsEntryStack.removeFirst();
      try
      {
        File currDir = new File(fsEntryName);
        if (currDir.exists() && currDir.isDirectory())
        {
          // files to process
          if (includeFiles)
          {
            // get files
            File[] fis = filterIn(currDir.listFiles(), new HashSet<FileAttribute>(Arrays.asList(new FileAttribute[] {FileAttribute.File})));
            fis = hideScanned(fis, hiddenMode, failMode, collectedScanErrors);

            List<String> dirFiles = new ArrayList<String>(64);
            for (File fi : fis)
              dirFiles.add(fi.getAbsolutePath());

            // sort
            dirFiles = sortScanned(dirFiles, sortMode);

            // next files to process
            for (int i = dirFiles.size() - 1; i >= 0; i--)
              fsEntryStack.addFirst(dirFiles.get(i));
          }

          // check if we need to include directories in results
          if (includeDirectories)
            result.addAll(Linq.toList(Linq.select(hideScanned(new File[] {currDir}, hiddenMode, failMode, collectedScanErrors),
                getAbsolutePath())));

          File[] dis = filterIn(currDir.listFiles(),
              new HashSet<FileAttribute>(Arrays.asList(new FileAttribute[] {FileAttribute.Directory})));
          dis = hideScanned(dis, hiddenMode, failMode, collectedScanErrors);

          List<String> dirDirs = new ArrayList<String>(64);
          for (File dirDir : dis)
            dirDirs.add(dirDir.getAbsolutePath());

          // sort
          dirDirs = sortScanned(dirDirs, sortMode);

          // next directories to process
          for (int i = dirDirs.size() - 1; i >= 0; i--)
            fsEntryStack.addFirst(dirDirs.get(i));
        } else if (currDir.exists() && currDir.isFile())
        {
          File currFile = currDir;

          // check if we need to include files in results
          if (includeFiles)
            result.addAll(Linq.toList(Linq.select(hideScanned(new File[] {currFile}, hiddenMode, failMode, collectedScanErrors),
                getAbsolutePath())));
        }
      }
      catch(Exception e)
      {
        switch(failMode)
        {
          case Never:
            continue;
          case Immediate:
            throw e;
          case SkipAndCollect:
            collectedScanErrors.add(new ScanErrorEntry(fsEntryName, e));
            break;
          default:
            throw new IllegalArgumentException("Unknown fail mode: " + failMode, e);
        }
      }
    }

    return result;
  }

  /**
   * Sorts a list using the specified sort mode
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException The sort mode is not recognised
   */
  private static List<String> sortScanned(List<String> names, ScanSortMode sortMode)
  {
    switch(sortMode)
    {
      case None:
        return names;
      case Ascending:
        Collections.sort(names);
        return names;
      case Descending:
        Collections.sort(names);
        Collections.reverse(names);
        return names;
      default:
        throw new IllegalArgumentException("Unrecognized sort mode: " + sortMode);
    }
  }

  /**
   * Applies the relevant hiding mode, e.g. returning only hidden files, only non-hidden files, or all files.
   * 
   * @throws NullPointerException An argument is null
   * @throws Exception An error occurs during file attribute checking
   */
  private static File[] hideScanned(File[] fis, ScanHiddenMode hiddenMode, ScanFailMode failMode, List<ScanErrorEntry> collectedScanErrors)
      throws Exception
  {
    if (fis == null)
      throw new NullPointerException("fis");

    ReifiedList<File> result = new ReifiedArrayList<File>(fis.length, File.class);
    switch(hiddenMode)
    {
      case All:
        return fis;
      case NormalOnly:
      case HiddenOnly:
        for (File fi : fis)
          try
          {
            // do not include hidden
            if (hiddenMode == ScanHiddenMode.NormalOnly)
            {
              if (!fi.isHidden())
                result.add(fi);
            } // only include hidden
            else if (fi.isHidden())
              result.add(fi);
          }
          catch(Exception e)
          {
            switch(failMode)
            {
              case Never:
                continue;
              case Immediate:
                throw e;
              case SkipAndCollect:
                collectedScanErrors.add(new ScanErrorEntry(fi.getAbsolutePath(), e));
                break;
              default:
                throw new IllegalArgumentException("Unrecognized fail mode: " + failMode);
            }
          }
        return result.toArray();
      default:
        throw new IllegalArgumentException("Unrecognized hide mode: " + hiddenMode);
    }
  }

  private FileUtils()
  {
  }
}
