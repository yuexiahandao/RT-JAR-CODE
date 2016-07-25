package java.io;

public abstract interface ObjectOutput extends DataOutput, AutoCloseable
{
  public abstract void writeObject(Object paramObject)
    throws IOException;

  public abstract void write(int paramInt)
    throws IOException;

  public abstract void write(byte[] paramArrayOfByte)
    throws IOException;

  public abstract void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void flush()
    throws IOException;

  public abstract void close()
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.ObjectOutput
 * JD-Core Version:    0.6.2
 */