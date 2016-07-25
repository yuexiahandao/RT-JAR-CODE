package java.io;

public abstract interface ObjectInput extends DataInput, AutoCloseable
{
  public abstract Object readObject()
    throws ClassNotFoundException, IOException;

  public abstract int read()
    throws IOException;

  public abstract int read(byte[] paramArrayOfByte)
    throws IOException;

  public abstract int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;

  public abstract long skip(long paramLong)
    throws IOException;

  public abstract int available()
    throws IOException;

  public abstract void close()
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.ObjectInput
 * JD-Core Version:    0.6.2
 */