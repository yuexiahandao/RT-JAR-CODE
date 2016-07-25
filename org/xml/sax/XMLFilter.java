package org.xml.sax;

public abstract interface XMLFilter extends XMLReader
{
  public abstract void setParent(XMLReader paramXMLReader);

  public abstract XMLReader getParent();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.XMLFilter
 * JD-Core Version:    0.6.2
 */