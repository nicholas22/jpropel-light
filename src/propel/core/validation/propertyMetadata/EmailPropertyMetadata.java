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

/**
 * Class aiding in validation of emails
 */
public class EmailPropertyMetadata
    extends StringPropertyMetadata
{
  /**
   * Appended to the email property's name when validating the local part as a separate propmeta class.
   */
  public static final String LOCAL_PART = " local-part";
  /**
   * Appended to the email property's name when validating the domain part as a separate propmeta class.
   */
  public static final String DOMAIN_PART = " domain-part";
  /**
   * Error message when an email does not contain the @ char
   */
  public static final String EMAIL_DOES_NOT_CONTAIN_AT_SIGN = "%s does not contain the @ character.";
  /**
   * Error message when an email contains too many @ chars
   */
  public static final String EMAIL_CONTAINS_MANY_AT_SIGNS = "%s should contain the @ character only once.";
  /**
   * The default minimum email length
   */
  public static final int DEFAULT_MIN_EMAIL_LENGTH = ConfigurableParameters
      .getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MIN_EMAIL_LENGTH);
  /**
   * The default maximum email length
   */
  public static final int DEFAULT_MAX_EMAIL_LENGTH = ConfigurableParameters
      .getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MAX_EMAIL_LENGTH);
  /**
   * If true, then email addresses may be simple local-parts, rather than full addresses e.g. local-part instead of local-part@example.com
   */
  private boolean localPartOnlyAllowed;
  /**
   * The property metadata used to validate the email's local-part
   */
  private EmailLocalPartPropertyMetadata localPartPropMeta;
  /**
   * The property metadata used to validate the email's domain part.
   */
  private DomainPropertyMetadata domainPartPropMeta;

  /**
   * Default constructor
   */
  protected EmailPropertyMetadata()
  {
  }

  /**
   * Initializes an email address with default RFC-specified constraints and whether to allow international characters and domain name
   * omission.
   * 
   * @throws IllegalArgumentException An argument is null
   */
  public EmailPropertyMetadata(String name, boolean notNull, boolean notEmpty, boolean noUnicodeChars, boolean localPartOnlyAllowed)
  {
    this(name, DEFAULT_MIN_EMAIL_LENGTH, DEFAULT_MAX_EMAIL_LENGTH, notNull, notEmpty, noUnicodeChars, localPartOnlyAllowed);
  }

  /**
   * Constructor initializes with custom constraints (but uses RFC-specified email hostname constraints)
   * 
   * @throws IllegalArgumentException An argument is null
   */
  public EmailPropertyMetadata(String name, int minLength, int maxLength, boolean notNull, boolean notEmpty, boolean noUnicodeChars,
                               boolean localPartOnlyAllowed)
  {
    super(name, minLength, maxLength, notNull, notEmpty, true);

    this.localPartOnlyAllowed = localPartOnlyAllowed;
    this.localPartPropMeta = new EmailLocalPartPropertyMetadata(name + LOCAL_PART, true, true, noUnicodeChars);
    this.domainPartPropMeta = new DomainPropertyMetadata(name + DOMAIN_PART, true, true, noUnicodeChars);
  }

  /**
   * Constructor initializes with fully customizable constraints
   * 
   * @throws IllegalArgumentException An argument is null
   */
  public EmailPropertyMetadata(String name, int minLength, int maxLength, int minLocalPartLength, int maxLocalPathLength,
                               int minDomainLength, int maxDomainLength, boolean notNull, boolean notEmpty, boolean noUnicodeChars,
                               boolean localPartOnlyAllowed)
  {
    super(name, minLength, maxLength, notNull, notEmpty, true);
    this.localPartOnlyAllowed = localPartOnlyAllowed;
    this.localPartPropMeta = new EmailLocalPartPropertyMetadata(name + LOCAL_PART, minLocalPartLength, maxLocalPathLength, true, true,
        noUnicodeChars);
    this.domainPartPropMeta = new DomainPropertyMetadata(name + DOMAIN_PART, minDomainLength, maxDomainLength, true, true, noUnicodeChars);
  }

  /**
   * Constructor initializes with fully customizable constraints
   * 
   * @throws IllegalArgumentException An argument is null
   */
  public EmailPropertyMetadata(String name, int minLength, int maxLength, EmailLocalPartPropertyMetadata localPartPropMeta,
                               DomainPropertyMetadata domainPartPropMeta, boolean notNull, boolean notEmpty, boolean localPartOnlyAllowed)
  {
    super(name, minLength, maxLength, notNull, notEmpty, true);

    if (localPartPropMeta == null)
      throw new IllegalArgumentException("localPartPropMeta is null");
    if (domainPartPropMeta == null)
      throw new IllegalArgumentException("domainPartPropMeta is null");

    this.localPartOnlyAllowed = localPartOnlyAllowed;
    this.localPartPropMeta = localPartPropMeta;
    this.domainPartPropMeta = domainPartPropMeta;
  }

  public boolean getLocalPartOnlyAllowed()
  {
    return localPartOnlyAllowed;
  }

  public void setLocalPartOnlyAllowed(boolean localPartOnlyAllowed)
  {
    this.localPartOnlyAllowed = localPartOnlyAllowed;
  }

  public EmailLocalPartPropertyMetadata getLocalPartPropMeta()
  {
    return localPartPropMeta;
  }

  public void setLocalPartPropMeta(EmailLocalPartPropertyMetadata localPartPropMeta)
  {
    this.localPartPropMeta = localPartPropMeta;
  }

  public DomainPropertyMetadata getDomainPartPropMeta()
  {
    return domainPartPropMeta;
  }

  public void setDomainPartPropMeta(DomainPropertyMetadata domainPartPropMeta)
  {
    this.domainPartPropMeta = domainPartPropMeta;
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
    {
      checkAtSign(value);
      checkLocalPart(value);
      checkDomainPart(value);
    }

    return value;
  }

  protected void checkAtSign(String email)
      throws ValidationException
  {
    if (!getLocalPartOnlyAllowed())
    {
      // then it must contain the at sign
      int atCount = StringUtils.count(email, CONSTANT.AT_SIGN_CHAR);
      if (atCount < 1)
        throw new ValidationException(String.format(EMAIL_DOES_NOT_CONTAIN_AT_SIGN, getName()));
      else if (atCount > 1)
        throw new ValidationException(String.format(EMAIL_CONTAINS_MANY_AT_SIGNS, getName()));
    }
  }

  protected void checkLocalPart(String email)
      throws ValidationException
  {
    // attempt to split, if at sign exists
    if (email.contains(CONSTANT.AT_SIGN))
    {
      String localpart = StringUtils.split(email, CONSTANT.AT_SIGN_CHAR)[0];
      localPartPropMeta.validate(localpart);
    }
  }

  protected void checkDomainPart(String email)
      throws ValidationException
  {
    // attempt to split, if at sign exists
    if (email.contains(CONSTANT.AT_SIGN))
    {
      // only check if there is a domain
      String domainpart = StringUtils.split(email, CONSTANT.AT_SIGN_CHAR)[1];
      domainPartPropMeta.validate(domainpart);
    }
  }
}
