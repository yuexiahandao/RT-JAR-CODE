package com.sun.jmx.snmp.agent;

import com.sun.jmx.snmp.SnmpOid;
import com.sun.jmx.snmp.SnmpStatusException;
import com.sun.jmx.snmp.SnmpVarBind;
import java.util.Enumeration;
import java.util.Vector;

public abstract interface SnmpMibSubRequest extends SnmpMibRequest
{
  public abstract Enumeration getElements();

  public abstract Vector<SnmpVarBind> getSubList();

  public abstract SnmpOid getEntryOid();

  public abstract boolean isNewEntry();

  public abstract SnmpVarBind getRowStatusVarBind();

  public abstract void registerGetException(SnmpVarBind paramSnmpVarBind, SnmpStatusException paramSnmpStatusException)
    throws SnmpStatusException;

  public abstract void registerSetException(SnmpVarBind paramSnmpVarBind, SnmpStatusException paramSnmpStatusException)
    throws SnmpStatusException;

  public abstract void registerCheckException(SnmpVarBind paramSnmpVarBind, SnmpStatusException paramSnmpStatusException)
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpMibSubRequest
 * JD-Core Version:    0.6.2
 */