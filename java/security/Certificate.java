package java.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Deprecated
public abstract interface Certificate
{
  public abstract Principal getGuarantor();

  public abstract Principal getPrincipal();

  public abstract PublicKey getPublicKey();

  public abstract void encode(OutputStream paramOutputStream)
    throws KeyException, IOException;

  public abstract void decode(InputStream paramInputStream)
    throws KeyException, IOException;

  public abstract String getFormat();

  public abstract String toString(boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.Certificate
 * JD-Core Version:    0.6.2
 */