package org.w3c.dom.stylesheets;

import org.w3c.dom.DOMException;

public abstract interface MediaList
{
  public abstract String getMediaText();

  public abstract void setMediaText(String paramString)
    throws DOMException;

  public abstract int getLength();

  public abstract String item(int paramInt);

  public abstract void deleteMedium(String paramString)
    throws DOMException;

  public abstract void appendMedium(String paramString)
    throws DOMException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.stylesheets.MediaList
 * JD-Core Version:    0.6.2
 */