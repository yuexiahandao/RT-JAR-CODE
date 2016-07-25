package com.sun.corba.se.spi.legacy.connection;

import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.transport.SocketInfo;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.omg.CORBA.ORB;

public abstract interface ORBSocketFactory
{
  public static final String IIOP_CLEAR_TEXT = "IIOP_CLEAR_TEXT";

  public abstract ServerSocket createServerSocket(String paramString, int paramInt)
    throws IOException;

  public abstract SocketInfo getEndPointInfo(ORB paramORB, IOR paramIOR, SocketInfo paramSocketInfo);

  public abstract Socket createSocket(SocketInfo paramSocketInfo)
    throws IOException, GetEndPointInfoAgainException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.legacy.connection.ORBSocketFactory
 * JD-Core Version:    0.6.2
 */