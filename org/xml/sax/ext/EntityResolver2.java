package org.xml.sax.ext;

import java.io.IOException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract interface EntityResolver2 extends EntityResolver
{
  public abstract InputSource getExternalSubset(String paramString1, String paramString2)
    throws SAXException, IOException;

  public abstract InputSource resolveEntity(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SAXException, IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.ext.EntityResolver2
 * JD-Core Version:    0.6.2
 */