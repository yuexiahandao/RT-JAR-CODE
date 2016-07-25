package sun.management.snmp.jvmmib;

import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface JvmMemPoolEntryMBean
{
  public abstract Long getJvmMemPoolCollectMaxSize()
    throws SnmpStatusException;

  public abstract Long getJvmMemPoolCollectCommitted()
    throws SnmpStatusException;

  public abstract Long getJvmMemPoolCollectUsed()
    throws SnmpStatusException;

  public abstract EnumJvmMemPoolCollectThreshdSupport getJvmMemPoolCollectThreshdSupport()
    throws SnmpStatusException;

  public abstract Long getJvmMemPoolCollectThreshdCount()
    throws SnmpStatusException;

  public abstract Long getJvmMemPoolCollectThreshold()
    throws SnmpStatusException;

  public abstract void setJvmMemPoolCollectThreshold(Long paramLong)
    throws SnmpStatusException;

  public abstract void checkJvmMemPoolCollectThreshold(Long paramLong)
    throws SnmpStatusException;

  public abstract Long getJvmMemPoolMaxSize()
    throws SnmpStatusException;

  public abstract Long getJvmMemPoolCommitted()
    throws SnmpStatusException;

  public abstract Long getJvmMemPoolUsed()
    throws SnmpStatusException;

  public abstract Long getJvmMemPoolInitSize()
    throws SnmpStatusException;

  public abstract EnumJvmMemPoolThreshdSupport getJvmMemPoolThreshdSupport()
    throws SnmpStatusException;

  public abstract Long getJvmMemPoolThreshdCount()
    throws SnmpStatusException;

  public abstract Long getJvmMemPoolThreshold()
    throws SnmpStatusException;

  public abstract void setJvmMemPoolThreshold(Long paramLong)
    throws SnmpStatusException;

  public abstract void checkJvmMemPoolThreshold(Long paramLong)
    throws SnmpStatusException;

  public abstract Long getJvmMemPoolPeakReset()
    throws SnmpStatusException;

  public abstract void setJvmMemPoolPeakReset(Long paramLong)
    throws SnmpStatusException;

  public abstract void checkJvmMemPoolPeakReset(Long paramLong)
    throws SnmpStatusException;

  public abstract EnumJvmMemPoolState getJvmMemPoolState()
    throws SnmpStatusException;

  public abstract EnumJvmMemPoolType getJvmMemPoolType()
    throws SnmpStatusException;

  public abstract String getJvmMemPoolName()
    throws SnmpStatusException;

  public abstract Long getJvmMemPoolPeakMaxSize()
    throws SnmpStatusException;

  public abstract Integer getJvmMemPoolIndex()
    throws SnmpStatusException;

  public abstract Long getJvmMemPoolPeakCommitted()
    throws SnmpStatusException;

  public abstract Long getJvmMemPoolPeakUsed()
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmMemPoolEntryMBean
 * JD-Core Version:    0.6.2
 */