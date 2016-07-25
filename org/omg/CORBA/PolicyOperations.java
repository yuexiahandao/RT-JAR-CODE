package org.omg.CORBA;

public abstract interface PolicyOperations
{
  public abstract int policy_type();

  public abstract Policy copy();

  public abstract void destroy();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.PolicyOperations
 * JD-Core Version:    0.6.2
 */