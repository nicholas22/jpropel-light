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

import propel.core.userTypes.UnsignedInteger;
import propel.core.userTypes.UnsignedLong;
import propel.core.userTypes.UnsignedShort;
import java.math.BigInteger;
import java.util.UUID;

/**
 * Provides utility functionality for hashing data in non-cryptographically secure ways.
 */
public final class HashingUtils
{
  /**
   * Private constructor
   */
  private HashingUtils()
  {
  }

  /**
   * Hashes a UUID to an Int32
   * 
   * @throws NullPointerException An argument is null
   */
  public static int uuidToInt32(UUID uuid)
  {
    if (uuid == null)
      throw new NullPointerException("uuid");

    byte[] ba = ByteArrayUtils.getBytes(uuid);

    int a = ba[0] | ba[1] << 8 | ba[2] << 0x10 | ba[3] << 0x18;
    short b = (short) (ba[4] | ba[5] << 8);
    short c = (short) (ba[6] | ba[7] << 8);
    byte f = ba[10];
    byte k = ba[15];

    int x = ((a ^ ((b << 16) | (c))) ^ ((f << 24) | k));

    return x;
  }

  /**
   * Hashes a Guid to an Int64. Good distribution.
   * 
   * @throws NullPointerException An argument is null
   */
  public static long uuidToInt64(UUID uuid)
  {
    if (uuid == null)
      throw new NullPointerException("uuid");

    byte[] ba = ByteArrayUtils.getBytes(uuid);

    int a = ba[0] | ba[1] << 8 | ba[2] << 0x10 | ba[3] << 0x18;
    short b = (short) (ba[4] | ba[5] << 8);
    short c = (short) (ba[6] | ba[7] << 8);
    byte d = ba[8];
    byte e = ba[9];
    byte f = ba[10];
    byte g = ba[11];
    byte h = ba[12];
    byte i = ba[13];
    byte j = ba[14];
    byte k = ba[15];

    int x = ((a ^ ((b << 16) | (c))) ^ ((f << 24) | k));

    byte jd = (byte) (j ^ d);
    byte ig = (byte) (i | g);
    byte he = (byte) (h ^ e);

    int y = (jd << 24) | (ig << 16) | (he << 8) + j;

    long unique = (long) y << 32 | x;

    return unique;
  }

  // Djb2 algorithm taken from http://www.cse.yorku.ca/~oz/hash.html

  // TODO: Test toInt functions work with "from UInt".intValue() conversion

  /**
   * An efficient hashing algorithm with good distribution
   */
  public static long stringToInt64(String str)
  {
    return bytesToInt64(ConversionUtils.toByteArray(str));
  }

  /**
   * An efficient hashing algorithm with good distribution
   * 
   * @throws NullPointerException An argument is null
   */
  public static long bytesToInt64(byte[] ba)
  {
    return bytesToUInt64(ba).longValue();
  }

  /**
   * An efficient hashing algorithm with good distribution
   * 
   * @throws NullPointerException An argument is null
   */
  public static UnsignedLong bytesToUInt64(byte[] ba)
  {
    if (ba == null)
      throw new NullPointerException("ba");

    BigInteger hash = BigInteger.ZERO;

    for (byte b : ba)
      hash = hash.shiftLeft(5).add(hash).add(new BigInteger(Integer.toString(0xFF & b)));

    return new UnsignedLong(hash);
  }

  /**
   * An efficient hashing algorithm with good distribution
   * 
   * @throws NullPointerException An argument is null
   */
  public static UnsignedLong stringtoUInt64(String str)
  {
    return bytesToUInt64(ConversionUtils.toByteArray(str));
  }

  /**
   * An efficient hashing algorithm with good distribution
   * 
   * @throws NullPointerException An argument is null
   */
  public static int stringToInt32(String str)
  {
    return stringToUInt32(str).intValue();
  }

  /**
   * An efficient hashing algorithm with good distribution
   * 
   * @throws NullPointerException An argument is null
   */
  public static int bytesToInt32(byte[] ba)
  {
    return bytesToUInt32(ba).intValue();
  }

  /**
   * An efficient hashing algorithm with good distribution
   * 
   * @throws NullPointerException An argument is null
   */
  public static UnsignedInteger stringToUInt32(String str)
  {
    return bytesToUInt32(ConversionUtils.toByteArray(str));
  }

  /**
   * An efficient hashing algorithm with good distribution
   * 
   * @throws NullPointerException An argument is null
   */
  public static UnsignedInteger bytesToUInt32(byte[] ba)
  {
    if (ba == null)
      throw new NullPointerException("ba");

    BigInteger hash = BigInteger.ZERO;

    for (byte b : ba)
      hash = hash.shiftLeft(5).add(hash).add(new BigInteger(Integer.toString(0xFF & b)));

    return new UnsignedInteger(hash);
  }

  /**
   * An efficient hashing algorithm with good distribution
   * 
   * @throws NullPointerException An argument is null
   */
  public static UnsignedInteger stringToUInt24(String str)
  {
    return bytesToUInt24(ConversionUtils.toByteArray(str));
  }

  /**
   * An efficient hashing algorithm with good distribution
   * 
   * @throws NullPointerException An argument is null
   */
  public static UnsignedInteger bytesToUInt24(byte[] ba)
  {
    if (ba == null)
      throw new NullPointerException("ba");

    BigInteger hash = BigInteger.ZERO;

    for (byte b : ba)
      hash = hash.shiftLeft(5).add(hash).add(new BigInteger(Integer.toString(0xFF & b)));

    // blanks msb
    return new UnsignedInteger(hash.and(new BigInteger("16777215")));
  }

  /**
   * An efficient hashing algorithm with good distribution
   * 
   * @throws NullPointerException An argument is null
   */
  public static int stringToInt24(String str)
  {
    return stringToUInt24(str).intValue();
  }

  /**
   * An efficient hashing algorithm with good distribution
   * 
   * @throws NullPointerException An argument is null
   */
  public static int bytesToInt24(byte[] ba)
  {
    return bytesToUInt24(ba).intValue();
  }

  /**
   * An efficient hashing algorithm with good distribution
   * 
   * @throws NullPointerException An argument is null
   */
  public static short stringToInt16(String str)
  {
    return stringToUInt16(str).shortValue();
  }

  /**
   * An efficient hashing algorithm with good distribution
   * 
   * @throws NullPointerException An argument is null
   */
  public static short bytesToInt16(byte[] ba)
  {
    return bytesToUInt16(ba).shortValue();
  }

  /**
   * An efficient hashing algorithm with good distribution
   * 
   * @throws NullPointerException An argument is null
   */
  public static UnsignedShort stringToUInt16(String str)
  {
    return bytesToUInt16(ConversionUtils.toByteArray(str));
  }

  /**
   * An efficient hashing algorithm with good distribution
   * 
   * @throws NullPointerException An argument is null
   */
  public static UnsignedShort bytesToUInt16(byte[] ba)
  {
    if (ba == null)
      throw new NullPointerException("ba");

    UnsignedShort hash = new UnsignedShort("5381");

    for (byte b : ba)
    {
      BigInteger bi = hash.bigIntegerValue();
      hash = new UnsignedShort(bi.shiftLeft(5).add(bi).add(new BigInteger(Integer.toString(0xFF & b))));
    }

    return hash;
  }
}
