package org.w3c.dom.css;

import org.w3c.dom.DOMException;

public abstract interface CSSStyleDeclaration
{
  public abstract String getCssText();

  public abstract void setCssText(String paramString)
    throws DOMException;

  public abstract String getPropertyValue(String paramString);

  public abstract CSSValue getPropertyCSSValue(String paramString);

  public abstract String removeProperty(String paramString)
    throws DOMException;

  public abstract String getPropertyPriority(String paramString);

  public abstract void setProperty(String paramString1, String paramString2, String paramString3)
    throws DOMException;

  public abstract int getLength();

  public abstract String item(int paramInt);

  public abstract CSSRule getParentRule();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.css.CSSStyleDeclaration
 * JD-Core Version:    0.6.2
 */