package javax.xml.xpath;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import org.xml.sax.InputSource;

public abstract interface XPath
{
  public abstract void reset();

  public abstract void setXPathVariableResolver(XPathVariableResolver paramXPathVariableResolver);

  public abstract XPathVariableResolver getXPathVariableResolver();

  public abstract void setXPathFunctionResolver(XPathFunctionResolver paramXPathFunctionResolver);

  public abstract XPathFunctionResolver getXPathFunctionResolver();

  public abstract void setNamespaceContext(NamespaceContext paramNamespaceContext);

  public abstract NamespaceContext getNamespaceContext();

  public abstract XPathExpression compile(String paramString)
    throws XPathExpressionException;

  public abstract Object evaluate(String paramString, Object paramObject, QName paramQName)
    throws XPathExpressionException;

  public abstract String evaluate(String paramString, Object paramObject)
    throws XPathExpressionException;

  public abstract Object evaluate(String paramString, InputSource paramInputSource, QName paramQName)
    throws XPathExpressionException;

  public abstract String evaluate(String paramString, InputSource paramInputSource)
    throws XPathExpressionException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.xpath.XPath
 * JD-Core Version:    0.6.2
 */