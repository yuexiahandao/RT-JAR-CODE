package java.nio.file.attribute;

import java.io.IOException;

public abstract class UserPrincipalLookupService
{
  public abstract UserPrincipal lookupPrincipalByName(String paramString)
    throws IOException;

  public abstract GroupPrincipal lookupPrincipalByGroupName(String paramString)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.attribute.UserPrincipalLookupService
 * JD-Core Version:    0.6.2
 */