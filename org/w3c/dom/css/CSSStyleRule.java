package org.w3c.dom.css;

import org.w3c.dom.DOMException;

public abstract interface CSSStyleRule extends CSSRule
{
  public abstract String getSelectorText();

  public abstract void setSelectorText(String paramString)
    throws DOMException;

  public abstract CSSStyleDeclaration getStyle();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.css.CSSStyleRule
 * JD-Core Version:    0.6.2
 */