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
package propel.core.configuration;

import propel.core.collections.KeyValuePair;
import propel.core.collections.ReifiedIterable;
import propel.core.collections.lists.ReifiedArrayList;
import propel.core.common.CONSTANT;
import propel.core.utils.ArrayUtils;
import propel.core.utils.OsUtils;
import propel.core.utils.StringUtils;

public final class ConfigurableConsts
{
  /**
   * Private constructor
   */
  private ConfigurableConsts()
  {
  }

  // Common
  /**
   * The number of stack frames to show when ABBREVIATED stack trace logging is used
   */
  public static final KeyValuePair<String, Integer> STACKTRACE_LOGGER_MAX_STACKFRAMES = new KeyValuePair<String, Integer>(
      "StackTraceLogger.MAX_STACKFRAMES", 5);

  // DbC
  /**
   * The full type name of the DbC contracts engine used
   */
  public static final KeyValuePair<String, String> CONTRACTSENGINE_TYPENAME = new KeyValuePair<String, String>("ContractsEngine.TYPENAME",
      "propel.dbc.engine.ContractsEngine");
  /**
   * The default maximum number of objects created between forced sweeps.
   */
  public static final KeyValuePair<String, Integer> CONTRACTSENGINE_DEFAULT_SWEEP_THRESHOLD = new KeyValuePair<String, Integer>(
      "ContractsEngine.DEFAULT_SWEEP_THRESHOLD", 50);
  /**
   * ToStringCombiner classs does not combine more than this amount of elements
   */
  public static final KeyValuePair<String, Short> TOSTRINGCOMBINER_MAX_COUNT = new KeyValuePair<String, Short>(
      "ToStringCombiner.MAX_COUNT", (short) 20);

  // Linq
  /**
   * When Linq needs to create a list for storing a collection of items, it will set the initial size to this.
   */
  public static final KeyValuePair<String, Integer> LINQ_DEFAULT_LIST_SIZE = new KeyValuePair<String, Integer>("Linq.DEFAULT_LIST_SIZE",
      128);

  // Network
  /**
   * This is the maximum amount of time to wait for a ping response
   */
  public static final KeyValuePair<String, Short> NETWORK_PING_TIMEOUT_MILLIS = new KeyValuePair<String, Short>(
      "Network.PING_TIMEOUT_MILLIS", (short) 1000);
  /**
   * The port designated to the SNMP protocol
   */
  public static final KeyValuePair<String, Short> NETWORK_SNMP_PORT = new KeyValuePair<String, Short>("Network.SNMP_PORT", (short) 161);
  /**
   * The amount of time in millis to wait for an SNMP response
   */
  public static final KeyValuePair<String, Short> NETWORK_SNMP_READ_TIMEOUT_MILLIS = new KeyValuePair<String, Short>(
      "Network.SNMP_READ_TIMEOUT_MILLIS", (short) 3500);
  /**
   * Standard SNMP community that is open for unrestricted reading
   */
  public static final KeyValuePair<String, String> NETWORK_SNMP_COMMUNITY = new KeyValuePair<String, String>("Network.SNMP_COMMUNITY",
      "public");
  /**
   * SNMP retry interval upon timeout failure
   */
  public static final KeyValuePair<String, Short> NETWORK_SNMP_RETRY_INTERVAL_MILLIS = new KeyValuePair<String, Short>(
      "Network.SNMP_RETRY_INTERVAL_MILLIS", (short) 250);
  /**
   * URL for whatismyip.com that allows external IP polling
   */
  public static final KeyValuePair<String, String> NETWORK_WHATISMYIP_COM_URL = new KeyValuePair<String, String>(
      "Network.WHATISMYIP_COM_URL", "http://www.whatismyip.com/automation/n09230945.asp");
  /**
   * URL for whatismyip.org that allows external IP polling
   */
  public static final KeyValuePair<String, String> NETWORK_WHATISMYIP_ORG_URL = new KeyValuePair<String, String>(
      "Network.WHATISMYIP_ORG_URL", "http://www.whatismyip.org");
  /**
   * URL for checkip.dyndns.org that allows external IP polling
   */
  public static final KeyValuePair<String, String> NETWORK_CHECKIP_DYNDNS_URL = new KeyValuePair<String, String>(
      "Network.CHECKIP_DYNDNS_URL", "http://checkip.dyndns.org");

