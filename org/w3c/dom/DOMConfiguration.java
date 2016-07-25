package org.w3c.dom;

public abstract interface DOMConfiguration
{
  public abstract void setParameter(String paramString, Object paramObject)
    throws DOMException;

  public abstract Object getParameter(String paramString)
    throws DOMException;

  public abstract boolean canSetParameter(String paramString, Object paramObject);

  public abstract DOMStringList getParameterNames();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.DOMConfiguration
 * JD-Core Version:    0.6.2
 */