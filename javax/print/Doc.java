package javax.print;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import javax.print.attribute.DocAttributeSet;

public abstract interface Doc
{
  public abstract DocFlavor getDocFlavor();

  public abstract Object getPrintData()
    throws IOException;

  public abstract DocAttributeSet getAttributes();

  public abstract Reader getReaderForText()
    throws IOException;

  public abstract InputStream getStreamForBytes()
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.Doc
 * JD-Core Version:    0.6.2
 */