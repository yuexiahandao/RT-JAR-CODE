package sun.management.snmp.jvmmib;

import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface JvmThreadInstanceEntryMBean
{
  public abstract String getJvmThreadInstName()
    throws SnmpStatusException;

  public abstract Long getJvmThreadInstCpuTimeNs()
    throws SnmpStatusException;

  public abstract Long getJvmThreadInstWaitTimeMs()
    throws SnmpStatusException;

  public abstract Long getJvmThreadInstWaitCount()
    throws SnmpStatusException;

  public abstract Long getJvmThreadInstBlockTimeMs()
    throws SnmpStatusException;

  public abstract Long getJvmThreadInstBlockCount()
    throws SnmpStatusException;

  public abstract Byte[] getJvmThreadInstState()
    throws SnmpStatusException;

  public abstract String getJvmThreadInstLockOwnerPtr()
    throws SnmpStatusException;

  public abstract Long getJvmThreadInstId()
    throws SnmpStatusException;

  public abstract String getJvmThreadInstLockName()
    throws SnmpStatusException;

  public abstract Byte[] getJvmThreadInstIndex()
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmThreadInstanceEntryMBean
 * JD-Core Version:    0.6.2
 */