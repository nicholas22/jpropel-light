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
package propel.core.utils;

import java.util.ArrayList;
import java.util.List;
import lombok.Functions.Function1;
import lombok.Predicates.Predicate1;
import lombok.Validate;
import lombok.Validate.NotNull;
import propel.core.functional.tuples.Pair;

/**
 * Class allows for matching, helps in reducing the usage of nested if-then-else statements
 */
public class Matcher<T, TResult>
{
  private final List<Pair<Predicate1<T>, Function1<T, ?>>> cases;
  private Function1<T, ?> defaultFunc;

  /**
   * Default constructor
   */
  public Matcher()
  {
    cases = new ArrayList<Pair<Predicate1<T>, Function1<T, ?>>>();
  }

  /**
   * Adds a predicate and a function. If the predicate is true for the element being matched by match(), the given function will execute and
   * its result will be returned by the match() method, effectively matching the input to this given function's result.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public void addFunction(@NotNull final Predicate1<T> predicate, @NotNull final Function1<T, TResult> func)
  {
    cases.add(new Pair<Predicate1<T>, Function1<T, ?>>(predicate, func));
  }

  /**
   * Adds a predicate and a function. If the predicate is true for the element being matched by match(), the an identity function (F(x)=>x)
   * with TResult cast to T will execute and its result will be returned by the match() method, effectively matching and returning the
   * argument.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public void addFunction(@NotNull final Predicate1<T> predicate)
  {
    cases.add(new Pair<Predicate1<T>, Function1<T, ?>>(predicate, new Function1<T, TResult>() {
      @SuppressWarnings("unchecked")
      public TResult apply(T arg)
      {
        return (TResult) arg;
      }
    }));
  }

  /**
   * Adds a predicate and a function. If the predicate is true for the element being matched by match(), the given function will execute and
   * its result will be returned by the match() method, effectively matching the input to this given function's result.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public void addAction(@NotNull final Predicate1<T> predicate, @NotNull final Function1<T, Void> func)
  {
    cases.add(new Pair<Predicate1<T>, Function1<T, ?>>(predicate, func));
  }

  /**
   * Sets the action that generates a default result, if there are no matches. Since this is an action, null is returned, but there is a
   * chance to perform an operation, e.g. throw an exception. Setting a null value means that the matcher will return null upon no matches,
   * as there is no default value generator.
   */
  public void setDefaultAction(final Function1<T, Void> func)
  {
    defaultFunc = func;
  }

  /**
   * Sets the function that generates a default result, if there are no matches Setting a null value means that the matcher will return null
   * upon no matches, as there is no default value generator.
   */
  public void setDefaultFunction(final Function1<T, TResult> func)
  {
    defaultFunc = func;
  }

  /**
   * Returns the first match based on the given input. It iterates over all of the given predicates and if there is one that is true, its
   * associated function executes. If none of the predicates is true, the default function is executed. If there is no default matcher, then
   * null is returned.
   */
  @SuppressWarnings("unchecked")
  public TResult match(T input)
  {
    // attempt to match in order that matchers were added
    for (int i = 0; i < cases.size(); i++)
      if (cases.get(i).getFirst().apply(input))
        return (TResult) cases.get(i).getSecond().apply(input);

    // use default matcher
    if (defaultFunc != null)
      return (TResult) defaultFunc.apply(input);

    return null;
  }
}
