package javax.naming.ldap;

import javax.naming.NamingException;

public abstract interface UnsolicitedNotification extends ExtendedResponse, HasControls
{
  public abstract String[] getReferrals();

  public abstract NamingException getException();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.UnsolicitedNotification
 * JD-Core Version:    0.6.2
 */