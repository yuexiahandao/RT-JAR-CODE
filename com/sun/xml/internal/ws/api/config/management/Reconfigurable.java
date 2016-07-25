package com.sun.xml.internal.ws.api.config.management;

import javax.xml.ws.WebServiceException;

public abstract interface Reconfigurable
{
  public abstract void reconfigure()
    throws WebServiceException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.config.management.Reconfigurable
 * JD-Core Version:    0.6.2
 */