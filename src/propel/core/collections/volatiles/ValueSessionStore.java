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
package propel.core.collections.volatiles;

import org.joda.time.LocalDateTime;
import propel.core.collections.IValueStore;
import propel.core.collections.lists.ReifiedList;
import propel.core.collections.maps.avl.AvlHashtable;
import propel.core.common.CONSTANT;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A type-aware thread-safe Session object that allows multiple threads to operate on a collection of time-expiring objects. This collection
 * does not allow null keys to be inserted.
 * 
 * Instantiate using e.g.: new ValueSessionStore&lt;String&gt;(1000*60, 5000){}; -OR- new ValueSessionStore&lt;String&gt;(1000*60, 5000,
 * String.class);
 */
public class ValueSessionStore<T extends Comparable<T>>
    implements ISessionStore<T>, IValueStore<T>
{
  /**
   * The default expiration time for session objects is 30 minutes.
   */
  public static final int DEFAULT_EXPIRATION_MILLIS = 30 * 60 * 1000;
  /**
   * The default polling interval to determine expired objects is 1 minute.
   */
  public static final int DEFAULT_POLLING_MILLIS = 60 * 1000;
  /**
   * Timer used for removing expired objects
   */
  private Timer expirationTimer;
  /**
   * Objects and their expiry date are stored here
   */
  private final AvlHashtable<T, LocalDateTime> expiries;
  private final ReentrantLock reEntrantLock;
  private int expirationMillis;
  private int pollingIntervalMillis;

  /**
   * Overloaded constructor, initializes session with a session expiration time and polling interval in milliseconds. Set to
   * CONSTANT.TIMEOUT_INFINITE to disable expiration.
   * 
   * @throws IllegalArgumentException When the values provided are out of range.
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public ValueSessionStore(int expirationMillis, int pollingIntervalMillis)
  {
    setExpirationMillis(expirationMillis);
    setPollingIntervalMillis(pollingIntervalMillis);

    reEntrantLock = new ReentrantLock();
    expiries = new AvlHashtable<T, LocalDateTime>(SuperTypeToken.getClazz(this.getClass(), 0), LocalDateTime.class);

    // daemon timer
    expirationTimer = new Timer(true);

    // check if expiration is enabled
    if (expirationMillis >= 0)
      expirationTimer.scheduleAtFixedRate(new ExpirationCheckingTimerTask(), new LocalDateTime().plusMillis(expirationMillis).toDateTime()
          .toDate(), pollingIntervalMillis);
  }

  /**
   * Overloaded constructor, initializes session with a session expiration time and polling interval in milliseconds. Set to
   * CONSTANT.TIMEOUT_INFINITE to disable expiration.
   * 
   * @throws IllegalArgumentException When the values provided are out of range.
   * @throws NullPointerException When the generic type parameter is null.
   */
  public ValueSessionStore(int expirationMillis, int pollingIntervalMillis, Class<?> genericTypeParameter)
  {
    setExpirationMillis(expirationMillis);
    setPollingIntervalMillis(pollingIntervalMillis);

    reEntrantLock = new ReentrantLock();
    expiries = new AvlHashtable<T, LocalDateTime>(genericTypeParameter, LocalDateTime.class);

    // daemon timer
    expirationTimer = new Timer(true);

    // check if expiration is enabled
    if (expirationMillis >= 0)
      expirationTimer.scheduleAtFixedRate(new ExpirationCheckingTimerTask(), new LocalDateTime().plusMillis(expirationMillis).toDateTime()
          .toDate(), pollingIntervalMillis);
  }

  /**
   * Adds an item to the collection Returns true if successful. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException When the item is null.
   */
  @Override
  public boolean add(T item)
  {
    if (item == null)
      throw new NullPointerException("item");

    lock();
    try
    {
      return expiries.add(item, new LocalDateTime().plusMillis(expirationMillis));
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Clears the collection. This is an O(1) operation.
   */
  @Override
  public void clear()
  {
    lock();
    try
    {
      expiries.clear();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Returns true if an item is contained in the collection. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException When the item is null.
   */
  @Override
  public boolean contains(T item)
  {
    if (item == null)
      throw new NullPointerException("item");

    lock();
    try
    {
      return expiries.containsKey(item);
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Expires the time of a session item, i.e. removes the item from the collection. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException When the item is null.
   */
  @Override
  public boolean expire(T item)
  {
    if (item == null)
      throw new NullPointerException("item");

    lock();
    try
    {
      return expiries.replace(item, new LocalDateTime());
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Gets the duration of a session in milliseconds. If the value is CONSTANT.TIMEOUT_INFINITE then there is no expiration.
   */
  @Override
  public int getExpirationMillis()
  {
    return expirationMillis;
  }

  /**
   * Gets the polling interval in milliseconds. If the value is CONSTANT.TIMEOUT_INFINITE then there is no polling.
   */
  public int getPollingIntervalMillis()
  {
    return pollingIntervalMillis;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameter()
  {
    return expiries.getGenericTypeParameterKey();
  }

  /**
   * Returns an enumerator for a copy of the collection. This is an O(n) operation.
   */
  @Override
  public Iterator<T> iterator()
  {
    lock();
    try
    {
      return expiries.getKeys().toList().iterator();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Renews the session expiration time for an item. Returns true if the item was found and refreshed. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException When the item is null.
   */
  @Override
  public boolean refresh(T item)
  {
    if (item == null)
      throw new NullPointerException("item");

    lock();
    try
    {
      return expiries.replace(item, new LocalDateTime().plusMillis(expirationMillis));
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Removes an item from the collection. Returns true if item was found and removed. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException When the item is null.
   */
  @Override
  public boolean remove(T item)
  {
    if (item == null)
      throw new NullPointerException("item");

    lock();
    try
    {
      return expiries.remove(item);
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Sets the duration of a session in milliseconds. If the value is CONSTANT.TIMEOUT_INFINITE then there is no expiration.
   * 
   * @throws IllegalArgumentException When the period is a negative but not CONSTANT.TIMEOUT_INFINITE
   */
  protected void setExpirationMillis(int value)
  {
    if ((value != CONSTANT.TIMEOUT_INFINITE) && (value < 0))
      throw new IllegalArgumentException("The expiration period can either be CONSTANT.TIMEOUT_INFINITE, or a positive number: " + value);
    this.expirationMillis = value;
  }

  /**
   * Sets polling interval in milliseconds.
   * 
   * @throws IllegalArgumentException When the interval is a non-positive number.
   */
  protected void setPollingIntervalMillis(int value)
  {
    if (value < 0)
      throw new IllegalArgumentException("The polling interval must be a positive number: " + value);
    this.pollingIntervalMillis = value;
  }

  /**
   * Returns the collection size, volatile value. This is an O(1) operation.
   */
  @Override
  public int size()
  {
    lock();
    try
    {
      return expiries.size();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Puts all elements in an array and returns them. This is an O(nlog2(n)) operation.
   */
  @Override
  public T[] toArray()
  {
    lock();
    try
    {
      return expiries.getKeys().toArray();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Puts all elements in a list and returns them. This is an O(nlog2(n)) operation.
   */
  @Override
  public ReifiedList<T> toList()
  {
    lock();
    try
    {
      return expiries.getKeys().toList();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Locks the object
   */
  @Override
  public void lock()
  {
    reEntrantLock.lock();
  }

  /**
   * Unlocks the object.
   * 
   * @throws IllegalMonitorStateException The current thread does not own the lock
   */
  @Override
  public void unlock()
  {
    reEntrantLock.unlock();
  }

  /**
   * Scans all objects and removes the ones that have expired. This is an O(nlog2(n)) operation.
   */
  private class ExpirationCheckingTimerTask
      extends TimerTask
  {
    @Override
    public void run()
    {
      lock();
      try
      {
        // check objects expiring before now
        LocalDateTime now = new LocalDateTime();
        List<T> toBeRemoved = new ArrayList<T>(64);

        // add expiring object to a list
        for (T key : expiries.getKeys())
          if (expiries.get(key).isBefore(now))
            toBeRemoved.add(key);

        // remove from store
        for (T key : toBeRemoved)
          expiries.remove(key);
      }
      finally
      {
        unlock();
      }
    }
  }
}
