package sun.security.util;

import java.io.IOException;
import java.io.OutputStream;

public abstract interface DerEncoder
{
  public abstract void derEncode(OutputStream paramOutputStream)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.DerEncoder
 * JD-Core Version:    0.6.2
 */