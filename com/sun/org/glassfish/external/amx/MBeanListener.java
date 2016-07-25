/*     */ package com.sun.org.glassfish.external.amx;
/*     */ 
/*     */ import com.sun.org.glassfish.external.arc.Stability;
/*     */ import com.sun.org.glassfish.external.arc.Taxonomy;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import javax.management.MBeanServerConnection;
/*     */ import javax.management.MBeanServerNotification;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationListener;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ @Taxonomy(stability=Stability.UNCOMMITTED)
/*     */ public class MBeanListener<T extends Callback>
/*     */   implements NotificationListener
/*     */ {
/*     */   private final String mJMXDomain;
/*     */   private final String mType;
/*     */   private final String mName;
/*     */   private final ObjectName mObjectName;
/*     */   private final MBeanServerConnection mMBeanServer;
/*     */   private final T mCallback;
/*     */ 
/*     */   private static void debug(Object o)
/*     */   {
/*  48 */     System.out.println("" + o);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  65 */     return "MBeanListener: ObjectName=" + this.mObjectName + ", type=" + this.mType + ", name=" + this.mName;
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/*  70 */     return this.mType;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  75 */     return this.mName;
/*     */   }
/*     */ 
/*     */   public MBeanServerConnection getMBeanServer()
/*     */   {
/*  80 */     return this.mMBeanServer;
/*     */   }
/*     */ 
/*     */   public T getCallback()
/*     */   {
/* 150 */     return this.mCallback;
/*     */   }
/*     */ 
/*     */   public MBeanListener(MBeanServerConnection server, ObjectName objectName, T callback)
/*     */   {
/* 165 */     this.mMBeanServer = server;
/* 166 */     this.mObjectName = objectName;
/* 167 */     this.mJMXDomain = null;
/* 168 */     this.mType = null;
/* 169 */     this.mName = null;
/* 170 */     this.mCallback = callback;
/*     */   }
/*     */ 
/*     */   public MBeanListener(MBeanServerConnection server, String domain, String type, T callback)
/*     */   {
/* 186 */     this(server, domain, type, null, callback);
/*     */   }
/*     */ 
/*     */   public MBeanListener(MBeanServerConnection server, String domain, String type, String name, T callback)
/*     */   {
/* 205 */     this.mMBeanServer = server;
/* 206 */     this.mJMXDomain = domain;
/* 207 */     this.mType = type;
/* 208 */     this.mName = name;
/* 209 */     this.mObjectName = null;
/* 210 */     this.mCallback = callback;
/*     */   }
/*     */ 
/*     */   private boolean isRegistered(MBeanServerConnection conn, ObjectName objectName)
/*     */   {
/*     */     try
/*     */     {
/* 218 */       return conn.isRegistered(objectName);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 222 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startListening()
/*     */   {
/*     */     try
/*     */     {
/* 236 */       this.mMBeanServer.addNotificationListener(AMXUtil.getMBeanServerDelegateObjectName(), this, null, this);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 240 */       throw new RuntimeException("Can't add NotificationListener", e);
/*     */     }
/*     */ 
/* 243 */     if (this.mObjectName != null)
/*     */     {
/* 245 */       if (isRegistered(this.mMBeanServer, this.mObjectName))
/*     */       {
/* 247 */         this.mCallback.mbeanRegistered(this.mObjectName, this);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 253 */       String props = "type=" + this.mType;
/* 254 */       if (this.mName != null)
/*     */       {
/* 256 */         props = props + "," + "name" + this.mName;
/*     */       }
/*     */ 
/* 259 */       ObjectName pattern = AMXUtil.newObjectName(this.mJMXDomain + ":" + props);
/*     */       try
/*     */       {
/* 262 */         Set matched = this.mMBeanServer.queryNames(pattern, null);
/* 263 */         for (ObjectName objectName : matched)
/*     */         {
/* 265 */           this.mCallback.mbeanRegistered(objectName, this);
/*     */         }
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 270 */         throw new RuntimeException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void stopListening()
/*     */   {
/*     */     try
/*     */     {
/* 281 */       this.mMBeanServer.removeNotificationListener(AMXUtil.getMBeanServerDelegateObjectName(), this);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 285 */       throw new RuntimeException("Can't remove NotificationListener " + this, e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handleNotification(Notification notifIn, Object handback)
/*     */   {
/* 293 */     if ((notifIn instanceof MBeanServerNotification))
/*     */     {
/* 295 */       MBeanServerNotification notif = (MBeanServerNotification)notifIn;
/* 296 */       ObjectName objectName = notif.getMBeanName();
/*     */ 
/* 298 */       boolean match = false;
/* 299 */       if ((this.mObjectName != null) && (this.mObjectName.equals(objectName)))
/*     */       {
/* 301 */         match = true;
/*     */       }
/* 303 */       else if (objectName.getDomain().equals(this.mJMXDomain))
/*     */       {
/* 305 */         if ((this.mType != null) && (this.mType.equals(objectName.getKeyProperty("type"))))
/*     */         {
/* 307 */           String mbeanName = objectName.getKeyProperty("name");
/* 308 */           if ((this.mName != null) && (this.mName.equals(mbeanName)))
/*     */           {
/* 310 */             match = true;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 315 */       if (match)
/*     */       {
/* 317 */         String notifType = notif.getType();
/* 318 */         if ("JMX.mbean.registered".equals(notifType))
/*     */         {
/* 320 */           this.mCallback.mbeanRegistered(objectName, this);
/*     */         }
/* 322 */         else if ("JMX.mbean.unregistered".equals(notifType))
/*     */         {
/* 324 */           this.mCallback.mbeanUnregistered(objectName, this);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface Callback
/*     */   {
/*     */     public abstract void mbeanRegistered(ObjectName paramObjectName, MBeanListener paramMBeanListener);
/*     */ 
/*     */     public abstract void mbeanUnregistered(ObjectName paramObjectName, MBeanListener paramMBeanListener);
/*     */   }
/*     */ 
/*     */   public static class CallbackImpl
/*     */     implements MBeanListener.Callback
/*     */   {
/*  96 */     private volatile ObjectName mRegistered = null;
/*  97 */     private volatile ObjectName mUnregistered = null;
/*     */     private final boolean mStopAtFirst;
/* 112 */     protected final CountDownLatch mLatch = new CountDownLatch(1);
/*     */ 
/*     */     public CallbackImpl()
/*     */     {
/* 101 */       this(true);
/*     */     }
/*     */ 
/*     */     public CallbackImpl(boolean stopAtFirst)
/*     */     {
/* 106 */       this.mStopAtFirst = stopAtFirst;
/*     */     }
/*     */     public ObjectName getRegistered() {
/* 109 */       return this.mRegistered; } 
/* 110 */     public ObjectName getUnregistered() { return this.mUnregistered; }
/*     */ 
/*     */ 
/*     */     public void await()
/*     */     {
/*     */       try
/*     */       {
/* 122 */         this.mLatch.await();
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 126 */         throw new RuntimeException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void mbeanRegistered(ObjectName objectName, MBeanListener listener)
/*     */     {
/* 132 */       this.mRegistered = objectName;
/* 133 */       if (this.mStopAtFirst)
/*     */       {
/* 135 */         listener.stopListening();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void mbeanUnregistered(ObjectName objectName, MBeanListener listener) {
/* 140 */       this.mUnregistered = objectName;
/* 141 */       if (this.mStopAtFirst)
/*     */       {
/* 143 */         listener.stopListening();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.amx.MBeanListener
 * JD-Core Version:    0.6.2
 */