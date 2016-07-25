package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;

public abstract interface ReplyMessage extends Message, LocateReplyOrReplyMessage
{
  public static final int NO_EXCEPTION = 0;
  public static final int USER_EXCEPTION = 1;
  public static final int SYSTEM_EXCEPTION = 2;
  public static final int LOCATION_FORWARD = 3;
  public static final int LOCATION_FORWARD_PERM = 4;
  public static final int NEEDS_ADDRESSING_MODE = 5;

  public abstract ServiceContexts getServiceContexts();

  public abstract void setServiceContexts(ServiceContexts paramServiceContexts);

  public abstract void setIOR(IOR paramIOR);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage
 * JD-Core Version:    0.6.2
 */