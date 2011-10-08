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
package propel.core.counters;

/**
 * The interface of a counter
 */
public interface ICounter
{
	/**
	 * Returns the minimum value of the counter
	 */
	long getMinValue();

	/**
	 * Returns the maximum value of the counter
	 */
	long getMaxValue();

	/**
	 * Returns the counter value
	 */
	long getValue();

	/**
	 * Increments the counter and returns the next value
	 */
	long next();

	/**
	 * Resets the counter to the minimum value
	 */
	void reset();
}
