package com.sun.org.apache.xml.internal.security.encryption;

import com.sun.org.apache.xml.internal.security.keys.KeyInfo;
import java.util.Iterator;
import org.w3c.dom.Element;

public abstract interface AgreementMethod
{
  public abstract byte[] getKANonce();

  public abstract void setKANonce(byte[] paramArrayOfByte);

  public abstract Iterator getAgreementMethodInformation();

  public abstract void addAgreementMethodInformation(Element paramElement);

  public abstract void revoveAgreementMethodInformation(Element paramElement);

  public abstract KeyInfo getOriginatorKeyInfo();

  public abstract void setOriginatorKeyInfo(KeyInfo paramKeyInfo);

  public abstract KeyInfo getRecipientKeyInfo();

  public abstract void setRecipientKeyInfo(KeyInfo paramKeyInfo);

  public abstract String getAlgorithm();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.encryption.AgreementMethod
 * JD-Core Version:    0.6.2
 */