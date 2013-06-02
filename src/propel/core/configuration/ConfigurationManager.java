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
package propel.core.configuration;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import propel.core.utils.ReflectionUtils;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * A class behaving similarly to .NET's ConfigurationManager, i.e. being able to load an XML configuration file and access its contents in a
 * similar fashion and exposed API.
 */
public final class ConfigurationManager
{
  /**
   * Attribute type element of root (section) xml node, documents handler's full class name
   */
  protected static final String ATTR_TYPE = "type";

  /**
   * Retrieves a specified configuration section file, reads its contents and invokes the specified handler, returning the parsed
   * configuration. The client of this API needs to know in advance what is expected to be returned, e.g. an array, a concrete object, etc.
   * This is a limitation to the design of the configuration system of .NET and this class is designed to resemble it as closely as
   * possible.
   * 
   * @throws FileNotFoundException The specified file was not found
   * @throws ConfigurationErrorsException When configuration errors occur.
   */
  public static Object getSection(String appConfigSectionFile)
      throws FileNotFoundException, ConfigurationErrorsException
  {
    // load file
    File file = new File(appConfigSectionFile);
    if (!file.exists())
      throw new FileNotFoundException("The application configuration section file does not exist: " + appConfigSectionFile);

    // convert configuration file to XML
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    DocumentBuilder db;
    try
    {
      db = dbf.newDocumentBuilder();
    }
    catch(Throwable e)
    {
      throw new ConfigurationErrorsException("Parser configuration error while parsing application configuration section file: "
          + appConfigSectionFile, e);
    }

    Document doc;
    try
    {
      doc = db.parse(file);
    }
    catch(Throwable e)
    {
      throw new ConfigurationErrorsException("Parsing error occurred while parsing XML application configuration section file: "
          + appConfigSectionFile, e);
    }

    // get handler name
    Node root = doc.getFirstChild();
    if (root == null)
      throw new ConfigurationErrorsException("There are no elements in configuration section file: " + appConfigSectionFile);

    Node typeAttribute = root.getAttributes().getNamedItem(ATTR_TYPE);
    if (typeAttribute == null)
      throw new ConfigurationErrorsException("There is no '" + ATTR_TYPE + "' handler class specified for the root element '"
          + root.getNodeName() + "' of the XML application configuration section file: " + appConfigSectionFile);
    String handlerClassName = typeAttribute.getNodeValue();

    // get handler class and instantiate
    Class<?> handlerClass;
    try
    {
      handlerClass = Class.forName(handlerClassName);
    }
    catch(ClassNotFoundException cnfe)
    {
      throw new ConfigurationErrorsException("There is no configuration section handler class named '" + handlerClassName
          + "' as specified in the XML application configuration section file: " + appConfigSectionFile, cnfe);
    }

    // security check, expected to be derived from the right class
    if (!ReflectionUtils.isImplementing(handlerClass, IConfigSectionHandler.class))
      throw new ConfigurationErrorsException("The specified configuration section handler class named '" + handlerClassName
          + "', specified in the XML application configuration section file '" + appConfigSectionFile + "' does not extend "
          + IConfigSectionHandler.class.getSimpleName());

    // instantiate
    IConfigSectionHandler handler;
    try
    {
      handler = (IConfigSectionHandler) ReflectionUtils.activate(handlerClass);
    }
    catch(Throwable e)
    {
      throw new ConfigurationErrorsException("Could not instantiate section handler '" + handlerClassName
          + "' specified in the XML application configuration section file: " + appConfigSectionFile, e);
    }

    // use config handler to parse configuration section
    return handler.create(doc);
  }
}
