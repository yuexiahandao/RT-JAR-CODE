package java.nio.file;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

public abstract interface DirectoryStream<T> extends Closeable, Iterable<T>
{
  public abstract Iterator<T> iterator();

  public static abstract interface Filter<T>
  {
    public abstract boolean accept(T paramT)
      throws IOException;
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.DirectoryStream
 * JD-Core Version:    0.6.2
 */