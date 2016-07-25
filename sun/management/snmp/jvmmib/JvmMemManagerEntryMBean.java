package sun.management.snmp.jvmmib;

import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface JvmMemManagerEntryMBean
{
  public abstract EnumJvmMemManagerState getJvmMemManagerState()
    throws SnmpStatusException;

  public abstract String getJvmMemManagerName()
    throws SnmpStatusException;

  public abstract Integer getJvmMemManagerIndex()
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmMemManagerEntryMBean
 * JD-Core Version:    0.6.2
 */