package org.w3c.dom.ls;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract interface LSParser
{
  public static final short ACTION_APPEND_AS_CHILDREN = 1;
  public static final short ACTION_REPLACE_CHILDREN = 2;
  public static final short ACTION_INSERT_BEFORE = 3;
  public static final short ACTION_INSERT_AFTER = 4;
  public static final short ACTION_REPLACE = 5;

  public abstract DOMConfiguration getDomConfig();

  public abstract LSParserFilter getFilter();

  public abstract void setFilter(LSParserFilter paramLSParserFilter);

  public abstract boolean getAsync();

  public abstract boolean getBusy();

  public abstract Document parse(LSInput paramLSInput)
    throws DOMException, LSException;

  public abstract Document parseURI(String paramString)
    throws DOMException, LSException;

  public abstract Node parseWithContext(LSInput paramLSInput, Node paramNode, short paramShort)
    throws DOMException, LSException;

  public abstract void abort();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.ls.LSParser
 * JD-Core Version:    0.6.2
 */