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
package propel.core.functional.tuples;

/**
 * A tuple having two items
 */
public class Pair<T1, T2>
{
  private T1 first;
  private T2 second;

  public Pair(T1 first, T2 second)
  {
    this.first = first;
    this.second = second;
  }

  public T1 getFirst()
  {
    return first;
  }

  public void setFirst(T1 first)
  {
    this.first = first;
  }

  public T2 getSecond()
  {
    return second;
  }

  public void setSecond(T2 second)
  {
    this.second = second;
  }

}
