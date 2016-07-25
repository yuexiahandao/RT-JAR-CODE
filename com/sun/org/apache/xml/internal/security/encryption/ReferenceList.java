package com.sun.org.apache.xml.internal.security.encryption;

import java.util.Iterator;

public abstract interface ReferenceList
{
  public static final int DATA_REFERENCE = 1;
  public static final int KEY_REFERENCE = 2;

  public abstract void add(Reference paramReference);

  public abstract void remove(Reference paramReference);

  public abstract int size();

  public abstract boolean isEmpty();

  public abstract Iterator getReferences();

  public abstract Reference newDataReference(String paramString);

  public abstract Reference newKeyReference(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.encryption.ReferenceList
 * JD-Core Version:    0.6.2
 */