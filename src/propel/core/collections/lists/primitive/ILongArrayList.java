package propel.core.collections.lists.primitive;

/**
 * Interface of an ArrayList of primitive Long elements
 */
public interface ILongArrayList
{

  /**
   * Adds an element to the collection. This is an O(1) operation.
   * 
   * @return True always.
   */
   boolean add(long element);

  /**
   * Inserts the specified element at the specified position in this list. Shifts the element currently at that position and any subsequent
   * elements to the right (adds one to their indices). This is an O(1) operation.
   * 
   * @throws IndexOutOfBoundsException If the index is out of range
   */
   void add(int index, long element);

  /**
   * Appends all of the elements in the specified array to the end of this list, in the order that they are ordered.
   * 
   * This is an O(n) operation where n is the size of the given array.
   * 
   * @return True, if this list changed as a result of the call
   * 
   * @throws NullPointerException If the array is null.
   */
   boolean addAll(long[] array);

  /**
   * Empties the list and re-creates the internal buffer with a default size. This is an O(1) operation.
   */
   void clear();

  /**
   * Scans the collection linearly and returns true if the specified element is found. This is an O(n) operation.
   */
   boolean contains(long value);

  /**
   * Returns true if all elements in the provided collection are contained in this collection. Note: also returns true if the collection
   * given is empty. This is an O(n^2) operation
   * 
   * @throws NullPointerException The argument is null.
   */
   boolean containsAll(long[] array);

  /**
   * Returns the element at the specified index. This is an O(1) operation.
   * 
   * @throws IndexOutOfBoundsException When the index is out of range
   */
   long get(int index);

  /**
   * Scans the collection linearly from start to end and returns the index of the element specified. Returns -1 if the element is not found.
   * This is an O(n) operation.
   */
   int indexOf(long value);

  /**
   * Returns true if the list is empty. This is an O(1) operation.
   */
   boolean isEmpty();

  /**
   * Scans the collection linearly from end to start returns the index of the element specified. Returns -1 if the element is not found.
   * This is an O(n) operation.
   */
   int lastIndexOf(long value);

  /**
   * Removes the first occurrence of an element. Elements to the right of the removed element are shifted to the left by one position (their
   * indices decrease by one). This is an O(n) operation.
   * 
   * @return True if an element was removed, false otherwise.
   */
   boolean removeValue(long value);

  /**
   * Removes the element at the specified position. Elements to the right of the removed element are shifted to the left by one position
   * (their indices decrease by one). This is an O(n) operation.
   * 
   * @return The element removed.
   * 
   * @throws IndexOutOfBoundsException When the index is out of range.
   */
   long remove(int index);

  /**
   * Replaces the element at the specified position in this list with the specified element. This is an O(1) operation.
   * 
   * @return The old element
   * 
   * @throws IndexOutOfBoundsException When the index is out of range
   */
   long set(int index, long value);

  /**
   * Returns the size of the list
   */
   int size();

  /**
   * {@inheritDoc}
   */
   long[] toArray();

  /**
   * Returns all elements in an array. Attempts to use the provided array, but creates a new one if the length of the given array is not
   * sufficient to fit all elements (or if it is null). This is an O(n) operation.
   */
   long[] toArray(long[] a);

}
