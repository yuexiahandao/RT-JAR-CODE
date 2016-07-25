package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import org.xml.sax.Attributes;

public abstract interface AttributesEx extends Attributes
{
  public abstract CharSequence getData(int paramInt);

  public abstract CharSequence getData(String paramString1, String paramString2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.AttributesEx
 * JD-Core Version:    0.6.2
 */