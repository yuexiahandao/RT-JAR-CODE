package com.sun.jmx.snmp.agent;

import com.sun.jmx.snmp.SnmpPdu;
import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface SnmpUserDataFactory
{
  public abstract Object allocateUserData(SnmpPdu paramSnmpPdu)
    throws SnmpStatusException;

  public abstract void releaseUserData(Object paramObject, SnmpPdu paramSnmpPdu)
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpUserDataFactory
 * JD-Core Version:    0.6.2
 */