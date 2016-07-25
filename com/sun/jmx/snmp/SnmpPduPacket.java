package com.sun.jmx.snmp;

import java.io.Serializable;

public abstract class SnmpPduPacket extends SnmpPdu
  implements Serializable
{
  public byte[] community;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpPduPacket
 * JD-Core Version:    0.6.2
 */