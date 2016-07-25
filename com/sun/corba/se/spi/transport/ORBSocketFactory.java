package com.sun.corba.se.spi.transport;

import com.sun.corba.se.pept.transport.Acceptor;
import com.sun.corba.se.spi.orb.ORB;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public abstract interface ORBSocketFactory
{
  public abstract void setORB(ORB paramORB);

  public abstract ServerSocket createServerSocket(String paramString, InetSocketAddress paramInetSocketAddress)
    throws IOException;

  public abstract Socket createSocket(String paramString, InetSocketAddress paramInetSocketAddress)
    throws IOException;

  public abstract void setAcceptedSocketOptions(Acceptor paramAcceptor, ServerSocket paramServerSocket, Socket paramSocket)
    throws SocketException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.transport.ORBSocketFactory
 * JD-Core Version:    0.6.2
 */