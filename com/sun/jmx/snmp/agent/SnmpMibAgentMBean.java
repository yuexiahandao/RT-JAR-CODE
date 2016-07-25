package com.sun.jmx.snmp.agent;

import com.sun.jmx.snmp.SnmpOid;
import com.sun.jmx.snmp.SnmpStatusException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ServiceNotFoundException;

public abstract interface SnmpMibAgentMBean
{
  public abstract void get(SnmpMibRequest paramSnmpMibRequest)
    throws SnmpStatusException;

  public abstract void getNext(SnmpMibRequest paramSnmpMibRequest)
    throws SnmpStatusException;

  public abstract void getBulk(SnmpMibRequest paramSnmpMibRequest, int paramInt1, int paramInt2)
    throws SnmpStatusException;

  public abstract void set(SnmpMibRequest paramSnmpMibRequest)
    throws SnmpStatusException;

  public abstract void check(SnmpMibRequest paramSnmpMibRequest)
    throws SnmpStatusException;

  public abstract MBeanServer getMBeanServer();

  public abstract SnmpMibHandler getSnmpAdaptor();

  public abstract void setSnmpAdaptor(SnmpMibHandler paramSnmpMibHandler);

  public abstract void setSnmpAdaptor(SnmpMibHandler paramSnmpMibHandler, SnmpOid[] paramArrayOfSnmpOid);

  public abstract void setSnmpAdaptor(SnmpMibHandler paramSnmpMibHandler, String paramString);

  public abstract void setSnmpAdaptor(SnmpMibHandler paramSnmpMibHandler, String paramString, SnmpOid[] paramArrayOfSnmpOid);

  public abstract ObjectName getSnmpAdaptorName();

  public abstract void setSnmpAdaptorName(ObjectName paramObjectName)
    throws InstanceNotFoundException, ServiceNotFoundException;

  public abstract void setSnmpAdaptorName(ObjectName paramObjectName, SnmpOid[] paramArrayOfSnmpOid)
    throws InstanceNotFoundException, ServiceNotFoundException;

  public abstract void setSnmpAdaptorName(ObjectName paramObjectName, String paramString)
    throws InstanceNotFoundException, ServiceNotFoundException;

  public abstract void setSnmpAdaptorName(ObjectName paramObjectName, String paramString, SnmpOid[] paramArrayOfSnmpOid)
    throws InstanceNotFoundException, ServiceNotFoundException;

  public abstract boolean getBindingState();

  public abstract String getMibName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpMibAgentMBean
 * JD-Core Version:    0.6.2
 */