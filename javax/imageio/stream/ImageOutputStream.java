package javax.imageio.stream;

import java.io.DataOutput;
import java.io.IOException;

public abstract interface ImageOutputStream extends ImageInputStream, DataOutput
{
  public abstract void write(int paramInt)
    throws IOException;

  public abstract void write(byte[] paramArrayOfByte)
    throws IOException;

  public abstract void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void writeBoolean(boolean paramBoolean)
    throws IOException;

  public abstract void writeByte(int paramInt)
    throws IOException;

  public abstract void writeShort(int paramInt)
    throws IOException;

  public abstract void writeChar(int paramInt)
    throws IOException;

  public abstract void writeInt(int paramInt)
    throws IOException;

  public abstract void writeLong(long paramLong)
    throws IOException;

  public abstract void writeFloat(float paramFloat)
    throws IOException;

  public abstract void writeDouble(double paramDouble)
    throws IOException;

  public abstract void writeBytes(String paramString)
    throws IOException;

  public abstract void writeChars(String paramString)
    throws IOException;

  public abstract void writeUTF(String paramString)
    throws IOException;

  public abstract void writeShorts(short[] paramArrayOfShort, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void writeChars(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void writeInts(int[] paramArrayOfInt, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void writeLongs(long[] paramArrayOfLong, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void writeFloats(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void writeDoubles(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void writeBit(int paramInt)
    throws IOException;

  public abstract void writeBits(long paramLong, int paramInt)
    throws IOException;

  public abstract void flushBefore(long paramLong)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.stream.ImageOutputStream
 * JD-Core Version:    0.6.2
 */