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
package propel.core.utils;

import java.util.ArrayList;
import java.util.List;
import lombok.Validate;
import lombok.Validate.NotNull;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import propel.core.common.CONSTANT;

/**
 * Provides utility functionality for parsing XML nodes/attributes
 */
public final class XmlUtils
{

  /**
   * Private constructor
   */
  private XmlUtils()
  {
  }

  /**
   * Compacts an XML text string, by removing CR, LF, TAB and converting double WHITESPACE characters to single. Be aware that this method
   * does not preserve CDATA content.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static String compact(@NotNull String xml)
  {
    xml = xml.replace(CONSTANT.CR, CONSTANT.EMPTY_STRING);
    xml = xml.replace(CONSTANT.LF, CONSTANT.EMPTY_STRING);
    xml = xml.replace(CONSTANT.TAB, CONSTANT.EMPTY_STRING);

    while (xml.contains(CONSTANT.DOUBLE_WHITESPACE))
      xml = xml.replace(CONSTANT.DOUBLE_WHITESPACE, CONSTANT.WHITESPACE);

    return xml;
  }

  /**
   * Compacts an XML string builder, by removing CR, LF, TAB and converting double WHITESPACE characters to single. Be aware that this
   * method does not preserve CDATA content. It is more efficient than using Strings.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static void compact(@NotNull StringBuilder xml)
  {
    StringUtils.replace(xml, CONSTANT.CR, CONSTANT.EMPTY_STRING);
    StringUtils.replace(xml, CONSTANT.LF, CONSTANT.EMPTY_STRING);
    StringUtils.replace(xml, CONSTANT.TAB, CONSTANT.EMPTY_STRING);

    while (xml.indexOf(CONSTANT.DOUBLE_WHITESPACE) >= 0)
      StringUtils.replace(xml, CONSTANT.DOUBLE_WHITESPACE, CONSTANT.WHITESPACE);
  }

  /**
   * Searches for the first matching attribute found on the given Node. Uses StringMatch.Equals and OrdinalIgnoreCase string comparison. If
   * none is found, null is returned.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static Node findAttribute(Node node, String attributeName)
  {
    return findAttribute(node, attributeName, MatchType.Equals, StringComparison.OrdinalIgnoreCase);
  }

  /**
   * Searches for the first matching attribute found on the given Node. If none is found, null is returned.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static Node findAttribute(@NotNull final Node node, @NotNull final String attributeName, MatchType matchType,
                                   StringComparison comparisonType)
  {
    switch(matchType)
    {
      case Equals:
        return findAttributeEquals(node, attributeName, comparisonType);
      case Contains:
        return findAttributeContains(node, attributeName, comparisonType);
      case StartsWith:
        return findAttributeStartsWith(node, attributeName, comparisonType);
      case EndsWith:
        return findAttributeEndsWith(node, attributeName, comparisonType);
      default:
        throw new IllegalArgumentException("Unrecognized string matching type: " + matchType);
    }
  }

  /**
   * Parses the node for a given attribute. The comparison type can be specified, e.g. OrdinalIgnoreCase. If the attribute is not found,
   * null is returned.
   */
  private static Node findAttributeEquals(Node node, String attributeName, StringComparison comparisonType)
  {
    NamedNodeMap attributes = node.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++)
    {
      Node attr = attributes.item(i);
      String name = attr.getNodeName();
      if (!StringUtils.isNullOrEmpty(name))
        if (StringUtils.equal(name, attributeName, comparisonType))
          return attr;
    }

