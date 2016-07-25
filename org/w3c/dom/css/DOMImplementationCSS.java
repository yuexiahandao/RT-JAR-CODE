package org.w3c.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;

public abstract interface DOMImplementationCSS extends DOMImplementation
{
  public abstract CSSStyleSheet createCSSStyleSheet(String paramString1, String paramString2)
    throws DOMException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.css.DOMImplementationCSS
 * JD-Core Version:    0.6.2
 */