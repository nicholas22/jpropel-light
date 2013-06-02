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

import java.util.Arrays;
import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.counters.SharedModuloIndexerLight;

/**
 * Thread-safe class implementing a fixed-size buffer of Long primitives.
 */
public final class SharedFixedSizeLongBuffer
    implements ILongBuffer
{
  private volatile long[] buffer;
  private final SharedModuloIndexerLight indexer;

  /**
   * Constructor, initialises with the buffer holding initial values
   */
  @Validate
  public SharedFixedSizeLongBuffer(@NotNull final long[] buffer)
  {
    int length = buffer.length;
    this.buffer = new long[length];
    System.arraycopy(buffer, 0, this.buffer, 0, length);
    indexer = new SharedModuloIndexerLight(length);
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
    buffer[indexer.next()] = value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return Arrays.toString(buffer);
  }
}
