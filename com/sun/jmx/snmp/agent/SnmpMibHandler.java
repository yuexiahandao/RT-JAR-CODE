package com.sun.jmx.snmp.agent;

import com.sun.jmx.snmp.SnmpOid;

public abstract interface SnmpMibHandler
{
  public abstract SnmpMibHandler addMib(SnmpMibAgent paramSnmpMibAgent)
    throws IllegalArgumentException;

  public abstract SnmpMibHandler addMib(SnmpMibAgent paramSnmpMibAgent, SnmpOid[] paramArrayOfSnmpOid)
    throws IllegalArgumentException;

  public abstract SnmpMibHandler addMib(SnmpMibAgent paramSnmpMibAgent, String paramString)
    throws IllegalArgumentException;

  public abstract SnmpMibHandler addMib(SnmpMibAgent paramSnmpMibAgent, String paramString, SnmpOid[] paramArrayOfSnmpOid)
    throws IllegalArgumentException;

  public abstract boolean removeMib(SnmpMibAgent paramSnmpMibAgent);

  public abstract boolean removeMib(SnmpMibAgent paramSnmpMibAgent, SnmpOid[] paramArrayOfSnmpOid);

  public abstract boolean removeMib(SnmpMibAgent paramSnmpMibAgent, String paramString);

  public abstract boolean removeMib(SnmpMibAgent paramSnmpMibAgent, String paramString, SnmpOid[] paramArrayOfSnmpOid);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpMibHandler
 * JD-Core Version:    0.6.2
 */