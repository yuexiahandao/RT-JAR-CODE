package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract interface ReadableByteChannel extends Channel
{
  public abstract int read(ByteBuffer paramByteBuffer)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.ReadableByteChannel
 * JD-Core Version:    0.6.2
 */