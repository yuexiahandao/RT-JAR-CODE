package javax.management;

import java.io.IOException;
import java.util.Set;

public abstract interface MBeanServerConnection
{
  public abstract ObjectInstance createMBean(String paramString, ObjectName paramObjectName)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException;

  public abstract ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException;

  public abstract ObjectInstance createMBean(String paramString, ObjectName paramObjectName, Object[] paramArrayOfObject, String[] paramArrayOfString)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException;

  public abstract ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2, Object[] paramArrayOfObject, String[] paramArrayOfString)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException;

  public abstract void unregisterMBean(ObjectName paramObjectName)
    throws InstanceNotFoundException, MBeanRegistrationException, IOException;

  public abstract ObjectInstance getObjectInstance(ObjectName paramObjectName)
    throws InstanceNotFoundException, IOException;

  public abstract Set<ObjectInstance> queryMBeans(ObjectName paramObjectName, QueryExp paramQueryExp)
    throws IOException;

  public abstract Set<ObjectName> queryNames(ObjectName paramObjectName, QueryExp paramQueryExp)
    throws IOException;

  public abstract boolean isRegistered(ObjectName paramObjectName)
    throws IOException;

  public abstract Integer getMBeanCount()
    throws IOException;

  public abstract Object getAttribute(ObjectName paramObjectName, String paramString)
    throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException;

  public abstract AttributeList getAttributes(ObjectName paramObjectName, String[] paramArrayOfString)
    throws InstanceNotFoundException, ReflectionException, IOException;

  public abstract void setAttribute(ObjectName paramObjectName, Attribute paramAttribute)
    throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException;

  public abstract AttributeList setAttributes(ObjectName paramObjectName, AttributeList paramAttributeList)
    throws InstanceNotFoundException, ReflectionException, IOException;

  public abstract Object invoke(ObjectName paramObjectName, String paramString, Object[] paramArrayOfObject, String[] paramArrayOfString)
    throws InstanceNotFoundException, MBeanException, ReflectionException, IOException;

  public abstract String getDefaultDomain()
    throws IOException;

  public abstract String[] getDomains()
    throws IOException;

  public abstract void addNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
    throws InstanceNotFoundException, IOException;

  public abstract void addNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, NotificationFilter paramNotificationFilter, Object paramObject)
    throws InstanceNotFoundException, IOException;

  public abstract void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2)
    throws InstanceNotFoundException, ListenerNotFoundException, IOException;

  public abstract void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, NotificationFilter paramNotificationFilter, Object paramObject)
    throws InstanceNotFoundException, ListenerNotFoundException, IOException;

  public abstract void removeNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener)
    throws InstanceNotFoundException, ListenerNotFoundException, IOException;

  public abstract void removeNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
    throws InstanceNotFoundException, ListenerNotFoundException, IOException;

  public abstract MBeanInfo getMBeanInfo(ObjectName paramObjectName)
    throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException;

  public abstract boolean isInstanceOf(ObjectName paramObjectName, String paramString)
    throws InstanceNotFoundException, IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanServerConnection
 * JD-Core Version:    0.6.2
 */