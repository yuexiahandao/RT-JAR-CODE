package javax.management.remote.rmi;

import java.io.IOException;
import java.io.Serializable;
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
import javax.rmi.CORBA.Tie;
import javax.rmi.CORBA.Util;
import javax.security.auth.Subject;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.CORBA.portable.UnknownException;

public class _RMIConnectionImpl_Tie extends org.omg.CORBA_2_3.portable.ObjectImpl
  implements Tie
{
  private volatile RMIConnectionImpl target = null;
  private static final String[] _type_ids = { "RMI:javax.management.remote.rmi.RMIConnection:0000000000000000" };

  public String[] _ids()
  {
    return (String[])_type_ids.clone();
  }

  public org.omg.CORBA.portable.OutputStream _invoke(String paramString, org.omg.CORBA.portable.InputStream paramInputStream, ResponseHandler paramResponseHandler)
    throws SystemException
  {
    try
    {
      RMIConnectionImpl localRMIConnectionImpl = this.target;
      if (localRMIConnectionImpl == null)
        throw new IOException();
      org.omg.CORBA_2_3.portable.InputStream localInputStream = (org.omg.CORBA_2_3.portable.InputStream)paramInputStream;
      java.lang.Object localObject1;
      java.lang.Object localObject4;
      java.lang.Object localObject18;
      java.lang.Object localObject22;
      java.lang.Object localObject8;
      java.lang.Object localObject14;
      java.lang.Object localObject3;
      java.lang.Object localObject5;
      java.lang.Object localObject9;
      java.lang.Object localObject25;
      java.lang.Object localObject19;
      java.lang.Object localObject15;
      java.lang.Object localObject10;
      java.lang.Object localObject16;
      java.lang.Object localObject20;
      java.lang.Object localObject27;
      java.lang.Object localObject26;
      org.omg.CORBA_2_3.portable.OutputStream localOutputStream9;
      ObjectName localObjectName;
      java.lang.Object localObject6;
      java.lang.Object localObject17;
      java.lang.Object localObject24;
      java.lang.Object localObject21;
      java.lang.Object localObject12;
      switch (paramString.charAt(3))
      {
      case 'A':
        java.lang.Object localObject2;
        java.lang.Object localObject7;
        if (paramString.equals("getAttribute"))
        {
          localObject1 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject2 = (String)localInputStream.read_value(String.class);
          localObject4 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localObject7 = localRMIConnectionImpl.getAttribute((ObjectName)localObject1, (String)localObject2, (Subject)localObject4);
          }
          catch (MBeanException localMBeanException2)
          {
            localObject18 = "IDL:javax/management/MBeanEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject18);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localMBeanException2, MBeanException.class);
            return localObject22;
          }
          catch (AttributeNotFoundException localAttributeNotFoundException2)
          {
            localObject18 = "IDL:javax/management/AttributeNotFoundEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject18);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localAttributeNotFoundException2, AttributeNotFoundException.class);
            return localObject22;
          }
          catch (InstanceNotFoundException localInstanceNotFoundException7)
          {
            localObject18 = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject18);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localInstanceNotFoundException7, InstanceNotFoundException.class);
            return localObject22;
          }
          catch (ReflectionException localReflectionException3)
          {
            localObject18 = "IDL:javax/management/ReflectionEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject18);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localReflectionException3, ReflectionException.class);
            return localObject22;
          }
          catch (IOException localIOException13)
          {
            localObject18 = "IDL:java/io/IOEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject18);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localIOException13, IOException.class);
            return localObject22;
          }
          org.omg.CORBA.portable.OutputStream localOutputStream6 = paramResponseHandler.createReply();
          Util.writeAny(localOutputStream6, localObject7);
          return localOutputStream6;
        }
        java.lang.Object localObject13;
        if (paramString.equals("getAttributes"))
        {
          localObject1 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject2 = (String[])localInputStream.read_value(new String[0].getClass());
          localObject4 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localObject7 = localRMIConnectionImpl.getAttributes((ObjectName)localObject1, (String[])localObject2, (Subject)localObject4);
          }
          catch (InstanceNotFoundException localInstanceNotFoundException8)
          {
            localObject18 = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject18);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localInstanceNotFoundException8, InstanceNotFoundException.class);
            return localObject22;
          }
          catch (ReflectionException localReflectionException4)
          {
            localObject18 = "IDL:javax/management/ReflectionEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject18);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localReflectionException4, ReflectionException.class);
            return localObject22;
          }
          catch (IOException localIOException14)
          {
            localObject18 = "IDL:java/io/IOEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject18);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localIOException14, IOException.class);
            return localObject22;
          }
          localObject13 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          ((org.omg.CORBA_2_3.portable.OutputStream)localObject13).write_value((Serializable)localObject7, AttributeList.class);
          return localObject13;
        }
        if (paramString.equals("setAttribute"))
        {
          localObject1 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject2 = (MarshalledObject)localInputStream.read_value(MarshalledObject.class);
          localObject4 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localRMIConnectionImpl.setAttribute((ObjectName)localObject1, (MarshalledObject)localObject2, (Subject)localObject4);
          }
          catch (InstanceNotFoundException localInstanceNotFoundException2)
          {
            localObject13 = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localObject18 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject18).write_string((String)localObject13);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject18).write_value(localInstanceNotFoundException2, InstanceNotFoundException.class);
            return localObject18;
          }
          catch (AttributeNotFoundException localAttributeNotFoundException1)
          {
            localObject13 = "IDL:javax/management/AttributeNotFoundEx:1.0";
            localObject18 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject18).write_string((String)localObject13);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject18).write_value(localAttributeNotFoundException1, AttributeNotFoundException.class);
            return localObject18;
          }
          catch (InvalidAttributeValueException localInvalidAttributeValueException)
          {
            localObject13 = "IDL:javax/management/InvalidAttributeValueEx:1.0";
            localObject18 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject18).write_string((String)localObject13);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject18).write_value(localInvalidAttributeValueException, InvalidAttributeValueException.class);
            return localObject18;
          }
          catch (MBeanException localMBeanException1)
          {
            localObject13 = "IDL:javax/management/MBeanEx:1.0";
            localObject18 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject18).write_string((String)localObject13);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject18).write_value(localMBeanException1, MBeanException.class);
            return localObject18;
          }
          catch (ReflectionException localReflectionException1)
          {
            localObject13 = "IDL:javax/management/ReflectionEx:1.0";
            localObject18 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject18).write_string((String)localObject13);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject18).write_value(localReflectionException1, ReflectionException.class);
            return localObject18;
          }
          catch (IOException localIOException7)
          {
            localObject13 = "IDL:java/io/IOEx:1.0";
            localObject18 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject18).write_string((String)localObject13);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject18).write_value(localIOException7, IOException.class);
            return localObject18;
          }
          localObject8 = paramResponseHandler.createReply();
          return localObject8;
        }
        if (paramString.equals("setAttributes"))
        {
          localObject1 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject2 = (MarshalledObject)localInputStream.read_value(MarshalledObject.class);
          localObject4 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localObject8 = localRMIConnectionImpl.setAttributes((ObjectName)localObject1, (MarshalledObject)localObject2, (Subject)localObject4);
          }
          catch (InstanceNotFoundException localInstanceNotFoundException9)
          {
            localObject18 = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject18);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localInstanceNotFoundException9, InstanceNotFoundException.class);
            return localObject22;
          }
          catch (ReflectionException localReflectionException5)
          {
            localObject18 = "IDL:javax/management/ReflectionEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject18);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localReflectionException5, ReflectionException.class);
            return localObject22;
          }
          catch (IOException localIOException15)
          {
            localObject18 = "IDL:java/io/IOEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject18);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localIOException15, IOException.class);
            return localObject22;
          }
          localObject14 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          ((org.omg.CORBA_2_3.portable.OutputStream)localObject14).write_value((Serializable)localObject8, AttributeList.class);
          return localObject14;
        }
      case 'C':
        if (paramString.equals("getConnectionId"))
        {
          try
          {
            localObject1 = localRMIConnectionImpl.getConnectionId();
          }
          catch (IOException localIOException2)
          {
            localObject4 = "IDL:java/io/IOEx:1.0";
            localObject8 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject8).write_string((String)localObject4);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject8).write_value(localIOException2, IOException.class);
            return localObject8;
          }
          localObject3 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          ((org.omg.CORBA_2_3.portable.OutputStream)localObject3).write_value((Serializable)localObject1, String.class);
          return localObject3;
        }
      case 'D':
        if (paramString.equals("getDefaultDomain"))
        {
          localObject1 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localObject3 = localRMIConnectionImpl.getDefaultDomain((Subject)localObject1);
          }
          catch (IOException localIOException3)
          {
            localObject8 = "IDL:java/io/IOEx:1.0";
            localObject14 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject14).write_string((String)localObject8);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject14).write_value(localIOException3, IOException.class);
            return localObject14;
          }
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream2 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          localOutputStream2.write_value((Serializable)localObject3, String.class);
          return localOutputStream2;
        }
        if (paramString.equals("getDomains"))
        {
          localObject1 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localObject3 = localRMIConnectionImpl.getDomains((Subject)localObject1);
          }
          catch (IOException localIOException4)
          {
            localObject8 = "IDL:java/io/IOEx:1.0";
            localObject14 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject14).write_string((String)localObject8);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject14).write_value(localIOException4, IOException.class);
            return localObject14;
          }
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream3 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          localOutputStream3.write_value(cast_array(localObject3), new String[0].getClass());
          return localOutputStream3;
        }
      case 'M':
        if (paramString.equals("getMBeanCount"))
        {
          localObject1 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localObject3 = localRMIConnectionImpl.getMBeanCount((Subject)localObject1);
          }
          catch (IOException localIOException5)
          {
            localObject8 = "IDL:java/io/IOEx:1.0";
            localObject14 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject14).write_string((String)localObject8);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject14).write_value(localIOException5, IOException.class);
            return localObject14;
          }
          localObject5 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          ((org.omg.CORBA_2_3.portable.OutputStream)localObject5).write_value((Serializable)localObject3, Integer.class);
          return localObject5;
        }
        if (paramString.equals("getMBeanInfo"))
        {
          localObject1 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject3 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localObject5 = localRMIConnectionImpl.getMBeanInfo((ObjectName)localObject1, (Subject)localObject3);
          }
          catch (InstanceNotFoundException localInstanceNotFoundException3)
          {
            localObject14 = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localObject18 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject18).write_string((String)localObject14);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject18).write_value(localInstanceNotFoundException3, InstanceNotFoundException.class);
            return localObject18;
          }
          catch (IntrospectionException localIntrospectionException)
          {
            localObject14 = "IDL:javax/management/IntrospectionEx:1.0";
            localObject18 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject18).write_string((String)localObject14);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject18).write_value(localIntrospectionException, IntrospectionException.class);
            return localObject18;
          }
          catch (ReflectionException localReflectionException2)
          {
            localObject14 = "IDL:javax/management/ReflectionEx:1.0";
            localObject18 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject18).write_string((String)localObject14);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject18).write_value(localReflectionException2, ReflectionException.class);
            return localObject18;
          }
          catch (IOException localIOException8)
          {
            localObject14 = "IDL:java/io/IOEx:1.0";
            localObject18 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject18).write_string((String)localObject14);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject18).write_value(localIOException8, IOException.class);
            return localObject18;
          }
          localObject9 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          ((org.omg.CORBA_2_3.portable.OutputStream)localObject9).write_value((Serializable)localObject5, MBeanInfo.class);
          return localObject9;
        }
      case 'N':
        if (paramString.equals("addNotificationListener"))
        {
          localObject1 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject3 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject5 = (MarshalledObject)localInputStream.read_value(MarshalledObject.class);
          localObject9 = (MarshalledObject)localInputStream.read_value(MarshalledObject.class);
          localObject14 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localRMIConnectionImpl.addNotificationListener((ObjectName)localObject1, (ObjectName)localObject3, (MarshalledObject)localObject5, (MarshalledObject)localObject9, (Subject)localObject14);
          }
          catch (InstanceNotFoundException localInstanceNotFoundException12)
          {
            localObject22 = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localObject25 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject25).write_string((String)localObject22);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject25).write_value(localInstanceNotFoundException12, InstanceNotFoundException.class);
            return localObject25;
          }
          catch (IOException localIOException21)
          {
            localObject22 = "IDL:java/io/IOEx:1.0";
            localObject25 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject25).write_string((String)localObject22);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject25).write_value(localIOException21, IOException.class);
            return localObject25;
          }
          localObject19 = paramResponseHandler.createReply();
          return localObject19;
        }
        if (paramString.equals("addNotificationListeners"))
        {
          localObject1 = (ObjectName[])localInputStream.read_value(new ObjectName[0].getClass());
          localObject3 = (MarshalledObject[])localInputStream.read_value(new MarshalledObject[0].getClass());
          localObject5 = (Subject[])localInputStream.read_value(new Subject[0].getClass());
          try
          {
            localObject9 = localRMIConnectionImpl.addNotificationListeners((ObjectName[])localObject1, (MarshalledObject[])localObject3, (Subject[])localObject5);
          }
          catch (InstanceNotFoundException localInstanceNotFoundException10)
          {
            localObject19 = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject19);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localInstanceNotFoundException10, InstanceNotFoundException.class);
            return localObject22;
          }
          catch (IOException localIOException16)
          {
            localObject19 = "IDL:java/io/IOEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject19);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localIOException16, IOException.class);
            return localObject22;
          }
          localObject15 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          ((org.omg.CORBA_2_3.portable.OutputStream)localObject15).write_value(cast_array(localObject9), new Integer[0].getClass());
          return localObject15;
        }
      case 'O':
        if (paramString.equals("getObjectInstance"))
        {
          localObject1 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject3 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localObject5 = localRMIConnectionImpl.getObjectInstance((ObjectName)localObject1, (Subject)localObject3);
          }
          catch (InstanceNotFoundException localInstanceNotFoundException4)
          {
            localObject15 = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localObject19 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject19).write_string((String)localObject15);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject19).write_value(localInstanceNotFoundException4, InstanceNotFoundException.class);
            return localObject19;
          }
          catch (IOException localIOException9)
          {
            localObject15 = "IDL:java/io/IOEx:1.0";
            localObject19 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject19).write_string((String)localObject15);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject19).write_value(localIOException9, IOException.class);
            return localObject19;
          }
          localObject10 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          ((org.omg.CORBA_2_3.portable.OutputStream)localObject10).write_value((Serializable)localObject5, ObjectInstance.class);
          return localObject10;
        }
      case 'a':
        if (paramString.equals("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_security_auth_Subject"))
        {
          localObject1 = (String)localInputStream.read_value(String.class);
          localObject3 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject5 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localObject10 = localRMIConnectionImpl.createMBean((String)localObject1, (ObjectName)localObject3, (Subject)localObject5);
          }
          catch (ReflectionException localReflectionException6)
          {
            localObject19 = "IDL:javax/management/ReflectionEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject19);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localReflectionException6, ReflectionException.class);
            return localObject22;
          }
          catch (InstanceAlreadyExistsException localInstanceAlreadyExistsException1)
          {
            localObject19 = "IDL:javax/management/InstanceAlreadyExistsEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject19);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localInstanceAlreadyExistsException1, InstanceAlreadyExistsException.class);
            return localObject22;
          }
          catch (MBeanException localMBeanException3)
          {
            localObject19 = "IDL:javax/management/MBeanEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject19);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localMBeanException3, MBeanException.class);
            return localObject22;
          }
          catch (NotCompliantMBeanException localNotCompliantMBeanException1)
          {
            localObject19 = "IDL:javax/management/NotCompliantMBeanEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject19);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localNotCompliantMBeanException1, NotCompliantMBeanException.class);
            return localObject22;
          }
          catch (IOException localIOException17)
          {
            localObject19 = "IDL:java/io/IOEx:1.0";
            localObject22 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject22).write_string((String)localObject19);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject22).write_value(localIOException17, IOException.class);
            return localObject22;
          }
          localObject16 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          ((org.omg.CORBA_2_3.portable.OutputStream)localObject16).write_value((Serializable)localObject10, ObjectInstance.class);
          return localObject16;
        }
        if (paramString.equals("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_management_ObjectName__javax_security_auth_Subject"))
        {
          localObject1 = (String)localInputStream.read_value(String.class);
          localObject3 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject5 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject10 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localObject16 = localRMIConnectionImpl.createMBean((String)localObject1, (ObjectName)localObject3, (ObjectName)localObject5, (Subject)localObject10);
          }
          catch (ReflectionException localReflectionException7)
          {
            localObject22 = "IDL:javax/management/ReflectionEx:1.0";
            localObject25 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject25).write_string((String)localObject22);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject25).write_value(localReflectionException7, ReflectionException.class);
            return localObject25;
          }
          catch (InstanceAlreadyExistsException localInstanceAlreadyExistsException2)
          {
            localObject22 = "IDL:javax/management/InstanceAlreadyExistsEx:1.0";
            localObject25 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject25).write_string((String)localObject22);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject25).write_value(localInstanceAlreadyExistsException2, InstanceAlreadyExistsException.class);
            return localObject25;
          }
          catch (MBeanException localMBeanException4)
          {
            localObject22 = "IDL:javax/management/MBeanEx:1.0";
            localObject25 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject25).write_string((String)localObject22);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject25).write_value(localMBeanException4, MBeanException.class);
            return localObject25;
          }
          catch (NotCompliantMBeanException localNotCompliantMBeanException2)
          {
            localObject22 = "IDL:javax/management/NotCompliantMBeanEx:1.0";
            localObject25 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject25).write_string((String)localObject22);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject25).write_value(localNotCompliantMBeanException2, NotCompliantMBeanException.class);
            return localObject25;
          }
          catch (InstanceNotFoundException localInstanceNotFoundException13)
          {
            localObject22 = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localObject25 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject25).write_string((String)localObject22);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject25).write_value(localInstanceNotFoundException13, InstanceNotFoundException.class);
            return localObject25;
          }
          catch (IOException localIOException22)
          {
            localObject22 = "IDL:java/io/IOEx:1.0";
            localObject25 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject25).write_string((String)localObject22);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject25).write_value(localIOException22, IOException.class);
            return localObject25;
          }
          localObject20 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          ((org.omg.CORBA_2_3.portable.OutputStream)localObject20).write_value((Serializable)localObject16, ObjectInstance.class);
          return localObject20;
        }
        java.lang.Object localObject23;
        if (paramString.equals("createMBean__CORBA_WStringValue__javax_management_ObjectName__java_rmi_MarshalledObject__org_omg_boxedRMI_CORBA_seq1_WStringValue__javax_security_auth_Subject"))
        {
          localObject1 = (String)localInputStream.read_value(String.class);
          localObject3 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject5 = (MarshalledObject)localInputStream.read_value(MarshalledObject.class);
          localObject10 = (String[])localInputStream.read_value(new String[0].getClass());
          localObject16 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localObject20 = localRMIConnectionImpl.createMBean((String)localObject1, (ObjectName)localObject3, (MarshalledObject)localObject5, (String[])localObject10, (Subject)localObject16);
          }
          catch (ReflectionException localReflectionException8)
          {
            localObject25 = "IDL:javax/management/ReflectionEx:1.0";
            localObject27 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject27).write_string((String)localObject25);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject27).write_value(localReflectionException8, ReflectionException.class);
            return localObject27;
          }
          catch (InstanceAlreadyExistsException localInstanceAlreadyExistsException3)
          {
            localObject25 = "IDL:javax/management/InstanceAlreadyExistsEx:1.0";
            localObject27 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject27).write_string((String)localObject25);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject27).write_value(localInstanceAlreadyExistsException3, InstanceAlreadyExistsException.class);
            return localObject27;
          }
          catch (MBeanException localMBeanException5)
          {
            localObject25 = "IDL:javax/management/MBeanEx:1.0";
            localObject27 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject27).write_string((String)localObject25);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject27).write_value(localMBeanException5, MBeanException.class);
            return localObject27;
          }
          catch (NotCompliantMBeanException localNotCompliantMBeanException3)
          {
            localObject25 = "IDL:javax/management/NotCompliantMBeanEx:1.0";
            localObject27 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject27).write_string((String)localObject25);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject27).write_value(localNotCompliantMBeanException3, NotCompliantMBeanException.class);
            return localObject27;
          }
          catch (IOException localIOException24)
          {
            localObject25 = "IDL:java/io/IOEx:1.0";
            localObject27 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject27).write_string((String)localObject25);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject27).write_value(localIOException24, IOException.class);
            return localObject27;
          }
          localObject23 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          ((org.omg.CORBA_2_3.portable.OutputStream)localObject23).write_value((Serializable)localObject20, ObjectInstance.class);
          return localObject23;
        }
        if (paramString.equals("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_management_ObjectName__java_rmi_MarshalledObject__org_omg_boxedRMI_CORBA_seq1_WStringValue__javax_security_auth_Subject"))
        {
          localObject1 = (String)localInputStream.read_value(String.class);
          localObject3 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject5 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject10 = (MarshalledObject)localInputStream.read_value(MarshalledObject.class);
          localObject16 = (String[])localInputStream.read_value(new String[0].getClass());
          localObject20 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localObject23 = localRMIConnectionImpl.createMBean((String)localObject1, (ObjectName)localObject3, (ObjectName)localObject5, (MarshalledObject)localObject10, (String[])localObject16, (Subject)localObject20);
          }
          catch (ReflectionException localReflectionException10)
          {
            localObject27 = "IDL:javax/management/ReflectionEx:1.0";
            localOutputStream10 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            localOutputStream10.write_string((String)localObject27);
            localOutputStream10.write_value(localReflectionException10, ReflectionException.class);
            return localOutputStream10;
          }
          catch (InstanceAlreadyExistsException localInstanceAlreadyExistsException4)
          {
            localObject27 = "IDL:javax/management/InstanceAlreadyExistsEx:1.0";
            localOutputStream10 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            localOutputStream10.write_string((String)localObject27);
            localOutputStream10.write_value(localInstanceAlreadyExistsException4, InstanceAlreadyExistsException.class);
            return localOutputStream10;
          }
          catch (MBeanException localMBeanException7)
          {
            localObject27 = "IDL:javax/management/MBeanEx:1.0";
            localOutputStream10 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            localOutputStream10.write_string((String)localObject27);
            localOutputStream10.write_value(localMBeanException7, MBeanException.class);
            return localOutputStream10;
          }
          catch (NotCompliantMBeanException localNotCompliantMBeanException4)
          {
            localObject27 = "IDL:javax/management/NotCompliantMBeanEx:1.0";
            localOutputStream10 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            localOutputStream10.write_string((String)localObject27);
            localOutputStream10.write_value(localNotCompliantMBeanException4, NotCompliantMBeanException.class);
            return localOutputStream10;
          }
          catch (InstanceNotFoundException localInstanceNotFoundException16)
          {
            localObject27 = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localOutputStream10 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            localOutputStream10.write_string((String)localObject27);
            localOutputStream10.write_value(localInstanceNotFoundException16, InstanceNotFoundException.class);
            return localOutputStream10;
          }
          catch (IOException localIOException27)
          {
            localObject27 = "IDL:java/io/IOEx:1.0";
            org.omg.CORBA_2_3.portable.OutputStream localOutputStream10 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            localOutputStream10.write_string((String)localObject27);
            localOutputStream10.write_value(localIOException27, IOException.class);
            return localOutputStream10;
          }
          localObject26 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          ((org.omg.CORBA_2_3.portable.OutputStream)localObject26).write_value((Serializable)localObject23, ObjectInstance.class);
          return localObject26;
        }
      case 'c':
        if (paramString.equals("fetchNotifications"))
        {
          long l1 = localInputStream.read_longlong();
          int i = localInputStream.read_long();
          long l2 = localInputStream.read_longlong();
          try
          {
            localObject20 = localRMIConnectionImpl.fetchNotifications(l1, i, l2);
          }
          catch (IOException localIOException25)
          {
            localObject26 = "IDL:java/io/IOEx:1.0";
            localObject27 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject27).write_string((String)localObject26);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject27).write_value(localIOException25, IOException.class);
            return localObject27;
          }
          localOutputStream9 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          localOutputStream9.write_value((Serializable)localObject20, NotificationResult.class);
          return localOutputStream9;
        }
      case 'e':
        if (paramString.equals("unregisterMBean"))
        {
          localObjectName = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject3 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localRMIConnectionImpl.unregisterMBean(localObjectName, (Subject)localObject3);
          }
          catch (InstanceNotFoundException localInstanceNotFoundException1)
          {
            str = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localObject16 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject16).write_string(str);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject16).write_value(localInstanceNotFoundException1, InstanceNotFoundException.class);
            return localObject16;
          }
          catch (MBeanRegistrationException localMBeanRegistrationException)
          {
            str = "IDL:javax/management/MBeanRegistrationEx:1.0";
            localObject16 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject16).write_string(str);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject16).write_value(localMBeanRegistrationException, MBeanRegistrationException.class);
            return localObject16;
          }
          catch (IOException localIOException6)
          {
            String str = "IDL:java/io/IOEx:1.0";
            localObject16 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject16).write_string(str);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject16).write_value(localIOException6, IOException.class);
            return localObject16;
          }
          org.omg.CORBA.portable.OutputStream localOutputStream4 = paramResponseHandler.createReply();
          return localOutputStream4;
        }
        if (paramString.equals("isRegistered"))
        {
          localObjectName = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject3 = (Subject)localInputStream.read_value(Subject.class);
          boolean bool1;
          try
          {
            bool1 = localRMIConnectionImpl.isRegistered(localObjectName, (Subject)localObject3);
          }
          catch (IOException localIOException10)
          {
            localObject16 = "IDL:java/io/IOEx:1.0";
            localObject20 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject20).write_string((String)localObject16);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject20).write_value(localIOException10, IOException.class);
            return localObject20;
          }
          org.omg.CORBA.portable.OutputStream localOutputStream5 = paramResponseHandler.createReply();
          localOutputStream5.write_boolean(bool1);
          return localOutputStream5;
        }
      case 'n':
        if (paramString.equals("isInstanceOf"))
        {
          localObjectName = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject3 = (String)localInputStream.read_value(String.class);
          localObject6 = (Subject)localInputStream.read_value(Subject.class);
          boolean bool2;
          try
          {
            bool2 = localRMIConnectionImpl.isInstanceOf(localObjectName, (String)localObject3, (Subject)localObject6);
          }
          catch (InstanceNotFoundException localInstanceNotFoundException11)
          {
            localObject20 = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localOutputStream9 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            localOutputStream9.write_string((String)localObject20);
            localOutputStream9.write_value(localInstanceNotFoundException11, InstanceNotFoundException.class);
            return localOutputStream9;
          }
          catch (IOException localIOException18)
          {
            localObject20 = "IDL:java/io/IOEx:1.0";
            localOutputStream9 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            localOutputStream9.write_string((String)localObject20);
            localOutputStream9.write_value(localIOException18, IOException.class);
            return localOutputStream9;
          }
          localObject17 = paramResponseHandler.createReply();
          ((org.omg.CORBA.portable.OutputStream)localObject17).write_boolean(bool2);
          return localObject17;
        }
      case 'o':
        if (paramString.equals("invoke"))
        {
          localObjectName = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject3 = (String)localInputStream.read_value(String.class);
          localObject6 = (MarshalledObject)localInputStream.read_value(MarshalledObject.class);
          String[] arrayOfString = (String[])localInputStream.read_value(new String[0].getClass());
          localObject17 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localObject20 = localRMIConnectionImpl.invoke(localObjectName, (String)localObject3, (MarshalledObject)localObject6, arrayOfString, (Subject)localObject17);
          }
          catch (InstanceNotFoundException localInstanceNotFoundException15)
          {
            localObject26 = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localObject27 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject27).write_string((String)localObject26);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject27).write_value(localInstanceNotFoundException15, InstanceNotFoundException.class);
            return localObject27;
          }
          catch (MBeanException localMBeanException6)
          {
            localObject26 = "IDL:javax/management/MBeanEx:1.0";
            localObject27 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject27).write_string((String)localObject26);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject27).write_value(localMBeanException6, MBeanException.class);
            return localObject27;
          }
          catch (ReflectionException localReflectionException9)
          {
            localObject26 = "IDL:javax/management/ReflectionEx:1.0";
            localObject27 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject27).write_string((String)localObject26);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject27).write_value(localReflectionException9, ReflectionException.class);
            return localObject27;
          }
          catch (IOException localIOException26)
          {
            localObject26 = "IDL:java/io/IOEx:1.0";
            localObject27 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject27).write_string((String)localObject26);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject27).write_value(localIOException26, IOException.class);
            return localObject27;
          }
          localObject24 = paramResponseHandler.createReply();
          Util.writeAny((org.omg.CORBA.portable.OutputStream)localObject24, localObject20);
          return localObject24;
        }
        java.lang.Object localObject11;
        if (paramString.equals("removeNotificationListener__javax_management_ObjectName__javax_management_ObjectName__javax_security_auth_Subject"))
        {
          localObjectName = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject3 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject6 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localRMIConnectionImpl.removeNotificationListener(localObjectName, (ObjectName)localObject3, (Subject)localObject6);
          }
          catch (InstanceNotFoundException localInstanceNotFoundException5)
          {
            localObject17 = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localObject20 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject20).write_string((String)localObject17);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject20).write_value(localInstanceNotFoundException5, InstanceNotFoundException.class);
            return localObject20;
          }
          catch (ListenerNotFoundException localListenerNotFoundException1)
          {
            localObject17 = "IDL:javax/management/ListenerNotFoundEx:1.0";
            localObject20 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject20).write_string((String)localObject17);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject20).write_value(localListenerNotFoundException1, ListenerNotFoundException.class);
            return localObject20;
          }
          catch (IOException localIOException11)
          {
            localObject17 = "IDL:java/io/IOEx:1.0";
            localObject20 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject20).write_string((String)localObject17);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject20).write_value(localIOException11, IOException.class);
            return localObject20;
          }
          localObject11 = paramResponseHandler.createReply();
          return localObject11;
        }
        if (paramString.equals("removeNotificationListener__javax_management_ObjectName__javax_management_ObjectName__java_rmi_MarshalledObject__java_rmi_MarshalledObject__javax_security_auth_Subject"))
        {
          localObjectName = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject3 = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject6 = (MarshalledObject)localInputStream.read_value(MarshalledObject.class);
          localObject11 = (MarshalledObject)localInputStream.read_value(MarshalledObject.class);
          localObject17 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localRMIConnectionImpl.removeNotificationListener(localObjectName, (ObjectName)localObject3, (MarshalledObject)localObject6, (MarshalledObject)localObject11, (Subject)localObject17);
          }
          catch (InstanceNotFoundException localInstanceNotFoundException14)
          {
            localObject24 = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localObject26 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject26).write_string((String)localObject24);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject26).write_value(localInstanceNotFoundException14, InstanceNotFoundException.class);
            return localObject26;
          }
          catch (ListenerNotFoundException localListenerNotFoundException3)
          {
            localObject24 = "IDL:javax/management/ListenerNotFoundEx:1.0";
            localObject26 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject26).write_string((String)localObject24);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject26).write_value(localListenerNotFoundException3, ListenerNotFoundException.class);
            return localObject26;
          }
          catch (IOException localIOException23)
          {
            localObject24 = "IDL:java/io/IOEx:1.0";
            localObject26 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject26).write_string((String)localObject24);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject26).write_value(localIOException23, IOException.class);
            return localObject26;
          }
          localObject21 = paramResponseHandler.createReply();
          return localObject21;
        }
        if (paramString.equals("removeNotificationListeners"))
        {
          localObjectName = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject3 = (Integer[])localInputStream.read_value(new Integer[0].getClass());
          localObject6 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localRMIConnectionImpl.removeNotificationListeners(localObjectName, (Integer[])localObject3, (Subject)localObject6);
          }
          catch (InstanceNotFoundException localInstanceNotFoundException6)
          {
            localObject17 = "IDL:javax/management/InstanceNotFoundEx:1.0";
            localObject21 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject21).write_string((String)localObject17);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject21).write_value(localInstanceNotFoundException6, InstanceNotFoundException.class);
            return localObject21;
          }
          catch (ListenerNotFoundException localListenerNotFoundException2)
          {
            localObject17 = "IDL:javax/management/ListenerNotFoundEx:1.0";
            localObject21 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject21).write_string((String)localObject17);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject21).write_value(localListenerNotFoundException2, ListenerNotFoundException.class);
            return localObject21;
          }
          catch (IOException localIOException12)
          {
            localObject17 = "IDL:java/io/IOEx:1.0";
            localObject21 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject21).write_string((String)localObject17);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject21).write_value(localIOException12, IOException.class);
            return localObject21;
          }
          localObject12 = paramResponseHandler.createReply();
          return localObject12;
        }
      case 'r':
        if (paramString.equals("queryMBeans"))
        {
          localObjectName = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject3 = (MarshalledObject)localInputStream.read_value(MarshalledObject.class);
          localObject6 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localObject12 = localRMIConnectionImpl.queryMBeans(localObjectName, (MarshalledObject)localObject3, (Subject)localObject6);
          }
          catch (IOException localIOException19)
          {
            localObject21 = "IDL:java/io/IOEx:1.0";
            localObject24 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject24).write_string((String)localObject21);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject24).write_value(localIOException19, IOException.class);
            return localObject24;
          }
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream7 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          localOutputStream7.write_value((Serializable)localObject12, Set.class);
          return localOutputStream7;
        }
        if (paramString.equals("queryNames"))
        {
          localObjectName = (ObjectName)localInputStream.read_value(ObjectName.class);
          localObject3 = (MarshalledObject)localInputStream.read_value(MarshalledObject.class);
          localObject6 = (Subject)localInputStream.read_value(Subject.class);
          try
          {
            localObject12 = localRMIConnectionImpl.queryNames(localObjectName, (MarshalledObject)localObject3, (Subject)localObject6);
          }
          catch (IOException localIOException20)
          {
            localObject21 = "IDL:java/io/IOEx:1.0";
            localObject24 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject24).write_string((String)localObject21);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject24).write_value(localIOException20, IOException.class);
            return localObject24;
          }
          org.omg.CORBA_2_3.portable.OutputStream localOutputStream8 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          localOutputStream8.write_value((Serializable)localObject12, Set.class);
          return localOutputStream8;
        }
      case 's':
        if (paramString.equals("close"))
        {
          try
          {
            localRMIConnectionImpl.close();
          }
          catch (IOException localIOException1)
          {
            localObject3 = "IDL:java/io/IOEx:1.0";
            localObject6 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            ((org.omg.CORBA.portable.OutputStream)localObject6).write_string((String)localObject3);
            ((org.omg.CORBA_2_3.portable.OutputStream)localObject6).write_value(localIOException1, IOException.class);
            return localObject6;
          }
          org.omg.CORBA.portable.OutputStream localOutputStream1 = paramResponseHandler.createReply();
          return localOutputStream1;
        }
        break;
      }
      throw new BAD_OPERATION();
    }
    catch (SystemException localSystemException)
    {
      throw localSystemException;
    }
    catch (Throwable localThrowable)
    {
      throw new UnknownException(localThrowable);
    }
  }

  private Serializable cast_array(java.lang.Object paramObject)
  {
    return (Serializable)paramObject;
  }

  public void deactivate()
  {
    _orb().disconnect(this);
    _set_delegate(null);
    this.target = null;
  }

  public Remote getTarget()
  {
    return this.target;
  }

  public ORB orb()
  {
    return _orb();
  }

  public void orb(ORB paramORB)
  {
    paramORB.connect(this);
  }

  public void setTarget(Remote paramRemote)
  {
    this.target = ((RMIConnectionImpl)paramRemote);
  }

  public org.omg.CORBA.Object thisObject()
  {
    return this;
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.rmi._RMIConnectionImpl_Tie
 * JD-Core Version:    0.6.2
 */