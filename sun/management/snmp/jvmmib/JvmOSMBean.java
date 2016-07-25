package sun.management.snmp.jvmmib;

import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface JvmOSMBean
{
  public abstract Integer getJvmOSProcessorCount()
    throws SnmpStatusException;

  public abstract String getJvmOSVersion()
    throws SnmpStatusException;

  public abstract String getJvmOSArch()
    throws SnmpStatusException;

  public abstract String getJvmOSName()
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmOSMBean
 * JD-Core Version:    0.6.2
 */