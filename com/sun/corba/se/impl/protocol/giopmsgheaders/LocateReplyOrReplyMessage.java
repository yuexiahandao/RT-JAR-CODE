package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.spi.ior.IOR;
import org.omg.CORBA.SystemException;

public abstract interface LocateReplyOrReplyMessage extends Message
{
  public abstract int getRequestId();

  public abstract int getReplyStatus();

  public abstract SystemException getSystemException(String paramString);

  public abstract IOR getIOR();

  public abstract short getAddrDisposition();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage
 * JD-Core Version:    0.6.2
 */