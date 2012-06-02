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

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.Validate;
import lombok.Validate.NotNull;
import lombok.val;
import propel.core.common.CONSTANT;

/**
 * Provides utility functionality for obtaining random data. Please note that the random number generators are not thread safe.
 */
public final class RandomUtils
{
  private static final SecureRandom CRYPTO_RANDOM = new SecureRandom();
  private static final Random PSEUDO_RANDOM = new Random();

  /**
   * Returns a random Int32, created by the information entropy from bytes in a newly created UUID.
   */
  public static int getPseudoInt32()
  {
    val guid = UUID.randomUUID();
    return HashingUtils.uuidToInt32(guid);
  }

  /**
   * Returns a pseudo random within the specified range [min, max) i.e. inclusive minimum and exclusive maximum, created by java.util.Random
   * 
   * @throws IllegalArgumentException An argument is out of range
   */
  public static int getPseudoInt32(final int min, final int max)
  {
    if (min < 0)
      throw new IllegalArgumentException("min=" + min);
    if (max < min)
      throw new IllegalArgumentException("max=" + max + " min=" + min);

    return min + PSEUDO_RANDOM.nextInt(max - min);
  }

  /**
   * Returns a random Int64, created by the information entropy from bytes in a newly created UUID.
   */
  public static long getPseudoInt64()
  {
    val guid = UUID.randomUUID();
    return HashingUtils.uuidToInt64(guid);
  }

  /**
   * Returns random bytes, created by the information entropy from bytes in a newly created Guid.
   * 
   * @throws IllegalArgumentException An argument is out of range
   */
  public static byte[] getPseudoBytes(final int length)
  {
    if (length < 0)
      throw new IllegalArgumentException("length=" + length);
    if (length == 0)
      return new byte[0];

    val result = new byte[length];
    int index = 0;

    while (true)
    {
      val randomUuid = UUID.randomUUID();
      val random = ByteArrayUtils.getBytes(randomUuid);

      for (int i = 0; i < 16; i++)
      {
        if (index < result.length)
          result[index++] = random[i];
        else
          return result;
      }
    }
  }

  /**
   * Returns random text characters (uses the Base64 set). Randomness sourced from the information entropy of a new UUID.
   * 
   * @throws IllegalArgumentException An argument is out of range.
   */
  public static String getPseudoBase64Text(final int length)
  {
    if (length < 0)
      throw new IllegalArgumentException("length=" + length);
    if (length == 0)
      return CONSTANT.EMPTY_STRING;

    val sb = new StringBuilder(length + 32);
    while (sb.length() < length)
      sb.append(ConversionUtils.toBase64(getPseudoInt64()));

    return sb.substring(0, length);
  }

  /**
   * Returns random text characters (uses the 0..9, A..Z and a..z character sets). Randomness sourced from the information entropy of a new
   * UUID.
   * 
   * @throws IllegalArgumentException An argument is out of range.
   */
  public static String getPseudoAlphanumericText(final int length)
  {
    if (length < 0)
      throw new IllegalArgumentException("length=" + length);
    if (length == 0)
      return CONSTANT.EMPTY_STRING;

    val sb = new StringBuilder(length + 32);

    while (sb.length() < length)
    {
      val base64 = ConversionUtils.toBase64(getPseudoInt64());
      for (char ch : base64.toCharArray())
        if (Character.isLetterOrDigit(ch))
          sb.append(ch);
    }

    return sb.substring(0, length);
  }

  /**
   * Randomizes elements in an array.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of range.
   */
  @Validate
  public static <T> void pseudoRandomize(@NotNull final T[] data, final int start, final int end)
  {
    if (start < 0 || start >= data.length || start > end)
      throw new IndexOutOfBoundsException("start=" + start + " end=" + end + " dataLen=" + data.length);
    if (end < 0 || end > data.length)
      throw new IndexOutOfBoundsException("end=" + end + " dataLen=" + data.length);

    val from = new int[end - start];
    val to = new int[end - start];
    for (int i = start; i < end; i++)
    {
      from[i] = PSEUDO_RANDOM.nextInt(end - start) + start;
      to[i] = PSEUDO_RANDOM.nextInt(end - start) + start;
    }

    Linq.swap(data, from, to);
  }

  /**
   * Randomizes elements in an array.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of range.
   */
  @Validate
  public static <T> void pseudoRandomize(@NotNull final List<T> data, final int start, final int end)
  {
    if (start < 0 || start >= data.size() || start > end)
      throw new IndexOutOfBoundsException("start=" + start + " end=" + end + " dataLen=" + data.size());
    if (end < 0 || end > data.size())
      throw new IndexOutOfBoundsException("end=" + end + " dataLen=" + data.size());

    val from = new int[end - start];
    val to = new int[end - start];
    for (int i = start; i < end; i++)
    {
      from[i] = PSEUDO_RANDOM.nextInt(end - start) + start;
      to[i] = PSEUDO_RANDOM.nextInt(end - start) + start;
    }

    Linq.swap(data, from, to);
  }

  /**
   * Returns a cryptographically secure random Int32.
   */
  public static int getSecureInt32()
  {
    val buffer = getSecureBytes(4); // size of int
    return ByteArrayUtils.toInt32(buffer);
  }

  /**
   * Returns a cryptographically secure random Int32.
   */
  public static long getSecureInt64()
  {
    val buffer = getSecureBytes(8); // size of long
    return ByteArrayUtils.toInt64(buffer);
  }

  /**
   * Returns a key filled with cryptographically secure random data.
   * 
   * @throws IllegalArgumentException An argument is out of range
   */
  public static byte[] getSecureBytes(final int length)
  {
    if (length < 0)
      throw new IllegalArgumentException("length=" + length);
    if (length == 0)
      return new byte[0];

    val buffer = new byte[length];
    CRYPTO_RANDOM.nextBytes(buffer);
    return buffer;
  }

  /**
   * Returns random text characters (uses the Base64 set), created by a cryptographically secure source.
   * 
   * @throws IllegalArgumentException An argument is out of range
   */
  public static String getSecureBase64Text(final int length)
  {
    if (length < 0)
      throw new IllegalArgumentException("length=" + length);
    if (length == 0)
      return CONSTANT.EMPTY_STRING;

    val sb = new StringBuilder(length + 32);
    while (sb.length() < length)
      sb.append(ConversionUtils.toBase64(getSecureInt64()));

    return sb.substring(0, length);
  }

  /**
   * Returns random text characters (uses the 0..9, A..Z and a..z character sets) created by a cryptographically secure source.
   * 
   * @throws IllegalArgumentException An argument is out of range
   */
  public static String getSecureAlphanumericText(final int length)
  {
    if (length < 0)
      throw new IllegalArgumentException("length=" + length);
    if (length == 0)
      return CONSTANT.EMPTY_STRING;

    val sb = new StringBuilder(length + 32);

    while (sb.length() < length)
    {
      String base64 = ConversionUtils.toBase64(getSecureInt64());
      for (char ch : base64.toCharArray())
        if (Character.isLetterOrDigit(ch))
          sb.append(ch);
    }

    return sb.substring(0, length);
  }

  private RandomUtils()
  {
  }
}
