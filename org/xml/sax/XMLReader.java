package org.xml.sax;

import java.io.IOException;

public abstract interface XMLReader
{
  public abstract boolean getFeature(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException;

  public abstract void setFeature(String paramString, boolean paramBoolean)
    throws SAXNotRecognizedException, SAXNotSupportedException;

  public abstract Object getProperty(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException;

  public abstract void setProperty(String paramString, Object paramObject)
    throws SAXNotRecognizedException, SAXNotSupportedException;

  public abstract void setEntityResolver(EntityResolver paramEntityResolver);

  public abstract EntityResolver getEntityResolver();

  public abstract void setDTDHandler(DTDHandler paramDTDHandler);

  public abstract DTDHandler getDTDHandler();

  public abstract void setContentHandler(ContentHandler paramContentHandler);

  public abstract ContentHandler getContentHandler();

  public abstract void setErrorHandler(ErrorHandler paramErrorHandler);

  public abstract ErrorHandler getErrorHandler();

  public abstract void parse(InputSource paramInputSource)
    throws IOException, SAXException;

  public abstract void parse(String paramString)
    throws IOException, SAXException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.XMLReader
 * JD-Core Version:    0.6.2
 */