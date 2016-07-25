package com.sun.org.apache.xml.internal.serialize;

import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

public abstract interface DOMSerializer
{
  public abstract void serialize(Element paramElement)
    throws IOException;

  public abstract void serialize(Document paramDocument)
    throws IOException;

  public abstract void serialize(DocumentFragment paramDocumentFragment)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serialize.DOMSerializer
 * JD-Core Version:    0.6.2
 */