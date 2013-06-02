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

import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.common.CONSTANT;
import propel.core.functional.Functions.Function1;
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
  @Validate
  public static Function1<String, String> append(@NotNull final String _suffix)
  {
    return new Function1<String, String>() {
      @Override
      public String apply(final String element)
      {
        return element + _suffix;
      }
    };
  }

  /**
   * Returns the character at specified index
   * 
   * @throws NullPointerException An argument is null
   * @throws IndexOutOfBoundsException Index is out of string bounds
   */
  public static Function1<String, Character> charAt(final int _index)
  {
    return new Function1<String, Character>() {
      @Override
      public Character apply(final String element)
      {
        return element.charAt(_index);
      }
    };
  }

  /**
   * Returns the number of occurrences of a string within a string
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Function1<String, Integer> count(@NotNull final String _str, @NotNull final StringComparison _stringComparison)
  {
    return new Function1<String, Integer>() {
      @Override
      public Integer apply(final String element)
      {
        return StringUtils.count(element, _str, _stringComparison);
      }
    };
  }

  /**
   * Returns the number of occurrences of a character within a character array
   * 
   * @throws NullPointerException An argument is null
   */
  public static Function1<char[], Integer> countChars(final char _char)
  {
    return new Function1<char[], Integer>() {
      @Override
      public Integer apply(final char[] element)
      {
        return StringUtils.count(element, _char);
      }
    };
  }

  /**
   * Crops all characters from the start/end of the given string, until an except character is encountered
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Function1<String, String> crop(@NotNull final char[] _except)
  {
    return new Function1<String, String>() {
      @Override
      public String apply(final String element)
      {
        return StringUtils.crop(element, _except);
      }
    };
  }

  /**
   * Crops all characters from the end of the given string, until an except character is encountered
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Function1<String, String> cropEnd(@NotNull final char[] _except)
  {
    return new Function1<String, String>() {
      @Override
      public String apply(final String element)
      {
        return StringUtils.cropEnd(element, _except);
      }
    };
  }

  /**
   * Crops all characters from the start of the given string, until an except character is encountered
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Function1<String, String> cropStart(@NotNull final char[] _except)
  {
    return new Function1<String, String>() {
      @Override
      public String apply(final String element)
      {
        return StringUtils.cropStart(element, _except);
      }
    };
  }

  /**
   * Returns the first index where a part is encountered within a string value. If the part is not existent, -1 is returned. Uses the
   * specified string comparison.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Function1<String, Integer> indexOf(@NotNull final String _part, @NotNull final StringComparison _stringComparison)
  {
    return new Function1<String, Integer>() {
      @Override
      public Integer apply(final String element)
      {
        return StringUtils.indexOf(element, _part, _stringComparison);
      }
    };
  }

  /**
   * Returns the last index where a part is encountered within a string value. If the part is not existent, -1 is returned. Uses the
   * specified string comparison.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Function1<String, Integer> lastIndexOf(@NotNull final String value, @NotNull final String _part,
                                                       @NotNull final StringComparison _stringComparison)
  {
    return new Function1<String, Integer>() {
      @Override
      public Integer apply(final String element)
      {
        return StringUtils.lastIndexOf(value, _part, _stringComparison);
      }
    };
  }

  /**
   * Returns the length of the string value
   * 
   * @throws NullPointerException An argument is null
   */
  public static Function1<String, Integer> length()
  {
    return LENGTH;
  }
  private static final Function1<String, Integer> LENGTH = new Function1<String, Integer>() {
    @Override
    public Integer apply(final String element)
    {
      return element.length();
    }
  };

  /**
   * Returns the string with some prepended text
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Function1<String, String> prepend(@NotNull final String _prefix)
  {
    return new Function1<String, String>() {
      @Override
      public String apply(final String element)
      {
        return _prefix + element;
      }
    };
  }

  /**
   * Returns the string after some text replacement
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Function1<String, String> replace(@NotNull final String _textToReplace, @NotNull final String _replaceWithText,
                                                  @NotNull final StringComparison _stringComparison)
  {
    return new Function1<String, String>() {
      @Override
      public String apply(final String element)
      {
        return StringUtils.replace(element, _textToReplace, _replaceWithText, _stringComparison);
      }
    };
  }

  /**
   * Returns the string at specified index
   * 
   * @throws NullPointerException An argument is null
   * @throws IndexOutOfBoundsException Index is out of string bounds
   */
  public static Function1<String, String> stringAt(final int _index)
  {
    return new Function1<String, String>() {
      @Override
      public String apply(final String element)
      {
        return Character.toString(element.charAt(_index));
      }
    };
  }

  /**
   * Returns a sub-string of the given string, using a start index from start
   * 
   * @throws NullPointerException An argument is null
   * @throws IndexOutOfBoundsException The index or length is out of range
   */
  public static Function1<String, String> substringStart(final int _startIndex)
  {
    return new Function1<String, String>() {
      @Override
      public String apply(final String element)
      {
        return StringUtils.substring(element, _startIndex);
      }
    };
  }

  /**
   * Returns a sub-string of the given string, using a start index from start
   * 
   * @throws NullPointerException An argument is null
   * @throws IndexOutOfBoundsException The index or length is out of range
   */
  public static Function1<String, String> substringEnd(final int _length)
  {
    return new Function1<String, String>() {
      @Override
      public String apply(final String element)
      {
        return StringUtils.substring(element, element.length() - _length, _length);
      }
    };
  }

  /**
   * Returns a sub-string of the given string, using a start index and a length from start
   * 
   * @throws NullPointerException An argument is null
   * @throws IndexOutOfBoundsException The index or length is out of range
   */
  public static Function1<String, String> substring(final int _startIndex, final int _length)
  {
    return new Function1<String, String>() {
      @Override
      public String apply(final String element)
      {
        return StringUtils.substring(element, _startIndex, _length);
      }
    };
  }

  /**
   * Returns a sub-string of the given string, using a start index and a length from start. If a null string is encountered, or and
   * index/length is out of range, then an empty string is returned.
   * 
   * @throws NullPointerException An argument is null
   * @throws IndexOutOfBoundsException The index or length is out of range
   */
  public static Function1<String, String> substringNullCoalescing(final int _startIndex, final int _length)
  {
    return new Function1<String, String>() {
      @Override
      public String apply(final String element)
      {
        try
        {
          return StringUtils.substring(element, _startIndex, _length);
        }
        catch(Exception e)
        {
          return CONSTANT.EMPTY_STRING;
        }
      }
    };
  }

  /**
   * Returns a sub-string of the given string, using a start index and a length from start. If a null string is encountered, or and
   * index/length is out of range, then the given default string is returned.
   * 
   * @throws NullPointerException An argument is null
   * @throws IndexOutOfBoundsException The index or length is out of range
   */
  public static Function1<String, String> substringSafe(final int _startIndex, final int _length, final String _defaultValue)
  {
    return new Function1<String, String>() {
      @Override
      public String apply(final String element)
      {
        try
        {
          return StringUtils.substring(element, _startIndex, _length);
        }
        catch(Exception e)
        {
          return _defaultValue;
        }
      }
    };
  }

  /**
   * Calls toLowerCase() on function arguments
   * 
   * @throws NullPointerException An argument is null
   */
  public static Function1<String, String> toLowerCase()
  {
    return TO_LOWERCASE;
  }
  private static final Function1<String, String> TO_LOWERCASE = new Function1<String, String>() {
    @Override
    public String apply(final String element)
    {
      return element.toLowerCase();
    }
  };

  /**
   * Calls titleCase() on function arguments
   * 
   * @throws NullPointerException An argument is null
   */
  public static Function1<String, String> toTitleCase()
  {
    return TO_TITLECASE;
  }
  private static final Function1<String, String> TO_TITLECASE = new Function1<String, String>() {
    @Override
    public String apply(final String element)
    {
      return StringUtils.titleCase(element);
    }
  };

  /**
   * Calls toUpperCase() on function arguments
   * 
   * @throws NullPointerException An argument is null
   */
  public static Function1<String, String> toUpperCase()
  {
    return TO_UPPERCASE;
  }
  private static final Function1<String, String> TO_UPPERCASE = new Function1<String, String>() {
    @Override
    public String apply(final String element)
    {
      return element.toUpperCase();
    }
  };

  /**
   * Returns the trimmed version of the given string
   * 
   * @throws NullPointerException An argument is null
   */
  public static Function1<String, String> trim()
  {
    return TRIM;
  }
  private static final Function1<String, String> TRIM = new Function1<String, String>() {
    @Override
    public String apply(final String element)
    {
      return StringUtils.trim(element);
    }
  };

  /**
   * Trims the end and returns the given string
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Function1<String, String> trimEnd(@NotNull final char[] _chars)
  {
    return new Function1<String, String>() {
      @Override
      public String apply(final String element)
      {
        return StringUtils.trimEnd(element, _chars);
      }
    };
  }

  /**
   * Trims the start and returns the given string
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public static Function1<String, String> trimStart(@NotNull final char[] _chars)
  {
    return new Function1<String, String>() {
      @Override
      public String apply(final String element)
      {
        return StringUtils.trimStart(element, _chars);
      }
    };
  }

  private Strings()
  {
  }
}
