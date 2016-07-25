package sun.management.snmp.jvmmib;

import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface JvmRTInputArgsEntryMBean
{
  public abstract String getJvmRTInputArgsItem()
    throws SnmpStatusException;

  public abstract Integer getJvmRTInputArgsIndex()
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmRTInputArgsEntryMBean
 * JD-Core Version:    0.6.2
 */