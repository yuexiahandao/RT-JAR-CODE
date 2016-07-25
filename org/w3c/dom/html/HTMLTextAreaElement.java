package org.w3c.dom.html;

public abstract interface HTMLTextAreaElement extends HTMLElement
{
  public abstract String getDefaultValue();

  public abstract void setDefaultValue(String paramString);

  public abstract HTMLFormElement getForm();

  public abstract String getAccessKey();

  public abstract void setAccessKey(String paramString);

  public abstract int getCols();

  public abstract void setCols(int paramInt);

  public abstract boolean getDisabled();

  public abstract void setDisabled(boolean paramBoolean);

  public abstract String getName();

  public abstract void setName(String paramString);

  public abstract boolean getReadOnly();

  public abstract void setReadOnly(boolean paramBoolean);

  public abstract int getRows();

  public abstract void setRows(int paramInt);

  public abstract int getTabIndex();

  public abstract void setTabIndex(int paramInt);

  public abstract String getType();

  public abstract String getValue();

  public abstract void setValue(String paramString);

  public abstract void blur();

  public abstract void focus();

  public abstract void select();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.html.HTMLTextAreaElement
 * JD-Core Version:    0.6.2
 */