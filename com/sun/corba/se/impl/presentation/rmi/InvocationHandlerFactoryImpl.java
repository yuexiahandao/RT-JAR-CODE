/*     */ package com.sun.corba.se.impl.presentation.rmi;
/*     */ 
/*     */ import com.sun.corba.se.spi.orbutil.proxy.CompositeInvocationHandler;
/*     */ import com.sun.corba.se.spi.orbutil.proxy.CompositeInvocationHandlerImpl;
/*     */ import com.sun.corba.se.spi.orbutil.proxy.DelegateInvocationHandlerImpl;
/*     */ import com.sun.corba.se.spi.orbutil.proxy.InvocationHandlerFactory;
/*     */ import com.sun.corba.se.spi.orbutil.proxy.LinkedInvocationHandler;
/*     */ import com.sun.corba.se.spi.presentation.rmi.DynamicStub;
/*     */ import com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.ClassData;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ public class InvocationHandlerFactoryImpl
/*     */   implements InvocationHandlerFactory
/*     */ {
/*     */   private final PresentationManager.ClassData classData;
/*     */   private final PresentationManager pm;
/*     */   private Class[] proxyInterfaces;
/*     */ 
/*     */   public InvocationHandlerFactoryImpl(PresentationManager paramPresentationManager, PresentationManager.ClassData paramClassData)
/*     */   {
/*  58 */     this.classData = paramClassData;
/*  59 */     this.pm = paramPresentationManager;
/*     */ 
/*  61 */     Class[] arrayOfClass = paramClassData.getIDLNameTranslator().getInterfaces();
/*     */ 
/*  63 */     this.proxyInterfaces = new Class[arrayOfClass.length + 1];
/*  64 */     for (int i = 0; i < arrayOfClass.length; i++) {
/*  65 */       this.proxyInterfaces[i] = arrayOfClass[i];
/*     */     }
/*  67 */     this.proxyInterfaces[arrayOfClass.length] = DynamicStub.class;
/*     */   }
/*     */ 
/*     */   public InvocationHandler getInvocationHandler()
/*     */   {
/* 106 */     DynamicStubImpl localDynamicStubImpl = new DynamicStubImpl(this.classData.getTypeIds());
/*     */ 
/* 109 */     return getInvocationHandler(localDynamicStubImpl);
/*     */   }
/*     */ 
/*     */   InvocationHandler getInvocationHandler(DynamicStub paramDynamicStub)
/*     */   {
/* 119 */     final InvocationHandler localInvocationHandler = DelegateInvocationHandlerImpl.create(paramDynamicStub);
/*     */ 
/* 124 */     StubInvocationHandlerImpl localStubInvocationHandlerImpl = new StubInvocationHandlerImpl(this.pm, this.classData, paramDynamicStub);
/*     */ 
/* 129 */     final CustomCompositeInvocationHandlerImpl localCustomCompositeInvocationHandlerImpl = new CustomCompositeInvocationHandlerImpl(paramDynamicStub);
/*     */ 
/* 132 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run() {
/* 135 */         localCustomCompositeInvocationHandlerImpl.addInvocationHandler(DynamicStub.class, localInvocationHandler);
/*     */ 
/* 137 */         localCustomCompositeInvocationHandlerImpl.addInvocationHandler(org.omg.CORBA.Object.class, localInvocationHandler);
/*     */ 
/* 139 */         localCustomCompositeInvocationHandlerImpl.addInvocationHandler(java.lang.Object.class, localInvocationHandler);
/*     */ 
/* 141 */         return null;
/*     */       }
/*     */     });
/* 159 */     localCustomCompositeInvocationHandlerImpl.setDefaultHandler(localStubInvocationHandlerImpl);
/*     */ 
/* 161 */     return localCustomCompositeInvocationHandlerImpl;
/*     */   }
/*     */ 
/*     */   public Class[] getProxyInterfaces()
/*     */   {
/* 166 */     return this.proxyInterfaces;
/*     */   }
/*     */ 
/*     */   private class CustomCompositeInvocationHandlerImpl extends CompositeInvocationHandlerImpl
/*     */     implements LinkedInvocationHandler, Serializable
/*     */   {
/*     */     private transient DynamicStub stub;
/*     */ 
/*     */     public void setProxy(Proxy paramProxy)
/*     */     {
/*  78 */       ((DynamicStubImpl)this.stub).setSelf((DynamicStub)paramProxy);
/*     */     }
/*     */ 
/*     */     public Proxy getProxy()
/*     */     {
/*  83 */       return (Proxy)((DynamicStubImpl)this.stub).getSelf();
/*     */     }
/*     */ 
/*     */     public CustomCompositeInvocationHandlerImpl(DynamicStub arg2)
/*     */     {
/*     */       java.lang.Object localObject;
/*  88 */       this.stub = localObject;
/*     */     }
/*     */ 
/*     */     public java.lang.Object writeReplace()
/*     */       throws ObjectStreamException
/*     */     {
/* 100 */       return this.stub;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.InvocationHandlerFactoryImpl
 * JD-Core Version:    0.6.2
 */