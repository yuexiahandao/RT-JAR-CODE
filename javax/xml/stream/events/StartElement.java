package javax.xml.stream.events;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

public abstract interface StartElement extends XMLEvent
{
  public abstract QName getName();

  public abstract Iterator getAttributes();

  public abstract Iterator getNamespaces();

  public abstract Attribute getAttributeByName(QName paramQName);

  public abstract NamespaceContext getNamespaceContext();

  public abstract String getNamespaceURI(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.events.StartElement
 * JD-Core Version:    0.6.2
 */