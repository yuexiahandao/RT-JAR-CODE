package com.sun.corba.se.impl.corba;

public abstract interface TypeCodeFactory
{
  public abstract void setTypeCode(String paramString, TypeCodeImpl paramTypeCodeImpl);

  public abstract TypeCodeImpl getTypeCode(String paramString);

  public abstract void setTypeCodeForClass(Class paramClass, TypeCodeImpl paramTypeCodeImpl);

  public abstract TypeCodeImpl getTypeCodeForClass(Class paramClass);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.corba.TypeCodeFactory
 * JD-Core Version:    0.6.2
 */