  // SOA
  /**
   * The D/H A and B parameter length in bits
   */
  public static final KeyValuePair<String, Short> PROTOCOLMANAGER_AB_PRIME_BITS = new KeyValuePair<String, Short>(
      "ProtocolManager.AB_PRIME_BITS", (short) 256);
  /**
   * The D/H P parameter length in bits
   */
  public static final KeyValuePair<String, Short> PROTOCOLMANAGER_P_PRIME_BITS = new KeyValuePair<String, Short>(
      "ProtocolManager.P_PRIME_BITS", (short) 256);
  /**
   * The maximum transferred object length, if object is larger than this then it will be sent/received in chunks of this size, to avoid
   * excessive memory consumption
   */
  public static final KeyValuePair<String, Integer> TCPSOCKETMANAGER_MAX_CHUNK_SIZE = new KeyValuePair<String, Integer>(
      "TcpSocketManager.MAX_CHUNK_SIZE", 10485760); // 10meg
  /**
   * The maximum transferred object length, if object is larger than this then it will be sent/received in chunks of this size, to avoid
   * excessive memory consumption
   */
  public static final KeyValuePair<String, Integer> HTTPSOCKETMANAGER_MAX_CHUNK_SIZE = new KeyValuePair<String, Integer>(
      "HttpSocketManager.MAX_CHUNK_SIZE", 10485760); // 10meg

  // Tasks
  /**
   * Whether task threads are daemon threads or not. Non-daemon threads prevent a program from exiting until they complete.
   */
  public static final KeyValuePair<String, Boolean> TASKTHREADPOOL_DAEMON_THREADS = new KeyValuePair<String, Boolean>(
      "TaskThreadPool.DAEMON_THREADS", true);
  /**
   * Task threadpool has this many (initial) threads available to execute tasks for bursts of parallel activity.
   */
  public static final KeyValuePair<String, Integer> TASKTHREADPOOL_MIN_IDLE_THREADS = new KeyValuePair<String, Integer>(
      "TaskThreadPool.MIN_IDLE_THREADS", Runtime.getRuntime().availableProcessors() * 4);
  /**
   * Thread naming convention: Prefixes this to thread ID, postfixed by creator thread
   */
  public static final KeyValuePair<String, String> TASKTHREADPOOL_THREAD_ID_PREFIX = new KeyValuePair<String, String>(
      "TaskThreadPool.THREAD_ID_PREFIX", "-T");
  /**
   * When the available number of threads falls below this threshold, the threadpool will start creating a number of extra threads
   */
  public static final KeyValuePair<String, Short> TASKTHREADPOOL_MIN_THREAD_REPLENISH_TRIGGER = new KeyValuePair<String, Short>(
      "TaskThreadPool.MIN_THREAD_REPLENISH_TRIGGER", (short) 2);
  /**
   * This is the number of threads that the threadpool will create if reached a low thread count threshold that triggers it
   */
  public static final KeyValuePair<String, Short> TASKTHREADPOOL_THREAD_REPLENISH_COUNT = new KeyValuePair<String, Short>(
      "TaskThreadPool.TASKTHREADPOOL_THREAD_REPLENISH_COUNT", (short) 4);

  // UniversalPath
  /**
   * See UniversalPath's CONVERSION_UNIX_TO_WINDOWS_DEFAULT_DRIVE constant
   */
  public static final KeyValuePair<String, String> UNIVERSALPATH_CONVERSION_UNIX_TO_WINDOWS_DEFAULT_DRIVE = new KeyValuePair<String, String>(
      "UniversalPath.CONVERSION_UNIX_TO_WINDOWS_DEFAULT_DRIVE", "C");
  /**
   * See UniversalPath's CONVERSION_UNIX_TO_WINDOWS_DEFAULT_FOLDER constant
   */
  public static final KeyValuePair<String, String> UNIVERSALPATH_CONVERSION_UNIX_TO_WINDOWS_DEFAULT_FOLDER = new KeyValuePair<String, String>(
      "UniversalPath.CONVERSION_UNIX_TO_WINDOWS_DEFAULT_FOLDER", "unicsFS");
  /**
   * See UniversalPath's CONVERSION_WINDOWS_TO_UNIX_DRIVE_ENCODING_PREFIX constant
   */
  public static final KeyValuePair<String, String> UNIVERSALPATH_CONVERSION_WINDOWS_TO_UNIX_DRIVE_ENCODING_PREFIX = new KeyValuePair<String, String>(
      "UniversalPath.CONVERSION_WINDOWS_TO_UNIX_DRIVE_ENCODING_PREFIX", "WinVolume_");

