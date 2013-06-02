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
package propel.core.functional.predicates;

import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.functional.Predicates.Predicate1;
import propel.core.utils.StringComparison;
import propel.core.utils.StringUtils;

/**
 * Some common, re-usable predicates for Strings
 */
public final class Strings
{
  /**
   * Predicate returning true when the function argument contains some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<String> contains(@NotNull final String _part)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return StringUtils.contains(element, _part, StringComparison.Ordinal);
      }
    };
  }

  /**
   * Predicate returning true when the function argument contains some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<String> contains(@NotNull final String _part, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return StringUtils.contains(element, _part, _comparison);
      }
    };
  }

  /**
   * Predicate returning true when the function argument ends with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<String> endsWith(@NotNull final String _suffix)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return StringUtils.endsWith(element, _suffix, StringComparison.Ordinal);
      }
    };
  }

  /**
   * Predicate returning true when the function argument ends with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<String> endsWith(@NotNull final String _suffix, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return StringUtils.endsWith(element, _suffix, _comparison);
      }
    };
  }

  /**
   * Predicate returning true when the function argument is empty
   * 
   * @throws NullPointerException When an argument is null
   */
  public static Predicate1<String> isEmpty()
  {
    return IS_EMPTY;
  }
  private static final Predicate1<String> IS_EMPTY = new Predicate1<String>() {
    @Override
    public boolean evaluate(final String element)
    {
      return element.isEmpty();
    }
  };

  /**
   * Predicate returning true when the function argument is equal to some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<String> isEqual(@NotNull final String _other)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return StringUtils.equal(element, _other, StringComparison.Ordinal);
      }
    };
  }

  /**
   * Predicate returning true when the function argument is equal to some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<String> isEqual(@NotNull final String _other, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return StringUtils.equal(element, _other, _comparison);
      }
    };
  }

  /**
   * Predicate returning true when the function argument is null or empty
   * 
   * @throws NullPointerException When an argument is null
   */
  public static Predicate1<String> isNullOrEmpty()
  {
    return IS_NULL_OR_EMPTY;
  }
  private static final Predicate1<String> IS_NULL_OR_EMPTY = new Predicate1<String>() {
    @Override
    public boolean evaluate(final String element)
    {
      return StringUtils.isNullOrEmpty(element);
    }
  };

  /**
   * Predicate returning true when the function argument is not null or empty
   * 
   * @throws NullPointerException When an argument is null
   */
  public static Predicate1<String> isNotNullOrEmpty()
  {
    return IS_NOT_NULL_OR_EMPTY;
  }
  private static final Predicate1<String> IS_NOT_NULL_OR_EMPTY = new Predicate1<String>() {
    @Override
    public boolean evaluate(final String element)
    {
      return !StringUtils.isNullOrEmpty(element);
    }
  };

  /**
   * Predicate returning true when the function argument is null, empty or blank.
   * 
   * @throws NullPointerException When an argument is null
   */
  public static Predicate1<String> isNullOrBlank()
  {
    return IS_NULL_OR_BLANK;
  }
  private static final Predicate1<String> IS_NULL_OR_BLANK = new Predicate1<String>() {
    @Override
    public boolean evaluate(final String element)
    {
      return StringUtils.isNullOrBlank(element);
    }
  };

  /**
   * Predicate returning true when the function argument is not null, empty or blank.
   * 
   * @throws NullPointerException When an argument is null
   */
  public static Predicate1<String> isNotNullOrBlank()
  {
    return IS_NOT_NULL_OR_BLANK;
  }
  private static final Predicate1<String> IS_NOT_NULL_OR_BLANK = new Predicate1<String>() {
    @Override
    public boolean evaluate(final String element)
    {
      return !StringUtils.isNullOrBlank(element);
    }
  };

  /**
   * Predicate returning true when the function argument has a specified length
   * 
   * @throws NullPointerException When an argument is null
   */
  public static Predicate1<String> lengthEquals(final int _len)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return element.length() == _len;
      }
    };
  }

  /**
   * Predicate returning true when the function argument has a length greater than specified
   * 
   * @throws NullPointerException When an argument is null
   */
  public static Predicate1<String> lengthGreaterThan(final int _len)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return element.length() > _len;
      }
    };
  }

  /**
   * Predicate returning true when the function argument has a length greater than or equal to specified
   * 
   * @throws NullPointerException When an argument is null
   */
  public static Predicate1<String> lengthGreaterThanOrEqual(final int _len)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return element.length() >= _len;
      }
    };
  }

  /**
   * Predicate returning true when the function argument has a length less than specified
   * 
   * @throws NullPointerException When an argument is null
   */
  public static Predicate1<String> lengthLessThan(final int _len)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return element.length() < _len;
      }
    };
  }

  /**
   * Predicate returning true when the function argument has a length less than or equal to specified
   * 
   * @throws NullPointerException When an argument is null
   */
  public static Predicate1<String> lengthLessThanOrEqual(final int _len)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return element.length() <= _len;
      }
    };
  }

  /**
   * Predicate returning true when the function argument does not have a specified length
   * 
   * @throws NullPointerException When an argument is null
   */
  public static Predicate1<String> lengthNotEqual(final int _len)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return element.length() != _len;
      }
    };
  }

  /**
   * Predicate returning true when the function argument does not contain some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<String> notContains(final String _part)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return !StringUtils.contains(element, _part, StringComparison.Ordinal);
      }
    };
  }

  /**
   * Predicate returning true when the function argument does not contain some string
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<String> notContains(final String _part, final StringComparison _comparison)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return !StringUtils.contains(element, _part, _comparison);
      }
    };
  }

  /**
   * Predicate returning true when the function argument does not end with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<String> notEndsWith(final String _suffix)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return !StringUtils.endsWith(element, _suffix, StringComparison.Ordinal);
      }
    };
  }

  /**
   * Predicate returning true when the function argument does not end with a suffix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<String> notEndsWith(final String _suffix, final StringComparison _comparison)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return !StringUtils.endsWith(element, _suffix, _comparison);
      }
    };
  }

  /**
   * Predicate returning true when the function argument does not start with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<String> notStartsWith(final String _prefix)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return !StringUtils.startsWith(element, _prefix, StringComparison.Ordinal);
      }
    };
  }

  /**
   * Predicate returning true when the function argument does not start with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<String> notStartsWith(@NotNull final String _prefix, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return !StringUtils.startsWith(element, _prefix, _comparison);
      }
    };
  }

  /**
   * Predicate returning true when the function argument starts with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<String> startsWith(@NotNull final String _prefix)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return StringUtils.startsWith(element, _prefix, StringComparison.Ordinal);
      }
    };
  }

  /**
   * Predicate returning true when the function argument starts with a prefix
   * 
   * @throws NullPointerException When an argument is null
   */
  @Validate
  public static Predicate1<String> startsWith(@NotNull final String _prefix, @NotNull final StringComparison _comparison)
  {
    return new Predicate1<String>() {
      @Override
      public boolean evaluate(final String element)
      {
        return StringUtils.startsWith(element, _prefix, _comparison);
      }
    };
  }

  private Strings()
  {
  }
}
