package java.awt.peer;

import java.awt.Rectangle;

public abstract interface RobotPeer
{
  public abstract void mouseMove(int paramInt1, int paramInt2);

  public abstract void mousePress(int paramInt);

  public abstract void mouseRelease(int paramInt);

  public abstract void mouseWheel(int paramInt);

  public abstract void keyPress(int paramInt);

  public abstract void keyRelease(int paramInt);

  public abstract int getRGBPixel(int paramInt1, int paramInt2);

  public abstract int[] getRGBPixels(Rectangle paramRectangle);

  public abstract void dispose();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.peer.RobotPeer
 * JD-Core Version:    0.6.2
 */