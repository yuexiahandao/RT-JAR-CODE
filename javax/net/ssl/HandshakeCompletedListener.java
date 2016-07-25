package javax.net.ssl;

import java.util.EventListener;

public abstract interface HandshakeCompletedListener extends EventListener
{
  public abstract void handshakeCompleted(HandshakeCompletedEvent paramHandshakeCompletedEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.HandshakeCompletedListener
 * JD-Core Version:    0.6.2
 */