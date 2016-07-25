package com.sun.corba.se.spi.transport;

import com.sun.corba.se.spi.ior.IOR;
import java.util.List;

public abstract interface IORToSocketInfo
{
  public abstract List getSocketInfo(IOR paramIOR);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.transport.IORToSocketInfo
 * JD-Core Version:    0.6.2
 */