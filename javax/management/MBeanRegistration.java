package javax.management;

public abstract interface MBeanRegistration
{
  public abstract ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
    throws Exception;

  public abstract void postRegister(Boolean paramBoolean);

  public abstract void preDeregister()
    throws Exception;

  public abstract void postDeregister();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanRegistration
 * JD-Core Version:    0.6.2
 */