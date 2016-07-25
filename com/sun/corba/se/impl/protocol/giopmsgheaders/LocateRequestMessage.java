package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.spi.ior.ObjectKey;

public abstract interface LocateRequestMessage extends Message
{
  public abstract int getRequestId();

  public abstract ObjectKey getObjectKey();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.LocateRequestMessage
 * JD-Core Version:    0.6.2
 */