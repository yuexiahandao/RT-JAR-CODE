package com.sun.corba.se.spi.orb;

import org.omg.CORBA.portable.OutputStream;

public abstract interface ORBVersion extends Comparable
{
  public static final byte FOREIGN = 0;
  public static final byte OLD = 1;
  public static final byte NEW = 2;
  public static final byte JDK1_3_1_01 = 3;
  public static final byte NEWER = 10;
  public static final byte PEORB = 20;

  public abstract byte getORBType();

  public abstract void write(OutputStream paramOutputStream);

  public abstract boolean lessThan(ORBVersion paramORBVersion);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orb.ORBVersion
 * JD-Core Version:    0.6.2
 */