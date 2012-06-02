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

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import lombok.Validate;
import lombok.Validate.NotNull;
import lombok.val;
import propel.core.common.CONSTANT;

/**
 * Provides utility functionality for Stream-related classes
 */
public final class StreamUtils
{
  /**
   * Message used in thrown exception when a stream end is encountered prematurely.
   */
  public static final String EOF_EXCEPTION_MESSAGE_BYTES = "The stream " + CONSTANT.EOF
      + "ed unexpectedly, while %d extra bytes were expected.";
  /**
   * Message used in thrown exception when a stream end is encountered prematurely.
   */
  public static final String EOF_EXCEPTION_MESSAGE_CHARACTERS = "The stream " + CONSTANT.EOF
      + "ed unexpectedly, while %d extra characters were expected.";

  /**
   * Copies data from one stream to another. Reads all data from source stream, then writes it to destination, then the destination stream
   * is flushed.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static void copy(@NotNull final InputStream from, @NotNull final OutputStream to, final long length)
      throws IOException
  {
    if (length < 0)
      throw new IllegalArgumentException("length=" + length);
    if (length == 0)
      return;

    writeFully(to, readFully(from, length));
  }

  /**
   * Blocks until all data is written to the given stream. The stream is flushed.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static void writeFully(@NotNull final OutputStream stream, @NotNull final byte[] data)
      throws IOException
  {
    stream.write(data, 0, data.length);
    stream.flush();
  }

  /**
   * Blocks until all data is written to the given stream. The stream is flushed. Performs multiple writes if the size if bigger than the
   * maximum allowed chunk size.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static void writeFully(@NotNull final OutputStream stream, @NotNull final byte[] data, final int maxChunkSize)
      throws IOException
  {
    if (maxChunkSize <= 0)
      throw new IllegalArgumentException("maxChunkSize=" + maxChunkSize);

    val length = data.length;

    if (length < maxChunkSize)
      stream.write(data, 0, length);
    else
    {
      int remainder = length % maxChunkSize;
      int chunks = length / maxChunkSize;
      for (int i = 0; i < chunks; i++)
        stream.write(data, i * maxChunkSize, maxChunkSize);

      if (remainder > 0)
        stream.write(data, chunks * maxChunkSize, remainder);
    }

    stream.flush();
  }

  /**
   * Blocks until all bytes are read from the stream, returns the decoded data using the UTF8 encoding.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  public static String readAllCharacters(final InputStream stream)
      throws IOException
  {
    return readAllCharacters(stream, CONSTANT.UTF8);
  }

  /**
   * Blocks until all bytes are read from the stream, returns the decoded data using the specified encoding.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static String readAllCharacters(@NotNull final InputStream stream, @NotNull final Charset streamEncoding)
      throws IOException
  {
    val data = readFully(stream, stream.available());
    return new String(data, streamEncoding);
  }

  /**
   * Blocks until a specified amount of bytes is read from a stream. Puts the data read into the specified array.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static void readFully(@NotNull final InputStream stream, @NotNull final byte[] buffer, final long length)
      throws IOException
  {
    if (length < 0)
      throw new IllegalArgumentException("length=" + length);
    if (buffer.length < length)
      throw new IllegalArgumentException("bufferLen=" + buffer.length + " length=" + length);
    if (length > Integer.MAX_VALUE)
      throw new IllegalArgumentException("length=" + length);

    int totalRemaining = (int) length;
    int totalRead = 0;

    while (totalRemaining > 0)
    {
      // attempt to read
      int read = stream.read(buffer, totalRead, totalRemaining);

      if (read <= 0)
        throw new IOException(String.format(EOF_EXCEPTION_MESSAGE_BYTES, totalRemaining));

      // maintain counters
      totalRead += read;
      totalRemaining -= read;
    }
  }

  /**
   * Blocks until a specified amount of bytes is read from a stream. Returns the data read.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static byte[] readFully(@NotNull final InputStream stream, final long length)
      throws IOException
  {
    if (length < 0)
      throw new IllegalArgumentException("length=" + length);
    if (length > Integer.MAX_VALUE)
      throw new IllegalArgumentException("length=" + length);

    val buffer = new byte[(int) length];
    readFully(stream, buffer, length);
    return buffer;
  }

  /**
   * Blocks until a specified amount of bytes is read from a stream. Puts the data read into the specified array. Performs 'chunked'
   * transfer if size if bigger than the maximum allowed packet size.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static void readFully(@NotNull final InputStream stream, @NotNull final byte[] buffer, final long length, final int maxChunkSize)
      throws IOException
  {
    if (length < 0)
      throw new IllegalArgumentException("length=" + length);
    if (buffer.length < length)
      throw new IllegalArgumentException("bufferLen=" + buffer.length + " length" + length);
    if (length > Integer.MAX_VALUE)
      throw new IllegalArgumentException("length=" + length);
    if (maxChunkSize <= 0)
      throw new IllegalArgumentException("maxChunkSize=" + maxChunkSize);

    int totalRemaining = (int) length;
    int totalRead = 0;

    while (totalRemaining > 0)
    {
      // attempt to read chunk or complete length depending on size
      int read = stream.read(buffer, totalRead, (totalRemaining < maxChunkSize ? totalRemaining : maxChunkSize));

      if (read <= 0)
        throw new IOException(String.format(EOF_EXCEPTION_MESSAGE_BYTES, totalRemaining));

      // maintain counters
      totalRead += read;
      totalRemaining -= read;
    }
  }

  /**
   * Blocks until a specified amount of bytes is read from a stream. Returns the data read. Performs multiple reads if the size if bigger
   * than the maximum allowed chunk size.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static byte[] readFully(@NotNull final InputStream stream, final long length, final int maxChunkSize)
      throws IOException
  {
    if (length < 0 || length > Integer.MAX_VALUE)
      throw new IllegalArgumentException("length=" + length);

    val buffer = new byte[(int) length];
    readFully(stream, buffer, length, maxChunkSize);
    return buffer;
  }

  /**
   * Blocks until the specified character is encountered and all characters read until then from the UTF8-encoded byte-stream are skipped.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  public static void skipUntil(final InputStream stream, final char terminator)
      throws IOException
  {
    skipUntil(stream, terminator, CONSTANT.UTF8);
  }

  /**
   * Blocks until the specified character is encountered and all characters read until then from the byte-stream are skipped.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws IOException An I/O exception occurred.
   */
  public static void skipUntil(final InputStream stream, final char terminator, final Charset streamEncoding)
      throws IOException
  {
    while (readCharacter(stream, streamEncoding) != terminator)
    {}
  }

