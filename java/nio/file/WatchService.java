package java.nio.file;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public abstract interface WatchService extends Closeable
{
  public abstract void close()
    throws IOException;

  public abstract WatchKey poll();

  public abstract WatchKey poll(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException;

  public abstract WatchKey take()
    throws InterruptedException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.WatchService
 * JD-Core Version:    0.6.2
 */