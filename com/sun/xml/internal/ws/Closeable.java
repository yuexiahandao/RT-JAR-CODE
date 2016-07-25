package com.sun.xml.internal.ws;

import javax.xml.ws.WebServiceException;

public abstract interface Closeable extends java.io.Closeable
{
  public abstract void close()
    throws WebServiceException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.Closeable
 * JD-Core Version:    0.6.2
 */