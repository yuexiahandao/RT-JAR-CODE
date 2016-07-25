package org.w3c.dom.html;

public abstract interface HTMLInputElement extends HTMLElement
{
  public abstract String getDefaultValue();

  public abstract void setDefaultValue(String paramString);

  public abstract boolean getDefaultChecked();

  public abstract void setDefaultChecked(boolean paramBoolean);

  public abstract HTMLFormElement getForm();

  public abstract String getAccept();

  public abstract void setAccept(String paramString);

  public abstract String getAccessKey();

  public abstract void setAccessKey(String paramString);

  public abstract String getAlign();

  public abstract void setAlign(String paramString);

  public abstract String getAlt();

  public abstract void setAlt(String paramString);

  public abstract boolean getChecked();

  public abstract void setChecked(boolean paramBoolean);

  public abstract boolean getDisabled();

  public abstract void setDisabled(boolean paramBoolean);

  public abstract int getMaxLength();

  public abstract void setMaxLength(int paramInt);

  public abstract String getName();

  public abstract void setName(String paramString);

  public abstract boolean getReadOnly();

  public abstract void setReadOnly(boolean paramBoolean);

  public abstract String getSize();

  public abstract void setSize(String paramString);

  public abstract String getSrc();

  public abstract void setSrc(String paramString);

  public abstract int getTabIndex();

  public abstract void setTabIndex(int paramInt);

  public abstract String getType();

  public abstract String getUseMap();

  public abstract void setUseMap(String paramString);

  public abstract String getValue();

  public abstract void setValue(String paramString);

  public abstract void blur();

  public abstract void focus();

  public abstract void select();

  public abstract void click();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.html.HTMLInputElement
 * JD-Core Version:    0.6.2
 */