package com.sun.jmx.snmp;

public abstract interface SnmpPduRequestType extends SnmpAckPdu
{
  public abstract void setErrorIndex(int paramInt);

  public abstract void setErrorStatus(int paramInt);

  public abstract int getErrorIndex();

  public abstract int getErrorStatus();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpPduRequestType
 * JD-Core Version:    0.6.2
 */