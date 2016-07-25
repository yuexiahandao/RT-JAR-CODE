package javax.management.remote.rmi;

import java.io.Closeable;
import java.io.IOException;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.util.Set;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.NotificationResult;
import javax.security.auth.Subject;

public abstract interface RMIConnection extends Closeable, Remote
{
  public abstract String getConnectionId()
    throws IOException;

  public abstract void close()
    throws IOException;

  public abstract ObjectInstance createMBean(String paramString, ObjectName paramObjectName, Subject paramSubject)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException;

  public abstract ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2, Subject paramSubject)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException;

  public abstract ObjectInstance createMBean(String paramString, ObjectName paramObjectName, MarshalledObject paramMarshalledObject, String[] paramArrayOfString, Subject paramSubject)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException;

  public abstract ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2, MarshalledObject paramMarshalledObject, String[] paramArrayOfString, Subject paramSubject)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException;

  public abstract void unregisterMBean(ObjectName paramObjectName, Subject paramSubject)
    throws InstanceNotFoundException, MBeanRegistrationException, IOException;

  public abstract ObjectInstance getObjectInstance(ObjectName paramObjectName, Subject paramSubject)
    throws InstanceNotFoundException, IOException;

  public abstract Set<ObjectInstance> queryMBeans(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
    throws IOException;

  public abstract Set<ObjectName> queryNames(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
    throws IOException;

  public abstract boolean isRegistered(ObjectName paramObjectName, Subject paramSubject)
    throws IOException;

  public abstract Integer getMBeanCount(Subject paramSubject)
    throws IOException;

  public abstract Object getAttribute(ObjectName paramObjectName, String paramString, Subject paramSubject)
    throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException;

  public abstract AttributeList getAttributes(ObjectName paramObjectName, String[] paramArrayOfString, Subject paramSubject)
    throws InstanceNotFoundException, ReflectionException, IOException;

  public abstract void setAttribute(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
    throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException;

  public abstract AttributeList setAttributes(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
    throws InstanceNotFoundException, ReflectionException, IOException;

  public abstract Object invoke(ObjectName paramObjectName, String paramString, MarshalledObject paramMarshalledObject, String[] paramArrayOfString, Subject paramSubject)
    throws InstanceNotFoundException, MBeanException, ReflectionException, IOException;

  public abstract String getDefaultDomain(Subject paramSubject)
    throws IOException;

  public abstract String[] getDomains(Subject paramSubject)
    throws IOException;

  public abstract MBeanInfo getMBeanInfo(ObjectName paramObjectName, Subject paramSubject)
    throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException;

  public abstract boolean isInstanceOf(ObjectName paramObjectName, String paramString, Subject paramSubject)
    throws InstanceNotFoundException, IOException;

  public abstract void addNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, MarshalledObject paramMarshalledObject1, MarshalledObject paramMarshalledObject2, Subject paramSubject)
    throws InstanceNotFoundException, IOException;

  public abstract void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, Subject paramSubject)
    throws InstanceNotFoundException, ListenerNotFoundException, IOException;

  public abstract void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, MarshalledObject paramMarshalledObject1, MarshalledObject paramMarshalledObject2, Subject paramSubject)
    throws InstanceNotFoundException, ListenerNotFoundException, IOException;

  public abstract Integer[] addNotificationListeners(ObjectName[] paramArrayOfObjectName, MarshalledObject[] paramArrayOfMarshalledObject, Subject[] paramArrayOfSubject)
    throws InstanceNotFoundException, IOException;

  public abstract void removeNotificationListeners(ObjectName paramObjectName, Integer[] paramArrayOfInteger, Subject paramSubject)
    throws InstanceNotFoundException, ListenerNotFoundException, IOException;

  public abstract NotificationResult fetchNotifications(long paramLong1, int paramInt, long paramLong2)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.rmi.RMIConnection
 * JD-Core Version:    0.6.2
 */