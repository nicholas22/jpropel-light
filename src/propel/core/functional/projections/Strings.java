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

import lombok.Function;
import propel.core.common.CONSTANT;
import propel.core.utils.StringComparison;
import propel.core.utils.StringUtils;

/**
 * Some common, re-usable projections for string manipulation
 */
public final class Strings
{

  /**
   * Returns the string with some appended text
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String append(final String value, final String _suffix)
  {
    return value + _suffix;
  }

  /**
   * Returns the character at specified index
   * 
   * @throws NullPointerException An argument is null
   * @throws IndexOutOfBoundsException Index is out of string bounds
   */
  @Function
  public static char charAt(final String value, final int _index)
  {
    return value.charAt(_index);
  }

  /**
   * Returns the number of occurrences of a string within a string
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static int count(final String value, final String _str, final StringComparison _stringComparison)
  {
    return StringUtils.count(value, _str, _stringComparison);
  }

  /**
   * Returns the number of occurrences of a character within a character array
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static int countChars(final char[] value, final char _char)
  {
    return StringUtils.count(value, _char);
  }

  /**
   * Crops all characters from the start/end of the given string, until an except character is encountered
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String crop(final String value, final char[] _except)
  {
    return StringUtils.crop(value, _except);
  }

  /**
   * Crops all characters from the end of the given string, until an except character is encountered
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String cropEnd(final String value, final char[] _except)
  {
    return StringUtils.cropEnd(value, _except);
  }

  /**
   * Crops all characters from the start of the given string, until an except character is encountered
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String cropStart(final String value, final char[] _except)
  {
    return StringUtils.cropStart(value, _except);
  }

  /**
   * Returns the first index where a part is encountered within a string value. If the part is not existent, -1 is returned. Uses the
   * specified string comparison.
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static int indexOf(final String value, final String _part, final StringComparison _stringComparison)
  {
    return StringUtils.indexOf(value, _part, _stringComparison);
  }

  /**
   * Returns the last index where a part is encountered within a string value. If the part is not existent, -1 is returned. Uses the
   * specified string comparison.
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static int lastIndexOf(final String value, final String _part, final StringComparison _stringComparison)
  {
    return StringUtils.lastIndexOf(value, _part, _stringComparison);
  }

  /**
   * Returns the length of the string value
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static int length(final String value)
  {
    return value.length();
  }

  /**
   * Returns the string with some prepended text
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String prepend(final String value, final String _prefix)
  {
    return _prefix + value;
  }

  /**
   * Returns the string after some text replacement
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String replace(final String value, final String _textToReplace, final String _replaceWithText,
                               final StringComparison _stringComparison)
  {
    return StringUtils.replace(value, _textToReplace, _replaceWithText, _stringComparison);
  }

  /**
   * Returns the string at specified index
   * 
   * @throws NullPointerException An argument is null
   * @throws IndexOutOfBoundsException Index is out of string bounds
   */
  @Function
  public static String stringAt(final String value, final int _index)
  {
    return Character.toString(value.charAt(_index));
  }

  /**
   * Returns a sub-string of the given string, using a start index and a length from start
   * 
   * @throws NullPointerException An argument is null
   * @throws IndexOutOfBoundsException The index or length is out of range
   */
  @Function
  public static String substring(final String value, final int _startIndex, final int _length)
  {
    return StringUtils.substring(value, _startIndex, _length);
  }

  /**
   * Returns a sub-string of the given string, using a start index and a length from start. If a null string is encountered, or and
   * index/length is out of range, then an empty string is returned.
   * 
   * @throws NullPointerException An argument is null
   * @throws IndexOutOfBoundsException The index or length is out of range
   */
  @Function
  public static String substringNullCoalescing(final String value, final int _startIndex, final int _length)
  {
    try
    {
      return StringUtils.substring(value, _startIndex, _length);
    }
    catch(Exception e)
    {
      return CONSTANT.EMPTY_STRING;
    }
  }

  /**
   * Returns a sub-string of the given string, using a start index and a length from start. If a null string is encountered, or and
   * index/length is out of range, then the given default string is returned.
   * 
   * @throws NullPointerException An argument is null
   * @throws IndexOutOfBoundsException The index or length is out of range
   */
  @Function
  public static String substringSafe(final String value, final int _startIndex, final int _length, final String _defaultValue)
  {
    try
    {
      return StringUtils.substring(value, _startIndex, _length);
    }
    catch(Exception e)
    {
      return _defaultValue;
    }
  }

  /**
   * Calls toLowerCase() on function arguments
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String toLowerCase(final String value)
  {
    return value.toLowerCase();
  }

  /**
   * Calls titleCase() on function arguments
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String toTitleCase(final String value)
  {
    return StringUtils.titleCase(value);
  }

  /**
   * Calls toUpperCase() on function arguments
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String toUpperCase(final String value)
  {
    return value.toUpperCase();
  }

  /**
   * Returns the trimmed version of the given string
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String trim(final String value)
  {
    return StringUtils.trim(value);
  }

  /**
   * Trims the end and returns the given string
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String trimEnd(final String value, final char[] _chars)
  {
    return StringUtils.trimEnd(value, _chars);
  }

  /**
   * Trims the start and returns the given string
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static String trimStart(final String value, final char[] _chars)
  {
    return StringUtils.trimStart(value, _chars);
  }

  private Strings()
  {
  }
}
