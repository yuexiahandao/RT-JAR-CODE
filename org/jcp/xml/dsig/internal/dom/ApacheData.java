package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import javax.xml.crypto.Data;

public abstract interface ApacheData extends Data
{
  public abstract XMLSignatureInput getXMLSignatureInput();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.ApacheData
 * JD-Core Version:    0.6.2
 */