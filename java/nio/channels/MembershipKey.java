package java.nio.channels;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;

public abstract class MembershipKey
{
  public abstract boolean isValid();

  public abstract void drop();

  public abstract MembershipKey block(InetAddress paramInetAddress)
    throws IOException;

  public abstract MembershipKey unblock(InetAddress paramInetAddress);

  public abstract MulticastChannel channel();

  public abstract InetAddress group();

  public abstract NetworkInterface networkInterface();

  public abstract InetAddress sourceAddress();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.MembershipKey
 * JD-Core Version:    0.6.2
 */