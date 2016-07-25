package javax.xml.crypto.dsig;

import java.io.InputStream;
import java.util.List;
import javax.xml.crypto.XMLStructure;

public abstract interface SignedInfo extends XMLStructure
{
  public abstract CanonicalizationMethod getCanonicalizationMethod();

  public abstract SignatureMethod getSignatureMethod();

  public abstract List getReferences();

  public abstract String getId();

  public abstract InputStream getCanonicalizedData();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.SignedInfo
 * JD-Core Version:    0.6.2
 */