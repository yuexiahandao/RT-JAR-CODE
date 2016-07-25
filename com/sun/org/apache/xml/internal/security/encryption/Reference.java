package com.sun.org.apache.xml.internal.security.encryption;

import java.util.Iterator;
import org.w3c.dom.Element;

public abstract interface Reference
{
  public abstract String getURI();

  public abstract void setURI(String paramString);

  public abstract Iterator getElementRetrievalInformation();

  public abstract void addElementRetrievalInformation(Element paramElement);

  public abstract void removeElementRetrievalInformation(Element paramElement);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.encryption.Reference
 * JD-Core Version:    0.6.2
 */