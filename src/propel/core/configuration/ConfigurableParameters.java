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

import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import propel.core.TryResult;
import propel.core.collections.KeyValuePair;
import propel.core.collections.ReifiedIterable;
import propel.core.collections.maps.avl.AvlHashtable;
import propel.core.common.CONSTANT;
import propel.core.common.StackTraceLogger;
import propel.core.userTypes.UnsignedByte;
import propel.core.userTypes.UnsignedInteger;
import propel.core.userTypes.UnsignedLong;
import propel.core.userTypes.UnsignedShort;
import propel.core.utils.ConversionUtils;
import propel.core.utils.ReflectionUtils;
import propel.core.utils.StringComparison;
import propel.core.utils.StringUtils;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Allows for the application configuration file to be probed for the presence of key/value pairs. It is used for setting constant values in
 * a way that can be configurable from the config file.
 */
@SuppressWarnings("unchecked")
public final class ConfigurableParameters
{
  // file of similar functionality to app.config in .NET
  private static final String FILENAME = ConfigurableParamsInput.FILENAME;
  // multi-element separator header
  private static final String SEP_PREFIX = "(sep=";
  // type casting header
  private static final String CAST_PREFIX = "(cast=";
  private static AvlHashtable<String, String> lookup;

  private ConfigurableParameters()
  {
  }

  // static constructor
  static
  {
    lookup = new AvlHashtable<String, String>(String.class, String.class);

    try
    {
      lookup = (AvlHashtable<String, String>) ConfigurationManager.getSection(FILENAME);
    }
    catch(FileNotFoundException e)
    {
      // there is no configurable parameters file found, hence proceeding with default values
    }
    catch(Throwable e)
    {
      logError("There was an error while parsing configurable parameters: " + new StackTraceLogger(e));
    }
  }

