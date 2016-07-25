package com.sun.jmx.snmp;

import java.util.Vector;

public abstract interface SnmpOidTable
{
  public abstract SnmpOidRecord resolveVarName(String paramString)
    throws SnmpStatusException;

  public abstract SnmpOidRecord resolveVarOid(String paramString)
    throws SnmpStatusException;

  public abstract Vector<?> getAllEntries();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpOidTable
 * JD-Core Version:    0.6.2
 */