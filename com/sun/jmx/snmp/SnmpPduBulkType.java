package com.sun.jmx.snmp;

public abstract interface SnmpPduBulkType extends SnmpAckPdu
{
  public abstract void setMaxRepetitions(int paramInt);

  public abstract void setNonRepeaters(int paramInt);

  public abstract int getMaxRepetitions();

  public abstract int getNonRepeaters();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpPduBulkType
 * JD-Core Version:    0.6.2
 */