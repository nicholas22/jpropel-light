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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.Predicate;
import lombok.Validate;
import lombok.Validate.NotNull;
import lombok.val;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import propel.core.collections.maps.avl.AvlHashtable;
import propel.core.common.CONSTANT;
import propel.core.userTypes.Int128;
import propel.core.userTypes.SignedByte;
import propel.core.userTypes.UnsignedByte;
import propel.core.userTypes.UnsignedInteger;
import propel.core.userTypes.UnsignedLong;
import propel.core.userTypes.UnsignedShort;

/**
 * Class aiding in casting, encoding/decoding and converting
 */
public final class ConversionUtils
{
  /**
   * Used to convert Durations into milliseconds
   */
  private static final Calendar ZERO_AD = new GregorianCalendar(0, 0, 0, 0, 0, 0);

  /**
   * Packs two 32-bit integers into a 64-bit long (a,b => ab)
   */
  public static long pack64(int a, int b)
  {
    long c1 = (((long) a) << 32);
    long c2 = ((long) b) & 0xFFFFFFFFL;
    long num = c1 | c2;
    return num;
  }

  /**
   * Reversed the pack64() function
   */
  public static int[] unpack64(long num)
  {
    int a = (int) (num >>> 32);
    int b = (int) num;

    return new int[] {a, b};
  }

  /**
   * Conversion utility method, allows for a C# unsigned byte to be converted to a Java signed byte.
   */
  public static byte byteDotNetToJvm(int i)
  {
    if (i > 127)
    {
      return (byte) (i - 256);
    } else
    {
      return (byte) i;
    }
  }

  /**
   * Conversion utility method, allows for a Java signed byte to be converted to a C# unsigned byte.
   */
  public static int byteJvmToDotNet(byte b)
  {
    return b & 0xFF;
  }

  /**
   * .NET-style Convert.ChangeType functionality. Works for most built-in types. Attempts to use casting for others.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An incompatible argument was passed (e.g. an array or annotation)
   * @throws NumberFormatException An invalid input value was given that does not parse to the specified target type
   * @throws ClassCastException A class cast failed
   */
  @Validate
  @SuppressWarnings("unchecked")
  public static <T> T changeType(@NotNull final Object value, @NotNull final Class<T> targetType)
      throws InstantiationException, IllegalAccessException
  {
    val sourceType = value.getClass();

    // check that we're not working with incompatible types
    if (sourceType.isAnnotation() || targetType.isAnnotation())
      throw new IllegalArgumentException("This conversion process does not support annotations.");
    if (sourceType.isArray() || targetType.isArray())
      throw new IllegalArgumentException("This conversion process does not support arrays.");

    // handle conversion to String
    if (targetType.equals(String.class))
      return (T) value.toString();

    // handle parsing from String
    if (sourceType.equals(String.class))
      return (T) changeTypeParseFromString(value, targetType);

    // handle simple casting scenario
    if (sourceType.isAssignableFrom(targetType))
      return targetType.cast(value);

    // handle number conversion
    if (value instanceof Number)
      return (T) changeTypeFromNumber(value, targetType);

    // handle primitive conversion
    PrimitiveType primitiveType = ReflectionUtils.getPrimitiveType(sourceType);
    if (primitiveType != PrimitiveType.NotPrimitive)
      return (T) changeTypeFromPrimitive(value, primitiveType, targetType);

    // handle number-like Character and Boolean conversion (these don't implement Number)
    if (value instanceof Character)
      return (T) changeTypeFromCharacter(value, targetType);
    if (value instanceof Boolean)
      return (T) changeTypeFromBoolean(value, targetType);

    // all attempts have failed
    throw new IllegalArgumentException("The provided object of type '" + sourceType.getSimpleName() + "' could not be converted to '"
        + targetType.getSimpleName());
  }

  // JODA, XML data types, etc.

  /**
   * Converts an XML Gregorian Calendar data type to a Joda LocalDateTime
   */
  @Validate
  public static LocalDateTime fromXMLGregorianCalendar(@NotNull final XMLGregorianCalendar value)
  {
    return new LocalDateTime(value.getYear(), value.getMonth(), value.getDay(), value.getHour(), value.getMinute(), value.getSecond(),
        value.getMillisecond());
  }

  /**
   * Converts an XML Duration data type to a Joda Duration
   */
  @Validate
  public static Duration fromXMLDuration(@NotNull final javax.xml.datatype.Duration value)
  {
    return new Duration(value.getTimeInMillis(ZERO_AD));
  }

  /**
   * Converts a Joda LocalDateTime object to an XML Gregorian Calendar data type
   */
  @Validate
  public static XMLGregorianCalendar toXMLGregorianCalendar(@NotNull final LocalDateTime value)
      throws DatatypeConfigurationException
  {
    val gc = new GregorianCalendar(value.getYear(), value.getMonthOfYear() - 1, value.getDayOfMonth(), value.getHourOfDay(),
        value.getMinuteOfHour(), value.getSecondOfMinute());
    gc.set(Calendar.MILLISECOND, value.getMillisOfSecond());

    return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
  }

  /**
   * Converts a Joda Duration object to an XML Duration data type
   */
  @Validate
  public static javax.xml.datatype.Duration toXMLDuration(@NotNull final Duration value)
      throws DatatypeConfigurationException
  {
    return DatatypeFactory.newInstance().newDuration(value.getMillis());
  }

  // BINARY conversions

