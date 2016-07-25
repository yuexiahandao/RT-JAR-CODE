package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import org.omg.CORBA.Principal;

public abstract interface RequestMessage extends Message
{
  public static final byte RESPONSE_EXPECTED_BIT = 1;

  public abstract ServiceContexts getServiceContexts();

  public abstract int getRequestId();

  public abstract boolean isResponseExpected();

  public abstract byte[] getReserved();

  public abstract ObjectKey getObjectKey();

  public abstract String getOperation();

  public abstract Principal getPrincipal();

  public abstract void setThreadPoolToUse(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage
 * JD-Core Version:    0.6.2
 */