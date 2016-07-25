package org.w3c.dom.css;

import org.w3c.dom.DOMException;

public abstract interface CSSRule
{
  public static final short UNKNOWN_RULE = 0;
  public static final short STYLE_RULE = 1;
  public static final short CHARSET_RULE = 2;
  public static final short IMPORT_RULE = 3;
  public static final short MEDIA_RULE = 4;
  public static final short FONT_FACE_RULE = 5;
  public static final short PAGE_RULE = 6;

  public abstract short getType();

  public abstract String getCssText();

  public abstract void setCssText(String paramString)
    throws DOMException;

  public abstract CSSStyleSheet getParentStyleSheet();

  public abstract CSSRule getParentRule();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.css.CSSRule
 * JD-Core Version:    0.6.2
 */