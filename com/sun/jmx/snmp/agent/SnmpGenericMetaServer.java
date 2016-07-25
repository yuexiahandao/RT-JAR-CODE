package com.sun.jmx.snmp.agent;

import com.sun.jmx.snmp.SnmpStatusException;
import com.sun.jmx.snmp.SnmpValue;

public abstract interface SnmpGenericMetaServer
{
  public abstract Object buildAttributeValue(long paramLong, SnmpValue paramSnmpValue)
    throws SnmpStatusException;

  public abstract SnmpValue buildSnmpValue(long paramLong, Object paramObject)
    throws SnmpStatusException;

  public abstract String getAttributeName(long paramLong)
    throws SnmpStatusException;

  public abstract void checkSetAccess(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
    throws SnmpStatusException;

  public abstract void checkGetAccess(long paramLong, Object paramObject)
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpGenericMetaServer
 * JD-Core Version:    0.6.2
 */