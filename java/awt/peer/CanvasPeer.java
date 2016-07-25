package java.awt.peer;

import java.awt.GraphicsConfiguration;

public abstract interface CanvasPeer extends ComponentPeer
{
  public abstract GraphicsConfiguration getAppropriateGraphicsConfiguration(GraphicsConfiguration paramGraphicsConfiguration);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.peer.CanvasPeer
 * JD-Core Version:    0.6.2
 */