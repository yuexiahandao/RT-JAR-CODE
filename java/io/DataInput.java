package java.io;

public abstract interface DataInput
{
  public abstract void readFully(byte[] paramArrayOfByte)
    throws IOException;

  public abstract void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;

  public abstract int skipBytes(int paramInt)
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
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.DataInput
 * JD-Core Version:    0.6.2
 */