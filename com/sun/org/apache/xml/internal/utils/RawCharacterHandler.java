package com.sun.org.apache.xml.internal.utils;

import javax.xml.transform.TransformerException;

public abstract interface RawCharacterHandler
{
  public abstract void charactersRaw(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws TransformerException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.RawCharacterHandler
 * JD-Core Version:    0.6.2
 */