  /**
   * Blocks until the specified string of characters is encountered and all characters read until then from the byte-stream are skipped. The
   * character encoding can be specified.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  public static void skipUntil(final InputStream stream, final String terminator)
      throws IOException
  {
    skipUntil(stream, terminator, CONSTANT.UTF8);
  }

  /**
   * Blocks until the specified string of characters is encountered and all characters read until then from the UTF8-encoded byte-stream are
   * skipped.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static void skipUntil(@NotNull final InputStream stream, @NotNull final String terminator, @NotNull final Charset streamEncoding)
      throws IOException
  {
    // this is the terminator string
    val terminatorCharBuffer = ByteBuffer.wrap(terminator.getBytes(streamEncoding)).asCharBuffer();

    // buffer holding the latest characters
    CharBuffer tempCharBuffer = CharBuffer.allocate(terminatorCharBuffer.length());

    // read the first batch
    for (int i = 0; i < terminatorCharBuffer.length(); i++)
      tempCharBuffer.put(readCharacter(stream, streamEncoding));

    // compare and read next until arrays hold equal values
    while (!(StringUtils.sequenceEqual(terminatorCharBuffer.array(), tempCharBuffer.array())))
    {
      // read next char
      val ch = readCharacter(stream, streamEncoding);

      // left shift elements in array, throw away the first read
      tempCharBuffer = CharBuffer.wrap(tempCharBuffer.subSequence(1, tempCharBuffer.length())).append(ch);
    }
  }

  /**
   * Blocks until the specified byte is encountered and all bytes read until then are skipped.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static void skipUntil(@NotNull final InputStream stream, final byte terminator)
      throws IOException
  {
    while (true)
    {
      // read next byte
      val read = stream.read();

      // check status
      if (read >= 0)
      {
        if ((byte) read == terminator)
          break;
      } else
        throw new IOException(String.format(EOF_EXCEPTION_MESSAGE_BYTES, 1));
    }
  }

  /**
   * Blocks until the specified byte sequence is encountered and all bytes read until then are skipped.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static void skipUntil(@NotNull final InputStream stream, @NotNull final byte[] terminator)
      throws IOException
  {
    // buffer holding the latest bytes
    val tempBuffer = new byte[terminator.length];

    // read the first batch
    for (int i = 0; i < tempBuffer.length; i++)
    {
      val read = stream.read();

      // check status
      if (read >= 0)
        tempBuffer[i] = (byte) read;
      else
        throw new IOException(String.format(EOF_EXCEPTION_MESSAGE_BYTES, tempBuffer.length - i));
    }

    // compare and read next until arrays hold equal values
    while (!(ByteArrayUtils.sequenceEqual(tempBuffer, terminator)))
    {
      // read next byte
      val read = stream.read();

      // check status
      if (read >= 0)
      {
        // left shift elements in array, throw away the first read
        for (int i = 0; i < tempBuffer.length - 1; i++)
          tempBuffer[i] = tempBuffer[i + 1];

        // set last array element to read char
        tempBuffer[tempBuffer.length - 1] = (byte) read;
      } else
        throw new IOException(String.format(EOF_EXCEPTION_MESSAGE_BYTES, 1));
    }
  }

  /**
   * Blocks until a specified amount of bytes is read from a stream. Throws away the bytes read.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws IOException An I/O exception occurred.
   */
  public static void skipBytes(final InputStream stream, final int length)
      throws IOException
  {
    readFully(stream, length);
  }

