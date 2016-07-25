package javax.xml.transform.dom;

import javax.xml.transform.SourceLocator;
import org.w3c.dom.Node;

public abstract interface DOMLocator extends SourceLocator
{
  public abstract Node getOriginatingNode();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.transform.dom.DOMLocator
 * JD-Core Version:    0.6.2
 */