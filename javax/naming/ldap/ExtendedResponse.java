package javax.naming.ldap;

import java.io.Serializable;

public abstract interface ExtendedResponse extends Serializable
{
  public abstract String getID();

  public abstract byte[] getEncodedValue();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.ExtendedResponse
 * JD-Core Version:    0.6.2
 */