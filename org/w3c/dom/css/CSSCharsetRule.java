package org.w3c.dom.css;

import org.w3c.dom.DOMException;

public abstract interface CSSCharsetRule extends CSSRule
{
  public abstract String getEncoding();

  public abstract void setEncoding(String paramString)
    throws DOMException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.css.CSSCharsetRule
 * JD-Core Version:    0.6.2
 */