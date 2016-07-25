package org.w3c.dom.ls;

import org.w3c.dom.DOMException;

public abstract interface DOMImplementationLS
{
  public static final short MODE_SYNCHRONOUS = 1;
  public static final short MODE_ASYNCHRONOUS = 2;

  public abstract LSParser createLSParser(short paramShort, String paramString)
    throws DOMException;

  public abstract LSSerializer createLSSerializer();

  public abstract LSInput createLSInput();

  public abstract LSOutput createLSOutput();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.ls.DOMImplementationLS
 * JD-Core Version:    0.6.2
 */