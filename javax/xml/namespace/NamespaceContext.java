package javax.xml.namespace;

import java.util.Iterator;

public abstract interface NamespaceContext
{
  public abstract String getNamespaceURI(String paramString);

  public abstract String getPrefix(String paramString);

  public abstract Iterator getPrefixes(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.namespace.NamespaceContext
 * JD-Core Version:    0.6.2
 */