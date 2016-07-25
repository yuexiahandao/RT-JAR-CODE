package sun.awt;

import java.awt.Point;
import java.awt.Window;
import java.awt.peer.MouseInfoPeer;

public class DefaultMouseInfoPeer
  implements MouseInfoPeer
{
  public native int fillPointWithCoords(Point paramPoint);

  public native boolean isWindowUnderMouse(Window paramWindow);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.DefaultMouseInfoPeer
 * JD-Core Version:    0.6.2
 */