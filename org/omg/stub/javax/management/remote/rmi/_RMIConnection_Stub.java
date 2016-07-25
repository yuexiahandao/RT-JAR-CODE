package org.omg.stub.javax.management.remote.rmi;

import java.io.IOException;
import java.io.Serializable;
import java.io.SerializablePermission;
import java.rmi.MarshalledObject;
import java.rmi.UnexpectedException;
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
import javax.management.remote.rmi.RMIConnection;
import javax.rmi.CORBA.Stub;
import javax.rmi.CORBA.Util;
import javax.security.auth.Subject;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CORBA.portable.ServantObject;

public class _RMIConnection_Stub extends Stub
  implements RMIConnection
{
  private static final String[] _type_ids = { "RMI:javax.management.remote.rmi.RMIConnection:0000000000000000" };

  public _RMIConnection_Stub()
  {
    this(checkPermission());
  }

  private _RMIConnection_Stub(Void paramVoid)
  {
  }

  public String[] _ids()
  {
    return (String[])_type_ids.clone();
  }

  public void addNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, MarshalledObject paramMarshalledObject1, MarshalledObject paramMarshalledObject2, Subject paramSubject)
    throws InstanceNotFoundException, IOException
  {
    Object localObject5;
    if (!Util.isLocal(this))
    {
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("addNotificationListener", true);
          localOutputStream.write_value(paramObjectName1, ObjectName.class);
          localOutputStream.write_value(paramObjectName2, ObjectName.class);
          localOutputStream.write_value(paramMarshalledObject1, MarshalledObject.class);
          localOutputStream.write_value(paramMarshalledObject2, MarshalledObject.class);
          localOutputStream.write_value(paramSubject, Subject.class);
          _invoke(localOutputStream);
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          addNotificationListener(paramObjectName1, paramObjectName2, paramMarshalledObject1, paramMarshalledObject2, paramSubject);
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    }
    else
    {
      ServantObject localServantObject = _servant_preinvoke("addNotificationListener", RMIConnection.class);
      if (localServantObject == null)
      {
        addNotificationListener(paramObjectName1, paramObjectName2, paramMarshalledObject1, paramMarshalledObject2, paramSubject);
        return;
      }
      try
      {
        Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName1, paramObjectName2, paramMarshalledObject1, paramMarshalledObject2, paramSubject }, _orb());
        localObject5 = (ObjectName)arrayOfObject[0];
        ObjectName localObjectName = (ObjectName)arrayOfObject[1];
        MarshalledObject localMarshalledObject1 = (MarshalledObject)arrayOfObject[2];
        MarshalledObject localMarshalledObject2 = (MarshalledObject)arrayOfObject[3];
        Subject localSubject = (Subject)arrayOfObject[4];
        ((RMIConnection)localServantObject.servant).addNotificationListener((ObjectName)localObject5, localObjectName, localMarshalledObject1, localMarshalledObject2, localSubject);
      }
      catch (Throwable localThrowable)
      {
        localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
        if ((localObject5 instanceof InstanceNotFoundException))
          throw ((InstanceNotFoundException)localObject5);
        if ((localObject5 instanceof IOException))
          throw ((IOException)localObject5);
        throw Util.wrapException((Throwable)localObject5);
      }
      finally
      {
        _servant_postinvoke(localServantObject);
      }
    }
  }

  public Integer[] addNotificationListeners(ObjectName[] paramArrayOfObjectName, MarshalledObject[] paramArrayOfMarshalledObject, Subject[] paramArrayOfSubject)
    throws InstanceNotFoundException, IOException
  {
    Integer[] arrayOfInteger1;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("addNotificationListeners", true);
          localOutputStream.write_value(cast_array(paramArrayOfObjectName), new ObjectName[0].getClass());
          localOutputStream.write_value(cast_array(paramArrayOfMarshalledObject), new MarshalledObject[0].getClass());
          localOutputStream.write_value(cast_array(paramArrayOfSubject), new Subject[0].getClass());
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          arrayOfInteger1 = (Integer[])localInputStream.read_value(new Integer[0].getClass());
          return arrayOfInteger1;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          arrayOfInteger1 = addNotificationListeners(paramArrayOfObjectName, paramArrayOfMarshalledObject, paramArrayOfSubject);
          return arrayOfInteger1;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("addNotificationListeners", RMIConnection.class);
    if (localServantObject == null)
      return addNotificationListeners(paramArrayOfObjectName, paramArrayOfMarshalledObject, paramArrayOfSubject);
    try
    {
      Object[] arrayOfObject = Util.copyObjects(new Object[] { paramArrayOfObjectName, paramArrayOfMarshalledObject, paramArrayOfSubject }, _orb());
      localObject5 = (ObjectName[])arrayOfObject[0];
      MarshalledObject[] arrayOfMarshalledObject = (MarshalledObject[])arrayOfObject[1];
      Subject[] arrayOfSubject = (Subject[])arrayOfObject[2];
      Integer[] arrayOfInteger2 = ((RMIConnection)localServantObject.servant).addNotificationListeners((ObjectName[])localObject5, arrayOfMarshalledObject, arrayOfSubject);
      arrayOfInteger1 = (Integer[])Util.copyObject(arrayOfInteger2, _orb());
      return arrayOfInteger1;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof InstanceNotFoundException))
        throw ((InstanceNotFoundException)localObject5);
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  private Serializable cast_array(Object paramObject)
  {
    return (Serializable)paramObject;
  }

  private static Void checkPermission()
  {
    SecurityManager localSecurityManager = System.getSecurityManager();
    if (localSecurityManager != null)
      localSecurityManager.checkPermission(new SerializablePermission("enableSubclassImplementation"));
    return null;
  }

  public void close()
    throws IOException
  {
    Object localObject5;
    if (!Util.isLocal(this))
    {
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA.portable.OutputStream localOutputStream = _request("close", true);
          _invoke(localOutputStream);
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          close();
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    }
    else
    {
      ServantObject localServantObject = _servant_preinvoke("close", RMIConnection.class);
      if (localServantObject == null)
      {
        close();
        return;
      }
      try
      {
        ((RMIConnection)localServantObject.servant).close();
      }
      catch (Throwable localThrowable)
      {
        localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
        if ((localObject5 instanceof IOException))
          throw ((IOException)localObject5);
        throw Util.wrapException((Throwable)localObject5);
      }
      finally
      {
        _servant_postinvoke(localServantObject);
      }
    }
  }

  public ObjectInstance createMBean(String paramString, ObjectName paramObjectName, MarshalledObject paramMarshalledObject, String[] paramArrayOfString, Subject paramSubject)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException
  {
    ObjectInstance localObjectInstance1;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("createMBean__CORBA_WStringValue__javax_management_ObjectName__java_rmi_MarshalledObject__org_omg_boxedRMI_CORBA_seq1_WStringValue__javax_security_auth_Subject", true);
          localOutputStream.write_value(paramString, String.class);
          localOutputStream.write_value(paramObjectName, ObjectName.class);
          localOutputStream.write_value(paramMarshalledObject, MarshalledObject.class);
          localOutputStream.write_value(cast_array(paramArrayOfString), new String[0].getClass());
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          localObjectInstance1 = (ObjectInstance)localInputStream.read_value(ObjectInstance.class);
          return localObjectInstance1;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/ReflectionEx:1.0"))
            throw ((ReflectionException)localInputStream.read_value(ReflectionException.class));
          if (((String)localObject5).equals("IDL:javax/management/InstanceAlreadyExistsEx:1.0"))
            throw ((InstanceAlreadyExistsException)localInputStream.read_value(InstanceAlreadyExistsException.class));
          if (((String)localObject5).equals("IDL:javax/management/MBeanRegistrationEx:1.0"))
            throw ((MBeanRegistrationException)localInputStream.read_value(MBeanRegistrationException.class));
          if (((String)localObject5).equals("IDL:javax/management/MBeanEx:1.0"))
            throw ((MBeanException)localInputStream.read_value(MBeanException.class));
          if (((String)localObject5).equals("IDL:javax/management/NotCompliantMBeanEx:1.0"))
            throw ((NotCompliantMBeanException)localInputStream.read_value(NotCompliantMBeanException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          localObjectInstance1 = createMBean(paramString, paramObjectName, paramMarshalledObject, paramArrayOfString, paramSubject);
          return localObjectInstance1;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("createMBean__CORBA_WStringValue__javax_management_ObjectName__java_rmi_MarshalledObject__org_omg_boxedRMI_CORBA_seq1_WStringValue__javax_security_auth_Subject", RMIConnection.class);
    if (localServantObject == null)
      return createMBean(paramString, paramObjectName, paramMarshalledObject, paramArrayOfString, paramSubject);
    try
    {
      Object[] arrayOfObject = Util.copyObjects(new Object[] { paramString, paramObjectName, paramMarshalledObject, paramArrayOfString, paramSubject }, _orb());
      localObject5 = (String)arrayOfObject[0];
      ObjectName localObjectName = (ObjectName)arrayOfObject[1];
      MarshalledObject localMarshalledObject = (MarshalledObject)arrayOfObject[2];
      String[] arrayOfString = (String[])arrayOfObject[3];
      Subject localSubject = (Subject)arrayOfObject[4];
      ObjectInstance localObjectInstance2 = ((RMIConnection)localServantObject.servant).createMBean((String)localObject5, localObjectName, localMarshalledObject, arrayOfString, localSubject);
      localObjectInstance1 = (ObjectInstance)Util.copyObject(localObjectInstance2, _orb());
      return localObjectInstance1;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof ReflectionException))
        throw ((ReflectionException)localObject5);
      if ((localObject5 instanceof InstanceAlreadyExistsException))
        throw ((InstanceAlreadyExistsException)localObject5);
      if ((localObject5 instanceof MBeanRegistrationException))
        throw ((MBeanRegistrationException)localObject5);
      if ((localObject5 instanceof MBeanException))
        throw ((MBeanException)localObject5);
      if ((localObject5 instanceof NotCompliantMBeanException))
        throw ((NotCompliantMBeanException)localObject5);
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2, MarshalledObject paramMarshalledObject, String[] paramArrayOfString, Subject paramSubject)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException
  {
    ObjectInstance localObjectInstance1;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_management_ObjectName__java_rmi_MarshalledObject__org_omg_boxedRMI_CORBA_seq1_WStringValue__javax_security_auth_Subject", true);
          localOutputStream.write_value(paramString, String.class);
          localOutputStream.write_value(paramObjectName1, ObjectName.class);
          localOutputStream.write_value(paramObjectName2, ObjectName.class);
          localOutputStream.write_value(paramMarshalledObject, MarshalledObject.class);
          localOutputStream.write_value(cast_array(paramArrayOfString), new String[0].getClass());
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          localObjectInstance1 = (ObjectInstance)localInputStream.read_value(ObjectInstance.class);
          return localObjectInstance1;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/ReflectionEx:1.0"))
            throw ((ReflectionException)localInputStream.read_value(ReflectionException.class));
          if (((String)localObject5).equals("IDL:javax/management/InstanceAlreadyExistsEx:1.0"))
            throw ((InstanceAlreadyExistsException)localInputStream.read_value(InstanceAlreadyExistsException.class));
          if (((String)localObject5).equals("IDL:javax/management/MBeanRegistrationEx:1.0"))
            throw ((MBeanRegistrationException)localInputStream.read_value(MBeanRegistrationException.class));
          if (((String)localObject5).equals("IDL:javax/management/MBeanEx:1.0"))
            throw ((MBeanException)localInputStream.read_value(MBeanException.class));
          if (((String)localObject5).equals("IDL:javax/management/NotCompliantMBeanEx:1.0"))
            throw ((NotCompliantMBeanException)localInputStream.read_value(NotCompliantMBeanException.class));
          if (((String)localObject5).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          localObjectInstance1 = createMBean(paramString, paramObjectName1, paramObjectName2, paramMarshalledObject, paramArrayOfString, paramSubject);
          return localObjectInstance1;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_management_ObjectName__java_rmi_MarshalledObject__org_omg_boxedRMI_CORBA_seq1_WStringValue__javax_security_auth_Subject", RMIConnection.class);
    if (localServantObject == null)
      return createMBean(paramString, paramObjectName1, paramObjectName2, paramMarshalledObject, paramArrayOfString, paramSubject);
    try
    {
      Object[] arrayOfObject = Util.copyObjects(new Object[] { paramString, paramObjectName1, paramObjectName2, paramMarshalledObject, paramArrayOfString, paramSubject }, _orb());
      localObject5 = (String)arrayOfObject[0];
      ObjectName localObjectName1 = (ObjectName)arrayOfObject[1];
      ObjectName localObjectName2 = (ObjectName)arrayOfObject[2];
      MarshalledObject localMarshalledObject = (MarshalledObject)arrayOfObject[3];
      String[] arrayOfString = (String[])arrayOfObject[4];
      Subject localSubject = (Subject)arrayOfObject[5];
      ObjectInstance localObjectInstance2 = ((RMIConnection)localServantObject.servant).createMBean((String)localObject5, localObjectName1, localObjectName2, localMarshalledObject, arrayOfString, localSubject);
      localObjectInstance1 = (ObjectInstance)Util.copyObject(localObjectInstance2, _orb());
      return localObjectInstance1;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof ReflectionException))
        throw ((ReflectionException)localObject5);
      if ((localObject5 instanceof InstanceAlreadyExistsException))
        throw ((InstanceAlreadyExistsException)localObject5);
      if ((localObject5 instanceof MBeanRegistrationException))
        throw ((MBeanRegistrationException)localObject5);
      if ((localObject5 instanceof MBeanException))
        throw ((MBeanException)localObject5);
      if ((localObject5 instanceof NotCompliantMBeanException))
        throw ((NotCompliantMBeanException)localObject5);
      if ((localObject5 instanceof InstanceNotFoundException))
        throw ((InstanceNotFoundException)localObject5);
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2, Subject paramSubject)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException
  {
    ObjectInstance localObjectInstance1;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_management_ObjectName__javax_security_auth_Subject", true);
          localOutputStream.write_value(paramString, String.class);
          localOutputStream.write_value(paramObjectName1, ObjectName.class);
          localOutputStream.write_value(paramObjectName2, ObjectName.class);
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          localObjectInstance1 = (ObjectInstance)localInputStream.read_value(ObjectInstance.class);
          return localObjectInstance1;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/ReflectionEx:1.0"))
            throw ((ReflectionException)localInputStream.read_value(ReflectionException.class));
          if (((String)localObject5).equals("IDL:javax/management/InstanceAlreadyExistsEx:1.0"))
            throw ((InstanceAlreadyExistsException)localInputStream.read_value(InstanceAlreadyExistsException.class));
          if (((String)localObject5).equals("IDL:javax/management/MBeanRegistrationEx:1.0"))
            throw ((MBeanRegistrationException)localInputStream.read_value(MBeanRegistrationException.class));
          if (((String)localObject5).equals("IDL:javax/management/MBeanEx:1.0"))
            throw ((MBeanException)localInputStream.read_value(MBeanException.class));
          if (((String)localObject5).equals("IDL:javax/management/NotCompliantMBeanEx:1.0"))
            throw ((NotCompliantMBeanException)localInputStream.read_value(NotCompliantMBeanException.class));
          if (((String)localObject5).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          localObjectInstance1 = createMBean(paramString, paramObjectName1, paramObjectName2, paramSubject);
          return localObjectInstance1;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_management_ObjectName__javax_security_auth_Subject", RMIConnection.class);
    if (localServantObject == null)
      return createMBean(paramString, paramObjectName1, paramObjectName2, paramSubject);
    try
    {
      Object[] arrayOfObject = Util.copyObjects(new Object[] { paramString, paramObjectName1, paramObjectName2, paramSubject }, _orb());
      localObject5 = (String)arrayOfObject[0];
      ObjectName localObjectName1 = (ObjectName)arrayOfObject[1];
      ObjectName localObjectName2 = (ObjectName)arrayOfObject[2];
      Subject localSubject = (Subject)arrayOfObject[3];
      ObjectInstance localObjectInstance2 = ((RMIConnection)localServantObject.servant).createMBean((String)localObject5, localObjectName1, localObjectName2, localSubject);
      localObjectInstance1 = (ObjectInstance)Util.copyObject(localObjectInstance2, _orb());
      return localObjectInstance1;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof ReflectionException))
        throw ((ReflectionException)localObject5);
      if ((localObject5 instanceof InstanceAlreadyExistsException))
        throw ((InstanceAlreadyExistsException)localObject5);
      if ((localObject5 instanceof MBeanRegistrationException))
        throw ((MBeanRegistrationException)localObject5);
      if ((localObject5 instanceof MBeanException))
        throw ((MBeanException)localObject5);
      if ((localObject5 instanceof NotCompliantMBeanException))
        throw ((NotCompliantMBeanException)localObject5);
      if ((localObject5 instanceof InstanceNotFoundException))
        throw ((InstanceNotFoundException)localObject5);
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public ObjectInstance createMBean(String paramString, ObjectName paramObjectName, Subject paramSubject)
    throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException
  {
    ObjectInstance localObjectInstance1;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_security_auth_Subject", true);
          localOutputStream.write_value(paramString, String.class);
          localOutputStream.write_value(paramObjectName, ObjectName.class);
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          localObjectInstance1 = (ObjectInstance)localInputStream.read_value(ObjectInstance.class);
          return localObjectInstance1;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/ReflectionEx:1.0"))
            throw ((ReflectionException)localInputStream.read_value(ReflectionException.class));
          if (((String)localObject5).equals("IDL:javax/management/InstanceAlreadyExistsEx:1.0"))
            throw ((InstanceAlreadyExistsException)localInputStream.read_value(InstanceAlreadyExistsException.class));
          if (((String)localObject5).equals("IDL:javax/management/MBeanRegistrationEx:1.0"))
            throw ((MBeanRegistrationException)localInputStream.read_value(MBeanRegistrationException.class));
          if (((String)localObject5).equals("IDL:javax/management/MBeanEx:1.0"))
            throw ((MBeanException)localInputStream.read_value(MBeanException.class));
          if (((String)localObject5).equals("IDL:javax/management/NotCompliantMBeanEx:1.0"))
            throw ((NotCompliantMBeanException)localInputStream.read_value(NotCompliantMBeanException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          localObjectInstance1 = createMBean(paramString, paramObjectName, paramSubject);
          return localObjectInstance1;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_security_auth_Subject", RMIConnection.class);
    if (localServantObject == null)
      return createMBean(paramString, paramObjectName, paramSubject);
    try
    {
      Object[] arrayOfObject = Util.copyObjects(new Object[] { paramString, paramObjectName, paramSubject }, _orb());
      localObject5 = (String)arrayOfObject[0];
      ObjectName localObjectName = (ObjectName)arrayOfObject[1];
      Subject localSubject = (Subject)arrayOfObject[2];
      ObjectInstance localObjectInstance2 = ((RMIConnection)localServantObject.servant).createMBean((String)localObject5, localObjectName, localSubject);
      localObjectInstance1 = (ObjectInstance)Util.copyObject(localObjectInstance2, _orb());
      return localObjectInstance1;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof ReflectionException))
        throw ((ReflectionException)localObject5);
      if ((localObject5 instanceof InstanceAlreadyExistsException))
        throw ((InstanceAlreadyExistsException)localObject5);
      if ((localObject5 instanceof MBeanRegistrationException))
        throw ((MBeanRegistrationException)localObject5);
      if ((localObject5 instanceof MBeanException))
        throw ((MBeanException)localObject5);
      if ((localObject5 instanceof NotCompliantMBeanException))
        throw ((NotCompliantMBeanException)localObject5);
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public NotificationResult fetchNotifications(long paramLong1, int paramInt, long paramLong2)
    throws IOException
  {
    NotificationResult localNotificationResult1;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA.portable.OutputStream localOutputStream = _request("fetchNotifications", true);
          localOutputStream.write_longlong(paramLong1);
          localOutputStream.write_long(paramInt);
          localOutputStream.write_longlong(paramLong2);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          localNotificationResult1 = (NotificationResult)localInputStream.read_value(NotificationResult.class);
          return localNotificationResult1;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          localNotificationResult1 = fetchNotifications(paramLong1, paramInt, paramLong2);
          return localNotificationResult1;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("fetchNotifications", RMIConnection.class);
    if (localServantObject == null)
      return fetchNotifications(paramLong1, paramInt, paramLong2);
    try
    {
      NotificationResult localNotificationResult2 = ((RMIConnection)localServantObject.servant).fetchNotifications(paramLong1, paramInt, paramLong2);
      localNotificationResult1 = (NotificationResult)Util.copyObject(localNotificationResult2, _orb());
      return localNotificationResult1;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public Object getAttribute(ObjectName paramObjectName, String paramString, Subject paramSubject)
    throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException
  {
    Object localObject1;
    Object localObject6;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("getAttribute", true);
          localOutputStream.write_value(paramObjectName, ObjectName.class);
          localOutputStream.write_value(paramString, String.class);
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          localObject1 = Util.readAny(localInputStream);
          return localObject1;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject6 = localInputStream.read_string();
          if (((String)localObject6).equals("IDL:javax/management/MBeanEx:1.0"))
            throw ((MBeanException)localInputStream.read_value(MBeanException.class));
          if (((String)localObject6).equals("IDL:javax/management/AttributeNotFoundEx:1.0"))
            throw ((AttributeNotFoundException)localInputStream.read_value(AttributeNotFoundException.class));
          if (((String)localObject6).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject6).equals("IDL:javax/management/ReflectionEx:1.0"))
            throw ((ReflectionException)localInputStream.read_value(ReflectionException.class));
          if (((String)localObject6).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject6);
        }
        catch (RemarshalException localRemarshalException)
        {
          localObject1 = getAttribute(paramObjectName, paramString, paramSubject);
          return localObject1;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("getAttribute", RMIConnection.class);
    if (localServantObject == null)
      return getAttribute(paramObjectName, paramString, paramSubject);
    try
    {
      Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName, paramString, paramSubject }, _orb());
      localObject6 = (ObjectName)arrayOfObject[0];
      String str = (String)arrayOfObject[1];
      Subject localSubject = (Subject)arrayOfObject[2];
      Object localObject7 = ((RMIConnection)localServantObject.servant).getAttribute((ObjectName)localObject6, str, localSubject);
      localObject1 = Util.copyObject(localObject7, _orb());
      return localObject1;
    }
    catch (Throwable localThrowable)
    {
      localObject6 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject6 instanceof MBeanException))
        throw ((MBeanException)localObject6);
      if ((localObject6 instanceof AttributeNotFoundException))
        throw ((AttributeNotFoundException)localObject6);
      if ((localObject6 instanceof InstanceNotFoundException))
        throw ((InstanceNotFoundException)localObject6);
      if ((localObject6 instanceof ReflectionException))
        throw ((ReflectionException)localObject6);
      if ((localObject6 instanceof IOException))
        throw ((IOException)localObject6);
      throw Util.wrapException((Throwable)localObject6);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public AttributeList getAttributes(ObjectName paramObjectName, String[] paramArrayOfString, Subject paramSubject)
    throws InstanceNotFoundException, ReflectionException, IOException
  {
    AttributeList localAttributeList1;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("getAttributes", true);
          localOutputStream.write_value(paramObjectName, ObjectName.class);
          localOutputStream.write_value(cast_array(paramArrayOfString), new String[0].getClass());
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          localAttributeList1 = (AttributeList)localInputStream.read_value(AttributeList.class);
          return localAttributeList1;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject5).equals("IDL:javax/management/ReflectionEx:1.0"))
            throw ((ReflectionException)localInputStream.read_value(ReflectionException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          localAttributeList1 = getAttributes(paramObjectName, paramArrayOfString, paramSubject);
          return localAttributeList1;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("getAttributes", RMIConnection.class);
    if (localServantObject == null)
      return getAttributes(paramObjectName, paramArrayOfString, paramSubject);
    try
    {
      Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName, paramArrayOfString, paramSubject }, _orb());
      localObject5 = (ObjectName)arrayOfObject[0];
      String[] arrayOfString = (String[])arrayOfObject[1];
      Subject localSubject = (Subject)arrayOfObject[2];
      AttributeList localAttributeList2 = ((RMIConnection)localServantObject.servant).getAttributes((ObjectName)localObject5, arrayOfString, localSubject);
      localAttributeList1 = (AttributeList)Util.copyObject(localAttributeList2, _orb());
      return localAttributeList1;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof InstanceNotFoundException))
        throw ((InstanceNotFoundException)localObject5);
      if ((localObject5 instanceof ReflectionException))
        throw ((ReflectionException)localObject5);
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public String getConnectionId()
    throws IOException
  {
    String str;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA.portable.OutputStream localOutputStream = _request("getConnectionId", true);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          str = (String)localInputStream.read_value(String.class);
          return str;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          str = getConnectionId();
          return str;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("getConnectionId", RMIConnection.class);
    if (localServantObject == null)
      return getConnectionId();
    try
    {
      str = ((RMIConnection)localServantObject.servant).getConnectionId();
      return str;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public String getDefaultDomain(Subject paramSubject)
    throws IOException
  {
    String str;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("getDefaultDomain", true);
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          str = (String)localInputStream.read_value(String.class);
          return str;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          str = getDefaultDomain(paramSubject);
          return str;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("getDefaultDomain", RMIConnection.class);
    if (localServantObject == null)
      return getDefaultDomain(paramSubject);
    try
    {
      Subject localSubject = (Subject)Util.copyObject(paramSubject, _orb());
      str = ((RMIConnection)localServantObject.servant).getDefaultDomain(localSubject);
      return str;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public String[] getDomains(Subject paramSubject)
    throws IOException
  {
    String[] arrayOfString;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("getDomains", true);
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          arrayOfString = (String[])localInputStream.read_value(new String[0].getClass());
          return arrayOfString;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          arrayOfString = getDomains(paramSubject);
          return arrayOfString;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("getDomains", RMIConnection.class);
    if (localServantObject == null)
      return getDomains(paramSubject);
    try
    {
      Subject localSubject = (Subject)Util.copyObject(paramSubject, _orb());
      localObject5 = ((RMIConnection)localServantObject.servant).getDomains(localSubject);
      arrayOfString = (String[])Util.copyObject(localObject5, _orb());
      return arrayOfString;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public Integer getMBeanCount(Subject paramSubject)
    throws IOException
  {
    Integer localInteger;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("getMBeanCount", true);
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          localInteger = (Integer)localInputStream.read_value(Integer.class);
          return localInteger;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          localInteger = getMBeanCount(paramSubject);
          return localInteger;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("getMBeanCount", RMIConnection.class);
    if (localServantObject == null)
      return getMBeanCount(paramSubject);
    try
    {
      Subject localSubject = (Subject)Util.copyObject(paramSubject, _orb());
      localObject5 = ((RMIConnection)localServantObject.servant).getMBeanCount(localSubject);
      localInteger = (Integer)Util.copyObject(localObject5, _orb());
      return localInteger;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public MBeanInfo getMBeanInfo(ObjectName paramObjectName, Subject paramSubject)
    throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException
  {
    MBeanInfo localMBeanInfo1;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("getMBeanInfo", true);
          localOutputStream.write_value(paramObjectName, ObjectName.class);
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          localMBeanInfo1 = (MBeanInfo)localInputStream.read_value(MBeanInfo.class);
          return localMBeanInfo1;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject5).equals("IDL:javax/management/IntrospectionEx:1.0"))
            throw ((IntrospectionException)localInputStream.read_value(IntrospectionException.class));
          if (((String)localObject5).equals("IDL:javax/management/ReflectionEx:1.0"))
            throw ((ReflectionException)localInputStream.read_value(ReflectionException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          localMBeanInfo1 = getMBeanInfo(paramObjectName, paramSubject);
          return localMBeanInfo1;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("getMBeanInfo", RMIConnection.class);
    if (localServantObject == null)
      return getMBeanInfo(paramObjectName, paramSubject);
    try
    {
      Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName, paramSubject }, _orb());
      localObject5 = (ObjectName)arrayOfObject[0];
      Subject localSubject = (Subject)arrayOfObject[1];
      MBeanInfo localMBeanInfo2 = ((RMIConnection)localServantObject.servant).getMBeanInfo((ObjectName)localObject5, localSubject);
      localMBeanInfo1 = (MBeanInfo)Util.copyObject(localMBeanInfo2, _orb());
      return localMBeanInfo1;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof InstanceNotFoundException))
        throw ((InstanceNotFoundException)localObject5);
      if ((localObject5 instanceof IntrospectionException))
        throw ((IntrospectionException)localObject5);
      if ((localObject5 instanceof ReflectionException))
        throw ((ReflectionException)localObject5);
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public ObjectInstance getObjectInstance(ObjectName paramObjectName, Subject paramSubject)
    throws InstanceNotFoundException, IOException
  {
    ObjectInstance localObjectInstance1;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("getObjectInstance", true);
          localOutputStream.write_value(paramObjectName, ObjectName.class);
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          localObjectInstance1 = (ObjectInstance)localInputStream.read_value(ObjectInstance.class);
          return localObjectInstance1;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          localObjectInstance1 = getObjectInstance(paramObjectName, paramSubject);
          return localObjectInstance1;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("getObjectInstance", RMIConnection.class);
    if (localServantObject == null)
      return getObjectInstance(paramObjectName, paramSubject);
    try
    {
      Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName, paramSubject }, _orb());
      localObject5 = (ObjectName)arrayOfObject[0];
      Subject localSubject = (Subject)arrayOfObject[1];
      ObjectInstance localObjectInstance2 = ((RMIConnection)localServantObject.servant).getObjectInstance((ObjectName)localObject5, localSubject);
      localObjectInstance1 = (ObjectInstance)Util.copyObject(localObjectInstance2, _orb());
      return localObjectInstance1;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof InstanceNotFoundException))
        throw ((InstanceNotFoundException)localObject5);
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public Object invoke(ObjectName paramObjectName, String paramString, MarshalledObject paramMarshalledObject, String[] paramArrayOfString, Subject paramSubject)
    throws InstanceNotFoundException, MBeanException, ReflectionException, IOException
  {
    Object localObject1;
    Object localObject6;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("invoke", true);
          localOutputStream.write_value(paramObjectName, ObjectName.class);
          localOutputStream.write_value(paramString, String.class);
          localOutputStream.write_value(paramMarshalledObject, MarshalledObject.class);
          localOutputStream.write_value(cast_array(paramArrayOfString), new String[0].getClass());
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          localObject1 = Util.readAny(localInputStream);
          return localObject1;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject6 = localInputStream.read_string();
          if (((String)localObject6).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject6).equals("IDL:javax/management/MBeanEx:1.0"))
            throw ((MBeanException)localInputStream.read_value(MBeanException.class));
          if (((String)localObject6).equals("IDL:javax/management/ReflectionEx:1.0"))
            throw ((ReflectionException)localInputStream.read_value(ReflectionException.class));
          if (((String)localObject6).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject6);
        }
        catch (RemarshalException localRemarshalException)
        {
          localObject1 = invoke(paramObjectName, paramString, paramMarshalledObject, paramArrayOfString, paramSubject);
          return localObject1;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("invoke", RMIConnection.class);
    if (localServantObject == null)
      return invoke(paramObjectName, paramString, paramMarshalledObject, paramArrayOfString, paramSubject);
    try
    {
      Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName, paramString, paramMarshalledObject, paramArrayOfString, paramSubject }, _orb());
      localObject6 = (ObjectName)arrayOfObject[0];
      String str = (String)arrayOfObject[1];
      MarshalledObject localMarshalledObject = (MarshalledObject)arrayOfObject[2];
      String[] arrayOfString = (String[])arrayOfObject[3];
      Subject localSubject = (Subject)arrayOfObject[4];
      Object localObject7 = ((RMIConnection)localServantObject.servant).invoke((ObjectName)localObject6, str, localMarshalledObject, arrayOfString, localSubject);
      localObject1 = Util.copyObject(localObject7, _orb());
      return localObject1;
    }
    catch (Throwable localThrowable)
    {
      localObject6 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject6 instanceof InstanceNotFoundException))
        throw ((InstanceNotFoundException)localObject6);
      if ((localObject6 instanceof MBeanException))
        throw ((MBeanException)localObject6);
      if ((localObject6 instanceof ReflectionException))
        throw ((ReflectionException)localObject6);
      if ((localObject6 instanceof IOException))
        throw ((IOException)localObject6);
      throw Util.wrapException((Throwable)localObject6);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public boolean isInstanceOf(ObjectName paramObjectName, String paramString, Subject paramSubject)
    throws InstanceNotFoundException, IOException
  {
    boolean bool;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("isInstanceOf", true);
          localOutputStream.write_value(paramObjectName, ObjectName.class);
          localOutputStream.write_value(paramString, String.class);
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          bool = localInputStream.read_boolean();
          return bool;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          bool = isInstanceOf(paramObjectName, paramString, paramSubject);
          return bool;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("isInstanceOf", RMIConnection.class);
    if (localServantObject == null)
      return isInstanceOf(paramObjectName, paramString, paramSubject);
    try
    {
      Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName, paramString, paramSubject }, _orb());
      localObject5 = (ObjectName)arrayOfObject[0];
      String str = (String)arrayOfObject[1];
      Subject localSubject = (Subject)arrayOfObject[2];
      bool = ((RMIConnection)localServantObject.servant).isInstanceOf((ObjectName)localObject5, str, localSubject);
      return bool;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof InstanceNotFoundException))
        throw ((InstanceNotFoundException)localObject5);
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public boolean isRegistered(ObjectName paramObjectName, Subject paramSubject)
    throws IOException
  {
    boolean bool;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("isRegistered", true);
          localOutputStream.write_value(paramObjectName, ObjectName.class);
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          bool = localInputStream.read_boolean();
          return bool;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          bool = isRegistered(paramObjectName, paramSubject);
          return bool;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("isRegistered", RMIConnection.class);
    if (localServantObject == null)
      return isRegistered(paramObjectName, paramSubject);
    try
    {
      Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName, paramSubject }, _orb());
      localObject5 = (ObjectName)arrayOfObject[0];
      Subject localSubject = (Subject)arrayOfObject[1];
      bool = ((RMIConnection)localServantObject.servant).isRegistered((ObjectName)localObject5, localSubject);
      return bool;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public Set queryMBeans(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
    throws IOException
  {
    Set localSet1;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("queryMBeans", true);
          localOutputStream.write_value(paramObjectName, ObjectName.class);
          localOutputStream.write_value(paramMarshalledObject, MarshalledObject.class);
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          localSet1 = (Set)localInputStream.read_value(Set.class);
          return localSet1;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          localSet1 = queryMBeans(paramObjectName, paramMarshalledObject, paramSubject);
          return localSet1;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("queryMBeans", RMIConnection.class);
    if (localServantObject == null)
      return queryMBeans(paramObjectName, paramMarshalledObject, paramSubject);
    try
    {
      Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName, paramMarshalledObject, paramSubject }, _orb());
      localObject5 = (ObjectName)arrayOfObject[0];
      MarshalledObject localMarshalledObject = (MarshalledObject)arrayOfObject[1];
      Subject localSubject = (Subject)arrayOfObject[2];
      Set localSet2 = ((RMIConnection)localServantObject.servant).queryMBeans((ObjectName)localObject5, localMarshalledObject, localSubject);
      localSet1 = (Set)Util.copyObject(localSet2, _orb());
      return localSet1;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public Set queryNames(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
    throws IOException
  {
    Set localSet1;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("queryNames", true);
          localOutputStream.write_value(paramObjectName, ObjectName.class);
          localOutputStream.write_value(paramMarshalledObject, MarshalledObject.class);
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          localSet1 = (Set)localInputStream.read_value(Set.class);
          return localSet1;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          localSet1 = queryNames(paramObjectName, paramMarshalledObject, paramSubject);
          return localSet1;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("queryNames", RMIConnection.class);
    if (localServantObject == null)
      return queryNames(paramObjectName, paramMarshalledObject, paramSubject);
    try
    {
      Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName, paramMarshalledObject, paramSubject }, _orb());
      localObject5 = (ObjectName)arrayOfObject[0];
      MarshalledObject localMarshalledObject = (MarshalledObject)arrayOfObject[1];
      Subject localSubject = (Subject)arrayOfObject[2];
      Set localSet2 = ((RMIConnection)localServantObject.servant).queryNames((ObjectName)localObject5, localMarshalledObject, localSubject);
      localSet1 = (Set)Util.copyObject(localSet2, _orb());
      return localSet1;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, MarshalledObject paramMarshalledObject1, MarshalledObject paramMarshalledObject2, Subject paramSubject)
    throws InstanceNotFoundException, ListenerNotFoundException, IOException
  {
    Object localObject5;
    if (!Util.isLocal(this))
    {
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("removeNotificationListener__javax_management_ObjectName__javax_management_ObjectName__java_rmi_MarshalledObject__java_rmi_MarshalledObject__javax_security_auth_Subject", true);
          localOutputStream.write_value(paramObjectName1, ObjectName.class);
          localOutputStream.write_value(paramObjectName2, ObjectName.class);
          localOutputStream.write_value(paramMarshalledObject1, MarshalledObject.class);
          localOutputStream.write_value(paramMarshalledObject2, MarshalledObject.class);
          localOutputStream.write_value(paramSubject, Subject.class);
          _invoke(localOutputStream);
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject5).equals("IDL:javax/management/ListenerNotFoundEx:1.0"))
            throw ((ListenerNotFoundException)localInputStream.read_value(ListenerNotFoundException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          removeNotificationListener(paramObjectName1, paramObjectName2, paramMarshalledObject1, paramMarshalledObject2, paramSubject);
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    }
    else
    {
      ServantObject localServantObject = _servant_preinvoke("removeNotificationListener__javax_management_ObjectName__javax_management_ObjectName__java_rmi_MarshalledObject__java_rmi_MarshalledObject__javax_security_auth_Subject", RMIConnection.class);
      if (localServantObject == null)
      {
        removeNotificationListener(paramObjectName1, paramObjectName2, paramMarshalledObject1, paramMarshalledObject2, paramSubject);
        return;
      }
      try
      {
        Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName1, paramObjectName2, paramMarshalledObject1, paramMarshalledObject2, paramSubject }, _orb());
        localObject5 = (ObjectName)arrayOfObject[0];
        ObjectName localObjectName = (ObjectName)arrayOfObject[1];
        MarshalledObject localMarshalledObject1 = (MarshalledObject)arrayOfObject[2];
        MarshalledObject localMarshalledObject2 = (MarshalledObject)arrayOfObject[3];
        Subject localSubject = (Subject)arrayOfObject[4];
        ((RMIConnection)localServantObject.servant).removeNotificationListener((ObjectName)localObject5, localObjectName, localMarshalledObject1, localMarshalledObject2, localSubject);
      }
      catch (Throwable localThrowable)
      {
        localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
        if ((localObject5 instanceof InstanceNotFoundException))
          throw ((InstanceNotFoundException)localObject5);
        if ((localObject5 instanceof ListenerNotFoundException))
          throw ((ListenerNotFoundException)localObject5);
        if ((localObject5 instanceof IOException))
          throw ((IOException)localObject5);
        throw Util.wrapException((Throwable)localObject5);
      }
      finally
      {
        _servant_postinvoke(localServantObject);
      }
    }
  }

  public void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, Subject paramSubject)
    throws InstanceNotFoundException, ListenerNotFoundException, IOException
  {
    Object localObject5;
    if (!Util.isLocal(this))
    {
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("removeNotificationListener__javax_management_ObjectName__javax_management_ObjectName__javax_security_auth_Subject", true);
          localOutputStream.write_value(paramObjectName1, ObjectName.class);
          localOutputStream.write_value(paramObjectName2, ObjectName.class);
          localOutputStream.write_value(paramSubject, Subject.class);
          _invoke(localOutputStream);
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject5).equals("IDL:javax/management/ListenerNotFoundEx:1.0"))
            throw ((ListenerNotFoundException)localInputStream.read_value(ListenerNotFoundException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          removeNotificationListener(paramObjectName1, paramObjectName2, paramSubject);
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    }
    else
    {
      ServantObject localServantObject = _servant_preinvoke("removeNotificationListener__javax_management_ObjectName__javax_management_ObjectName__javax_security_auth_Subject", RMIConnection.class);
      if (localServantObject == null)
      {
        removeNotificationListener(paramObjectName1, paramObjectName2, paramSubject);
        return;
      }
      try
      {
        Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName1, paramObjectName2, paramSubject }, _orb());
        localObject5 = (ObjectName)arrayOfObject[0];
        ObjectName localObjectName = (ObjectName)arrayOfObject[1];
        Subject localSubject = (Subject)arrayOfObject[2];
        ((RMIConnection)localServantObject.servant).removeNotificationListener((ObjectName)localObject5, localObjectName, localSubject);
      }
      catch (Throwable localThrowable)
      {
        localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
        if ((localObject5 instanceof InstanceNotFoundException))
          throw ((InstanceNotFoundException)localObject5);
        if ((localObject5 instanceof ListenerNotFoundException))
          throw ((ListenerNotFoundException)localObject5);
        if ((localObject5 instanceof IOException))
          throw ((IOException)localObject5);
        throw Util.wrapException((Throwable)localObject5);
      }
      finally
      {
        _servant_postinvoke(localServantObject);
      }
    }
  }

  public void removeNotificationListeners(ObjectName paramObjectName, Integer[] paramArrayOfInteger, Subject paramSubject)
    throws InstanceNotFoundException, ListenerNotFoundException, IOException
  {
    Object localObject5;
    if (!Util.isLocal(this))
    {
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("removeNotificationListeners", true);
          localOutputStream.write_value(paramObjectName, ObjectName.class);
          localOutputStream.write_value(cast_array(paramArrayOfInteger), new Integer[0].getClass());
          localOutputStream.write_value(paramSubject, Subject.class);
          _invoke(localOutputStream);
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject5).equals("IDL:javax/management/ListenerNotFoundEx:1.0"))
            throw ((ListenerNotFoundException)localInputStream.read_value(ListenerNotFoundException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          removeNotificationListeners(paramObjectName, paramArrayOfInteger, paramSubject);
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    }
    else
    {
      ServantObject localServantObject = _servant_preinvoke("removeNotificationListeners", RMIConnection.class);
      if (localServantObject == null)
      {
        removeNotificationListeners(paramObjectName, paramArrayOfInteger, paramSubject);
        return;
      }
      try
      {
        Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName, paramArrayOfInteger, paramSubject }, _orb());
        localObject5 = (ObjectName)arrayOfObject[0];
        Integer[] arrayOfInteger = (Integer[])arrayOfObject[1];
        Subject localSubject = (Subject)arrayOfObject[2];
        ((RMIConnection)localServantObject.servant).removeNotificationListeners((ObjectName)localObject5, arrayOfInteger, localSubject);
      }
      catch (Throwable localThrowable)
      {
        localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
        if ((localObject5 instanceof InstanceNotFoundException))
          throw ((InstanceNotFoundException)localObject5);
        if ((localObject5 instanceof ListenerNotFoundException))
          throw ((ListenerNotFoundException)localObject5);
        if ((localObject5 instanceof IOException))
          throw ((IOException)localObject5);
        throw Util.wrapException((Throwable)localObject5);
      }
      finally
      {
        _servant_postinvoke(localServantObject);
      }
    }
  }

  public void setAttribute(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
    throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException
  {
    Object localObject5;
    if (!Util.isLocal(this))
    {
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("setAttribute", true);
          localOutputStream.write_value(paramObjectName, ObjectName.class);
          localOutputStream.write_value(paramMarshalledObject, MarshalledObject.class);
          localOutputStream.write_value(paramSubject, Subject.class);
          _invoke(localOutputStream);
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject5).equals("IDL:javax/management/AttributeNotFoundEx:1.0"))
            throw ((AttributeNotFoundException)localInputStream.read_value(AttributeNotFoundException.class));
          if (((String)localObject5).equals("IDL:javax/management/InvalidAttributeValueEx:1.0"))
            throw ((InvalidAttributeValueException)localInputStream.read_value(InvalidAttributeValueException.class));
          if (((String)localObject5).equals("IDL:javax/management/MBeanEx:1.0"))
            throw ((MBeanException)localInputStream.read_value(MBeanException.class));
          if (((String)localObject5).equals("IDL:javax/management/ReflectionEx:1.0"))
            throw ((ReflectionException)localInputStream.read_value(ReflectionException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          setAttribute(paramObjectName, paramMarshalledObject, paramSubject);
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    }
    else
    {
      ServantObject localServantObject = _servant_preinvoke("setAttribute", RMIConnection.class);
      if (localServantObject == null)
      {
        setAttribute(paramObjectName, paramMarshalledObject, paramSubject);
        return;
      }
      try
      {
        Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName, paramMarshalledObject, paramSubject }, _orb());
        localObject5 = (ObjectName)arrayOfObject[0];
        MarshalledObject localMarshalledObject = (MarshalledObject)arrayOfObject[1];
        Subject localSubject = (Subject)arrayOfObject[2];
        ((RMIConnection)localServantObject.servant).setAttribute((ObjectName)localObject5, localMarshalledObject, localSubject);
      }
      catch (Throwable localThrowable)
      {
        localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
        if ((localObject5 instanceof InstanceNotFoundException))
          throw ((InstanceNotFoundException)localObject5);
        if ((localObject5 instanceof AttributeNotFoundException))
          throw ((AttributeNotFoundException)localObject5);
        if ((localObject5 instanceof InvalidAttributeValueException))
          throw ((InvalidAttributeValueException)localObject5);
        if ((localObject5 instanceof MBeanException))
          throw ((MBeanException)localObject5);
        if ((localObject5 instanceof ReflectionException))
          throw ((ReflectionException)localObject5);
        if ((localObject5 instanceof IOException))
          throw ((IOException)localObject5);
        throw Util.wrapException((Throwable)localObject5);
      }
      finally
      {
        _servant_postinvoke(localServantObject);
      }
    }
  }

  public AttributeList setAttributes(ObjectName paramObjectName, MarshalledObject paramMarshalledObject, Subject paramSubject)
    throws InstanceNotFoundException, ReflectionException, IOException
  {
    AttributeList localAttributeList1;
    Object localObject5;
    if (!Util.isLocal(this))
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("setAttributes", true);
          localOutputStream.write_value(paramObjectName, ObjectName.class);
          localOutputStream.write_value(paramMarshalledObject, MarshalledObject.class);
          localOutputStream.write_value(paramSubject, Subject.class);
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)_invoke(localOutputStream);
          localAttributeList1 = (AttributeList)localInputStream.read_value(AttributeList.class);
          return localAttributeList1;
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject5).equals("IDL:javax/management/ReflectionEx:1.0"))
            throw ((ReflectionException)localInputStream.read_value(ReflectionException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          localAttributeList1 = setAttributes(paramObjectName, paramMarshalledObject, paramSubject);
          return localAttributeList1;
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    ServantObject localServantObject = _servant_preinvoke("setAttributes", RMIConnection.class);
    if (localServantObject == null)
      return setAttributes(paramObjectName, paramMarshalledObject, paramSubject);
    try
    {
      Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName, paramMarshalledObject, paramSubject }, _orb());
      localObject5 = (ObjectName)arrayOfObject[0];
      MarshalledObject localMarshalledObject = (MarshalledObject)arrayOfObject[1];
      Subject localSubject = (Subject)arrayOfObject[2];
      AttributeList localAttributeList2 = ((RMIConnection)localServantObject.servant).setAttributes((ObjectName)localObject5, localMarshalledObject, localSubject);
      localAttributeList1 = (AttributeList)Util.copyObject(localAttributeList2, _orb());
      return localAttributeList1;
    }
    catch (Throwable localThrowable)
    {
      localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
      if ((localObject5 instanceof InstanceNotFoundException))
        throw ((InstanceNotFoundException)localObject5);
      if ((localObject5 instanceof ReflectionException))
        throw ((ReflectionException)localObject5);
      if ((localObject5 instanceof IOException))
        throw ((IOException)localObject5);
      throw Util.wrapException((Throwable)localObject5);
    }
    finally
    {
      _servant_postinvoke(localServantObject);
    }
  }

  public void unregisterMBean(ObjectName paramObjectName, Subject paramSubject)
    throws InstanceNotFoundException, MBeanRegistrationException, IOException
  {
    Object localObject5;
    if (!Util.isLocal(this))
    {
      try
      {
        org.omg.CORBA_2_3.portable.InputStream localInputStream = null;
        try
        {
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)_request("unregisterMBean", true);
          localOutputStream.write_value(paramObjectName, ObjectName.class);
          localOutputStream.write_value(paramSubject, Subject.class);
          _invoke(localOutputStream);
        }
        catch (ApplicationException localApplicationException)
        {
          localInputStream = (org.omg.CORBA_2_3.portable.InputStream)localApplicationException.getInputStream();
          localObject5 = localInputStream.read_string();
          if (((String)localObject5).equals("IDL:javax/management/InstanceNotFoundEx:1.0"))
            throw ((InstanceNotFoundException)localInputStream.read_value(InstanceNotFoundException.class));
          if (((String)localObject5).equals("IDL:javax/management/MBeanRegistrationEx:1.0"))
            throw ((MBeanRegistrationException)localInputStream.read_value(MBeanRegistrationException.class));
          if (((String)localObject5).equals("IDL:java/io/IOEx:1.0"))
            throw ((IOException)localInputStream.read_value(IOException.class));
          throw new UnexpectedException((String)localObject5);
        }
        catch (RemarshalException localRemarshalException)
        {
          unregisterMBean(paramObjectName, paramSubject);
        }
        finally
        {
          _releaseReply(localInputStream);
        }
      }
      catch (SystemException localSystemException)
      {
        throw Util.mapSystemException(localSystemException);
      }
    }
    else
    {
      ServantObject localServantObject = _servant_preinvoke("unregisterMBean", RMIConnection.class);
      if (localServantObject == null)
      {
        unregisterMBean(paramObjectName, paramSubject);
        return;
      }
      try
      {
        Object[] arrayOfObject = Util.copyObjects(new Object[] { paramObjectName, paramSubject }, _orb());
        localObject5 = (ObjectName)arrayOfObject[0];
        Subject localSubject = (Subject)arrayOfObject[1];
        ((RMIConnection)localServantObject.servant).unregisterMBean((ObjectName)localObject5, localSubject);
      }
      catch (Throwable localThrowable)
      {
        localObject5 = (Throwable)Util.copyObject(localThrowable, _orb());
        if ((localObject5 instanceof InstanceNotFoundException))
          throw ((InstanceNotFoundException)localObject5);
        if ((localObject5 instanceof MBeanRegistrationException))
          throw ((MBeanRegistrationException)localObject5);
        if ((localObject5 instanceof IOException))
          throw ((IOException)localObject5);
        throw Util.wrapException((Throwable)localObject5);
      }
      finally
      {
        _servant_postinvoke(localServantObject);
      }
    }
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.stub.javax.management.remote.rmi._RMIConnection_Stub
 * JD-Core Version:    0.6.2
 */