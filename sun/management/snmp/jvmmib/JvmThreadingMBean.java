package sun.management.snmp.jvmmib;

import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface JvmThreadingMBean
{
  public abstract EnumJvmThreadCpuTimeMonitoring getJvmThreadCpuTimeMonitoring()
    throws SnmpStatusException;

  public abstract void setJvmThreadCpuTimeMonitoring(EnumJvmThreadCpuTimeMonitoring paramEnumJvmThreadCpuTimeMonitoring)
    throws SnmpStatusException;

  public abstract void checkJvmThreadCpuTimeMonitoring(EnumJvmThreadCpuTimeMonitoring paramEnumJvmThreadCpuTimeMonitoring)
    throws SnmpStatusException;

  public abstract EnumJvmThreadContentionMonitoring getJvmThreadContentionMonitoring()
    throws SnmpStatusException;

  public abstract void setJvmThreadContentionMonitoring(EnumJvmThreadContentionMonitoring paramEnumJvmThreadContentionMonitoring)
    throws SnmpStatusException;

  public abstract void checkJvmThreadContentionMonitoring(EnumJvmThreadContentionMonitoring paramEnumJvmThreadContentionMonitoring)
    throws SnmpStatusException;

  public abstract Long getJvmThreadTotalStartedCount()
    throws SnmpStatusException;

  public abstract Long getJvmThreadPeakCount()
    throws SnmpStatusException;

  public abstract Long getJvmThreadDaemonCount()
    throws SnmpStatusException;

  public abstract Long getJvmThreadCount()
    throws SnmpStatusException;

  public abstract Long getJvmThreadPeakCountReset()
    throws SnmpStatusException;

  public abstract void setJvmThreadPeakCountReset(Long paramLong)
    throws SnmpStatusException;

  public abstract void checkJvmThreadPeakCountReset(Long paramLong)
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmThreadingMBean
 * JD-Core Version:    0.6.2
 */