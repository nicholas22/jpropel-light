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

import propel.core.collections.KeyValuePair;

/**
 * Provides utility functionality for escaping and un-escaping text-based data e.g. XML, HTML and URLs
 */
public final class EscapingUtils
{
	private EscapingUtils()
	{
	}

	;
	/**
	 * These are the XML escaping conversions performed
	 */
	@SuppressWarnings("unchecked")
  private static final KeyValuePair<String, String>[] XML_PAIRS = Linq.cast(new Object[] {new KeyValuePair<String, String>("&", "&amp;"),
			new KeyValuePair<String, String>("<", "&lt;"), new KeyValuePair<String, String>(">", "&gt;"), new KeyValuePair<String, String>("\"", "&quot;"),
			new KeyValuePair<String, String>("'", "&apos;"), new KeyValuePair<String, String>("\t", "&#009;"), new KeyValuePair<String, String>("\n", "&#010;"),
			new KeyValuePair<String, String>("\r", "&#013;"), new KeyValuePair<String, String>(" ", "&#032;")}, KeyValuePair.class);
	/**
	 * These are the HTML escaping conversions performed
	 */
  @SuppressWarnings("unchecked")
	private static final KeyValuePair<String, String>[] HTML_PAIRS = Linq.cast( new Object[] {new KeyValuePair<String, String>("#", "&#035;"),
			XML_PAIRS[0], XML_PAIRS[1], XML_PAIRS[2], XML_PAIRS[3], XML_PAIRS[4], XML_PAIRS[5], XML_PAIRS[6], XML_PAIRS[7], XML_PAIRS[8],
			new KeyValuePair<String, String>("!", "&#033;"), new KeyValuePair<String, String>("$", "&#036;"), new KeyValuePair<String, String>("%", "&#037;"),
			new KeyValuePair<String, String>("(", "&#040;"), new KeyValuePair<String, String>(")", "&#041;"), new KeyValuePair<String, String>("*", "&#042;"),
			new KeyValuePair<String, String>("+", "&#043;"), new KeyValuePair<String, String>(",", "&#044;"), new KeyValuePair<String, String>("-", "&#045;"),
			new KeyValuePair<String, String>(".", "&#046;"), new KeyValuePair<String, String>("/", "&#047;"), new KeyValuePair<String, String>(":", "&#058;"),
			new KeyValuePair<String, String>("=", "&#061;"), new KeyValuePair<String, String>("?", "&#063;"), new KeyValuePair<String, String>("@", "&#064;"),
			new KeyValuePair<String, String>("[", "&#091;"), new KeyValuePair<String, String>("\\", "&#092;"), new KeyValuePair<String, String>("]", "&#093;"),
			new KeyValuePair<String, String>("^", "&#094;"), new KeyValuePair<String, String>("_", "&#095;"), new KeyValuePair<String, String>("`", "&#096;"),
			new KeyValuePair<String, String>("{", "&#123;"), new KeyValuePair<String, String>("|", "&#124;"), new KeyValuePair<String, String>("}", "&#125;"),
			new KeyValuePair<String, String>("~", "&#126;")}, KeyValuePair.class);
	/**
	 * These are the URL escaping conversions performed
	 */
  @SuppressWarnings("unchecked")
	private static final KeyValuePair<String, String>[] URL_PAIRS = Linq.cast(new Object[] {new KeyValuePair<String, String>("%", "%25"), // do this first to avoid e.g. %2520 for %20
			new KeyValuePair<String, String>("\t", "%09"), new KeyValuePair<String, String>("\n", "%0A"), new KeyValuePair<String, String>("\r", "%0D"), new KeyValuePair<String, String>(" ", "%20"),
			new KeyValuePair<String, String>("!", "%21"), new KeyValuePair<String, String>("\"", "%22"), new KeyValuePair<String, String>("#", "%23"), new KeyValuePair<String, String>("$", "%24"),
			new KeyValuePair<String, String>("&", "%26"), new KeyValuePair<String, String>("'", "%27"), new KeyValuePair<String, String>("(", "%28"), new KeyValuePair<String, String>(")", "%29"),
			new KeyValuePair<String, String>("*", "%2A"), new KeyValuePair<String, String>("+", "%2B"), new KeyValuePair<String, String>(",", "%2C"), new KeyValuePair<String, String>("-", "%2D"),
			new KeyValuePair<String, String>(".", "%2E"), new KeyValuePair<String, String>("/", "%2F"), new KeyValuePair<String, String>(":", "%3A"), new KeyValuePair<String, String>(";", "%3B"),
			new KeyValuePair<String, String>("<", "%3C"), new KeyValuePair<String, String>("=", "%3D"), new KeyValuePair<String, String>(">", "%3E"), new KeyValuePair<String, String>("?", "%3F"),
			new KeyValuePair<String, String>("@", "%40"), new KeyValuePair<String, String>("[", "%5B"), new KeyValuePair<String, String>("\\", "%5C"), new KeyValuePair<String, String>("]", "%5D"),
			new KeyValuePair<String, String>("^", "%5E"), new KeyValuePair<String, String>("_", "%5F"), new KeyValuePair<String, String>("`", "%60"), new KeyValuePair<String, String>("{", "%7B"),
			new KeyValuePair<String, String>("|", "%7C"), new KeyValuePair<String, String>("}", "%7D"), new KeyValuePair<String, String>("~", "%7E")}, KeyValuePair.class);

	/**
	 * Escapes a String for use in XML Strings e.g. &lt; becomes &amp;lt;
	 */
	public static String toXml(String text)
	{
		return escape(text, XML_PAIRS);
	}

	/**
	 * Escapes a String for use in HTML Strings e.g. @ becomes &amp;#064;
	 * The idea is to avoid use of scripts embedded in the HTML.
	 */
	public static String toHtml(String text)
	{
		return escape(text, HTML_PAIRS);
	}

	/**
	 * Escapes a String for use in URLs e.g. # becomes %23
	 */
	public static String toUrl(String text)
	{
		return escape(text, URL_PAIRS);
	}

	/**
	 * Unescapes an XML String e.g. &amp;lt; becomes &lt;
	 */
	public static String fromXml(String xml)
	{
		return unEscape(xml, XML_PAIRS);
	}

	/**
	 * Unescapes an HTML String e.g. &amp;#064; becomes @
	 */
	public static String fromHtml(String html)
	{
		return unEscape(html, HTML_PAIRS);
	}

	/**
	 * Unescapes a URL String e.g. %23 becomes #
	 */
	public static String fromUrl(String url)
	{
		return unEscape(url, URL_PAIRS);
	}

	/**
	 * Escapes some text with the provided conversions
	 */
	private static String escape(String text, KeyValuePair<String, String>[] pairs)
	{
		if(text == null)
			return null;
		else
		{
			// replace literal values with encoded entities
			for(KeyValuePair<String, String> kvp : pairs)
				text = StringUtils.replace(text, kvp.getKey(), kvp.getValue(), StringComparison.OrdinalIgnoreCase);

			return text;
		}
	}

	/**
	 * Un-escapes some text with the provided conversions
	 */
	private static String unEscape(String text, KeyValuePair<String, String>[] pairs)
	{
		if(text == null)
			return null;
		else
		{
			// replace literal values with encoded entities
			for(KeyValuePair<String, String> kvp : pairs)
				text = StringUtils.replace(text, kvp.getValue(), kvp.getKey(), StringComparison.OrdinalIgnoreCase);

			return text;
		}
	}
}
