package com.sun.jmx.snmp;

public abstract interface SnmpEngine
{
  public abstract int getEngineTime();

  public abstract SnmpEngineId getEngineId();

  public abstract int getEngineBoots();

  public abstract SnmpUsmKeyHandler getUsmKeyHandler();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpEngine
 * JD-Core Version:    0.6.2
 */