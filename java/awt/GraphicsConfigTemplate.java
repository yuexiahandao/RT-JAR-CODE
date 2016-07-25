package java.awt;

import java.io.Serializable;

public abstract class GraphicsConfigTemplate
  implements Serializable
{
  private static final long serialVersionUID = -8061369279557787079L;
  public static final int REQUIRED = 1;
  public static final int PREFERRED = 2;
  public static final int UNNECESSARY = 3;

  public abstract GraphicsConfiguration getBestConfiguration(GraphicsConfiguration[] paramArrayOfGraphicsConfiguration);

  public abstract boolean isGraphicsConfigSupported(GraphicsConfiguration paramGraphicsConfiguration);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.GraphicsConfigTemplate
 * JD-Core Version:    0.6.2
 */