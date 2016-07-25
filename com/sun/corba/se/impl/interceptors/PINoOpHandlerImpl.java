/*     */ package com.sun.corba.se.impl.interceptors;
/*     */ 
/*     */ import com.sun.corba.se.impl.corba.RequestImpl;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import com.sun.corba.se.spi.protocol.PIHandler;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.NVList;
/*     */ import org.omg.CORBA.Policy;
/*     */ import org.omg.CORBA.PolicyError;
/*     */ import org.omg.CORBA.portable.RemarshalException;
/*     */ import org.omg.PortableInterceptor.Current;
/*     */ import org.omg.PortableInterceptor.Interceptor;
/*     */ import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
/*     */ import org.omg.PortableInterceptor.ObjectReferenceTemplate;
/*     */ import org.omg.PortableInterceptor.PolicyFactory;
/*     */ 
/*     */ public class PINoOpHandlerImpl
/*     */   implements PIHandler
/*     */ {
/*     */   public void close()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void initialize()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void destroyInterceptors()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void objectAdapterCreated(ObjectAdapter paramObjectAdapter)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void adapterManagerStateChanged(int paramInt, short paramShort)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void adapterStateChanged(ObjectReferenceTemplate[] paramArrayOfObjectReferenceTemplate, short paramShort)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void disableInterceptorsThisThread()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void enableInterceptorsThisThread()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void invokeClientPIStartingPoint()
/*     */     throws RemarshalException
/*     */   {
/*     */   }
/*     */ 
/*     */   public Exception invokeClientPIEndingPoint(int paramInt, Exception paramException)
/*     */   {
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */   public Exception makeCompletedClientRequest(int paramInt, Exception paramException)
/*     */   {
/* 115 */     return null;
/*     */   }
/*     */ 
/*     */   public void initiateClientPIRequest(boolean paramBoolean)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void cleanupClientPIRequest()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setClientPIInfo(CorbaMessageMediator paramCorbaMessageMediator)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setClientPIInfo(RequestImpl paramRequestImpl)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final void sendCancelRequestIfFinalFragmentNotSent()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void invokeServerPIStartingPoint()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void invokeServerPIIntermediatePoint()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void invokeServerPIEndingPoint(ReplyMessage paramReplyMessage)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setServerPIInfo(Exception paramException)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setServerPIInfo(NVList paramNVList)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setServerPIExceptionInfo(Any paramAny)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setServerPIInfo(Any paramAny)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void initializeServerPIInfo(CorbaMessageMediator paramCorbaMessageMediator, ObjectAdapter paramObjectAdapter, byte[] paramArrayOfByte, ObjectKeyTemplate paramObjectKeyTemplate)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setServerPIInfo(Object paramObject, String paramString)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void cleanupServerPIRequest()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void register_interceptor(Interceptor paramInterceptor, int paramInt) throws DuplicateName
/*     */   {
/*     */   }
/*     */ 
/*     */   public Current getPICurrent() {
/* 183 */     return null;
/*     */   }
/*     */ 
/*     */   public Policy create_policy(int paramInt, Any paramAny)
/*     */     throws PolicyError
/*     */   {
/* 189 */     return null;
/*     */   }
/*     */ 
/*     */   public void registerPolicyFactory(int paramInt, PolicyFactory paramPolicyFactory)
/*     */   {
/*     */   }
/*     */ 
/*     */   public int allocateServerRequestId() {
/* 197 */     return 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.interceptors.PINoOpHandlerImpl
 * JD-Core Version:    0.6.2
 */