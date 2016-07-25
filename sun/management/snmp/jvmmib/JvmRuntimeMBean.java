package sun.management.snmp.jvmmib;

import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface JvmRuntimeMBean
{
  public abstract EnumJvmRTBootClassPathSupport getJvmRTBootClassPathSupport()
    throws SnmpStatusException;

  public abstract String getJvmRTManagementSpecVersion()
    throws SnmpStatusException;

  public abstract String getJvmRTSpecVersion()
    throws SnmpStatusException;

  public abstract String getJvmRTSpecVendor()
    throws SnmpStatusException;

  public abstract String getJvmRTSpecName()
    throws SnmpStatusException;

  public abstract String getJvmRTVMVersion()
    throws SnmpStatusException;

  public abstract String getJvmRTVMVendor()
    throws SnmpStatusException;

  public abstract Long getJvmRTStartTimeMs()
    throws SnmpStatusException;

  public abstract Long getJvmRTUptimeMs()
    throws SnmpStatusException;

  public abstract String getJvmRTVMName()
    throws SnmpStatusException;

  public abstract String getJvmRTName()
    throws SnmpStatusException;

  public abstract Integer getJvmRTInputArgsCount()
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmRuntimeMBean
 * JD-Core Version:    0.6.2
 */