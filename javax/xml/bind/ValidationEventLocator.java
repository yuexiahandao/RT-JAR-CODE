package javax.xml.bind;

import java.net.URL;
import org.w3c.dom.Node;

public abstract interface ValidationEventLocator
{
  public abstract URL getURL();

  public abstract int getOffset();

  public abstract int getLineNumber();

  public abstract int getColumnNumber();

  public abstract Object getObject();

  public abstract Node getNode();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.ValidationEventLocator
 * JD-Core Version:    0.6.2
 */