package javax.management.remote.rmi;

import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.MarshalledObject;
import java.rmi.UnexpectedException;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;
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

public final class RMIConnectionImpl_Stub extends RemoteStub
  implements RMIConnection
{
  private static final long serialVersionUID = 2L;
  private static Method $method_addNotificationListener_0;
  private static Method $method_addNotificationListeners_1;
  private static Method $method_close_2;
  private static Method $method_createMBean_3;
  private static Method $method_createMBean_4;
  private static Method $method_createMBean_5;
  private static Method $method_createMBean_6;
  private static Method $method_fetchNotifications_7;
  private static Method $method_getAttribute_8;
  private static Method $method_getAttributes_9;
  private static Method $method_getConnectionId_10;
  private static Method $method_getDefaultDomain_11;
  private static Method $method_getDomains_12;
  private static Method $method_getMBeanCount_13;
  private static Method $method_getMBeanInfo_14;
  private static Method $method_getObjectInstance_15;
  private static Method $method_invoke_16;
  private static Method $method_isInstanceOf_17;
  private static Method $method_isRegistered_18;
  private static Method $method_queryMBeans_19;
  private static Method $method_queryNames_20;
  private static Method $method_removeNotificationListener_21;
  private static Method $method_removeNotificationListener_22;
  private static Method $method_removeNotificationListeners_23;
  private static Method $method_setAttribute_24;
  private static Method $method_setAttributes_25;
  private static Method $method_unregisterMBean_26;

  static
  {
    try
    {
      $method_addNotificationListener_0 = RMIConnection.class.getMethod("addNotificationListener", new Class[] { ObjectName.class, ObjectName.class, MarshalledObject.class, MarshalledObject.class, Subject.class });
      $method_addNotificationListeners_1 = RMIConnection.class.getMethod("addNotificationListeners", new Class[] { new ObjectName[0].getClass(), new MarshalledObject[0].getClass(), new Subject[0].getClass() });
      $method_close_2 = AutoCloseable.class.getMethod("close", new Class[0]);
      $method_createMBean_3 = RMIConnection.class.getMethod("createMBean", new Class[] { String.class, ObjectName.class, MarshalledObject.class, new String[0].getClass(), Subject.class });
      $method_createMBean_4 = RMIConnection.class.getMethod("createMBean", new Class[] { String.class, ObjectName.class, ObjectName.class, MarshalledObject.class, new String[0].getClass(), Subject.class });
      $method_createMBean_5 = RMIConnection.class.getMethod("createMBean", new Class[] { String.class, ObjectName.class, ObjectName.class, Subject.class });
      $method_createMBean_6 = RMIConnection.class.getMethod("createMBean", new Class[] { String.class, ObjectName.class, Subject.class });
      $method_fetchNotifications_7 = RMIConnection.class.getMethod("fetchNotifications", new Class[] { Long.TYPE, Integer.TYPE, Long.TYPE });
      $method_getAttribute_8 = RMIConnection.class.getMethod("getAttribute", new Class[] { ObjectName.class, String.class, Subject.class });
      $method_getAttributes_9 = RMIConnection.class.getMethod("getAttributes", new Class[] { ObjectName.class, new String[0].getClass(), Subject.class });
      $method_getConnectionId_10 = RMIConnection.class.getMethod("getConnectionId", new Class[0]);
      $method_getDefaultDomain_11 = RMIConnection.class.getMethod("getDefaultDomain", new Class[] { Subject.class });
      $method_getDomains_12 = RMIConnection.class.getMethod("getDomains", new Class[] { Subject.class });
      $method_getMBeanCount_13 = RMIConnection.class.getMethod("getMBeanCount", new Class[] { Subject.class });
      $method_getMBeanInfo_14 = RMIConnection.class.getMethod("getMBeanInfo", new Class[] { ObjectName.class, Subject.class });
      $method_getObjectInstance_15 = RMIConnection.class.getMethod("getObjectInstance", new Class[] { ObjectName.class, Subject.class });
      $method_invoke_16 = RMIConnection.class.getMethod("invoke", new Class[] { ObjectName.class, String.class, MarshalledObject.class, new String[0].getClass(), Subject.class });
      $method_isInstanceOf_17 = RMIConnection.class.getMethod("isInstanceOf", new Class[] { ObjectName.class, String.class, Subject.class });
      $method_isRegistered_18 = RMIConnection.class.getMethod("isRegistered", new Class[] { ObjectName.class, Subject.class });
      $method_queryMBeans_19 = RMIConnection.class.getMethod("queryMBeans", new Class[] { ObjectName.class, MarshalledObject.class, Subject.class });
      $method_queryNames_20 = RMIConnection.class.getMethod("queryNames", new Class[] { ObjectName.class, MarshalledObject.class, Subject.class });
      $method_removeNotificationListener_21 = RMIConnection.class.getMethod("removeNotificationListener", new Class[] { ObjectName.class, ObjectName.class, MarshalledObject.class, MarshalledObject.class, Subject.class });
      $method_removeNotificationListener_22 = RMIConnection.class.getMethod("removeNotificationListener", new Class[] { ObjectName.class, ObjectName.class, Subject.class });
      $method_removeNotificationListeners_23 = RMIConnection.class.getMethod("removeNotificationListeners", new Class[] { ObjectName.class, new Integer[0].getClass(), Subject.class });
      $method_setAttribute_24 = RMIConnection.class.getMethod("setAttribute", new Class[] { ObjectName.class, MarshalledObject.class, Subject.class });
      $method_setAttributes_25 = RMIConnection.class.getMethod("setAttributes", new Class[] { ObjectName.class, MarshalledObject.class, Subject.class });
      $method_unregisterMBean_26 = RMIConnection.class.getMethod("unregisterMBean", new Class[] { ObjectName.class, Subject.class });
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      throw new NoSuchMethodError("stub class initialization failed");
    }
  }

  public RMIConnectionImpl_Stub(RemoteRef paramRemoteRef)
  {
    super(paramRemoteRef);
  }

  public void addNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, MarshalledObject paramMarshalledObject1, MarshalledObject paramMarshalledObject2, Subject paramSubject)
    throws IOException, InstanceNotFoundException
  {
    try
    {
      this.ref.invoke(this, $method_addNotificationListener_0, new Object[] { paramObjectName1, paramObjectName2, paramMarshalledObject1, paramMarshalledObject2, paramSubject }, -8578317696269497109L);
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public Integer[] addNotificationListeners(ObjectName[] paramArrayOfObjectName, MarshalledObject[] paramArrayOfMarshalledObject, Subject[] paramArrayOfSubject)
    throws IOException, InstanceNotFoundException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_addNotificationListeners_1, new Object[] { paramArrayOfObjectName, paramArrayOfMarshalledObject, paramArrayOfSubject }, -5321691879380783377L);
      return (Integer[])localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public void close()
    throws IOException
  {
    try
    {
      this.ref.invoke(this, $method_close_2, null, -4742752445160157748L);
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public ObjectInstance createMBean(String paramString, ObjectName paramObjectName, MarshalledObject paramMarshalledObject, String[] paramArrayOfString, Subject paramSubject)
    throws IOException, InstanceAlreadyExistsException, MBeanException, MBeanRegistrationException, NotCompliantMBeanException, ReflectionException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_createMBean_3, new Object[] { paramString, paramObjectName, paramMarshalledObject, paramArrayOfString, paramSubject }, 4867822117947806114L);
      return (ObjectInstance)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceAlreadyExistsException localInstanceAlreadyExistsException)
    {
      throw localInstanceAlreadyExistsException;
    }
    catch (MBeanException localMBeanException)
    {
      throw localMBeanException;
    }
    catch (NotCompliantMBeanException localNotCompliantMBeanException)
    {
      throw localNotCompliantMBeanException;
    }
    catch (ReflectionException localReflectionException)
    {
      throw localReflectionException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2, MarshalledObject paramMarshalledObject, String[] paramArrayOfString, Subject paramSubject)
    throws IOException, InstanceAlreadyExistsException, InstanceNotFoundException, MBeanException, MBeanRegistrationException, NotCompliantMBeanException, ReflectionException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_createMBean_4, new Object[] { paramString, paramObjectName1, paramObjectName2, paramMarshalledObject, paramArrayOfString, paramSubject }, -6604955182088909937L);
      return (ObjectInstance)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceAlreadyExistsException localInstanceAlreadyExistsException)
    {
      throw localInstanceAlreadyExistsException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (MBeanException localMBeanException)
    {
      throw localMBeanException;
    }
    catch (NotCompliantMBeanException localNotCompliantMBeanException)
    {
      throw localNotCompliantMBeanException;
    }
    catch (ReflectionException localReflectionException)
    {
      throw localReflectionException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2, Subject paramSubject)
    throws IOException, InstanceAlreadyExistsException, InstanceNotFoundException, MBeanException, MBeanRegistrationException, NotCompliantMBeanException, ReflectionException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_createMBean_5, new Object[] { paramString, paramObjectName1, paramObjectName2, paramSubject }, -8679469989872508324L);
      return (ObjectInstance)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceAlreadyExistsException localInstanceAlreadyExistsException)
    {
      throw localInstanceAlreadyExistsException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (MBeanException localMBeanException)
    {
      throw localMBeanException;
    }
    catch (NotCompliantMBeanException localNotCompliantMBeanException)
    {
      throw localNotCompliantMBeanException;
    }
    catch (ReflectionException localReflectionException)
    {
      throw localReflectionException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public ObjectInstance createMBean(String paramString, ObjectName paramObjectName, Subject paramSubject)
    throws IOException, InstanceAlreadyExistsException, MBeanException, MBeanRegistrationException, NotCompliantMBeanException, ReflectionException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_createMBean_6, new Object[] { paramString, paramObjectName, paramSubject }, 2510753813974665446L);
      return (ObjectInstance)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceAlreadyExistsException localInstanceAlreadyExistsException)
    {
      throw localInstanceAlreadyExistsException;
    }
    catch (MBeanException localMBeanException)
    {
      throw localMBeanException;
    }
    catch (NotCompliantMBeanException localNotCompliantMBeanException)
    {
      throw localNotCompliantMBeanException;
    }
    catch (ReflectionException localReflectionException)
    {
      throw localReflectionException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public NotificationResult fetchNotifications(long paramLong1, int paramInt, long paramLong2)
    throws IOException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_fetchNotifications_7, new Object[] { new Long(paramLong1), new Integer(paramInt), new Long(paramLong2) }, -5037523307973544478L);
      return (NotificationResult)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public Object getAttribute(ObjectName paramObjectName, String paramString, Subject paramSubject)
    throws IOException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_getAttribute_8, new Object[] { paramObjectName, paramString, paramSubject }, -1089783104982388203L);
      return localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (AttributeNotFoundException localAttributeNotFoundException)
    {
      throw localAttributeNotFoundException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (MBeanException localMBeanException)
    {
      throw localMBeanException;
    }
    catch (ReflectionException localReflectionException)
    {
      throw localReflectionException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public AttributeList getAttributes(ObjectName paramObjectName, String[] paramArrayOfString, Subject paramSubject)
    throws IOException, InstanceNotFoundException, ReflectionException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_getAttributes_9, new Object[] { paramObjectName, paramArrayOfString, paramSubject }, 6285293806596348999L);
      return (AttributeList)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (ReflectionException localReflectionException)
    {
      throw localReflectionException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public String getConnectionId()
    throws IOException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_getConnectionId_10, null, -67907180346059933L);
      return (String)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public String getDefaultDomain(Subject paramSubject)
    throws IOException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_getDefaultDomain_11, new Object[] { paramSubject }, 6047668923998658472L);
      return (String)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public String[] getDomains(Subject paramSubject)
    throws IOException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_getDomains_12, new Object[] { paramSubject }, -6662314179953625551L);
      return (String[])localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public Integer getMBeanCount(Subject paramSubject)
    throws IOException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_getMBeanCount_13, new Object[] { paramSubject }, -2042362057335820635L);
      return (Integer)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public MBeanInfo getMBeanInfo(ObjectName paramObjectName, Subject paramSubject)
    throws IOException, InstanceNotFoundException, IntrospectionException, ReflectionException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_getMBeanInfo_14, new Object[] { paramObjectName, paramSubject }, -7404813916326233354L);
      return (MBeanInfo)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (IntrospectionException localIntrospectionException)
    {
      throw localIntrospectionException;
    }
    catch (ReflectionException localReflectionException)
    {
      throw localReflectionException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public ObjectInstance getObjectInstance(ObjectName paramObjectName, Subject paramSubject)
    throws IOException, InstanceNotFoundException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_getObjectInstance_15, new Object[] { paramObjectName, paramSubject }, 6950095694996159938L);
      return (ObjectInstance)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public Object invoke(ObjectName paramObjectName, String paramString, MarshalledObject paramMarshalledObject, String[] paramArrayOfString, Subject paramSubject)
    throws IOException, InstanceNotFoundException, MBeanException, ReflectionException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_invoke_16, new Object[] { paramObjectName, paramString, paramMarshalledObject, paramArrayOfString, paramSubject }, 1434350937885235744L);
      return localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (MBeanException localMBeanException)
    {
      throw localMBeanException;
    }
    catch (ReflectionException localReflectionException)
    {
      throw localReflectionException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public boolean isInstanceOf(ObjectName paramObjectName, String paramString, Subject paramSubject)
    throws IOException, InstanceNotFoundException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_isInstanceOf_17, new Object[] { paramObjectName, paramString, paramSubject }, -2147516868461740814L);
      return ((Boolean)localObject).booleanValue();
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public boolean isRegistered(ObjectName paramObjectName, Subject paramSubject)
    throws IOException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_isRegistered_18, new Object[] { paramObjectName, paramSubject }, 8325683335228268564L);
      return ((Boolean)localObject).booleanValue();
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public Set queryMBeans(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
    throws IOException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_queryMBeans_19, new Object[] { paramObjectName, paramMarshalledObject, paramSubject }, 2915881009400597976L);
      return (Set)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public Set queryNames(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
    throws IOException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_queryNames_20, new Object[] { paramObjectName, paramMarshalledObject, paramSubject }, 9152567528369059802L);
      return (Set)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, MarshalledObject paramMarshalledObject1, MarshalledObject paramMarshalledObject2, Subject paramSubject)
    throws IOException, InstanceNotFoundException, ListenerNotFoundException
  {
    try
    {
      this.ref.invoke(this, $method_removeNotificationListener_21, new Object[] { paramObjectName1, paramObjectName2, paramMarshalledObject1, paramMarshalledObject2, paramSubject }, 2578029900065214857L);
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (ListenerNotFoundException localListenerNotFoundException)
    {
      throw localListenerNotFoundException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, Subject paramSubject)
    throws IOException, InstanceNotFoundException, ListenerNotFoundException
  {
    try
    {
      this.ref.invoke(this, $method_removeNotificationListener_22, new Object[] { paramObjectName1, paramObjectName2, paramSubject }, 6604721169198089513L);
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (ListenerNotFoundException localListenerNotFoundException)
    {
      throw localListenerNotFoundException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public void removeNotificationListeners(ObjectName paramObjectName, Integer[] paramArrayOfInteger, Subject paramSubject)
    throws IOException, InstanceNotFoundException, ListenerNotFoundException
  {
    try
    {
      this.ref.invoke(this, $method_removeNotificationListeners_23, new Object[] { paramObjectName, paramArrayOfInteger, paramSubject }, 2549120024456183446L);
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (ListenerNotFoundException localListenerNotFoundException)
    {
      throw localListenerNotFoundException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public void setAttribute(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
    throws IOException, AttributeNotFoundException, InstanceNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
  {
    try
    {
      this.ref.invoke(this, $method_setAttribute_24, new Object[] { paramObjectName, paramMarshalledObject, paramSubject }, 6738606893952597516L);
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (AttributeNotFoundException localAttributeNotFoundException)
    {
      throw localAttributeNotFoundException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (InvalidAttributeValueException localInvalidAttributeValueException)
    {
      throw localInvalidAttributeValueException;
    }
    catch (MBeanException localMBeanException)
    {
      throw localMBeanException;
    }
    catch (ReflectionException localReflectionException)
    {
      throw localReflectionException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public AttributeList setAttributes(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
    throws IOException, InstanceNotFoundException, ReflectionException
  {
    try
    {
      Object localObject = this.ref.invoke(this, $method_setAttributes_25, new Object[] { paramObjectName, paramMarshalledObject, paramSubject }, -230470228399681820L);
      return (AttributeList)localObject;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (ReflectionException localReflectionException)
    {
      throw localReflectionException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }

  public void unregisterMBean(ObjectName paramObjectName, Subject paramSubject)
    throws IOException, InstanceNotFoundException, MBeanRegistrationException
  {
    try
    {
      this.ref.invoke(this, $method_unregisterMBean_26, new Object[] { paramObjectName, paramSubject }, -159498580868721452L);
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (InstanceNotFoundException localInstanceNotFoundException)
    {
      throw localInstanceNotFoundException;
    }
    catch (MBeanRegistrationException localMBeanRegistrationException)
    {
      throw localMBeanRegistrationException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException("undeclared checked exception", localException);
    }
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.rmi.RMIConnectionImpl_Stub
 * JD-Core Version:    0.6.2
 */