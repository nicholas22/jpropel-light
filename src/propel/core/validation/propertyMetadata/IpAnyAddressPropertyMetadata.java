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

import propel.core.configuration.ConfigurableConsts;
import propel.core.configuration.ConfigurableParameters;
import propel.core.utils.Linq;
import propel.core.utils.StringUtils;
import propel.core.validation.ValidationException;

/**
 * Class aiding in validation of IPs (any version)
 */
public class IpAnyAddressPropertyMetadata
    extends StringPropertyMetadata
{
  /**
   * Error message when an IP is invalid
   */
  public static final String NOT_AN_IP_ADDRESS = "%s is not a valid IPv4 or IPv6 address.";
  /**
   * The default minimum IP length
   */
  public static final int DEFAULT_MIN_IPANY_LENGTH = IpV6AddressPropertyMetadata.DEFAULT_MIN_IPV6_LENGTH;
  /**
   * The default maximum IP length
   */
  public static final int DEFAULT_MAX_IPANY_LENGTH = IpV6AddressPropertyMetadata.DEFAULT_MAX_IPV6_LENGTH;
  /**
   * The IPv4 property metadata
   */
  private IpV4AddressPropertyMetadata ipv4PropMeta;
  /**
   * The IPv6 property metadata
   */
  private IpV6AddressPropertyMetadata ipv6PropMeta;

  /**
   * Default constructor
   */
  protected IpAnyAddressPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with RFC-specified constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  @SuppressWarnings("unchecked")
  public IpAnyAddressPropertyMetadata(String name, boolean notNull, boolean notEmpty)
  {
    this(name, Linq.distinct(Linq.concat(ConfigurableParameters.tryGetIterable(ConfigurableConsts.VALIDATION_ALLOWED_IPV4_CHARS),
        ConfigurableParameters.tryGetIterable(ConfigurableConsts.VALIDATION_ALLOWED_IPV6_CHARS))), DEFAULT_MIN_IPANY_LENGTH,
        DEFAULT_MAX_IPANY_LENGTH, notNull, notEmpty);
  }

  /**
   * Constructor initializes with custom constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public IpAnyAddressPropertyMetadata(String name, Iterable<Character> allowedChars, int minLength, int maxLength, boolean notNull,
                                      boolean notEmpty)
  {
    super(name, minLength, maxLength, notNull, notEmpty, true);
    ipv4PropMeta = new IpV4AddressPropertyMetadata(name, allowedChars, minLength, maxLength, notNull, notEmpty);
    ipv6PropMeta = new IpV6AddressPropertyMetadata(name, allowedChars, minLength, maxLength, notNull, notEmpty);
  }

  public IpV4AddressPropertyMetadata getIpv4PropMeta()
  {
    return ipv4PropMeta;
  }

  public void setIpv4PropMeta(IpV4AddressPropertyMetadata ipv4PropMeta)
  {
    this.ipv4PropMeta = ipv4PropMeta;
  }

  public IpV6AddressPropertyMetadata getIpv6PropMeta()
  {
    return ipv6PropMeta;
  }

  public void setIpv6PropMeta(IpV6AddressPropertyMetadata ipv6PropMeta)
  {
    this.ipv6PropMeta = ipv6PropMeta;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String validate(String value)
      throws ValidationException
  {
    value = super.validate(value);

    if (!StringUtils.isNullOrEmpty(value))
      checkIpAnyAddress(value);

    return value;
  }

  protected void checkIpAnyAddress(String value)
      throws ValidationException
  {
    try
    {
      getIpv4PropMeta().validate(value);
    }
    catch(ValidationException e1)
    {
      try
      {
        getIpv6PropMeta().validate(value);
      }
      catch(ValidationException e2)
      {
        throw new ValidationException(String.format(NOT_AN_IP_ADDRESS, getName()));
      }
    }
  }
}
