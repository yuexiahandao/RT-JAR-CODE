package org.omg.CORBA;

public abstract class ContextList
{
  public abstract int count();

  public abstract void add(String paramString);

  public abstract String item(int paramInt)
    throws Bounds;

  public abstract void remove(int paramInt)
    throws Bounds;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ContextList
 * JD-Core Version:    0.6.2
 */