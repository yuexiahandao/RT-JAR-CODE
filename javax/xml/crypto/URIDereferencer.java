package javax.xml.crypto;

public abstract interface URIDereferencer
{
  public abstract Data dereference(URIReference paramURIReference, XMLCryptoContext paramXMLCryptoContext)
    throws URIReferenceException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.URIDereferencer
 * JD-Core Version:    0.6.2
 */