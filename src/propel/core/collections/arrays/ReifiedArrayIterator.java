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
package propel.core.collections.arrays;

import java.lang.reflect.Array;
import java.util.Iterator;

public class ReifiedArrayIterator<T>
    implements Iterator<T>
{
  private Object array;
  private int length;
  private int index;

  public ReifiedArrayIterator(Object array, int length)
  {
    if (array == null)
      throw new NullPointerException("array");
    if (length < 0)
      throw new IllegalArgumentException("length=" + length);

    this.array = array;
    this.length = length;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasNext()
  {
    return index < length;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public T next()
  {
    return (T) Array.get(array, index++);
  }

  /**
   * Throws UnsupportedOperationException
   */
  @Deprecated
  @Override
  public void remove()
  {
    throw new UnsupportedOperationException();
  }
}
