package com.sun.jmx.snmp;

public abstract interface SnmpEngineFactory
{
  public abstract SnmpEngine createEngine(SnmpEngineParameters paramSnmpEngineParameters);

  public abstract SnmpEngine createEngine(SnmpEngineParameters paramSnmpEngineParameters, InetAddressAcl paramInetAddressAcl);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpEngineFactory
 * JD-Core Version:    0.6.2
 */