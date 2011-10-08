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
package propel.core.collections;

/**
 * The exception that is thrown when the key specified for accessing an element in a collection does not match any key in the collection.
 */
public final class KeyNotFoundException
		extends RuntimeException
{
  private static final long serialVersionUID = -541804579843337545L;

  /**
	 * Default constructor
	 */
	public KeyNotFoundException()
	{
	}

	/**
	 * Overloaded constructor
	 *
	 * @param msg The message
	 */
	public KeyNotFoundException(String msg)
	{
		super(msg);
	}

	/**
	 * Overloaded constructor
	 *
	 * @param msg   The message
	 * @param cause The cause
	 */
	public KeyNotFoundException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
