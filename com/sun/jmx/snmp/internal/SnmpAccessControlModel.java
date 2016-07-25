package com.sun.jmx.snmp.internal;

import com.sun.jmx.snmp.SnmpOid;
import com.sun.jmx.snmp.SnmpPdu;
import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface SnmpAccessControlModel extends SnmpModel
{
  public abstract void checkAccess(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, SnmpOid paramSnmpOid)
    throws SnmpStatusException;

  public abstract void checkPduAccess(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, SnmpPdu paramSnmpPdu)
    throws SnmpStatusException;

  public abstract boolean enableSnmpV1V2SetRequest();

  public abstract boolean disableSnmpV1V2SetRequest();

  public abstract boolean isSnmpV1V2SetRequestAuthorized();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.internal.SnmpAccessControlModel
 * JD-Core Version:    0.6.2
 */