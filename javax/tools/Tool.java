package javax.tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import javax.lang.model.SourceVersion;

public abstract interface Tool
{
  public abstract int run(InputStream paramInputStream, OutputStream paramOutputStream1, OutputStream paramOutputStream2, String[] paramArrayOfString);

  public abstract Set<SourceVersion> getSourceVersions();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.tools.Tool
 * JD-Core Version:    0.6.2
 */