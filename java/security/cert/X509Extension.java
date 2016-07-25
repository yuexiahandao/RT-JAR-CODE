package java.security.cert;

import java.util.Set;

public abstract interface X509Extension
{
  public abstract boolean hasUnsupportedCriticalExtension();

  public abstract Set<String> getCriticalExtensionOIDs();

  public abstract Set<String> getNonCriticalExtensionOIDs();

  public abstract byte[] getExtensionValue(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.X509Extension
 * JD-Core Version:    0.6.2
 */