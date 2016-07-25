package org.w3c.dom.html;

import org.w3c.dom.DOMException;

public abstract interface HTMLTableElement extends HTMLElement
{
  public abstract HTMLTableCaptionElement getCaption();

  public abstract void setCaption(HTMLTableCaptionElement paramHTMLTableCaptionElement);

  public abstract HTMLTableSectionElement getTHead();

  public abstract void setTHead(HTMLTableSectionElement paramHTMLTableSectionElement);

  public abstract HTMLTableSectionElement getTFoot();

  public abstract void setTFoot(HTMLTableSectionElement paramHTMLTableSectionElement);

  public abstract HTMLCollection getRows();

  public abstract HTMLCollection getTBodies();

  public abstract String getAlign();

  public abstract void setAlign(String paramString);

  public abstract String getBgColor();

  public abstract void setBgColor(String paramString);

  public abstract String getBorder();

  public abstract void setBorder(String paramString);

  public abstract String getCellPadding();

  public abstract void setCellPadding(String paramString);

  public abstract String getCellSpacing();

  public abstract void setCellSpacing(String paramString);

  public abstract String getFrame();

  public abstract void setFrame(String paramString);

  public abstract String getRules();

  public abstract void setRules(String paramString);

  public abstract String getSummary();

  public abstract void setSummary(String paramString);

  public abstract String getWidth();

  public abstract void setWidth(String paramString);

  public abstract HTMLElement createTHead();

  public abstract void deleteTHead();

  public abstract HTMLElement createTFoot();

  public abstract void deleteTFoot();

  public abstract HTMLElement createCaption();

  public abstract void deleteCaption();

  public abstract HTMLElement insertRow(int paramInt)
    throws DOMException;

  public abstract void deleteRow(int paramInt)
    throws DOMException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.html.HTMLTableElement
 * JD-Core Version:    0.6.2
 */