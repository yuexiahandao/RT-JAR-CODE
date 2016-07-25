package javax.xml.crypto.dsig.keyinfo;

import java.math.BigInteger;
import javax.xml.crypto.XMLStructure;

public abstract interface X509IssuerSerial extends XMLStructure
{
  public abstract String getIssuerName();

  public abstract BigInteger getSerialNumber();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.keyinfo.X509IssuerSerial
 * JD-Core Version:    0.6.2
 */