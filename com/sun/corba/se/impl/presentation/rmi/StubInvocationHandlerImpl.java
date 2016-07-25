/*     */ package com.sun.corba.se.impl.presentation.rmi;
/*     */ 
/*     */ import com.sun.corba.se.pept.transport.ContactInfoList;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orbutil.proxy.LinkedInvocationHandler;
/*     */ import com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller;
/*     */ import com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.ClassData;
/*     */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*     */ import com.sun.corba.se.spi.protocol.CorbaClientDelegate;
/*     */ import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoList;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import javax.rmi.CORBA.Util;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.RemarshalException;
/*     */ import org.omg.CORBA.portable.ServantObject;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ import org.omg.CORBA_2_3.portable.OutputStream;
/*     */ 
/*     */ public final class StubInvocationHandlerImpl
/*     */   implements LinkedInvocationHandler
/*     */ {
/*     */   private transient PresentationManager.ClassData classData;
/*     */   private transient PresentationManager pm;
/*     */   private transient org.omg.CORBA.Object stub;
/*     */   private transient Proxy self;
/*     */ 
/*     */   public void setProxy(Proxy paramProxy)
/*     */   {
/*  80 */     this.self = paramProxy;
/*     */   }
/*     */ 
/*     */   public Proxy getProxy()
/*     */   {
/*  85 */     return this.self;
/*     */   }
/*     */ 
/*     */   public StubInvocationHandlerImpl(PresentationManager paramPresentationManager, PresentationManager.ClassData paramClassData, org.omg.CORBA.Object paramObject)
/*     */   {
/*  91 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  92 */     if (localSecurityManager != null) {
/*  93 */       localSecurityManager.checkPermission(new DynamicAccessPermission("access"));
/*     */     }
/*  95 */     this.classData = paramClassData;
/*  96 */     this.pm = paramPresentationManager;
/*  97 */     this.stub = paramObject;
/*     */   }
/*     */ 
/*     */   private boolean isLocal()
/*     */   {
/* 102 */     boolean bool = false;
/* 103 */     Delegate localDelegate = StubAdapter.getDelegate(this.stub);
/*     */ 
/* 105 */     if ((localDelegate instanceof CorbaClientDelegate)) {
/* 106 */       CorbaClientDelegate localCorbaClientDelegate = (CorbaClientDelegate)localDelegate;
/* 107 */       ContactInfoList localContactInfoList = localCorbaClientDelegate.getContactInfoList();
/* 108 */       if ((localContactInfoList instanceof CorbaContactInfoList)) {
/* 109 */         CorbaContactInfoList localCorbaContactInfoList = (CorbaContactInfoList)localContactInfoList;
/* 110 */         LocalClientRequestDispatcher localLocalClientRequestDispatcher = localCorbaContactInfoList.getLocalClientRequestDispatcher();
/*     */ 
/* 112 */         bool = localLocalClientRequestDispatcher.useLocalInvocation(null);
/*     */       }
/*     */     }
/*     */ 
/* 116 */     return bool;
/*     */   }
/*     */ 
/*     */   public java.lang.Object invoke(java.lang.Object paramObject, final Method paramMethod, java.lang.Object[] paramArrayOfObject)
/*     */     throws Throwable
/*     */   {
/* 126 */     String str = this.classData.getIDLNameTranslator().getIDLName(paramMethod);
/*     */ 
/* 128 */     DynamicMethodMarshaller localDynamicMethodMarshaller = this.pm.getDynamicMethodMarshaller(paramMethod);
/*     */ 
/* 131 */     Delegate localDelegate = null;
/*     */     try {
/* 133 */       localDelegate = StubAdapter.getDelegate(this.stub);
/*     */     } catch (SystemException localSystemException1) {
/* 135 */       throw Util.mapSystemException(localSystemException1);
/*     */     }
/*     */     java.lang.Object localObject1;
/* 138 */     if (!isLocal()) {
/*     */       try {
/* 140 */         InputStream localInputStream = null;
/*     */         try
/*     */         {
/* 143 */           OutputStream localOutputStream = (OutputStream)localDelegate.request(this.stub, str, true);
/*     */ 
/* 148 */           localDynamicMethodMarshaller.writeArguments(localOutputStream, paramArrayOfObject);
/*     */ 
/* 151 */           localInputStream = (InputStream)localDelegate.invoke(this.stub, localOutputStream);
/*     */ 
/* 155 */           return localDynamicMethodMarshaller.readResult(localInputStream);
/*     */         } catch (ApplicationException localApplicationException) {
/* 157 */           throw localDynamicMethodMarshaller.readException(localApplicationException);
/*     */         } catch (RemarshalException localRemarshalException) {
/* 159 */           return invoke(paramObject, paramMethod, paramArrayOfObject);
/*     */         } finally {
/* 161 */           localDelegate.releaseReply(this.stub, localInputStream);
/*     */         }
/*     */       } catch (SystemException localSystemException2) {
/* 164 */         throw Util.mapSystemException(localSystemException2);
/*     */       }
/*     */     }
/*     */ 
/* 168 */     ORB localORB = (ORB)localDelegate.orb(this.stub);
/* 169 */     ServantObject localServantObject = localDelegate.servant_preinvoke(this.stub, str, paramMethod.getDeclaringClass());
/*     */ 
/* 171 */     if (localServantObject == null)
/* 172 */       return invoke(this.stub, paramMethod, paramArrayOfObject);
/*     */     try
/*     */     {
/* 175 */       localObject1 = localDynamicMethodMarshaller.copyArguments(paramArrayOfObject, localORB);
/*     */ 
/* 177 */       if (!paramMethod.isAccessible())
/*     */       {
/* 181 */         AccessController.doPrivileged(new PrivilegedAction() {
/*     */           public java.lang.Object run() {
/* 183 */             paramMethod.setAccessible(true);
/* 184 */             return null;
/*     */           }
/*     */         });
/*     */       }
/*     */ 
/* 189 */       localObject3 = paramMethod.invoke(localServantObject.servant, (java.lang.Object[])localObject1);
/*     */ 
/* 191 */       return localDynamicMethodMarshaller.copyResult(localObject3, localORB);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 193 */       java.lang.Object localObject3 = localInvocationTargetException.getCause();
/*     */ 
/* 195 */       java.lang.Object localObject4 = (Throwable)Util.copyObject(localObject3, localORB);
/* 196 */       if (localDynamicMethodMarshaller.isDeclaredException((Throwable)localObject4)) {
/* 197 */         throw ((Throwable)localObject4);
/*     */       }
/* 199 */       throw Util.wrapException((Throwable)localObject4);
/*     */     } catch (Throwable localThrowable) {
/* 201 */       if ((localThrowable instanceof ThreadDeath)) {
/* 202 */         throw ((ThreadDeath)localThrowable);
/*     */       }
/*     */ 
/* 207 */       throw Util.wrapException(localThrowable);
/*     */     } finally {
/* 209 */       localDelegate.servant_postinvoke(this.stub, localServantObject);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.StubInvocationHandlerImpl
 * JD-Core Version:    0.6.2
 */