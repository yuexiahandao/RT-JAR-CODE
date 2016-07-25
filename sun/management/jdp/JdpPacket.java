package sun.management.jdp;

import java.io.IOException;

public abstract interface JdpPacket
{
  public abstract byte[] getPacketData()
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.jdp.JdpPacket
 * JD-Core Version:    0.6.2
 */