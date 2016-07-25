package com.sun.org.apache.xerces.internal.xni.grammars;

public abstract interface XMLGrammarPool
{
  public abstract Grammar[] retrieveInitialGrammarSet(String paramString);

  public abstract void cacheGrammars(String paramString, Grammar[] paramArrayOfGrammar);

  public abstract Grammar retrieveGrammar(XMLGrammarDescription paramXMLGrammarDescription);

  public abstract void lockPool();

  public abstract void unlockPool();

  public abstract void clear();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
 * JD-Core Version:    0.6.2
 */