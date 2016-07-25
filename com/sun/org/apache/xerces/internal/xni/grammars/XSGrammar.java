package com.sun.org.apache.xerces.internal.xni.grammars;

import com.sun.org.apache.xerces.internal.xs.XSModel;

public abstract interface XSGrammar extends Grammar
{
  public abstract XSModel toXSModel();

  public abstract XSModel toXSModel(XSGrammar[] paramArrayOfXSGrammar);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xni.grammars.XSGrammar
 * JD-Core Version:    0.6.2
 */