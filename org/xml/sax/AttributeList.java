package org.xml.sax;

/** @deprecated */
public abstract interface AttributeList
{
  public abstract int getLength();

  public abstract String getName(int paramInt);

  public abstract String getType(int paramInt);

  public abstract String getValue(int paramInt);

  public abstract String getType(String paramString);

  public abstract String getValue(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.AttributeList
 * JD-Core Version:    0.6.2
 */