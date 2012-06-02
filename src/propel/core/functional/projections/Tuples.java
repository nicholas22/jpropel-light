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
package propel.core.functional.projections;

import java.util.Map;
import propel.core.collections.KeyValuePair;
import propel.core.functional.tuples.Pair;
import propel.core.functional.tuples.Quadruple;
import propel.core.functional.tuples.Quintuple;
import propel.core.functional.tuples.Triple;
import lombok.Function;

/**
 * Some common, re-usable projections for tuples
 */
public final class Tuples
{
  /**
   * Returns the first element of the pair
   */
  @Function
  public static <TFirst, TSecond> TFirst pairFirstSelector(final Pair<TFirst, TSecond> pair)
  {
    return pair.getFirst();
  }

  /**
   * Returns the second element of the pair
   */
  @Function
  public static <TFirst, TSecond> TSecond pairSecondSelector(final Pair<TFirst, TSecond> pair)
  {
    return pair.getSecond();
  }

  /**
   * Returns the first element of the triple
   */
  @Function
  public static <TFirst, TSecond, TThird> TFirst tripleFirstSelector(final Triple<TFirst, TSecond, TThird> triple)
  {
    return triple.getFirst();
  }

  /**
   * Returns the second element of the triple
   */
  @Function
  public static <TFirst, TSecond, TThird> TSecond tripleSecondSelector(final Triple<TFirst, TSecond, TThird> triple)
  {
    return triple.getSecond();
  }

  /**
   * Returns the third element of the triple
   */
  @Function
  public static <TFirst, TSecond, TThird> TThird tripleThirdSelector(final Triple<TFirst, TSecond, TThird> triple)
  {
    return triple.getThird();
  }

  /**
   * Returns the first element of the quadruple
   */
  @Function
  public static <TFirst, TSecond, TThird, TFourth> TFirst
      quadrupleFirstSelector(final Quadruple<TFirst, TSecond, TThird, TFourth> quadruple)
  {
    return quadruple.getFirst();
  }

  /**
   * Returns the second element of the quadruple
   */
  @Function
  public static <TFirst, TSecond, TThird, TFourth> TSecond
      quadrupleSecondSelector(final Quadruple<TFirst, TSecond, TThird, TFourth> quadruple)
  {
    return quadruple.getSecond();
  }

  /**
   * Returns the third element of the quadruple
   */
  @Function
  public static <TFirst, TSecond, TThird, TFourth> TThird
      quadrupleThirdSelector(final Quadruple<TFirst, TSecond, TThird, TFourth> quadruple)
  {
    return quadruple.getThird();
  }

  /**
   * Returns the fourth element of the quadruple
   */
  @Function
  public static <TFirst, TSecond, TThird, TFourth, TFifth> TFourth
      quadrupleFourthSelector(final Quadruple<TFirst, TSecond, TThird, TFourth> quadruple)
  {
    return quadruple.getFourth();
  }

  /**
   * Returns the first element of the quintuple
   */
  @Function
  public static <TFirst, TSecond, TThird, TFourth, TFifth> TFirst
      quintupleFirstSelector(final Quintuple<TFirst, TSecond, TThird, TFourth, TFifth> quintuple)
  {
    return quintuple.getFirst();
  }

  /**
   * Returns the second element of the quintuple
   */
  @Function
  public static <TFirst, TSecond, TThird, TFourth, TFifth> TSecond
      quintupleSecondSelector(final Quintuple<TFirst, TSecond, TThird, TFourth, TFifth> quintuple)
  {
    return quintuple.getSecond();
  }

  /**
   * Returns the third element of the quintuple
   */
  @Function
  public static <TFirst, TSecond, TThird, TFourth, TFifth> TThird
      quintupleThirdSelector(final Quintuple<TFirst, TSecond, TThird, TFourth, TFifth> quintuple)
  {
    return quintuple.getThird();
  }

  /**
   * Returns the fourth element of the quintuple
   */
  @Function
  public static <TFirst, TSecond, TThird, TFourth, TFifth> TFourth
      quintupleFourthSelector(final Quintuple<TFirst, TSecond, TThird, TFourth, TFifth> quintuple)
  {
    return quintuple.getFourth();
  }

  /**
   * Returns the fifth element of the quintuple
   */
  @Function
  public static <TFirst, TSecond, TThird, TFourth, TFifth> TFifth
      quintupleFifthSelector(final Quintuple<TFirst, TSecond, TThird, TFourth, TFifth> quintuple)
  {
    return quintuple.getFifth();
  }

  /**
   * Calls getKey() on map entries specified
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static <K, V> K mapKeySelector(final Map.Entry<K, V> entry)
  {
    return entry.getKey();
  }

  /**
   * Calls getValue() on map entries specified
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static <K, V> V mapValueSelector(final Map.Entry<K, V> entry)
  {
    return entry.getValue();
  }

  /**
   * Calls getKey() on map entries specified
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static <K, V> K kvpKeySelector(final KeyValuePair<K, V> kvp)
  {
    return kvp.getKey();
  }

  /**
   * Calls getValue() on key/value pair function arguments
   * 
   * @throws NullPointerException An argument is null
   */
  @Function
  public static <K, V> V kvpValueSelector(final KeyValuePair<K, V> kvp)
  {
    return kvp.getValue();
  }

  private Tuples()
  {
  }
}
