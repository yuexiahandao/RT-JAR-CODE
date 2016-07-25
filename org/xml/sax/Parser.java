package org.xml.sax;

import java.io.IOException;
import java.util.Locale;

/** @deprecated */
public abstract interface Parser
{
  public abstract void setLocale(Locale paramLocale)
    throws SAXException;

  public abstract void setEntityResolver(EntityResolver paramEntityResolver);

  public abstract void setDTDHandler(DTDHandler paramDTDHandler);

  public abstract void setDocumentHandler(DocumentHandler paramDocumentHandler);

  public abstract void setErrorHandler(ErrorHandler paramErrorHandler);

  public abstract void parse(InputSource paramInputSource)
    throws SAXException, IOException;

  public abstract void parse(String paramString)
    throws SAXException, IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.Parser
 * JD-Core Version:    0.6.2
 */