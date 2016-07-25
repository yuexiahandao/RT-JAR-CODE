package com.sun.org.apache.xml.internal.resolver.readers;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public abstract interface CatalogReader
{
  public abstract void readCatalog(Catalog paramCatalog, String paramString)
    throws MalformedURLException, IOException, CatalogException;

  public abstract void readCatalog(Catalog paramCatalog, InputStream paramInputStream)
    throws IOException, CatalogException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.readers.CatalogReader
 * JD-Core Version:    0.6.2
 */