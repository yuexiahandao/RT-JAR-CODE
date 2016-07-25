package org.omg.stub.javax.management.remote.rmi;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.Remote;
import javax.management.remote.rmi.RMIServerImpl;
import javax.rmi.CORBA.Tie;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.CORBA.portable.UnknownException;

public class _RMIServerImpl_Tie extends org.omg.CORBA_2_3.portable.ObjectImpl
  implements Tie
{
  private volatile RMIServerImpl target = null;
  private static final String[] _type_ids = { "RMI:javax.management.remote.rmi.RMIServer:0000000000000000" };

  public String[] _ids()
  {
    return (String[])_type_ids.clone();
  }

  public org.omg.CORBA.portable.OutputStream _invoke(String paramString, org.omg.CORBA.portable.InputStream paramInputStream, ResponseHandler paramResponseHandler)
    throws SystemException
  {
    try
    {
      RMIServerImpl localRMIServerImpl = this.target;
      if (localRMIServerImpl == null)
        throw new IOException();
      org.omg.CORBA_2_3.portable.InputStream localInputStream = (org.omg.CORBA_2_3.portable.InputStream)paramInputStream;
      java.lang.Object localObject1;
      java.lang.Object localObject2;
      switch (paramString.length())
      {
      case 9:
        if (paramString.equals("newClient"))
        {
          localObject1 = Util.readAny(localInputStream);
          try
          {
            localObject2 = localRMIServerImpl.newClient(localObject1);
          }
          catch (IOException localIOException)
          {
            String str = "IDL:java/io/IOEx:1.0";
            org.omg.CORBA_2_3.portable.OutputStream localOutputStream1 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
            localOutputStream1.write_string(str);
            localOutputStream1.write_value(localIOException, IOException.class);
            return localOutputStream1;
          }
          org.omg.CORBA.portable.OutputStream localOutputStream = paramResponseHandler.createReply();
          Util.writeRemoteObject(localOutputStream, localObject2);
          return localOutputStream;
        }
      case 12:
        if (paramString.equals("_get_version"))
        {
          localObject1 = localRMIServerImpl.getVersion();
          localObject2 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
          ((org.omg.CORBA_2_3.portable.OutputStream)localObject2).write_value((Serializable)localObject1, String.class);
          return localObject2;
        }
        break;
      case 10:
      case 11:
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
    this.target = ((RMIServerImpl)paramRemote);
  }

  public org.omg.CORBA.Object thisObject()
  {
    return this;
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.stub.javax.management.remote.rmi._RMIServerImpl_Tie
 * JD-Core Version:    0.6.2
 */