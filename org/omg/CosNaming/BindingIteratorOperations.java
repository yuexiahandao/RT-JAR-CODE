package org.omg.CosNaming;

public abstract interface BindingIteratorOperations
{
  public abstract boolean next_one(BindingHolder paramBindingHolder);

  public abstract boolean next_n(int paramInt, BindingListHolder paramBindingListHolder);

  public abstract void destroy();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.BindingIteratorOperations
 * JD-Core Version:    0.6.2
 */