// Ported to Java by Nikolaos Tountas
//
// GOLETAS COMMUNITY SOURCE CODE LICENSE AGREEMENT
// Version: June 11, 2006
// Copyright © 2005 - 2008 Maksim Goleta. All Rights Reserved.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of this source code and associated documentation files (the
// "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish,
// distribute, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
// following conditions:
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software or
// documentation (and/or other materials) for the binary forms, if any, produced from the Software or derived source code.
// Products derived from the Software may not be called Goletas, nor may Goletas appear in their name without the prior written permission
// of Goletas. This license does not grant you any rights to use Goletas’ logos or trademarks. If you alter the Software in any way, you
// must remove the modified source code from the Goletas code namespace, if applicable, and cause the modified files to carry prominent
// notices stating that you changed the files.
// If you begin patent litigation against Goletas over patents that you think may apply to the Software (including a cross-claim or
// counterclaim in a lawsuit), your license to the Software is terminated automatically.
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS. IN NO EVENT SHALL GOLETAS, THE CONTRIBUTORS
// OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
// Except as contained in this notice, neither the name Goletas nor the names of contributors or copyright holders may be used in
// advertising or otherwise to promote the sale, use or other dealings in the Software or products derived from the Software without the
// specific prior written authorization.

package propel.core.collections.maps.avl;

import propel.core.TryResult;
import propel.core.collections.KeyNotFoundException;
import propel.core.collections.KeyValuePair;
import propel.core.collections.maps.IHashtable;
import propel.core.collections.maps.ReifiedMap;
import propel.core.utils.Linq;
import propel.core.utils.SuperTypeToken;
import propel.core.utils.SuperTypeTokenException;
import java.util.Iterator;
import java.util.Map;

/**
 * A type-aware AVL-tree-backed hashtable. This map does not allow null keys to be inserted.
 */
