package sun.misc;

import java.nio.ByteBuffer;
import java.util.zip.Adler32;

public abstract interface JavaUtilZipAccess
{
  public abstract void update(Adler32 paramAdler32, ByteBuffer paramByteBuffer);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.JavaUtilZipAccess
 * JD-Core Version:    0.6.2
 */