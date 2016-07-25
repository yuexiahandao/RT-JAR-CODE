/*     */ package com.sun.org.glassfish.gmbal;
/*     */ 
/*     */ import com.sun.org.glassfish.gmbal.util.GenericConstructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public final class ManagedObjectManagerFactory
/*     */ {
/*  46 */   private static GenericConstructor<ManagedObjectManager> objectNameCons = new GenericConstructor(ManagedObjectManager.class, "com.sun.org.glassfish.gmbal.impl.ManagedObjectManagerImpl", new Class[] { ObjectName.class });
/*     */ 
/*  53 */   private static GenericConstructor<ManagedObjectManager> stringCons = new GenericConstructor(ManagedObjectManager.class, "com.sun.org.glassfish.gmbal.impl.ManagedObjectManagerImpl", new Class[] { String.class });
/*     */ 
/*     */   public static Method getMethod(Class<?> cls, final String name, final Class<?>[] types)
/*     */   {
/*     */     try
/*     */     {
/*  71 */       return (Method)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Method run() throws Exception {
/*  74 */           return this.val$cls.getDeclaredMethod(name, types);
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException ex) {
/*  78 */       throw new GmbalException("Unexpected exception", ex);
/*     */     } catch (SecurityException exc) {
/*  80 */       throw new GmbalException("Unexpected exception", exc);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static ManagedObjectManager createStandalone(String domain)
/*     */   {
/*  94 */     ManagedObjectManager result = (ManagedObjectManager)stringCons.create(new Object[] { domain });
/*  95 */     if (result == null) {
/*  96 */       return ManagedObjectManagerNOPImpl.self;
/*     */     }
/*  98 */     return result;
/*     */   }
/*     */ 
/*     */   public static ManagedObjectManager createFederated(ObjectName rootParentName)
/*     */   {
/* 116 */     ManagedObjectManager result = (ManagedObjectManager)objectNameCons.create(new Object[] { rootParentName });
/* 117 */     if (result == null) {
/* 118 */       return ManagedObjectManagerNOPImpl.self;
/*     */     }
/* 120 */     return result;
/*     */   }
/*     */ 
/*     */   public static ManagedObjectManager createNOOP()
/*     */   {
/* 130 */     return ManagedObjectManagerNOPImpl.self;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.gmbal.ManagedObjectManagerFactory
 * JD-Core Version:    0.6.2
 */