package javax.imageio.stream;

import java.io.Closeable;
import java.io.DataInput;
import java.io.IOException;
import java.nio.ByteOrder;

public abstract interface ImageInputStream extends DataInput, Closeable
{
  public abstract void setByteOrder(ByteOrder paramByteOrder);

  public abstract ByteOrder getByteOrder();

  public abstract int read()
    throws IOException;

  public abstract int read(byte[] paramArrayOfByte)
    throws IOException;

  public abstract int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void readBytes(IIOByteBuffer paramIIOByteBuffer, int paramInt)
    throws IOException;

  public abstract boolean readBoolean()
    throws IOException;

  public abstract byte readByte()
    throws IOException;

  public abstract int readUnsignedByte()
    throws IOException;

  public abstract short readShort()
    throws IOException;

  public abstract int readUnsignedShort()
    throws IOException;

  public abstract char readChar()
    throws IOException;

  public abstract int readInt()
    throws IOException;

  public abstract long readUnsignedInt()
    throws IOException;

  public abstract long readLong()
    throws IOException;

  public abstract float readFloat()
    throws IOException;

  public abstract double readDouble()
    throws IOException;

  public abstract String readLine()
    throws IOException;

  public abstract String readUTF()
    throws IOException;

  public abstract void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void readFully(byte[] paramArrayOfByte)
    throws IOException;

  public abstract void readFully(short[] paramArrayOfShort, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void readFully(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void readFully(int[] paramArrayOfInt, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void readFully(long[] paramArrayOfLong, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void readFully(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void readFully(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
    throws IOException;

  public abstract long getStreamPosition()
    throws IOException;

  public abstract int getBitOffset()
    throws IOException;

  public abstract void setBitOffset(int paramInt)
    throws IOException;

  public abstract int readBit()
    throws IOException;

  public abstract long readBits(int paramInt)
    throws IOException;

  public abstract long length()
    throws IOException;

  public abstract int skipBytes(int paramInt)
    throws IOException;

  public abstract long skipBytes(long paramLong)
    throws IOException;

  public abstract void seek(long paramLong)
    throws IOException;

  public abstract void mark();

  public abstract void reset()
    throws IOException;

  public abstract void flushBefore(long paramLong)
    throws IOException;

  public abstract void flush()
    throws IOException;

  public abstract long getFlushedPosition();

  public abstract boolean isCached();

  public abstract boolean isCachedMemory();

  public abstract boolean isCachedFile();

  public abstract void close()
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.stream.ImageInputStream
 * JD-Core Version:    0.6.2
 */