package org.w3c.dom;

public abstract interface DOMLocator
{
  public abstract int getLineNumber();

  public abstract int getColumnNumber();

  public abstract int getByteOffset();

  public abstract int getUtf16Offset();

  public abstract Node getRelatedNode();

  public abstract String getUri();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.DOMLocator
 * JD-Core Version:    0.6.2
 */