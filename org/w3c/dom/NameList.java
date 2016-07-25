package org.w3c.dom;

public abstract interface NameList
{
  public abstract String getName(int paramInt);

  public abstract String getNamespaceURI(int paramInt);

  public abstract int getLength();

  public abstract boolean contains(String paramString);

  public abstract boolean containsNS(String paramString1, String paramString2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.NameList
 * JD-Core Version:    0.6.2
 */