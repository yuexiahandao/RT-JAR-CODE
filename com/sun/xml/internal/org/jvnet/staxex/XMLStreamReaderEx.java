package com.sun.xml.internal.org.jvnet.staxex;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public abstract interface XMLStreamReaderEx extends XMLStreamReader
{
  public abstract CharSequence getPCDATA()
    throws XMLStreamException;

  public abstract NamespaceContextEx getNamespaceContext();

  public abstract String getElementTextTrim()
    throws XMLStreamException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx
 * JD-Core Version:    0.6.2
 */