    return null;
  }

  /**
   * Parses the node for an attribute that ends with a specific string. The comparison type can be specified, e.g. OrdinalIgnoreCase. If the
   * attribute is not found, null is returned.
   */
  private static Node findAttributeEndsWith(Node node, String attributeName, StringComparison comparisonType)
  {
    NamedNodeMap attributes = node.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++)
    {
      Node attr = attributes.item(i);
      String name = attr.getNodeName();
      if (!StringUtils.isNullOrEmpty(name))
        if (StringUtils.endsWith(name, attributeName, comparisonType))
          return attr;
    }

    return null;
  }

  /**
   * Parses the node for an attribute that starts with a specific string. The comparison type can be specified, e.g. OrdinalIgnoreCase. If
   * the attribute is not found, null is returned.
   */
  private static Node findAttributeStartsWith(Node node, String attributeName, StringComparison comparisonType)
  {
    NamedNodeMap attributes = node.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++)
    {
      Node attr = attributes.item(i);
      String name = attr.getNodeName();
      if (!StringUtils.isNullOrEmpty(name))
        if (StringUtils.startsWith(name, attributeName, comparisonType))
          return attr;
    }

    return null;
  }

  /**
   * Parses the node for an attribute that contains in its name a specific string. The comparison type can be specified, e.g.
   * OrdinalIgnoreCase. If the attribute is not found, null is returned.
   */
  private static Node findAttributeContains(Node node, String attributeName, StringComparison comparisonType)
  {
    NamedNodeMap attributes = node.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++)
    {
      Node attr = attributes.item(i);
      String name = attr.getNodeName();
      if (!StringUtils.isNullOrEmpty(name))
        if (StringUtils.contains(name, attributeName, comparisonType))
          return attr;
    }
    return null;
  }

  /**
   * Parses the first matching attribute found on the given Node. Uses StringMatch.Equals and OrdinalIgnoreCase string comparison. If none
   * is found, null is returned.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public static String parseAttribute(Node node, String attributeName)
  {
    return parseAttribute(node, attributeName, MatchType.Equals, StringComparison.OrdinalIgnoreCase);
  }

  /**
   * Parses the first matching attribute found on the given Node. If none is found, null is returned.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public static String parseAttribute(Node node, String attributeName, MatchType matchType, StringComparison comparisonType)
  {
    Node attr = findAttribute(node, attributeName, matchType, comparisonType);
    return attr != null ? attr.getNodeValue() : null;
  }

  /**
   * Searches for a matching child node found under the given Node. Uses StringMatch.Equals and OrdinalIgnoreCase string comparison. Returns
   * the first node found or, if no node is found, null.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public static Node findNode(Node node, String childNodeName)
  {
    return findNode(node, childNodeName, 0, MatchType.Equals, StringComparison.OrdinalIgnoreCase);
  }

  /**
   * Searches for a matching child node found under the given Node. Use 0 for index to indicate that the first node found is to be returned,
   * 1 for second, etc. Uses StringMatch.Equals and OrdinalIgnoreCase string comparison. If none is found or the index is not reached, null
   * is returned.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public static Node findNode(Node node, String childNodeName, int index)
  {
    return findNode(node, childNodeName, index, MatchType.Equals, StringComparison.OrdinalIgnoreCase);
  }

  /**
   * Searches for a matching child node found under the given Node. By default the first node found is returned, but this is configurable.
   * If none is found, null is returned.
   * 
   * @throws NullPointerException When an argument is null.
   */
  @Validate
  public static Node findNode(@NotNull final Node node, @NotNull final String childNodeName, int index, MatchType matchType,
                              StringComparison comparisonType)
  {
    switch(matchType)
    {
      case Equals:
        return findNodeEquals(node, childNodeName, comparisonType, index);
      case Contains:
        return findNodeContains(node, childNodeName, comparisonType, index);
      case StartsWith:
        return findNodeStartsWith(node, childNodeName, comparisonType, index);
      case EndsWith:
        return findNodeEndsWith(node, childNodeName, comparisonType, index);
      default:
        throw new IllegalArgumentException("Unrecognized string matching type: " + matchType);
    }
  }

  /**
   * Parses the children of the given node and returns a child node matching the given name, skipping the first few matching nodes as
   * specified. The comparison type can be specified, e.g. OrdinalIgnoreCase. If the node is not found, or the index is invalid, null is
   * returned.
   */
  private static Node findNodeEquals(Node node, String nodeName, StringComparison comparisonType, int index)
  {
    int count = 0;
    NodeList nodeChildren = node.getChildNodes();
    int nodeChildrenLen = nodeChildren.getLength();

    for (int i = 0; i < nodeChildrenLen; i++)
    {
      Node child = nodeChildren.item(i);
      String childName = child.getNodeName();
      if (!StringUtils.isNullOrEmpty(childName))
        if (StringUtils.equal(childName, nodeName, comparisonType))
        {
          if (index == count)
            return child;

          count++;
        }
    }

    return null;
  }

  /**
   * Parses the children of the given node and returns a child node ending with the given name, skipping the first few matching nodes as
   * specified. The comparison type can be specified, e.g. OrdinalIgnoreCase. If the node is not found, or the index is invalid, null is
   * returned.
   */
  private static Node findNodeEndsWith(Node node, String nodeName, StringComparison comparisonType, int index)
  {
    int count = 0;
    NodeList nodeChildren = node.getChildNodes();
    int nodeChildrenLen = nodeChildren.getLength();

    for (int i = 0; i < nodeChildrenLen; i++)
    {
      Node child = nodeChildren.item(i);
      String childName = child.getNodeName();
      if (!StringUtils.isNullOrEmpty(childName))
        if (StringUtils.endsWith(childName, nodeName, comparisonType))
        {
          if (index == count)
            return child;

          count++;
        }
    }

    return null;
  }

  /**
   * Parses the children of the given node and returns a child node starting with the given name, skipping the first few matching nodes as
   * specified. The comparison type can be specified, e.g. OrdinalIgnoreCase. If the node is not found, or the index is invalid, null is
   * returned.
   */
  private static Node findNodeStartsWith(Node node, String nodeName, StringComparison comparisonType, int index)
  {
    int count = 0;
    NodeList nodeChildren = node.getChildNodes();
    int nodeChildrenLen = nodeChildren.getLength();

    for (int i = 0; i < nodeChildrenLen; i++)
    {
      Node child = nodeChildren.item(i);
      String childName = child.getNodeName();
      if (!StringUtils.isNullOrEmpty(childName))
        if (StringUtils.startsWith(childName, nodeName, comparisonType))
        {
          if (index == count)
            return child;

          count++;
        }
    }

    return null;
  }

  /**
   * Parses the children of the given node and returns the first child node containing in its name a specified string, skipping the first
   * few matching nodes as specified. The comparison type can be specified, e.g. OrdinalIgnoreCase. If the node is not found, or the index
   * is invalid, null is returned.
   */
  private static Node findNodeContains(Node node, String nodeName, StringComparison comparisonType, int index)
  {
    int count = 0;
    NodeList nodeChildren = node.getChildNodes();
    int nodeChildrenLen = nodeChildren.getLength();

    for (int i = 0; i < nodeChildrenLen; i++)
    {
      Node child = nodeChildren.item(i);
      String childName = child.getNodeName();
      if (!StringUtils.isNullOrEmpty(childName))
        if (StringUtils.contains(childName, nodeName, comparisonType))
        {
          if (index == count)
            return child;

          count++;
        }
    }

    return null;
  }

  /**
   * Searches for all matching child nodes found under the given Node. Uses StringMatch.Equals and OrdinalIgnoreCase string comparison. If
   * none is found, an empty list is returned.
   * 
   * @throws NullPointerException An argument is null.
   */
  public static List<Node> findAllNodes(Node node, String childNodeName)
  {
    return findAllNodes(node, childNodeName, MatchType.Equals, StringComparison.OrdinalIgnoreCase);
  }

  /**
   * Searches for all matching child nodes found under the given Node. If none is found, an empty list is returned.
   * 
   * @throws NullPointerException An argument is null.
   */
  @Validate
  public static List<Node> findAllNodes(@NotNull final Node node, @NotNull final String childNodeName, MatchType matchType,
                                        StringComparison comparisonType)
  {
    switch(matchType)
    {
      case Equals:
        return findAllNodesEquals(node, childNodeName, comparisonType);
      case Contains:
        return findAllNodesContains(node, childNodeName, comparisonType);
      case StartsWith:
        return findAllNodesStartsWith(node, childNodeName, comparisonType);
      case EndsWith:
        return findAllNodesEndsWith(node, childNodeName, comparisonType);
      default:
        throw new IllegalArgumentException("Unrecognized string matching type: " + matchType);
    }
  }

  /**
   * Parses the children of the given node and returns all matching children nodes. The comparison type can be specified, e.g.
   * OrdinalIgnoreCase. If no nodes are found, an empty list is returned.
   */
  private static List<Node> findAllNodesEquals(Node node, String nodeName, StringComparison comparisonType)
  {
    List<Node> result = new ArrayList<Node>(64);

    NodeList children = node.getChildNodes();
    int childrenLen = children.getLength();

    for (int i = 0; i < childrenLen; i++)
    {
      Node child = children.item(i);
      String childName = child.getNodeName();
      if (!StringUtils.isNullOrEmpty(childName))
        if (StringUtils.equal(childName, nodeName, comparisonType))
          result.add(child);
    }

    return result;
  }

  /**
   * Parses the children of the given node and returns all children nodes ending with a specified string. The comparison type can be
   * specified, e.g. OrdinalIgnoreCase. If no nodes are found, an empty list is returned.
   */
  private static List<Node> findAllNodesEndsWith(Node node, String nodeName, StringComparison comparisonType)
  {
    List<Node> result = new ArrayList<Node>(64);

    NodeList children = node.getChildNodes();
    int childrenLen = children.getLength();

    for (int i = 0; i < childrenLen; i++)
    {
      Node child = children.item(i);
      String childName = child.getNodeName();
      if (!StringUtils.isNullOrEmpty(childName))
        if (StringUtils.endsWith(childName, nodeName, comparisonType))
          result.add(child);
    }

    return result;
  }

  /**
   * Parses the children of the given node and returns all children nodes starting with a specified string. The comparison type can be
   * specified, e.g. OrdinalIgnoreCase. If no nodes are found, an empty list is returned.
   */
  private static List<Node> findAllNodesStartsWith(Node node, String nodeName, StringComparison comparisonType)
  {
    List<Node> result = new ArrayList<Node>(64);

    NodeList children = node.getChildNodes();
    int childrenLen = children.getLength();

    for (int i = 0; i < childrenLen; i++)
    {
      Node child = children.item(i);
      String childName = child.getNodeName();
      if (!StringUtils.isNullOrEmpty(childName))
        if (StringUtils.startsWith(childName, nodeName, comparisonType))
          result.add(child);
    }

    return result;
  }

  /**
   * Parses the children of the given node and returns all children nodes containing in their name a specified string. The comparison type
   * can be specified, e.g. OrdinalIgnoreCase. If no nodes are found, an empty list is returned.
   */
  private static List<Node> findAllNodesContains(Node node, String nodeName, StringComparison comparisonType)
  {
    List<Node> result = new ArrayList<Node>(64);

    NodeList children = node.getChildNodes();
    int childrenLen = children.getLength();

    for (int i = 0; i < childrenLen; i++)
    {
      Node child = children.item(i);
      String childName = child.getNodeName();
      if (!StringUtils.isNullOrEmpty(childName))
        if (StringUtils.contains(childName, nodeName, comparisonType))
          result.add(child);
    }

    return result;
  }

  /**
   * Parses a child node found on the given Node. Uses StringMatch.Equals and OrdinalIgnoreCase string comparison. Returns the first node
   * found or, if no node is found, null.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public static String parseNode(Node node, String childNodeName)
  {
    return parseNode(node, childNodeName, 0, MatchType.Equals, StringComparison.OrdinalIgnoreCase);
  }

  /**
   * Parses a child node found on the given Node. Use 0 for index to indicate that the first node found is to be parsed, 1 for second, etc.
   * Uses StringMatch.Equals and OrdinalIgnoreCase string comparison. If none is found or the index is not reached, null is returned.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public static String parseNode(Node node, String childNodeName, int index)
  {
    return parseNode(node, childNodeName, index, MatchType.Equals, StringComparison.OrdinalIgnoreCase);
  }

  /**
   * Parses a child node found on the given Node. Use 0 for index to indicate that the first node found is to be parsed, 1 for second, etc.
   * If none is found or the index is not reached, null is returned.
   * 
   * @throws NullPointerException When an argument is null.
   */
  public static String parseNode(Node node, String childNodeName, int index, MatchType matchType, StringComparison comparisonType)
  {
    Node childNode = findNode(node, childNodeName, index, matchType, comparisonType);
    return childNode != null ? childNode.getNodeValue() : null;
  }
}
