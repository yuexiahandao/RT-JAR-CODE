package org.omg.CORBA;

public abstract class ExceptionList
{
  public abstract int count();

  public abstract void add(TypeCode paramTypeCode);

  public abstract TypeCode item(int paramInt)
    throws Bounds;

  public abstract void remove(int paramInt)
    throws Bounds;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ExceptionList
 * JD-Core Version:    0.6.2
 */