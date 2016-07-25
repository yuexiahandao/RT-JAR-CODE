package org.w3c.dom;

public abstract interface Document extends Node
{
  public abstract DocumentType getDoctype();

  public abstract DOMImplementation getImplementation();

  public abstract Element getDocumentElement();

  public abstract Element createElement(String paramString)
    throws DOMException;

  public abstract DocumentFragment createDocumentFragment();

  public abstract Text createTextNode(String paramString);

  public abstract Comment createComment(String paramString);

  public abstract CDATASection createCDATASection(String paramString)
    throws DOMException;

  public abstract ProcessingInstruction createProcessingInstruction(String paramString1, String paramString2)
    throws DOMException;

  public abstract Attr createAttribute(String paramString)
    throws DOMException;

  public abstract EntityReference createEntityReference(String paramString)
    throws DOMException;

  public abstract NodeList getElementsByTagName(String paramString);

  public abstract Node importNode(Node paramNode, boolean paramBoolean)
    throws DOMException;

  public abstract Element createElementNS(String paramString1, String paramString2)
    throws DOMException;

  public abstract Attr createAttributeNS(String paramString1, String paramString2)
    throws DOMException;

  public abstract NodeList getElementsByTagNameNS(String paramString1, String paramString2);

  public abstract Element getElementById(String paramString);

  public abstract String getInputEncoding();

  public abstract String getXmlEncoding();

  public abstract boolean getXmlStandalone();

  public abstract void setXmlStandalone(boolean paramBoolean)
    throws DOMException;

  public abstract String getXmlVersion();

  public abstract void setXmlVersion(String paramString)
    throws DOMException;

  public abstract boolean getStrictErrorChecking();

  public abstract void setStrictErrorChecking(boolean paramBoolean);

  public abstract String getDocumentURI();

  public abstract void setDocumentURI(String paramString);

  public abstract Node adoptNode(Node paramNode)
    throws DOMException;

  public abstract DOMConfiguration getDomConfig();

  public abstract void normalizeDocument();

  public abstract Node renameNode(Node paramNode, String paramString1, String paramString2)
    throws DOMException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.Document
 * JD-Core Version:    0.6.2
 */