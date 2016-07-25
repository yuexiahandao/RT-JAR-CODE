package javax.management;

import java.io.ObjectInputStream;
import java.util.Set;
import javax.management.loading.ClassLoaderRepository;

public abstract interface MBeanServer extends MBeanServerConnection
{
  public abstract ObjectInstance createMBean(String paramString, ObjectName paramObjectName)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException;

  public abstract ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException;

  public abstract ObjectInstance createMBean(String paramString, ObjectName paramObjectName, Object[] paramArrayOfObject, String[] paramArrayOfString)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException;

  public abstract ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2, Object[] paramArrayOfObject, String[] paramArrayOfString)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException;

  public abstract ObjectInstance registerMBean(Object paramObject, ObjectName paramObjectName)
    throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException;

  public abstract void unregisterMBean(ObjectName paramObjectName)
    throws InstanceNotFoundException, MBeanRegistrationException;

  public abstract ObjectInstance getObjectInstance(ObjectName paramObjectName)
    throws InstanceNotFoundException;

  public abstract Set<ObjectInstance> queryMBeans(ObjectName paramObjectName, QueryExp paramQueryExp);

  public abstract Set<ObjectName> queryNames(ObjectName paramObjectName, QueryExp paramQueryExp);

  public abstract boolean isRegistered(ObjectName paramObjectName);

  public abstract Integer getMBeanCount();

  public abstract Object getAttribute(ObjectName paramObjectName, String paramString)
    throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException;

  public abstract AttributeList getAttributes(ObjectName paramObjectName, String[] paramArrayOfString)
    throws InstanceNotFoundException, ReflectionException;

  public abstract void setAttribute(ObjectName paramObjectName, Attribute paramAttribute)
    throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException;

  public abstract AttributeList setAttributes(ObjectName paramObjectName, AttributeList paramAttributeList)
    throws InstanceNotFoundException, ReflectionException;

  public abstract Object invoke(ObjectName paramObjectName, String paramString, Object[] paramArrayOfObject, String[] paramArrayOfString)
    throws InstanceNotFoundException, MBeanException, ReflectionException;

  public abstract String getDefaultDomain();

  public abstract String[] getDomains();

  public abstract void addNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
    throws InstanceNotFoundException;

  public abstract void addNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, NotificationFilter paramNotificationFilter, Object paramObject)
    throws InstanceNotFoundException;

  public abstract void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2)
    throws InstanceNotFoundException, ListenerNotFoundException;

  public abstract void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, NotificationFilter paramNotificationFilter, Object paramObject)
    throws InstanceNotFoundException, ListenerNotFoundException;

  public abstract void removeNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener)
    throws InstanceNotFoundException, ListenerNotFoundException;

  public abstract void removeNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
    throws InstanceNotFoundException, ListenerNotFoundException;

  public abstract MBeanInfo getMBeanInfo(ObjectName paramObjectName)
    throws InstanceNotFoundException, IntrospectionException, ReflectionException;

  public abstract boolean isInstanceOf(ObjectName paramObjectName, String paramString)
    throws InstanceNotFoundException;

  public abstract Object instantiate(String paramString)
    throws ReflectionException, MBeanException;

  public abstract Object instantiate(String paramString, ObjectName paramObjectName)
    throws ReflectionException, MBeanException, InstanceNotFoundException;

  public abstract Object instantiate(String paramString, Object[] paramArrayOfObject, String[] paramArrayOfString)
    throws ReflectionException, MBeanException;

  public abstract Object instantiate(String paramString, ObjectName paramObjectName, Object[] paramArrayOfObject, String[] paramArrayOfString)
    throws ReflectionException, MBeanException, InstanceNotFoundException;

  @Deprecated
  public abstract ObjectInputStream deserialize(ObjectName paramObjectName, byte[] paramArrayOfByte)
    throws InstanceNotFoundException, OperationsException;

  @Deprecated
  public abstract ObjectInputStream deserialize(String paramString, byte[] paramArrayOfByte)
    throws OperationsException, ReflectionException;

  @Deprecated
  public abstract ObjectInputStream deserialize(String paramString, ObjectName paramObjectName, byte[] paramArrayOfByte)
    throws InstanceNotFoundException, OperationsException, ReflectionException;

  public abstract ClassLoader getClassLoaderFor(ObjectName paramObjectName)
    throws InstanceNotFoundException;

  public abstract ClassLoader getClassLoader(ObjectName paramObjectName)
    throws InstanceNotFoundException;

  public abstract ClassLoaderRepository getClassLoaderRepository();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanServer
 * JD-Core Version:    0.6.2
 */