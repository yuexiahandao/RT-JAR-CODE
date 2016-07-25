package java.rmi.server;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StreamCorruptedException;

@Deprecated
public abstract interface RemoteCall
{
  @Deprecated
  public abstract ObjectOutput getOutputStream()
    throws IOException;

  @Deprecated
  public abstract void releaseOutputStream()
    throws IOException;

  @Deprecated
  public abstract ObjectInput getInputStream()
    throws IOException;

  @Deprecated
  public abstract void releaseInputStream()
    throws IOException;

  @Deprecated
  public abstract ObjectOutput getResultStream(boolean paramBoolean)
    throws IOException, StreamCorruptedException;

  @Deprecated
  public abstract void executeCall()
    throws Exception;

  @Deprecated
  public abstract void done()
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.server.RemoteCall
 * JD-Core Version:    0.6.2
 */