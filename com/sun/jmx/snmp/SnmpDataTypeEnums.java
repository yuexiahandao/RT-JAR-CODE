package com.sun.jmx.snmp;

public abstract interface SnmpDataTypeEnums
{
  public static final int BooleanTag = 1;
  public static final int IntegerTag = 2;
  public static final int BitStringTag = 2;
  public static final int OctetStringTag = 4;
  public static final int NullTag = 5;
  public static final int ObjectIdentiferTag = 6;
  public static final int UnknownSyntaxTag = 255;
  public static final int SequenceTag = 48;
  public static final int TableTag = 254;
  public static final int ApplFlag = 64;
  public static final int CtxtFlag = 128;
  public static final int IpAddressTag = 64;
  public static final int CounterTag = 65;
  public static final int GaugeTag = 66;
  public static final int TimeticksTag = 67;
  public static final int OpaqueTag = 68;
  public static final int Counter64Tag = 70;
  public static final int NsapTag = 69;
  public static final int UintegerTag = 71;
  public static final int errNoSuchObjectTag = 128;
  public static final int errNoSuchInstanceTag = 129;
  public static final int errEndOfMibViewTag = 130;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpDataTypeEnums
 * JD-Core Version:    0.6.2
 */