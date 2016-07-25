/*     */ package com.sun.corba.se.impl.protocol;
/*     */ 
/*     */ import com.sun.corba.se.impl.corba.RequestImpl;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.util.JDKBridge;
/*     */ import com.sun.corba.se.pept.broker.Broker;
/*     */ import com.sun.corba.se.pept.encoding.InputObject;
/*     */ import com.sun.corba.se.pept.encoding.OutputObject;
/*     */ import com.sun.corba.se.pept.protocol.ClientInvocationInfo;
/*     */ import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
/*     */ import com.sun.corba.se.pept.transport.ContactInfoList;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
/*     */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*     */ import com.sun.corba.se.spi.protocol.CorbaClientDelegate;
/*     */ import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfo;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoList;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoListIterator;
/*     */ import java.util.Iterator;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.Context;
/*     */ import org.omg.CORBA.ContextList;
/*     */ import org.omg.CORBA.ExceptionList;
/*     */ import org.omg.CORBA.NVList;
/*     */ import org.omg.CORBA.NamedValue;
/*     */ import org.omg.CORBA.Request;
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.RemarshalException;
/*     */ import org.omg.CORBA.portable.ServantObject;
/*     */ 
/*     */ public class CorbaClientDelegateImpl extends CorbaClientDelegate
/*     */ {
/*     */   private com.sun.corba.se.spi.orb.ORB orb;
/*     */   private ORBUtilSystemException wrapper;
/*     */   private CorbaContactInfoList contactInfoList;
/*     */ 
/*     */   public CorbaClientDelegateImpl(com.sun.corba.se.spi.orb.ORB paramORB, CorbaContactInfoList paramCorbaContactInfoList)
/*     */   {
/*  89 */     this.orb = paramORB;
/*  90 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.protocol");
/*     */ 
/*  92 */     this.contactInfoList = paramCorbaContactInfoList;
/*     */   }
/*     */ 
/*     */   public Broker getBroker()
/*     */   {
/* 101 */     return this.orb;
/*     */   }
/*     */ 
/*     */   public ContactInfoList getContactInfoList()
/*     */   {
/* 106 */     return this.contactInfoList;
/*     */   }
/*     */ 
/*     */   public OutputStream request(org.omg.CORBA.Object paramObject, String paramString, boolean paramBoolean)
/*     */   {
/* 117 */     ClientInvocationInfo localClientInvocationInfo = this.orb.createOrIncrementInvocationInfo();
/*     */ 
/* 119 */     Iterator localIterator = localClientInvocationInfo.getContactInfoListIterator();
/*     */ 
/* 121 */     if (localIterator == null) {
/* 122 */       localIterator = this.contactInfoList.iterator();
/* 123 */       localClientInvocationInfo.setContactInfoListIterator(localIterator);
/*     */     }
/* 125 */     if (!localIterator.hasNext()) {
/* 126 */       throw ((CorbaContactInfoListIterator)localIterator).getFailureException();
/*     */     }
/*     */ 
/* 129 */     CorbaContactInfo localCorbaContactInfo = (CorbaContactInfo)localIterator.next();
/* 130 */     ClientRequestDispatcher localClientRequestDispatcher = localCorbaContactInfo.getClientRequestDispatcher();
/*     */ 
/* 135 */     localClientInvocationInfo.setClientRequestDispatcher(localClientRequestDispatcher);
/* 136 */     return (OutputStream)localClientRequestDispatcher.beginRequest(paramObject, paramString, !paramBoolean, localCorbaContactInfo);
/*     */   }
/*     */ 
/*     */   public InputStream invoke(org.omg.CORBA.Object paramObject, OutputStream paramOutputStream)
/*     */     throws ApplicationException, RemarshalException
/*     */   {
/* 146 */     ClientRequestDispatcher localClientRequestDispatcher = getClientRequestDispatcher();
/* 147 */     return (InputStream)localClientRequestDispatcher.marshalingComplete(paramObject, (OutputObject)paramOutputStream);
/*     */   }
/*     */ 
/*     */   public void releaseReply(org.omg.CORBA.Object paramObject, InputStream paramInputStream)
/*     */   {
/* 154 */     ClientRequestDispatcher localClientRequestDispatcher = getClientRequestDispatcher();
/* 155 */     localClientRequestDispatcher.endRequest(this.orb, paramObject, (InputObject)paramInputStream);
/* 156 */     this.orb.releaseOrDecrementInvocationInfo();
/*     */   }
/*     */ 
/*     */   private ClientRequestDispatcher getClientRequestDispatcher()
/*     */   {
/* 161 */     return ((CorbaInvocationInfo)this.orb.getInvocationInfo()).getClientRequestDispatcher();
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object get_interface_def(org.omg.CORBA.Object paramObject)
/*     */   {
/* 168 */     InputStream localInputStream = null;
/*     */ 
/* 170 */     org.omg.CORBA.Object localObject1 = null;
/*     */     try
/*     */     {
/* 173 */       OutputStream localOutputStream = request(null, "_interface", true);
/* 174 */       localInputStream = invoke((org.omg.CORBA.Object)null, localOutputStream);
/*     */ 
/* 176 */       localObject2 = localInputStream.read_Object();
/*     */ 
/* 180 */       if (!localObject2._is_a("IDL:omg.org/CORBA/InterfaceDef:1.0"))
/* 181 */         throw this.wrapper.wrongInterfaceDef(CompletionStatus.COMPLETED_MAYBE);
/*     */       try
/*     */       {
/* 184 */         localObject1 = (org.omg.CORBA.Object)JDKBridge.loadClass("org.omg.CORBA._InterfaceDefStub").newInstance();
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 188 */         throw this.wrapper.noInterfaceDefStub(localException);
/*     */       }
/*     */ 
/* 191 */       Delegate localDelegate = StubAdapter.getDelegate(localObject2);
/*     */ 
/* 193 */       StubAdapter.setDelegate(localObject1, localDelegate);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 196 */       throw this.wrapper.applicationExceptionInSpecialMethod(localApplicationException);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       org.omg.CORBA.Object localObject2;
/* 198 */       return get_interface_def(paramObject);
/*     */     } finally {
/* 200 */       releaseReply((org.omg.CORBA.Object)null, localInputStream);
/*     */     }
/*     */ 
/* 203 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public boolean is_a(org.omg.CORBA.Object paramObject, String paramString)
/*     */   {
/* 213 */     String[] arrayOfString = StubAdapter.getTypeIds(paramObject);
/* 214 */     String str = this.contactInfoList.getTargetIOR().getTypeId();
/* 215 */     if (paramString.equals(str)) {
/* 216 */       return true;
/*     */     }
/* 218 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 219 */       if (paramString.equals(arrayOfString[i])) {
/* 220 */         return true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 227 */     InputStream localInputStream = null;
/*     */     try {
/* 229 */       OutputStream localOutputStream = request(null, "_is_a", true);
/* 230 */       localOutputStream.write_string(paramString);
/* 231 */       localInputStream = invoke((org.omg.CORBA.Object)null, localOutputStream);
/*     */ 
/* 233 */       return localInputStream.read_boolean();
/*     */     }
/*     */     catch (ApplicationException localApplicationException)
/*     */     {
/* 237 */       throw this.wrapper.applicationExceptionInSpecialMethod(localApplicationException);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       boolean bool;
/* 239 */       return is_a(paramObject, paramString);
/*     */     } finally {
/* 241 */       releaseReply((org.omg.CORBA.Object)null, localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean non_existent(org.omg.CORBA.Object paramObject)
/*     */   {
/* 247 */     InputStream localInputStream = null;
/*     */     try {
/* 249 */       OutputStream localOutputStream = request(null, "_non_existent", true);
/* 250 */       localInputStream = invoke((org.omg.CORBA.Object)null, localOutputStream);
/*     */ 
/* 252 */       return localInputStream.read_boolean();
/*     */     }
/*     */     catch (ApplicationException localApplicationException)
/*     */     {
/* 256 */       throw this.wrapper.applicationExceptionInSpecialMethod(localApplicationException);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       boolean bool;
/* 258 */       return non_existent(paramObject);
/*     */     } finally {
/* 260 */       releaseReply((org.omg.CORBA.Object)null, localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object duplicate(org.omg.CORBA.Object paramObject)
/*     */   {
/* 266 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public void release(org.omg.CORBA.Object paramObject)
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean is_equivalent(org.omg.CORBA.Object paramObject1, org.omg.CORBA.Object paramObject2)
/*     */   {
/* 280 */     if (paramObject2 == null) {
/* 281 */       return false;
/*     */     }
/*     */ 
/* 284 */     if (!StubAdapter.isStub(paramObject2)) {
/* 285 */       return false;
/*     */     }
/* 287 */     Delegate localDelegate = StubAdapter.getDelegate(paramObject2);
/* 288 */     if (localDelegate == null) {
/* 289 */       return false;
/*     */     }
/*     */ 
/* 292 */     if (localDelegate == this) {
/* 293 */       return true;
/*     */     }
/*     */ 
/* 296 */     if (!(localDelegate instanceof CorbaClientDelegateImpl)) {
/* 297 */       return false;
/*     */     }
/* 299 */     CorbaClientDelegateImpl localCorbaClientDelegateImpl = (CorbaClientDelegateImpl)localDelegate;
/* 300 */     CorbaContactInfoList localCorbaContactInfoList = (CorbaContactInfoList)localCorbaClientDelegateImpl.getContactInfoList();
/*     */ 
/* 302 */     return this.contactInfoList.getTargetIOR().isEquivalent(localCorbaContactInfoList.getTargetIOR());
/*     */   }
/*     */ 
/*     */   public boolean equals(org.omg.CORBA.Object paramObject, java.lang.Object paramObject1)
/*     */   {
/* 312 */     if (paramObject1 == null) {
/* 313 */       return false;
/*     */     }
/* 315 */     if (!StubAdapter.isStub(paramObject1)) {
/* 316 */       return false;
/*     */     }
/*     */ 
/* 319 */     Delegate localDelegate = StubAdapter.getDelegate(paramObject1);
/* 320 */     if (localDelegate == null) {
/* 321 */       return false;
/*     */     }
/* 323 */     if ((localDelegate instanceof CorbaClientDelegateImpl)) {
/* 324 */       CorbaClientDelegateImpl localCorbaClientDelegateImpl = (CorbaClientDelegateImpl)localDelegate;
/*     */ 
/* 326 */       IOR localIOR = localCorbaClientDelegateImpl.contactInfoList.getTargetIOR();
/* 327 */       return this.contactInfoList.getTargetIOR().equals(localIOR);
/*     */     }
/*     */ 
/* 331 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode(org.omg.CORBA.Object paramObject)
/*     */   {
/* 336 */     return hashCode();
/*     */   }
/*     */ 
/*     */   public int hash(org.omg.CORBA.Object paramObject, int paramInt)
/*     */   {
/* 341 */     int i = hashCode();
/* 342 */     if (i > paramInt)
/* 343 */       return 0;
/* 344 */     return i;
/*     */   }
/*     */ 
/*     */   public Request request(org.omg.CORBA.Object paramObject, String paramString)
/*     */   {
/* 349 */     return new RequestImpl(this.orb, paramObject, null, paramString, null, null, null, null);
/*     */   }
/*     */ 
/*     */   public Request create_request(org.omg.CORBA.Object paramObject, Context paramContext, String paramString, NVList paramNVList, NamedValue paramNamedValue)
/*     */   {
/* 359 */     return new RequestImpl(this.orb, paramObject, paramContext, paramString, paramNVList, paramNamedValue, null, null);
/*     */   }
/*     */ 
/*     */   public Request create_request(org.omg.CORBA.Object paramObject, Context paramContext, String paramString, NVList paramNVList, NamedValue paramNamedValue, ExceptionList paramExceptionList, ContextList paramContextList)
/*     */   {
/* 371 */     return new RequestImpl(this.orb, paramObject, paramContext, paramString, paramNVList, paramNamedValue, paramExceptionList, paramContextList);
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.ORB orb(org.omg.CORBA.Object paramObject)
/*     */   {
/* 377 */     return this.orb;
/*     */   }
/*     */ 
/*     */   public boolean is_local(org.omg.CORBA.Object paramObject)
/*     */   {
/* 393 */     return this.contactInfoList.getEffectiveTargetIOR().getProfile().isLocal();
/*     */   }
/*     */ 
/*     */   public ServantObject servant_preinvoke(org.omg.CORBA.Object paramObject, String paramString, Class paramClass)
/*     */   {
/* 401 */     return this.contactInfoList.getLocalClientRequestDispatcher().servant_preinvoke(paramObject, paramString, paramClass);
/*     */   }
/*     */ 
/*     */   public void servant_postinvoke(org.omg.CORBA.Object paramObject, ServantObject paramServantObject)
/*     */   {
/* 409 */     this.contactInfoList.getLocalClientRequestDispatcher().servant_postinvoke(paramObject, paramServantObject);
/*     */   }
/*     */ 
/*     */   public String get_codebase(org.omg.CORBA.Object paramObject)
/*     */   {
/* 421 */     if (this.contactInfoList.getTargetIOR() != null) {
/* 422 */       return this.contactInfoList.getTargetIOR().getProfile().getCodebase();
/*     */     }
/* 424 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString(org.omg.CORBA.Object paramObject)
/*     */   {
/* 429 */     return this.contactInfoList.getTargetIOR().stringify();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 439 */     return this.contactInfoList.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.CorbaClientDelegateImpl
 * JD-Core Version:    0.6.2
 */