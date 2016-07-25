package com.sun.org.apache.xerces.internal.xni.parser;

import com.sun.org.apache.xerces.internal.xni.XNIException;
import java.io.IOException;

public abstract interface XMLPullParserConfiguration extends XMLParserConfiguration
{
  public abstract void setInputSource(XMLInputSource paramXMLInputSource)
    throws XMLConfigurationException, IOException;

  public abstract boolean parse(boolean paramBoolean)
    throws XNIException, IOException;

  public abstract void cleanup();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xni.parser.XMLPullParserConfiguration
 * JD-Core Version:    0.6.2
 */