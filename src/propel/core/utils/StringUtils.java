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

import lombok.Predicate;
import lombok.Function;
import lombok.Validate;
import lombok.Validate.NotNull;
import lombok.val;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.format.*;
import propel.core.TryResult;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.collections.lists.ReifiedList;
import propel.core.common.CONSTANT;
import propel.core.functional.predicates.Strings;
import propel.core.userTypes.*;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.text.Collator;
import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.*;

/**
 * Provides helper functionality for Strings and char arrays.
 */
public final class StringUtils
{

  /**
   * The current locale of the JVM
   */
  public static final Locale CURRENT_LOCALE = Locale.getDefault();
  /**
   * An invariant locale across JVMs
   */
  public static final Locale INVARIANT_LOCALE = Locale.US;
  /**
   * The current locale's collator
   */
  public static final Collator CURRENT_LOCALE_COLLATOR = Collator.getInstance(CURRENT_LOCALE);
  /**
   * The invariant locale's collator
   */
  public static final Collator INVARIANT_LOCALE_COLLATOR = Collator.getInstance(INVARIANT_LOCALE);
  private static final DecimalFormatSymbols CURRENT_DECIMAL_SYMBOLS = new DecimalFormatSymbols(CURRENT_LOCALE);
  /**
   * Current locale decimal separator symbol
   */
  public static final char DECIMAL_SEPARATOR = CURRENT_DECIMAL_SYMBOLS.getDecimalSeparator();
  /**
   * Current locale decimal grouping symbol
   */
  public static final char GROUPING_SEPARATOR = CURRENT_DECIMAL_SYMBOLS.getGroupingSeparator();
  private static final Duration MIN_DURATION = new Duration(Long.MIN_VALUE);
  private static final Duration MAX_DURATION = new Duration(Long.MAX_VALUE);
  private static final LocalDateTime MIN_DATETIME = new LocalDateTime(1, 1, 1, 0, 0, 0); // 1/1/0001 00:00:00
  private static final LocalDateTime MAX_DATETIME = new LocalDateTime(9999, 12, 31, 23, 59, 59); // 31/12/9999 23:59:59
  /**
   * ISO standard date/time formatters (composite class)
   */
  public static final DateTimeFormatter STANDARD_FORMATTERS = (new DateTimeFormatterBuilder()).append(null, createCommonDateTimeParsers())
      .toFormatter();

  private StringUtils()
  {
  }

  /**
   * Returns a character range from start (inclusive) to end (exclusive).
   * 
   * @throws IllegalArgumentException When the end is before start
   */
  public static char[] charRange(char start, char end)
  {
    int length = (int) end - (int) start;
    if (length < 0)
      throw new IllegalArgumentException("start=" + (int) start + " end=" + (int) end);

    char[] result = new char[length];

    int index = 0;
    for (char ch = start; ch < end; ch++)
      result[index++] = ch;

    return result;
  }

  /**
   * Returns a character range from start (inclusive) to end (exclusive).
   * 
   * @throws IllegalArgumentException When the end is before start
   */
  public static char[] charRange(int start, int end)
  {
    return charRange((char) start, (char) end);
  }

  /**
   * Compares two Strings using a CurrentLocale string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int compare(String a, String b)
  {
    return compare(a, b, StringComparison.CurrentLocale);
  }

  /**
   * Compares two Strings using the specified string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int compare(String a, String b, StringComparison stringComparison)
  {
    if (a == null)
      throw new NullPointerException("a");
    if (b == null)
      throw new NullPointerException("b");

    switch(stringComparison)
    {
      case CurrentLocale:
        return compareLocaleSensitive(a, b, CURRENT_LOCALE, CURRENT_LOCALE_COLLATOR, true);
      case CurrentLocaleIgnoreCase:
        return compareLocaleSensitive(a, b, CURRENT_LOCALE, CURRENT_LOCALE_COLLATOR, false);
      case InvariantLocale:
        return compareLocaleSensitive(a, b, INVARIANT_LOCALE, INVARIANT_LOCALE_COLLATOR, true);
      case InvariantLocaleIgnoreCase:
        return compareLocaleSensitive(a, b, INVARIANT_LOCALE, INVARIANT_LOCALE_COLLATOR, false);
      case Ordinal:
        return compareOrdinal(a, b, true);
      case OrdinalIgnoreCase:
        return compareOrdinal(a, b, false);
      default:
        throw new IllegalArgumentException("stringComparison has an unexpected value: " + stringComparison.toString());
    }
  }

  /**
   * Comparison function, uses higher performance locale-aware comparison, uses existing collator to avoid creating one every time.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int compare(String a, String b, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (a == null)
      throw new NullPointerException("a");
    if (b == null)
      throw new NullPointerException("b");

    return compareLocaleSensitive(a, b, locale, collator, caseSensitive);
  }

  /**
   * Locale-aware comparison.
   * 
   * @throws NullPointerException An argument is null
   */
  private static int compareLocaleSensitive(String a, String b, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (locale == null)
      throw new NullPointerException("locale");
    if (collator == null)
      throw new NullPointerException("collator");

    if (!caseSensitive)
    {
      a = a.toLowerCase(locale);
      b = b.toLowerCase(locale);
    }

    return collator.compare(a, b);
  }

  /**
   * Compares two strings lexicographically
   * 
   * @throws NullPointerException An argument is null
   */
  private static int compareOrdinal(String a, String b, boolean caseSensitive)
  {
    if (caseSensitive)
      return a.compareTo(b);
    else
    {
      // ordinal ignore case
      int len1 = a.length();
      int len2 = b.length();
      int lim = len1 < len2 ? len1 : len2;
      char v1[] = a.toCharArray();
      char v2[] = b.toCharArray();

      int i = 0;
      while (i < lim)
      {
        char c1 = v1[i];
        char c2 = v2[i];

        // letters
        if ((c1 >= 65 && c1 <= 90) || (c1 >= 97 && c1 <= 122))
        {
          c1 = Character.toLowerCase(c1);
          c2 = Character.toLowerCase(c2);
        }

        if (c1 != c2)
          return c1 - c2;

        i++;
      }

      return len1 - len2;
    }
  }

  /**
   * Concatenates a collection of strings into a single string. Ignores null items.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String concat(Iterable<String> values)
  {
    return delimit(values, CONSTANT.EMPTY_STRING, null);
  }

  /**
   * Concatenates a collection of strings into a single string. Substitutes null items with the null-replacement value provided, if not
   * null.
   * 
   * @throws NullPointerException The values argument is null.
   */
  public static String concat(Iterable<String> values, String nullReplacementValue)
  {
    return delimit(values, CONSTANT.EMPTY_STRING, nullReplacementValue);
  }

  /**
   * Concatenates a collection of strings into a single string. Ignores null items.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String concat(String[] values)
  {
    return delimit(values, CONSTANT.EMPTY_STRING, null);
  }

  /**
   * Concatenates a collection of strings into a single string. Substitutes null items with the null-replacement value provided, if not
   * null.
   * 
   * @throws NullPointerException The values argument is null.
   */
  public static String concat(String[] values, String nullReplacementValue)
  {
    return delimit(values, CONSTANT.EMPTY_STRING, nullReplacementValue);
  }

  /**
   * Concatenates the given chars. Returns String.Empty if an empty collection was provided.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String concat(char[] values)
  {
    if (values == null)
      throw new NullPointerException("values");

    return new String(values);
  }

  /**
   * Concatenates the given chars. Returns an empty array if no chars are found.
   * 
   * @throws NullPointerException When the values or one of its arguments is null.
   */
  public static char[] concat(char[]... values)
  {
    if (values == null)
      throw new NullPointerException("values");

    int count = 0;
    for (char[] arr : values)
    {
      if (arr == null)
        throw new NullPointerException("Item of values");

      count += arr.length;
    }

    char[] result = new char[count];

    int index = 0;
    for (char[] arr : values)
    {
      System.arraycopy(arr, 0, result, index, arr.length);
      index += arr.length;
    }

    return result;
  }

  /**
   * Returns true if a char sequence contains a character
   * 
   * @throws NullPointerException When the sequence is null.
   */
  public static boolean contains(char[] sequence, char ch)
  {
    if (sequence == null)
      throw new NullPointerException("sequence");

    for (char c : sequence)
      if (c == ch)
        return true;

    return false;
  }

  /**
   * Returns true if a string contains a character
   * 
   * @throws NullPointerException When the sequence is null.
   */
  public static boolean contains(String value, char ch)
  {
    if (value == null)
      throw new NullPointerException("value");

    for (char c : value.toCharArray())
      if (c == ch)
        return true;

    return false;
  }

