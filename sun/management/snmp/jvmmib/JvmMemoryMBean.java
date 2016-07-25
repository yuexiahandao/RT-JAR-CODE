package sun.management.snmp.jvmmib;

import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface JvmMemoryMBean
{
  public abstract Long getJvmMemoryNonHeapMaxSize()
    throws SnmpStatusException;

  public abstract Long getJvmMemoryNonHeapCommitted()
    throws SnmpStatusException;

  public abstract Long getJvmMemoryNonHeapUsed()
    throws SnmpStatusException;

  public abstract Long getJvmMemoryNonHeapInitSize()
    throws SnmpStatusException;

  public abstract Long getJvmMemoryHeapMaxSize()
    throws SnmpStatusException;

  public abstract Long getJvmMemoryHeapCommitted()
    throws SnmpStatusException;

  public abstract EnumJvmMemoryGCCall getJvmMemoryGCCall()
    throws SnmpStatusException;

  public abstract void setJvmMemoryGCCall(EnumJvmMemoryGCCall paramEnumJvmMemoryGCCall)
    throws SnmpStatusException;

  public abstract void checkJvmMemoryGCCall(EnumJvmMemoryGCCall paramEnumJvmMemoryGCCall)
    throws SnmpStatusException;

  public abstract Long getJvmMemoryHeapUsed()
    throws SnmpStatusException;

  public abstract EnumJvmMemoryGCVerboseLevel getJvmMemoryGCVerboseLevel()
    throws SnmpStatusException;

  public abstract void setJvmMemoryGCVerboseLevel(EnumJvmMemoryGCVerboseLevel paramEnumJvmMemoryGCVerboseLevel)
    throws SnmpStatusException;

  public abstract void checkJvmMemoryGCVerboseLevel(EnumJvmMemoryGCVerboseLevel paramEnumJvmMemoryGCVerboseLevel)
    throws SnmpStatusException;

  public abstract Long getJvmMemoryHeapInitSize()
    throws SnmpStatusException;

  public abstract Long getJvmMemoryPendingFinalCount()
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmMemoryMBean
 * JD-Core Version:    0.6.2
 */