package com.sun.corba.se.spi.protocol;

import com.sun.corba.se.impl.corba.RequestImpl;
import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import java.io.Closeable;
import org.omg.CORBA.Any;
import org.omg.CORBA.NVList;
import org.omg.CORBA.Policy;
import org.omg.CORBA.PolicyError;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.PortableInterceptor.Current;
import org.omg.PortableInterceptor.Interceptor;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.omg.PortableInterceptor.ObjectReferenceTemplate;
import org.omg.PortableInterceptor.PolicyFactory;

public abstract interface PIHandler extends Closeable
{
  public abstract void initialize();

  public abstract void destroyInterceptors();

  public abstract void objectAdapterCreated(ObjectAdapter paramObjectAdapter);

  public abstract void adapterManagerStateChanged(int paramInt, short paramShort);

  public abstract void adapterStateChanged(ObjectReferenceTemplate[] paramArrayOfObjectReferenceTemplate, short paramShort);

  public abstract void disableInterceptorsThisThread();

  public abstract void enableInterceptorsThisThread();

  public abstract void invokeClientPIStartingPoint()
    throws RemarshalException;

  public abstract Exception invokeClientPIEndingPoint(int paramInt, Exception paramException);

  public abstract Exception makeCompletedClientRequest(int paramInt, Exception paramException);

  public abstract void initiateClientPIRequest(boolean paramBoolean);

  public abstract void cleanupClientPIRequest();

  public abstract void setClientPIInfo(RequestImpl paramRequestImpl);

  public abstract void setClientPIInfo(CorbaMessageMediator paramCorbaMessageMediator);

  public abstract void invokeServerPIStartingPoint();

  public abstract void invokeServerPIIntermediatePoint();

  public abstract void invokeServerPIEndingPoint(ReplyMessage paramReplyMessage);

  public abstract void initializeServerPIInfo(CorbaMessageMediator paramCorbaMessageMediator, ObjectAdapter paramObjectAdapter, byte[] paramArrayOfByte, ObjectKeyTemplate paramObjectKeyTemplate);

  public abstract void setServerPIInfo(Object paramObject, String paramString);

  public abstract void setServerPIInfo(Exception paramException);

  public abstract void setServerPIInfo(NVList paramNVList);

  public abstract void setServerPIExceptionInfo(Any paramAny);

  public abstract void setServerPIInfo(Any paramAny);

  public abstract void cleanupServerPIRequest();

  public abstract Policy create_policy(int paramInt, Any paramAny)
    throws PolicyError;

  public abstract void register_interceptor(Interceptor paramInterceptor, int paramInt)
    throws DuplicateName;

  public abstract Current getPICurrent();

  public abstract void registerPolicyFactory(int paramInt, PolicyFactory paramPolicyFactory);

  public abstract int allocateServerRequestId();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.protocol.PIHandler
 * JD-Core Version:    0.6.2
 */