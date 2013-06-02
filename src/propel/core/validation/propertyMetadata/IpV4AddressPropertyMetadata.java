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
import propel.core.utils.StringUtils;
import propel.core.validation.ValidationException;
import java.util.List;

/**
 * Class aiding in validation of IPv4 addresses
 */
public class IpV4AddressPropertyMetadata
    extends EncodedStringPropertyMetadata
{
  /**
   * Error message when an IPv4 has an invalid number of dot separators
   */
  public static final String IP_MISSING_DOTS_MALFORMED = "%s is not valid because an IPv4 should comprise of 4 dot-separated octets.";
  /**
   * Error message when an IPv4 has an invalid number of octets
   */
  public static final String IP_MISSING_OCTET_MALFORMED = "%s is not valid because an IPv4 should comprise of 4 octets in total.";
  /**
   * Error message when an IPv4 has an invalid octet
   */
  public static final String IP_OCTET_PROBLEM = "%s is not valid because an IPv4 octet's valid range is from 0 to 255: ";
  /**
   * The default minimum IPv4 length
   */
  public static final int DEFAULT_MIN_IPV4_LENGTH = ConfigurableParameters.getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MIN_IPV4_LENGTH);
  /**
   * The default maximum IPv4 length
   */
  public static final int DEFAULT_MAX_IPV4_LENGTH = ConfigurableParameters.getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MAX_IPV4_LENGTH);

  /**
   * Default constructor
   */
  protected IpV4AddressPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with RFC-specified constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public IpV4AddressPropertyMetadata(String name, boolean notNull, boolean notEmpty)
  {
    this(name, ConfigurableParameters.tryGetIterable(ConfigurableConsts.VALIDATION_ALLOWED_IPV4_CHARS), DEFAULT_MIN_IPV4_LENGTH,
        DEFAULT_MAX_IPV4_LENGTH, notNull, notEmpty);
  }

  /**
   * Constructor initializes with custom constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public IpV4AddressPropertyMetadata(String name, Iterable<Character> allowedChars, int minLength, int maxLength, boolean notNull,
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

    // only check if not null
    if (value != null)
      checkIpv4Address(value.toCharArray());

    return value;
  }

  protected void checkIpv4Address(char[] value)
      throws ValidationException
  {
    if (StringUtils.count(value, CONSTANT.DOT_CHAR) != 3)
      throw new ValidationException(String.format(IP_MISSING_DOTS_MALFORMED, getName()));

    List<char[]> octets = StringUtils.split(value, CONSTANT.DOT_CHAR);
    if (octets.size() != 4)
      throw new ValidationException(String.format(IP_MISSING_OCTET_MALFORMED, getName()));

    for (char[] octet : octets)
    {
      String octetString = StringUtils.concat(octet);

      // parse
      try
      {
        StringUtils.parseUInt8(octetString);
      }
      catch(Throwable e)
      {
        throw new ValidationException(String.format(IP_OCTET_PROBLEM, getName()) + octetString);
      }
    }
  }
}
