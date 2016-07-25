package sun.management.snmp.jvmmib;

import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface JvmMemGCEntryMBean
{
  public abstract Long getJvmMemGCTimeMs()
    throws SnmpStatusException;

  public abstract Long getJvmMemGCCount()
    throws SnmpStatusException;

  public abstract Integer getJvmMemManagerIndex()
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmMemGCEntryMBean
 * JD-Core Version:    0.6.2
 */