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
import propel.core.utils.StringSplitOptions;
import propel.core.utils.StringUtils;
import propel.core.validation.ValidationException;

/**
 * Class aiding in validation of domains
 */
public class DomainPropertyMetadata
    extends StringPropertyMetadata
{
  /**
   * The default minimum domain length
   */
  public static final int DEFAULT_MIN_DOMAIN_LENGTH = ConfigurableParameters
      .getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MIN_DOMAIN_LENGTH);
  /**
   * The default maximum domain length
   */
  public static final int DEFAULT_MAX_DOMAIN_LENGTH = ConfigurableParameters
      .getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MAX_DOMAIN_LENGTH);
  /**
   * Used to validate the domain's sub-domains.
   */
  private SubDomainPropertyMetadata subDomainPropMeta;
  /**
   * Used to name subdomain parts of the domain
   */
  private static final String DEFAULT_SUBDOMAIN_NAME_POSTFIX = " subdomain";

  /**
   * Default constructor
   */
  protected DomainPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with RFC-specified domain name constraints and whether to allow international characters
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public DomainPropertyMetadata(String name, boolean notNull, boolean notEmpty, boolean noUnicodeChars)
  {
    this(name, DEFAULT_MIN_DOMAIN_LENGTH, DEFAULT_MAX_DOMAIN_LENGTH, notNull, notEmpty, noUnicodeChars);
  }

  /**
   * Constructor initializes with custom domain constraints and RFC-specified sub-domain name constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public DomainPropertyMetadata(String name, int minLength, int maxLength, boolean notNull, boolean notEmpty, boolean noUnicodeChars)
  {
    this(name, minLength, maxLength, SubDomainPropertyMetadata.DEFAULT_MIN_SUBDOMAIN_LENGTH,
        SubDomainPropertyMetadata.DEFAULT_MAX_SUBDOMAIN_LENGTH, DEFAULT_SUBDOMAIN_NAME_POSTFIX, notNull, notEmpty, noUnicodeChars);
  }

  /**
   * Constructor initializes with custom domain and sub-domain constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public DomainPropertyMetadata(String name, int minLength, int maxLength, int minSubDomainLength, int maxSubDomainLength,
                                String subDomainNamePostfix, boolean notNull, boolean notEmpty, boolean noUnicodeChars)
  {
    super(name, minLength, maxLength, notNull, notEmpty, true);
    subDomainPropMeta = new SubDomainPropertyMetadata(name + subDomainNamePostfix, minSubDomainLength, maxSubDomainLength, true, true,
        noUnicodeChars);
  }

  /**
   * Constructor initializes with custom domain and sub-domain constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public DomainPropertyMetadata(String name, int minLength, int maxLength, SubDomainPropertyMetadata subDomainPropMeta, boolean notNull,
                                boolean notEmpty)
  {
    super(name, minLength, maxLength, notNull, notEmpty, true);

    if (subDomainPropMeta == null)
      throw new IllegalArgumentException("subDomainPropMeta is null");
    this.subDomainPropMeta = subDomainPropMeta;
  }

  public SubDomainPropertyMetadata getSubDomainPropMeta()
  {
    return subDomainPropMeta;
  }

  public void setSubDomainPropMeta(SubDomainPropertyMetadata subDomainPropMeta)
  {
    this.subDomainPropMeta = subDomainPropMeta;
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
      checkSubDomains(value);

    return value;
  }

  protected void checkSubDomains(String domain)
      throws ValidationException
  {
    String[] subdomains = StringUtils.split(domain, CONSTANT.DOT_CHAR, StringSplitOptions.None);

    for (String subdomain : subdomains)
      subDomainPropMeta.validate(subdomain);
  }
}
