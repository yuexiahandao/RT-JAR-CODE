package sun.net;

import java.net.URL;

public abstract interface ProgressMeteringPolicy
{
  public abstract boolean shouldMeterInput(URL paramURL, String paramString);

  public abstract int getProgressUpdateThreshold();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.ProgressMeteringPolicy
 * JD-Core Version:    0.6.2
 */