package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;

public abstract interface XMLEntityDescription extends XMLResourceIdentifier
{
  public abstract void setEntityName(String paramString);

  public abstract String getEntityName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XMLEntityDescription
 * JD-Core Version:    0.6.2
 */