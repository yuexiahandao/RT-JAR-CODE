package javax.xml.crypto.dsig;

import java.security.spec.AlgorithmParameterSpec;
import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.XMLStructure;

public abstract interface SignatureMethod extends XMLStructure, AlgorithmMethod
{
  public static final String DSA_SHA1 = "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
  public static final String RSA_SHA1 = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
  public static final String HMAC_SHA1 = "http://www.w3.org/2000/09/xmldsig#hmac-sha1";

  public abstract AlgorithmParameterSpec getParameterSpec();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.SignatureMethod
 * JD-Core Version:    0.6.2
 */