  // Validation
  /**
   * Allowed sub-domain characters: A-Z, a-z, 0-9 and hyphen
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_DISALLOWED_SUBDOMAIN_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.DISALLOWED_SUBDOMAIN_CHARS", new ReifiedArrayList<Character>(ArrayUtils.boxChars(StringUtils.concat(
          StringUtils.charRange(1, 45), StringUtils.charRange(46, 48), StringUtils.charRange(58, 65), StringUtils.charRange(91, 97),
          StringUtils.charRange(123, 128))), Character.class));
  /**
   * Characters that are not allowed at the start of a sub-domain
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_DISALLOWED_SUBDOMAIN_START_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.DISALLOWED_SUBDOMAIN_START_CHARS", new ReifiedArrayList<Character>(
          ArrayUtils.boxChars(new char[] {CONSTANT.HYPHEN_CHAR}), Character.class));
  /**
   * Characters that are not allowed at the end of a sub-domain
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_DISALLOWED_SUBDOMAIN_END_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.DISALLOWED_SUBDOMAIN_END_CHARS", new ReifiedArrayList<Character>(ArrayUtils.boxChars(new char[] {CONSTANT.HYPHEN_CHAR}),
          Character.class));
  /**
   * Allowed localpart characters: A-Z, a-z, 0-9, ! # $ % &amp; ' * + - / = ? ^ _ ` { | } ~ .
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_DISALLOWED_EMAIL_LOCALPART_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.DISALLOWED_EMAIL_LOCALPART_CHARS", new ReifiedArrayList<Character>(ArrayUtils.boxChars(StringUtils.concat(
          StringUtils.charRange(1, 33), new char[] {CONSTANT.DOUBLE_QUOTE_CHAR, CONSTANT.OPEN_PARENTHESIS_CHAR,
            CONSTANT.CLOSE_PARENTHESIS_CHAR, CONSTANT.COMMA_CHAR, CONSTANT.COLON_CHAR, CONSTANT.SEMICOLON_CHAR, CONSTANT.ANGLE_OPEN_CHAR,
            CONSTANT.ANGLE_CLOSE_CHAR, CONSTANT.AT_SIGN_CHAR, CONSTANT.BRACKET_OPEN_CHAR, CONSTANT.BACK_SLASH_CHAR,
            CONSTANT.BRACKET_CLOSE_CHAR, (char) 127})), Character.class));
  /**
   * Characters not allowed at the start of and email's local part
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_DISALLOWED_EMAIL_LOCALPART_START_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.DISALLOWED_EMAIL_LOCALPART_START_CHARS", new ReifiedArrayList<Character>(
          ArrayUtils.boxChars(new char[] {CONSTANT.DOT_CHAR}), Character.class));
  /**
   * Characters not allowed at the end of and email's local part
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_DISALLOWED_EMAIL_LOCALPART_END_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.DISALLOWED_EMAIL_LOCALPART_END_CHARS", new ReifiedArrayList<Character>(
          ArrayUtils.boxChars(new char[] {CONSTANT.DOT_CHAR}), Character.class));
  /**
   * Disallowed filename characters
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_DISALLOWED_FILENAME_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.DISALLOWED_FILENAME_CHARS", OsUtils.isWindows() ? new ReifiedArrayList<Character>(
          ArrayUtils.boxChars(CONSTANT.INVALID_FILENAME_CHARS_WINDOWS), Character.class) : new ReifiedArrayList<Character>(
          ArrayUtils.boxChars(CONSTANT.INVALID_FILENAME_CHARS_POSIX), Character.class));
  /**
   * Characters not allowed to be at the start of a filename
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_DISALLOWED_FILENAME_START_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.DISALLOWED_FILENAME_START_CHARS", new ReifiedArrayList<Character>(
          ArrayUtils.boxChars(new char[] {CONSTANT.WHITESPACE_CHAR}), Character.class));
  /**
   * Characters not allowed to be at the end of a filename
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_DISALLOWED_FILENAME_END_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.DISALLOWED_FILENAME_END_CHARS", new ReifiedArrayList<Character>(
          ArrayUtils.boxChars(new char[] {CONSTANT.WHITESPACE_CHAR}), Character.class));
  /**
   * Disallowed path characters
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_DISALLOWED_PATH_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.DISALLOWED_PATH_CHARS", OsUtils.isWindows() ? new ReifiedArrayList<Character>(
          ArrayUtils.boxChars(CONSTANT.INVALID_PATH_CHARS_WINDOWS), Character.class) : new ReifiedArrayList<Character>(
          ArrayUtils.boxChars(CONSTANT.INVALID_PATH_CHARS_POSIX), Character.class));
  /**
   * Characters not allowed to be at the start of a path
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_DISALLOWED_PATH_START_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.DISALLOWED_PATH_START_CHARS", new ReifiedArrayList<Character>(ArrayUtils.boxChars(new char[] {CONSTANT.WHITESPACE_CHAR}),
          Character.class));
  /**
   * Characters not allowed to be at the end of a path
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_DISALLOWED_PATH_END_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.DISALLOWED_PATH_END_CHARS", new ReifiedArrayList<Character>(ArrayUtils.boxChars(new char[] {CONSTANT.WHITESPACE_CHAR}),
          Character.class));
  /**
   * Disallowed Active Directory username characters
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_DISALLOWED_AD_USERNAME_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.DISALLOWED_AD_USERNAME_CHARS", new ReifiedArrayList<Character>(ArrayUtils.boxChars(new char[] {CONSTANT.BACK_SLASH_CHAR,
        CONSTANT.FORWARD_SLASH_CHAR, CONSTANT.DOUBLE_QUOTE_CHAR, CONSTANT.BRACKET_OPEN_CHAR, CONSTANT.BRACE_CLOSE_CHAR,
        CONSTANT.COLON_CHAR, CONSTANT.VERTICAL_BAR_CHAR, CONSTANT.ANGLE_OPEN_CHAR, CONSTANT.ANGLE_CLOSE_CHAR, CONSTANT.PLUS_SIGN_CHAR,
        CONSTANT.EQUALS_SIGN_CHAR, CONSTANT.SEMICOLON_CHAR, CONSTANT.COMMA_CHAR, CONSTANT.QUESTIONMARK_CHAR, CONSTANT.ASTERISK_CHAR,
        CONSTANT.AT_SIGN_CHAR}), Character.class));
  /**
   * Characters disallowed to appear at the start of an Active Directory username
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_DISALLOWED_AD_USERNAME_START_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.DISALLOWED_AD_USERNAME_START_CHARS", new ReifiedArrayList<Character>(
          ArrayUtils.boxChars(new char[] {CONSTANT.WHITESPACE_CHAR}), Character.class));
  /**
   * Characters disallowed to appear at the end of an Active Directory username
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_DISALLOWED_AD_USERNAME_END_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.DISALLOWED_AD_USERNAME_END_CHARS", new ReifiedArrayList<Character>(ArrayUtils.boxChars(new char[] {
        CONSTANT.WHITESPACE_CHAR, CONSTANT.DOT_CHAR}), Character.class));
  /**
   * Allowed telephone number characters
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_ALLOWED_TEL_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.ALLOWED_TEL_CHARS", new ReifiedArrayList<Character>(ArrayUtils.boxChars(StringUtils.concat(StringUtils.charRange(48, 58),
          new char[] {CONSTANT.OPEN_PARENTHESIS_CHAR, CONSTANT.CLOSE_PARENTHESIS_CHAR, CONSTANT.PLUS_SIGN_CHAR, CONSTANT.HYPHEN_CHAR,
            CONSTANT.WHITESPACE_CHAR, CONSTANT.X.charAt(0), CONSTANT.x.charAt(0)})), Character.class));
  /**
   * Allowed IPv4 characters
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_ALLOWED_IPV4_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.ALLOWED_IPV4_CHARS", new ReifiedArrayList<Character>(ArrayUtils.boxChars(StringUtils.concat(
          StringUtils.charRange(48, 58), new char[] {CONSTANT.DOT_CHAR})), Character.class));
  /**
   * Allowed IPv6 characters
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_ALLOWED_IPV6_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.ALLOWED_IPV6_CHARS", new ReifiedArrayList<Character>(ArrayUtils.boxChars(StringUtils.concat(
          StringUtils.charRange(48, 58), StringUtils.charRange(65, 71), StringUtils.charRange(97, 103), new char[] {CONSTANT.COLON_CHAR})),
          Character.class));
  /**
   * Allowed MAC characters
   */
  public static final KeyValuePair<String, ReifiedIterable<Character>> VALIDATION_ALLOWED_MAC_CHARS = new KeyValuePair<String, ReifiedIterable<Character>>(
      "Validation.ALLOWED_MAC_CHARS", new ReifiedArrayList<Character>(ArrayUtils.boxChars(StringUtils.concat(StringUtils.charRange(48, 58),
          StringUtils.charRange(65, 71), StringUtils.charRange(97, 103))), Character.class));
  /**
   * Default minimum credit card length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MIN_CREDITCARD_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MIN_CREDITCARD_LENGTH", 13);
  /**
   * Default maximum credit card length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MAX_CREDITCARD_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MAX_CREDITCARD_LENGTH", 16);
  /**
   * Default minimum sub-domain length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MIN_SUBDOMAIN_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MIN_SUBDOMAIN_LENGTH", 1);
  /**
   * Default maximum sub-domain length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MAX_SUBDOMAIN_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MAX_SUBDOMAIN_LENGTH", 63);
  /**
   * Default minimum sub-domain length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MIN_DOMAIN_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MIN_DOMAIN_LENGTH", VALIDATION_DEFAULT_MIN_SUBDOMAIN_LENGTH.getValue());
  /**
   * Default maximum sub-domain length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MAX_DOMAIN_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MAX_DOMAIN_LENGTH", 255);
  /**
   * Default minimum email address length e.g. a@b.co
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MIN_EMAIL_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MIN_EMAIL_LENGTH", 6);
  /**
   * Default maximum email address length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MAX_EMAIL_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MAX_EMAIL_LENGTH", 255);
  /**
   * Default minimum email address local part length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MIN_LOCALPART_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MIN_LOCALPART_LENGTH", 1);
  /**
   * Default maximum email address local part length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MAX_LOCALPART_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MAX_LOCALPART_LENGTH", 63);
  /**
   * Default minimum filename length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MIN_FILENAME_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MIN_FILENAME_LENGTH", 1);
  /**
   * Default maximum filename length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MAX_FILENAME_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MAX_FILENAME_LENGTH", 255);
  /**
   * Default minimum path length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MIN_PATH_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MIN_PATH_LENGTH", 1);
  /**
   * Default maximum path length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MAX_PATH_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MAX_PATH_LENGTH", 255);
  /**
   * Default minimum telephone number length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MIN_TEL_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MIN_TEL_LENGTH", 1);
  /**
   * Default maximum telephone number length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MAX_TEL_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MAX_TEL_LENGTH", 48);
  /**
   * Default minimum Windows username length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MIN_WIN_USERNAME_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MIN_WIN_USERNAME_LENGTH", 1);
  /**
   * Default maximum Windows username length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MAX_WIN_USERNAME_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MAX_WIN_USERNAME_LENGTH", 20);
  /**
   * Default minimum IPv4 length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MIN_IPV4_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MIN_IPV4_LENGTH", 7); // 0.0.0.0
  /**
   * Default maximum IPv4 length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MAX_IPV4_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MAX_IPV4_LENGTH", 15); // 255.255.255.255
  /**
   * Default minimum IPv6 length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MIN_IPV6_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MIN_IPV6_LENGTH", 2); // ::
  /**
   * Default maximum IPv6 length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MAX_IPV6_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MAX_IPV6_LENGTH", 39); // 0000:0000:0000:0000:0000:0000:0000:0000
  /**
   * Default maximum MAC length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MIN_MAC_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MIN_MAC_LENGTH", 12); // 0010db6be894
  /**
   * Default maximum MAC length
   */
  public static final KeyValuePair<String, Integer> VALIDATION_DEFAULT_MAX_MAC_LENGTH = new KeyValuePair<String, Integer>(
      "Validation.DEFAULT_MAX_MAC_LENGTH", 12);
}
