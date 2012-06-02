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
package propel.core.functional.closure;

import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;

/**
 * A generic object wrapper that can be used for capturing values used across multiple closure constructs e.g. when using multiple anonymous
 * classes. A wrapper is needed because these objects must be declared final, otherwise cannot be shared as such.
 * 
 * @param <T> The type of the object being wrapped
 */
public class ClosureVariableWrapper<T>
{
  private final Class<?> genericTypeParameter;
  private T item;

  /**
   * Initializes with the encapsulated item.
   * 
   * @throws SuperTypeTokenException When not instantiated using anonymous class semantics.
   */
  public ClosureVariableWrapper(T item)
  {
    this.item = item;
    this.genericTypeParameter = SuperTypeToken.getClazz(this.getClass());
  }

  /**
   * Initializes with the encapsulated item.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public ClosureVariableWrapper(T item, Class<?> genericTypeParameter)
  {
    this.item = item;
    if (genericTypeParameter == null)
      throw new NullPointerException("genericTypeParameter");
    this.genericTypeParameter = genericTypeParameter;
  }

  /**
   * Returns the generic type parameter used by the closure wrapper
   */
  public Class<?> getGenericTypeParameter()
  {
    return genericTypeParameter;
  }

  /**
   * Gets the item
   */
  public T get()
  {
    return item;
  }

  /**
   * Sets the item
   */
  public void set(T item)
  {
    this.item = item;
  }
}