  /**
   * Converts a number to binary, e.g. 255 -> 11111111. Binary representations can be endian dependent.
   */
  @Validate
  public static String toBinary(@NotNull final UnsignedLong num, boolean padLeft)
  {
    return !padLeft ? toBinary(num) : StringUtils.padLeft(toBinary(num), 64, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a number to binary, e.g. 255 -> 11111111. Binary representations can be endian dependent.
   */
  public static String toBinary(UnsignedLong num)
  {
    return num.bigIntegerValue().toString(2);
  }

  /**
   * Converts a number to binary, e.g. 255 -> 11111111. Binary representations can be endian dependent.
   */
  public static String toBinary(long num, boolean padLeft)
  {
    return !padLeft ? toBinary(num) : StringUtils.padLeft(toBinary(num), 64, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a number to binary, e.g. 255 -> 11111111. Binary representations can be endian dependent.
   */
  public static String toBinary(long num)
  {
    return Long.toBinaryString(num);
  }

  /**
   * Converts a number to binary, e.g. 255 -> 11111111. Binary representations can be endian dependent.
   */
  @Validate
  public static String toBinary(@NotNull final UnsignedInteger num, boolean padLeft)
  {
    return !padLeft ? toBinary(num) : StringUtils.padLeft(toBinary(num), 32, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a number to binary, e.g. 255 -> 11111111. Binary representations can be endian dependent.
   */
  @Validate
  public static String toBinary(@NotNull final UnsignedInteger num)
  {
    return num.bigIntegerValue().toString(2);
  }

  /**
   * Converts a number to binary, e.g. 255 -> 11111111. Binary representations can be endian dependent.
   */
  public static String toBinary(int num, boolean padLeft)
  {
    return !padLeft ? toBinary(num) : StringUtils.padLeft(toBinary(num), 32, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a number to binary, e.g. 255 -> 11111111. Binary representations can be endian dependent.
   */
  public static String toBinary(int num)
  {
    return Integer.toBinaryString(num);
  }

  /**
   * Converts a number to binary, e.g. 255 -> 11111111. Binary representations can be endian dependent.
   */
  @Validate
  public static String toBinary(@NotNull final UnsignedShort num, boolean padLeft)
  {
    return !padLeft ? toBinary(num) : StringUtils.padLeft(toBinary(num), 16, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a number to binary, e.g. 255 -> 11111111. Binary representations can be endian dependent.
   */
  public static String toBinary(UnsignedShort num)
  {
    return num.bigIntegerValue().toString(2);
  }

  /**
   * Converts a number to binary, e.g. 255 -> 11111111. Binary representations can be endian dependent.
   */
  public static String toBinary(short num, boolean padLeft)
  {
    return !padLeft ? toBinary(num) : StringUtils.padLeft(toBinary(num), 16, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a number to binary, e.g. 255 -> 11111111. Binary representations can be endian dependent.
   */
  public static String toBinary(short num)
  {
    return Integer.toBinaryString(num);
  }

  /**
   * Converts a number to binary, e.g. 255 -> 11111111. Binary representations can be endian dependent.
   */
  @Validate
  public static String toBinary(@NotNull final UnsignedByte num, boolean padLeft)
  {
    return !padLeft ? toBinary(num) : StringUtils.padLeft(toBinary(num), 8, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a number to binary, e.g. 255 -> 11111111. Binary representations can be endian dependent.
   */
  public static String toBinary(UnsignedByte num)
  {
    return num.bigIntegerValue().toString(2);
  }

  /**
   * Converts a number to binary, e.g. 255 -> 11111111. Binary representations can be endian dependent.
   */
  public static String toBinary(byte num, boolean padLeft)
  {
    return !padLeft ? toBinary(num) : StringUtils.padLeft(toBinary(num), 8, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a number to binary
   */
  public static String toBinary(byte num)
  {
    return Integer.toBinaryString(num);
  }

  /**
   * Converts a byte array to a binary string. Binary representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static String toBinary(@NotNull final byte[] bytes)
  {
    val sb = new StringBuilder(bytes.length * 8);
    for (byte b : bytes)
      sb.append(toBinary(b, true));

    return sb.toString();
  }

  /**
   * Converts a byte array to a binary string. Binary representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds.
   * @throws IllegalArgumentException An argument is out of range.
   */
  @Validate
  public static String toBinary(@NotNull final byte[] bytes, int startIndex, int length)
  {
    if (startIndex < 0 || startIndex > bytes.length)
      throw new IndexOutOfBoundsException("startIndex=" + startIndex + " bytesLen=" + bytes.length);
    if (length < 0 || length + startIndex > bytes.length || length + startIndex < 0)
      throw new IllegalArgumentException("startIndex=" + startIndex + " length=" + length + " bytesLen=" + bytes.length);

    StringBuilder sb = new StringBuilder(bytes.length * 8);
    for (int i = startIndex; i < startIndex + length; i++)
      sb.append(toBinary(bytes[i], true));

    return sb.toString();
  }

  /**
   * Converts a binary back to an int. Binary representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static int fromBinaryToInt32(@NotNull final String binary)
  {
    return Integer.parseInt(binary, 2);
  }

  /**
   * Converts a binary back to an uint. Binary representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedInteger fromBinaryToUInt32(@NotNull final String binary)
  {
    return new UnsignedInteger(new BigInteger(binary, 2));
  }

  /**
   * Converts a binary back to a long. Binary representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static long fromBinaryToInt64(@NotNull final String binary)
  {
    return Long.parseLong(binary, 2);
  }

  /**
   * Converts a binary back to a ulong. Binary representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedLong fromBinaryToUInt64(@NotNull final String binary)
  {
    return new UnsignedLong(new BigInteger(binary, 2));
  }

  /**
   * Converts a binary back to a short. Binary representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static short fromBinaryToInt16(@NotNull final String binary)
  {
    return Short.parseShort(binary, 2);
  }

  /**
   * Converts a binary back to a ushort. Binary representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedShort fromBinaryToUInt16(@NotNull final String binary)
  {
    return new UnsignedShort(new BigInteger(binary, 2));
  }

  /**
   * Converts a binary back to a byte. Binary representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static byte fromBinaryToByte(@NotNull final String binary)
  {
    return Byte.parseByte(binary, 2);
  }

  /**
   * Converts a binary back to an unsigned byte. Binary representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedByte fromBinaryToUInt8(@NotNull final String binary)
  {
    return new UnsignedByte(new BigInteger(binary, 2));
  }

  /**
   * Converts a binary back to a byte[]. Binary representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static byte[] fromBinaryToByteArray(@NotNull final String binary)
  {
    return fromBinaryToByteArray(binary, 0, binary.length());
  }

  /**
   * Converts a binary back to a byte[]. Binary representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static byte[] fromBinaryToByteArray(@NotNull final String binary, int startIndex, int length)
  {
    if (startIndex < 0 || startIndex > binary.length())
      throw new IndexOutOfBoundsException("startIndex=" + startIndex + " binaryLen=" + binary.length());
    if (length < 0 || length + startIndex > binary.length() || length + startIndex < 0)
      throw new IllegalArgumentException("startIndex=" + startIndex + " length=" + length + " binaryLen=" + binary.length());
    if (binary.length() == 0 || binary.length() % 8 != 0)
      throw new IllegalArgumentException("The binary string should use 8 digits per byte: " + binary.length());

    byte[] result = new byte[length / 8];

    for (int i = startIndex; i < startIndex + length; i += 8)
      result[i / 8 - startIndex / 8] = fromBinaryToByte(binary.substring(i, 8));

    return result;
  }

  // DECIMAL conversions

  /**
   * Converts a string back to a uint
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedInteger fromStringToUInt32(@NotNull final String value)
  {
    return new UnsignedInteger(value);
  }

  /**
   * Converts a string back to an int
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static int fromStringToInt32(@NotNull final String value)
  {
    return Integer.parseInt(value, 10);
  }

  /**
   * Converts a string back to a ulong
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedLong fromStringToUInt64(@NotNull final String value)
  {
    return new UnsignedLong(value);
  }

  /**
   * Converts a string back to a long
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static long fromStringToInt64(@NotNull final String value)
  {
    return Long.parseLong(value, 10);
  }

  /**
   * Converts a string back to a ushort
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedShort fromStringToUInt16(@NotNull final String value)
  {
    return new UnsignedShort(value);
  }

  /**
   * Converts a string back to a short
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static short fromStringToInt16(@NotNull final String value)
  {
    return Short.parseShort(value, 10);
  }

  /**
   * Converts a string back to an unsigned byte
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedByte fromStringToUInt8(@NotNull final String value)
  {
    return new UnsignedByte(value);
  }

  /**
   * Converts a string back to a byte
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static byte fromStringToByte(@NotNull final String value)
  {
    return Byte.parseByte(value, 10);
  }

  // HEX conversions

  /**
   * Converts a number to hex, e.g. 255 -> FF. Hex representations can be endian dependent.
   */
  @Validate
  public static String toHex(@NotNull final UnsignedLong num)
  {
    return num.bigIntegerValue().toString(16);
  }

  /**
   * Converts a number to hex, e.g. 255 -> FF Hex representations can be endian dependent.
   */
  @Validate
  public static String toHex(@NotNull final UnsignedLong num, boolean padLeft)
  {
    return !padLeft ? num.bigIntegerValue().toString(16) : StringUtils.padLeft(num.bigIntegerValue().toString(16), 16, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a number to hex, e.g. 255 -> FF. Hex representations can be endian dependent.
   */
  public static String toHex(long num)
  {
    return Long.toHexString(num);
  }

  /**
   * Converts a number to hex, e.g. 255 -> FF Hex representations can be endian dependent.
   */
  public static String toHex(long num, boolean padLeft)
  {
    return !padLeft ? Long.toHexString(num) : StringUtils.padLeft(Long.toHexString(num), 16, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a number to hex, e.g. 255 -> FF. Hex representations can be endian dependent.
   */
  @Validate
  public static String toHex(@NotNull final UnsignedInteger num)
  {
    return num.bigIntegerValue().toString(16);
  }

  /**
   * Converts a number to hex, e.g. 255 -> FF Hex representations can be endian dependent.
   */
  @Validate
  public static String toHex(@NotNull final UnsignedInteger num, boolean padLeft)
  {
    return !padLeft ? num.bigIntegerValue().toString(16) : StringUtils.padLeft(num.bigIntegerValue().toString(16), 8, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a number to hex, e.g. 255 -> FF Hex representations can be endian dependent.
   */
  public static String toHex(int num)
  {
    return Integer.toHexString(num);
  }

  /**
   * Converts a number to hex, e.g. 255 -> FF Hex representations can be endian dependent.
   */
  public static String toHex(int num, boolean padLeft)
  {
    return !padLeft ? Integer.toHexString(num) : StringUtils.padLeft(Integer.toHexString(num), 8, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a number to hex, e.g. 255 -> FF. Hex representations can be endian dependent.
   */
  @Validate
  public static String toHex(@NotNull final UnsignedShort num)
  {
    return num.bigIntegerValue().toString(16);
  }

  /**
   * Converts a number to hex, e.g. 255 -> FF Hex representations can be endian dependent.
   */
  @Validate
  public static String toHex(@NotNull final UnsignedShort num, boolean padLeft)
  {
    return !padLeft ? num.bigIntegerValue().toString(16) : StringUtils.padLeft(num.bigIntegerValue().toString(16), 4, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a number to hex, e.g. 255 -> FF Hex representations can be endian dependent.
   */
  public static String toHex(short num)
  {
    return Integer.toHexString(num);
  }

  /**
   * Converts a number to hex, e.g. 255 -> FF Hex representations can be endian dependent.
   */
  public static String toHex(short num, boolean padLeft)
  {
    return !padLeft ? Integer.toHexString(num) : StringUtils.padLeft(Integer.toHexString(num), 4, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a number to hex, e.g. 255 -> FF. Hex representations can be endian dependent.
   */
  @Validate
  public static String toHex(@NotNull final UnsignedByte num)
  {
    return num.bigIntegerValue().toString(16);
  }

  /**
   * Converts a number to hex, e.g. 255 -> FF Hex representations can be endian dependent.
   */
  @Validate
  public static String toHex(@NotNull final UnsignedByte num, boolean padLeft)
  {
    return !padLeft ? num.bigIntegerValue().toString(16) : StringUtils.padLeft(num.bigIntegerValue().toString(16), 2, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a number to hex, e.g. 255 -> FF Hex representations can be endian dependent.
   */
  public static String toHex(byte num)
  {
    return toHex(new byte[] {num});
  }

  /**
   * Converts a number to hex, e.g. 255 -> FF Hex representations can be endian dependent.
   */
  public static String toHex(byte num, boolean padLeft)
  {
    return !padLeft ? toHex(new byte[] {num}) : StringUtils.padLeft(toHex(new byte[] {num}), 2, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a byte array to hex Hex representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static String toHex(@NotNull final byte[] ba)
  {
    return toHex(ba, 0, ba.length);
  }

  /**
   * Converts a byte array to hex Hex representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds.
   * @throws IllegalArgumentException An argument is out of range.
   */
  @Validate
  public static String toHex(@NotNull final byte[] ba, int offset, int length)
  {
    if (offset < 0 || offset > ba.length)
      throw new IndexOutOfBoundsException("offset=" + offset + " baLen=" + ba.length);
    if (length < 0 || offset + length > ba.length || offset + length < 0)
      throw new IndexOutOfBoundsException("offset=" + offset + " length=" + length + " baLen=" + ba.length);

    final String HEX = "0123456789ABCDEF";
    StringBuilder hex = new StringBuilder(2 * ba.length);

    for (int i = offset; i < offset + length; i++)
      hex.append(HEX.charAt((ba[i] & 0xF0) >> 4)).append(HEX.charAt((ba[i] & 0x0F)));

    return hex.toString();
  }

  /**
   * Converts a hex string to a byte array Hex representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws NumberFormatException A number could not be parsed.
   */
  public static byte[] fromHexToByteArray(String hex)
  {
    return fromHexToByteArray(hex, 0, hex.length());
  }

  /**
   * Converts a hex string to a byte array Hex representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static byte[] fromHexToByteArray(@NotNull final String hex, int startIndex, int length)
  {
    if (startIndex < 0 || startIndex > hex.length())
      throw new IndexOutOfBoundsException("startIndex=" + startIndex + " hexLen=" + hex.length());
    if (length < 0 || length + startIndex > hex.length() || length + startIndex < 0)
      throw new IllegalArgumentException("startIndex=" + startIndex + " length=" + length + " hexLen=" + hex.length());
    if (length == 0 || length % 2 != 0)
      throw new IllegalArgumentException("The hex string should use two digits per byte: " + hex.length());

    byte[] result = new byte[hex.length() / 2];
    for (int i = 0; i < result.length; i++)
    {
      int index = i * 2;
      int v = Integer.parseInt(hex.substring(index, index + 2), 16);
      result[i] = (byte) v;
    }
    return result;
  }

  /**
   * Converts a hex back to a ulong Hex representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedLong fromHexToUInt64(@NotNull final String hex)
  {
    return new UnsignedLong(new BigInteger(hex, 16));
  }

  /**
   * Converts a hex back to a long Hex representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static long fromHexToInt64(@NotNull final String hex)
  {
    return Long.parseLong(hex, 16);
  }

  /**
   * Converts a hex back to a uint Hex representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedInteger fromHexToUInt32(@NotNull final String hex)
  {
    return new UnsignedInteger(new BigInteger(hex, 16));
  }

  /**
   * Converts a hex back to an int Hex representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static int fromHexToInt32(@NotNull final String hex)
  {
    return Integer.parseInt(hex, 16);

  }

  /**
   * Converts a hex back to a ushort Hex representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedInteger fromHexToUInt16(@NotNull final String hex)
  {
    return new UnsignedInteger(new BigInteger(hex, 16));
  }

  /**
   * Converts a hex back to a short Hex representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  public static short fromHexToInt16(@NotNull final String hex)
  {
    int i = Integer.parseInt(hex, 16);
    if (i < Short.MIN_VALUE || i > Short.MAX_VALUE)
      throw new NumberFormatException("The hex string '" + hex + "' cannot be represented as a Short.");

    return (short) i;
  }

  /**
   * Converts a hex back to a ushort Hex representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedByte fromHexToUInt8(@NotNull final String hex)
  {
    return new UnsignedByte(new BigInteger(hex, 16));
  }

  /**
   * Converts a hex back to a byte Hex representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static byte fromHexToByte(@NotNull final String hex)
  {
    int i = Integer.parseInt(hex, 16);
    if (i < Byte.MIN_VALUE || i > Byte.MAX_VALUE)
      throw new NumberFormatException("The hex string '" + hex + "' cannot be represented as a Byte.");

    return (byte) i;
  }

  // BASE64 conversions

  /**
   * Converts a number to Base64. Base64 representations can be endian dependent.
   */
  public static String toBase64(UnsignedLong num)
  {
    return Base64.encodeBytes(ByteArrayUtils.getBytes(num));
  }

  /**
   * Converts a number to Base64. Base64 representations can be endian dependent.
   */
  public static String toBase64(long num)
  {
    return Base64.encodeBytes(ByteArrayUtils.getBytes(num));
  }

  /**
   * Converts a number to Base64. Base64 representations can be endian dependent.
   */
  public static String toBase64(UnsignedInteger num)
  {
    return Base64.encodeBytes(ByteArrayUtils.getBytes(num));
  }

  /**
   * Converts a number to Base64. Base64 representations can be endian dependent.
   */
  public static String toBase64(int num)
  {
    return Base64.encodeBytes(ByteArrayUtils.getBytes(num));
  }

  /**
   * Converts a number to Base64. Base64 representations can be endian dependent.
   */
  public static String toBase64(UnsignedShort num)
  {
    return Base64.encodeBytes(ByteArrayUtils.getBytes(num));
  }

  /**
   * Converts a number to Base64. Base64 representations can be endian dependent.
   */
  public static String toBase64(short num)
  {
    return Base64.encodeBytes(ByteArrayUtils.getBytes(num));
  }

  /**
   * Converts a number to Base64. Base64 representations can be endian dependent.
   */
  public static String toBase64(byte num)
  {
    return Base64.encodeBytes(new byte[] {num});
  }

  /**
   * Converts a byte array to Base64. Base64 representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String toBase64(byte[] array)
  {
    return Base64.encodeBytes(array);
  }

  /**
   * Converts a byte array to Base64. Base64 representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds.
   * @throws IllegalArgumentException An argument is out of range.
   */
  @Validate
  public static String toBase64(@NotNull final byte[] array, int offset, int length)
  {
    return Base64.encodeBytes(array, offset, length);
  }

  /**
   * Converts a Base64 back to a long. Base64 representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static long fromBase64ToInt64(@NotNull final String base64)
  {
    try
    {
      return ByteArrayUtils.toInt64(Base64.decode(base64));
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The base64 string '" + base64 + "' could not be parsed as a Long: " + e.getMessage());
    }
  }

  /**
   * Converts a Base64 back to a ulong. Base64 representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedLong fromBase64ToUInt64(@NotNull final String base64)
  {
    try
    {
      return ByteArrayUtils.toUInt64(Base64.decode(base64));
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The base64 string '" + base64 + "' could not be parsed as a ULong: " + e.getMessage());
    }
  }

  /**
   * Converts a Base64 back to an int. Base64 representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static int fromBase64ToInt32(@NotNull final String base64)
  {
    try
    {
      return ByteArrayUtils.toInt32(Base64.decode(base64));
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The base64 string '" + base64 + "' could not be parsed as an Int: " + e.getMessage());
    }
  }

  /**
   * Converts a Base64 back to a uint. Base64 representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedInteger fromBase64ToUInt32(@NotNull final String base64)
  {
    try
    {
      return ByteArrayUtils.toUInt32(Base64.decode(base64));
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The base64 string '" + base64 + "' could not be parsed as a UInt: " + e.getMessage());
    }
  }

  /**
   * Converts a Base64 back to a short. Base64 representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static short fromBase64ToInt16(@NotNull final String base64)
  {
    try
    {
      return ByteArrayUtils.toInt16(Base64.decode(base64));
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The base64 string '" + base64 + "' could not be parsed as an Short: " + e.getMessage());
    }
  }

  /**
   * Converts a Base64 back to a ushort. Base64 representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedShort fromBase64ToUInt16(@NotNull final String base64)
  {
    try
    {
      return ByteArrayUtils.toUInt16(Base64.decode(base64));
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The base64 string '" + base64 + "' could not be parsed as an Short: " + e.getMessage());
    }
  }

  /**
   * Converts a Base64 back to a byte. Base64 representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static byte fromBase64ToByte(@NotNull final String base64)
  {
    try
    {
      short s = ByteArrayUtils.toInt16(Base64.decode(base64));
      if (s < Byte.MIN_VALUE || s > Byte.MAX_VALUE)
        throw new Exception("Byte value is out of valid range.");

      return (byte) s;
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The base64 string '" + base64 + "' could not be parsed as a Byte: " + e.getMessage());
    }
  }

  /**
   * Converts a Base64 representation to a byte array. Base64 representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static byte[] fromBase64ToByteArray(@NotNull final String base64)
  {
    try
    {
      return Base64.decode(base64);
    }
    catch(Throwable e)
    {
      throw new IllegalArgumentException("The specified base64 string could not be decoded.", e);
    }
  }

  /**
   * Converts a Base64 representation to a byte array. Base64 representations can be endian dependent.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of range.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static byte[] fromBase64ToByteArray(@NotNull final String base64, int startIndex, int length)
  {
    if (startIndex < 0 || startIndex > base64.length())
      throw new IndexOutOfBoundsException("startIndex=" + startIndex + " baseLen=" + base64.length());
    if (length < 0 || startIndex + length > base64.length() || startIndex + length < 0)
      throw new IllegalArgumentException("startIndex=" + startIndex + " length=" + length + " base64Len=" + base64.length());

    try
    {
      return Base64.decode(base64.substring(startIndex, startIndex + length));
    }
    catch(Throwable e)
    {
      throw new IllegalArgumentException("The specified base64 string could not be decoded.", e);
    }
  }

  // Alphanumeric conversions

  /**
   * Converts a number to alphanumeric, e.g. 0 -> 0, 16 -> G, 61 -> z, 62 -> 10. Only works for positive numbers.
   */
  @Validate
  public static String toAlphanumeric(@NotNull final UnsignedLong num)
  {
    if (num.bigIntegerValue().equals(BigInteger.ZERO))
      return CONSTANT.ZERO;

    StringBuilder sb = new StringBuilder(16);

    BigInteger sixtyTwo = new BigInteger("62");
    BigInteger value = num.bigIntegerValue();

    // this algorithm gives us the alphanumeric number in reverse order
    while (value.compareTo(BigInteger.ZERO) > 0)
    {
      int remainder = value.mod(sixtyTwo).intValue();
      sb.append(CONSTANT.ALPHANUMERIC_DIGITS[remainder]);
      value = value.divide(sixtyTwo);
    }

    // reverse result
    return StringUtils.reverse(sb.toString());
  }

  /**
   * Converts a number to alphanumeric, e.g. 0 -> 0, 16 -> G, 61 -> z, 62 -> 10
   */
  @Validate
  public static String toAlphanumeric(@NotNull final UnsignedLong num, boolean padLeft)
  {
    return !padLeft ? toAlphanumeric(num) : StringUtils.padLeft(toAlphanumeric(num), 11, CONSTANT.ZERO_CHAR);
  }

  /**
   * Converts a byte array to alphanumeric
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static String toAlphanumeric(@NotNull final byte[] ba)
  {
    return toAlphanumeric(ba, 0, ba.length);
  }

  /**
   * Converts a byte array to alphanumeric
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds.
   * @throws IllegalArgumentException An argument is out of range.
   */
  @Validate
  public static String toAlphanumeric(@NotNull final byte[] ba, int offset, int length)
  {
    if (offset < 0 || offset > ba.length)
      throw new IndexOutOfBoundsException("offset=" + offset + " baLen=" + ba.length);
    if (length < 0 || length + offset > ba.length || length + offset < 0)
      throw new IllegalArgumentException("length=" + length + " offset=" + offset + " baLen=" + ba.length);
    if (length == 0 || length % 8 != 0)
      throw new IllegalArgumentException("Length should be divisible by 8: " + length);

    // convert blocks to alphanumerics
    StringBuilder sb = new StringBuilder(16);
    for (int i = offset; i < offset + length; i += 8)
    {
      byte[] ulBytes = ByteArrayUtils.subarray(ba, i, 8);
      UnsignedLong num = ByteArrayUtils.toUInt64(ulBytes);
      sb.append(toAlphanumeric(num, true));
    }

    return sb.toString();
  }

  /**
   * Converts an alphanumeric string to a byte array
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static byte[] fromAlphanumericToByteArray(@NotNull final String alphanumeric)
  {
    return fromAlphanumericToByteArray(alphanumeric, 0, alphanumeric.length());
  }

  /**
   * Converts an alphanumeric string to a byte array
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds.
   * @throws IllegalArgumentException An argument is out of range.
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static byte[] fromAlphanumericToByteArray(@NotNull final String alphanumeric, int startIndex, int length)
  {
    if (startIndex < 0 || startIndex > alphanumeric.length())
      throw new IndexOutOfBoundsException("startIndex=" + startIndex + " alphaLen=" + alphanumeric.length());
    if (length < 0 || length + startIndex > alphanumeric.length() || length + startIndex < 0)
      throw new IllegalArgumentException("length=" + length + " startIndex=" + startIndex + " alphaLen=" + alphanumeric.length());
    if (length == 0 || alphanumeric.length() % 11 != 0)
      throw new NumberFormatException("The alphanumeric string should use 11 digits per word: " + alphanumeric.length());

    byte[] result = new byte[alphanumeric.length() / 11 * 8];

    int j = 0;
    for (int i = 0; i < alphanumeric.length(); i += 11)
    {
      UnsignedLong num = fromAlphanumericToUInt64(alphanumeric.substring(i, i + 11));
      byte[] bytes = ByteArrayUtils.getBytes(num);
      System.arraycopy(bytes, 0, result, j, 8);
      j += 8;
    }

    return result;
  }

  /**
   * Converts an alphanumeric back to a ulong
   * 
   * @throws NullPointerException An argument is null
   * @throws NumberFormatException A number could not be parsed.
   */
  @Validate
  public static UnsignedLong fromAlphanumericToUInt64(@NotNull final String alphanumeric)
  {
    if (alphanumeric.equals(CONSTANT.ZERO))
      return new UnsignedLong(BigInteger.ZERO);

    UnsignedLong result = new UnsignedLong(BigInteger.ZERO);
    BigInteger sixtyTwo = new BigInteger("62");

    int len = alphanumeric.length();
    for (int i = 0; i < len; i++)
    {
      char ch = alphanumeric.charAt(i);
      if (!StringUtils.contains(CONSTANT.ALPHANUMERIC_DIGITS, ch))
        throw new NumberFormatException("The digit does not belong to the alphanumeric number structure: " + ch);

      // add and check for overflows
      int pos = len - i - 1;
      int charIndex = StringUtils.indexOf(CONSTANT.ALPHANUMERIC_DIGITS, ch);
      BigInteger added = sixtyTwo.pow(pos).multiply(new BigInteger(charIndex + ""));
      result = new UnsignedLong(result.bigIntegerValue().add(added));
    }

    return result;
  }

  /*
   * TODO: implement these
   * 
   * // Human-readable values // TODO: these are locale sensitive date formats
   * 
   * /// <summary> /// Built-in date/time ToString styles. /// </summary> public enum DateTimeStyle { /// <summary> /// Displays e.g.
   * 15/04/2008 for en-GB, 04/15/2008 for en-US, 4/15/2008 for en-NZ, 15.04.2008 for de-DE, etc. /// </summary> ShortDate, /// <summary> ///
   * Displays e.g. Thursday, April 10, 2008 for en-GB, quinta-feira, 10 de abril de 2008 for pt-BR, jueves, 10 de abril de 2008 for es-MX
   * /// </summary> LongDate, /// <summary> /// Displays e.g. Thursday, April 10, 2008 6:30 AM for en-US, jeudi 10 avril 2008 06:30 for
   * fr-FR /// </summary> FullDateShortTime, /// <summary> /// Displays e.g. Thursday, April 10, 2008 6:30:00 AM for en-US, jeudi 10 avril
   * 2008 06:30:00 for fr-FR /// </summary> FullDateLongTime, /// <summary> /// Displays e.g. 4/10/2008 6:30 AM for en-US, 10/04/2008 6:30
   * for fr-BE /// </summary> GeneralDateShortTime, /// <summary> /// Displays e.g. 4/10/2008 6:30:00 AM for en-US, 10/04/2008 6:30:00 for
   * nl-BE /// </summary> GeneralDateLongTime, /// <summary> /// Displays e.g. April 10 for en-US, 10 April for ms-MY /// </summary>
   * MonthDay, /// <summary> /// Displays e.g. 2008-04-10T06:30:00.0000000 for a DateTime, 2008-04-10T06:30:00.0000000-07:00 for a
   * DateTimeOffset /// </summary> RoundTrip, /// <summary> /// Displays e.g. Thu, 10 Apr 2008 13:30:00 GMT /// </summary> Rfc1123, ///
   * <summary> /// Displays e.g. 2008-04-10T06:30:00 /// </summary> Sortable, /// <summary> /// Displays e.g. 6:30 AM for en-US, 6:30 for
   * es-ES /// </summary> ShortTime, /// <summary> /// Displays e.g. 6:30:00 AM for en-US, 6:30:00 AM for es-ES /// </summary> LongTime, ///
   * <summary> /// Displays e.g. 2008-04-10 13:30:00Z /// </summary> UniversalSortable, /// <summary> /// Displays e.g. Thursday, April 10,
   * 2008 1:30:00 PM for en-US, den 10 april 2008 13:30:00 for sv-FI /// </summary> UniversalFull, /// <summary> /// Displays e.g. April,
   * 2008 for en-US, April 2008 for af-ZA /// </summary> YearMonth }
   */
  @Validate
  public static String toHumanReadable(@NotNull final Period p)
  {
    return toHumanReadable(p.toStandardDuration());
  }

  /**
   * Returns the value of the given timespan in a human-readable form, appending the suffix.
   * 
   * <pre>
   * Example: 10 seconds become "less than a minute".
   * Example: 1.1 minutes become "about a minute from now".
   * Example: 50 minutes become "50 minutes".
   * Example: 13 hours, 10 minutes become "about 13 hours".
   * The suffix " ago" or " from now" is appended depending on the sign of the timespan.
   * </pre>
   */
  @Validate
  public static String toHumanReadable(@NotNull Duration ts)
  {
    String suffix = " ago";
    if (ts.getMillis() < 0.0)
    {
      // negate
      ts = new Duration(-ts.getMillis());

      // indicate this is
      suffix = " from now";
    }

    return toHumanReadable(ts, suffix);
  }

  /**
   * Returns the value of the given timespan in a human-readable form, appending the suffix. Example: 10 seconds become
   * "less than a minute". Example: 1.1 minutes become "about a minute". Example: 50 minutes become "50 minutes". Example: 13 hours, 10
   * minutes become "about 13 hours". The suffix can be used to describe the event's position in time, use e.g. " ago" or " from now"
   */
  @Validate
  public static String toHumanReadable(@NotNull final Duration ts, @NotNull final String suffix)
  {
    val values = new AvlHashtable<Double, String>(Double.class, String.class);

    long totalSeconds = Math.abs(ts.getStandardSeconds());
    final double totalMinutes = Math.round(totalSeconds / 60);
    double totalHours = Math.round(totalMinutes / 60);
    double totalDays = Math.floor(totalHours / 24);
    double totalMonths = Math.floor(totalDays / 30);
    double totalYears = Math.floor(totalMonths / 12);

    values.add(0.75d, "less than a minute");
    values.add(1.5d, "about a minute");
    values.add(45d, String.format("%d minutes", (int) totalMinutes));
    values.add(90d, "about an hour");
    values.add(1440d, String.format("about %d hours", (int) totalHours)); // 60 * 24
    values.add(2880d, "a day"); // 60 * 48
    values.add(43200d, String.format("%d days", (int) totalDays)); // 60 * 24 * 30
    values.add(86400d, "about a month"); // 60 * 24 * 60
    values.add(525600d, String.format("%d months", (int) totalMonths)); // 60 * 24 * 365
    values.add(1051200d, "about a year"); // 60 * 24 * 365 * 2
    values.add(Double.MAX_VALUE, String.format("%d years", (int) totalYears));

    Double key = Linq.first(values.getKeys(), keyGreaterThan(totalMinutes));
    return values.get(key) + suffix;
  }

  @Predicate
  private static boolean keyGreaterThan(Double key, final double _totalMinutes)
  {
    return _totalMinutes < key;
  }

  /**
   * Returns the value of the given number in a human-readable form. Example: 1000 becomes 1,000 (UK) or 1.000 (US).
   */
  public static String toHumanReadable(long number)
  {
    return new Formatter().format("%,d", number).toString();
  }

  /**
   * Returns the value of the given number in a human-readable form. Example: 1000 becomes 1,000 (UK) or 1.000 (US).
   */
  public static String toHumanReadable(int number)
  {
    return new Formatter().format("%,d", number).toString();
  }

  /**
   * Returns the value of the given number in a human-readable form. Example: 1000 becomes 1,000 (UK) or 1.000 (US).
   */
  public static String toHumanReadable(short number)
  {
    return new Formatter().format("%,d", number).toString();
  }

  /**
   * Returns the value of the given number in a human-readable form. Example: 1000 becomes 1,000 (UK) or 1.000 (US).
   */
  public static String toHumanReadable(byte number)
  {
    return new Formatter().format("%,d", number).toString();
  }

  /**
   * Returns the value of the given number in a human-readable form. Example: 1000 becomes 1,000 (UK) or 1.000 (US).
   */
  public static String toHumanReadable(float number)
  {
    return new Formatter().format("%,f", number).toString();
  }

  /**
   * Returns the value of the given number in a human-readable form. Example: 1000 becomes 1,000 (UK) or 1.000 (US).
   * 
   * @throws IllegalArgumentException An argument was invalid
   */
  public static String toHumanReadable(float number, int decimalPlaces)
  {
    if (decimalPlaces < 0)
      throw new IllegalArgumentException("decimalPlaces=" + decimalPlaces);

    return new Formatter().format("%,." + decimalPlaces + "f", number).toString();
  }

  /**
   * Returns the value of the given number in a human-readable form. Example: 1000 becomes 1,000 (UK) or 1.000 (US).
   */
  public static String toHumanReadable(double number)
  {
    return new Formatter().format("%,f", number).toString();
  }

  /**
   * Returns the value of the given number in a human-readable form. Example: 1000 becomes 1,000 (UK) or 1.000 (US).
   * 
   * @throws IllegalArgumentException An argument was invalid
   */
  public static String toHumanReadable(double number, int decimalPlaces)
  {
    if (decimalPlaces < 0)
      throw new IllegalArgumentException("decimalPlaces=" + decimalPlaces);

    return new Formatter().format("%,." + decimalPlaces + "f", number).toString();
  }

  /**
   * Returns the value of the given number in a human-readable form. Example: 1000 becomes 1,000 (UK) or 1.000 (US).
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static String toHumanReadable(@NotNull final BigDecimal number)
  {
    return new Formatter().format("%,f", number).toString();
  }

  /**
   * Returns the value of the given number in a human-readable form. Example: 1000 becomes 1,000 (UK) or 1.000 (US).
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException An argument was invalid
   */
  @Validate
  public static String toHumanReadable(@NotNull final BigDecimal number, int decimalPlaces)
  {
    return new Formatter().format("%,." + decimalPlaces + "f", number).toString();
  }

  // TODO: implement these
  /*
   * /// <summary> /// Returns the date/time value in a human-readable and standard form, /// as specified by the given style selection.
   * Uses current thread locale. /// </summary> public static String ToHumanReadable(DateTime dateTime, DateTimeStyle style) { return
   * ToHumanReadable(dateTime, style, CultureInfo.CurrentCulture); } /// <summary> /// Returns the date/time value in a human-readable and
   * standard form, /// as specified by the given style selection. Uses current specified locale. /// </summary> /// <exception
   * cref="ArgumentException">Unrecognized date/time style used.</exception> public static String ToHumanReadable(DateTime dateTime,
   * DateTimeStyle style, CultureInfo cultureInfo) { switch(style) { case DateTimeStyle.ShortDate: return dateTime.ToString("d",
   * cultureInfo); case DateTimeStyle.LongDate: return dateTime.ToString("D", cultureInfo); case DateTimeStyle.FullDateShortTime: return
   * dateTime.ToString("f", cultureInfo); case DateTimeStyle.FullDateLongTime: return dateTime.ToString("F", cultureInfo); case
   * DateTimeStyle.GeneralDateShortTime: return dateTime.ToString("g", cultureInfo); case DateTimeStyle.GeneralDateLongTime: return
   * dateTime.ToString("G", cultureInfo); case DateTimeStyle.MonthDay: return dateTime.ToString("m", cultureInfo); case
   * DateTimeStyle.RoundTrip: return dateTime.ToString("o", cultureInfo); case DateTimeStyle.Rfc1123: return dateTime.ToString("r",
   * cultureInfo); case DateTimeStyle.Sortable: return dateTime.ToString("s", cultureInfo); case DateTimeStyle.ShortTime: return
   * dateTime.ToString("t", cultureInfo); case DateTimeStyle.LongTime: return dateTime.ToString("T", cultureInfo); case
   * DateTimeStyle.UniversalSortable: return dateTime.ToString("u", cultureInfo); case DateTimeStyle.UniversalFull: return
   * dateTime.ToString("U", cultureInfo); case DateTimeStyle.YearMonth: return dateTime.ToString("y", cultureInfo); default: throw new
   * ArgumentException("Unrecognized date/time format: " + style); } }
   */

  // Text <-> Byte-array conversions

  /**
   * Converts a UTF8-encoded string to a byte[]
   * 
   * @throws NullPointerException An argument is null.
   * @throws RuntimeException The UTF8 encoding is not supported.
   */
  @Validate
  public static byte[] toByteArray(@NotNull final String text)
  {
    try
    {
      return text.getBytes(CONSTANT.UTF8);
    }
    catch(Throwable e)
    {
      throw new RuntimeException("UTF8 encoding is not supported by this JVM.", e);
    }
  }

  /**
   * Converts a UTF8-encoded string to a byte[]
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds
   * @throws IllegalArgumentException An argument is out of range
   * @throws RuntimeException The UTF8 encoding is not supported
   */
  @Validate
  public static byte[] toByteArray(@NotNull final String text, int index, int count)
  {
    if (index < 0 || index > text.length())
      throw new IndexOutOfBoundsException("index=" + index + " textLen=" + text.length());
    if (count < 0 || index + count > text.length() || index + count < 0)
      throw new IllegalArgumentException("count=" + count + " index=" + index + " textLen=" + text.length());

    return toByteArray(text.substring(index, index + count));
  }

  /**
   * Converts a string to a byte[]
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static byte[] toByteArray(@NotNull final String text, @NotNull final Charset encoding)
  {
    return text.getBytes(encoding);
  }

  /**
   * Converts a string to a byte[]
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds
   * @throws IllegalArgumentException An argument is out of range
   * @throws RuntimeException The UTF8 encoding is not supported
   */
  @Validate
  public static byte[] toByteArray(@NotNull final String text, @NotNull final Charset encoding, int index, int count)
  {
    if (index < 0 || index > text.length())
      throw new IndexOutOfBoundsException("index=" + index + " textLen=" + text.length());
    if (count < 0 || index + count > text.length() || index + count < 0)
      throw new IllegalArgumentException("count=" + count + " index=" + index + " textLen=" + text.length());

    return text.substring(index, index + count).getBytes(encoding);
  }

  /**
   * Converts a byte array to a UTF8-encoded string
   */
  @Validate
  public static String toString(@NotNull final byte[] ba)
  {
    return new String(ba);
  }

  /**
   * Converts a byte array to a UTF8-encoded string
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds
   * @throws IllegalArgumentException An argument is out of range
   */
  @Validate
  public static String toString(@NotNull final byte[] ba, int index, int count)
  {
    if (index < 0 || index > ba.length)
      throw new IndexOutOfBoundsException("index=" + index + " baLen=" + ba.length);
    if (count < 0 || index + count > ba.length || index + count < 0)
      throw new IllegalArgumentException("count=" + count + " index=" + index + " baLen=" + ba.length);

    return new String(ba, index, count);
  }

  /**
   * Converts a byte array to an encoded string
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static String toString(@NotNull final byte[] ba, @NotNull final Charset encoding)
  {
    return new String(ba, encoding);
  }

  /**
   * Converts a byte array to an encoded string
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds
   * @throws IllegalArgumentException An argument is out of range
   */
  @Validate
  public static String toString(@NotNull final byte[] ba, @NotNull final Charset encoding, int index, int count)
  {
    if (index < 0 || index > ba.length)
      throw new IndexOutOfBoundsException("index=" + index + " baLen=" + ba.length);
    if (count < 0 || index + count > ba.length || index + count < 0)
      throw new IllegalArgumentException("count=" + count + " index=" + index + " baLen=" + ba.length);

    return new String(ba, index, count, encoding);
  }

  private static Object changeTypeFromPrimitive(Object value, PrimitiveType sourceType, Class<?> targetType)
  {
    switch(sourceType)
    {
      case Boolean:
        return changeTypeFromBoolean(Boolean.valueOf(value.toString()), targetType);
      case Char:
        if (StringUtils.isNullOrEmpty(value.toString()))
          throw new IllegalArgumentException("Cannot change primitive to Char, because it is null or empty.");
        return changeTypeFromCharacter(Character.valueOf(value.toString().charAt(0)), targetType);
      case Byte:
        return changeTypeFromNumber(Byte.valueOf(value.toString()), targetType);
      case Short:
        return changeTypeFromNumber(Short.valueOf(value.toString()), targetType);
      case Int:
        return changeTypeFromNumber(Integer.valueOf(value.toString()), targetType);
      case Long:
        return changeTypeFromNumber(Long.valueOf(value.toString()), targetType);
      case Double:
        return changeTypeFromNumber(Double.valueOf(value.toString()), targetType);
      case Float:
        return changeTypeFromNumber(Float.valueOf(value.toString()), targetType);
      default:
        throw new IllegalArgumentException("Unexpected source type: " + sourceType);
    }

  }

  private static Object changeTypeFromNumber(Object value, Class<?> targetType)
  {
    Number number = (Number) value;

    // dispatch to appropriate constructor method
    if (targetType.equals(Byte.class))
      return new Byte(number.byteValue());
    if (targetType.equals(Boolean.class))
      return number.longValue() == 0 ? new Boolean(false) : new Boolean(true);
    if (targetType.equals(Short.class))
      return new Short(number.shortValue());
    if (targetType.equals(Integer.class))
      return new Integer(number.intValue());
    if (targetType.equals(Long.class))
      return new Long(number.longValue());
    if (targetType.equals(Float.class))
      return new Float(number.floatValue());
    if (targetType.equals(Double.class))
      return new Double(number.doubleValue());
    if (targetType.equals(Character.class))
      return new Character((char) number.shortValue());

    if (targetType.equals(byte.class))
      return number.byteValue();
    if (targetType.equals(boolean.class))
      return number.longValue() == 0 ? false : true;
    if (targetType.equals(char.class))
      return (char) number.shortValue();
    if (targetType.equals(short.class))
      return number.shortValue();
    if (targetType.equals(int.class))
      return number.intValue();
    if (targetType.equals(long.class))
      return number.longValue();
    if (targetType.equals(float.class))
      return number.floatValue();
    if (targetType.equals(double.class))
      return number.doubleValue();
    if (targetType.equals(LocalDateTime.class))
      return new LocalDateTime(number.longValue());
    if (targetType.equals(BigDecimal.class))
      return new BigDecimal(number.toString());
    if (targetType.equals(Duration.class))
      return new Duration(number.longValue());
    if (targetType.equals(Int128.class))
      return new Int128(number.toString());
    if (targetType.equals(UnsignedByte.class))
      return new UnsignedByte((short) (number.byteValue() & 0xFF));
    if (targetType.equals(UnsignedShort.class))
      return new UnsignedShort((int) (number.shortValue() & 0xFFFF));
    if (targetType.equals(UnsignedInteger.class))
      return new UnsignedInteger((long) (number.intValue() & 0xFFFFFFFF));
    if (targetType.equals(UnsignedLong.class))
    {
      // perform similar operation as above to get rid of the negative values
      long ln = new BigInteger(number.toString()).longValue();
      return new UnsignedLong(new BigInteger("0" + toBinary(ln)));
    }
    if (targetType.equals(SignedByte.class))
      return new SignedByte(number.byteValue());
    if (targetType.equals(BigInteger.class))
      return new BigInteger(number.toString());
    if (targetType.equals(propel.core.userTypes.BigInteger.class))
    {
      propel.core.userTypes.BigInteger bi = new propel.core.userTypes.BigInteger();
      bi.setCurrentValue(number.toString());
      return bi;
    }

    throw new IllegalArgumentException("The provided Number could not be converted to type '" + targetType.getSimpleName() + "': " + value);
  }

  private static Object changeTypeFromCharacter(Object value, Class<?> targetType)
  {
    char ch = ((Character) value).charValue();

    // dispatch to appropriate construction method
    if (targetType.equals(Byte.class))
      return new Byte((byte) ch);
    if (targetType.equals(Boolean.class))
      return ch == 0 ? new Boolean(false) : new Boolean(true);
    if (targetType.equals(Short.class))
      return new Short((short) ch);
    if (targetType.equals(Integer.class))
      return new Integer(ch);
    if (targetType.equals(Long.class))
      return new Long(ch);
    if (targetType.equals(Float.class))
      return new Float(ch);
    if (targetType.equals(Double.class))
      return new Double(ch);

    if (targetType.equals(byte.class))
      return (byte) ch;
    if (targetType.equals(boolean.class))
      return ch == 0 ? false : true;
    if (targetType.equals(char.class))
      return ch;
    if (targetType.equals(short.class))
      return (short) ch;
    if (targetType.equals(int.class))
      return (int) ch;
    if (targetType.equals(long.class))
      return (long) ch;
    if (targetType.equals(float.class))
      return (float) ch;
    if (targetType.equals(double.class))
      return (double) ch;

    if (targetType.equals(LocalDateTime.class))
      return new LocalDateTime((long) ch);
    if (targetType.equals(BigDecimal.class))
      return new BigDecimal(ch);
    if (targetType.equals(Duration.class))
      return new Duration(ch);
    if (targetType.equals(Int128.class))
      return new Int128(Integer.valueOf(ch).toString());
    if (targetType.equals(UnsignedByte.class))
      return new UnsignedByte((byte) ch);
    if (targetType.equals(UnsignedShort.class))
      return new UnsignedShort(ch);
    if (targetType.equals(UnsignedInteger.class))
      return new UnsignedInteger(ch);
    if (targetType.equals(UnsignedLong.class))
      return new UnsignedLong(new Integer(ch).toString());
    if (targetType.equals(SignedByte.class))
      return new SignedByte((byte) ch);
    if (targetType.equals(BigInteger.class))
      return new BigInteger(Integer.valueOf(ch).toString());
    if (targetType.equals(propel.core.userTypes.BigInteger.class))
    {
      propel.core.userTypes.BigInteger bi = new propel.core.userTypes.BigInteger();
      bi.setCurrentValue(Integer.valueOf(ch).toString());
      return bi;
    }

    throw new IllegalArgumentException("The provided Character could not be converted to type '" + targetType.getSimpleName() + "': "
        + value);
  }

  private static Object changeTypeFromBoolean(Object value, Class<?> targetType)
  {
    boolean bool = ((Boolean) value).booleanValue();

    // dispatch to appropriate construction method
    if (targetType.equals(Byte.class))
      return bool ? new Byte((byte) 1) : new Byte((byte) 0);
    if (targetType.equals(Character.class))
      return bool ? new Character((char) 0) : new Character((char) 1);
    if (targetType.equals(Short.class))
      return bool ? new Short((short) 1) : new Short((short) 0);
    if (targetType.equals(Integer.class))
      return bool ? new Integer(1) : new Integer(0);
    if (targetType.equals(Long.class))
      return bool ? new Long(1) : new Long(0);
    if (targetType.equals(Float.class))
      return bool ? new Float(1) : new Float(0);
    if (targetType.equals(Double.class))
      return bool ? new Double(1) : new Double(0);

    if (targetType.equals(byte.class))
      return bool ? (byte) 1 : (byte) 0;
    if (targetType.equals(boolean.class))
      return bool;
    if (targetType.equals(char.class))
      return bool ? (char) 1 : (char) 0;
    if (targetType.equals(short.class))
      return bool ? (short) 1 : (short) 0;
    if (targetType.equals(int.class))
      return bool ? (int) 1 : (int) 0;
    if (targetType.equals(long.class))
      return bool ? (long) 1 : (long) 0;
    if (targetType.equals(float.class))
      return bool ? (float) 1.0 : (float) 0.0;
    if (targetType.equals(double.class))
      return bool ? (double) 1.0 : (double) 0.0;

    if (targetType.equals(LocalDateTime.class))
      return bool ? new LocalDateTime(1) : new LocalDateTime(0);
    if (targetType.equals(BigDecimal.class))
      return bool ? new BigDecimal(1) : new BigDecimal(0);
    if (targetType.equals(Duration.class))
      return bool ? new Duration(1) : new Duration(0);
    if (targetType.equals(Int128.class))
      return bool ? new Int128("1") : new Int128("0");
    if (targetType.equals(UnsignedByte.class))
      return bool ? new UnsignedByte((byte) 1) : new UnsignedByte((byte) 0);
    if (targetType.equals(UnsignedShort.class))
      return bool ? new UnsignedShort(1) : new UnsignedShort(0);
    if (targetType.equals(UnsignedInteger.class))
      return bool ? new UnsignedInteger(1) : new UnsignedInteger(0);
    if (targetType.equals(UnsignedLong.class))
      return bool ? new UnsignedLong("1") : new UnsignedLong("0");
    if (targetType.equals(SignedByte.class))
      return bool ? new SignedByte("1") : new SignedByte("0");
    if (targetType.equals(BigInteger.class))
      return bool ? new BigInteger("1") : new BigInteger("0");
    if (targetType.equals(propel.core.userTypes.BigInteger.class))
    {
      propel.core.userTypes.BigInteger bi = new propel.core.userTypes.BigInteger();
      bi.setCurrentValue(bool ? "1" : "0");
      return bi;
    }

    throw new IllegalArgumentException("The provided Boolean could not be converted to type '" + targetType.getSimpleName() + "': " + value);
  }

  private static Object changeTypeParseFromString(Object value, Class<?> targetType)
  {
    String str = value.toString();

    // dispatch to appropriate parser method
    if (targetType.equals(Byte.class))
      return new Byte(StringUtils.parseInt8(str));
    if (targetType.equals(Boolean.class))
      return new Boolean(StringUtils.parseBool(str));
    if (targetType.equals(Character.class))
      return new Character(StringUtils.parseChar(str));
    if (targetType.equals(Short.class))
      return new Short(StringUtils.parseInt16(str));
    if (targetType.equals(Integer.class))
      return new Integer(StringUtils.parseInt32(str));
    if (targetType.equals(Long.class))
      return new Long(StringUtils.parseInt64(str));
    if (targetType.equals(Float.class))
      return new Float(StringUtils.parseFloat(str));
    if (targetType.equals(Double.class))
      return new Double(StringUtils.parseDouble(str));

    if (targetType.equals(byte.class))
      return StringUtils.parseInt8(str);
    if (targetType.equals(boolean.class))
      return StringUtils.parseBool(str);
    if (targetType.equals(char.class))
      return StringUtils.parseChar(str);
    if (targetType.equals(short.class))
      return StringUtils.parseInt16(str);
    if (targetType.equals(int.class))
      return StringUtils.parseInt32(str);
    if (targetType.equals(long.class))
      return StringUtils.parseInt64(str);
    if (targetType.equals(float.class))
      return StringUtils.parseFloat(str);
    if (targetType.equals(double.class))
      return StringUtils.parseDouble(str);

    if (targetType.equals(LocalDateTime.class))
      return StringUtils.parseDateTime(str);
    if (targetType.equals(BigDecimal.class))
      return StringUtils.parseDecimal(str);
    if (targetType.equals(Duration.class))
      return StringUtils.parseTimeSpan(str);
    if (targetType.equals(Int128.class))
      return StringUtils.parseInt128(str);
    if (targetType.equals(UnsignedByte.class))
      return new UnsignedByte(str);
    if (targetType.equals(UnsignedShort.class))
      return new UnsignedShort(str);
    if (targetType.equals(UnsignedInteger.class))
      return new UnsignedInteger(str);
    if (targetType.equals(UnsignedLong.class))
      return new UnsignedLong(str);
    if (targetType.equals(SignedByte.class))
      return new SignedByte(str);
    if (targetType.equals(BigInteger.class))
      return new BigInteger(str);
    if (targetType.equals(propel.core.userTypes.BigInteger.class))
    {
      propel.core.userTypes.BigInteger bi = new propel.core.userTypes.BigInteger();
      bi.setCurrentValue(str);
      return bi;
    }

    throw new IllegalArgumentException("The provided object could not be converted to type '" + targetType.getSimpleName() + "': " + value);
  }

  private ConversionUtils()
  {
  }
}
