package javax.xml.xpath;

import javax.xml.namespace.QName;
import org.xml.sax.InputSource;

public abstract interface XPathExpression
{
  public abstract Object evaluate(Object paramObject, QName paramQName)
    throws XPathExpressionException;

  public abstract String evaluate(Object paramObject)
    throws XPathExpressionException;

  public abstract Object evaluate(InputSource paramInputSource, QName paramQName)
    throws XPathExpressionException;

  public abstract String evaluate(InputSource paramInputSource)
    throws XPathExpressionException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.xpath.XPathExpression
 * JD-Core Version:    0.6.2
 */