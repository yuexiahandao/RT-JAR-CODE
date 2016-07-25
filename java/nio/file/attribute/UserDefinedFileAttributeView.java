package java.nio.file.attribute;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public abstract interface UserDefinedFileAttributeView extends FileAttributeView
{
  public abstract String name();

  public abstract List<String> list()
    throws IOException;

  public abstract int size(String paramString)
    throws IOException;

  public abstract int read(String paramString, ByteBuffer paramByteBuffer)
    throws IOException;

  public abstract int write(String paramString, ByteBuffer paramByteBuffer)
    throws IOException;

  public abstract void delete(String paramString)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.attribute.UserDefinedFileAttributeView
 * JD-Core Version:    0.6.2
 */