package com.sun.jmx.snmp.agent;

import com.sun.jmx.snmp.SnmpStatusException;
import com.sun.jmx.snmp.SnmpValue;

public abstract interface SnmpStandardMetaServer
{
  public abstract SnmpValue get(long paramLong, Object paramObject)
    throws SnmpStatusException;

  public abstract SnmpValue set(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
    throws SnmpStatusException;

  public abstract void check(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpStandardMetaServer
 * JD-Core Version:    0.6.2
 */