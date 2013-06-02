package propel.core.validation.propertyMetadata;

import lombok.Getter;
import lombok.Setter;

abstract class EnumerablePropertyMetadata
    extends NullablePropertyMetadata<Object>
{
  /**
   * Error message when a maximum value is less than the minimum value
   */
  public static final String PROPERTY_ERROR_MAX_LESS_THAN_MIN = "%s maximum size cannot be less than the allowed minimum size!";
  /**
   * Error message when the size is set
   */
  public static final String SHOULD_BE_EXACTLY = "%s should have a size of ";
  /**
   * Error message when a size is too high
   */
  public static final String SHOULD_NOT_BE_GREATER_THAN = "%s should not have a size larger than ";
  /**
   * Error message when a size is too low
   */
  public static final String SHOULD_NOT_BE_LESS_THAN = "%s should not have a size smaller than ";
  /**
   * Error message when a size is negative
   */
  public static final String SHOULD_NOT_BE_NEGATIVE = "%s bound cannot be negative!";
  /**
   * Error message when a null element exists
   */
  public static final String SHOULD_NOT_CONTAIN_NULL_ELEMENTS = "%s should not contain null elements!";

  /**
   * The minimum inclusive value allowed
   */
  @Getter
  @Setter
  protected int minSize;
  /**
   * The maximum inclusive value allowed
   */
  @Getter
  @Setter
  protected int maxSize;
  /**
   * Whether empty elements are allowed
   */
  @Getter
  @Setter
  protected boolean noNullElements;

  /**
   * Default constructor
   */
  protected EnumerablePropertyMetadata()
  {
    super();
  }

  /**
   * Constructor
   * 
   * @throws IllegalArgumentException Property metadata name cannot be null or empty!
   */
  protected EnumerablePropertyMetadata(String name, boolean notNull)
  {
    super(name, notNull);
  }
}
