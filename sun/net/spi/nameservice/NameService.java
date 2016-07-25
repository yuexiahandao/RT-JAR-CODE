package sun.net.spi.nameservice;

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract interface NameService
{
  public abstract InetAddress[] lookupAllHostAddr(String paramString)
    throws UnknownHostException;

  public abstract String getHostByAddr(byte[] paramArrayOfByte)
    throws UnknownHostException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.spi.nameservice.NameService
 * JD-Core Version:    0.6.2
 */