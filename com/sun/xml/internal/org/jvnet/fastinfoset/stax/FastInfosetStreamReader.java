package com.sun.xml.internal.org.jvnet.fastinfoset.stax;

import javax.xml.stream.XMLStreamException;

public abstract interface FastInfosetStreamReader
{
  public abstract int peekNext()
    throws XMLStreamException;

  public abstract int accessNamespaceCount();

  public abstract String accessLocalName();

  public abstract String accessNamespaceURI();

  public abstract String accessPrefix();

  public abstract char[] accessTextCharacters();

  public abstract int accessTextStart();

  public abstract int accessTextLength();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.fastinfoset.stax.FastInfosetStreamReader
 * JD-Core Version:    0.6.2
 */