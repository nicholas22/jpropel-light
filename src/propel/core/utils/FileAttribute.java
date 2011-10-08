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
 * Denotes a file/folder attribute
 */
public enum FileAttribute
{
	/**
	 * File/Folder can be read
	 */
	Readable(1),
	/**
	 * File/Folder can be written to
	 */
	Writeable(2),
	/**
	 * File/Folder can be executed
	 */
	Executable(3),
	/**
	 * File/Folder is hidden
	 */
	Hidden(4),
	/**
	 * File type
	 */
	File(5),
	/**
	 * Folder type
	 */
	Directory(6);
   
	// private
	int attribute;

	private FileAttribute(int attribute)
	{
		this.attribute = attribute;
		if(attribute < 1 || attribute > 6)
			throw new IllegalArgumentException("attribute=" + attribute);
	}

	public int getAttribute()
	{
		return attribute;
	}
}
