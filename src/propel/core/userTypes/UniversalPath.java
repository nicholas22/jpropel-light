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
package propel.core.userTypes;

/**
 * Entity encapsulating an immutable absolute path in a multi-platform format. Windows and *nix paths are supported, encoded in a neutral
 * format. This allows for easy conversion between platform-specific paths. As this is a cross-platform path, characters that are disallowed
 * in one platform are also disallowed in all other platforms.
 */
// TODO: implement
public final class UniversalPath
{
  private String drive;
  private String path;

  public UniversalPath()
  {

  }

  public UniversalPath(String drive, String path)
  {
    this.drive = drive;
    this.path = path;
  }

  public String getDrive()
  {
    return drive;
  }

  public String getPath()
  {
    return path;
  }
}
