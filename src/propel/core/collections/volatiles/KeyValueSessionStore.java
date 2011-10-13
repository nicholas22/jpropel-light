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
import propel.core.collections.IKeyValueStore;
import propel.core.collections.KeyNotFoundException;
import propel.core.collections.KeyValuePair;
import propel.core.collections.maps.avl.AvlHashtable;
import propel.core.common.CONSTANT;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A type-aware thread-safe session storage object that allows multiple threads to operate on a collection of time-expiring key/value pairs.
 * This map does not allow null keys to be inserted.
 * 
 * Instantiate using e.g.: new KeyValueSessionStore&lt;String, Object&gt;(1000*60, 5000){}; -OR- new KeyValueSessionStore&lt;String,
 * Object&gt;(1000*60, 5000, String.class, Object.class);
 */
public class KeyValueSessionStore<TKey extends Comparable<TKey>, TValue>
    implements ISessionStore<TKey>, IKeyValueStore<TKey, TValue>
{
  /**
   * The default expiration time for session objects is 30 minutes.
   */
  public static final int DEFAULT_EXPIRATION_MILLIS = 30 * 60 * 1000;
  /**
   * The default polling interval to determine expired objects is 1 minute.
   */
  public static final int DEFAULT_POLLING_MILLIS = 60 * 1000;
  private final Timer expirationTimer;
  private final AvlHashtable<TKey, LocalDateTime> expiries;
  private final ReentrantLock reEntrantLock;
  private final AvlHashtable<TKey, TValue> store;
  private int expirationMillis;
  private int pollingIntervalMillis;

  /**
   * Overloaded constructor, initializes session with a session expiration time and polling interval in milliseconds. Set to
   * CONSTANT.TIMEOUT_INFINITE to disable expiration.
   * 
   * @throws IllegalArgumentException When the values provided are out of range.
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public KeyValueSessionStore(int expirationMillis, int pollingIntervalMillis)
  {
    setExpirationMillis(expirationMillis);
    setPollingIntervalMillis(pollingIntervalMillis);

    reEntrantLock = new ReentrantLock();
    store = new AvlHashtable<TKey, TValue>(SuperTypeToken.getClazz(this.getClass(), 0), SuperTypeToken.getClazz(this.getClass(), 1));
    expiries = new AvlHashtable<TKey, LocalDateTime>(store.getGenericTypeParameterKey(), LocalDateTime.class);

    // daemon timer, allows for app shutdown when no other thread exists that could use the session
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
   * @throws NullPointerException When a generic type parameter class is null.
   */
  public KeyValueSessionStore(int expirationMillis, int pollingIntervalMillis, Class<?> genericTypeParameterKey,
                              Class<?> genericTypeParameterValue)
  {
    setExpirationMillis(expirationMillis);
    setPollingIntervalMillis(pollingIntervalMillis);

    reEntrantLock = new ReentrantLock();
    store = new AvlHashtable<TKey, TValue>(genericTypeParameterKey, genericTypeParameterValue);
    expiries = new AvlHashtable<TKey, LocalDateTime>(genericTypeParameterKey, LocalDateTime.class);

    // daemon timer, allows for app shutdown when no other thread exists that could use the session
    expirationTimer = new Timer(true);

    // check if expiration is enabled
    if (expirationMillis >= 0)
      expirationTimer.scheduleAtFixedRate(new ExpirationCheckingTimerTask(), new LocalDateTime().plusMillis(expirationMillis).toDateTime()
          .toDate(), pollingIntervalMillis);
  }

  /**
   * Adds an item to the collection Returns true if successful. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException Key is null
   */
  @Override
  public boolean add(TKey key, TValue value)
  {
    if (key == null)
      throw new NullPointerException("key");

    lock();
    try
    {
      // attempt to add value
      if (store.add(key, value))
      {
        // set expiry
        expiries.add(key, new LocalDateTime().plusMillis(expirationMillis));
        return true;
      }

      return false;
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
      store.clear();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Returns true if the given key is contained in the collection. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException Key is null
   */
  @Override
  public boolean containsKey(TKey key)
  {
    if (key == null)
      throw new NullPointerException("key");

    lock();
    try
    {
      return store.containsKey(key);
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Expires the time of a session item, i.e. removes the item from the collection. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException Key is null
   */
  @Override
  public boolean expire(TKey key)
  {
    if (key == null)
      throw new NullPointerException("key");

    lock();
    try
    {
      return expiries.replace(key, new LocalDateTime());
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Returns the value associated with a key. Throws KeyNotFoundException if the key does not exist. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException Key is null
   * @throws KeyNotFoundException When the key is not found
   */
  @Override
  public TValue get(TKey key)
  {
    if (key == null)
      throw new NullPointerException("key");

    lock();
    try
    {
      return store.get(key);
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Returns all keys. Result is in ascending Key order. This is an O(1) operation.
   */
  @Override
  public Iterable<TKey> getKeys()
  {
    lock();
    try
    {
      return store.getKeys();
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Returns all values. Result is in ascending Key (not value) order. This is an O(1) operation.
   */
  @Override
  public Iterable<TValue> getValues()
  {
    lock();
    try
    {
      return store.getValues();
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
  @Override
  public int getPollingIntervalMillis()
  {
    return pollingIntervalMillis;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameterKey()
  {
    return store.getGenericTypeParameterKey();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameterValue()
  {
    return store.getGenericTypeParameterValue();
  }

  /**
   * Renews the session expiration time for an item. Returns true if the item was found and refreshed. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException Key is null
   */
  @Override
  public boolean refresh(TKey key)
  {
    if (key == null)
      throw new NullPointerException("key");

    lock();
    try
    {
      return expiries.replace(key, new LocalDateTime().plusMillis(expirationMillis));
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Removes an item from the collection. Returns true if item was found and removed. This is an O(log2(n)) operation.
   * 
   * @throws NullPointerException Key is null.
   */
  @Override
  public boolean remove(TKey key)
  {
    if (key == null)
      throw new NullPointerException("key");

    lock();
    try
    {
      if (store.remove(key))
      {
        expiries.remove(key);
        return true;
      }

      return false;
    }
    finally
    {
      unlock();
    }
  }

  /**
   * Replaces a key's value with the given new value. Returns true if the key was found and its value was replaced. This is an O(log2(n))
   * operation.
   * 
   * @throws NullPointerException Key is null
   */
  @Override
  public boolean replace(TKey key, TValue newValue)
  {
    if (key == null)
      throw new NullPointerException("key");

    lock();
    try
    {
      return store.replace(key, newValue);
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
      return store.size();
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
   * {@inheritDoc}
   */
  @Override
  public Iterator<KeyValuePair<TKey, TValue>> iterator()
  {
    lock();
    try
    {
      return new AvlHashtable<TKey, TValue>(store).iterator();
    }
    finally
    {
      unlock();
    }
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
        List<TKey> toBeRemoved = new ArrayList<TKey>(64);

        // add expiring object to a list
        for (TKey key : store.getKeys())
          if (expiries.get(key).isBefore(now))
            toBeRemoved.add(key);

        // remove from store/expiry table
        for (TKey key : toBeRemoved)
          remove(key);
      }
      finally
      {
        unlock();
      }
    }
  }
}
