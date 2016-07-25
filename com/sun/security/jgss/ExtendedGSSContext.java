package com.sun.security.jgss;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;

public abstract interface ExtendedGSSContext extends GSSContext
{
  public abstract Object inquireSecContext(InquireType paramInquireType)
    throws GSSException;

  public abstract void requestDelegPolicy(boolean paramBoolean)
    throws GSSException;

  public abstract boolean getDelegPolicyState();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.jgss.ExtendedGSSContext
 * JD-Core Version:    0.6.2
 */