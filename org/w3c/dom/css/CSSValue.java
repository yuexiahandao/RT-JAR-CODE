package org.w3c.dom.css;

import org.w3c.dom.DOMException;

public abstract interface CSSValue
{
  public static final short CSS_INHERIT = 0;
  public static final short CSS_PRIMITIVE_VALUE = 1;
  public static final short CSS_VALUE_LIST = 2;
  public static final short CSS_CUSTOM = 3;

  public abstract String getCssText();

  public abstract void setCssText(String paramString)
    throws DOMException;

  public abstract short getCssValueType();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.css.CSSValue
 * JD-Core Version:    0.6.2
 */