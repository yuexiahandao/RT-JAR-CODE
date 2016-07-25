package com.sun.xml.internal.org.jvnet.staxex;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;

public abstract interface NamespaceContextEx extends NamespaceContext, Iterable<Binding>
{
  public abstract Iterator<Binding> iterator();

  public static abstract interface Binding
  {
    public abstract String getPrefix();

    public abstract String getNamespaceURI();
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx
 * JD-Core Version:    0.6.2
 */