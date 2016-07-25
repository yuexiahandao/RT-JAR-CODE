package com.sun.org.apache.xml.internal.resolver.readers;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import org.w3c.dom.Node;

public abstract interface DOMCatalogParser
{
  public abstract void parseCatalogEntry(Catalog paramCatalog, Node paramNode);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.readers.DOMCatalogParser
 * JD-Core Version:    0.6.2
 */