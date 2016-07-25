package javax.xml.crypto.dsig;

import java.security.spec.AlgorithmParameterSpec;
import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.XMLStructure;

public abstract interface DigestMethod extends XMLStructure, AlgorithmMethod
{
  public static final String SHA1 = "http://www.w3.org/2000/09/xmldsig#sha1";
  public static final String SHA256 = "http://www.w3.org/2001/04/xmlenc#sha256";
  public static final String SHA512 = "http://www.w3.org/2001/04/xmlenc#sha512";
  public static final String RIPEMD160 = "http://www.w3.org/2001/04/xmlenc#ripemd160";

  public abstract AlgorithmParameterSpec getParameterSpec();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.DigestMethod
 * JD-Core Version:    0.6.2
 */