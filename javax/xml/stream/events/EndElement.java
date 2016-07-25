package javax.xml.stream.events;

import java.util.Iterator;
import javax.xml.namespace.QName;

public abstract interface EndElement extends XMLEvent
{
  public abstract QName getName();

  public abstract Iterator getNamespaces();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.events.EndElement
 * JD-Core Version:    0.6.2
 */