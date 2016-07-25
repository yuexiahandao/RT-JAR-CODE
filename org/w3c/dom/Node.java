package org.w3c.dom;

public abstract interface Node
{
  public static final short ELEMENT_NODE = 1;
  public static final short ATTRIBUTE_NODE = 2;
  public static final short TEXT_NODE = 3;
  public static final short CDATA_SECTION_NODE = 4;
  public static final short ENTITY_REFERENCE_NODE = 5;
  public static final short ENTITY_NODE = 6;
  public static final short PROCESSING_INSTRUCTION_NODE = 7;
  public static final short COMMENT_NODE = 8;
  public static final short DOCUMENT_NODE = 9;
  public static final short DOCUMENT_TYPE_NODE = 10;
  public static final short DOCUMENT_FRAGMENT_NODE = 11;
  public static final short NOTATION_NODE = 12;
  public static final short DOCUMENT_POSITION_DISCONNECTED = 1;
  public static final short DOCUMENT_POSITION_PRECEDING = 2;
  public static final short DOCUMENT_POSITION_FOLLOWING = 4;
  public static final short DOCUMENT_POSITION_CONTAINS = 8;
  public static final short DOCUMENT_POSITION_CONTAINED_BY = 16;
  public static final short DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;

  public abstract String getNodeName();

  public abstract String getNodeValue()
    throws DOMException;

  public abstract void setNodeValue(String paramString)
    throws DOMException;

  public abstract short getNodeType();

  public abstract Node getParentNode();

  public abstract NodeList getChildNodes();

  public abstract Node getFirstChild();

  public abstract Node getLastChild();

  public abstract Node getPreviousSibling();

  public abstract Node getNextSibling();

  public abstract NamedNodeMap getAttributes();

  public abstract Document getOwnerDocument();

  public abstract Node insertBefore(Node paramNode1, Node paramNode2)
    throws DOMException;

  public abstract Node replaceChild(Node paramNode1, Node paramNode2)
    throws DOMException;

  public abstract Node removeChild(Node paramNode)
    throws DOMException;

  public abstract Node appendChild(Node paramNode)
    throws DOMException;

  public abstract boolean hasChildNodes();

  public abstract Node cloneNode(boolean paramBoolean);

  public abstract void normalize();

  public abstract boolean isSupported(String paramString1, String paramString2);

  public abstract String getNamespaceURI();

  public abstract String getPrefix();

  public abstract void setPrefix(String paramString)
    throws DOMException;

  public abstract String getLocalName();

  public abstract boolean hasAttributes();

  public abstract String getBaseURI();

  public abstract short compareDocumentPosition(Node paramNode)
    throws DOMException;

  public abstract String getTextContent()
    throws DOMException;

  public abstract void setTextContent(String paramString)
    throws DOMException;

  public abstract boolean isSameNode(Node paramNode);

  public abstract String lookupPrefix(String paramString);

  public abstract boolean isDefaultNamespace(String paramString);

  public abstract String lookupNamespaceURI(String paramString);

  public abstract boolean isEqualNode(Node paramNode);

  public abstract Object getFeature(String paramString1, String paramString2);

  public abstract Object setUserData(String paramString, Object paramObject, UserDataHandler paramUserDataHandler);

  public abstract Object getUserData(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.Node
 * JD-Core Version:    0.6.2
 */