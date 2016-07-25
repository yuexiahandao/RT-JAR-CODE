package com.sun.org.apache.xml.internal.security.encryption;

import java.util.Iterator;
import org.w3c.dom.Element;

public abstract interface EncryptionProperty
{
  public abstract String getTarget();

  public abstract void setTarget(String paramString);

  public abstract String getId();

  public abstract void setId(String paramString);

  public abstract String getAttribute(String paramString);

  public abstract void setAttribute(String paramString1, String paramString2);

  public abstract Iterator getEncryptionInformation();

  public abstract void addEncryptionInformation(Element paramElement);

  public abstract void removeEncryptionInformation(Element paramElement);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.encryption.EncryptionProperty
 * JD-Core Version:    0.6.2
 */