package sun.net;

import java.util.EventListener;

public abstract interface ProgressListener extends EventListener
{
  public abstract void progressStart(ProgressEvent paramProgressEvent);

  public abstract void progressUpdate(ProgressEvent paramProgressEvent);

  public abstract void progressFinish(ProgressEvent paramProgressEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.ProgressListener
 * JD-Core Version:    0.6.2
 */