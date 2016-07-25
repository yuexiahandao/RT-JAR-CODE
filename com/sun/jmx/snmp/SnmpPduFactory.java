package com.sun.jmx.snmp;

public abstract interface SnmpPduFactory
{
  public abstract SnmpPdu decodeSnmpPdu(SnmpMsg paramSnmpMsg)
    throws SnmpStatusException;

  public abstract SnmpMsg encodeSnmpPdu(SnmpPdu paramSnmpPdu, int paramInt)
    throws SnmpStatusException, SnmpTooBigException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpPduFactory
 * JD-Core Version:    0.6.2
 */