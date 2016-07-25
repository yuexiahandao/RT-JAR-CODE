package sun.management.snmp.jvmmib;

import com.sun.jmx.snmp.SnmpStatusException;

public abstract interface JvmCompilationMBean
{
  public abstract EnumJvmJITCompilerTimeMonitoring getJvmJITCompilerTimeMonitoring()
    throws SnmpStatusException;

  public abstract Long getJvmJITCompilerTimeMs()
    throws SnmpStatusException;

  public abstract String getJvmJITCompilerName()
    throws SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmCompilationMBean
 * JD-Core Version:    0.6.2
 */