  /**
   * Returns true if the part is contained in the value. Uses CurrentLocale string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean contains(String value, String part)
  {
    return contains(value, part, StringComparison.CurrentLocale);
  }

  /**
   * Returns true if the part is contained in the value. Uses the specified string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean contains(String value, String part, StringComparison stringComparison)
  {
    return indexOf(value, part, 0, value.length(), stringComparison) >= 0;
  }

  /**
   * Returns true if the part is contained in the value. Uses culture-aware higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean contains(String value, String part, Locale locale, Collator collator, boolean caseSensitive)
  {
    return indexOf(value, part, 0, value.length(), locale, collator, caseSensitive) >= 0;
  }

  /**
   * Returns true if the value is contained in the collection of values. Uses the specified string comparison mode.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean contains(Iterable<String> values, String value, StringComparison stringComparison)
  {
    if (values == null)
      throw new NullPointerException("values");

    if (value == null)
      return Linq.contains(values, null);
    else
      for (String val : values)
        if (equal(value, val, stringComparison))
          return true;

    return false;
  }

  /**
   * Returns true if the value is contained in the collection of values. Uses culture-aware higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean contains(Iterable<String> values, String value, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (values == null)
      throw new NullPointerException("values");

    if (value == null)
      return Linq.contains(values, null);
    else
      for (String val : values)
        if (equal(value, val, locale, collator, caseSensitive))
          return true;

    return false;
  }

  /**
   * Returns true if the value is contained in the collection of values. Uses the specified string comparison mode.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean contains(String[] values, String value, StringComparison stringComparison)
  {
    if (values == null)
      throw new NullPointerException("values");

    if (value == null)
      return Linq.contains(values, null);
    else
      for (String val : values)
        if (equal(value, val, stringComparison))
          return true;

    return false;
  }

  /**
   * Returns true if the value is contained in the collection of values. Uses culture-aware higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean contains(String[] values, String value, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (values == null)
      throw new NullPointerException("values");

    if (value == null)
      return Linq.contains(values, null);
    else
      for (String val : values)
        if (equal(value, val, locale, collator, caseSensitive))
          return true;

    return false;
  }

  /**
   * Returns true if all characters are contained in a string. Order is not taken into consideration.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean containsAll(String value, char[] characters)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (characters == null)
      throw new NullPointerException("characters");

    for (char ch : characters)
      if (value.indexOf(ch) < 0)
        return false;

    return true;
  }

  /**
   * Returns true if all strings specified are contained in the value string. Order is not taken into consideration. Uses the CurrentLocale
   * StringComparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean containsAll(String value, Iterable<String> parts)
  {
    return containsAll(value, parts, StringComparison.CurrentLocale);
  }

  /**
   * Returns true if all strings specified are contained in the value string. Order is not taken into consideration. If a part is null then
   * false is returned.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean containsAll(String value, Iterable<String> parts, StringComparison stringComparison)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (parts == null)
      throw new NullPointerException("parts");

    // check that all exist
    for (String part : parts)
      if (part == null)
        return false;
      else if (indexOf(value, part, 0, value.length(), stringComparison) < 0)
        return false;

    return true;
  }

  /**
   * Returns true if all strings specified are contained in the value string. Order is not taken into consideration. If a part is null then
   * false is returned. Uses culture-aware higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean containsAll(String value, Iterable<String> parts, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (parts == null)
      throw new NullPointerException("parts");

    // check that all exist
    for (String part : parts)
      if (part == null)
        return false;
      else if (indexOf(value, part, 0, value.length(), locale, collator, caseSensitive) < 0)
        return false;

    return true;
  }

  /**
   * Returns true if all items are contained in the values string collection. Order is not taken into consideration. You may use null
   * elements for both values and items. Uses CurrentLocale string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean containsAll(Iterable<String> values, Iterable<String> items)
  {
    return containsAll(values, items, StringComparison.CurrentLocale);
  }

  /**
   * Returns true if all items are contained in the values string collection. Order is not taken into consideration. You may use null
   * elements for both values and items.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean containsAll(Iterable<String> values, Iterable<String> items, StringComparison stringComparison)
  {
    if (values == null)
      throw new NullPointerException("values");
    if (items == null)
      throw new NullPointerException("items");

    for (String item : items)
    {
      boolean found = false;

      for (String value : values)
      {
        if (item == null && value == null)
          continue;
        if (item == null || value == null)
          return false;
        if (equal(item, value, stringComparison))
          found = true;
      }

      if (!found)
        return false;
    }

    return true;
  }

  /**
   * Returns true if all items in items are contained in the values string collection. Order is not taken into consideration. You may use
   * null elements for values and items. Uses a culture-aware higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean
      containsAll(Iterable<String> values, Iterable<String> items, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (values == null)
      throw new NullPointerException("values");
    if (items == null)
      throw new NullPointerException("items");

    for (String item : items)
    {

      boolean found = false;

      for (String value : values)
      {
        if (item == null && value == null)
          continue;
        if (item == null || value == null)
          return false;
        if (equal(item, value, locale, collator, caseSensitive))
          found = true;
      }

      if (!found)
        return false;
    }

    return true;
  }

  /**
   * Returns true if all characters are contained in a string. Order is not taken into consideration.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean containsAny(String value, char[] characters)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (characters == null)
      throw new NullPointerException("characters");

    for (char ch : characters)
      if (value.indexOf(ch) >= 0)
        return true;

    return false;
  }

  /**
   * Returns true if any of the strings are contained in a string. Order is not taken into consideration. Uses the CurrentLocale
   * StringComparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean containsAny(String value, Iterable<String> parts)
  {
    return containsAny(value, parts, StringComparison.CurrentLocale);
  }

  /**
   * Returns true if any of the strings are contained in a string. Order is not taken into consideration. If a part is null then it is
   * ignored.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean containsAny(String value, Iterable<String> parts, StringComparison stringComparison)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (parts == null)
      throw new NullPointerException("parts");

    // check if parts contained
    for (String part : parts)
      if (part != null)
        if (contains(value, part, stringComparison))
          return true;

    return false;
  }

  /**
   * Returns true if any of the strings are contained in a string. Order is not taken into consideration. If a part is null then it is
   * ignored. Uses a culture-aware higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean containsAny(String value, Iterable<String> parts, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (parts == null)
      throw new NullPointerException("parts");

    // check if parts contained
    for (String part : parts)
      if (part != null)
        if (contains(value, part, locale, collator, caseSensitive))
          return true;

    return false;
  }

  /**
   * Returns true if any item in items is contained in the values string collection. Order is not taken into consideration. You may use null
   * elements for values and items.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean containsAny(Iterable<String> values, Iterable<String> items, StringComparison stringComparison)
  {
    if (values == null)
      throw new NullPointerException("values");
    if (items == null)
      throw new NullPointerException("items");

    for (String item : items)
      for (String value : values)
      {
        if (item == null && value == null)
          return true;
        if (item == null || value == null)
          continue;
        if (equal(item, value, stringComparison))
          return true;
      }

    return false;
  }

  /**
   * Returns true if any item in items is contained in the values string collection. Order is not taken into consideration. You may use null
   * elements for values and items. Uses a culture-aware higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean
      containsAny(Iterable<String> values, Iterable<String> items, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (values == null)
      throw new NullPointerException("values");
    if (items == null)
      throw new NullPointerException("items");

    for (String item : items)
      for (String value : values)
      {
        if (item == null && value == null)
          return true;
        if (item == null || value == null)
          continue;
        if (equal(item, value, locale, collator, caseSensitive))
          return true;
      }

    return false;
  }

  /**
   * Similar to String.Substring
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds.
   */
  public static String copy(String value, int startIndex, int endIndex)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (startIndex < 0 || startIndex > endIndex)
      throw new IndexOutOfBoundsException("startIndex=" + startIndex + " endIndex=" + endIndex);
    if (endIndex > value.length())
      throw new IndexOutOfBoundsException("endIndex=" + endIndex + " length=" + value.length());

