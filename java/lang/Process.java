package java.lang;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class Process
{
  public abstract OutputStream getOutputStream();

  public abstract InputStream getInputStream();

  public abstract InputStream getErrorStream();

  public abstract int waitFor()
    throws InterruptedException;

  public abstract int exitValue();

  public abstract void destroy();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Process
 * JD-Core Version:    0.6.2
 */