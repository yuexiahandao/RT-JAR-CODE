package sun.management.snmp.jvmmib;

import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface JvmRTLibraryPathEntryMBean
{
  public abstract String getJvmRTLibraryPathItem()
    throws SnmpStatusException;

  public abstract Integer getJvmRTLibraryPathIndex()
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmRTLibraryPathEntryMBean
 * JD-Core Version:    0.6.2
 */