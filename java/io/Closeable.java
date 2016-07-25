package java.io;

public abstract interface Closeable extends AutoCloseable
{
  public abstract void close()
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.Closeable
 * JD-Core Version:    0.6.2
 */