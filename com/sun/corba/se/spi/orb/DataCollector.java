package com.sun.corba.se.spi.orb;

import java.util.Properties;

public abstract interface DataCollector
{
  public abstract boolean isApplet();

  public abstract boolean initialHostIsLocal();

  public abstract void setParser(PropertyParser paramPropertyParser);

  public abstract Properties getProperties();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orb.DataCollector
 * JD-Core Version:    0.6.2
 */