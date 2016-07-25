package com.sun.org.apache.xml.internal.dtm;

public abstract interface DTMWSFilter
{
  public static final short NOTSTRIP = 1;
  public static final short STRIP = 2;
  public static final short INHERIT = 3;

  public abstract short getShouldStripSpace(int paramInt, DTM paramDTM);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.DTMWSFilter
 * JD-Core Version:    0.6.2
 */