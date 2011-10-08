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

/**
 * The string comparison mode
 */
public enum StringComparison
{
	/**
	 * Case sensitive comparison using the current locale e.g. en_GB
	 */
	CurrentLocale,
	/**
	 * Case insensitive comparison using the current locale e.g. en_GB
	 */
	CurrentLocaleIgnoreCase,
	/**
	 * Case sensitive comparison using an invariant locale i.e. en_US
	 */
	InvariantLocale,
	/**
	 * Case insensitive comparison using an invariant locale i.e. en_US
	 */
	InvariantLocaleIgnoreCase,
	/**
	 * Case sensitive comparison using String.indexOf
	 */
	Ordinal,
	/**
	 * Case insensitive per-character comparison, ignoring case for ASCII characters.
	 */
	OrdinalIgnoreCase
}
