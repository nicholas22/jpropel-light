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
package propel.core.collections.buffers.primitive;

import lombok.Validate;
import lombok.Validate.NotNull;

/**
 * Class implementing a fixed-size buffer of Long primitives.
 */
public final class FixedSizeLongBuffer
    implements ILongBuffer
{
  private final long[] buffer;
  private int position;

  /**
   * Constructor, initialises with the buffer holding initial values
   */
  @Validate
  public FixedSizeLongBuffer(@NotNull final long[] buffer)
  {
    int length = buffer.length;
    this.buffer = new long[length];
    System.arraycopy(buffer, 0, this.buffer, 0, length);
    position = 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long[] get()
  {
    return buffer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void put(final long value)
  {
    position += 1;
    if (position >= buffer.length)
      position = 0;
    buffer[position] = value;
  }
}
