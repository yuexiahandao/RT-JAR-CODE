package org.w3c.dom;

public abstract interface Text extends CharacterData
{
  public abstract Text splitText(int paramInt)
    throws DOMException;

  public abstract boolean isElementContentWhitespace();

  public abstract String getWholeText();

  public abstract Text replaceWholeText(String paramString)
    throws DOMException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.Text
 * JD-Core Version:    0.6.2
 */