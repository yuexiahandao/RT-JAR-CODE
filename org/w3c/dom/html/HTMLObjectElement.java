package org.w3c.dom.html;

import org.w3c.dom.Document;

public abstract interface HTMLObjectElement extends HTMLElement
{
  public abstract HTMLFormElement getForm();

  public abstract String getCode();

  public abstract void setCode(String paramString);

  public abstract String getAlign();

  public abstract void setAlign(String paramString);

  public abstract String getArchive();

  public abstract void setArchive(String paramString);

  public abstract String getBorder();

  public abstract void setBorder(String paramString);

  public abstract String getCodeBase();

  public abstract void setCodeBase(String paramString);

  public abstract String getCodeType();

  public abstract void setCodeType(String paramString);

  public abstract String getData();

  public abstract void setData(String paramString);

  public abstract boolean getDeclare();

  public abstract void setDeclare(boolean paramBoolean);

  public abstract String getHeight();

  public abstract void setHeight(String paramString);

  public abstract String getHspace();

  public abstract void setHspace(String paramString);

  public abstract String getName();

  public abstract void setName(String paramString);

  public abstract String getStandby();

  public abstract void setStandby(String paramString);

  public abstract int getTabIndex();

  public abstract void setTabIndex(int paramInt);

  public abstract String getType();

  public abstract void setType(String paramString);

  public abstract String getUseMap();

  public abstract void setUseMap(String paramString);

  public abstract String getVspace();

  public abstract void setVspace(String paramString);

  public abstract String getWidth();

  public abstract void setWidth(String paramString);

  public abstract Document getContentDocument();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.html.HTMLObjectElement
 * JD-Core Version:    0.6.2
 */