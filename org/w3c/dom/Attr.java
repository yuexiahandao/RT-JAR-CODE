package org.w3c.dom;

public abstract interface Attr extends Node
{
  public abstract String getName();

  public abstract boolean getSpecified();

  public abstract String getValue();

  public abstract void setValue(String paramString)
    throws DOMException;

  public abstract Element getOwnerElement();

  public abstract TypeInfo getSchemaTypeInfo();

  public abstract boolean isId();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.Attr
 * JD-Core Version:    0.6.2
 */