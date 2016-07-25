package javax.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

public abstract interface FileObject
{
  public abstract URI toUri();

  public abstract String getName();

  public abstract InputStream openInputStream()
    throws IOException;

  public abstract OutputStream openOutputStream()
    throws IOException;

  public abstract Reader openReader(boolean paramBoolean)
    throws IOException;

  public abstract CharSequence getCharContent(boolean paramBoolean)
    throws IOException;

  public abstract Writer openWriter()
    throws IOException;

  public abstract long getLastModified();

  public abstract boolean delete();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.tools.FileObject
 * JD-Core Version:    0.6.2
 */