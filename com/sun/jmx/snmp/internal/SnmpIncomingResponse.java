package com.sun.jmx.snmp.internal;

import com.sun.jmx.snmp.SnmpMsg;
import com.sun.jmx.snmp.SnmpPdu;
import com.sun.jmx.snmp.SnmpSecurityException;
import com.sun.jmx.snmp.SnmpSecurityParameters;
import com.sun.jmx.snmp.SnmpStatusException;
import java.net.InetAddress;

public abstract interface SnmpIncomingResponse
{
  public abstract InetAddress getAddress();

  public abstract int getPort();

  public abstract SnmpSecurityParameters getSecurityParameters();

  public abstract void setSecurityCache(SnmpSecurityCache paramSnmpSecurityCache);

  public abstract int getSecurityLevel();

  public abstract int getSecurityModel();

  public abstract byte[] getContextName();

  public abstract SnmpMsg decodeMessage(byte[] paramArrayOfByte, int paramInt1, InetAddress paramInetAddress, int paramInt2)
    throws SnmpStatusException, SnmpSecurityException;

  public abstract SnmpPdu decodeSnmpPdu()
    throws SnmpStatusException;

  public abstract int getRequestId(byte[] paramArrayOfByte)
    throws SnmpStatusException;

  public abstract String printMessage();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.internal.SnmpIncomingResponse
 * JD-Core Version:    0.6.2
 */