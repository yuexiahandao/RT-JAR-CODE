package org.ietf.jgss;

public abstract interface GSSCredential extends Cloneable
{
  public static final int INITIATE_AND_ACCEPT = 0;
  public static final int INITIATE_ONLY = 1;
  public static final int ACCEPT_ONLY = 2;
  public static final int DEFAULT_LIFETIME = 0;
  public static final int INDEFINITE_LIFETIME = 2147483647;

  public abstract void dispose()
    throws GSSException;

  public abstract GSSName getName()
    throws GSSException;

  public abstract GSSName getName(Oid paramOid)
    throws GSSException;

  public abstract int getRemainingLifetime()
    throws GSSException;

  public abstract int getRemainingInitLifetime(Oid paramOid)
    throws GSSException;

  public abstract int getRemainingAcceptLifetime(Oid paramOid)
    throws GSSException;

  public abstract int getUsage()
    throws GSSException;

  public abstract int getUsage(Oid paramOid)
    throws GSSException;

  public abstract Oid[] getMechs()
    throws GSSException;

  public abstract void add(GSSName paramGSSName, int paramInt1, int paramInt2, Oid paramOid, int paramInt3)
    throws GSSException;

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.ietf.jgss.GSSCredential
 * JD-Core Version:    0.6.2
 */