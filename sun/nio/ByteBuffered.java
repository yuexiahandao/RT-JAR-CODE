package sun.nio;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract interface ByteBuffered
{
  public abstract ByteBuffer getByteBuffer()
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ByteBuffered
 * JD-Core Version:    0.6.2
 */