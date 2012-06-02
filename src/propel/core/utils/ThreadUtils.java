package propel.core.utils;

import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.val;
import propel.core.tracing.Stopwatch;

/**
 * Common utilities around threading
 */
public final class ThreadUtils
{

  /**
   * Puts the thread to the sleep state, for the specified time interval, unless an interrupt makes it runnable again
   * 
   * @throws InterruptedException Another thread interrupted this thread
   * @throws IllegalStateException An un-recognised time unit was given
   */
  @SneakyThrows
  public static void sleep(final int value, final TimeUnit unit)
  {
    if (value >= 0)
    {
      switch(unit)
      {
        case DAYS:
          Thread.sleep(value * 1000 * 3600 * 24);
          break;
        case HOURS:
          Thread.sleep(value * 1000 * 3600);
          break;
        case MINUTES:
          Thread.sleep(value * 1000 * 60);
          break;
        case SECONDS:
          Thread.sleep(value * 1000);
          break;
        case MILLISECONDS:
          Thread.sleep(value);
          break;
        case MICROSECONDS:
          TimeUnit.MICROSECONDS.sleep(value);
          break;
        case NANOSECONDS:
          TimeUnit.NANOSECONDS.sleep(value);
          break;
        default:
          throw new IllegalStateException("Unrecognised time unit: " + unit);
      }
    }
  }

  /**
   * Busy-spins the thread for the specified time interval
   * 
   * @throws InterruptedException Another thread interrupted this thread
   * @throws IllegalStateException An un-recognised time unit was given
   */
  public static void busySpin(final int value, final TimeUnit unit)
  {
    if (value >= 0)
    {
      val sw = Stopwatch.startNew();
      switch(unit)
      {
        case DAYS:
          while (sw.getElapsedSeconds() < value * 3600 * 24);
          break;
        case HOURS:
          while (sw.getElapsedSeconds() < value * 3600);
          break;
        case MINUTES:
          while (sw.getElapsedSeconds() < value * 60);
          break;
        case SECONDS:
          while (sw.getElapsedSeconds() < value);
          break;
        case MILLISECONDS:
          while (sw.getElapsedMillis() < value);
          break;
        case MICROSECONDS:
          while (sw.getElapsedMicros() < value);
          break;
        case NANOSECONDS:
          while (sw.getElapsedNanos() < value);
          break;
        default:
          throw new IllegalStateException("Unrecognised time unit: " + unit);
      }
    }
  }

  /**
   * Performs a yielding wait for the specified time interval
   * 
   * @throws InterruptedException Another thread interrupted this thread
   * @throws IllegalStateException An un-recognised time unit was given
   */
  public static void yieldWait(final int value, final TimeUnit unit)
  {
    if (value >= 0)
    {
      val sw = Stopwatch.startNew();
      switch(unit)
      {
        case DAYS:
          while (sw.getElapsedSeconds() < value * 3600 * 24)
            Thread.yield();
          break;
        case HOURS:
          while (sw.getElapsedSeconds() < value * 3600)
            Thread.yield();
          break;
        case MINUTES:
          while (sw.getElapsedSeconds() < value * 60)
            Thread.yield();
          break;
        case SECONDS:
          while (sw.getElapsedSeconds() < value)
            Thread.yield();
          break;
        case MILLISECONDS:
          while (sw.getElapsedMillis() < value)
            Thread.yield();
          break;
        case MICROSECONDS:
          while (sw.getElapsedMicros() < value)
            Thread.yield();
          break;
        case NANOSECONDS:
          while (sw.getElapsedNanos() < value)
            Thread.yield();
          break;
        default:
          throw new IllegalStateException("Unrecognised time unit: " + unit);
      }
    }
  }

  private ThreadUtils()
  {
  }
}
