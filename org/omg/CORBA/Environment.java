package org.omg.CORBA;

public abstract class Environment
{
  public abstract Exception exception();

  public abstract void exception(Exception paramException);

  public abstract void clear();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.Environment
 * JD-Core Version:    0.6.2
 */