    return value.substring(startIndex, endIndex);
  }

  /**
   * Returns the number of occurences of a character in a character array.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int count(char[] array, char ch)
  {
    if (array == null)
      throw new NullPointerException("array");

    int count = 0;
    for (char c : array)
      if (c == ch)
        count++;

    return count;
  }

  /**
   * Returns the number of occurences of a character in a string value.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int count(String value, char character)
  {
    if (value == null)
      throw new NullPointerException("value");

    int result = 0;

    for (char s : value.toCharArray())
      if (s == character)
        result++;

    return result;
  }

  /**
   * Returns the number of occurences of a string element in a string value, using the CurrentLocale StringComparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int count(String value, String element)
  {
    return count(value, element, StringComparison.CurrentLocale);
  }

  /**
   * Returns the number of occurences of a string element in a string value, using the specified string comparison type.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int count(String value, String element, StringComparison stringComparison)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (element == null)
      throw new NullPointerException("element");

    int valLen = value.length();

    if (valLen <= 0 || element.length() <= 0)
      return 0;

    int result = 0;
    int index = 0;

    while ((index = indexOf(value, element, index, valLen - index, stringComparison)) >= 0)
    {
      index++;
      result++;
    }

    return result;
  }

  /**
   * Returns the number of occurences of a string element in a string value, using the specified string comparison type. Uses a
   * culture-aware higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int count(String value, String element, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (element == null)
      throw new NullPointerException("element");

    int valLen = value.length();

    if (valLen <= 0 || element.length() <= 0)
      return 0;

    int result = 0;
    int index = 0;

    while ((index = indexOf(value, element, index, valLen - index, locale, collator, caseSensitive)) >= 0)
    {
      index++;
      result++;
    }

    return result;
  }

  /**
   * Performs a cropStart and cropEnd, returning the result
   * 
   * @throws NullPointerException An argument is null
   */
  public static String crop(String value, char except)
  {
    return cropStart(cropEnd(value, except), except);
  }

  /**
   * Performs a cropStart and cropEnd, returning the result
   * 
   * @throws NullPointerException An argument is null
   */
  public static String crop(String value, char[] except)
  {
    return cropStart(cropEnd(value, except), except);
  }

  /**
   * Crops all characters from the start of the given string, until the except character is encountered
   * 
   * @throws NullPointerException An argument is null
   */
  public static String cropStart(String value, char except)
  {
    return cropStart(value, new char[] {except});
  }

  /**
   * Crops all characters from the start of the given string, until a character is encountered which exists in the given exception array
   * 
   * @throws NullPointerException An argument is null
   */
  public static String cropStart(String value, char[] except)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (except == null)
      throw new NullPointerException("except");

    int startIndex = 0;
    while (startIndex <= value.length() - 1 && !contains(except, value.charAt(startIndex)))
      startIndex++;

    return value.substring(startIndex);
  }

  /**
   * Crops all characters from the end of the given string, until the except character is encountered
   * 
   * @throws NullPointerException An argument is null
   */
  public static String cropEnd(String value, char except)
  {
    return cropEnd(value, new char[] {except});
  }

  /**
   * Crops all characters from the end of the given string, until a character is encountered which exists in the given exception array
   * 
   * @throws NullPointerException An argument is null
   */
  public static String cropEnd(String value, char[] except)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (except == null)
      throw new NullPointerException("except");

    int endIndex = value.length() - 1;
    while (endIndex > 0 && !contains(except, value.charAt(endIndex)))
      endIndex--;

    return value.substring(0, endIndex + 1);
  }

  /**
   * Returns CR, LF or CRLF, depending on the frequency of line separators found in the given text data.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String detectLineSeparator(String text)
  {
    return detectLineSeparator(text, 2.0f);
  }

  /**
   * Returns CR, LF or CRLF, depending on the frequency of line separators found in the given text data. Accepts a ratio, to ensure the
   * decision is not swayed by CRs or LFs appearing randomly in the code. For example a value of 2.0f for a CR End-Of-Line (EOL) terminated
   * file would return CR if the file contains twice the number of CRs than the number of LFs.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String detectLineSeparator(String text, float ratio)
  {
    if (text == null)
      throw new NullPointerException("text");

    int crs = count(text, CONSTANT.CR_CHAR);
    int lfs = count(text, CONSTANT.LF_CHAR);

    // multiply to ensure random CR/LFs used in source do not affect the outcome
    if (crs > lfs * ratio)
      return CONSTANT.CR;
    else if (lfs > crs * ratio)
      return CONSTANT.LF;
    else
      return CONSTANT.CRLF;
  }

  /**
   * Deletes the specified range of characters from the string.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds.
   */
  public static String delete(String value, int startIndex, int endIndex)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (startIndex < 0 || startIndex > endIndex)
      throw new IndexOutOfBoundsException("startIndex=" + startIndex + " endIndex=" + endIndex);
    if (endIndex > value.length())
      throw new IndexOutOfBoundsException("endIndex=" + endIndex + " length=" + value.length());

    return value.substring(0, startIndex) + value.substring(endIndex);
  }

  /**
   * Concatenates the given values using their ToString method and appending the given delimiter between all values. Returns String.Empty if
   * an empty or null collection was provided. Ignores null collection items.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String delimit(Iterable<String> values, String delimiter)
  {
    return delimit(values, delimiter, null);
  }

  /**
   * Concatenates the given values using their ToString method and appending the given delimiter between all values. Returns String.Empty if
   * an empty or null collection was provided. Substitutes null items with a null-replacement value, if provided and is not null.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String delimit(Iterable<String> values, String delimiter, String nullReplacementValue)
  {
    if (values == null)
      throw new NullPointerException("values");
    if (delimiter == null)
      throw new NullPointerException("delimiter");

    StringBuilder sb = new StringBuilder(256);

    for (String value : values)
      if (value != null)
      {
        sb.append(value.toString());
        sb.append(delimiter);
      } else
      // append null replacement
      if (nullReplacementValue != null)
      {
        sb.append(nullReplacementValue);
        sb.append(delimiter);
      }

    if (sb.length() > 0)
      return sb.subSequence(0, sb.length() - delimiter.length()).toString();

    return CONSTANT.EMPTY_STRING;
  }

  /**
   * Concatenates the given values using their ToString method and appending the given delimiter between all values. Returns String.Empty if
   * an empty or null collection was provided. Ignores null collection items.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String delimit(String[] values, String delimiter)
  {
    return delimit(values, delimiter, null);
  }

  /**
   * Concatenates the given values using their ToString method and appending the given delimiter between all values. Returns String.Empty if
   * an empty or null collection was provided. Substitutes null items with a null-replacement value, if provided and is not null.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String delimit(String[] values, String delimiter, String nullReplacementValue)
  {
    if (values == null)
      throw new NullPointerException("values");
    if (delimiter == null)
      throw new NullPointerException("delimiter");

    StringBuilder sb = new StringBuilder(256);

    for (String value : values)
      if (value != null)
      {
        sb.append(value.toString());
        sb.append(delimiter);
      } else
      // append null replacement
      if (nullReplacementValue != null)
      {
        sb.append(nullReplacementValue);
        sb.append(delimiter);
      }

    if (sb.length() > 0)
      return sb.subSequence(0, sb.length() - delimiter.length()).toString();

    return CONSTANT.EMPTY_STRING;
  }

  /**
   * Concatenates the given chars with the given delimiter between all values. Returns String.Empty if an empty or null collection was
   * provided.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String delimit(char[] values, String delimiter)
  {
    if (values == null)
      throw new NullPointerException("values");
    if (delimiter == null)
      throw new NullPointerException("delimiter");

    StringBuilder sb = new StringBuilder(256);

    for (char value : values)
    {
      sb.append(value);
      sb.append(delimiter);
    }

    if (sb.length() > 0)
      return sb.subSequence(0, sb.length() - delimiter.length()).toString();

    return CONSTANT.EMPTY_STRING;
  }

  /**
   * Returns true if the value ends with a suffix.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean endsWith(String value, char suffix)
  {
    if (value == null)
      throw new NullPointerException("value");

    if (value.length() == 0)
      return false;

    return value.charAt(value.length() - 1) == suffix;
  }

  /**
   * Returns true if a value ends with a suffix. Uses the CurrentLocale string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean endsWith(String value, String suffix)
  {
    return endsWith(value, suffix, StringComparison.CurrentLocale);
  }

  /**
   * Returns true if a value ends with a suffix. Uses the specified string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean endsWith(String value, String suffix, StringComparison stringComparison)
  {
    return lastIndexOf(value, suffix, value.length() - 1, 1, stringComparison) >= 0;
  }

  /**
   * Returns true if a value ends with a suffix. Uses a culture-aware higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean endsWith(String value, String suffix, Locale locale, Collator collator, boolean caseSensitive)
  {
    return lastIndexOf(value, suffix, value.length() - 1, 1, locale, collator, caseSensitive) >= 0;
  }

  /**
   * Returns true if a value equals with another. Uses the CurrentLocale string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean equal(String a, String b)
  {
    return equal(a, b, StringComparison.CurrentLocale);
  }

  /**
   * Returns true if a value equals with another. Uses the specified string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean equal(String a, String b, StringComparison stringComparison)
  {
    if (a == null)
      throw new NullPointerException("a");
    if (b == null)
      throw new NullPointerException("b");

    switch(stringComparison)
    {
      case CurrentLocale:
        return equalLocaleSensitive(a, b, CURRENT_LOCALE, CURRENT_LOCALE_COLLATOR, true);
      case CurrentLocaleIgnoreCase:
        return equalLocaleSensitive(a, b, CURRENT_LOCALE, CURRENT_LOCALE_COLLATOR, false);
      case InvariantLocale:
        return equalLocaleSensitive(a, b, INVARIANT_LOCALE, INVARIANT_LOCALE_COLLATOR, true);
      case InvariantLocaleIgnoreCase:
        return equalLocaleSensitive(a, b, INVARIANT_LOCALE, INVARIANT_LOCALE_COLLATOR, false);
      case Ordinal:
        return equalOrdinal(a, b, true);
      case OrdinalIgnoreCase:
        return equalOrdinal(a, b, false);
      default:
        throw new IllegalArgumentException("stringComparison has an unexpected value: " + stringComparison.toString());
    }
  }

  /**
   * Returns true if a value equals with another. Uses a culture-aware higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean equal(String a, String b, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (a == null)
      throw new NullPointerException("a");
    if (b == null)
      throw new NullPointerException("b");

    return equalLocaleSensitive(a, b, locale, collator, caseSensitive);
  }

  /**
   * Locale-aware string equality comparison.
   */
  private static boolean equalLocaleSensitive(String a, String b, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (locale == null)
      throw new NullPointerException("locale");
    if (collator == null)
      throw new NullPointerException("collator");

    if (!caseSensitive)
    {
      a = a.toLowerCase(locale);
      b = b.toLowerCase(locale);
    }

    return collator.equals(a, b);
  }

  /**
   * Compares two strings for equality
   */
  private static boolean equalOrdinal(String a, String b, boolean caseSensitive)
  {
    if (a.length() != b.length())
      return false;

    return indexOfOrdinal(a, b, 0, 1, caseSensitive) == 0;
  }

  /**
   * Finds the first index encountered of a particular character. Returns -1 if not found.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int indexOf(char[] array, char ch)
  {
    if (array == null)
      throw new NullPointerException("array");

    for (int i = 0; i < array.length; i++)
      if (array[i] == ch)
        return i;

    return -1;
  }

  /**
   * Returns the first index where a part is encountered within a string value. If the part is not existent, -1 is returned. Uses the
   * CurrentLocale string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int indexOf(String value, String part)
  {
    return indexOf(value, part, 0, value.length(), StringComparison.CurrentLocale);
  }

  /**
   * Returns the first index where a part is encountered within a string value. If the part is not existent, -1 is returned. Uses the
   * specified string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int indexOf(String value, String part, StringComparison stringComparison)
  {
    return indexOf(value, part, 0, value.length(), stringComparison);
  }

  /**
   * Returns the first index where a part is encountered within a string value. If the part is not existent, -1 is returned. Uses a
   * culture-aware higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int indexOf(String value, String part, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (part == null)
      throw new NullPointerException("part");

    return indexOfLocaleSensitive(value, part, 0, value.length(), locale, collator, caseSensitive);
  }

  /**
   * Returns the first, second, third, etc. index where a part is encountered within a string value, starting at the specified index and
   * moving to the right of the string. If the part is not existent, -1 is returned. Uses the specified string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int indexOf(String value, String part, int startIndex, int count, StringComparison stringComparison)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (part == null)
      throw new NullPointerException("part");

    switch(stringComparison)
    {
      case CurrentLocale:
        return indexOfLocaleSensitive(value, part, startIndex, count, CURRENT_LOCALE, CURRENT_LOCALE_COLLATOR, true);
      case CurrentLocaleIgnoreCase:
        return indexOfLocaleSensitive(value, part, startIndex, count, CURRENT_LOCALE, CURRENT_LOCALE_COLLATOR, false);
      case InvariantLocale:
        return indexOfLocaleSensitive(value, part, startIndex, count, INVARIANT_LOCALE, INVARIANT_LOCALE_COLLATOR, true);
      case InvariantLocaleIgnoreCase:
        return indexOfLocaleSensitive(value, part, startIndex, count, INVARIANT_LOCALE, INVARIANT_LOCALE_COLLATOR, false);
      case Ordinal:
        return indexOfOrdinal(value, part, startIndex, count, true);
      case OrdinalIgnoreCase:
        return indexOfOrdinal(value, part, startIndex, count, false);
      default:
        throw new IllegalArgumentException("stringComparison has an unexpected value: " + stringComparison.toString());
    }
  }

  /**
   * Returns the first, second, third, etc. index where a part is encountered within a string value, starting at the specified index and
   * moving to the right of the string. If the part is not existent, -1 is returned. Uses a culture-aware higher performance string
   * comparison.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds
   * @throws IllegalArgumentException An argument is out of range
   */
  public static int indexOf(String value, String part, int startIndex, int count, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (part == null)
      throw new NullPointerException("part");

    return indexOfLocaleSensitive(value, part, startIndex, count, locale, collator, caseSensitive);
  }

  /**
   * Performs a locale-sensitive string comparison
   * 
   * @param value The source value
   * @param part The value we're looking for
   * @param startIndex The index in source where we start looking
   * @param count Up to how many positions to look forward
   * @param locale The locale to use for toLowercase conversions, if a case sensitive comparison
   * @param collator The collator to use to compare locale-sensitive strings
   * @param caseSensitive Whether case sensitive or not
   * 
   * @return The index where the part was found in value, or -1 if not found.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds
   * @throws IllegalArgumentException An argument is out of range
   */
  private static int indexOfLocaleSensitive(String value, String part, int startIndex, int count, Locale locale, Collator collator,
                                            boolean caseSensitive)
  {
    if (locale == null)
      throw new NullPointerException("locale");
    if (collator == null)
      throw new NullPointerException("collator");

    int valueLen = value.length();
    int partLen = part.length();
    int endIndex = startIndex + count;

    if (startIndex > valueLen)
      throw new IndexOutOfBoundsException("startIndex=" + startIndex + " valueLen=" + valueLen);

    if (valueLen == 0)
      if (partLen == 0)
        return 0;
      else
        return -1;

    if (startIndex < 0)
      throw new IndexOutOfBoundsException("startIndex=" + startIndex);
    if (count < 0 || endIndex > valueLen)
      throw new IllegalArgumentException("count=" + count + " endIndex=" + endIndex + " valueLen=" + valueLen);

    // select comparison type
    if (caseSensitive)
      // case-sensitive comparison from start position to end position
      for (int i = startIndex; i < endIndex; i++)
        // some Strings have different lengths but are equal
        // e.g. for US: "\u00C4" "LATIN CAPITAL LETTER A WITH DIAERESIS" (Ä) and "\u0041\u0308" "LATIN CAPITAL LETTER A" (A) -
        // "COMBINING DIAERESIS" (̈)
        // therefore we have to examine whether the part is equal to ANY portion of the length of the string
        for (int j = 1; j < valueLen - i + 1; j++)
        {
          String val = value.substring(i, i + j);
          if (collator.equals(val, part))
            return i;
        }
    else
    {
      String lowerPart = part.toLowerCase(locale);

      // case-insensitive comparison from start position to end position
      for (int i = startIndex; i < endIndex; i++)
        // some Strings have different lengths but are equal such as the diaeresis example above,
        // additionally (for this case) in some instances the string lengths of the toLowercase conversion will differ
        // e.g. for LT: "\u00cc" (Ì) becomes 3 characters (i̇̀)
        for (int j = 1; j < valueLen - i + 1; j++)
        {
          String val = value.substring(i, i + j);
          if (collator.equals(val.toLowerCase(locale), lowerPart))
            return i;
        }
    }

    return -1;
  }

  /**
   * Performs an ordinal string comparison.
   * 
   * @param value The source value
   * @param part The value we're looking for
   * @param startIndex The index in source where we start looking
   * @param count Up to how many positions to look forward
   * @param caseSensitive Whether case sensitive or not
   * 
   * @return The index where the part was found in value, or -1 if not found.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds
   * @throws IllegalArgumentException An argument is out of range
   */
  private static int indexOfOrdinal(String value, String part, int startIndex, int count, boolean caseSensitive)
  {
    int valueLen = value.length();
    int partLen = part.length();

    if (startIndex > valueLen)
      throw new IndexOutOfBoundsException("startIndex=" + startIndex);
    if (valueLen == 0)
      if (partLen == 0)
        return 0;
      else
        return -1;

    if (startIndex < 0)
      throw new IndexOutOfBoundsException("startIndex=" + startIndex);
    if ((count < 0) || startIndex + count > valueLen || startIndex + count < 0)
      throw new IllegalArgumentException("count=" + count + " startIndex=" + startIndex + " partLen=" + partLen);

    // calculate the least amount of time we have to iterate through string characters
    int minCount = valueLen - partLen + 1;
    if (count + startIndex < minCount)
      minCount = count + startIndex;

    char[] vArr = value.toCharArray();
    char[] pArr = part.toCharArray();

    if (caseSensitive)
      for (int i = startIndex; i < minCount; i++)
      {
        boolean found = true;
        for (int j = 0; j < partLen; j++)
        {
          char vCh = vArr[i + j];
          char pCh = pArr[j];
          // compare chars
          if (vCh != pCh)
          {
            found = false;
            break;
          }
        }
        if (found)
          return i;
      }
    else
      // case insensitive
      for (int i = startIndex; i < minCount; i++)
      {
        boolean found = true;
        for (int j = 0; j < partLen; j++)
        {
          char vCh = vArr[i + j];
          char pCh = pArr[j];

          // ASCII letters are converted to lowercase
          if ((vCh >= 65 && vCh <= 90) || (vCh >= 97 && vCh <= 122))
          {
            vCh = Character.toLowerCase(vCh);
            pCh = Character.toLowerCase(pCh);
          }
          // compare chars
          if (vCh != pCh)
          {
            found = false;
            break;
          }
        }
        if (found)
          return i;
      }

    return -1;
  }

  /**
   * Returns the index of the first/second/third/etc. occurrence of an element in a value, starting from the left and moving forward.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException Occurrence is out of range.
   */
  public static int indexOf(String value, String part, int occurrence, StringComparison stringComparison)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (part == null)
      throw new NullPointerException("part");
    if (value.length() <= 0 || part.length() <= 0)
      return 0;
    if (occurrence <= 0)
      throw new IllegalArgumentException("occurrence" + occurrence);

    int result = 0;
    int index = 0;

    while ((index = indexOf(value, part, index, value.length() - index, stringComparison)) >= 0)
    {
      result++;

      // check if occurrence reached, if so we found our result
      if (result == occurrence)
        return index;

      index++;
    }

    return -1;
  }

  /**
   * Returns the index of the first/second/third/etc. occurrence of an element in a value, starting from the left and moving forward. Uses a
   * culture-aware higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException Occurrence is out of range.
   */
  public static int indexOf(String value, String part, int occurrence, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (part == null)
      throw new NullPointerException("part");
    if (value.length() <= 0 || part.length() <= 0)
      return 0;
    if (occurrence <= 0)
      throw new IllegalArgumentException("occurrence=" + occurrence);

    int result = 0;
    int index = 0;

    while ((index = indexOf(value, part, index, value.length() - index, locale, collator, caseSensitive)) >= 0)
    {
      result++;

      // check if occurrence reached, if so we found our result
      if (result == occurrence)
        return index;

      index++;
    }

    return -1;
  }

  /**
   * Inserts a string in a position in the given string.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException Index is out of range.
   */
  public static String insert(String value, int index, String insertedValue)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (insertedValue == null)
      throw new NullPointerException("insertedValue");
    if (index < 0 || index > value.length())
      throw new IndexOutOfBoundsException("index=" + index + " length=" + value.length());

    return value.substring(0, index) + insertedValue + value.substring(index);
  }

  /**
   * Returns true if the given string is null or empty.
   */
  public static boolean isNullOrEmpty(String value)
  {
    return value == null || value.length() == 0;
  }

  /**
   * Returns true if the given string is null or contains only whitespace chars (' ', '\t', '\r' and '\n').
   * 
   * @author Martin Lamparski
   */
  public static boolean isNullOrBlank(String value)
  {
    return value == null || trim(value).length() == 0;
  }

  /**
   * Finds the last index encountered of a particular character. Returns -1 if not found.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int lastIndexOf(char[] array, char ch)
  {
    if (array == null)
      throw new NullPointerException("array");

    for (int i = array.length - 1; i >= 0; i--)
      if (array[i] == ch)
        return i;

    return -1;
  }

  /**
   * Returns the last index where a part is encountered within a string value. If the part is not existent, -1 is returned. Uses the
   * CurrentLocale string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int lastIndexOf(String value, String part)
  {
    return lastIndexOf(value, part, StringComparison.CurrentLocale);
  }

  /**
   * Returns the last index where a part is encountered within a string value. If the part is not existent, -1 is returned. Uses the
   * specified string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int lastIndexOf(String value, String part, StringComparison stringComparison)
  {
    return lastIndexOf(value, part, value.length() - 1, value.length(), stringComparison);
  }

  /**
   * Returns the last index where a part is encountered within a string value. If the part is not existent, -1 is returned. Uses a
   * culture-aware higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static int lastIndexOf(String value, String part, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (part == null)
      throw new NullPointerException("part");

    return lastIndexOfLocaleSensitive(value, part, value.length() - 1, value.length(), locale, collator, caseSensitive);
  }

  /**
   * Returns the first, second, third, etc. index where a part is encountered within a string value, starting at the specified index and
   * moving to the left of the string. If the part is not existent, -1 is returned. Uses the specified string comparison.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds
   * @throws IllegalArgumentException An argument is out of range
   */
  public static int lastIndexOf(String value, String part, int startIndex, int count, StringComparison stringComparison)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (part == null)
      throw new NullPointerException("part");

    switch(stringComparison)
    {
      case CurrentLocale:
        return lastIndexOfLocaleSensitive(value, part, startIndex, count, CURRENT_LOCALE, CURRENT_LOCALE_COLLATOR, true);
      case CurrentLocaleIgnoreCase:
        return lastIndexOfLocaleSensitive(value, part, startIndex, count, CURRENT_LOCALE, CURRENT_LOCALE_COLLATOR, false);
      case InvariantLocale:
        return lastIndexOfLocaleSensitive(value, part, startIndex, count, INVARIANT_LOCALE, INVARIANT_LOCALE_COLLATOR, true);
      case InvariantLocaleIgnoreCase:
        return lastIndexOfLocaleSensitive(value, part, startIndex, count, INVARIANT_LOCALE, INVARIANT_LOCALE_COLLATOR, false);
      case Ordinal:
        return lastIndexOfOrdinal(value, part, startIndex, count, true);
      case OrdinalIgnoreCase:
        return lastIndexOfOrdinal(value, part, startIndex, count, false);
      default:
        throw new IllegalArgumentException("stringComparison has an unexpected value: " + stringComparison.toString());
    }
  }

  /**
   * Returns the first, second, third, etc. index where a part is encountered within a string value, starting at the specified index and
   * moving to the left of the string. If the part is not existent, -1 is returned. Uses a culture-aware higher performance string
   * comparison.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds
   * @throws IllegalArgumentException An argument is out of range
   */
  public static int lastIndexOf(String value, String part, int startIndex, int count, Locale locale, Collator collator,
                                boolean caseSensitive)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (part == null)
      throw new NullPointerException("part");

    return lastIndexOfLocaleSensitive(value, part, startIndex, count, locale, collator, caseSensitive);
  }

  /**
   * Performs a locale-sensitive string comparison
   * 
   * @param value The source value
   * @param part The value we're looking for
   * @param startIndex The index in source where we start looking
   * @param count Up to how many positions to look backward
   * @param locale The locale to use for toLowercase conversions, if a case sensitive comparison
   * @param collator The collator to use to compare locale-sensitive strings
   * @param caseSensitive Whether case sensitive or not
   * 
   * @return The index where the part was found in value, or -1 if not found.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds
   * @throws IllegalArgumentException An argument is out of range
   */
  private static int lastIndexOfLocaleSensitive(String value, String part, int startIndex, int count, Locale locale, Collator collator,
                                                boolean caseSensitive)
  {
    if (locale == null)
      throw new NullPointerException("locale");
    if (collator == null)
      throw new NullPointerException("collator");

    int valueLen = value.length();
    int partLen = part.length();
    int endIndex = startIndex - count + 1;

    if ((valueLen == 0) && ((startIndex == -1) || (startIndex == 0)))
      if (partLen != 0)
        return -1;
      else
        return 0;

    if ((startIndex < 0) || (startIndex > valueLen))
      throw new IndexOutOfBoundsException("startIndex=" + startIndex + " valueLen=" + valueLen);

    if (count < 0 || endIndex < 0)
      throw new IllegalArgumentException("count=" + count + " startIndex=" + startIndex);

    // select comparison type
    if (caseSensitive)
      // case-sensitive comparison from start position to end position
      for (int i = startIndex + 1; i > endIndex; i--)
        // some Strings have different lengths but are equal
        // e.g. for US: "\u00C4" "LATIN CAPITAL LETTER A WITH DIAERESIS" (Ä) and "\u0041\u0308" "LATIN CAPITAL LETTER A" (A) -
        // "COMBINING DIAERESIS" (̈)
        // therefore we have to examine whether the part is equal to ANY portion of the length of the string
        for (int j = i - 1; j >= 0; j--)
        {
          String val = value.substring(j, i);
          if (collator.equals(val, part))
            return j;
        }
    else
    {
      String lowerPart = part.toLowerCase(locale);

      // case-insensitive comparison from start position to end position
      for (int i = startIndex + 1; i > endIndex; i--)
        // some Strings have different lengths but are equal such as the diaeresis example above,
        // additionally (for this case) in some instances the string lengths of the toLowercase conversion will differ
        // e.g. for LT: "\u00cc" (Ì) becomes 3 characters (i̇̀)
        for (int j = i - 1; j >= 0; j--)
        {
          String val = value.substring(j, i);
          if (collator.equals(val.toLowerCase(locale), lowerPart))
            return j;
        }
    }

    return -1;
  }

  /**
   * Performs an ordinal string comparison.
   * 
   * @param value The source value
   * @param part The value we're looking for
   * @param startIndex The index in source where we start looking
   * @param count Up to how many positions to look forward
   * @param caseSensitive Whether case sensitive or not
   * 
   * @return The index where the part was found in value, or -1 if not found.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds
   * @throws IllegalArgumentException An argument is out of range
   */
  private static int lastIndexOfOrdinal(String value, String part, int startIndex, int count, boolean caseSensitive)
  {
    int valueLen = value.length();
    int partLen = part.length();
    int endIndex = startIndex - count + 1;

    if ((valueLen == 0) && (startIndex == -1 || startIndex == 0))
      if (partLen != 0)
        return -1;
      else
        return 0;

    if ((startIndex < 0) || (startIndex > valueLen))
      throw new IndexOutOfBoundsException("startIndex=" + startIndex + " valueLen=" + valueLen);

    // following commented out: replaced by this
    if ((count < 0) || endIndex < 0)
      throw new IllegalArgumentException("count=" + count + " startIndex=" + startIndex);

    // calculate the least amount of time we have to iterate through string characters
    int minCount = startIndex - (valueLen - partLen);
    if (endIndex > minCount)
      minCount = endIndex;

    char[] vArr = value.toCharArray();
    char[] pArr = part.toCharArray();

    if (caseSensitive)
      for (int i = startIndex; i >= minCount; i--)
      {
        boolean found = true;
        for (int j = 0; j < partLen; j++)
          if (i - j < 0)
            found = false;
          else
          {
            char vCh = vArr[i - j];
            char pCh = pArr[partLen - j - 1];
            // compare chars
            if (vCh != pCh)
            {
              found = false;
              break;
            }
          }

        if (found)
          return i - partLen + 1;
      }
    else
      // case insensitive
      for (int i = startIndex; i >= minCount; i--)
      {
        boolean found = true;
        for (int j = 0; j < partLen; j++)
          if (i - j < 0)
            found = false;
          else
          {
            char vCh = vArr[i - j];
            char pCh = pArr[partLen - j - 1];

            // ASCII letters are converted to lowercase
            if ((vCh >= 65 && vCh <= 90) || (vCh >= 97 && vCh <= 122))
            {
              vCh = Character.toLowerCase(vCh);
              pCh = Character.toLowerCase(pCh);
            }
            // compare chars
            if (vCh != pCh)
            {
              found = false;
              break;
            }
          }

        if (found)
          return i - partLen + 1;
      }

    return -1;
  }

  /**
   * Returns the index of the first/second/third/etc. occurrence of an element in a value, starting from the right and moving backward.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException Occurrence is out of range.
   */
  public static int lastIndexOf(String value, String part, int occurrenceFromEnd, StringComparison stringComparison)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (part == null)
      throw new NullPointerException("part");
    if (value.length() <= 0 || part.length() <= 0)
      return 0;
    if (occurrenceFromEnd <= 0)
      throw new IllegalArgumentException("occurrenceFromEnd" + occurrenceFromEnd);

    int result = 0;
    int index = value.length() - 1;

    while ((index = lastIndexOf(value, part, index, index + 1, stringComparison)) >= 0)
    {
      result++;

      // check if occurrence reached, if so we found our result
      if (result == occurrenceFromEnd)
        return index;

      index--;
      if (index < 0)
        break;
    }

    return -1;
  }

  /**
   * Returns the index of the first/second/third/etc. occurrence of an element in a value, starting from the right and moving backward. Uses
   * a culture-aware higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException Occurrence is out of range.
   */
  public static int lastIndexOf(String value, String part, int occurrenceFromEnd, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (part == null)
      throw new NullPointerException("part");
    if (value.length() <= 0 || part.length() <= 0)
      return 0;
    if (occurrenceFromEnd <= 0)
      throw new IllegalArgumentException("occurrenceFromEnd" + occurrenceFromEnd);

    int result = 0;
    int index = value.length() - 1;

    while ((index = lastIndexOf(value, part, index, index + 1, locale, collator, caseSensitive)) >= 0)
    {
      result++;

      // check if occurrence reached, if so we found our result
      if (result == occurrenceFromEnd)
        return index;

      index--;
      if (index < 0)
        break;
    }

    return -1;
  }

  /**
   * Allows for matching a string to another, using Equals, StartsWith, EndsWith or Contains and a string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean match(String a, MatchType stringMatch, String b, StringComparison stringComparison)
  {
    if (a == null)
      throw new NullPointerException("a");
    if (b == null)
      throw new NullPointerException("b");

    switch(stringMatch)
    {
      case Equals:
        return equal(a, b, stringComparison);
      case Contains:
        return indexOf(a, b, stringComparison) >= 0;
      case StartsWith:
        return startsWith(a, b, stringComparison);
      case EndsWith:
        return endsWith(a, b, stringComparison);
      default:
        throw new IllegalArgumentException("Unrecognized string match type: " + stringMatch);
    }
  }

  /**
   * Allows for matching a string to another, using Equals, StartsWith, EndsWith or Contains and a string comparison. Uses a culture-aware
   * higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean match(String a, MatchType stringMatch, String b, Locale locale, Collator collator, boolean caseSensitive)
  {
    if (a == null)
      throw new NullPointerException("a");
    if (b == null)
      throw new NullPointerException("b");

    switch(stringMatch)
    {
      case Equals:
        return equal(a, b, locale, collator, caseSensitive);
      case Contains:
        return indexOf(a, b, locale, collator, caseSensitive) >= 0;
      case StartsWith:
        return startsWith(a, b, locale, collator, caseSensitive);
      case EndsWith:
        return endsWith(a, b, locale, collator, caseSensitive);
      default:
        throw new IllegalArgumentException("Unrecognized string match type: " + stringMatch);
    }
  }

  /**
   * Right-aligns the characters in this instance, padding on the left a specified character
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An invalid argument was given.
   */
  public static String padRight(String value, int totalLength, char pad)
  {
    if (value == null)
      throw new NullPointerException("value");

    if (totalLength < 0)
      throw new IllegalArgumentException("totalLength=" + totalLength);

    int add = totalLength - value.length();
    if (add < 0)
      throw new IllegalArgumentException("totalLength=" + totalLength + " len=" + value.length());

    StringBuilder str = new StringBuilder(value);
    char[] ch = new char[add];
    Arrays.fill(ch, pad);
    str.append(ch);

    return str.toString();
  }

  /**
   * Left-aligns the characters in this instance, padding on the right a specified character
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException An invalid argument was given.
   */
  public static String padLeft(String value, int totalLength, char pad)
  {
    if (value == null)
      throw new NullPointerException("value");

    if (totalLength < 0)
      throw new IllegalArgumentException("totalLength=" + totalLength);

    int add = totalLength - value.length();
    if (add < 0)
      throw new IllegalArgumentException("totalLength=" + totalLength + " len=" + value.length());

    StringBuilder str = new StringBuilder(value);
    char[] ch = new char[add];
    Arrays.fill(ch, pad);
    str.insert(0, ch);

    return str.toString();
  }

  /**
   * Parses a boolean from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static boolean parseBool(String value)
  {
    return parseBool(value, CONSTANT.TRUE, CONSTANT.FALSE, StringComparison.OrdinalIgnoreCase);
  }

  /**
   * Parses a boolean from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static boolean parseBool(String value, String trueValue, String falseValue, StringComparison comparisonType)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (trueValue == null)
      throw new NullPointerException("trueValue");
    if (falseValue == null)
      throw new NullPointerException("falseValue");

    // parse
    if (equal(trueValue, value, comparisonType))
      return true;
    else if (equal(falseValue, value, comparisonType))
      return false;

    // sanity check
    throw new NumberFormatException("Value given was neither " + trueValue + " nor " + falseValue + ": " + value);
  }

  /**
   * Parses the first character from a string.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static char parseChar(String value)
  {
    return parseChar(value, Character.MIN_VALUE, Character.MAX_VALUE);
  }

  /**
   * Parses the first character from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static char parseChar(String value, char minValue, char maxValue)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (value.length() != 1)
      throw new NumberFormatException("Value is not 1 character long: " + value);

    char result = value.charAt(0);

    // sanity check
    if (result < minValue)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + minValue + ").");
    if (result > maxValue)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + maxValue + ").");

    return result;
  }

  /**
   * Parses a DateTime from a string. Uses common ISO formats as well as some locale-specific formats. See
   * http://joda-time.sourceforge.net/apidocs/org/joda/time/format/DateTimeFormat.html ISO format examples:
   * <ul>
   * <li>yyyyMMdd'T'HHmmssZ</li>
   * <li>yyyyMMdd'T'HHmmss.SSSZ</li>
   * <li>yyyy-MM-dd</li>
   * <li>yyyy-MM-dd'T'HH:mm:ss.SSS</li>
   * <li>yyyy-MM-dd'T'HH:mm:ssZZ</li>
   * <li>yyyy-MM-dd'T'HH:mm:ss.SSSZZ</li>
   * <li>yyyy-MM-dd HH:mm:ss</li>
   * <li>yyyy-MM-dd HH:mm:ss.SSSSSSS</li>
   * <li>yyyy-MM-dd'T'HH:mm:ss</li>
   * <li>yyyy-MM-dd'T'HH:mm:ss.SSSSSSS</li>
   * </ul>
   * <p/>
   * Also supports non-ISO formats such as yyyy/MM/dd. Furthermore attempts to parse using locale-specific parsers.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static LocalDateTime parseDateTime(String value)
  {
    LocalDateTime result = null;

    // attempt ISO standard parsing
    try
    {
      result = STANDARD_FORMATTERS.parseDateTime(value).toLocalDateTime();
    }
    catch(Throwable e)
    {
      // continues parsing attempts
    }

    if (result == null)
      // first try locale-specific date/time parsing
      for (int dateStyle = DateFormat.FULL; dateStyle <= DateFormat.SHORT; dateStyle++)
        for (int timeStyle = DateFormat.FULL; timeStyle <= DateFormat.SHORT; timeStyle++)
          if (result == null)
            try
            {
              // Parse with a default format
              Date date = DateFormat.getDateTimeInstance(dateStyle, timeStyle, CURRENT_LOCALE).parse(value);
              result = new LocalDateTime(date);

              break;
            }
            catch(ParseException e)
            {
              continue;
            }

    if (result == null)
      // now try locale-specific date parsing
      for (int dateStyle = DateFormat.FULL; dateStyle <= DateFormat.SHORT; dateStyle++)
        try
        {
          // Parse with a default format
          Date date = DateFormat.getDateInstance(dateStyle, CURRENT_LOCALE).parse(value);
          result = new LocalDateTime(date);
          break;
        }
        catch(ParseException e)
        {
          continue;
        }

    if (result == null)
      // lastly try locale-specific time parsing
      for (int timeStyle = DateFormat.FULL; timeStyle <= DateFormat.SHORT; timeStyle++)
        try
        {
          // Parse with a default format
          Date date = DateFormat.getTimeInstance(timeStyle, CURRENT_LOCALE).parse(value);
          result = new LocalDateTime(date);
          break;
        }
        catch(ParseException e)
        {
          continue;
        }

    if (result == null)
      throw new NumberFormatException("The specified date/time is not in an identifiable format: " + value);

    // sanity check
    if (result.compareTo(MIN_DATETIME) < 0)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + MIN_DATETIME + ").");
    if (result.compareTo(MAX_DATETIME) > 0)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + MAX_DATETIME + ").");

    return result;
  }

  /**
   * Parses a DateTime from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static LocalDateTime parseDateTime(String value, DateTimeFormatter formatter)
  {
    return parseDateTime(value, MIN_DATETIME, MAX_DATETIME, formatter);
  }

  /**
   * Parses a DateTime from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static LocalDateTime parseDateTime(String value, LocalDateTime minValue, LocalDateTime maxValue, DateTimeFormatter formatter)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (formatter == null)
      throw new NullPointerException("formatter");

    // parse
    LocalDateTime result = formatter.parseDateTime(value).toLocalDateTime();

    // sanity check
    if (result.compareTo(minValue) < 0)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + minValue + ").");
    if (result.compareTo(maxValue) > 0)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + maxValue + ").");

    return result;
  }

  /**
   * Parses a decimal from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static BigDecimal parseDecimal(String value)
  {
    if (value == null)
      throw new NullPointerException("value");

    // parse
    BigDecimal result;
    try
    {
      // check if the current locale uses the same decimal separator and grouping chars as expected by the parser
      if (DECIMAL_SEPARATOR != CONSTANT.COMMA_CHAR)
      {
        // remove grouping separators and replace decimal separators to commas
        String converted = StringUtils.replace(value, GROUPING_SEPARATOR + CONSTANT.EMPTY_STRING, CONSTANT.EMPTY_STRING,
            StringComparison.Ordinal);
        result = new BigDecimal(converted);
      } else
        result = new BigDecimal(value);
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The value '" + value + "' could not be parsed: " + e.getMessage());
    }

    return result;
  }

  /**
   * Parses a decimal from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static BigDecimal parseDecimal(String value, BigDecimal minValue, BigDecimal maxValue)
  {
    if (value == null)
      throw new NullPointerException("value");

    // parse
    BigDecimal result;
    try
    {
      // check if the current locale uses the same decimal separator and grouping chars as expected by the parser
      if (DECIMAL_SEPARATOR != CONSTANT.COMMA_CHAR)
      {
        // remove grouping separators and replace decimal separators to commas
        String converted = StringUtils.replace(value, GROUPING_SEPARATOR + CONSTANT.EMPTY_STRING, CONSTANT.EMPTY_STRING,
            StringComparison.Ordinal);
        result = new BigDecimal(converted);
      } else
        result = new BigDecimal(value);
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The value '" + value + "' could not be parsed: " + e.getMessage());
    }

    // sanity check
    if (result.compareTo(minValue) < 0)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + minValue + ").");
    if (result.compareTo(maxValue) > 0)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + maxValue + ").");

    return result;
  }

  /**
   * Parses a double from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static double parseDouble(String value)
  {
    return parseDouble(value, -Double.MAX_VALUE, Double.MAX_VALUE, true, true);
  }

  /**
   * Parses a double from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static double parseDouble(String value, double minValue, double maxValue, boolean allowInfinity, boolean allowNaN)
  {
    if (value == null)
      throw new NullPointerException("value");

    // parse
    double result;
    try
    {
      // check if the current locale uses the same decimal separator and grouping chars as expected by the parser
      if (DECIMAL_SEPARATOR != CONSTANT.COMMA_CHAR)
      {
        // remove grouping separators and replace decimal separators to commas
        String converted = StringUtils.replace(value, GROUPING_SEPARATOR + CONSTANT.EMPTY_STRING, CONSTANT.EMPTY_STRING,
            StringComparison.Ordinal);
        result = Double.parseDouble(converted);
      } else
        result = Double.parseDouble(value);
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The value '" + value + "' could not be parsed: " + e.getMessage());
    }

    if (allowInfinity)
      if (result == Double.POSITIVE_INFINITY || result == Double.NEGATIVE_INFINITY)
        return result;
    if (allowNaN)
      if (result == Double.NaN)
        return result;

    // sanity check
    if (result < minValue)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + minValue + ").");
    if (result > maxValue)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + maxValue + ").");

    return result;
  }

  /**
   * Parses a float from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static float parseFloat(String value)
  {
    return parseFloat(value, -Float.MAX_VALUE, Float.MAX_VALUE, true, true);
  }

  /**
   * Parses a float from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static float parseFloat(String value, float minValue, float maxValue, boolean allowInfinity, boolean allowNaN)
  {
    if (value == null)
      throw new NullPointerException("value");

    // parse
    float result;
    try
    {
      // check if the current locale uses the same decimal separator and grouping chars as expected by the parser
      if (DECIMAL_SEPARATOR != CONSTANT.COMMA_CHAR)
      {
        // remove grouping separators and replace decimal separators to commas
        String converted = StringUtils.replace(value, GROUPING_SEPARATOR + CONSTANT.EMPTY_STRING, CONSTANT.EMPTY_STRING,
            StringComparison.Ordinal);
        result = Float.parseFloat(converted);
      } else
        result = Float.parseFloat(value);
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The value '" + value + "' could not be parsed: " + e.getMessage());
    }

    if (allowInfinity)
      if (result == Double.POSITIVE_INFINITY || result == Double.NEGATIVE_INFINITY)
        return result;
    if (allowNaN)
      if (result == Double.NaN)
        return result;

    // sanity check
    if (result < minValue)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + minValue + ").");
    if (result > maxValue)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + maxValue + ").");

    return result;
  }

  /**
   * Parses a byte from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static byte parseInt8(String value)
  {
    return parseInt8(value, Byte.MIN_VALUE, Byte.MAX_VALUE);
  }

  /**
   * Parses a byte from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static byte parseInt8(String value, byte minValue, byte maxValue)
  {
    if (value == null)
      throw new NullPointerException("value");

    // parse
    byte result;
    try
    {
      result = Byte.parseByte(value);
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The value '" + value + "' could not be parsed: " + e.getMessage());
    }

    // sanity check
    if (result < minValue)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + minValue + ").");
    if (result > maxValue)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + maxValue + ").");

    return result;
  }

  /**
   * Parses a short from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static short parseInt16(String value)
  {
    return parseInt16(value, Short.MIN_VALUE, Short.MAX_VALUE);
  }

  /**
   * Parses a short from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static short parseInt16(String value, short minValue, short maxValue)
  {
    if (value == null)
      throw new NullPointerException("value");

    // parse
    short result;
    try
    {
      result = Short.parseShort(value);
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The value '" + value + "' could not be parsed: " + e.getMessage());
    }

    // sanity check
    if (result < minValue)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + minValue + ").");
    if (result > maxValue)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + maxValue + ").");

    return result;
  }

  /**
   * Parses an int from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static int parseInt32(String value)
  {
    return parseInt32(value, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  /**
   * Parses an int from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static int parseInt32(String value, int minValue, int maxValue)
  {
    if (value == null)
      throw new NullPointerException("value");

    // parse
    int result;
    try
    {
      result = Integer.parseInt(value);
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The value '" + value + "' could not be parsed: " + e.getMessage());
    }

    // sanity check
    if (result < minValue)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + minValue + ").");
    if (result > maxValue)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + maxValue + ").");

    return result;
  }

  /**
   * Parses a long from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static long parseInt64(String value)
  {
    return parseInt64(value, Long.MIN_VALUE, Long.MAX_VALUE);
  }

  /**
   * Parses a long from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static long parseInt64(String value, long minValue, long maxValue)
  {
    if (value == null)
      throw new NullPointerException("value");

    // parse
    long result;
    try
    {
      result = Long.parseLong(value);
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The value '" + value + "' could not be parsed: " + e.getMessage());
    }

    // sanity check
    if (result < minValue)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + minValue + ").");
    if (result > maxValue)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + maxValue + ").");

    return result;
  }

  /**
   * Parses an Int128 from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static Int128 parseInt128(String value)
  {
    return parseInt128(value, Int128.MIN_VALUE, Int128.MAX_VALUE);
  }

  /**
   * Parses an Int128 from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static Int128 parseInt128(String value, Int128 minValue, Int128 maxValue)
  {
    if (value == null)
      throw new NullPointerException("value");

    // parse
    Int128 result;
    try
    {
      result = new Int128(value);
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The value '" + value + "' could not be parsed: " + e.getMessage());
    }

    // sanity check
    if (result.compareTo(minValue) < 0)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + minValue + ").");
    if (result.compareTo(maxValue) > 0)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + maxValue + ").");

    return result;
  }

  /**
   * Parses the value of the given string: simply returns the string after checking for null.
   * 
   * @throws NullPointerException When an argument is null
   */
  public static String parseString(String value)
  {
    if (value == null)
      throw new NullPointerException("value");

    return value;
  }

  /**
   * Parses the value of the given object: simply returns the toString() result after checking for null, if the object is null the result is
   * null.
   */
  public static String parseString(Object value)
  {
    return value != null ? value.toString() : null;
  }

  /**
   * Parses a TimeSpan from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static Duration parseTimeSpan(String value)
  {
    return parseTimeSpan(value, MIN_DURATION, MAX_DURATION);
  }

  /**
   * Parses a TimeSpan from a string. TimeSpans are .NET structs and do not have an equivalent in Java. They express time durations in
   * ticks, which are units 1000 times smaller than milliseconds.
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static Duration parseTimeSpan(String value, Duration minValue, Duration maxValue)
  {
    if (value == null)
      throw new NullPointerException("value");

    Duration result;
    try
    {
      result = new Duration(Long.parseLong(value) / 1000);
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The value '" + value + "' could not be parsed: " + e.getMessage());
    }

    // sanity check
    if (result.compareTo(minValue) < 0)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + minValue + ").");
    if (result.compareTo(maxValue) > 0)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + maxValue + ").");

    return result;
  }

  /**
   * Parses an sbyte from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static UnsignedByte parseUInt8(String value)
  {
    return parseUInt8(value, UnsignedByte.MIN_VALUE, UnsignedByte.MAX_VALUE);
  }

  /**
   * Parses an sbyte from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static UnsignedByte parseUInt8(String value, UnsignedByte minValue, UnsignedByte maxValue)
  {
    if (value == null)
      throw new NullPointerException("value");

    // parse
    UnsignedByte result;
    try
    {
      result = new UnsignedByte(value);
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The value '" + value + "' could not be parsed: " + e.getMessage());
    }

    // sanity check
    if (result.compareTo(minValue) < 0)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + minValue + ").");
    if (result.compareTo(maxValue) > 0)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + maxValue + ").");

    return result;
  }

  /**
   * Parses a ushort from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static UnsignedShort parseUInt16(String value)
  {
    return parseUInt16(value, UnsignedShort.MIN_VALUE, UnsignedShort.MAX_VALUE);
  }

  /**
   * Parses a ushort from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static UnsignedShort parseUInt16(String value, UnsignedShort minValue, UnsignedShort maxValue)
  {
    if (value == null)
      throw new NullPointerException("value");

    // parse
    UnsignedShort result;
    try
    {
      result = new UnsignedShort(value);
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The value '" + value + "' could not be parsed: " + e.getMessage());
    }

    // sanity check
    if (result.compareTo(minValue) < 0)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + minValue + ").");
    if (result.compareTo(maxValue) > 0)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + maxValue + ").");

    return result;
  }

  /**
   * Parses a uint from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static UnsignedInteger parseUInt32(String value)
  {
    return parseUInt32(value, UnsignedInteger.MIN_VALUE, UnsignedInteger.MIN_VALUE);
  }

  /**
   * Parses a uint from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static UnsignedInteger parseUInt32(String value, UnsignedInteger minValue, UnsignedInteger maxValue)
  {
    if (value == null)
      throw new NullPointerException("value");

    // parse
    UnsignedInteger result;
    try
    {
      result = new UnsignedInteger(value);
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The value '" + value + "' could not be parsed: " + e.getMessage());
    }

    // sanity check
    if (result.compareTo(minValue) < 0)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + minValue + ").");
    if (result.compareTo(maxValue) > 0)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + maxValue + ").");

    return result;
  }

  /**
   * Parses a ulong from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static UnsignedLong parseUInt64(String value)
  {
    return parseUInt64(value, UnsignedLong.MIN_VALUE, UnsignedLong.MAX_VALUE);
  }

  /**
   * Parses a ulong from a string
   * 
   * @throws NullPointerException An argument is null.
   * @throws NumberFormatException Parsed value is outside of configured range, or not of correct type.
   */
  public static UnsignedLong parseUInt64(String value, UnsignedLong minValue, UnsignedLong maxValue)
  {
    if (value == null)
      throw new NullPointerException("value");

    // parse
    UnsignedLong result;
    try
    {
      result = new UnsignedLong(value);
    }
    catch(Throwable e)
    {
      throw new NumberFormatException("The value '" + value + "' could not be parsed: " + e.getMessage());
    }

    // sanity check
    if (result.compareTo(minValue) < 0)
      throw new NumberFormatException("Value (" + result + ") was less than allowed minimum (" + minValue + ").");
    if (result.compareTo(maxValue) > 0)
      throw new NumberFormatException("Value (" + result + ") was more than allowed maximum (" + maxValue + ").");

    return result;
  }

  /**
   * Replaces text in a value with the specified replacement text, using the given string comparison type.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String replace(String value, String textToReplace, String replaceWithText, StringComparison stringComparison)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (textToReplace == null)
      throw new NullPointerException("textToReplace");
    if (replaceWithText == null)
      throw new NullPointerException("replaceWithText");
    if (textToReplace.length() == 0)
      return value;

    int index = 0;

    switch(stringComparison)
    {
      case CurrentLocale:
      case CurrentLocaleIgnoreCase:
      case InvariantLocale:
      case InvariantLocaleIgnoreCase:
        // for these we must not assume that the length is the same as textToReplace.length()
        while ((index = indexOf(value, textToReplace, index, value.length() - index, stringComparison)) >= 0)
        {
          String prefix = value.substring(0, index);
          for (int i = index + 1; i <= value.length(); i++)
          {
            // find index
            String replace = value.substring(index, i);
            if (equal(replace, textToReplace, stringComparison))
            {
              // end index found, replace
              value = prefix + replaceWithText + value.substring(i);
              break;
            }
          }
          index += replaceWithText.length();
        }
        break;
      case Ordinal:
      case OrdinalIgnoreCase:
        while ((index = indexOf(value, textToReplace, index, value.length() - index, stringComparison)) >= 0)
        {
          value = value.substring(0, index) + replaceWithText + value.substring(index + textToReplace.length());
          index += replaceWithText.length();
        }
        break;
      default:
        throw new IllegalArgumentException("Unrecognized string comparison type: " + stringComparison);
    }

    return value;
  }

  /**
   * Replaces text in a value with the specified replacement text, using the given string comparison type. Uses a culture-aware higher
   * performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String replace(String value, String textToReplace, String replaceWithText, Locale locale, Collator collator,
                               boolean caseSensitive)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (textToReplace == null)
      throw new NullPointerException("textToReplace");
    if (replaceWithText == null)
      throw new NullPointerException("replaceWithText");
    if (textToReplace.length() == 0)
      return value;

    int index = 0;

    // for these we must not assume that the length is the same as textToReplace.length()
    while ((index = indexOf(value, textToReplace, index, value.length() - index, locale, collator, caseSensitive)) >= 0)
    {
      String prefix = value.substring(0, index);
      for (int i = index + 1; i <= value.length(); i++)
      {
        // find index
        String replace = value.substring(index, i);
        if (equal(replace, textToReplace, locale, collator, caseSensitive))
        {
          // end index found, replace
          value = prefix + replaceWithText + value.substring(i);
          break;
        }
      }
      index += replaceWithText.length();
    }

    return value;
  }

  /**
   * Returns a number of ToString() result concatenations of the given character.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException Repetitions argument is out of range.
   */
  public static String repeat(char value, int repetitions)
  {
    return repeat(Character.toString(value), repetitions);
  }

  /**
   * Returns a number of ToString() result concatenations of the given string value.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException Repetitions argument is out of range.
   */
  public static String repeat(String value, int repetitions)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (repetitions < 0)
      throw new IllegalArgumentException("repetitions=" + repetitions);

    StringBuilder sb = new StringBuilder(value.length() * repetitions);
    for (int i = 0; i < repetitions; i++)
      sb.append(value);

    return sb.toString();
  }

  /**
   * Reverses a string.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String reverse(String value)
  {
    if (value == null)
      throw new NullPointerException("value");

    StringBuilder sb = new StringBuilder(value);
    return sb.reverse().toString();
  }

  /**
   * Returns true if two character sequences are equal
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean sequenceEqual(char[] a, char[] b)
  {
    if (a == null)
      throw new NullPointerException("a");
    if (b == null)
      throw new NullPointerException("b");

    if (a.length != b.length)
      return false;
    if (a.length == 0)
      return false;

    return sequenceEqual(a, 0, b, 0, a.length);
  }

  /**
   * Returns true if two character sequences are equal
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException An index is out of bounds.
   * @throws IllegalArgumentException An argument is out of range.
   */
  public static boolean sequenceEqual(char[] a, int startIndexA, char[] b, int startIndexB, int count)
  {
    if (a == null)
      throw new NullPointerException("a");
    if (b == null)
      throw new NullPointerException("b");

    if (count == 0)
      return true;
    if (startIndexA < 0 || startIndexA > a.length)
      throw new IndexOutOfBoundsException("startIndexA=" + startIndexA + " aLen=" + a.length);
    if (startIndexB < 0 || startIndexB > b.length)
      throw new IndexOutOfBoundsException("startIndexB=" + startIndexB + " bLen=" + b.length);
    if (count < 0)
      throw new IllegalArgumentException("count=" + count);

    if (startIndexA + count > a.length || startIndexA + count < 0)
      throw new IllegalArgumentException("startIndexA=" + startIndexA + " count=" + count + " aLen=" + a.length);
    if (startIndexB + count > b.length || startIndexB + count < 0)
      throw new IllegalArgumentException("startIndexB=" + startIndexB + " count=" + count + " bLen=" + b.length);

    for (int i = 0; i < count; i++)
      if (a[startIndexA + i] != b[startIndexB + i])
        return false;

    return true;
  }

  /**
   * Splits a sequence into parts delimited by the specified delimited. Empty entries between delimiters are removed.
   * 
   * @throws NullPointerException When an argument is null, or an item in the iterable is null.
   */
  public static List<char[]> split(char[] values, char delimiter)
  {
    if (values == null)
      throw new NullPointerException("values");

    List<ReifiedList<Character>> parts = new ArrayList<ReifiedList<Character>>();
    parts.add(new ReifiedArrayList<Character>(Character.class));

    for (char item : values)
      if (item != delimiter)
        parts.get(parts.size() - 1).add(item);
      else
        parts.add(new ReifiedArrayList<Character>(Character.class));

    List<char[]> result = new ArrayList<char[]>();

    for (ReifiedList<Character> arr : parts)
      if (arr.size() > 0)
        result.add(ArrayUtils.unbox(arr.toArray()));

    return result;
  }

  /**
   * Splits a string, using StringSplitOptions.RemoveEmptyEntries.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String[] split(String text, char delimiter)
  {
    return split(text, delimiter, StringSplitOptions.RemoveEmptyEntries);
  }

  /**
   * Splits a string using the specified split option.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String[] split(String text, char delimiter, StringSplitOptions options)
  {
    char[] delimiters = new char[] {delimiter};
    return split(text, delimiters, options);
  }

  /**
   * Returns a string array that contains the substrings of the text instance that are delimited by elements of a specified Unicode
   * character array.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String[] split(String text, char[] delimiters, StringSplitOptions options)
  {
    if (text == null)
      throw new NullPointerException("text");
    if (delimiters == null)
      throw new NullPointerException("delimiters");

    ReifiedList<String> result = new ReifiedArrayList<String>(String.class);

    // if no separators, return the original string
    if (delimiters.length == 0)
    {
      result.add(text);
      return result.toArray();
    }

    int lastFound = 0;
    int index = 0;

    for (char ch : text.toCharArray())
    {
      // check if character is a separator
      if (contains(delimiters, ch))
      {
        if (lastFound == index)
          result.add(CONSTANT.EMPTY_STRING);
        else
          // add part without separator
          result.add(text.substring(lastFound, index));

        // mark last found position
        lastFound = index + 1;
      }
      index++;
    }
    // add last part if any
    if (index > lastFound)
      result.add(text.substring(lastFound, index));
    else if (index == lastFound && text.length() > 0 && contains(delimiters, text.charAt(text.length() - 1)))
      result.add(CONSTANT.EMPTY_STRING);

    switch(options)
    {
      case None:
        return result.toArray();
      case RemoveEmptyEntries:
        return Linq.where(result.toArray(), Strings.isNotNullOrEmpty());
      default:
        throw new IllegalArgumentException("stringSplitOptions has an unexpected value: " + options.toString());
    }
  }

  /**
   * Splits a string, using StringSplitOptions.RemoveEmptyEntries. When splitting strings, Ordinal string comparison is always used.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String[] split(String text, String delimiter)
  {
    return split(text, delimiter, StringSplitOptions.RemoveEmptyEntries);
  }

  /**
   * Splits a string using the specified split option. When splitting strings, Ordinal string comparison is always used.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String[] split(String text, String delimiter, StringSplitOptions options)
  {
    if (text == null)
      throw new NullPointerException("text");
    if (delimiter == null)
      throw new NullPointerException("delimiter");

    String[] delimiters = new String[] {delimiter};
    return split(text, delimiters, options);
  }

  /**
   * Returns a string array that contains the substrings of the text instance that are delimited by elements provided in the specified
   * Unicode string array. When splitting strings, Ordinal string comparison is always used.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String[] split(String text, String[] delimiters, StringSplitOptions options)
  {
    if (text == null)
      throw new NullPointerException("text");
    if (delimiters == null)
      throw new NullPointerException("delimiters");

    // ignore null and empty delimiters
    delimiters = Linq.where(delimiters, Strings.isNotNullOrEmpty());

    // case where there are no delimiters
    ReifiedList<String> result = new ReifiedArrayList<String>(String.class);
    if (delimiters.length <= 0)
    {
      result.add(text);
      return result.toArray();
    }

    // simplify if all delimiters are chars, call character delimiter method
    if (Linq.all(delimiters, lengthEqualTo(1)))
      return split(text, ArrayUtils.unbox(Linq.select(delimiters, getCharAt(0))), options);

    // multiple delimiters, handled separately
    result.add(text);
    for (int i = 0; i < delimiters.length; i++)
    {
      ReifiedList<String> currentResults = new ReifiedArrayList<String>(String.class);

      for (String part : result)
      {
        String[] splitParts = part.split(delimiters[i]);
        for (String splitPart : splitParts)
          currentResults.add(splitPart);
      }

      result = currentResults;
    }

    switch(options)
    {
      case None:
        return result.toArray();
      case RemoveEmptyEntries:
        return Linq.toArray(Linq.where(result, Strings.isNotNullOrEmpty()), String.class);
      default:
        throw new IllegalArgumentException("Unrecognized string split option: " + options);
    }
  }

  @Predicate
  private static boolean lengthEqualTo(String element, int _length)
  {
    return element.length() == _length;
  }

  @Function
  private static Character getCharAt(String element, int _pos)
  {
    return element.charAt(_pos);
  }

  /**
   * Splits a string by finding consecutive 'tags' i.e. delimiters. E.g. for 0d1h2m3s, using "d,h,m,s" as delimiters would return { 0,1,2,3
   * }. Delimiters that are not found will be ignored. E.g. for 0d1h2m3s, using "d,m,h,s" as delimiters would return { 0,1h2,3 } (i.e. h not
   * found after m). Uses Ordinal string comparison. Does not continuously split in the same way split() does, uses anchor points instead.
   * When splitting strings, Ordinal string comparison is always used.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IllegalArgumentException A delimiter is null or empty.
   */
  public static String[] splitAnchor(String text, Iterable<String> delimiters)
  {
    if (text == null)
      throw new NullPointerException("text");
    if (delimiters == null)
      throw new NullPointerException("delimiters");

    // parts are stored here
    ReifiedList<String> result = new ReifiedArrayList<String>(String.class);

    // process delimiters serially
    for (String delim : delimiters)
    {
      if (isNullOrEmpty(delim))
        throw new IllegalArgumentException("A delimiter cannot be null or empty!");

      if (!isNullOrEmpty(text))
      {
        ReifiedList<String> parts = new ReifiedArrayList<String>(split(text, delim));

        if (parts.size() >= 2)
        {
          // store if any found
          result.add(parts.get(0));
          text = text.substring(parts.get(0).length() + 1);
        }
      }
    }

    return result.toArray();
  }

  /**
   * Returns true if the value starts with a prefix.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean startsWith(String value, char prefix)
  {
    if (value == null)
      throw new NullPointerException("value");

    if (value.length() == 0)
      return false;

    return value.charAt(0) == prefix;
  }

  /**
   * Returns true if a value starts with a prefix. Uses the CurrentLocale string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean startsWith(String value, String prefix)
  {
    return indexOf(value, prefix, 0, value.length(), StringComparison.CurrentLocale) == 0;
  }

  /**
   * Returns true if a value starts with a prefix. Uses the specified string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean startsWith(String value, String prefix, StringComparison stringComparison)
  {
    return indexOf(value, prefix, 0, 1, stringComparison) == 0;
  }

  /**
   * Returns true if a value starts with a prefix. Uses a culture-aware higher performance string comparison.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static boolean startsWith(String value, String prefix, Locale locale, Collator collator, boolean caseSensitive)
  {
    return indexOf(value, prefix, 0, 1, locale, collator, caseSensitive) == 0;
  }

  /**
   * Similar to String.Substring of .NET, uses a length instead of endIndex.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException The length is out of range.
   */
  public static String substring(String value, int length)
  {
    return substring(value, 0, length);
  }

  /**
   * Similar to String.Substring of .NET, uses a length instead of endIndex.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException The index or length is out of range.
   */
  public static String substring(String value, int startIndex, int length)
  {
    if (value == null)
      throw new NullPointerException("value");

    int valueLen = value.length();
    if (startIndex < 0 || startIndex >= valueLen)
      throw new IndexOutOfBoundsException("startIndex=" + startIndex + " valueLen=" + valueLen);
    if (length < 0 || startIndex + length > valueLen || startIndex + length < 0)
      throw new IndexOutOfBoundsException("length=" + length + " startIndex=" + startIndex + " valueLen=" + valueLen);

    return new String(value.toCharArray(), startIndex, length);
  }

  /**
   * Title-cases a string
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static String titleCase(@NotNull final String value)
  {
    val parts = split(value, CONSTANT.WHITESPACE_CHAR, StringSplitOptions.None);
    for (int i = 0; i < parts.length; i++)
      if (parts[i].length() > 0)
        parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1).toLowerCase();

    return delimit(parts, CONSTANT.WHITESPACE);
  }

  /**
   * Returns a character range from start to end (inclusive)
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException When the end is before start
   */
  public static Character[] to(Character start, Character end)
  {
    return ArrayUtils.box(charRange(start.charValue(), end.charValue() + 1));
  }

  /**
   * Trims a value's beginning of all instances of the given char. Does so repeatedly until no more matches are found.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public static String trimStart(String value, char ch)
  {
    if (value == null)
      throw new NullPointerException("value");
    return trimStart(value, new char[] {ch});
  }

  /**
   * Trims a value's beginning of all the given chars. Does so repeatedly until no more matches are found.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public static String trimStart(String value, char[] chars)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (chars == null)
      throw new NullPointerException("chars");

    int startIndex = 0;
    while (startIndex <= value.length() - 1 && contains(chars, value.charAt(startIndex)))
      startIndex++;

    return value.substring(startIndex);
  }

  /**
   * Trims a value's tail of all the instance of the given char. Does so repeatedly until no more matches are found.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public static String trimEnd(String value, char ch)
  {
    if (value == null)
      throw new NullPointerException("value");

    return trimEnd(value, new char[] {ch});
  }

  /**
   * Trims a value's tail of all the given chars. Does so repeatedly until no more matches are found.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public static String trimEnd(String value, char[] chars)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (chars == null)
      throw new NullPointerException("chars");

    int endIndex = value.length() - 1;
    while (endIndex > 0 && contains(chars, value.charAt(endIndex)))
      endIndex--;

    return value.substring(0, endIndex + 1);
  }

  /**
   * Trims a value of all whitespace chars, i.e. ' ', '\t', '\r', '\n'. Does so repeatedly until no more matches are found.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public static String trim(String value)
  {
    return trim(value, CONSTANT.WHITESPACE_CHARS);
  }

  /**
   * Trims a value of all the given chars. Does so repeatedly until no more matches are found.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public static String trim(String value, char[] chars)
  {
    return trimEnd(trimStart(value, chars), chars);
  }

  /**
   * Trims a value using the trimmed string.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String trimStart(String value, String trimmed, StringComparison stringComparison)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (trimmed == null)
      throw new NullPointerException("value");

    switch(stringComparison)
    {
      case CurrentLocale:
      case CurrentLocaleIgnoreCase:
      case InvariantLocale:
      case InvariantLocaleIgnoreCase:
        // we must not assume that the length is the same as trimmed.length()
        while (startsWith(value, trimmed, stringComparison))
          // find end position
          for (int i = 0; i <= value.length(); i++)
            if (equal(trimmed, value.substring(0, i), stringComparison))
            {
              value = value.substring(i);
              break;
            }

      case Ordinal:
      case OrdinalIgnoreCase:
        while (startsWith(value, trimmed, stringComparison))
          value = value.substring(trimmed.length());
        break;
    }

    return value;
  }

  /**
   * Trims a value using the trimmed string.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String trimEnd(String value, String trimmed, StringComparison stringComparison)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (trimmed == null)
      throw new NullPointerException("value");

    switch(stringComparison)
    {
      case CurrentLocale:
      case CurrentLocaleIgnoreCase:
      case InvariantLocale:
      case InvariantLocaleIgnoreCase:
        // we must not assume that the length is the same as trimmed.length()
        while (endsWith(value, trimmed, stringComparison))
          // find end position
          for (int i = value.length() - 1; i >= 0; i--)
            if (equal(trimmed, value.substring(i, value.length())))
            {
              value = value.substring(0, i);
              break;
            }

      case Ordinal:
      case OrdinalIgnoreCase:
        while (endsWith(value, trimmed, stringComparison))
          value = value.substring(0, value.length() - trimmed.length());
        break;
    }

    return value;
  }

  /**
   * Trims a value using the trimmed string.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static String trim(String value, String trimmed, StringComparison stringComparison)
  {
    return trimEnd(trimStart(value, trimmed, stringComparison), trimmed, stringComparison);
  }

  /**
   * Cuts the tail of a string, at the specified index.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException The index is out of bounds.
   */
  public static String truncate(String value, int endIndex)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (endIndex < 0 || endIndex > value.length())
      throw new IndexOutOfBoundsException("endIndex=" + endIndex + " length=" + value.length());

    return value.substring(0, endIndex);
  }

  /**
   * Cuts the tail of a string, if it exceeds a specified index, otherwise does nothing.
   * 
   * @throws NullPointerException An argument is null.
   * @throws IndexOutOfBoundsException The index is negative.
   */
  public static String truncateIfLonger(String value, int endIndex)
  {
    if (value == null)
      throw new NullPointerException("value");
    if (endIndex < 0)
      throw new IndexOutOfBoundsException("endIndex=" + endIndex);
    if (endIndex > value.length())
      return value;

    return value.substring(0, endIndex);
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Boolean> tryParseBool(String value)
  {
    try
    {
      return new TryResult<Boolean>(parseBool(value));
    }
    catch(Throwable e)
    {
      return new TryResult<Boolean>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Boolean> tryParseBool(String value, String trueValue, String falseValue, StringComparison comparisonType)
  {
    try
    {
      return new TryResult<Boolean>(parseBool(value, trueValue, falseValue, comparisonType));
    }
    catch(Throwable e)
    {
      return new TryResult<Boolean>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Character> tryParseChar(String value)
  {
    try
    {
      return new TryResult<Character>(parseChar(value));
    }
    catch(Throwable e)
    {
      return new TryResult<Character>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Character> tryParseChar(String value, char minValue, char maxValue)
  {
    try
    {
      return new TryResult<Character>(parseChar(value, minValue, maxValue));
    }
    catch(Throwable e)
    {
      return new TryResult<Character>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<LocalDateTime> tryParseDateTime(String value)
  {
    try
    {
      return new TryResult<LocalDateTime>(parseDateTime(value));
    }
    catch(Throwable e)
    {
      return new TryResult<LocalDateTime>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<LocalDateTime> tryParseDateTime(String value, DateTimeFormatter fmt)
  {
    try
    {
      return new TryResult<LocalDateTime>(parseDateTime(value, fmt));
    }
    catch(Throwable e)
    {
      return new TryResult<LocalDateTime>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<LocalDateTime> tryParseDateTime(String value, LocalDateTime minValue, LocalDateTime maxValue,
                                                          DateTimeFormatter fmt)
  {
    try
    {
      return new TryResult<LocalDateTime>(parseDateTime(value, minValue, maxValue, fmt));
    }
    catch(Throwable e)
    {
      return new TryResult<LocalDateTime>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<BigDecimal> tryParseDecimal(String value)
  {
    try
    {
      return new TryResult<BigDecimal>(parseDecimal(value));
    }
    catch(Throwable e)
    {
      return new TryResult<BigDecimal>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<BigDecimal> tryParseDecimal(String value, BigDecimal minValue, BigDecimal maxValue)
  {
    try
    {
      return new TryResult<BigDecimal>(parseDecimal(value, minValue, maxValue));
    }
    catch(Throwable e)
    {
      return new TryResult<BigDecimal>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Double> tryParseDouble(String value)
  {
    try
    {
      return new TryResult<Double>(parseDouble(value));
    }
    catch(Throwable e)
    {
      return new TryResult<Double>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Double> tryParseDouble(String value, double minValue, double maxValue, boolean allowInfinity, boolean allowNaN)
  {
    try
    {
      return new TryResult<Double>(parseDouble(value, minValue, maxValue, allowInfinity, allowNaN));
    }
    catch(Throwable e)
    {
      return new TryResult<Double>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Float> tryParseFloat(String value)
  {
    try
    {
      return new TryResult<Float>(parseFloat(value));
    }
    catch(Throwable e)
    {
      return new TryResult<Float>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Float> tryParseFloat(String value, float minValue, float maxValue, boolean allowInfinity, boolean allowNaN)
  {
    try
    {
      return new TryResult<Float>(parseFloat(value, minValue, maxValue, allowInfinity, allowNaN));
    }
    catch(Throwable e)
    {
      return new TryResult<Float>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Byte> tryParseInt8(String value)
  {
    try
    {
      return new TryResult<Byte>(parseInt8(value));
    }
    catch(Throwable e)
    {
      return new TryResult<Byte>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Byte> tryParseInt8(String value, byte minValue, byte maxValue)
  {
    try
    {
      return new TryResult<Byte>(parseInt8(value, minValue, maxValue));
    }
    catch(Throwable e)
    {
      return new TryResult<Byte>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Short> tryParseInt16(String value)
  {
    try
    {
      return new TryResult<Short>(parseInt16(value));
    }
    catch(Throwable e)
    {
      return new TryResult<Short>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Short> tryParseInt16(String value, short minValue, short maxValue)
  {
    try
    {
      return new TryResult<Short>(parseInt16(value, minValue, maxValue));
    }
    catch(Throwable e)
    {
      return new TryResult<Short>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Integer> tryParseInt32(String value)
  {
    try
    {
      return new TryResult<Integer>(parseInt32(value));
    }
    catch(Throwable e)
    {
      return new TryResult<Integer>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Integer> tryParseInt32(String value, int minValue, int maxValue)
  {
    try
    {
      return new TryResult<Integer>(parseInt32(value, minValue, maxValue));
    }
    catch(Throwable e)
    {
      return new TryResult<Integer>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Long> tryParseInt64(String value)
  {
    try
    {
      return new TryResult<Long>(parseInt64(value));
    }
    catch(Throwable e)
    {
      return new TryResult<Long>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Long> tryParseInt64(String value, long minValue, long maxValue)
  {
    try
    {
      return new TryResult<Long>(parseInt64(value, minValue, maxValue));
    }
    catch(Throwable e)
    {
      return new TryResult<Long>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Int128> tryParseInt128(String value)
  {
    try
    {
      return new TryResult<Int128>(parseInt128(value));
    }
    catch(Throwable e)
    {
      return new TryResult<Int128>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Int128> tryParseInt128(String value, Int128 minValue, Int128 maxValue)
  {
    try
    {
      return new TryResult<Int128>(parseInt128(value, minValue, maxValue));
    }
    catch(Throwable e)
    {
      return new TryResult<Int128>();
    }
  }

  /**
   * Tries to parse the given value. Supports IPv4 and IPv6.
   * <p/>
   * Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<InetAddress> tryParseIpAddress(String value)
  {
    try
    {
      return new TryResult<InetAddress>(Inet4Address.getByName(value));
    }
    catch(Throwable e)
    {
      try
      {
        return new TryResult<InetAddress>(InetAddress.getByName(value));
      }
      catch(Throwable e2)
      {
        return new TryResult<InetAddress>();
      }
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Duration> tryParseTimeSpan(String value)
  {
    try
    {
      return new TryResult<Duration>(parseTimeSpan(value));
    }
    catch(Throwable e)
    {
      return new TryResult<Duration>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<Duration> tryParseTimeSpan(String value, Duration minValue, Duration maxValue)
  {
    try
    {
      return new TryResult<Duration>(parseTimeSpan(value, minValue, maxValue));
    }
    catch(Throwable e)
    {
      return new TryResult<Duration>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<UnsignedByte> tryParseUInt8(String value)
  {
    try
    {
      return new TryResult<UnsignedByte>(parseUInt8(value));
    }
    catch(Throwable e)
    {
      return new TryResult<UnsignedByte>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<UnsignedByte> tryParseUInt8(String value, UnsignedByte minValue, UnsignedByte maxValue)
  {
    try
    {
      return new TryResult<UnsignedByte>(parseUInt8(value, minValue, maxValue));
    }
    catch(Throwable e)
    {
      return new TryResult<UnsignedByte>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<UnsignedShort> tryParseUInt16(String value)
  {
    try
    {
      return new TryResult<UnsignedShort>(parseUInt16(value));
    }
    catch(Throwable e)
    {
      return new TryResult<UnsignedShort>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<UnsignedShort> tryParseUInt16(String value, UnsignedShort minValue, UnsignedShort maxValue)
  {
    try
    {
      return new TryResult<UnsignedShort>(parseUInt16(value, minValue, maxValue));
    }
    catch(Throwable e)
    {
      return new TryResult<UnsignedShort>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<UnsignedInteger> tryParseUInt32(String value)
  {
    try
    {
      return new TryResult<UnsignedInteger>(parseUInt32(value));
    }
    catch(Throwable e)
    {
      return new TryResult<UnsignedInteger>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<UnsignedInteger> tryParseUInt32(String value, UnsignedInteger minValue, UnsignedInteger maxValue)
  {
    try
    {
      return new TryResult<UnsignedInteger>(parseUInt32(value, minValue, maxValue));
    }
    catch(Throwable e)
    {
      return new TryResult<UnsignedInteger>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<UnsignedLong> tryParseUInt64(String value)
  {
    try
    {
      return new TryResult<UnsignedLong>(parseUInt64(value));
    }
    catch(Throwable e)
    {
      return new TryResult<UnsignedLong>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<UnsignedLong> tryParseUInt64(String value, UnsignedLong minValue, UnsignedLong maxValue)
  {
    try
    {
      return new TryResult<UnsignedLong>(parseUInt64(value, minValue, maxValue));
    }
    catch(Throwable e)
    {
      return new TryResult<UnsignedLong>();
    }
  }

  /**
   * Tries to parse the given value. Does not throw exceptions while parsing under any circumstances.
   */
  public static TryResult<UUID> tryParseUuid(String value)
  {
    try
    {
      return new TryResult<UUID>(UUID.fromString(value));
    }
    catch(Throwable e)
    {
      return new TryResult<UUID>();
    }
  }

  /**
   * Returns a character range from start (inclusive) to end (exclusive)
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException When the end is before start
   */
  public static Character[] until(Character start, Character end)
  {
    return ArrayUtils.box(charRange(start.charValue(), end.charValue()));
  }

  /**
   * Returns ISO standard and other frequently used date/time parsers
   */
  private static DateTimeParser[] createCommonDateTimeParsers()
  {
    return new DateTimeParser[] {
      ISODateTimeFormat.basicDateTimeNoMillis().getParser(), // yyyyMMdd'T'HHmmssZ
      ISODateTimeFormat.basicDateTime().getParser(), // yyyyMMdd'T'HHmmss.SSSZ
      ISODateTimeFormat.dateHourMinuteSecondFraction().getParser(), // yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS (only 3 ms positions used though)
      ISODateTimeFormat.dateTimeNoMillis().getParser(), // yyyy-MM-dd'T'HH:mm:ssZZ
      ISODateTimeFormat.dateTime().getParser(), // yyyy-MM-dd'T'HH:mm:ss.SSSZZ (ISO 8601)
      DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss Z").getParser(),// RFC 2822
      DateTimeFormat.forPattern("yyyy/MM/dd").getParser(), DateTimeFormat.forPattern("yyyy/MM/dd HH:mm").getParser(),
      DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss").getParser(), DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss.SSSSSSSSS").getParser(),
      DateTimeFormat.forPattern("yyyy-MM-dd").getParser(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").getParser(),
      DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").getParser(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS").getParser(),
      DateTimeFormat.forPattern("yyyy.MM.dd").getParser(), DateTimeFormat.forPattern("yyyy.MM.dd HH:mm").getParser(),
      DateTimeFormat.forPattern("yyyy.MM.dd HH:mm:ss").getParser(), DateTimeFormat.forPattern("yyyy.MM.dd HH:mm:ss.SSSSSSSSS").getParser(),
      DateTimeFormat.forPattern("HH:mm").getParser(), DateTimeFormat.forPattern("HH:mm:ss").getParser(),
      DateTimeFormat.forPattern("HH:mm:ss.SSSSSSSSS").getParser()};
  }
}