  /**
   * Skips a specified number of characters from byte-stream, using UTF8 to convert bytes into characters.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws IOException An I/O exception occurred.
   */
  public static void skipCharacters(final InputStream stream, final int count)
      throws IOException
  {
    skipCharacters(stream, count, CONSTANT.UTF8);
  }

  /**
   * Skips a specified number of characters from the byte-stream, using the specified encoding to convert bytes into characters.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static void skipCharacters(final InputStream stream, final int count, @NotNull final Charset streamEncoding)
      throws IOException
  {
    if (count < 0)
      throw new IllegalArgumentException("count");

    for (int i = 0; i < count; i++)
      readCharacter(stream, streamEncoding);
  }

  /**
   * Reads a string from the UTF8-encoded byte-stream until a terminator character is found. The terminator character is thrown away and the
   * stream moves past it.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  public static String readUntil(final InputStream stream, final char terminator)
      throws IOException
  {
    return readUntil(stream, terminator, CONSTANT.UTF8);
  }

  /**
   * Reads a string from the stream until a terminator character is found. The terminator character is thrown away and the stream moves past
   * it.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static String readUntil(final InputStream stream, final char terminator, @NotNull final Charset streamEncoding)
      throws IOException
  {
    char ch;
    val sb = new StringBuilder(256);

    while ((ch = readCharacter(stream, streamEncoding)) != terminator)
      sb.append(ch);

    return sb.toString();
  }

  /**
   * Reads a string from the UTF8-encoded byte-stream until a terminator string is found. The terminator string is thrown away and the
   * stream moves past it.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  public static String readUntil(final InputStream stream, final String terminator)
      throws IOException
  {
    return readUntil(stream, terminator, CONSTANT.UTF8);
  }

  /**
   * Reads a string from the byte-stream with the specified encoding until a terminator string is found. The terminator string is thrown
   * away and the stream moves past it.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static String
      readUntil(@NotNull final InputStream stream, @NotNull final String terminator, @NotNull final Charset streamEncoding)
          throws IOException
  {
    val result = new StringBuilder(256);

    // this is the terminator string
    val terminatorCharBuffer = CharBuffer.wrap(terminator.toCharArray());

    // buffer holding the latest characters read
    CharBuffer tempCharBuffer = CharBuffer.allocate(terminatorCharBuffer.length());

    // read the first batch to fill the buffer
    for (int i = 0; i < terminatorCharBuffer.length(); i++)
      tempCharBuffer.put(readCharacter(stream, streamEncoding));

    // compare and read next until arrays hold equal values
    while (!(StringUtils.sequenceEqual(terminatorCharBuffer.array(), tempCharBuffer.array())))
    {
      // read next char
      val ch = readCharacter(stream, streamEncoding);

      // left shift elements in array, throw away the first read
      tempCharBuffer = CharBuffer.wrap(tempCharBuffer.subSequence(1, tempCharBuffer.length())).append(ch);

      // add to result string
      result.append(ch);
    }

    return result.substring(0, result.length() - terminatorCharBuffer.length());
  }

  /**
   * Reads data from the stream until a terminator byte is found. The terminator byte is thrown away and the stream moves past it.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static byte[] readUntil(@NotNull final InputStream stream, final byte terminator)
      throws IOException
  {
    val result = new ByteArrayOutputStream(128);

    while (true)
    {
      // read next byte
      val read = stream.read();

      // check status
      if (read >= 0)
      {
        val bt = (byte) read;

        if (bt == terminator)
          break;

        result.write(bt);
      } else
        throw new IOException(String.format(EOF_EXCEPTION_MESSAGE_BYTES, 1));
    }

    return result.toByteArray();
  }

  /**
   * Reads data from the byte-stream until a terminator string is found. The terminator string is thrown away and the stream moves past it.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static byte[] readUntil(@NotNull final InputStream stream, @NotNull final byte[] terminator)
      throws IOException
  {
    val result = new ByteArrayOutputStream(128);

    // buffer holding the latest bytes
    val tempBuffer = new byte[terminator.length];

    // read the first batch
    for (int i = 0; i < tempBuffer.length; i++)
    {
      int read = stream.read();

      // check status
      if (read >= 0)
        tempBuffer[i] = (byte) read;
      else
        throw new IOException(String.format(EOF_EXCEPTION_MESSAGE_BYTES, tempBuffer.length - i));
    }
    result.write(tempBuffer, 0, tempBuffer.length);

    // compare and read next until arrays hold equal values
    while (!(ByteArrayUtils.sequenceEqual(tempBuffer, terminator)))
    {
      // read next byte
      val read = stream.read();

      // check status
      if (read >= 0)
      {
        val bt = (byte) read;

        // shift elements in array to the left, throw away the first read
        for (int i = 0; i < tempBuffer.length - 1; i++)
          tempBuffer[i] = tempBuffer[i + 1];

        // set last array element to read char
        tempBuffer[tempBuffer.length - 1] = bt;

        result.write(bt);
      } else
        throw new IOException(String.format(EOF_EXCEPTION_MESSAGE_BYTES, 1));
    }

    // remove terminator
    return ByteArrayUtils.resize(result.toByteArray(), result.size() - terminator.length);
  }

  /**
   * Reads a string from the byte-stream with the specified encoding until a terminator string is found. The terminator string is thrown
   * away and the stream moves past it.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static String readUntil(InputStream stream, byte[] terminator, @NotNull final Charset streamEncoding)
      throws IOException
  {
    val ba = readUntil(stream, terminator);
    val result = streamEncoding.decode(ByteBuffer.wrap(ba));
    return result.toString();
  }

  /**
   * Reads the next characters, if any, from a <param name="stream"/>,decoding bytes using the UTF8 decoder.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws IOException An I/O exception occurred.
   */
  public static char[] readCharacters(final InputStream stream, final int count)
      throws IOException
  {
    return readCharacters(stream, CONSTANT.UTF8, count);
  }

