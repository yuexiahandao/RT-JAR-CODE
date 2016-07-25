package org.w3c.dom.html;

import org.w3c.dom.Element;

public abstract interface HTMLElement extends Element
{
  public abstract String getId();

  public abstract void setId(String paramString);

  public abstract String getTitle();

  public abstract void setTitle(String paramString);

  public abstract String getLang();

  public abstract void setLang(String paramString);

  public abstract String getDir();

  public abstract void setDir(String paramString);

  public abstract String getClassName();

  public abstract void setClassName(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.html.HTMLElement
 * JD-Core Version:    0.6.2
 */