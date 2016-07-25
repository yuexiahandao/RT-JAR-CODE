package javax.xml.crypto.dsig;

import java.io.OutputStream;
import java.security.spec.AlgorithmParameterSpec;
import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.Data;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;

public abstract interface Transform extends XMLStructure, AlgorithmMethod
{
  public static final String BASE64 = "http://www.w3.org/2000/09/xmldsig#base64";
  public static final String ENVELOPED = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
  public static final String XPATH = "http://www.w3.org/TR/1999/REC-xpath-19991116";
  public static final String XPATH2 = "http://www.w3.org/2002/06/xmldsig-filter2";
  public static final String XSLT = "http://www.w3.org/TR/1999/REC-xslt-19991116";

  public abstract AlgorithmParameterSpec getParameterSpec();

  public abstract Data transform(Data paramData, XMLCryptoContext paramXMLCryptoContext)
    throws TransformException;

  public abstract Data transform(Data paramData, XMLCryptoContext paramXMLCryptoContext, OutputStream paramOutputStream)
    throws TransformException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.Transform
 * JD-Core Version:    0.6.2
 */