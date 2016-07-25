package sun.nio.fs;

import java.io.IOException;
import java.util.Map;

abstract interface DynamicFileAttributeView
{
  public abstract void setAttribute(String paramString, Object paramObject)
    throws IOException;

  public abstract Map<String, Object> readAttributes(String[] paramArrayOfString)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.DynamicFileAttributeView
 * JD-Core Version:    0.6.2
 */