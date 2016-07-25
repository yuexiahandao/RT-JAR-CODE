package org.w3c.dom.html;

public abstract interface HTMLButtonElement extends HTMLElement
{
  public abstract HTMLFormElement getForm();

  public abstract String getAccessKey();

  public abstract void setAccessKey(String paramString);

  public abstract boolean getDisabled();

  public abstract void setDisabled(boolean paramBoolean);

  public abstract String getName();

  public abstract void setName(String paramString);

  public abstract int getTabIndex();

  public abstract void setTabIndex(int paramInt);

  public abstract String getType();

  public abstract String getValue();

  public abstract void setValue(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.html.HTMLButtonElement
 * JD-Core Version:    0.6.2
 */