  /**
   * Reads the next characters, if any, from a <param name="stream"/>, decoding bytes using the specified decoder.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws IOException An I/O exception occurred.
   */
  public static char[] readCharacters(final InputStream stream, final Charset streamEncoding, final int count)
      throws IOException
  {
    if (count < 0)
      throw new IllegalArgumentException("count");

    char[] result = new char[count];
    int i = 0;

    try
    {
      for (i = 0; i < count; i++)
        result[i] = readCharacter(stream, streamEncoding);
    }
    catch(IOException e)
    {
      // repackage exception to mention actual number of characters expected
      if (e.getMessage().contains(CONSTANT.EOF))
        throw new IOException(String.format(EOF_EXCEPTION_MESSAGE_CHARACTERS, count - i));

      throw e;
    }
    return result;
  }

  /**
   * Reads the next character, if any, from a UTF8-encoded byte-stream,decoding bytes UTF8 format.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  public static char readCharacter(final InputStream stream)
      throws IOException
  {
    return readCharacter(stream, CONSTANT.UTF8);
  }

  /**
   * Reads the next character from a byte-stream, if any, decoding bytes into the specified encoding's decoder e.g.
   * UTF8Encoding.GetDecoder().
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static char readCharacter(@NotNull final InputStream stream, @NotNull final Charset streamEncoding)
      throws IOException
  {
    val byteBuffer = ByteBuffer.allocate(8);

    CharBuffer chars;
    do
    {
      int b = stream.read();

      if (b < 0)
        throw new IOException(String.format(EOF_EXCEPTION_MESSAGE_CHARACTERS, 1));

      byteBuffer.put((byte) b);

      // attempt to decode byte -> char, returns 0 if failed
      chars = streamEncoding.decode(byteBuffer);
    }
    while (chars.length() == 0);

    return chars.get();
  }

  /**
   * Reads all characters from a stream, returning a string.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static String readAllCharacters(@NotNull final Reader textStream)
      throws IOException
  {
    // create a 4KB buffer to read the file
    CharArrayWriter writer = new CharArrayWriter();
    final char[] bytes = new char[4096];

    int bytesRead;
    while ((bytesRead = textStream.read(bytes)) > -1)
    {
      writer.write(bytes, 0, bytesRead);
    }
    writer.flush();

    return writer.toString();
  }

  /**
   * Skips characters in the stream until the specified character is read
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  public static void skipUntil(final Reader textStream, final char terminator)
      throws IOException
  {
    while (readCharacter(textStream) != terminator)
    {}
  }

  /**
   * Skips characters in the stream until the specified string is read
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  public static void skipUntil(final Reader textStream, final String terminator)
      throws IOException
  {
    readUntil(textStream, terminator);
  }

  /**
   * Reads a specified number of characters from the stream.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws IOException An I/O exception occurred.
   */
  public static String readFully(final Reader textStream, final int count)
      throws IOException
  {
    if (count < 0)
      throw new IllegalArgumentException("count=" + count);

    val sb = new StringBuilder(256);

    for (int i = 0; i < count; i++)
      sb.append(readCharacter(textStream));

    return sb.toString();
  }

