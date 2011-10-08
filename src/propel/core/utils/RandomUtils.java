/*
 ///////////////////////////////////////////////////////////
 //  This file is part of Propel.
 //
 //  Propel is free software: you can redistribute it and/or modify
 //  it under the terms of the GNU Lesser General Public License as published by
 //  the Free Software Foundation, either version 3 of the License, or
 //  (at your option) any later version.
 //
 //  Propel is distributed in the hope that it will be useful,
 //  but WITHOUT ANY WARRANTY; without even the implied warranty of
 //  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 //  GNU Lesser General Public License for more details.
 //
 //  You should have received a copy of the GNU Lesser General Public License
 //  along with Propel.  If not, see <http://www.gnu.org/licenses/>.
 ///////////////////////////////////////////////////////////
 //  Authored by: Nikolaos Tountas -> salam.kaser-at-gmail.com
 ///////////////////////////////////////////////////////////
 */
package propel.core.utils;

import propel.core.common.CONSTANT;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class RandomUtils
{
	private static final SecureRandom CRYPTO_RANDOM = new SecureRandom();
	private static final Random PSEUDO_RANDOM = new Random();

	/**
	 * Private constructor
	 */
	private RandomUtils()
	{
	}

	/**
	 * Returns a random Int32, created by the information
	 * entropy from bytes in a newly. created UUID.
	 */
	public static int getPseudoInt32()
	{
		UUID guid = UUID.randomUUID();
		return HashingUtils.uuidToInt32(guid);
	}

	/**
	 * Returns a pseudo random within the specified range [min, max)
	 * i.e. inclusive minimum and exclusive maximum, created by java.util.Random
	 *
	 * @throws IllegalArgumentException An argument is out of range
	 */
	public static int getPseudoInt32(int min, int max)
	{
		if(min < 0)
			throw new IllegalArgumentException("min=" + min);
		if(max < min)
			throw new IllegalArgumentException("max=" + max + " min=" + min);

		return min + PSEUDO_RANDOM.nextInt(max - min);
	}

	/**
	 * Returns a random Int64, created by the information
	 * entropy from bytes in a newly created UUID.
	 */
	public static long getPseudoInt64()
	{
		UUID guid = UUID.randomUUID();
		return HashingUtils.uuidToInt64(guid);
	}

	/**
	 * Returns random bytes, created by the information
	 * entropy from bytes in a newly created Guid.
	 *
	 * @throws IllegalArgumentException An argument is out of range
	 */
	public static byte[] getPseudoBytes(int length)
	{
		if(length < 0)
			throw new IllegalArgumentException("length=" + length);
		if(length == 0)
			return new byte[0];

		byte[] result = new byte[length];
		int index = 0;

		while(true)
		{
			UUID randomUuid = UUID.randomUUID();
			byte[] random = ByteArrayUtils.getBytes(randomUuid);

			for(int i = 0; i < 16; i++)
			{
				if(index < result.length)
					result[index++] = random[i];
				else
					return result;
			}
		}
	}

	/**
	 * Returns random text characters (uses the Base64 set).
	 * Randomness sourced from the information entropy of a new UUID.
	 *
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static String getPseudoBase64Text(int length)
	{
		if(length < 0)
			throw new IllegalArgumentException("length=" + length);
		if(length == 0)
			return CONSTANT.EMPTY_STRING;

		StringBuilder sb = new StringBuilder(length + 32);
		while(sb.length() < length)
			sb.append(ConversionUtils.toBase64(getPseudoInt64()));

		return sb.substring(0, length);
	}

	/**
	 * Returns random text characters (uses the 0..9, A..Z and a..z character sets).
	 * Randomness sourced from the information entropy of a new UUID.
	 *
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static String getPseudoAlphanumericText(int length)
	{
		if(length < 0)
			throw new IllegalArgumentException("length=" + length);
		if(length == 0)
			return CONSTANT.EMPTY_STRING;

		StringBuilder sb = new StringBuilder(length + 32);

		while(sb.length() < length)
		{
			String base64 = ConversionUtils.toBase64(getPseudoInt64());
			for(char ch : base64.toCharArray())
				if(Character.isLetterOrDigit(ch))
					sb.append(ch);
		}

		return sb.substring(0, length);
	}

	/**
	 * Randomizes elements in an array.
	 *
	 * @throws NullPointerException	  An argument is null.
	 * @throws IndexOutOfBoundsException An index is out of range.
	 */
	public static <T> void pseudoRandomize(T[] data, int start, int end)
	{
		if(data == null)
			throw new NullPointerException("data");
		if(start < 0 || start >= data.length || start > end)
			throw new IndexOutOfBoundsException("start=" + start + " end=" + end + " dataLen=" + data.length);
		if(end < 0 || end > data.length)
			throw new IndexOutOfBoundsException("end=" + end + " dataLen=" + data.length);

		Random random = new Random();
		int[] from = new int[end - start];
		int[] to = new int[end - start];
		for(int i = start; i < end; i++)
		{
			from[i] = random.nextInt(end - start) + start;
			to[i] = random.nextInt(end - start) + start;
		}

		ArrayUtils.swap(data, from, to);
	}

	/**
	 * Randomizes elements in an array.
	 *
	 * @throws NullPointerException	  An argument is null.
	 * @throws IndexOutOfBoundsException An index is out of range.
	 */
	public static <T> void pseudoRandomize(List<T> data, int start, int end)
	{
		if(data == null)
			throw new NullPointerException("data");
		if(start < 0 || start >= data.size() || start > end)
			throw new IndexOutOfBoundsException("start=" + start + " end=" + end + " dataLen=" + data.size());
		if(end < 0 || end > data.size())
			throw new IndexOutOfBoundsException("end=" + end + " dataLen=" + data.size());

		Random random = new Random();
		int[] from = new int[end - start];
		int[] to = new int[end - start];
		for(int i = start; i < end; i++)
		{
			from[i] = random.nextInt(end - start) + start;
			to[i] = random.nextInt(end - start) + start;
		}

		ArrayUtils.swap(data, from, to);
	}

	/**
	 * Returns a cryptographically secure random Int32.
	 */
	public static int getSecureInt32()
	{
		byte[] buffer = getSecureBytes(4); // size of int
		return ByteArrayUtils.toInt32(buffer);
	}

	/**
	 * Returns a cryptographically secure random Int32.
	 */
	public static long getSecureInt64()
	{
		byte[] buffer = getSecureBytes(8); // size of long
		return ByteArrayUtils.toInt64(buffer);
	}

	/**
	 * Returns a key filled with cryptographically secure random data.
	 *
	 * @throws IllegalArgumentException An argument is out of range
	 */
	public static byte[] getSecureBytes(int length)
	{
		if(length < 0)
			throw new IllegalArgumentException("length=" + length);
		if(length == 0)
			return new byte[0];

		byte[] buffer = new byte[length];
		CRYPTO_RANDOM.nextBytes(buffer);
		return buffer;
	}

	/**
	 * Returns random text characters (uses the Base64 set), created by a cryptographically secure source.
	 *
	 * @throws IllegalArgumentException An argument is out of range
	 */
	public static String getSecureBase64Text(int length)
	{
		if(length < 0)
			throw new IllegalArgumentException("length=" + length);
		if(length == 0)
			return CONSTANT.EMPTY_STRING;

		StringBuilder sb = new StringBuilder(length + 32);
		while(sb.length() < length)
			sb.append(ConversionUtils.toBase64(getSecureInt64()));

		return sb.substring(0, length);
	}

	/**
	 * Returns random text characters (uses the 0..9, A..Z and a..z character sets) created by a cryptographically secure source.
	 *
	 * @throws IllegalArgumentException An argument is out of range
	 */
	public static String getSecureAlphanumericText(int length)
	{
		if(length < 0)
			throw new IllegalArgumentException("length=" + length);
		if(length == 0)
			return CONSTANT.EMPTY_STRING;

		StringBuilder sb = new StringBuilder(length + 32);

		while(sb.length() < length)
		{
			String base64 = ConversionUtils.toBase64(getSecureInt64());
			for(char ch : base64.toCharArray())
				if(Character.isLetterOrDigit(ch))
					sb.append(ch);
		}

		return sb.substring(0, length);
	}
}

