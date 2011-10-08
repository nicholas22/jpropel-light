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
import propel.core.userTypes.UnsignedInteger;
import propel.core.userTypes.UnsignedLong;
import propel.core.userTypes.UnsignedShort;

import java.math.BigInteger;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ByteArrayUtils
{
	private ByteArrayUtils()
	{
	}

	/**
	 * Whether the machine is little endian
	 */
	public static final boolean LITTLE_ENDIAN = ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN);

	/**
	 * Sets all elements to 0
	 *
	 * @throws NullPointerException An argument is null.
	 */
	public static void clear(byte[] array)
	{
		if(array == null)
			throw new NullPointerException("array");

		for(int i = 0; i < array.length; i++)
			array[i] = 0;
	}

	/**
	 * Returns the number of occurences of a byte in a byte sequence.
	 *
	 * @throws NullPointerException An argument is null.
	 */
	public static int count(byte[] data, byte[] value)
	{
		return count(data, 0, value);
	}

	/**
	 * Returns the number of occurences of a byte sequence within a byte sequence.
	 *
	 * @throws NullPointerException	 An argument is null.
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static int count(byte[] data, int startIndex, byte[] value)
	{
		if(data == null)
			throw new NullPointerException("data");
		if(value == null)
			throw new NullPointerException("value");

		int dataLen = data.length;
		int valueLen = value.length;

		if(startIndex > dataLen)
			throw new IllegalAccessError("startIndex=" + startIndex + " dataLen=" + dataLen);
		if(dataLen == 0 && valueLen == 0)
			return 1;
		if(dataLen == 0 || valueLen == 0)
			return 0;
		if(startIndex < 0)
			throw new IllegalAccessError("startIndex=" + startIndex);

		int count = 0;
		int pos = startIndex;
		while(pos + valueLen <= dataLen && (pos = indexOf(data, pos, value)) >= 0)
		{
			count++;
			pos += valueLen;
		}

		return count;
	}

	/**
	 * Returns the number of occurences of a byte in a byte sequence.
	 *
	 * @throws NullPointerException An argument is null.
	 */
	public static int count(byte[] data, byte value)
	{
		return count(data, 0, new byte[] {value});
	}

	/**
	 * Returns the number of occurences of a byte in a byte sequence, starting at the given offset.
	 *
	 * @throws NullPointerException	 An argument is null.
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static int count(byte[] data, int startIndex, byte value)
	{
		return count(data, startIndex, new byte[] {value});
	}

	/**
	 * Returns true if a buffer ends with a value.
	 *
	 * @throws NullPointerException An argument is null.
	 */
	public static boolean endsWith(byte[] data, byte[] value)
	{
		if(data == null)
			throw new NullPointerException("data");
		if(value == null)
			throw new NullPointerException("value");

		int dataLen = data.length;
		int valueLen = value.length;

		if(valueLen == 0)
			return true;
		if(dataLen == valueLen)
			if(sequenceEqual(data, value))
				return true;
		if(valueLen > dataLen)
			return false;

		for(int i = valueLen - 1; i >= 0; i--)
			if(data[dataLen - i - 1] != value[i])
				return false;

		return true;
	}

	/**
	 * Similar to BitConverter.GetBytes() of .NET.
	 */
	public static byte[] getBytes(short value)
	{
		byte[] ba = new byte[] {
				(byte) (0xff & (value >> 8)),
				(byte) (0xff & value)};

		return LITTLE_ENDIAN ? reverse(ba) : ba;
	}

	/**
	 * Similar to BitConverter.GetBytes() of .NET.
	 */
	public static byte[] getBytes(UnsignedShort value)
	{
		BigInteger bi = value.bigIntegerValue();

		byte[] ba = new byte[] {
				(byte) (0xff & ((bi.shiftRight(8).intValue()))),
				(byte) (0xff & bi.intValue())};

		return LITTLE_ENDIAN ? reverse(ba) : ba;
	}

	/**
	 * Similar to BitConverter.GetBytes() of .NET.
	 */
	public static byte[] getBytes(int value)
	{
		byte[] ba = new byte[] {
				(byte) (0xff & (value >> 24)),
				(byte) (0xff & (value >> 16)),
				(byte) (0xff & (value >> 8)),
				(byte) (0xff & value)};

		return LITTLE_ENDIAN ? reverse(ba) : ba;
	}

	/**
	 * Similar to BitConverter.GetBytes() of .NET.
	 */
	public static byte[] getBytes(UnsignedInteger value)
	{
		BigInteger bi = value.bigIntegerValue();

		byte[] ba = new byte[] {
				(byte) (0xff & ((bi.shiftRight(24).intValue()))),
				(byte) (0xff & ((bi.shiftRight(16).intValue()))),
				(byte) (0xff & ((bi.shiftRight(8).intValue()))),
				(byte) (0xff & bi.intValue())};

		return LITTLE_ENDIAN ? reverse(ba) : ba;
	}

	/**
	 * Similar to BitConverter.GetBytes() of .NET.
	 */
	public static byte[] getBytes(long value)
	{
		byte[] ba = new byte[] {
				(byte) (0xff & (value >> 56)),
				(byte) (0xff & (value >> 48)),
				(byte) (0xff & (value >> 40)),
				(byte) (0xff & (value >> 32)),
				(byte) (0xff & (value >> 24)),
				(byte) (0xff & (value >> 16)),
				(byte) (0xff & (value >> 8)),
				(byte) (0xff & value)};

		return LITTLE_ENDIAN ? reverse(ba) : ba;
	}

	/**
	 * Similar to BitConverter.GetBytes() of .NET.
	 */
	public static byte[] getBytes(UnsignedLong value)
	{
		BigInteger bi = value.bigIntegerValue();

		byte[] ba = new byte[] {
				(byte) (0xff & (bi.shiftRight(56).intValue())),
				(byte) (0xff & ((bi.shiftRight(48).intValue()))),
				(byte) (0xff & ((bi.shiftRight(40).intValue()))),
				(byte) (0xff & ((bi.shiftRight(32).intValue()))),
				(byte) (0xff & ((bi.shiftRight(24).intValue()))),
				(byte) (0xff & ((bi.shiftRight(16).intValue()))),
				(byte) (0xff & ((bi.shiftRight(8).intValue()))),
				(byte) (0xff & bi.intValue())};

		return LITTLE_ENDIAN ? reverse(ba) : ba;
	}

	/**
	 * Converts a UUID to a byte array.
	 */
	public static byte[] getBytes(UUID uuid)
	{
		if(uuid == null)
			throw new NullPointerException("uuid");

		long msb = uuid.getMostSignificantBits();
		long lsb = uuid.getLeastSignificantBits();

		byte[] msB = getBytes(msb);
		byte[] lsB = getBytes(lsb);

		byte[] result = new byte[16];
		for(int i = 0; i < msB.length; i++)
			result[i] = msB[i];
		for(int i = 0; i < lsB.length; i++)
			result[i + 8] = lsB[i];

		return result;
	}

	/**
	 * Returns the first index of a byte sequence encountered within some data.
	 *
	 * @throws NullPointerException	 An argument is null.
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static int indexOf(byte[] data, byte[] value)
	{
		return indexOf(data, 0, value);
	}

	/**
	 * Returns the first index of a byte sequence encountered within some data.
	 *
	 * @throws NullPointerException	  An argument is null.
	 * @throws IndexOutOfBoundsException An index is out of range.
	 */
	public static int indexOf(byte[] data, int startIndex, byte[] value)
	{
		if(data == null)
			throw new NullPointerException("data");
		if(value == null)
			throw new NullPointerException("value");

		int dataLen = data.length;
		int valueLen = value.length;

		if(startIndex > dataLen)
			throw new IndexOutOfBoundsException("startIndex=" + startIndex + " dataLen=" + dataLen);

		if(dataLen == 0)
		{
			if(valueLen == 0)
				return 0;

			return -1;
		}
		if(startIndex < 0)
			throw new IndexOutOfBoundsException("startIndex=" + startIndex);

		for(int i = startIndex; i <= dataLen - valueLen; i++)
			if(sequenceEqual(data, i, value, 0, valueLen))
				return i;

		return -1;
	}

	/**
	 * Returns the first index of a byte encountered within some data.
	 *
	 * @throws NullPointerException	 An argument is null.
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static int indexOf(byte[] data, byte value)
	{
		return indexOf(data, 0, new byte[] {value});
	}

	/**
	 * Returns the first index of a byte encountered within some data.
	 *
	 * @throws NullPointerException	 An argument is null.
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static int indexOf(byte[] data, int startIndex, byte value)
	{
		return indexOf(data, startIndex, new byte[] {value});
	}

	/**
	 * Returns the index of the first/second/third/etc. occurrence of a value in the given data.
	 *
	 * @throws NullPointerException	 An argument is null.
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static int indexOf(byte[] data, byte[] value, int occurrence)
	{
		if(data == null)
			throw new NullPointerException("data");
		if(value == null)
			throw new NullPointerException("value");
		if(data.length <= 0 || value.length <= 0)
			return 0;
		if(occurrence <= 0)
			throw new IllegalArgumentException("occurrence=" + occurrence);

		int result = 0;
		int index = 0;

		while((index = indexOf(data, index, value)) >= 0)
		{
			result++;

			// check if occurrence reached, if so we found our result
			if(result == occurrence)
				return index;

			index++;
		}

		return -1;
	}

	/**
	 * Joins two byte arrays.
	 *
	 * @throws NullPointerException An argument is null.
	 */
	public static byte[] join(byte[] a, byte[] b)
	{
		if(a == null)
			throw new NullPointerException("a");
		if(b == null)
			throw new NullPointerException("b");

		byte[] result = new byte[a.length + b.length];
		for(int i = 0; i < a.length; i++)
			result[i] = a[i];
		for(int i = 0; i < b.length; i++)
			result[i + a.length] = b[i];

		return result;
	}

	/**
	 * Joins two or more arrays in the order they were provided. Null array items are ignored.
	 *
	 * @throws NullPointerException An argument is null
	 */
	public static byte[] join(Iterable<byte[]> arrays)
	{
		if(arrays == null)
			throw new NullPointerException("arrays");

		int length = 0;

		for(byte[] array : arrays)
			if(array != null)
				length += array.length;

		byte[] result = new byte[length];

		int pos = 0;
		for(byte[] array : arrays)
			if(array != null)
			{
				int arrayLen = array.length;
				for(int i = 0; i < arrayLen; i++)
				{
					result[pos] = array[i];
					pos++;
				}
			}

		return result;
	}

	/**
	 * Returns the last index of a byte sequence encountered within some data.
	 *
	 * @throws NullPointerException	 An argument is null.
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static int lastIndexOf(byte[] data, byte[] value)
	{
		if(data == null)
			throw new NullPointerException("data");

		return lastIndexOf(data, data.length, value);
	}

	/**
	 * Returns the last index of a byte sequence encountered within some data.
	 *
	 * @throws NullPointerException	  An argument is null.
	 * @throws IndexOutOfBoundsException An index is out of range.
	 */
	public static int lastIndexOf(byte[] data, int startIndex, byte[] value)
	{
		if(data == null)
			throw new NullPointerException("data");
		if(value == null)
			throw new NullPointerException("value");

		int dataLen = data.length;
		int valueLen = value.length;

		if(dataLen == 0 && (startIndex == -1 || startIndex == 0))
		{
			if(valueLen != 0)
				return -1;

			return 0;
		}
		if(startIndex < 0 || startIndex > dataLen)
			throw new IndexOutOfBoundsException("startIndex=" + startIndex + " dataLen=" + dataLen);

		if(startIndex == dataLen)
		{
			startIndex--;
			if(valueLen == 0)
				return startIndex;
		}

		for(int i = startIndex - valueLen + 1; i >= 0; i--)
			if(sequenceEqual(data, i, value, 0, valueLen))
				return i;

		return -1;
	}

	/**
	 * Returns the last index of a byte encountered within some data.
	 *
	 * @throws NullPointerException	 An argument is null.
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static int lastIndexOf(byte[] data, byte value)
	{
		if(data == null)
			throw new NullPointerException("data");

		return lastIndexOf(data, data.length, new byte[] {value});
	}

	/**
	 * Returns the last index of a byte encountered within some data.
	 *
	 * @throws NullPointerException	 An argument is null.
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static int lastIndexOf(byte[] data, int startIndex, byte value)
	{
		return lastIndexOf(data, startIndex, new byte[] {value});
	}

	/**
	 * Returns the index of the first/second/third/etc. occurence of a value, starting from the end of the given data.
	 *
	 * @throws NullPointerException	 An argument is null.
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static int lastIndexOf(byte[] data, byte[] value, int occurrence)
	{
		if(data == null)
			throw new NullPointerException("data");
		if(value == null)
			throw new NullPointerException("value");
		if(data.length <= 0 || value.length <= 0)
			return 0;
		if(occurrence <= 0)
			throw new IllegalArgumentException("occurrence");

		int result = 0;
		int index = data.length;

		while((index = lastIndexOf(data, index, value)) >= 0)
		{
			result++;

			// check if occurrence reached, if so we found our result
			if(result == occurrence)
				return index;

			index--;
			if(index < 0)
				break;
		}

		return -1;
	}

	/**
	 * Allows for matching a byte[] to another, using Equals, StartsWith, EndsWith or Contains matching types.
	 *
	 * @throws NullPointerException	 An argument is null.
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static boolean match(byte[] a, MatchType byteMatch, byte[] b)
	{
		if(a == null)
			throw new NullPointerException("a");
		if(b == null)
			throw new NullPointerException("b");

		switch(byteMatch)
		{
			case Equals:
				return sequenceEqual(a, b);
			case Contains:
				return indexOf(a, b) >= 0;
			case StartsWith:
				return startsWith(a, b);
			case EndsWith:
				return endsWith(a, b);
			default:
				throw new IllegalAccessError("Unrecognized byte match type: " + byteMatch);
		}
	}

	/**
	 * Re-sizes an array to the specified size. If the new size is smaller,
	 * elements get truncated. If the new size if bigger, the new array will have
	 * several null-valued elements near its end (i.e. newSize-oldSize in count).
	 * If the sizes are equal, the same array is returned.
	 *
	 * @throws NullPointerException	 Array is null
	 * @throws IllegalArgumentException Length is out of range
	 */
	public static byte[] resize(byte[] array, int length)
	{
		if(array == null)
			throw new NullPointerException("array");

		// validate new size
		if(length < 0)
			throw new IllegalArgumentException("length=" + length);

		int oldLength = array.length;

		// check if the sizes match
		if(length == oldLength)
			return array;

		// create new array
		byte[] newArray = new byte[length];

		// select strategy based on given scenario
		if(length > oldLength)
		{
			// newer is larger, use old size as upper bound
			for(int i = 0; i < oldLength; i++)
				newArray[i] = array[i];
			System.arraycopy(array, 0, newArray, 0, oldLength);
		}
		else
		{
			// newer is smaller, use new size as upper bound
			for(int i = 0; i < length; i++)
				newArray[i] = array[i];
		}

		return newArray;
	}

	/**
	 * Reverses an array (in place)
	 *
	 * @throws NullPointerException Array is null.
	 */
	public static byte[] reverse(byte[] array)
	{
		if(array == null)
			throw new NullPointerException("array");

		byte item;
		int index = 0;
		for(int i = 0; i < array.length; i++)
		{
			item = array[i];
			index = array.length - i - 1;
			array[i] = array[index];
			array[index] = item;
		}

		return array;
	}

	/**
	 * Reverses an array (creates a new copy)
	 *
	 * @throws NullPointerException Array is null.
	 */
	public static byte[] reverseCopy(byte[] array)
	{
		if(array == null)
			throw new NullPointerException("array");

		byte[] result = new byte[array.length];
		for(int i = 0; i < array.length; i++)
			result[array.length - i - 1] = array[i];

		return result;
	}

	/**
	 * Returns true if the byte sequences within two arrays are equal.
	 * Also returns true if both arrays are null. Returns false if only one array is null.
	 */
	public static boolean sequenceEqual(byte[] a, byte[] b)
	{
		if(a == null && b == null)
			return true;
		if(a == null || b == null)
			return false;

		// if lengths are not equal then arrays cannot be equal
		if(a.length != b.length)
			return false;

		int count = a.length;
		for(int i = 0; i < count; i++)
			if(a[i] != b[i])
				return false;

		return true;
	}

	/**
	 * Returns true if the byte sequences within two arrays are equal
	 *
	 * @throws NullPointerException	  An argument is null.
	 * @throws IndexOutOfBoundsException An index is out of bounds.
	 * @throws IllegalArgumentException  An argument is out of range.
	 */
	public static boolean sequenceEqual(byte[] a, int startIndexA, byte[] b, int startIndexB, int count)
	{
		if(a == null)
			throw new NullPointerException("a");
		if(b == null)
			throw new NullPointerException("b");

		if(count == 0)
			return true;
		if(startIndexA < 0 || startIndexA > a.length)
			throw new IndexOutOfBoundsException("startIndexA=" + startIndexA + " aLen=" + a.length);
		if(startIndexB < 0 || startIndexB > b.length)
			throw new IndexOutOfBoundsException("startIndexB=" + startIndexB + " bLen=" + b.length);
		if(count < 0)
			throw new IllegalArgumentException("count=" + count);

		if(startIndexA + count > a.length || startIndexA + count < 0)
			throw new IllegalArgumentException("startIndexA=" + startIndexA + " count=" + count + " aLen=" + a.length);
		if(startIndexB + count > b.length || startIndexB + count < 0)
			throw new IllegalArgumentException("startIndexB=" + startIndexB + " count=" + count + " bLen=" + b.length);

		for(int i = 0; i < count; i++)
			if(a[startIndexA + i] != b[startIndexB + i])
				return false;

		return true;
	}

	/**
	 * Obfuscation-oriented byte shifting function, shifts individual bytes right, does not check for or handles overflow.
	 *
	 * @throws NullPointerException An argument is null
	 */
	public static byte[] shiftRight(byte[] data, int count)
	{
		if(data == null)
			throw new NullPointerException("data");

		// shift right all bytes by count positions
		byte[] result = new byte[data.length];
		for(int i = 0; i < data.length; i++)    
      result[i] = (byte)(ConversionUtils.byteJvmToDotNet(data[i]) >>> count);

		return result;
	}

	/**
	 * Obfuscation-oriented byte shifting function, shifts individual bytes left, does not check for or handles overflow.
	 *
	 * @throws NullPointerException An argument is null
	 */
	public static byte[] shiftLeft(byte[] data, int count)
	{
		if(data == null)
			throw new NullPointerException("data");

		// shift right all bytes by count positions
		byte[] result = new byte[data.length];
		for(int i = 0; i < data.length; i++)
			result[i] = (byte) (ConversionUtils.byteJvmToDotNet(data[i]) << count);

		return result;
	}

	/**
	 * Splits a byte array into parts using the given separator.
	 *
	 * @throws NullPointerException An argument is null
	 */
	public static List<byte[]> split(byte[] data, byte[] separator)
	{
		if(data == null)
			throw new NullPointerException("data");
		if(separator == null)
			throw new NullPointerException("separator");

		List<byte[]> result = new ArrayList<byte[]>(16);

		if(separator.length == 0)
			result.add(data);
		else
		{
			byte[] currentData = data;
			int pos = 0;
			while((pos = indexOf(currentData, pos, separator)) >= 0)
			{
				result.add(subarray(currentData, 0, pos));
				pos += separator.length;
				currentData = subarray(currentData, pos, currentData.length - pos);
				pos = 0;
				if(currentData.length <= 0)
					break;
			}

			result.add(currentData);
		}

		return result;
	}

	/**
	 * Splits a byte array into parts using the given separator.
	 *
	 * @throws NullPointerException An argument is null.
	 */
	public static List<byte[]> split(byte[] data, byte[] separator, StringSplitOptions options)
	{
		List<byte[]> result = split(data, separator);

		switch(options)
		{
			case None:
				return result;
			case RemoveEmptyEntries:
				for(int i = 0; i < result.size(); i++)
					if(result.get(i).length == 0)
						result.remove(i--);
				return result;
			default:
				throw new IllegalArgumentException("Unrecognized split option: " + options);
		}
	}

	/**
	 * Splits a byte array into parts using the given separator
	 *
	 * @throws NullPointerException An argument is null.
	 */
	public static List<byte[]> split(byte[] data, byte separator)
	{
		return split(data, new byte[] {separator});
	}

	/**
	 * Returns true if the given byte sequence in data starts with the bytes found in value.
	 *
	 * @throws NullPointerException An argument is null.
	 */
	public static boolean startsWith(byte[] data, byte[] value)
	{
		if(data == null)
			throw new NullPointerException("data");
		if(value == null)
			throw new NullPointerException("value");

		int dataLen = data.length;
		int valueLen = value.length;

		if(valueLen == 0)
			return true;
		if(dataLen == valueLen)
			if(sequenceEqual(data, value))
				return true;
		if(valueLen > dataLen)
			return false;

		for(int i = 0; i < valueLen; i++)
			if(data[i] != value[i])
				return false;

		return true;
	}

	/**
	 * Returns a sub-array of the given array
	 *
	 * @throws NullPointerException	  An argument is null.
	 * @throws IndexOutOfBoundsException An index is out of bounds.
	 * @throws IllegalArgumentException  An argument is out of range.
	 */
	public static byte[] subarray(byte[] buffer, int startIndex)
	{
		if(buffer == null)
			throw new NullPointerException("buffer");

		return subarray(buffer, startIndex, buffer.length - startIndex);
	}

	/**
	 * Returns a sub-array of the given array
	 *
	 * @throws NullPointerException	  An argument is null.
	 * @throws IndexOutOfBoundsException An index is out of bounds.
	 * @throws IllegalArgumentException  An argument is out of range.
	 */
	public static byte[] subarray(byte[] buffer, int startIndex, int length)
	{
		if(buffer == null)
			throw new NullPointerException("buffer");
		if(startIndex < 0 || startIndex > buffer.length)
			throw new IndexOutOfBoundsException("startIndex=" + startIndex + " bufferLen=" + buffer.length);
		if(length < 0)
			throw new IllegalArgumentException("length=" + length);
		if(startIndex > (buffer.length - length))
			throw new IllegalArgumentException("startIndex=" + startIndex + " bufferLen=" + buffer.length + " length=" + length);
		if(length == 0)
			return new byte[0];

		byte[] result = new byte[length];
		for(int i = 0; i < length; i++)
			result[i] = buffer[i + startIndex];

		return result;
	}

	/**
	 * Swaps two elements in an array.
	 *
	 * @throws NullPointerException	  Array is null
	 * @throws IndexOutOfBoundsException Array indices are out of range.
	 */
	public static void swap(byte[] array, int a, int b)
	{
		if(array == null)
			throw new NullPointerException("array");

		if(a < 0 || a >= array.length)
			throw new IndexOutOfBoundsException("a=" + a + " length=" + array.length);

		if(b < 0 || b >= array.length)
			throw new IndexOutOfBoundsException("b=" + b + " length=" + array.length);

		if(a == b)
			return;

		byte value = array[a];
		array[a] = array[b];
		array[b] = value;
	}

	/**
	 * Inverse function of GetBytes(short)
	 */
	public static short toInt16(byte[] value)
	{
		if(value == null)
			throw new NullPointerException("value");
		if(value.length != 2)
			throw new IllegalArgumentException("length=" + value.length);

		if(LITTLE_ENDIAN)
			value = reverse(value);

		int result = ((value[0] & 0xff) << 8) | (value[1] & 0xff);

		return (short) result;
	}

	/**
	 * Inverse function of GetBytes(UnsignedShort)
	 *
	 * @throws NullPointerException	 An argument is null.
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static UnsignedShort toUInt16(byte[] value)
	{
		if(value == null)
			throw new NullPointerException("value");
		if(value.length != 2)
			throw new IllegalArgumentException("length=" + value.length);

		if(LITTLE_ENDIAN)
			value = reverse(value);

		BigInteger result = BigInteger.ZERO;
		result = result.or(new BigInteger(CONSTANT.EMPTY_STRING + (((value[0] & 0xff) << 8) | (value[1] & 0xff))));

		return new UnsignedShort(result);
	}

	/**
	 * Inverse function of GetBytes(int)
	 */
	public static int toInt32(byte[] value)
	{
		if(value == null)
			throw new NullPointerException("value");
		if(value.length != 4)
			throw new IllegalArgumentException("length=" + value.length);

		if(LITTLE_ENDIAN)
			value = reverse(value);

		return ((value[0] & 0xff) << 24) |
				((value[1] & 0xff) << 16) |
				((value[2] & 0xff) << 8) |
				(value[3] & 0xff);
	}

	/**
	 * Inverse function of GetBytes(UnsignedInteger)
	 *
	 * @throws NullPointerException	 An argument is null.
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static UnsignedInteger toUInt32(byte[] value)
	{
		if(value == null)
			throw new NullPointerException("value");
		if(value.length != 4)
			throw new IllegalArgumentException("length=" + value.length);

		if(LITTLE_ENDIAN)
			value = reverse(value);

		BigInteger result = BigInteger.ZERO;
		result = result.or(new BigInteger(CONSTANT.EMPTY_STRING + (((value[0] & 0xff) << 24) |
				((value[1] & 0xff) << 16) |
				((value[2] & 0xff) << 8) |
				(value[3] & 0xff))));

		return new UnsignedInteger(result);
	}

	/**
	 * Inverse function of GetBytes(long)
	 *
	 * @throws NullPointerException	 An argument is null.
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static long toInt64(byte[] value)
	{
		if(value == null)
			throw new NullPointerException("value");
		if(value.length != 8)
			throw new IllegalArgumentException("length=" + value.length);

		if(LITTLE_ENDIAN)
			value = reverse(value);

		return ((value[0] & 0xff) << 56) |
				((value[1] & 0xff) << 48) |
				((value[2] & 0xff) << 40) |
				((value[3] & 0xff) << 32) |
				((value[4] & 0xff) << 24) |
				((value[5] & 0xff) << 16) |
				((value[6] & 0xff) << 8) |
				(value[7] & 0xff);
	}

	/**
	 * Inverse function of GetBytes(UnsignedLong)
	 *
	 * @throws NullPointerException	 An argument is null.
	 * @throws IllegalArgumentException An argument is out of range.
	 */
	public static UnsignedLong toUInt64(byte[] value)
	{
		if(value == null)
			throw new NullPointerException("value");
		if(value.length != 8)
			throw new IllegalArgumentException("length=" + value.length);

		if(LITTLE_ENDIAN)
			value = reverse(value);

		BigInteger result = BigInteger.ZERO;
		result = result.or(new BigInteger(CONSTANT.EMPTY_STRING + (value[0] & 0xff)).shiftLeft(56));
		result = result.or(new BigInteger(CONSTANT.EMPTY_STRING + (((value[1] & 0xff) << 48) |
				((value[2] & 0xff) << 40) |
				((value[3] & 0xff) << 32) |
				((value[4] & 0xff) << 24) |
				((value[5] & 0xff) << 16) |
				((value[6] & 0xff) << 8) |
				(value[7] & 0xff))));

		return new UnsignedLong(result);
	}
}
