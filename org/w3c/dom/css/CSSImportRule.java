package org.w3c.dom.css;

import org.w3c.dom.stylesheets.MediaList;

public abstract interface CSSImportRule extends CSSRule
{
  public abstract String getHref();

  public abstract MediaList getMedia();

  public abstract CSSStyleSheet getStyleSheet();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.css.CSSImportRule
 * JD-Core Version:    0.6.2
 */