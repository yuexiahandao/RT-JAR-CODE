package org.w3c.dom.html;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public abstract interface HTMLDocument extends Document
{
  public abstract String getTitle();

  public abstract void setTitle(String paramString);

  public abstract String getReferrer();

  public abstract String getDomain();

  public abstract String getURL();

  public abstract HTMLElement getBody();

  public abstract void setBody(HTMLElement paramHTMLElement);

  public abstract HTMLCollection getImages();

  public abstract HTMLCollection getApplets();

  public abstract HTMLCollection getLinks();

  public abstract HTMLCollection getForms();

  public abstract HTMLCollection getAnchors();

  public abstract String getCookie();

  public abstract void setCookie(String paramString);

  public abstract void open();

  public abstract void close();

  public abstract void write(String paramString);

  public abstract void writeln(String paramString);

  public abstract NodeList getElementsByName(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.html.HTMLDocument
 * JD-Core Version:    0.6.2
 */