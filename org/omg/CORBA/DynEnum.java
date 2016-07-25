package org.omg.CORBA;

@Deprecated
public abstract interface DynEnum extends Object, DynAny
{
  public abstract String value_as_string();

  public abstract void value_as_string(String paramString);

  public abstract int value_as_ulong();

  public abstract void value_as_ulong(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.DynEnum
 * JD-Core Version:    0.6.2
 */