  /**
   * Skips a specified number of characters from the stream
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws IOException An I/O exception occurred.
   */
  public static void skipCharacters(final Reader textStream, final int count)
      throws IOException
  {
    if (count < 0)
      throw new IllegalArgumentException("count=" + count);

    for (int i = 0; i < count; i++)
      readCharacter(textStream);
  }

  /**
   * Reads a string from the stream until a terminator character is found. The terminator character is thrown away and the stream moves past
   * it.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  public static String readUntil(final Reader textStream, final char terminator)
      throws IOException
  {
    char ch;
    val sb = new StringBuilder(256);

    while ((ch = readCharacter(textStream)) != terminator)
      sb.append(ch);

    return sb.toString();
  }

  /**
   * Reads a string from the stream until a terminator string is found. The terminator string is thrown away and the stream moves past it.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static String readUntil(final Reader textStream, @NotNull final String terminator)
      throws IOException
  {
    val result = new StringBuilder(256);

    // this is the terminator string
    val terminatorChars = terminator.toCharArray();

    // buffer holding the latest characters
    val tempBuffer = new char[terminatorChars.length];

    // read the first batch
    for (int i = 0; i < tempBuffer.length; i++)
    {
      tempBuffer[i] = readCharacter(textStream);
      result.append(tempBuffer[i]);
    }

    // compare and read next until arrays hold equal values
    while (!(StringUtils.sequenceEqual(terminatorChars, tempBuffer)))
    {
      // read next char
      val ch = readCharacter(textStream);

      // left shift elements in array, throw away the first read
      for (int i = 0; i < tempBuffer.length - 1; i++)
        tempBuffer[i] = tempBuffer[i + 1];

      // set last array element to read char
      tempBuffer[tempBuffer.length - 1] = ch;

      // add to result string
      result.append(ch);
    }

    return result.substring(0, result.length() - terminator.length());
  }

  /**
   * Reads a string from the stream until a terminator character is found. The terminator character is not read (but peeked) therefore the
   * stream does not move past it.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException A stream was passed that does not support peeking (mark/reset)
   * @throws IOException An I/O exception occurred.
   */
  public static String readUntilPeeking(final Reader textStream, final char terminator)
      throws IOException
  {
    val result = new StringBuilder(256);
    while (peekCharacter(textStream) != terminator)
      result.append(readCharacter(textStream));

    return result.toString();
  }

