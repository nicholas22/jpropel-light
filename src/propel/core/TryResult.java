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
package propel.core;

/**
 * Class encapsulates a boolean value and a result.
 * This is typically used to achieve returning two values from an operation
 * where the operation may succeed or fail; on the first instance two values
 * are returned (true and the result) but on the latter instance, only one (false).
 * This is also a way of achieving C#'s 'out' keyword functionality in Java.
 */
public final class TryResult<T>
{
	private T result;
	private boolean isSuccess;

	/**
	 * Default constructor.
	 * Use this when the attempt failed.
	 */
	public TryResult()
	{
		isSuccess = false;
	}

	/**
	 * Overloaded constructor.
	 * Used when the attempt succeeded, to store the result.
	 */
	public TryResult(T result)
	{
		isSuccess = true;
		this.result = result;
	}

	/**
	 * Returns the result of the try operation, if a success.
	 *
	 * @throws IllegalStateException When this is called and the operation is not a success.
	 */
	public T getResult()
	{
		if(!isSuccess())
			throw new IllegalStateException("Operation was not successful, so a result should not be requested.");

		return result;
	}

	/**
	 * Returns true if the attempt succeeded.
	 * The result has been set in this case.
	 * Do not attempt to retrieve the result if this returns false.
	 */
	public boolean isSuccess()
	{
		return isSuccess;
	}
}
