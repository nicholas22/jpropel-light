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
import propel.core.utils.ArrayUtils;
import propel.core.utils.StringUtils;
import propel.core.validation.ValidationException;

/**
 * Class aiding in validation of credit cards e.g. 1234 1234 1234 1234
 */
public class CreditCardPropertyMetadata
    extends EncodedStringPropertyMetadata
{
  /**
   * Error message when a credit card is invalid
   */
  public static final String CREDIT_CARD_NUMBER_INVALID = "%s is not a valid credit card number.";
  /**
   * The default minimum domain length
   */
  public static final int DEFAULT_MIN_CREDITCARD_LENGTH = ConfigurableParameters
      .getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MIN_CREDITCARD_LENGTH);
  /**
   * The default maximum domain length
   */
  public static final int DEFAULT_MAX_CREDITCARD_LENGTH = ConfigurableParameters
      .getInt32(ConfigurableConsts.VALIDATION_DEFAULT_MAX_CREDITCARD_LENGTH);

  /**
   * Default constructor
   */
  protected CreditCardPropertyMetadata()
  {
  }

  /**
   * Constructor initializes with default credit card constraints.
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public CreditCardPropertyMetadata(String name, boolean notNull, boolean notEmpty)
  {
    this(name, ArrayUtils.boxChars(StringUtils.charRange(48, 58)), DEFAULT_MIN_CREDITCARD_LENGTH, DEFAULT_MAX_CREDITCARD_LENGTH, notNull,
        notEmpty);
  }

  /**
   * Constructor initializes with specified constraints.
   * 
   * @throws IllegalArgumentException An argument is invalid
   */
  public CreditCardPropertyMetadata(String name, Iterable<Character> allowedChars, int minLength, int maxLength, boolean notNull,
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
      checkCreditCard(value.toCharArray());

    return value;
  }

  /**
   * Very fast Luhn algorithm for checking credit card number validity. Thanks to Thomas @ Orb of Knowledge:
   * http://orb-of-knowledge.blogspot.com/2009/08/extremely-fast-luhn-function-for-c.html
   */
  protected void checkCreditCard(char[] chars)
      throws ValidationException
  {
    int[] deltas = new int[] {0, 1, 2, 3, 4, -4, -3, -2, -1, 0};

    int checksum = 0;

    for (int i = chars.length - 1; i >= 0; i--)
    {
      int j = (chars[i]) - 48;
      if (j < 0 || j >= deltas.length)
        throw new ValidationException(String.format(CREDIT_CARD_NUMBER_INVALID, getName()));
      checksum += j;
      if (((i - chars.length) % 2) == 0)
        checksum += deltas[j];
    }

    if ((checksum % 10) != 0)
      throw new ValidationException(String.format(CREDIT_CARD_NUMBER_INVALID, getName()));
  }
}
