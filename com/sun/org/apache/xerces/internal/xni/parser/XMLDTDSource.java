package com.sun.org.apache.xerces.internal.xni.parser;

import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;

public abstract interface XMLDTDSource
{
  public abstract void setDTDHandler(XMLDTDHandler paramXMLDTDHandler);

  public abstract XMLDTDHandler getDTDHandler();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource
 * JD-Core Version:    0.6.2
 */