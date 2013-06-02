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
import propel.core.configuration.ConfigurableConsts;
import propel.core.configuration.ConfigurableParameters;
import propel.core.utils.StringComparison;
import propel.core.utils.StringUtils;
import propel.core.validation.ValidationException;

/**
 * Class aiding in validation of IPv6 addresses
 */
public class IpV6AddressPropertyMetadata
    extends EncodedStringPropertyMetadata
{
  /**
   * The max number of hex groups expected in an IPv6 address
   */
  public static final int MAX_NUM_HEX_GROUPS = 8;
  /**
   * Error message when an IPv6 has an invalid number of colon separators
   */
  public static final String IP_INVALID_NUMBER_OF_COLONS = "%s has an invalid number of IPv6 group-separator colons: ";
  /**
   * Error message when an IPv6 has an invalid number of double colon instances
   */
  public static final String IP_INVALID_NUMBER_OF_DOUBLE_COLONS = "%s has an invalid number of IPv6 group-separator double-colons: ";
  /**
   * Error message when an IPv6 has an invalid number of groups
   */
  public static final String IP_INVALID_NUMBER_OF_GROUPS = "%s has an invalid number of IPv6 groups: ";
  /**
   * The default minimum IPv6 length
   */
  public static final int DEFAULT_MIN_IPV6_LENGTH = ConfigurableParameters.getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MIN_IPV6_LENGTH);
  /**
   * The default maximum IPv6 length
   */
  public static final int DEFAULT_MAX_IPV6_LENGTH = ConfigurableParameters.getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MAX_IPV6_LENGTH);

  /**
   * Default constructor
   */
  protected IpV6AddressPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with RFC-specified constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public IpV6AddressPropertyMetadata(String name, boolean notNull, boolean notEmpty)
  {
    this(name, ConfigurableParameters.tryGetIterable(ConfigurableConsts.VALIDATION_ALLOWED_IPV6_CHARS), DEFAULT_MIN_IPV6_LENGTH,
        DEFAULT_MAX_IPV6_LENGTH, notNull, notEmpty);
  }

  /**
   * Constructor initializes with custom constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public IpV6AddressPropertyMetadata(String name, Iterable<Character> allowedChars, int minLength, int maxLength, boolean notNull,
                                     boolean notEmpty)
  {
    super(name, allowedChars, minLength, maxLength, notNull, notEmpty, true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String validate(String value)
      throws ValidationException
  {
    value = super.validate(value);

    if (value != null)
      checkIpAddress(value);

    return value;
  }

  protected void checkIpAddress(String value)
      throws ValidationException
  {
    // count number of colons, up to 8
    int numOfColons = StringUtils.count(value, CONSTANT.COLON_CHAR);

    if ((numOfColons >= 2) && (numOfColons <= MAX_NUM_HEX_GROUPS - 1))
    {
      // count number of double colons, must be 1
      int numOfDoubleColons = StringUtils.count(value, CONSTANT.DOUBLE_COLON, StringComparison.Ordinal);

      String[] groups;
      // substitute double cols with group of 4 zeroes
      if (numOfDoubleColons == 1)
      {
        groups = new String[MAX_NUM_HEX_GROUPS];
        for (int i = 0; i < MAX_NUM_HEX_GROUPS; i++)
          groups[i] = CONSTANT.ZERO;
        // where we put them depends on the double colon position
        if (value.startsWith(CONSTANT.DOUBLE_COLON))
        {
          String[] lastGroups = StringUtils.split(value.substring(CONSTANT.DOUBLE_COLON.length()), CONSTANT.COLON_CHAR);
          System.arraycopy(lastGroups, 0, groups, MAX_NUM_HEX_GROUPS - lastGroups.length, lastGroups.length);
        } else if (value.endsWith(CONSTANT.DOUBLE_COLON))
        {
          String[] firstGroups = StringUtils
              .split(value.substring(0, value.length() - CONSTANT.DOUBLE_COLON.length()), CONSTANT.COLON_CHAR);
          System.arraycopy(firstGroups, 0, groups, 0, firstGroups.length);
        } else
        {
          int index = value.indexOf(CONSTANT.DOUBLE_COLON);
          String[] firstGroups = StringUtils.split(value.substring(0, index), CONSTANT.COLON_CHAR);
          System.arraycopy(firstGroups, 0, groups, 0, firstGroups.length);
          String[] lastGroups = StringUtils.split(value.substring(index + CONSTANT.DOUBLE_COLON.length()), CONSTANT.COLON_CHAR);
          System.arraycopy(lastGroups, 0, groups, MAX_NUM_HEX_GROUPS - lastGroups.length, lastGroups.length);
        }
      } else if (numOfDoubleColons == 0)
        groups = StringUtils.split(value, CONSTANT.COLON_CHAR);
      else
        throw new ValidationException(String.format(IP_INVALID_NUMBER_OF_DOUBLE_COLONS, getName()) + numOfDoubleColons);

      // must have 8 groups
      if (groups.length != MAX_NUM_HEX_GROUPS)
        throw new ValidationException(String.format(IP_INVALID_NUMBER_OF_GROUPS, getName()) + groups.length);
    } else
      throw new ValidationException(String.format(IP_INVALID_NUMBER_OF_COLONS, getName()) + numOfColons);
  }
}
