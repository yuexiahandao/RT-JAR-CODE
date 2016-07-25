package sun.security.krb5.internal;

public class LoginOptions extends KDCOptions
{
  public static final int RESERVED = 0;
  public static final int FORWARDABLE = 1;
  public static final int PROXIABLE = 3;
  public static final int ALLOW_POSTDATE = 5;
  public static final int RENEWABLE = 8;
  public static final int RENEWABLE_OK = 27;
  public static final int ENC_TKT_IN_SKEY = 28;
  public static final int RENEW = 30;
  public static final int VALIDATE = 31;
  public static final int MAX = 31;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.LoginOptions
 * JD-Core Version:    0.6.2
 */