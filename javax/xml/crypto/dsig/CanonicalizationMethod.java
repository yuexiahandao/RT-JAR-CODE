package javax.xml.crypto.dsig;

import java.security.spec.AlgorithmParameterSpec;

public abstract interface CanonicalizationMethod extends Transform
{
  public static final String INCLUSIVE = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
  public static final String INCLUSIVE_WITH_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
  public static final String EXCLUSIVE = "http://www.w3.org/2001/10/xml-exc-c14n#";
  public static final String EXCLUSIVE_WITH_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";

  public abstract AlgorithmParameterSpec getParameterSpec();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.CanonicalizationMethod
 * JD-Core Version:    0.6.2
 */