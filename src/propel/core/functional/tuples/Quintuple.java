/*
 ///////////////////////////////////////////////////////////
 //  This file is part of Propel.
 //
 //  Propel is free software: you can redistribute it and/or modify
 //  it under the terms of the GNU Lesser General Public License as published by
 //  the Free Software Foundation, either version 3 of the License, or
 //  (at your option) any later version.
 //
 //  Propel is distributed in the hope that it will be useful,
 //  but WITHOUT ANY WARRANTY; without even the implied warranty of
 //  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 //  GNU Lesser General Public License for more details.
 //
 //  You should have received a copy of the GNU Lesser General Public License
 //  along with Propel.  If not, see <http://www.gnu.org/licenses/>.
 ///////////////////////////////////////////////////////////
 //  Authored by: Nikolaos Tountas -> salam.kaser-at-gmail.com
 ///////////////////////////////////////////////////////////
 */
package propel.core.functional.tuples;

/**
 * A tuple having four items
 */
public class Quintuple<T1, T2, T3, T4, T5>
{
  private T1 first;
  private T2 second;
  private T3 third;
  private T4 fourth;
  private T5 fifth;

  public Quintuple(T1 first, T2 second, T3 third, T4 fourth, T5 fifth)
  {
    this.first = first;
    this.second = second;
    this.third = third;
    this.fourth = fourth;
    this.fifth = fifth;
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

  public T3 getThird()
  {
    return third;
  }

  public void setThird(T3 third)
  {
    this.third = third;
  }

  public T4 getFourth()
  {
    return fourth;
  }

  public void setFourth(T4 fourth)
  {
    this.fourth = fourth;
  }

  public T5 getFifth()
  {
    return fifth;
  }

  public void setFifth(T5 fifth)
  {
    this.fifth = fifth;
  }
}
