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
package propel.core.functional.projections;

import java.util.Arrays;
import lombok.Action;
import lombok.Actions.Action0;
import lombok.Function;
import lombok.Functions.Function0;
import propel.core.collections.KeyValuePair;

/**
 * Some common, re-usable projections
 */
public final class Projections
{
  private Projections()
  {
  }

  @Function
  public static <T> T argumentToResult(T arg)
  {
    return arg;
  }

  @Function
  public static String toStringify(Object object)
  {
    return object.toString();
  }
  
  @Function
  public static <T> String toStringln(T[] array)
  {
    return Arrays.toString(array);
  }
  
  @Function
  public static <T> T orElse(T value, T _elseValue)
  {
    if (value != null)
      return value;

    return _elseValue;
  }

  @Function
  public static String toLowerCase(String value)
  {
    return value.toLowerCase();
  }

  @Function
  public static String toUpperCase(String value)
  {
    return value.toUpperCase();
  }

  @Function
  public static <K, V> K keySelector(KeyValuePair<K, V> kvp)
  {
    return kvp.getKey();
  }

  @Function
  public static <K, V> V valueSelector(KeyValuePair<K, V> kvp)
  {
    return kvp.getValue();
  }
  
  @Action
  public static void println(Object obj) {
    System.out.println(obj);
  }
  
  @Action
  public static <T> void printlns(T[] array) {
    for(T element : array)
      System.out.println(element);
  }

  public static Action0 doNothing()
  {
    return new Action0()
    {

      @Override
      public void apply()
      {
        // does nothing
      }
    };
  }
  
  public static Function0<Void> doNothingFunc()
  {
    return new Function0<Void>()
    {

      @Override
      public Void apply()
      {
        // does nothing
        return null;
      }
    };
  }

}
