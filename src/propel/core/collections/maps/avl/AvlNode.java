/*
Ported to Java by Nikolaos Tountas - nicholas.tountas@gmail.com

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

/**
 * Represents a node in the AVL hash table.
 * The AvlNode contains a key/value pair, a reference to the parent
 * node, a reference to the left child node, a reference to the right child node
 * and a balance factor for this node.
 *
 * @param <TKey>   The key type.
 * @param <TValue> The value type.
 */
final class AvlNode<TKey extends Comparable<TKey>, TValue>
{
	/**
	 * The balance factor of this node.
	 * The balance factor of a node is the height of its right sub-tree minus the height
	 * of its left sub-tree. A node with balance factor 1, 0, or -1 is considered balanced.
	 * A node with balance factor -2 or 2 is considered unbalanced and requires re-balancing
	 * the tree.
	 */
	byte balance;
	/**
	 * The key/value pair contained in this node.
	 */
	KeyValuePair<TKey, TValue> item;
	/**
	 * The reference to the left child node of this node,
	 * or null if this node has no left child node.
	 */
	AvlNode<TKey, TValue> left;
	/**
	 * The reference to the parent node of this node,
	 * or null if this is the root node of the tree.
	 */
	AvlNode<TKey, TValue> parent;
	/**
	 * The reference to the right child node of this node,
	 * or null if this node has no right child node.
	 */
	AvlNode<TKey, TValue> right;

	/**
	 * Default constructor.
	 */
	public AvlNode()
	{
	}

	/**
	 * Initializes a new AvlNode.
	 */
	AvlNode(KeyValuePair<TKey, TValue> item)
	{
		this.item = item;
	}

	/**
	 * Initializes a new AvlNode.
	 */
	AvlNode(KeyValuePair<TKey, TValue> item, AvlNode<TKey, TValue> parent)
	{
		this.item = item;
		this.parent = parent;
	}
}
