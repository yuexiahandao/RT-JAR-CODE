package com.sun.security.sasl.digest;

import javax.security.sasl.SaslException;

abstract interface SecurityCtx
{
  public abstract byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SaslException;

  public abstract byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SaslException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.digest.SecurityCtx
 * JD-Core Version:    0.6.2
 */