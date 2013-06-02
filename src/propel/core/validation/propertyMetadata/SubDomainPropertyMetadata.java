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
import propel.core.validation.ValidationException;

/**
 * Class aiding in validation of subdomains
 */
public class SubDomainPropertyMetadata
    extends RestrictedEdgesI18NStringPropertyMetadata
{
  /**
   * The default minimum subdomain length
   */
  public static final int DEFAULT_MIN_SUBDOMAIN_LENGTH = ConfigurableParameters
      .getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MIN_SUBDOMAIN_LENGTH);
  /**
   * The default maximum subdomain length
   */
  public static final int DEFAULT_MAX_SUBDOMAIN_LENGTH = ConfigurableParameters
      .getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MAX_SUBDOMAIN_LENGTH);

  /**
   * Default constructor
   */
  protected SubDomainPropertyMetadata()
  {
  }

  /**
   * Initializes with RFC-specified constraints and whether to allow international characters.
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public SubDomainPropertyMetadata(String name, boolean notNull, boolean notEmpty, boolean noUnicodeChars)
  {
    this(name, DEFAULT_MIN_SUBDOMAIN_LENGTH, DEFAULT_MAX_SUBDOMAIN_LENGTH, notNull, notEmpty, noUnicodeChars);
  }

  /**
   * Initializes with RFC-specified constraints, configurable length and whether to allow international characters.
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public SubDomainPropertyMetadata(String name, int minLength, int maxLength, boolean notNull, boolean notEmpty, boolean noUnicodeChars)
  {
    this(name, ConfigurableParameters.tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_SUBDOMAIN_CHARS), ConfigurableParameters
        .tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_SUBDOMAIN_START_CHARS), ConfigurableParameters
        .tryGetIterable(ConfigurableConsts.VALIDATION_DISALLOWED_SUBDOMAIN_END_CHARS), minLength, maxLength, notNull, notEmpty,
        noUnicodeChars);
  }

  /**
   * Initializes with custom constraints
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public SubDomainPropertyMetadata(String name, Iterable<Character> disallowedCharacters, Iterable<Character> disallowedStartChars,
                                   Iterable<Character> disallowedEndChars, int minLength, int maxLength, boolean notNull, boolean notEmpty,
                                   boolean noUnicodeChars)
  {
    super(name, disallowedCharacters, disallowedStartChars, disallowedEndChars, minLength, maxLength, notNull, notEmpty, true,
        noUnicodeChars);
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
      checkNoConsequtiveDots(value);

    return value;
  }

  protected void checkNoConsequtiveDots(String value)
      throws ValidationException
  {
    boolean previousIsDot = false;
    for (char ch : value.toCharArray())
    {
      if (ch == '.')
      {
        if (previousIsDot)
          throw new ValidationException(getName() + " cannot contain two consecutive dots.");
        else
          previousIsDot = true;
      } else
        previousIsDot = false;
    }
  }
}
