package com.sun.org.apache.xml.internal.utils;

import org.w3c.dom.Node;

public abstract interface PrefixResolver
{
  public abstract String getNamespaceForPrefix(String paramString);

  public abstract String getNamespaceForPrefix(String paramString, Node paramNode);

  public abstract String getBaseIdentifier();

  public abstract boolean handlesNullPrefixes();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.PrefixResolver
 * JD-Core Version:    0.6.2
 */