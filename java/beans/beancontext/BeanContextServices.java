package java.beans.beancontext;

import java.util.Iterator;
import java.util.TooManyListenersException;

public abstract interface BeanContextServices extends BeanContext, BeanContextServicesListener
{
  public abstract boolean addService(Class paramClass, BeanContextServiceProvider paramBeanContextServiceProvider);

  public abstract void revokeService(Class paramClass, BeanContextServiceProvider paramBeanContextServiceProvider, boolean paramBoolean);

  public abstract boolean hasService(Class paramClass);

  public abstract Object getService(BeanContextChild paramBeanContextChild, Object paramObject1, Class paramClass, Object paramObject2, BeanContextServiceRevokedListener paramBeanContextServiceRevokedListener)
    throws TooManyListenersException;

  public abstract void releaseService(BeanContextChild paramBeanContextChild, Object paramObject1, Object paramObject2);

  public abstract Iterator getCurrentServiceClasses();

  public abstract Iterator getCurrentServiceSelectors(Class paramClass);

  public abstract void addBeanContextServicesListener(BeanContextServicesListener paramBeanContextServicesListener);

  public abstract void removeBeanContextServicesListener(BeanContextServicesListener paramBeanContextServicesListener);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.beancontext.BeanContextServices
 * JD-Core Version:    0.6.2
 */