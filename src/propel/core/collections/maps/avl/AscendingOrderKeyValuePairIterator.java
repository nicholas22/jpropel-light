/*
GOLETAS COMMUNITY SOURCE CODE LICENSE AGREEMENT
Version: June 11, 2006
Copyright © 2005 - 2008 Maksim Goleta. All Rights Reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy of this source code and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software or documentation (and/or other materials) for the binary forms, if any, produced from the Software or derived source code.
Products derived from the Software may not be called Goletas, nor may Goletas appear in their name without the prior written permission of Goletas. This license does not grant you any rights to use Goletas’ logos or trademarks. If you alter the Software in any way, you must remove the modified source code from the Goletas code namespace, if applicable, and cause the modified files to carry prominent notices stating that you changed the files.
If you begin patent litigation against Goletas over patents that you think may apply to the Software (including a cross-claim or counterclaim in a lawsuit), your license to the Software is terminated automatically.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS. IN NO EVENT SHALL GOLETAS, THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
Except as contained in this notice, neither the name Goletas nor the names of contributors or copyright holders may be used in advertising or otherwise to promote the sale, use or other dealings in the Software or products derived from the Software without the specific prior written authorization.
*/
package propel.core.collections.maps.avl;

import propel.core.collections.KeyValuePair;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Enumerates the key/value pairs of the AVL tree.
 * The elements are enumerated in ascending order.
 */
public final class AscendingOrderKeyValuePairIterator<TKey extends Comparable<TKey>, TValue>
		implements Iterator<KeyValuePair<TKey, TValue>>
{
	/**
	 * The AvlNode at the current position of the enumerator.
	 */
	private AvlNode<TKey, TValue> next;

	/**
	 * Constructor initializes a new ascending order iterator.
	 *
	 * @param node The node from which to start enumerating.
	 */
	AscendingOrderKeyValuePairIterator(AvlNode<TKey, TValue> node)
	{
		next = node;
	}

	/**
	 * Returns true if the iteration has more elements.
	 *
	 * @return True if the iterator has more elements.
	 */
	@Override
	public boolean hasNext()
	{
		return next != null;
	}

	/**
	 * Returns the next element in the iteration.
	 * Calling this method repeatedly until the hasNext() method returns false will return each element in the underlying collection exactly once.
	 *
	 * @return The next element in the iteration.
	 *
	 * @throws NoSuchElementException Iteration has no more elements.
	 */
	@Override
	public KeyValuePair<TKey, TValue> next()
	{
		if(next == null)
			throw new NoSuchElementException("There is no next element.");

		KeyValuePair<TKey, TValue> result = next.item;

		if(next.right == null)
		{
			while((next.parent != null) && (next == next.parent.right))
			{
				next = next.parent;
			}

			next = next.parent;
		}
		else
		{
			next = next.right;

			while(next.left != null)
				next = next.left;
		}

		return result;
	}

	/**
	 * @throws UnsupportedOperationException The remove operation is not supported.
	 */
	@Override
	@Deprecated
	public void remove()
	{
		throw new UnsupportedOperationException();
	}
}
