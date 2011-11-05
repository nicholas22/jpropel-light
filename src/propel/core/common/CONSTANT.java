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
package propel.core.common;

import propel.core.utils.StringUtils;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Defines a wide range of constants
 */
public final class CONSTANT
{

  /**
   * The special character '\0'
   */
  public static final char NULL_CHAR = '\0';
  /**
   * The special String "\0" (String terminator)
   */
  public static final String NULL_STRING = "\0";
  /**
   * The double dot String ".."
   */
  public static final String DOUBLE_DOT = "..";
  /**
   * The double whitespace String "  "
   */
  public static final String DOUBLE_WHITESPACE = "  ";
  /**
   * The double colon String "::"
   */
  public static final String DOUBLE_COLON = "::";
  /**
   * The double backlash String "\\"
   */
  public static final String DOUBLE_BACKSLASH = "\\\\";
  /**
   * The double forward slash String "//"
   */
  public static final String DOUBLE_FORWARDSLASH = "//";
  /**
   * The line break HTML &lt;br&gt;
   */
  public static final String BR = "<br>";
  /**
   * The line break XHTML &lt;br/&gt;
   */
  public static final String BR_XHTML = "<br/>";
  /**
   * The tab String "\t"
   */
  public static final String TAB = "\t";
  /**
   * The tab char '\t'
   */
  public static final char TAB_CHAR = '\t';
  /**
   * The line feed String "\n"
   */
  public static final String LF = "\n";
  /**
   * The line feed char '\n'
   */
  public static final char LF_CHAR = '\n';
  /**
   * The carriage return String "\r"
   */
  public static final String CR = "\r";
  /**
   * The carriage return char '\r'
   */
  public static final char CR_CHAR = '\r';
  /**
   * The Carriage Return Line Feed String "\r\n"
   */
  public static final String CRLF = "\r\n";
  /**
   * The dot String "."
   */
  public static final String DOT = ".";
  /**
   * The dot char '.'
   */
  public static final char DOT_CHAR = '.';
  /**
   * The colon String ":"
   */
  public static final String COLON = ":";
  /**
   * The colon char ':'
   */
  public static final char COLON_CHAR = ':';
  /**
   * The open paranthesis String "("
   */
  public static final String OPEN_PARENTHESIS = "(";
  /**
   * The open parenthesis char '('
   */
  public static final char OPEN_PARENTHESIS_CHAR = '(';
  /**
   * The close parenthesis String ")"
   */
  public static final String CLOSE_PARENTHESIS = ")";
  /**
   * The close parenthesis char ')'
   */
  public static final char CLOSE_PARENTHESIS_CHAR = ')';
  /**
   * The at sign String "@"
   */
  public static final String AT_SIGN = "@";
  /**
   * The at sign char '@'
   */
  public static final char AT_SIGN_CHAR = '@';
  /**
   * The not sign String "\u00ac"
   */
  public static final String NOT_SIGN = "\u00ac";
  /**
   * The not sign char '\u00ac'
   */
  public static final char NOT_SIGN_CHAR = '\u00ac';
  /**
   * The underscore String "_"
   */
  public static final String UNDERSCORE = "_";
  /**
   * The underscore char '_'
   */
  public static final char UNDERSCORE_CHAR = '_';
  /**
   * The overscore String "\u00af"
   */
  public static final String OVERSCORE = "\u00af";
  /**
   * The overscore char '\u00af'
   */
  public static final char OVERSCORE_CHAR = '\u00af';
  /**
   * The en dash String "\u2013"
   */
  public static final String EN_DASH = "\u2013";
  /**
   * The en dash char '\u2013'
   */
  public static final char EN_DASH_CHAR = '\u2013';
  /**
   * The em dash String "\u2014"
   */
  public static final String EM_DASH = "\u2014";
  /**
   * The em dash char '\u2014'
   */
  public static final char EM_DASH_CHAR = '\u2014';
  /**
   * The hyphen String "-"
   */
  public static final String HYPHEN = "-";
  /**
   * The hyphen char '-'
   */
  public static final char HYPHEN_CHAR = '-';
  /**
   * The plusminus sign String "\u00b1"
   */
  public static final String PLUSMINUS_SIGN = "\u00b1";
  /**
   * The plusminus sign char '\u00b1'
   */
  public static final char PLUSMINUS_SIGN_CHAR = '\u00b1';
  /**
   * The minus sign String "-"
   */
  public static final String MINUS_SIGN = HYPHEN;
  /**
   * The minus sign char '-'
   */
  public static final char MINUS_SIGN_CHAR = HYPHEN_CHAR;
  /**
   * The plus sign String "+"
   */
  public static final String PLUS_SIGN = "+";
  /**
   * The plus sign char '+'
   */
  public static final char PLUS_SIGN_CHAR = '+';
  /**
   * The equals sign String "="
   */
  public static final String EQUALS_SIGN = "=";
  /**
   * The equals sign char '='
   */
  public static final char EQUALS_SIGN_CHAR = '=';
  /**
   * The division sign String "\u00f7"
   */
  public static final String DIVISION_SIGN = "\u00f7";
  /**
   * The division sign char '\u00f7'
   */
  public static final char DIVISION_SIGN_CHAR = '\u00f7';
  /**
   * The multiplication sign String "\u00d7"
   */
  public static final String MULTIPLICATION_SIGN = "\u00d7";
  /**
   * The multiplication sign char '\u00d7'
   */
  public static final char MULTIPLICATION_SIGN_CHAR = '\u00d7';
  /**
   * The whitespace String " "
   */
  public static final String WHITESPACE = " ";
  /**
   * The whitespace char ' '
   */
  public static final char WHITESPACE_CHAR = ' ';
  /**
   * The whitespace chars ' ', '\t', '\r', '\n'
   */
  public static final char[] WHITESPACE_CHARS = new char[] {' ', '\t', '\r', '\n'};
  /**
   * The double quote String "
   */
  public static final String DOUBLE_QUOTE = "\"";
  /**
   * The double quote char '"'
   */
  public static final char DOUBLE_QUOTE_CHAR = '"';
  /**
   * The double quotation mark open String "\u201c"
   */
  public static final String DOUBLE_QUOTATION_MARK_OPEN = "\u201c";
  /**
   * The double quotation mark open char '\u201c'
   */
  public static final char DOUBLE_QUOTATION_MARK_OPEN_CHAR = '\u201c';
  /**
   * The double quotation mark close String "\u201d"
   */
  public static final String DOUBLE_QUOTATION_MARK_CLOSE = "\u201d";
  /**
   * The double quotation mark close char '\u201d'
   */
  public static final char DOUBLE_QUOTATION_MARK_CLOSE_CHAR = '\u201d';
  /**
   * The single quote String "'"
   */
  public static final String SINGLE_QUOTE = "'";
  /**
   * The single quote char '
   */
  public static final char SINGLE_QUOTE_CHAR = '\'';
  /**
   * The bracket open String "["
   */
  public static final String BRACKET_OPEN = "[";
  /**
   * The bracket open char '['
   */
  public static final char BRACKET_OPEN_CHAR = '[';
  /**
   * The bracket close String "]"
   */
  public static final String BRACKET_CLOSE = "]";
  /**
   * The bracket close char ']'
   */
  public static final char BRACKET_CLOSE_CHAR = ']';
  /**
   * The vertical bar String "|"
   */
  public static final String VERTICAL_BAR = "|";
  /**
   * The vertical bar char '|'
   */
  public static final char VERTICAL_BAR_CHAR = '|';
  /**
   * The broken vertical bar String "\u00a6"
   */
  public static final String BROKEN_VERTICAL_BAR = "\u00a6";
  /**
   * The broken vertical bar char "\u00a6"
   */
  public static final char BROKEN_VERTICAL_BAR_CHAR = '\u00a6';
  /**
   * The angle open String "&lt;"
   */
  public static final String ANGLE_OPEN = "<";
  /**
   * The angle open char '&lt;'
   */
  public static final char ANGLE_OPEN_CHAR = '<';
  /**
   * The angle close String "&gt;"
   */
  public static final String ANGLE_CLOSE = ">";
  /**
   * The angle close char '&gt;'
   */
  public static final char ANGLE_CLOSE_CHAR = '>';
  /**
   * The double angle open String "\u00ab"
   */
  public static final String DOUBLE_ANGLE_OPEN = "\u00ab";
  /**
   * The double angle open char '\u00ab'
   */
  public static final char DOUBLE_ANGLE_OPEN_CHAR = '\u00ab';
  /**
   * The double angle close String "\u00bb"
   */
  public static final String DOUBLE_ANGLE_CLOSE = "\u00bb";
  /**
   * The double angle close char '\u00bb'
   */
  public static final char DOUBLE_ANGLE_CLOSE_CHAR = '\u00bb';
  /**
   * The semicolon String ";"
   */
  public static final String SEMICOLON = ";";
  /**
   * The semicolon char ';'
   */
  public static final char SEMICOLON_CHAR = ';';
  /**
   * The comma String ","
   */
  public static final String COMMA = ",";
  /**
   * The comma char ','
   */
  public static final char COMMA_CHAR = ',';
  /**
   * The questionmark String "?"
   */
  public static final String QUESTIONMARK = "?";
  /**
   * The questionmark char '?'
   */
  public static final char QUESTIONMARK_CHAR = '?';
  /**
   * The exlamation mark String "!"
   */
  public static final String EXCLAMATIONMARK = "!";
  /**
   * The exclamation mark char '!'
   */
  public static final char EXCLAMATIONMARK_CHAR = '!';
  /**
   * The asterisk String "*"
   */
  public static final String ASTERISK = "*";
  /**
   * The asterisk char '*'
   */
  public static final char ASTERISK_CHAR = '*';
  /**
   * The forward slash String "/"
   */
  public static final String FORWARD_SLASH = "/";
  /**
   * The forward slash char '/'
   */
  public static final char FORWARD_SLASH_CHAR = '/';
  /**
   * The back slash String \
   */
  public static final String BACK_SLASH = "\\";
  /**
   * The back slash char '\'
   */
  public static final char BACK_SLASH_CHAR = '\\';
  /**
   * The euro currency sign String "\u20ac"
   */
  public static final String EURO_SIGN = "\u20ac";
  /**
   * The euro currency sign char '\u20ac'
   */
  public static final char EURO_SIGN_CHAR = '\u20ac';
  /**
   * The sterling currency sign String "\u00a3"
   */
  public static final String STERLING_SIGN = "\u00a3";
  /**
   * The sterling currency sign char '\u00a3'
   */
  public static final char STERLING_SIGN_CHAR = '\u00a3';
  /**
   * The dollar currency sign String "$"
   */
  public static final String DOLLAR_SIGN = "$";
  /**
   * The dollar currency sign char '$'
   */
  public static final char DOLLAR_SIGN_CHAR = '$';
  /**
   * The yen currency sign String "\u00a5"
   */
  public static final String YEN_SIGN = "\u00a5";
  /**
   * The yen currency sign char '\u00a5'
   */
  public static final char YEN_SIGN_CHAR = '\u00a5';
  /**
   * The dollar currency cent sign String "\u00a2"
   */
  public static final String CENT_SIGN = "\u00a2";
  /**
   * The dollar currency cent sign char '\u00a2'
   */
  public static final char CENT_SIGN_CHAR = '\u00a2';
  /**
   * The hash String "#"
   */
  public static final String HASH_SIGN = "#";
  /**
   * The hash char '#'
   */
  public static final char HASH_SIGN_CHAR = '#';
  /**
   * The percent String "%"
   */
  public static final String PERCENT_SIGN = "%";
  /**
   * The percent char '%'
   */
  public static final char PERCENT_SIGN_CHAR = '%';
  /**
   * The perthousand String "\u2030"
   */
  public static final String PERTHOUSAND_SIGN = "\u2030";
  /**
   * The perthousand char '\u2030'
   */
  public static final char PERTHOUSAND_SIGN_CHAR = '\u2030';
  /**
   * The circumflex String "^"
   */
  public static final String CIRCUMFLEX = "^";
  /**
   * The circumflex char '^'
   */
  public static final char CIRCUMFLEX_CHAR = '^';
  /**
   * The ampersand String "&amp;"
   */
  public static final String AMPERSAND = "&";
  /**
   * The ampersand char '&amp;'
   */
  public static final char AMPERSAND_CHAR = '&';
  /**
   * The tilde String "~"
   */
  public static final String TILDE = "~";
  /**
   * The tilde char '~'
   */
  public static final char TILDE_CHAR = '~';
  /**
   * The open brace String "{"
   */
  public static final String BRACE_OPEN = "{";
  /**
   * The open brace char '{'
   */
  public static final char BRACE_OPEN_CHAR = '{';
  /**
   * The close brace String "}"
   */
  public static final String BRACE_CLOSE = "}";
  /**
   * The close brace char '}'
   */
  public static final char BRACE_CLOSE_CHAR = '}';
  /**
   * The copyright String "\u00a9"
   */
  public static final String COPYRIGHT = "\u00a9";
  /**
   * The copyright char '\u00a9'
   */
  public static final char COPYRIGHT_CHAR = '\u00a9';
  /**
   * The registered trademark String "\u00ae"
   */
  public static final String REGISTERED = "\u00ae";
  /**
   * The registered trademark char '\u00ae'
   */
  public static final char REGISTERED_CHAR = '\u00ae';
  /**
   * The trademark String "\u2122"
   */
  public static final String TRADEMARK = "\u2122";
  /**
   * The trademark char'\u2122'
   */
  public static final char TRADEMARK_CHAR = '\u2122';
  /**
   * The bullet String "\u2022"
   */
  public static final String BULLET = "\u2022";
  /**
   * The bullet char '\u2022'
   */
  public static final char BULLET_CHAR = '\u2022';
  /**
   * The section String "\u00a7"
   */
  public static final String SECTION = "\u00a7";
  /**
   * The section char '\u00a7'
   */
  public static final char SECTION_CHAR = '\u00a7';
  /**
   * The squared String "\u00b2"
   */
  public static final String SQUARED = "\u00b2";
  /**
   * The squared char '\u00b2'
   */
  public static final char SQUARED_CHAR = '\u00b2';
  /**
   * The cubed String "\u00b3"
   */
  public static final String CUBED = "\u00b3";
  /**
   * The cubed char '\u00b3'
   */
  public static final char CUBED_CHAR = '\u00b3';
  /**
   * The String value "true"
   */
  public static final String TRUE = "true";
  /**
   * The String value "false"
   */
  public static final String FALSE = "false";
  /**
   * The grave accent String "`"
   */
  public static final String GRAVE_ACCENT = "`";
  /**
   * The grave accent char '`'
   */
  public static final char GRAVE_ACCENT_CHAR = '`';
  /**
   * The "http://" schema
   */
  public static final String HTTP_SCHEMA = "http://";
  /**
   * The "https://" schema
   */
  public static final String HTTPS_SCHEMA = "https://";
  /**
   * The zero guid String representation "00000000-0000-0000-0000-000000000000"
   */
  public static final String ZERO_UUID = "00000000-0000-0000-0000-000000000000";
  /**
   * The Guid.Empty value in .NET
   */
  public static final UUID EMPTY_UUID = UUID.fromString(ZERO_UUID);
  /**
   * The String.Empty, as a constant
   */
  public static final String EMPTY_STRING = "";
  /**
   * The 0 number as a character
   */
  public static final char ZERO_CHAR = '0';
  /**
   * The 0 number
   */
  public static final String ZERO = "0";
  /**
   * The 1 number
   */
  public static final String ONE = "1";
  /**
   * The 2 number
   */
  public static final String TWO = "2";
  /**
   * The 3 number
   */
  public static final String THREE = "3";
  /**
   * The 4 number
   */
  public static final String FOUR = "4";
  /**
   * The 5 number
   */
  public static final String FIVE = "5";
  /**
   * The 6 number
   */
  public static final String SIX = "6";
  /**
   * The 7 number
   */
  public static final String SEVEN = "7";
  /**
   * The 8 number
   */
  public static final String EIGHT = "8";
  /**
   * The 9 number
   */
  public static final String NINE = "9";
  /**
   * The A letter
   */
  public static final String A = "A";
  /**
   * The B letter
   */
  public static final String B = "B";
  /**
   * The C letter
   */
  public static final String C = "C";
  /**
   * The D letter
   */
  public static final String D = "D";
  /**
   * The E letter
   */
  public static final String E = "E";
  /**
   * The F letter
   */
  public static final String F = "F";
  /**
   * The G letter
   */
  public static final String G = "G";
  /**
   * The H letter
   */
  public static final String H = "H";
  /**
   * The I letter
   */
  public static final String I = "I";
  /**
   * The J letter
   */
  public static final String J = "J";
  /**
   * The K letter
   */
  public static final String K = "K";
  /**
   * The L letter
   */
  public static final String L = "L";
  /**
   * The M letter
   */
  public static final String M = "M";
  /**
   * The N letter
   */
  public static final String N = "N";
  /**
   * The O letter
   */
  public static final String O = "O";
  /**
   * The P letter
   */
  public static final String P = "P";
  /**
   * The Q letter
   */
  public static final String Q = "Q";
  /**
   * The R letter
   */
  public static final String R = "R";
  /**
   * The S letter
   */
  public static final String S = "S";
  /**
   * The T letter
   */
  public static final String T = "T";
  /**
   * The U letter
   */
  public static final String U = "U";
  /**
   * The V letter
   */
  public static final String V = "V";
  /**
   * The W letter
   */
  public static final String W = "W";
  /**
   * The X letter
   */
  public static final String X = "X";
  /**
   * The Y letter
   */
  public static final String Y = "Y";
  /**
   * The Z letter
   */
  public static final String Z = "Z";
  /**
   * The a letter
   */
  public static final String a = "a";
  /**
   * The b letter
   */
  public static final String b = "b";
  /**
   * The c letter
   */
  public static final String c = "c";
  /**
   * The d letter
   */
  public static final String d = "d";
  /**
   * The e letter
   */
  public static final String e = "e";
  /**
   * The f letter
   */
  public static final String f = "f";
  /**
   * The g letter
   */
  public static final String g = "g";
  /**
   * The h letter
   */
  public static final String h = "h";
  /**
   * The i letter
   */
  public static final String i = "i";
  /**
   * The j letter
   */
  public static final String j = "j";
  /**
   * The k letter
   */
  public static final String k = "k";
  /**
   * The l letter
   */
  public static final String l = "l";
  /**
   * The m letter
   */
  public static final String m = "m";
  /**
   * The n letter
   */
  public static final String n = "n";
  /**
   * The o letter
   */
  public static final String o = "o";
  /**
   * The p letter
   */
  public static final String p = "p";
  /**
   * The q letter
   */
  public static final String q = "q";
  /**
   * The r letter
   */
  public static final String r = "r";
  /**
   * The s letter
   */
  public static final String s = "s";
  /**
   * The t letter
   */
  public static final String t = "t";
  /**
   * The u letter
   */
  public static final String u = "u";
  /**
   * The v letter
   */
  public static final String v = "v";
  /**
   * The w letter
   */
  public static final String w = "w";
  /**
   * The x letter
   */
  public static final String x = "x";
  /**
   * The y letter
   */
  public static final String y = "y";
  /**
   * The z letter
   */
  public static final String z = "z";
  /**
   * The "localhost" String
   */
  public static final String LOCALHOST = "localhost";
  /**
   * The "EOF" String
   */
  public static final String EOF = "EOF";
  /**
   * The Carriage Return Line Feed String "\r\n"
   */
  public static final char[] CRLF_CHARS = new char[] {'\r', '\n'};
  /**
   * The Carriage Return Line Feed string "\r\n"
   */
  public static final byte[] CRLF_BYTES = new byte[] {(byte) '\r', (byte) '\n'};
  /**
   * The allowed digits in a hex string
   */
  public static final char[] HEX_DIGITS = new char[] {ZERO.charAt(0), ONE.charAt(0), TWO.charAt(0), THREE.charAt(0), FOUR.charAt(0),
    FIVE.charAt(0), SIX.charAt(0), SEVEN.charAt(0), EIGHT.charAt(0), NINE.charAt(0), A.charAt(0), B.charAt(0), C.charAt(0), D.charAt(0),
    E.charAt(0), F.charAt(0), a.charAt(0), b.charAt(0), c.charAt(0), d.charAt(0), e.charAt(0), f.charAt(0)};
  /**
   * For 62-based numbering schemes
   */
  public static final char[] ALPHANUMERIC_DIGITS = {ZERO.charAt(0), ONE.charAt(0), TWO.charAt(0), THREE.charAt(0), FOUR.charAt(0),
    FIVE.charAt(0), SIX.charAt(0), SEVEN.charAt(0), EIGHT.charAt(0), NINE.charAt(0), A.charAt(0), B.charAt(0), C.charAt(0), D.charAt(0),
    E.charAt(0), F.charAt(0), G.charAt(0), H.charAt(0), I.charAt(0), J.charAt(0), K.charAt(0), L.charAt(0), M.charAt(0), N.charAt(0),
    O.charAt(0), P.charAt(0), Q.charAt(0), R.charAt(0), S.charAt(0), T.charAt(0), U.charAt(0), V.charAt(0), W.charAt(0), X.charAt(0),
    Y.charAt(0), Z.charAt(0), a.charAt(0), b.charAt(0), c.charAt(0), d.charAt(0), e.charAt(0), f.charAt(0), g.charAt(0), h.charAt(0),
    i.charAt(0), j.charAt(0), k.charAt(0), l.charAt(0), m.charAt(0), n.charAt(0), o.charAt(0), p.charAt(0), q.charAt(0), r.charAt(0),
    s.charAt(0), t.charAt(0), u.charAt(0), v.charAt(0), w.charAt(0), x.charAt(0), y.charAt(0), z.charAt(0)};
  /**
   * Number 0..9
   */
  public static final char[] NUMBERS = {ZERO.charAt(0), ONE.charAt(0), TWO.charAt(0), THREE.charAt(0), FOUR.charAt(0), FIVE.charAt(0),
    SIX.charAt(0), SEVEN.charAt(0), EIGHT.charAt(0), NINE.charAt(0)};
  /**
   * English alphabet (lowercase)
   */
  public static final char[] ALPHABET_LOWERCASE = {a.charAt(0), b.charAt(0), c.charAt(0), d.charAt(0), e.charAt(0), f.charAt(0),
    g.charAt(0), h.charAt(0), i.charAt(0), j.charAt(0), k.charAt(0), l.charAt(0), m.charAt(0), n.charAt(0), o.charAt(0), p.charAt(0),
    q.charAt(0), r.charAt(0), s.charAt(0), t.charAt(0), u.charAt(0), v.charAt(0), w.charAt(0), x.charAt(0), y.charAt(0), z.charAt(0)};
  /**
   * English alphabet (uppercase)
   */
  public static final char[] ALPHABET_UPPERCASE = {A.charAt(0), B.charAt(0), C.charAt(0), D.charAt(0), E.charAt(0), F.charAt(0),
    G.charAt(0), H.charAt(0), I.charAt(0), J.charAt(0), K.charAt(0), L.charAt(0), M.charAt(0), N.charAt(0), O.charAt(0), P.charAt(0),
    Q.charAt(0), R.charAt(0), S.charAt(0), T.charAt(0), U.charAt(0), V.charAt(0), W.charAt(0), X.charAt(0), Y.charAt(0), Z.charAt(0)};
  /**
   * English alphabet (uppercase and lowercase)
   */
  public static final char[] ALPHABET = {A.charAt(0), B.charAt(0), C.charAt(0), D.charAt(0), E.charAt(0), F.charAt(0), G.charAt(0),
    H.charAt(0), I.charAt(0), J.charAt(0), K.charAt(0), L.charAt(0), M.charAt(0), N.charAt(0), O.charAt(0), P.charAt(0), Q.charAt(0),
    R.charAt(0), S.charAt(0), T.charAt(0), U.charAt(0), V.charAt(0), W.charAt(0), X.charAt(0), Y.charAt(0), Z.charAt(0), a.charAt(0),
    b.charAt(0), c.charAt(0), d.charAt(0), e.charAt(0), f.charAt(0), g.charAt(0), h.charAt(0), i.charAt(0), j.charAt(0), k.charAt(0),
    l.charAt(0), m.charAt(0), n.charAt(0), o.charAt(0), p.charAt(0), q.charAt(0), r.charAt(0), s.charAt(0), t.charAt(0), u.charAt(0),
    v.charAt(0), w.charAt(0), x.charAt(0), y.charAt(0), z.charAt(0)};
  /**
   * The allowed digits in a base64 String. It includes (as the last character) the '=' pad character, hence they are 65.
   */
  public static final char[] BASE64_DIGITS = new char[] {ZERO.charAt(0), ONE.charAt(0), TWO.charAt(0), THREE.charAt(0), FOUR.charAt(0),
    FIVE.charAt(0), SIX.charAt(0), SEVEN.charAt(0), EIGHT.charAt(0), NINE.charAt(0), A.charAt(0), B.charAt(0), C.charAt(0), D.charAt(0),
    E.charAt(0), F.charAt(0), G.charAt(0), H.charAt(0), I.charAt(0), J.charAt(0), K.charAt(0), L.charAt(0), M.charAt(0), N.charAt(0),
    O.charAt(0), P.charAt(0), Q.charAt(0), R.charAt(0), S.charAt(0), T.charAt(0), U.charAt(0), V.charAt(0), W.charAt(0), X.charAt(0),
    Y.charAt(0), Z.charAt(0), a.charAt(0), b.charAt(0), c.charAt(0), d.charAt(0), e.charAt(0), f.charAt(0), g.charAt(0), h.charAt(0),
    i.charAt(0), j.charAt(0), k.charAt(0), l.charAt(0), m.charAt(0), n.charAt(0), o.charAt(0), p.charAt(0), q.charAt(0), r.charAt(0),
    s.charAt(0), t.charAt(0), u.charAt(0), v.charAt(0), w.charAt(0), x.charAt(0), y.charAt(0), z.charAt(0), PLUS_SIGN_CHAR,
    FORWARD_SLASH_CHAR, EQUALS_SIGN_CHAR /* this is the 'pad' character */};
  /**
   * A constant similar to Timeout.Infinite in .NET
   */
  public static final int TIMEOUT_INFINITE = -1;
  /**
   * UTF8 encoding, charsets are thread-safe
   */
  public static final Charset UTF8 = Charset.forName("UTF-8");
  /**
   * UTF16 encoding, charsets are thread-safe
   */
  public static final Charset UTF16 = Charset.forName("UTF-16");
  /**
   * UTF32 encoding, charsets are thread-safe
   */
  public static final Charset UTF32 = Charset.forName("UTF-32");
  /**
   * US-ASCII encoding, charsets are thread-safe
   */
  public static final Charset ASCII = Charset.forName("US-ASCII");
  /**
   * This is similar to .NET's Path.GetInvalidFilenameChars() (on Win)
   */
  public static final char[] INVALID_FILENAME_CHARS_WINDOWS = StringUtils.concat(StringUtils.charRange(0, 32), new char[] {34, 42, 47, 58,
    60, 62, 63, 92, 124});
  /**
   * This is similar to .NET's Path.GetInvalidPathChars() (on Win)
   */
  public static final char[] INVALID_PATH_CHARS_WINDOWS = StringUtils.concat(StringUtils.charRange(0, 32), new char[] {34, 60, 62, 124});
  /**
   * This is similar to Path.GetInvalidFilenameChars() (on Linux/OSX/BSD)
   */
  public static final char[] INVALID_FILENAME_CHARS_POSIX = new char[] {0, 47};
  /**
   * This is similar to .NET's Path.GetInvalidPathChars() (on Linux/OSX/BSD)
   */
  public static final char[] INVALID_PATH_CHARS_POSIX = new char[] {0};
  /**
   * An OS-specific new line constant, similar to Environment.Newline constant in .NET. E.g. CRLF in Windows.
   */
  public static final String ENVIRONMENT_NEWLINE = System.getProperty("line.separator");
  /**
   * An OS-specific file separator constant, similar to Path.DirectorySeparatorChar constant in .NET. E.g. \ in Windows.
   */
  public static final String DIRECTORY_SEPARATOR = System.getProperty("file.separator");
  /**
   * An OS-specific file separator constant, similar to Path.DirectorySeparatorChar constant in .NET. E.g. \ in Windows.
   */
  public static final char DIRECTORY_SEPARATOR_CHAR = DIRECTORY_SEPARATOR.charAt(0);
  /**
   * An OS-specific file separator constant, similar to Path.PathSeparatorChar constant in .NET. E.g. ; in Windows.
   */
  public static final String PATH_SEPARATOR = System.getProperty("path.separator");
  /**
   * An OS-specific file separator constant, similar to Path.PathSeparatorChar constant in .NET. E.g. ; in Windows.
   */
  public static final char PATH_SEPARATOR_CHAR = PATH_SEPARATOR.charAt(0);
  /**
   * Returns the operating system version e.g. Windows 7 (x86) v6.1
   */
  public static final String OS_VERSION = System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") v"
      + System.getProperty("os.version");
}