  /**
   * Reads the next stream character, if any.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static char readCharacter(@NotNull final Reader textStream)
      throws IOException
  {
    val ch = textStream.read();

    if (ch < 0)
      throw new IOException(String.format(EOF_EXCEPTION_MESSAGE_CHARACTERS, 1));

    return (char) ch;
  }

  /**
   * Reads the next stream characters, if any.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws IOException An I/O exception occurred.
   */
  public static char[] readCharacters(final Reader textStream, final int count)
      throws IOException
  {
    if (count < 0)
      throw new IllegalArgumentException("count=" + count);

    val result = new char[count];
    int i = 0;

    try
    {
      for (i = 0; i < count; i++)
        result[i] = readCharacter(textStream);
    }
    catch(IOException e)
    {
      // repackage exception to mention actual number of characters expected
      if (e.getMessage().contains(CONSTANT.EOF))
        throw new IOException(String.format(EOF_EXCEPTION_MESSAGE_CHARACTERS, count - i));

      throw e;
    }
    return result;
  }

  /**
   * Peeks the next stream character, if any.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException A stream was passed that does not support peeking (mark/reset)
   * @throws IOException An I/O exception occurred.
   */
  @Validate
  public static char peekCharacter(@NotNull final Reader textStream)
      throws IOException
  {
    if (!textStream.markSupported())
      throw new IllegalArgumentException("Peeking is not possible because mark()/reset() are not supported!");

    textStream.mark(1);

    val ch = textStream.read();

    textStream.reset();

    if (ch < 0)
      throw new IOException(String.format(EOF_EXCEPTION_MESSAGE_CHARACTERS, 1));

    return (char) ch;
  }

  private StreamUtils()
  {
  }
}
