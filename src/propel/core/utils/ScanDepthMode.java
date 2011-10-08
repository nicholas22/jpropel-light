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
 * The scan depth mode indicates whether a shallow
 * scan is to be performed, or a deep scan. There are
 * two flavours of deep scanning supported, breadth-first
 * which enumerates folder contents before proceeding to
 * any subdirectories, and depth-first which visits the
 * deepest folders first, followed by siblings.
 */
public enum ScanDepthMode
{
	/**
	 * Only scans one level, without
	 * traversing any sub-directories.
	 */
	Shallow,
	/**
	 * Traverses subdirectories, in a breadth-first manner.
	 */
	BreadthFirst,
	/**
	 * Traverses subdirectories, in a depth-first manner.
	 */
	DepthFirst
}
