package sun.management.snmp.util;

import com.sun.jmx.snmp.SnmpOid;

public abstract interface SnmpTableHandler
{
  public abstract Object getData(SnmpOid paramSnmpOid);

  public abstract SnmpOid getNext(SnmpOid paramSnmpOid);

  public abstract boolean contains(SnmpOid paramSnmpOid);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.util.SnmpTableHandler
 * JD-Core Version:    0.6.2
 */