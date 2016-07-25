package javax.xml.crypto;

public abstract interface XMLCryptoContext
{
  public abstract String getBaseURI();

  public abstract void setBaseURI(String paramString);

  public abstract KeySelector getKeySelector();

  public abstract void setKeySelector(KeySelector paramKeySelector);

  public abstract URIDereferencer getURIDereferencer();

  public abstract void setURIDereferencer(URIDereferencer paramURIDereferencer);

  public abstract String getNamespacePrefix(String paramString1, String paramString2);

  public abstract String putNamespacePrefix(String paramString1, String paramString2);

  public abstract String getDefaultNamespacePrefix();

  public abstract void setDefaultNamespacePrefix(String paramString);

  public abstract Object setProperty(String paramString, Object paramObject);

  public abstract Object getProperty(String paramString);

  public abstract Object get(Object paramObject);

  public abstract Object put(Object paramObject1, Object paramObject2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.XMLCryptoContext
 * JD-Core Version:    0.6.2
 */