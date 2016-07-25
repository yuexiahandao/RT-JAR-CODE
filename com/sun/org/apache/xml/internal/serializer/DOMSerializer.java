package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import org.w3c.dom.Node;

public abstract interface DOMSerializer
{
  public abstract void serialize(Node paramNode)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.DOMSerializer
 * JD-Core Version:    0.6.2
 */