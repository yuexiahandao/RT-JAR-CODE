package com.sun.org.apache.xml.internal.serializer;

import javax.xml.transform.Transformer;
import org.w3c.dom.Node;

public abstract interface TransformStateSetter
{
  public abstract void setCurrentNode(Node paramNode);

  public abstract void resetState(Transformer paramTransformer);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.TransformStateSetter
 * JD-Core Version:    0.6.2
 */