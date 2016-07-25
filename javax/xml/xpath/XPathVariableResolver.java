package javax.xml.xpath;

import javax.xml.namespace.QName;

public abstract interface XPathVariableResolver
{
  public abstract Object resolveVariable(QName paramQName);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.xpath.XPathVariableResolver
 * JD-Core Version:    0.6.2
 */