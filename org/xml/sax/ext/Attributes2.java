package org.xml.sax.ext;

import org.xml.sax.Attributes;

public abstract interface Attributes2 extends Attributes
{
  public abstract boolean isDeclared(int paramInt);

  public abstract boolean isDeclared(String paramString);

  public abstract boolean isDeclared(String paramString1, String paramString2);

  public abstract boolean isSpecified(int paramInt);

  public abstract boolean isSpecified(String paramString1, String paramString2);

  public abstract boolean isSpecified(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.ext.Attributes2
 * JD-Core Version:    0.6.2
 */