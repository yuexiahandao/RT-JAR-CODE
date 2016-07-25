package com.sun.jmx.snmp;

public abstract interface SnmpSecurityParameters
{
  public abstract int encode(byte[] paramArrayOfByte)
    throws SnmpTooBigException;

  public abstract void decode(byte[] paramArrayOfByte)
    throws SnmpStatusException;

  public abstract String getPrincipal();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpSecurityParameters
 * JD-Core Version:    0.6.2
 */