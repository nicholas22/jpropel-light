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
package propel.core.validation.propertyMetadata;

import propel.core.common.CONSTANT;
import propel.core.utils.Linq;
import propel.core.utils.StringComparison;
import propel.core.utils.StringUtils;
import propel.core.validation.ValidationException;

/**
 * Class used internally for aiding in validation of MQ trade types, e.g. SPOT, FORWARD, SWAP
 */
public class StringListPropertyMetadata
    extends StringPropertyMetadata
{

  /**
   * The possible values that we allow
   */
  private String[] allowedValues;
  /**
   * The string comparison mode used to compare given values to allowed values
   */
  private StringComparison comparison;
  /**
   * Whether to include a comma delimited list in the error messages, set to false if you have a lot of allowed values, to avoid having very
   * long exception messages.
   */
  private boolean includeAllowedStringsInErrorMessage;

  /**
   * Constructor
   */
  public StringListPropertyMetadata(String name, String[] allowedValues)
  {
    this(name, allowedValues, StringComparison.Ordinal, true);
  }

  /**
   * Constructor
   */
  public StringListPropertyMetadata(String name, Iterable<String> allowedValues)
  {
    this(name, allowedValues, StringComparison.Ordinal, true);
  }

  /**
   * Constructor
   */
  public StringListPropertyMetadata(String name, String[] allowedValues, StringComparison comparison,
                                    boolean includeAllowedStringsInErrorMessage)
  {
    super(name, detectShortest(allowedValues), detectLengthiest(allowedValues), !containsNull(allowedValues),
        !containsEmpty(allowedValues), !containsNullCharStrings(allowedValues));

    this.allowedValues = allowedValues;
    this.comparison = comparison;
    this.includeAllowedStringsInErrorMessage = includeAllowedStringsInErrorMessage;
  }

  /**
   * Constructor
   */
  public StringListPropertyMetadata(String name, Iterable<String> allowedValues, StringComparison comparison,
                                    boolean includeAllowedStringsInErrorMessage)
  {
    this(name, Linq.toArray(allowedValues, String.class), comparison, includeAllowedStringsInErrorMessage);
  }

  private static int detectShortest(String[] values)
  {
    if (values == null)
      throw new NullPointerException("values");

    if (values.length == 0)
      return 0;

    int min = 0;
    for (int i = 0; i < values.length; i++)
      if (values[i] != null)
      {
        min = values[i].length();
        break;
      }

    for (int i = 0; i < values.length; i++)
      if (values[i] != null)
        if (values[i].length() < min)
          min = values[i].length();

    return min;
  }

  private static int detectLengthiest(String[] values)
  {
    if (values == null)
      throw new NullPointerException("values");

    if (values.length == 0)
      return 0;

    int max = 0;
    for (int i = 0; i < values.length; i++)
      if (values[i] != null)
      {
        max = values[i].length();
        break;
      }

    for (int i = 0; i < values.length; i++)
      if (values[i] != null)
        if (values[i].length() > max)
          max = values[i].length();

    return max;
  }

  private static boolean containsNull(String[] values)
  {
    if (values == null)
      throw new NullPointerException("values");

    for (int i = 0; i < values.length; i++)
      if (values[i] == null)
        return true;

    return false;
  }

  private static boolean containsEmpty(String[] values)
  {
    if (values == null)
      throw new NullPointerException("values");

    for (int i = 0; i < values.length; i++)
      if (values[i] != null)
        if (values[i].length() == 0)
          return true;

    return false;
  }

  private static boolean containsNullCharStrings(String[] values)
  {
    if (values == null)
      throw new NullPointerException("values");

    for (int i = 0; i < values.length; i++)
      if (values[i] != null)
        if (values[i].contains(CONSTANT.NULL_STRING))
          return true;

    return false;
  }

  /**
   * Override to ensure one of the allowed strings is specified
   */
  @Override
  public String validate(final String value)
      throws ValidationException
  {
    String result = value;

    // perform null checks
    checkNotNull(value);

    // only check further if not null
    if (value != null)
      // check if empty first
      if (!checkEmpty(value))
      {
        // do not check length
        // checkLength(value);

        // check no null chars
        checkNoNullChars(value);

        // ensure one of allowed values is used
        if (!StringUtils.contains(allowedValues, value, comparison))
        {
          // decide whether to include allowed values in error message
          if (includeAllowedStringsInErrorMessage)
            throw new ValidationException(getName() + " specified ('" + value + "') is not one of the following: "
                + StringUtils.delimit(allowedValues, ", "));

          // do not include
          throw new ValidationException(getName() + " specified a value which is not an allowed value: " + value);
        }
      }

    return result;
  }
}