public class AvlHashtable<TKey extends Comparable<TKey>, TValue>
    implements IHashtable<TKey, TValue>
{
  /**
   * The number of elements contained
   */
  private int size;
  final KeyCollection<TKey, TValue> keys;
  final ValueCollection<TKey, TValue> values;
  AvlNode<TKey, TValue> root;

  /**
   * Default constructor
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   */
  public AvlHashtable()
  {
    keys = new KeyCollection<TKey, TValue>(this, SuperTypeToken.getClazz(this.getClass(), 0));
    values = new ValueCollection<TKey, TValue>(this, SuperTypeToken.getClazz(this.getClass(), 1));
  }

  /**
   * Constructor for initializing with the key/value generic type parameters
   * 
   * @throws NullPointerException When a generic type parameter is null.
   */
  public AvlHashtable(Class<?> genericTypeParameterKey, Class<?> genericTypeParameterValue)
  {
    if (genericTypeParameterKey == null)
      throw new NullPointerException("genericTypeParameterKey");
    if (genericTypeParameterValue == null)
      throw new NullPointerException("genericTypeParameterValue");

    keys = new KeyCollection<TKey, TValue>(this, genericTypeParameterKey);
    values = new ValueCollection<TKey, TValue>(this, genericTypeParameterValue);
  }

  /**
   * Constructor initializes with another reified map
   * 
   * @throws NullPointerException When the argument is null, or a key in the map provided is null.
   */
  public AvlHashtable(ReifiedMap<TKey, TValue> map)
  {
    if (map == null)
      throw new NullPointerException("map");

    keys = new KeyCollection<TKey, TValue>(this, map.getGenericTypeParameterKey());
    values = new ValueCollection<TKey, TValue>(this, map.getGenericTypeParameterValue());

    for (KeyValuePair<TKey, TValue> entry : map)
      add(entry.getKey(), entry.getValue());
  }

  /**
   * Constructor initializes from another map
   * 
   * @throws SuperTypeTokenException When called without using anonymous class semantics.
   * @throws NullPointerException When the argument is null.
   */
  public AvlHashtable(Map<? extends TKey, ? extends TValue> map)
  {
    if (map == null)
      throw new NullPointerException("map");

    keys = new KeyCollection<TKey, TValue>(this, SuperTypeToken.getClazz(this.getClass(), 0));
    values = new ValueCollection<TKey, TValue>(this, SuperTypeToken.getClazz(this.getClass(), 1));

    for (Map.Entry<? extends TKey, ? extends TValue> entry : map.entrySet())
      add(entry.getKey(), entry.getValue());
  }

  /**
   * Constructor initializes from another map and the list-map's key/value generic type parameters
   * 
   * @throws NullPointerException When an argument is null.
   */
  public AvlHashtable(Map<? extends TKey, ? extends TValue> map, Class<?> genericTypeParameterKey, Class<?> genericTypeParameterValue)
  {
    if (map == null)
      throw new NullPointerException("map");
    if (genericTypeParameterKey == null)
      throw new NullPointerException("genericTypeParameterKey");
    if (genericTypeParameterValue == null)
      throw new NullPointerException("genericTypeParameterValue");

    keys = new KeyCollection<TKey, TValue>(this, genericTypeParameterKey);
    values = new ValueCollection<TKey, TValue>(this, genericTypeParameterValue);

    for (Map.Entry<? extends TKey, ? extends TValue> entry : map.entrySet())
      add(entry.getKey(), entry.getValue());
  }

  /**
   * Adds an element represented by the provided key/value pair if the key is not already present. This is an O(log2(n)) operation.
   * 
   * @param kvp The key/value pair to add.
   * 
   * @throws NullPointerException If the key value pair or the key is null.
   */
  @Override
  public void add(KeyValuePair<? extends TKey, ? extends TValue> kvp)
  {
    if (kvp == null)
      throw new NullPointerException("kvp");

    add(kvp.getKey(), kvp.getValue());
  }

  /**
   * Adds the provided key/value pair if the key is not already present. This is an O(log2(n)) operation.
   * 
   * @param key The key to add.
   * @param value The value to add.
   * 
   * @return True if the key did not exist, therefore the value was added.
   * 
   * @throws NullPointerException If the key is null.
   */
  @Override
  public boolean add(TKey key, TValue value)
  {
    if (key == null)
      throw new NullPointerException("key");

    AvlNode<TKey, TValue> p = root;

    if (p == null)
      root = new AvlNode<TKey, TValue>(new KeyValuePair<TKey, TValue>(key, value));
    else
    {
      while (true)
      {
        int c = key.compareTo(p.item.getKey());

        if (c < 0)
        {
          if (p.left != null)
            p = p.left;
          else
          {
            p.left = new AvlNode<TKey, TValue>(new KeyValuePair<TKey, TValue>(key, value), p);
            p.balance--;

            break;
          }
        } else if (c > 0)
        {
          if (p.right != null)
            p = p.right;
          else
          {
            p.right = new AvlNode<TKey, TValue>(new KeyValuePair<TKey, TValue>(key, value), p);
            p.balance++;

            break;
          }
        } else
          return false;
      }

      while ((p.balance != 0) && (p.parent != null))
      {
        if (p.parent.left == p)
          p.parent.balance--;
        else
          p.parent.balance++;

        p = p.parent;

        if (p.balance == -2)
        {
          AvlNode<TKey, TValue> x = p.left;

          if (x.balance == -1)
          {
            x.parent = p.parent;

            if (p.parent == null)
              root = x;
            else
            {
              if (p.parent.left == p)
                p.parent.left = x;
              else
                p.parent.right = x;
            }

            p.left = x.right;

            if (p.left != null)
              p.left.parent = p;

            x.right = p;
            p.parent = x;

            x.balance = 0;
            p.balance = 0;
          } else
          {
            AvlNode<TKey, TValue> w = x.right;

            w.parent = p.parent;

            if (p.parent == null)
              root = w;
            else
            {
              if (p.parent.left == p)
                p.parent.left = w;
              else
                p.parent.right = w;
            }

            x.right = w.left;

            if (x.right != null)
              x.right.parent = x;

            p.left = w.right;

            if (p.left != null)
              p.left.parent = p;

            w.left = x;
            w.right = p;

            x.parent = w;
            p.parent = w;

            if (w.balance == -1)
            {
              x.balance = 0;
              p.balance = 1;
            } else if (w.balance == 0)
            {
              x.balance = 0;
              p.balance = 0;
            } else
            // w.Balance == 1
            {
              x.balance = -1;
              p.balance = 0;
            }

            w.balance = 0;
          }

          break;
        } else if (p.balance == 2)
        {
          AvlNode<TKey, TValue> x = p.right;

          if (x.balance == 1)
          {
            x.parent = p.parent;

            if (p.parent == null)
              root = x;
            else
            {
              if (p.parent.left == p)
                p.parent.left = x;
              else
                p.parent.right = x;
            }

            p.right = x.left;

            if (p.right != null)
              p.right.parent = p;

            x.left = p;
            p.parent = x;

            x.balance = 0;
            p.balance = 0;
          } else
          {
            AvlNode<TKey, TValue> w = x.left;

            w.parent = p.parent;

            if (p.parent == null)
              root = w;
            else
            {
              if (p.parent.left == p)
                p.parent.left = w;
              else
                p.parent.right = w;
            }

            x.left = w.right;

            if (x.left != null)
              x.left.parent = x;

            p.right = w.left;

            if (p.right != null)
              p.right.parent = p;

            w.right = x;
            w.left = p;

            x.parent = w;
            p.parent = w;

            if (w.balance == 1)
            {
              x.balance = 0;
              p.balance = -1;
            } else if (w.balance == 0)
            {
              x.balance = 0;
              p.balance = 0;
            } else
            // w.Balance == -1
            {
              x.balance = 1;
              p.balance = 0;
            }

            w.balance = 0;
          }

          break;
        }
      }
    }

    size++;
    return true;
  }

  /**
   * Removes all elements from this AVL hash table. This is an O(1) operation.
   */
  @Override
  public void clear()
  {
    root = null;
    size = 0;
  }

  /**
   * Returns true if a key exists in the key collection. This is an O(log2(n)) operation.
   * 
   * @param kvp A key value pair (only the key is used).
   * 
   * @return True if the key is found.
   * 
   * @throws NullPointerException If the key is null.
   */
  @Override
  public boolean contains(KeyValuePair<? extends TKey, ? extends TValue> kvp)
  {
    if (kvp == null)
      throw new NullPointerException("kvp");

    return keys.contains(kvp.getKey());
  }

  /**
   * Returns true if the key of the key/value pair exists in the key collection. This is an O(log2(n)) operation.
   * 
   * @param key The key to find.
   * 
   * @return True if the key is found.
   * 
   * @throws NullPointerException If the key is null.
   */
  @Override
  public boolean containsKey(TKey key)
  {
    if (key == null)
      throw new NullPointerException("key");

    return keys.contains(key);
  }

  /**
   * Gets the value associated with the specified key. This is an O(log2(n)) operation.
   * 
   * @param key The key to find.
   * 
   * @return The associated value.
   * 
   * @throws NullPointerException When the key is null.
   * @throws KeyNotFoundException When the key does not exist.
   */
  @Override
  public TValue get(TKey key)
  {
    if (key == null)
      throw new NullPointerException("key");

    AvlNode<TKey, TValue> p = root;

    while (p != null)
    {
      int c = key.compareTo(p.item.getKey());

      if (c < 0)
        p = p.left;
      else if (c > 0)
        p = p.right;
      else
        return p.item.getValue();
    }

    throw new KeyNotFoundException(key.toString());
  }

  /**
   * Gets a collection containing the keys in the AVL hashtable. This is an O(1) operation.
   * 
   * @return All keys, in ascending order.
   */
  @Override
  public KeyCollection<TKey, TValue> getKeys()
  {
    return keys;
  }

  /**
   * Gets a collection containing the values in the Returns the values of all key/value pairs, in ascending key order. This is an O(1)
   * operation.
   * 
   * @return All values.
   */
  @Override
  public ValueCollection<TKey, TValue> getValues()
  {
    return values;
  }

  /**
   * Returns an iterator of key/value pairs. Results are ordered in ascending key order.
   */
  @Override
  public Iterator<KeyValuePair<TKey, TValue>> iterator()
  {
    AvlNode<TKey, TValue> p = root;

    if (p != null)
      while (p.left != null)
        p = p.left;

    return new AscendingOrderKeyValuePairIterator<TKey, TValue>(p);
  }

  /**
   * Removes a key/value pair based on its key. This is an O(log2(n)) operation.
   * 
   * @param kvp The key/value pair (only the key is used).
   * 
   * @return True if found and removed.
   * 
   * @throws NullPointerException If the key/value pair or the key is null.
   */
  @Override
  public boolean remove(KeyValuePair<? extends TKey, ? extends TValue> kvp)
  {
    if (kvp == null)
      throw new NullPointerException("kvp");

    return remove(kvp.getKey());
  }

  /**
   * Removes the value associated with the specified key. This is an O(log2(n)) operation.
   * 
   * @param key The key to find.
   * 
   * @return True if found and removed.
   * 
   * @throws NullPointerException If the key is null.
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean remove(TKey key)
  {
    if (key == null)
      throw new NullPointerException("key");

    if (root == null)
      return false;

    AvlNode<TKey, TValue> p = root;
    AvlNode<TKey, TValue>[] nodes = new AvlNode[10];
    boolean[] path = new boolean[10];
    int depth = 0;
    nodes[depth] = new AvlNode<TKey, TValue>();
    nodes[depth].left = root;
    path[depth++] = true;

    while (p != null)
    {
      int val = key.compareTo(p.item.getKey());
      if (val == 0)
      {
        break; // have match
      }
      nodes[depth] = p;
      path[depth] = (val < 0);
      p = (path[depth]) ? p.left : p.right;
      if (++depth >= nodes.length)
      {
        AvlNode<TKey, TValue>[] old = nodes;
        nodes = new AvlNode[old.length + (old.length >> 1)];
        System.arraycopy(old, 0, nodes, 0, old.length);
        boolean[] old2 = path;
        path = new boolean[old2.length + (old2.length >> 1)];
        System.arraycopy(old2, 0, path, 0, old2.length);
      }
    }
    // delete item
    if (p != null)
    {
      if (p.right == null)
      {
        if (path[depth - 1])
        {
          nodes[depth - 1].left = p.left;
        } else
        {
          nodes[depth - 1].right = p.left;
        }
      } else
      {
        AvlNode<TKey, TValue> r = p.right;
        if (r.left == null)
        {
          r.left = p.left;
          r.balance = p.balance;
          if (path[depth - 1])
          {
            nodes[depth - 1].left = r;
          } else
          {
            nodes[depth - 1].right = r;
          }
          // cannot overflow as only 1 node added
          nodes[depth] = r;
          path[depth++] = false;

        } else
        {
          AvlNode<TKey, TValue> s;
          int index = depth;
          if (++depth >= nodes.length)
          {
            AvlNode<TKey, TValue>[] old = nodes;
            nodes = new AvlNode[old.length + (old.length >> 1)];
            System.arraycopy(old, 0, nodes, 0, old.length);
            boolean[] old2 = path;
            path = new boolean[old2.length + (old2.length >> 1)];
            System.arraycopy(old2, 0, path, 0, old2.length);
          }
          while (true)
          {
            nodes[depth] = r;
            path[depth] = true;
            if (++depth >= nodes.length)
            {
              AvlNode<TKey, TValue>[] old = nodes;
              nodes = new AvlNode[old.length + (old.length >> 1)];
              System.arraycopy(old, 0, nodes, 0, old.length);
              boolean[] old2 = path;
              path = new boolean[old2.length + (old2.length >> 1)];
              System.arraycopy(old2, 0, path, 0, old2.length);
            }
            s = r.left;
            if (s.left == null)
            {
              break;
            }
            r = s;
          }
          s.left = p.left;
          r.left = s.right;
          s.right = p.right;
          s.balance = p.balance;
          if (path[index - 1])
          {
            nodes[index - 1].left = s;
          } else
          {
            nodes[index - 1].right = s;
          }
          nodes[index] = s;
          path[index] = false;
        }
      }
      // update balance factors
      for (int i = depth - 1; i > 0; i--)
      {
        if (path[i])
        {
          nodes[i].balance++;
          if (nodes[i].balance == +1)
          {
            break;
          } else if (nodes[i].balance == +2)
          {
            AvlNode<TKey, TValue> y = nodes[i];
            AvlNode<TKey, TValue> x = y.right;
            if (x.balance == -1)
            {
              // left
              AvlNode<TKey, TValue> w = x.left;
              x.left = w.right;
              w.right = x;
              y.right = w.left;
              w.left = y;
              if (w.balance == 1)
              {
                x.balance = 0;
                y.balance = -1;
              } else if (w.balance == 0)
              {
                x.balance = 0;
                y.balance = 0;
              } else
              {
                x.balance = 1;
                y.balance = 0;
              }
              w.balance = 0;
              if (path[i - 1])
              {
                nodes[i - 1].left = w;
              } else
              {
                nodes[i - 1].right = w;
              }
            } else
            {
              // right
              y.right = x.left;
              x.left = y;
              if (path[i - 1])
              {
                nodes[i - 1].left = x;
              } else
              {
                nodes[i - 1].right = x;
              }
              if (x.balance == 0)
              {
                x.balance = -1;
                y.balance = 1;
                break;
              } else
              {
                x.balance = 0;
                y.balance = 0;
              }
            }
          }
        } else
        {
          nodes[i].balance--;
          if (nodes[i].balance == -1)
          {
            break;
          } else if (nodes[i].balance == -2)
          {
            AvlNode<TKey, TValue> y = nodes[i];
            AvlNode<TKey, TValue> x = y.left;
            if (x.balance == 1)
            {
              // right
              AvlNode<TKey, TValue> w = x.right;
              x.right = w.left;
              w.left = x;
              y.left = w.right;
              w.right = y;
              if (w.balance == -1)
              {
                x.balance = 0;
                y.balance = 1;
              } else if (w.balance == 0)
              {
                x.balance = 0;
                y.balance = 0;
              } else
              {
                x.balance = -1;
                y.balance = 0;
              }
              w.balance = 0;
              if (path[i - 1])
              {
                nodes[i - 1].left = w;
              } else
              {
                nodes[i - 1].right = w;
              }
            } else
            {
              // left
              y.left = x.right;
              x.right = y;
              if (path[i - 1])
              {
                nodes[i - 1].left = x;
              } else
              {
                nodes[i - 1].right = x;
              }
              if (x.balance == 0)
              {
                x.balance = 1;
                y.balance = -1;
                break;
              } else
              {
                x.balance = 0;
                y.balance = 0;
              }
            }
          }
        }
      }
      if (nodes[0].left != root)
      {
        root = nodes[0].left;
      }
      return true;
    } else
    {
      // not found
      return false;
    }

  }

  /**
   * Replaces a key's value with the specified value. This is an O(log2(n)) operation.
   * 
   * @param key The key to find.
   * @param value The new value.
   * 
   * @return True if the key is found and replaced. False otherwise.
   * 
   * @throws NullPointerException When the key is null.
   */
  @Override
  public boolean replace(TKey key, TValue value)
  {
    if (key == null)
      throw new NullPointerException("key");

    AvlNode<TKey, TValue> p = root;

    while (p != null)
    {
      int c = key.compareTo(p.item.getKey());

      if (c < 0)
      {
        p = p.left;
      } else if (c > 0)
      {
        p = p.right;
      } else
      {
        p.item = new KeyValuePair<TKey, TValue>(key, value);
        return true;
      }
    }

    return false;
  }

  /**
   * Gets the number of key/value pairs contained in the AVL hashtable. This is an O(1) operation.
   */
  @Override
  public int size()
  {
    return size;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameterKey()
  {
    return keys.getGenericTypeParameter();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getGenericTypeParameterValue()
  {
    return values.getGenericTypeParameter();
  }

  /**
   * Attempts to get a value by a given key. This is an O(log2(n)) operation.
   * 
   * @param key The key to find.
   * 
   * @return Results in success/failure with the key's value in the case of success.
   * 
   * @throws NullPointerException When the key is null.
   */
  @Override
  public TryResult<TValue> tryGetValue(TKey key)
  {
    if (key == null)
      throw new NullPointerException("key");

    try
    {
      return new TryResult<TValue>(get(key));
    }
    catch(KeyNotFoundException e)
    {
      return new TryResult<TValue>();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return Linq.toString(this);
  }
}
