package javax.xml.crypto.dsig;

import java.util.List;
import javax.xml.crypto.XMLStructure;

public abstract interface SignatureProperty extends XMLStructure
{
  public abstract String getTarget();

  public abstract String getId();

  public abstract List getContent();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.SignatureProperty
 * JD-Core Version:    0.6.2
 */