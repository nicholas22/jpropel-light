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
package propel.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides utility functionality for Exception manipulations
 */
public final class ExceptionUtils
{
	private ExceptionUtils()
	{
	}

	/**
	 * Gets all exception types and messages (top-level first, then inner) as an array of strings,
	 * e.g. MethodInvocationException: The method could not be invoked.
	 */
	public static List<String> getTypesAndMessages(Throwable e)
	{
		List<String> result = new ArrayList<String>();

		while(e != null)
		{
			result.add(e.getClass().getSimpleName() + ": " + e.getMessage());
			e = e.getCause();
		}

		return result;
	}

	/**
	 * Gets all exception messages (top-level first, then inner) as a string
	 * delimited with the specified string.
	 * e.g. MethodInvocationException: The method could not be invoked. [CRLF]
	 * e.g. IndexOutOrBoundsException: The provided index was outside the expected bounds.
	 */
	public static String getTypesAndMessages(Throwable e, String delimiter)
	{
		return StringUtils.delimit(getTypesAndMessages(e), delimiter);
	}

	/**
	 * Gets only exception messages (top-level first, then inner) as an array of strings
	 */
	public static List<String> getMessages(Throwable e)
	{
		List<String> result = new ArrayList<String>();

		// append elements
		while(e != null)
		{
			result.add(e.getMessage());
			e = e.getCause();
		}

		return result;
	}

	/**
	 * Gets only exception messages (top-level first, then inner) as a string
	 * delimited with the specified string.
	 */
	public static String getMessages(Throwable e, String delimiter)
	{
		return StringUtils.delimit(getMessages(e), delimiter);
	}

	/**
	 * If the Exception's inner exception is set, throws the inner exception.
	 * Otherwise throws the exception itself.
	 */
	public static void tryThrowInner(Throwable e)
			throws Throwable
	{
		if(e.getCause() != null)
			e = e.getCause();

		throw e;
	}
}
