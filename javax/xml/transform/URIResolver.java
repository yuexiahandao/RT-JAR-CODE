package javax.xml.transform;

public abstract interface URIResolver
{
  public abstract Source resolve(String paramString1, String paramString2)
    throws TransformerException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.transform.URIResolver
 * JD-Core Version:    0.6.2
 */