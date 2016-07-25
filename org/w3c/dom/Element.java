package org.w3c.dom;

public abstract interface Element extends Node
{
  public abstract String getTagName();

  public abstract String getAttribute(String paramString);

  public abstract void setAttribute(String paramString1, String paramString2)
    throws DOMException;

  public abstract void removeAttribute(String paramString)
    throws DOMException;

  public abstract Attr getAttributeNode(String paramString);

  public abstract Attr setAttributeNode(Attr paramAttr)
    throws DOMException;

  public abstract Attr removeAttributeNode(Attr paramAttr)
    throws DOMException;

  public abstract NodeList getElementsByTagName(String paramString);

  public abstract String getAttributeNS(String paramString1, String paramString2)
    throws DOMException;

  public abstract void setAttributeNS(String paramString1, String paramString2, String paramString3)
    throws DOMException;

  public abstract void removeAttributeNS(String paramString1, String paramString2)
    throws DOMException;

  public abstract Attr getAttributeNodeNS(String paramString1, String paramString2)
    throws DOMException;

  public abstract Attr setAttributeNodeNS(Attr paramAttr)
    throws DOMException;

  public abstract NodeList getElementsByTagNameNS(String paramString1, String paramString2)
    throws DOMException;

  public abstract boolean hasAttribute(String paramString);

  public abstract boolean hasAttributeNS(String paramString1, String paramString2)
    throws DOMException;

  public abstract TypeInfo getSchemaTypeInfo();

  public abstract void setIdAttribute(String paramString, boolean paramBoolean)
    throws DOMException;

  public abstract void setIdAttributeNS(String paramString1, String paramString2, boolean paramBoolean)
    throws DOMException;

  public abstract void setIdAttributeNode(Attr paramAttr, boolean paramBoolean)
    throws DOMException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.Element
 * JD-Core Version:    0.6.2
 */