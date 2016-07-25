package com.sun.org.apache.xerces.internal.impl.dv;

import java.util.Locale;

public abstract interface ValidationContext
{
  public abstract boolean needFacetChecking();

  public abstract boolean needExtraChecking();

  public abstract boolean needToNormalize();

  public abstract boolean useNamespaces();

  public abstract boolean isEntityDeclared(String paramString);

  public abstract boolean isEntityUnparsed(String paramString);

  public abstract boolean isIdDeclared(String paramString);

  public abstract void addId(String paramString);

  public abstract void addIdRef(String paramString);

  public abstract String getSymbol(String paramString);

  public abstract String getURI(String paramString);

  public abstract Locale getLocale();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
 * JD-Core Version:    0.6.2
 */