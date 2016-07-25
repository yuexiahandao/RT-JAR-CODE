package javax.xml.soap;

public abstract interface Node extends org.w3c.dom.Node
{
  public abstract String getValue();

  public abstract void setValue(String paramString);

  public abstract void setParentElement(SOAPElement paramSOAPElement)
    throws SOAPException;

  public abstract SOAPElement getParentElement();

  public abstract void detachNode();

  public abstract void recycleNode();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.Node
 * JD-Core Version:    0.6.2
 */