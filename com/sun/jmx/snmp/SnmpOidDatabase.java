package com.sun.jmx.snmp;

import java.util.Vector;

public abstract interface SnmpOidDatabase extends SnmpOidTable
{
  public abstract void add(SnmpOidTable paramSnmpOidTable);

  public abstract void remove(SnmpOidTable paramSnmpOidTable)
    throws SnmpStatusException;

  public abstract void removeAll();

  public abstract SnmpOidRecord resolveVarName(String paramString)
    throws SnmpStatusException;

  public abstract SnmpOidRecord resolveVarOid(String paramString)
    throws SnmpStatusException;

  public abstract Vector<?> getAllEntries();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpOidDatabase
 * JD-Core Version:    0.6.2
 */