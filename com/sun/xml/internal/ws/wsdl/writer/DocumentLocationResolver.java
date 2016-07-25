package com.sun.xml.internal.ws.wsdl.writer;

import com.sun.istack.internal.Nullable;

public abstract interface DocumentLocationResolver
{
  @Nullable
  public abstract String getLocationFor(String paramString1, String paramString2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.writer.DocumentLocationResolver
 * JD-Core Version:    0.6.2
 */