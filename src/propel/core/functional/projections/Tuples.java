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
import java.util.Map.Entry;
import propel.core.collections.KeyValuePair;
import propel.core.functional.Functions.Function1;
import propel.core.functional.tuples.Pair;

/**
 * Some common, re-usable projections for tuples
 */
public final class Tuples
{
  /**
   * Returns the first element of the pair
   */
  public static <TFirst, TSecond> Function1<Pair<TFirst, TSecond>, TFirst> pairFirstSelector()
  {
    return new Function1<Pair<TFirst, TSecond>, TFirst>() {
      @Override
      public TFirst apply(final Pair<TFirst, TSecond> element)
      {
        return element.getFirst();
      }
    };
  }

  /**
   * Returns the second element of the pair
   */
  public static <TFirst, TSecond> Function1<Pair<TFirst, TSecond>, TSecond> pairSecondSelector()
  {
    return new Function1<Pair<TFirst, TSecond>, TSecond>() {
      @Override
      public TSecond apply(final Pair<TFirst, TSecond> element)
      {
        return element.getSecond();
      }
    };
  }

  // TODO: low-priority conversion
  //
  // /**
  // * Returns the first element of the triple
  // */
  // @Function
  // public static <TFirst, TSecond, TThird> TFirst tripleFirstSelector(final Triple<TFirst, TSecond, TThird> triple)
  // {
  // return triple.getFirst();
  // }
  //
  // /**
  // * Returns the second element of the triple
  // */
  // @Function
  // public static <TFirst, TSecond, TThird> TSecond tripleSecondSelector(final Triple<TFirst, TSecond, TThird> triple)
  // {
  // return triple.getSecond();
  // }
  //
  // /**
  // * Returns the third element of the triple
  // */
  // @Function
  // public static <TFirst, TSecond, TThird> TThird tripleThirdSelector(final Triple<TFirst, TSecond, TThird> triple)
  // {
  // return triple.getThird();
  // }
  //
  // /**
  // * Returns the first element of the quadruple
  // */
  // @Function
  // public static <TFirst, TSecond, TThird, TFourth> TFirst
  // quadrupleFirstSelector(final Quadruple<TFirst, TSecond, TThird, TFourth> quadruple)
  // {
  // return quadruple.getFirst();
  // }
  //
  // /**
  // * Returns the second element of the quadruple
  // */
  // @Function
  // public static <TFirst, TSecond, TThird, TFourth> TSecond
  // quadrupleSecondSelector(final Quadruple<TFirst, TSecond, TThird, TFourth> quadruple)
  // {
  // return quadruple.getSecond();
  // }
  //
  // /**
  // * Returns the third element of the quadruple
  // */
  // @Function
  // public static <TFirst, TSecond, TThird, TFourth> TThird
  // quadrupleThirdSelector(final Quadruple<TFirst, TSecond, TThird, TFourth> quadruple)
  // {
  // return quadruple.getThird();
  // }
  //
  // /**
  // * Returns the fourth element of the quadruple
  // */
  // @Function
  // public static <TFirst, TSecond, TThird, TFourth, TFifth> TFourth
  // quadrupleFourthSelector(final Quadruple<TFirst, TSecond, TThird, TFourth> quadruple)
  // {
  // return quadruple.getFourth();
  // }
  //
  // /**
  // * Returns the first element of the quintuple
  // */
  // @Function
  // public static <TFirst, TSecond, TThird, TFourth, TFifth> TFirst
  // quintupleFirstSelector(final Quintuple<TFirst, TSecond, TThird, TFourth, TFifth> quintuple)
  // {
  // return quintuple.getFirst();
  // }
  //
  // /**
  // * Returns the second element of the quintuple
  // */
  // @Function
  // public static <TFirst, TSecond, TThird, TFourth, TFifth> TSecond
  // quintupleSecondSelector(final Quintuple<TFirst, TSecond, TThird, TFourth, TFifth> quintuple)
  // {
  // return quintuple.getSecond();
  // }
  //
  // /**
  // * Returns the third element of the quintuple
  // */
  // @Function
  // public static <TFirst, TSecond, TThird, TFourth, TFifth> TThird
  // quintupleThirdSelector(final Quintuple<TFirst, TSecond, TThird, TFourth, TFifth> quintuple)
  // {
  // return quintuple.getThird();
  // }
  //
  // /**
  // * Returns the fourth element of the quintuple
  // */
  // @Function
  // public static <TFirst, TSecond, TThird, TFourth, TFifth> TFourth
  // quintupleFourthSelector(final Quintuple<TFirst, TSecond, TThird, TFourth, TFifth> quintuple)
  // {
  // return quintuple.getFourth();
  // }
  //
  // /**
  // * Returns the fifth element of the quintuple
  // */
  // @Function
  // public static <TFirst, TSecond, TThird, TFourth, TFifth> TFifth
  // quintupleFifthSelector(final Quintuple<TFirst, TSecond, TThird, TFourth, TFifth> quintuple)
  // {
  // return quintuple.getFifth();
  // }

  /**
   * Calls getKey() on map entries specified
   * 
   * @throws NullPointerException An argument is null
   */
  public static <K, V> Function1<Map.Entry<K, V>, K> mapKeySelector()
  {
    return new Function1<Map.Entry<K, V>, K>() {
      @Override
      public K apply(final Entry<K, V> element)
      {
        return element.getKey();
      }
    };
  }

  /**
   * Calls getValue() on map entries specified
   * 
   * @throws NullPointerException An argument is null
   */
  public static <K, V> Function1<Map.Entry<K, V>, V> mapValueSelector()
  {
    return new Function1<Map.Entry<K, V>, V>() {
      @Override
      public V apply(final Entry<K, V> element)
      {
        return element.getValue();
      }
    };
  }

  /**
   * Calls getKey() on map entries specified
   * 
   * @throws NullPointerException An argument is null
   */
  public static <K, V> Function1<KeyValuePair<K, V>, K> kvpKeySelector()
  {
    return new Function1<KeyValuePair<K, V>, K>() {
      @Override
      public K apply(final KeyValuePair<K, V> element)
      {
        return element.getKey();
      }
    };
  }

  /**
   * Calls getValue() on key/value pair function arguments
   * 
   * @throws NullPointerException An argument is null
   */
  public static <K, V> Function1<KeyValuePair<K, V>, V> kvpValueSelector()
  {
    return new Function1<KeyValuePair<K, V>, V>() {
      @Override
      public V apply(final KeyValuePair<K, V> element)
      {
        return element.getValue();
      }
    };
  }

  private Tuples()
  {
  }
}
