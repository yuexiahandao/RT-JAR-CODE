package com.sun.org.apache.xml.internal.security.utils;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract interface ElementChecker
{
  public abstract void guaranteeThatElementInCorrectSpace(ElementProxy paramElementProxy, Element paramElement)
    throws XMLSecurityException;

  public abstract boolean isNamespaceElement(Node paramNode, String paramString1, String paramString2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.ElementChecker
 * JD-Core Version:    0.6.2
 */