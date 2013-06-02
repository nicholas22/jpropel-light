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

import lombok.val;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import propel.core.validation.ValidationException;

/**
 * Class aiding in validation of LocalDate classes
 */
public class LocalDatePropertyMetadata
    extends LocalDateTimePropertyMetadata
{
  /**
   * Constructor
   */
  public LocalDatePropertyMetadata(String name, LocalDate minValue, LocalDate maxValue, boolean notNull)
  {
    super(name, minValue.toLocalDateTime(LocalTime.MIDNIGHT), maxValue.toLocalDateTime(LocalTime.MIDNIGHT), notNull);

    setMinValue(minValue.toLocalDateTime(LocalTime.MIDNIGHT));
    setMaxValue(maxValue.toLocalDateTime(LocalTime.MIDNIGHT));
  }

  /**
   * Constructor
   */
  public LocalDatePropertyMetadata(String name, LocalDateTime minValue, LocalDateTime maxValue, boolean notNull)
  {
    this(name, minValue.toLocalDate(), maxValue.toLocalDate(), notNull);
  }

  /**
   * {@inheritDoc}
   */
  public LocalDate validate(LocalDate value)
      throws ValidationException
  {
    if (value != null)
    {
      // use the existing validation method
      val result = validate(value.toLocalDateTime(LocalTime.MIDNIGHT));
      if (result != null)
        return result.toLocalDate();
    } else
      // check if null allowed, using the other validate()
      validate((LocalDateTime) null);

    return null;
  }

}
