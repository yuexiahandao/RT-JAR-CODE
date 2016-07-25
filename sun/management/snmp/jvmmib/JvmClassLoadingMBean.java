package sun.management.snmp.jvmmib;

import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface JvmClassLoadingMBean
{
  public abstract EnumJvmClassesVerboseLevel getJvmClassesVerboseLevel()
    throws SnmpStatusException;

  public abstract void setJvmClassesVerboseLevel(EnumJvmClassesVerboseLevel paramEnumJvmClassesVerboseLevel)
    throws SnmpStatusException;

  public abstract void checkJvmClassesVerboseLevel(EnumJvmClassesVerboseLevel paramEnumJvmClassesVerboseLevel)
    throws SnmpStatusException;

  public abstract Long getJvmClassesUnloadedCount()
    throws SnmpStatusException;

  public abstract Long getJvmClassesTotalLoadedCount()
    throws SnmpStatusException;

  public abstract Long getJvmClassesLoadedCount()
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmClassLoadingMBean
 * JD-Core Version:    0.6.2
 */