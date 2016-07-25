package com.sun.xml.internal.ws.wsdl;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.server.SDDocument;

public abstract interface SDDocumentResolver
{
  @Nullable
  public abstract SDDocument resolve(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.SDDocumentResolver
 * JD-Core Version:    0.6.2
 */