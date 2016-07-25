package javax.management;

public abstract interface PersistentMBean
{
  public abstract void load()
    throws MBeanException, RuntimeOperationsException, InstanceNotFoundException;

  public abstract void store()
    throws MBeanException, RuntimeOperationsException, InstanceNotFoundException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.PersistentMBean
 * JD-Core Version:    0.6.2
 */