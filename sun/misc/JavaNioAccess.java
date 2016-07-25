package sun.misc;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public abstract interface JavaNioAccess
{
  public abstract BufferPool getDirectBufferPool();

  public abstract ByteBuffer newDirectByteBuffer(long paramLong, int paramInt, Object paramObject);

  public abstract void truncate(Buffer paramBuffer);

  public static abstract interface BufferPool
  {
    public abstract String getName();

    public abstract long getCount();

    public abstract long getTotalCapacity();

    public abstract long getMemoryUsed();
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.JavaNioAccess
 * JD-Core Version:    0.6.2
 */