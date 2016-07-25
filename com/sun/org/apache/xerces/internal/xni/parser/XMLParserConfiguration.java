package com.sun.org.apache.xerces.internal.xni.parser;

import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import java.io.IOException;
import java.util.Locale;

public abstract interface XMLParserConfiguration extends XMLComponentManager
{
  public abstract void parse(XMLInputSource paramXMLInputSource)
    throws XNIException, IOException;

  public abstract void addRecognizedFeatures(String[] paramArrayOfString);

  public abstract void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException;

  public abstract boolean getFeature(String paramString)
    throws XMLConfigurationException;

  public abstract void addRecognizedProperties(String[] paramArrayOfString);

  public abstract void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException;

  public abstract Object getProperty(String paramString)
    throws XMLConfigurationException;

  public abstract void setErrorHandler(XMLErrorHandler paramXMLErrorHandler);

  public abstract XMLErrorHandler getErrorHandler();

  public abstract void setDocumentHandler(XMLDocumentHandler paramXMLDocumentHandler);

  public abstract XMLDocumentHandler getDocumentHandler();

  public abstract void setDTDHandler(XMLDTDHandler paramXMLDTDHandler);

  public abstract XMLDTDHandler getDTDHandler();

  public abstract void setDTDContentModelHandler(XMLDTDContentModelHandler paramXMLDTDContentModelHandler);

  public abstract XMLDTDContentModelHandler getDTDContentModelHandler();

  public abstract void setEntityResolver(XMLEntityResolver paramXMLEntityResolver);

  public abstract XMLEntityResolver getEntityResolver();

  public abstract void setLocale(Locale paramLocale)
    throws XNIException;

  public abstract Locale getLocale();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
 * JD-Core Version:    0.6.2
 */