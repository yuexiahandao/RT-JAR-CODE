package org.xml.sax;

public abstract interface ErrorHandler
{
  public abstract void warning(SAXParseException paramSAXParseException)
    throws SAXException;

  public abstract void error(SAXParseException paramSAXParseException)
    throws SAXException;

  public abstract void fatalError(SAXParseException paramSAXParseException)
    throws SAXException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.ErrorHandler
 * JD-Core Version:    0.6.2
 */