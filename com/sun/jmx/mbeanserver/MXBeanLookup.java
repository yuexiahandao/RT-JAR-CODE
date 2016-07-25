/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.security.AccessController;
/*     */ import java.util.Map;
/*     */ import javax.management.InstanceAlreadyExistsException;
/*     */ import javax.management.JMX;
/*     */ import javax.management.MBeanServerConnection;
/*     */ import javax.management.MBeanServerInvocationHandler;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.openmbean.OpenDataException;
/*     */ 
/*     */ public class MXBeanLookup
/*     */ {
/* 177 */   private static final ThreadLocal<MXBeanLookup> currentLookup = new ThreadLocal();
/*     */   private final MBeanServerConnection mbsc;
/* 181 */   private final WeakIdentityHashMap<Object, ObjectName> mxbeanToObjectName = WeakIdentityHashMap.make();
/*     */ 
/* 183 */   private final Map<ObjectName, WeakReference<Object>> objectNameToProxy = Util.newMap();
/*     */ 
/* 187 */   private static final WeakIdentityHashMap<MBeanServerConnection, WeakReference<MXBeanLookup>> mbscToLookup = WeakIdentityHashMap.make();
/*     */ 
/*     */   private MXBeanLookup(MBeanServerConnection paramMBeanServerConnection)
/*     */   {
/*  89 */     this.mbsc = paramMBeanServerConnection;
/*     */   }
/*     */ 
/*     */   static MXBeanLookup lookupFor(MBeanServerConnection paramMBeanServerConnection) {
/*  93 */     synchronized (mbscToLookup) {
/*  94 */       WeakReference localWeakReference = (WeakReference)mbscToLookup.get(paramMBeanServerConnection);
/*  95 */       MXBeanLookup localMXBeanLookup = localWeakReference == null ? null : (MXBeanLookup)localWeakReference.get();
/*  96 */       if (localMXBeanLookup == null) {
/*  97 */         localMXBeanLookup = new MXBeanLookup(paramMBeanServerConnection);
/*  98 */         mbscToLookup.put(paramMBeanServerConnection, new WeakReference(localMXBeanLookup));
/*     */       }
/* 100 */       return localMXBeanLookup;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized <T> T objectNameToMXBean(ObjectName paramObjectName, Class<T> paramClass) {
/* 105 */     WeakReference localWeakReference = (WeakReference)this.objectNameToProxy.get(paramObjectName);
/* 106 */     if (localWeakReference != null) {
/* 107 */       localObject = localWeakReference.get();
/* 108 */       if (paramClass.isInstance(localObject))
/* 109 */         return paramClass.cast(localObject);
/*     */     }
/* 111 */     Object localObject = JMX.newMXBeanProxy(this.mbsc, paramObjectName, paramClass);
/* 112 */     this.objectNameToProxy.put(paramObjectName, new WeakReference(localObject));
/* 113 */     return localObject;
/*     */   }
/*     */ 
/*     */   synchronized ObjectName mxbeanToObjectName(Object paramObject)
/*     */     throws OpenDataException
/*     */   {
/*     */     String str;
/* 119 */     if ((paramObject instanceof Proxy)) {
/* 120 */       localObject = Proxy.getInvocationHandler(paramObject);
/* 121 */       if ((localObject instanceof MBeanServerInvocationHandler)) {
/* 122 */         MBeanServerInvocationHandler localMBeanServerInvocationHandler = (MBeanServerInvocationHandler)localObject;
/*     */ 
/* 124 */         if (localMBeanServerInvocationHandler.getMBeanServerConnection().equals(this.mbsc)) {
/* 125 */           return localMBeanServerInvocationHandler.getObjectName();
/*     */         }
/* 127 */         str = "proxy for a different MBeanServer";
/*     */       } else {
/* 129 */         str = "not a JMX proxy";
/*     */       }
/*     */     } else { localObject = (ObjectName)this.mxbeanToObjectName.get(paramObject);
/* 132 */       if (localObject != null)
/* 133 */         return localObject;
/* 134 */       str = "not an MXBean registered in this MBeanServer";
/*     */     }
/* 136 */     Object localObject = "object of type " + paramObject.getClass().getName();
/*     */ 
/* 138 */     throw new OpenDataException("Could not convert " + (String)localObject + " to an ObjectName: " + str);
/*     */   }
/*     */ 
/*     */   synchronized void addReference(ObjectName paramObjectName, Object paramObject)
/*     */     throws InstanceAlreadyExistsException
/*     */   {
/* 146 */     ObjectName localObjectName = (ObjectName)this.mxbeanToObjectName.get(paramObject);
/* 147 */     if (localObjectName != null) {
/* 148 */       String str = (String)AccessController.doPrivileged(new GetPropertyAction("jmx.mxbean.multiname"));
/*     */ 
/* 150 */       if (!"true".equalsIgnoreCase(str)) {
/* 151 */         throw new InstanceAlreadyExistsException("MXBean already registered with name " + localObjectName);
/*     */       }
/*     */     }
/*     */ 
/* 155 */     this.mxbeanToObjectName.put(paramObject, paramObjectName);
/*     */   }
/*     */ 
/*     */   synchronized boolean removeReference(ObjectName paramObjectName, Object paramObject) {
/* 159 */     if (paramObjectName.equals(this.mxbeanToObjectName.get(paramObject))) {
/* 160 */       this.mxbeanToObjectName.remove(paramObject);
/* 161 */       return true;
/*     */     }
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */   static MXBeanLookup getLookup()
/*     */   {
/* 170 */     return (MXBeanLookup)currentLookup.get();
/*     */   }
/*     */ 
/*     */   static void setLookup(MXBeanLookup paramMXBeanLookup) {
/* 174 */     currentLookup.set(paramMXBeanLookup);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.MXBeanLookup
 * JD-Core Version:    0.6.2
 */