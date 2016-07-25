package java.nio.channels;

import java.io.IOException;

public abstract interface InterruptibleChannel extends Channel
{
  public abstract void close()
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.InterruptibleChannel
 * JD-Core Version:    0.6.2
 */