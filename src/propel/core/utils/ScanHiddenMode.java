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
 * Used to specified whether hidden files are included in scan results or not
 */
public enum ScanHiddenMode
{
	/**
	 * Returns normal and hidden files.
	 */
	All,
	/**
	 * Returns normal and excludes hidden files.
	 */
	NormalOnly,
	/**
	 * Returns hidden and excludes normal files.
	 * Please be aware that when using this option for deep scans you may get unexpected results- this is because non-hidden directories are omitted from search!
	 */
	HiddenOnly
}
