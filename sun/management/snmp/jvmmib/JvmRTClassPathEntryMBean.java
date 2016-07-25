package sun.management.snmp.jvmmib;

import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface JvmRTClassPathEntryMBean
{
  public abstract String getJvmRTClassPathItem()
    throws SnmpStatusException;

  public abstract Integer getJvmRTClassPathIndex()
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmRTClassPathEntryMBean
 * JD-Core Version:    0.6.2
 */