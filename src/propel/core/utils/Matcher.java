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
import lombok.Actions.Action0;
import lombok.Actions.Action1;
import lombok.Functions.Function1;
import lombok.Predicates.Predicate1;
import lombok.Validate;
import lombok.Validate.NotNull;
import lombok.val;
import propel.core.functional.projections.MiscProjections;
import propel.core.functional.tuples.Pair;

/**
 * Class allows for matching, helps in reducing the usage of nested if-then-else statements. It acts much like a switch statement: a number
 * of predicates are tested and if one matches, its associated action/function executes. These is a default one which can be executed if
 * there are no matches to, for example, throw a mismatch exception.
 */
public class Matcher<T, TResult>
{
  @SuppressWarnings("rawtypes")
  private static final Function1 argToResult = (Function1) MiscProjections.argumentToResult();

  // list of (predicate -> function)
  private final List<Pair<Predicate1<T>, Function1<T, TResult>>> cases;
  // default function (if any), used when no predicates match
  private Function1<T, TResult> defaultFunc;

  /**
   * Default constructor
   */
  public Matcher()
  {
    cases = new ArrayList<Pair<Predicate1<T>, Function1<T, TResult>>>();
  }

  /**
   * Adds a predicate and a function. If the predicate is true for the element being matched, the given function will execute and its result
   * will be returned by the match() method, effectively matching the input to the result.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public void addFunction(@NotNull final Predicate1<T> predicate, @NotNull final Function1<T, TResult> func)
  {
    cases.add(new Pair<Predicate1<T>, Function1<T, TResult>>(predicate, func));
  }

  /**
   * Adds a predicate and an identity function (F(x)=>x). If the predicate is true for the element being matched, the input T will be cast
   * to TResult and returned by the match() method, effectively matching the input to the result.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public void addFunction(@NotNull final Predicate1<T> predicate)
  {
    cases.add(new Pair<Predicate1<T>, Function1<T, TResult>>(predicate, argToResult));
  }

  /**
   * Adds a predicate and a function. If the predicate is true for the element being matched by match(), the given function will execute and
   * its result will be returned by the match() method, effectively matching the input to this given function's result.
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  public void addAction(@NotNull final Predicate1<T> predicate, @NotNull final Action1<T> action)
  {
    val func = actionToFunction(action);
    cases.add(new Pair<Predicate1<T>, Function1<T, TResult>>(predicate, func));
  }

  /**
   * Sets the action that generates a default result, if there are no matches. Since this is an action, null is returned, but there is a
   * chance to perform an operation rather than return a result, for example, throw an exception. Setting the default action to null value
   * means that the matcher will return null upon no matches.
   */
  public void setDefaultAction(final Action1<T> action)
  {
    if (action == null)
      defaultFunc = null;
    else
      defaultFunc = actionToFunction(action);
  }

  /**
   * Sets the function that generates a default result, if there are no matches. Although this is a function, there is a chance to perform
   * an operation rather than return a result, for example, throw an exception. Setting the default function to null value means that the
   * matcher will return null upon no matches.
   */
  public void setDefaultFunction(final Function1<T, TResult> func)
  {
    defaultFunc = func;
  }

  /**
   * Convenience method for throwing a specified exception type and message, if there are no matches to the matcher's input
   * 
   * @throws NullPointerException An argument is null
   */
  public void setDefaultThrow(@NotNull final Throwable e)
  {
    defaultFunc = actionToFunction(MiscProjections.throwException(e));
  }

  /**
   * Convenience method for throwing a specified exception type and message, if there are no matches to the matcher's input, with the
   * input's toString() representation being appended to the exception message.
   * 
   * @throws NullPointerException An argument is null
   */
  @SuppressWarnings("unchecked")
  public void setDefaultThrowDetailed(@NotNull final Throwable e)
  {
    defaultFunc = (Function1<T, TResult>) MiscProjections.throwDetailedFunc(e);
  }

  /**
   * Returns the first match based on the given input. It iterates over all of the given predicates and if there is one that is true, its
   * associated function executes, returning a result. If none of the predicates is true, the default function (if one exists) is executed.
   * Note that sometimes the default function may be to throw an exception. Otherwise null is returned.
   * 
   * @throws Throwable One of the predicates or actions may throw an exception
   */
  public TResult match(final T input)
  {
    // attempt to match in order that matchers were added
    for (val current : cases)
    {
      val predicate = current.getFirst();
      if (predicate.apply(input))
      {
        val function = current.getSecond();
        return function.apply(input);
      }
    }

    // use default matcher
    if (defaultFunc != null)
      return defaultFunc.apply(input);

    return null;
  }

  /**
   * Converts an action to a function which returns null post execution
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  private Function1<T, TResult> actionToFunction(@NotNull final Action1<T> action)
  {
    return new Function1<T, TResult>() {
      @Override
      public TResult apply(T arg0)
      {
        action.apply(arg0);
        return null;
      }
    };
  }

  /**
   * Converts a no-arg action to a function which returns null post execution
   * 
   * @throws NullPointerException An argument is null
   */
  @Validate
  private Function1<T, TResult> actionToFunction(@NotNull final Action0 action)
  {
    return new Function1<T, TResult>() {
      @Override
      public TResult apply(T arg0)
      {
        action.apply();
        return null;
      }
    };
  }
}
