package sun.management.snmp.jvmmib;

import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface JvmRTBootClassPathEntryMBean
{
  public abstract String getJvmRTBootClassPathItem()
    throws SnmpStatusException;

  public abstract Integer getJvmRTBootClassPathIndex()
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmRTBootClassPathEntryMBean
 * JD-Core Version:    0.6.2
 */