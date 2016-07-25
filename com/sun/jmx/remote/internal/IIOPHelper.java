/*     */ package com.sun.jmx.remote.internal;
/*     */ 
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public final class IIOPHelper
/*     */ {
/*     */   private static final String IMPL_CLASS = "com.sun.jmx.remote.protocol.iiop.IIOPProxyImpl";
/*  51 */   private static final IIOPProxy proxy = (IIOPProxy)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public IIOPProxy run() {
/*     */       try {
/*  55 */         Class localClass = Class.forName("com.sun.jmx.remote.protocol.iiop.IIOPProxyImpl", true, null);
/*  56 */         return (IIOPProxy)localClass.newInstance();
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/*  58 */         return null;
/*     */       } catch (InstantiationException localInstantiationException) {
/*  60 */         throw new AssertionError(localInstantiationException);
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/*  62 */         throw new AssertionError(localIllegalAccessException);
/*     */       }
/*     */     }
/*     */   });
/*     */ 
/*     */   public static boolean isAvailable()
/*     */   {
/*  70 */     return proxy != null;
/*     */   }
/*     */ 
/*     */   private static void ensureAvailable() {
/*  74 */     if (proxy == null)
/*  75 */       throw new AssertionError("Should not here");
/*     */   }
/*     */ 
/*     */   public static boolean isStub(Object paramObject)
/*     */   {
/*  82 */     return proxy == null ? false : proxy.isStub(paramObject);
/*     */   }
/*     */ 
/*     */   public static Object getDelegate(Object paramObject)
/*     */   {
/*  89 */     ensureAvailable();
/*  90 */     return proxy.getDelegate(paramObject);
/*     */   }
/*     */ 
/*     */   public static void setDelegate(Object paramObject1, Object paramObject2)
/*     */   {
/*  97 */     ensureAvailable();
/*  98 */     proxy.setDelegate(paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public static Object getOrb(Object paramObject)
/*     */   {
/* 109 */     ensureAvailable();
/* 110 */     return proxy.getOrb(paramObject);
/*     */   }
/*     */ 
/*     */   public static void connect(Object paramObject1, Object paramObject2)
/*     */     throws RemoteException
/*     */   {
/* 119 */     ensureAvailable();
/* 120 */     proxy.connect(paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public static boolean isOrb(Object paramObject)
/*     */   {
/* 127 */     ensureAvailable();
/* 128 */     return proxy.isOrb(paramObject);
/*     */   }
/*     */ 
/*     */   public static Object createOrb(String[] paramArrayOfString, Properties paramProperties)
/*     */   {
/* 135 */     ensureAvailable();
/* 136 */     return proxy.createOrb(paramArrayOfString, paramProperties);
/*     */   }
/*     */ 
/*     */   public static Object stringToObject(Object paramObject, String paramString)
/*     */   {
/* 144 */     ensureAvailable();
/* 145 */     return proxy.stringToObject(paramObject, paramString);
/*     */   }
/*     */ 
/*     */   public static String objectToString(Object paramObject1, Object paramObject2)
/*     */   {
/* 152 */     ensureAvailable();
/* 153 */     return proxy.objectToString(paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public static <T> T narrow(Object paramObject, Class<T> paramClass)
/*     */   {
/* 161 */     ensureAvailable();
/* 162 */     return proxy.narrow(paramObject, paramClass);
/*     */   }
/*     */ 
/*     */   public static void exportObject(Remote paramRemote)
/*     */     throws RemoteException
/*     */   {
/* 169 */     ensureAvailable();
/* 170 */     proxy.exportObject(paramRemote);
/*     */   }
/*     */ 
/*     */   public static void unexportObject(Remote paramRemote)
/*     */     throws NoSuchObjectException
/*     */   {
/* 177 */     ensureAvailable();
/* 178 */     proxy.unexportObject(paramRemote);
/*     */   }
/*     */ 
/*     */   public static Remote toStub(Remote paramRemote)
/*     */     throws NoSuchObjectException
/*     */   {
/* 185 */     ensureAvailable();
/* 186 */     return proxy.toStub(paramRemote);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.internal.IIOPHelper
 * JD-Core Version:    0.6.2
 */