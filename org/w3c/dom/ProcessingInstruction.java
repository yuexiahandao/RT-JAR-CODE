package org.w3c.dom;

public abstract interface ProcessingInstruction extends Node
{
  public abstract String getTarget();

  public abstract String getData();

  public abstract void setData(String paramString)
    throws DOMException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.ProcessingInstruction
 * JD-Core Version:    0.6.2
 */