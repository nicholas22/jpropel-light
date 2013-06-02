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

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import lombok.Validate;
import lombok.Validate.NotNull;
import lombok.val;
import propel.core.configuration.ConfigurableConsts;
import propel.core.configuration.ConfigurableParameters;
import propel.core.utils.ConversionUtils;
import propel.core.utils.ExceptionUtils;
import propel.core.utils.StringComparison;
import propel.core.utils.StringUtils;

/**
 * Wraps around a Throwable and can retrieve its stacktrace, use the toString() method for this.
 */
public class StackTraceLogger
{
  protected String stackTrace;
  protected int maxStackFrames = ConfigurableParameters.getInt32(ConfigurableConsts.STACKTRACE_LOGGER_MAX_STACKFRAMES);

  /**
   * Constructor, initialises with a Throwable
   */
  public StackTraceLogger(Throwable e)
  {
    this(e, StackTraceLevel.FULL);
  }

  /**
   * Constructor, initialises with a Throwable
   * 
   * @throws NullPointerException An argument is null
   * @throws IllegalArgumentException An unrecognised stack trace level was given
   */
  @Validate
  public StackTraceLogger(@NotNull final Throwable e, @NotNull final StackTraceLevel level)
  {
    val baos = new ByteArrayOutputStream();
    val pw = new PrintWriter(baos);

    switch(level)
    {
      case FULL:

        // full stack trace is used
        e.printStackTrace(pw);
        pw.flush();
        stackTrace = ConversionUtils.toString(baos.toByteArray(), CONSTANT.UTF8);
        break;

      case ABBREVIATED:

        try
        {
          // only first few stack frames are shown, in a non-verbose format
          val sb = new StringBuilder();
          Throwable current = e;
          while (current != null)
          {
            String lastClassFile = CONSTANT.EMPTY_STRING;
            // limit stack frames being shown
            if (current.getStackTrace() != null)
            {
              for (int i = 0; (i < current.getStackTrace().length) && (i < maxStackFrames); i++)
              {
                val frame = current.getStackTrace()[i];
                if (frame != null && frame.getClassName() != null)
                {
                  val classFile = getSimpleClassName(frame.getClassName());
                  if (!classFile.equals(lastClassFile))
                  {
                    // don't append this for first frame
                    sb.append(i == 0 ? "In " : "; ");

                    // store for comparison, to group line numbers
                    lastClassFile = classFile;

                    sb.append(classFile);
                    sb.append(" ");
                  } else
                    // same file, just append line number
                    sb.append(",");

                  sb.append(frame.getLineNumber());
                }
              }
            }

            sb.append(" ");

            // format exception type/message
            String type = current.getClass().getSimpleName();
            type = StringUtils.replace(type, "Exception", "Ex", StringComparison.Ordinal);
            type = StringUtils.replace(type, "Throwable", "Th", StringComparison.Ordinal);
            sb.append(type);
            sb.append("-> ");
            sb.append(current.getMessage());
            sb.append(CONSTANT.ENVIRONMENT_NEWLINE);

            // next, process the cause
            current = current.getCause();
          }

          stackTrace = sb.toString();
        }
        catch(Throwable abbreviatedProcessingException)
        {
          // better to have everything than not enough
          stackTrace = new StackTraceLogger(e, StackTraceLevel.FULL).toString();
        }
        break;
      case MINIMAL:

        // types & messages only used
        stackTrace = ExceptionUtils.getTypesAndMessages(e, CONSTANT.ENVIRONMENT_NEWLINE);
        break;
      default:
        throw new IllegalArgumentException("Unrecognised stack trace level: " + level);
    }
  }

  private String getSimpleClassName(String fullClassName)
  {
    int lastIndex = fullClassName.lastIndexOf(CONSTANT.DOT_CHAR);
    if (lastIndex < 0 || lastIndex >= fullClassName.length())
      return fullClassName;

    return fullClassName.substring(lastIndex + 1);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return StringUtils.trimEnd(stackTrace, CONSTANT.CRLF_CHARS);
  }

}
