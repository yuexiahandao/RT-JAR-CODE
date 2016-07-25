package javax.naming.ldap;

import javax.naming.NamingException;

public abstract interface HasControls
{
  public abstract Control[] getControls()
    throws NamingException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.HasControls
 * JD-Core Version:    0.6.2
 */