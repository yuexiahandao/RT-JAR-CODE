package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;

public abstract interface XSGrammarPoolContainer
{
  public abstract XMLGrammarPool getGrammarPool();

  public abstract boolean isFullyComposed();

  public abstract Boolean getFeature(String paramString);

  public abstract void setFeature(String paramString, boolean paramBoolean);

  public abstract Object getProperty(String paramString);

  public abstract void setProperty(String paramString, Object paramObject);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer
 * JD-Core Version:    0.6.2
 */