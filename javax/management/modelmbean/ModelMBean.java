package javax.management.modelmbean;

import javax.management.DynamicMBean;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.PersistentMBean;
import javax.management.RuntimeOperationsException;

public abstract interface ModelMBean extends DynamicMBean, PersistentMBean, ModelMBeanNotificationBroadcaster
{
  public abstract void setModelMBeanInfo(ModelMBeanInfo paramModelMBeanInfo)
    throws MBeanException, RuntimeOperationsException;

  public abstract void setManagedResource(Object paramObject, String paramString)
    throws MBeanException, RuntimeOperationsException, InstanceNotFoundException, InvalidTargetObjectTypeException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.modelmbean.ModelMBean
 * JD-Core Version:    0.6.2
 */