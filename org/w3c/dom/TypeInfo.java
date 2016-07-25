package org.w3c.dom;

public abstract interface TypeInfo
{
  public static final int DERIVATION_RESTRICTION = 1;
  public static final int DERIVATION_EXTENSION = 2;
  public static final int DERIVATION_UNION = 4;
  public static final int DERIVATION_LIST = 8;

  public abstract String getTypeName();

  public abstract String getTypeNamespace();

  public abstract boolean isDerivedFrom(String paramString1, String paramString2, int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.TypeInfo
 * JD-Core Version:    0.6.2
 */