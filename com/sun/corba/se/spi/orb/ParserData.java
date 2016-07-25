package com.sun.corba.se.spi.orb;

import java.util.Properties;

public abstract interface ParserData
{
  public abstract String getPropertyName();

  public abstract Operation getOperation();

  public abstract String getFieldName();

  public abstract Object getDefaultValue();

  public abstract Object getTestValue();

  public abstract void addToParser(PropertyParser paramPropertyParser);

  public abstract void addToProperties(Properties paramProperties);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orb.ParserData
 * JD-Core Version:    0.6.2
 */