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
package propel.core.tracing;

/**
 * Stop watch class for measuring time taken to execute some code. This is not a thread-safe class.
 */
public class Stopwatch
{

  private long totalNanos;
  private long currentNanos;
  private boolean isRunning;

  /**
   * Default constructor
   */
  public Stopwatch()
  {
  }

  /**
   * Static instance creation method, returns a new started stopwatch
   */
  public static Stopwatch startNew()
  {
    Stopwatch sw = new Stopwatch();
    sw.start();
    return sw;
  }

  /**
   * Starts the stop watch.
   * 
   * @throws IllegalStateException If the stop watch is started already
   */
  public void start()
  {
    if (isRunning)
      throw new IllegalStateException("The current state of the stop watch is started");

    isRunning = true;
    currentNanos = System.nanoTime();
  }

  /**
   * Stops the stop watch.
   * 
   * @throws IllegalStateException If the stop watch is not stopped
   */
  public void stop()
  {
    if (!isRunning)
      throw new IllegalStateException("The current state of the stop watch is stopped");

    isRunning = false;
    totalNanos += System.nanoTime() - currentNanos;
  }

  /**
   * Resets the total nanosecond count. Also stops the stop watch if it is running
   */
  public void reset()
  {
    if (isRunning)
      stop();

    totalNanos = 0;
  }

  /**
   * Returns the total elapsed nanosecond count.
   */
  public long getElapsedNanos()
  {
    if (isRunning)
      return totalNanos + System.nanoTime() - currentNanos;

    return totalNanos;
  }

  /**
   * Returns the approximate total elapsed microsecond count.
   */
  public long getElapsedMicros()
  {
    return getElapsedNanos() / 1000;
  }

  /**
   * Returns the approximate total elapsed millisecond count.
   */
  public long getElapsedMillis()
  {
    return getElapsedNanos() / 1000000;
  }

  /**
   * Returns the approximate total elapsed second count.
   */
  public long getElapsedSeconds()
  {
    return getElapsedNanos() / 1000000000;
  }
}