  /**
   * Gets the value of an Int64 key if it exists, otherwise returns the specified default value.
   */
  public static Long getInt64(KeyValuePair<String, Long> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<Long> result = StringUtils.tryParseInt64(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of a UInt64 key if it exists, otherwise returns the specified default value.
   */
  public static UnsignedLong getUInt64(KeyValuePair<String, UnsignedLong> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<UnsignedLong> result = StringUtils.tryParseUInt64(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of an Int32 key if it exists, otherwise returns the specified default value.
   */
  public static int getInt32(KeyValuePair<String, Integer> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<Integer> result = StringUtils.tryParseInt32(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of a UInt32 key if it exists, otherwise returns the specified default value.
   */
  public static UnsignedInteger getUInt32(KeyValuePair<String, UnsignedInteger> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<UnsignedInteger> result = StringUtils.tryParseUInt32(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of an Int16 key if it exists, otherwise returns the specified default value.
   */
  public static Short getInt16(KeyValuePair<String, Short> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<Short> result = StringUtils.tryParseInt16(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of a UInt16 key if it exists, otherwise returns the specified default value.
   */
  public static UnsignedShort getUInt16(KeyValuePair<String, UnsignedShort> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<UnsignedShort> result = StringUtils.tryParseUInt16(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of an UnsignedByte key if it exists, otherwise returns the specified default value.
   */
  public static UnsignedByte getUInt8(KeyValuePair<String, UnsignedByte> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<UnsignedByte> result = StringUtils.tryParseUInt8(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of an SByte key if it exists, otherwise returns the specified default value.
   */
  public static Byte getInt8(KeyValuePair<String, Byte> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<Byte> result = StringUtils.tryParseInt8(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of an Boolean key if it exists, otherwise returns the specified default value.
   */
  public static Boolean getBool(KeyValuePair<String, Boolean> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<Boolean> result = StringUtils.tryParseBool(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of a Char key if it exists, otherwise returns the specified default value.
   */
  public static Character getChar(KeyValuePair<String, Character> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<Character> result = StringUtils.tryParseChar(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of a DateTime key if it exists, otherwise returns the specified default value.
   */
  public static LocalDateTime getDateTime(KeyValuePair<String, LocalDateTime> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<LocalDateTime> result = StringUtils.tryParseDateTime(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of a Decimal key if it exists, otherwise returns the specified default value.
   */
  public static BigDecimal getDecimal(KeyValuePair<String, BigDecimal> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<BigDecimal> result = StringUtils.tryParseDecimal(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of a Double key if it exists, otherwise returns the specified default value.
   */
  public static Double getDouble(KeyValuePair<String, Double> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<Double> result = StringUtils.tryParseDouble(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of a Float key if it exists, otherwise returns the specified default value.
   */
  public static Float getFloat(KeyValuePair<String, Float> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<Float> result = StringUtils.tryParseFloat(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of a UUID key if it exists, otherwise returns the specified default value.
   */
  public static UUID getUuid(KeyValuePair<String, UUID> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<UUID> result = StringUtils.tryParseUuid(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of a IPAddress key if it exists, otherwise returns the specified default value.
   */
  public static InetAddress getIPAddress(KeyValuePair<String, InetAddress> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<InetAddress> result = StringUtils.tryParseIpAddress(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the value of a String key if it exists, otherwise returns the specified default value.
   */
  public static String getString(KeyValuePair<String, String> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
      return lookup.get(kvp.getKey());

    return kvp.getValue();
  }

  /**
   * Gets the value of a TimeSpan key if it exists, otherwise returns the specified default value.
   */
  public static Duration getTimeSpan(KeyValuePair<String, Duration> kvp)
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String key = kvp.getKey();
      TryResult<Duration> result = StringUtils.tryParseTimeSpan(lookup.get(key));
      if (result.isSuccess())
      {
        logChangedKvp(key, result);
        return result.getResult();
      }
    }

    return kvp.getValue();
  }

  /**
   * Gets the type name of an object from the configured parameter and instantiates it. If no arguments are passed, it uses the default
   * constructor, otherwise an overloaded version.
   * 
   * @throws ConfigurationErrorsException Failed to instantiate the default (provided) object
   */
  public static Object getObject(KeyValuePair<String, String> kvp, Object[] constructorArgs)
      throws ConfigurationErrorsException
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      try
      {
        String key = kvp.getKey();
        TryResult<Object> result = new TryResult<Object>(ReflectionUtils.activate(lookup.get(key), constructorArgs));
        logChangedKvp(kvp.getKey(), result);
        return result;
      }
      catch(Throwable e)
      {
        // warn user of failed attempt
        logError("Failed to instantiate configured object for " + kvp.getKey() + ": " + new StackTraceLogger(e));
      }
    }

    // upon failure, use default value
    try
    {
      Object result = ReflectionUtils.activate(kvp.getValue(), constructorArgs);
      return result;
    }
    catch(Throwable e)
    {
      // warn user of failed attempt
      throw new ConfigurationErrorsException("Failed to instantiate DEFAULT object for " + kvp.getKey() + " value: " + kvp.getValue(), e);
    }
  }

  /**
   * Gets the type name from the configured parameters and attempts to parse it, returning a Type.
   * 
   * @throws ConfigurationErrorsException Failed to parse the default (provided) class type
   */
  public static Class<?> getType(KeyValuePair<String, String> kvp)
      throws ConfigurationErrorsException
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      try
      {
        Class<?> result = ReflectionUtils.parse(lookup.get(kvp.getKey()));
        logChangedKvp(kvp.getKey(), result.getName());
        return result;
      }
      catch(Throwable e)
      {
        // warn user of failed attempt
        logError("Failed to parse new Type for " + kvp.getKey() + ": " + new StackTraceLogger(e));
      }
    }

    // upon failure, use default value
    try
    {
      Class<?> result = ReflectionUtils.parse(kvp.getValue());
      return result;
    }
    catch(Throwable e)
    {
      // warn user of failed attempt
      throw new ConfigurationErrorsException("Failed to parse DEFAULT Type for " + kvp.getKey() + " with value: " + kvp.getValue(), e);
    }
  }

  // Multi-element

  /**
   * Gets the value of an Iterable if it exists, otherwise returns the specified default value. If an error occurs, this method will log the
   * problem and return an empty collection.
   * 
   * Examples:
   * <ul>
   * <li>(sep= )[1 2 3]</li>
   * <li>(sep=,)[10/12/2010,15/11/2011,18/01/2012]</li>
   * <li>(sep=,)(cast=java.lang.Integer,java.lang.Character)[1,2,127,9,8]</li>
   * 
   * @throws ConfigurationErrorsException There was a problem in successfully parsing or re-configuring an Iterable value
   */
  public static <T> Iterable<T> tryGetIterable(KeyValuePair<String, ReifiedIterable<T>> kvp)
  {
    try
    {
      return getIterable(kvp);
    }
    catch(ConfigurationErrorsException e)
    {
      logError("Could not retrieve " + kvp.getKey() + ": " + new StackTraceLogger(e));
      return new ArrayList<T>();
    }
  }

  /**
   * Gets the value of an Iterable if it exists, otherwise returns the specified default value.
   * 
   * Examples:
   * <ul>
   * <li>(sep= )[1 2 3]</li>
   * <li>(sep=,)[10/12/2010,15/11/2011,18/01/2012]</li>
   * <li>(sep=,)(cast=java.lang.Integer,java.lang.Character)[1,2,127,9,8]</li>
   * 
   * @throws ConfigurationErrorsException There was a problem in successfully parsing or re-configuring an Iterable value
   */
  public static <T> Iterable<T> getIterable(KeyValuePair<String, ReifiedIterable<T>> kvp)
      throws ConfigurationErrorsException
  {
    if (StringUtils.contains(lookup.getKeys(), kvp.getKey(), StringComparison.Ordinal))
    {
      String value = lookup.get(kvp.getKey());
      if (value != null && value.length() > SEP_PREFIX.length() + 2
          && StringUtils.startsWith(value, SEP_PREFIX, StringComparison.OrdinalIgnoreCase)) // e.g. (sep=,)
      {
        // get separator directive's char
        char separator = value.charAt(SEP_PREFIX.length());
        value = StringUtils.delete(value, 0, SEP_PREFIX.length() + 2);

        // check for recast directive
        String fromTypeName = null;
        String toTypeName = null;
        if (StringUtils.startsWith(value, CAST_PREFIX) && value.contains(CONSTANT.COMMA)
            && StringUtils.substring(value, value.indexOf(CONSTANT.COMMA)).contains(CONSTANT.CLOSE_PARENTHESIS))
        {
          fromTypeName = StringUtils.copy(value, CAST_PREFIX.length(), value.indexOf(CONSTANT.COMMA_CHAR));
          toTypeName = StringUtils.copy(value, value.indexOf(CONSTANT.COMMA_CHAR) + 1, value.indexOf(CONSTANT.CLOSE_PARENTHESIS));
          value = StringUtils.delete(value, 0, value.indexOf(CONSTANT.CLOSE_PARENTHESIS) + 1);
        }

        value = StringUtils.trimEnd(StringUtils.trimStart(value, CONSTANT.BRACKET_OPEN_CHAR), CONSTANT.BRACKET_CLOSE_CHAR);
        String[] values = StringUtils.split(value, separator);

        logChangedKvp(kvp.getKey(), value);

        List<T> result = new ArrayList<T>();

        Class<?> requestedType = kvp.getValue().getGenericTypeParameter();

        // check if recast requested
        if (fromTypeName == null || toTypeName == null)
        {
          // no further re-casting required, just convert from string to requested T type
          String val = null;
          try
          {
            for (int i = 0; i < values.length; i++)
            {
              val = values[i];
              result.add((T) ConversionUtils.changeType(val, requestedType));
            }
          }
          catch(Throwable e)
          {
            throw new ConfigurationErrorsException("The '" + kvp.getKey()
                + "' configurable enumerable values contained an invalid element: " + val, e);
          }
        } else
        {
          // get from/to casts
          Class<?> fromType = null;
          Class<?> toType = null;
          try
          {
            fromType = ReflectionUtils.parse(fromTypeName);
          }
          catch(Throwable e)
          {
            throw new ConfigurationErrorsException("The '" + kvp.getKey() + "' defined an invalid source cast type: " + fromType, e);
          }
          try
          {
            toType = ReflectionUtils.parse(toTypeName);
          }
          catch(Throwable e)
          {
            throw new ConfigurationErrorsException("The '" + kvp.getKey() + "' defined an invalid destination cast type: " + toType, e);
          }

          // check that method caller and configured destination are referring to the same type
          // if(toType != typeof(T))
          // throw new ConfigurationErrorsException("The '" + kvp.Key + "' configured enumerable cast (" + toType.Name + ") must match T ("
          // + typeof(T).Name + ") for casting to take place.");

          for (String val : values)
          {
            try
            {
              // first cast from string to fromType
              Object castVal = ConversionUtils.changeType(val, fromType);
              // then from fromType to toType
              result.add((T) ConversionUtils.changeType(castVal, toType));
            }
            catch(Throwable e)
            {
              throw new ConfigurationErrorsException("The '" + kvp.getKey()
                  + "' configurable enumerable values contained an element that could not be re-cast: " + val, e);
            }
          }
        }

        return result;
      } else
        throw new ConfigurationErrorsException("The format of '" + kvp.getKey()
            + "' configurable enumerable value should look like this: (sep= )(cast=System.Int32,System.Char)[1 2 3 4]");
    }

    return kvp.getValue();
  }

  /**
   * Logs a change
   */
  private static void logChangedKvp(String key, Object value)
  {
    // LLTODO:
    // PropelLog.debug("Configuration parameter '" + key + "' changed: " + value);
  }

  private static void logError(String msg)
  {
    // LLTODO:
    // PropelLog.error(msg);
    throw new RuntimeException(msg);
  }
}
