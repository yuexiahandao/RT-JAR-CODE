/*     */ package com.sun.org.glassfish.external.amx;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MBeanServerConnection;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public final class AMXGlassfish
/*     */ {
/*     */   public static final String DEFAULT_JMX_DOMAIN = "amx";
/*  44 */   public static final AMXGlassfish DEFAULT = new AMXGlassfish("amx");
/*     */   private final String mJMXDomain;
/*     */   private final ObjectName mDomainRoot;
/*     */ 
/*     */   public AMXGlassfish(String jmxDomain)
/*     */   {
/*  52 */     this.mJMXDomain = jmxDomain;
/*  53 */     this.mDomainRoot = newObjectName("", "domain-root", null);
/*     */   }
/*     */ 
/*     */   public static String getGlassfishVersion()
/*     */   {
/*  60 */     String version = System.getProperty("glassfish.version");
/*  61 */     return version;
/*     */   }
/*     */ 
/*     */   public String amxJMXDomain()
/*     */   {
/*  72 */     return this.mJMXDomain;
/*     */   }
/*     */ 
/*     */   public String amxSupportDomain()
/*     */   {
/*  78 */     return amxJMXDomain() + "-support";
/*     */   }
/*     */ 
/*     */   public String dasName()
/*     */   {
/*  84 */     return "server";
/*     */   }
/*     */ 
/*     */   public String dasConfig()
/*     */   {
/*  90 */     return dasName() + "-config";
/*     */   }
/*     */ 
/*     */   public ObjectName domainRoot()
/*     */   {
/*  96 */     return this.mDomainRoot;
/*     */   }
/*     */ 
/*     */   public ObjectName monitoringRoot()
/*     */   {
/* 102 */     return newObjectName("/", "mon", null);
/*     */   }
/*     */ 
/*     */   public ObjectName serverMon(String serverName)
/*     */   {
/* 108 */     return newObjectName("/mon", "server-mon", serverName);
/*     */   }
/*     */ 
/*     */   public ObjectName serverMonForDAS()
/*     */   {
/* 113 */     return serverMon("server");
/*     */   }
/*     */ 
/*     */   public ObjectName newObjectName(String pp, String type, String name)
/*     */   {
/* 129 */     String props = prop("pp", pp) + "," + prop("type", type);
/* 130 */     if (name != null) {
/* 131 */       props = props + "," + prop("name", name);
/*     */     }
/*     */ 
/* 134 */     return newObjectName(props);
/*     */   }
/*     */ 
/*     */   public ObjectName newObjectName(String s)
/*     */   {
/* 140 */     String name = s;
/* 141 */     if (!name.startsWith(amxJMXDomain())) {
/* 142 */       name = amxJMXDomain() + ":" + name;
/*     */     }
/*     */ 
/* 145 */     return AMXUtil.newObjectName(name);
/*     */   }
/*     */ 
/*     */   private static String prop(String key, String value)
/*     */   {
/* 150 */     return key + "=" + value;
/*     */   }
/*     */ 
/*     */   public ObjectName getBootAMXMBeanObjectName()
/*     */   {
/* 158 */     return AMXUtil.newObjectName(amxSupportDomain() + ":type=boot-amx");
/*     */   }
/*     */ 
/*     */   public void invokeBootAMX(MBeanServerConnection conn)
/*     */   {
/*     */     try
/*     */     {
/* 171 */       conn.invoke(getBootAMXMBeanObjectName(), "bootAMX", null, null);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 175 */       e.printStackTrace();
/* 176 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void invokeWaitAMXReady(MBeanServerConnection conn, ObjectName objectName)
/*     */   {
/*     */     try
/*     */     {
/* 187 */       conn.invoke(objectName, "waitAMXReady", null, null);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 191 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T extends MBeanListener.Callback> MBeanListener<T> listenForDomainRoot(MBeanServerConnection server, T callback)
/*     */   {
/* 203 */     MBeanListener listener = new MBeanListener(server, domainRoot(), callback);
/* 204 */     listener.startListening();
/* 205 */     return listener;
/*     */   }
/*     */ 
/*     */   public ObjectName waitAMXReady(MBeanServerConnection server)
/*     */   {
/* 231 */     WaitForDomainRootListenerCallback callback = new WaitForDomainRootListenerCallback(server);
/* 232 */     listenForDomainRoot(server, callback);
/* 233 */     callback.await();
/* 234 */     return callback.getRegistered();
/*     */   }
/*     */ 
/*     */   public <T extends MBeanListener.Callback> MBeanListener<T> listenForBootAMX(MBeanServerConnection server, T callback)
/*     */   {
/* 245 */     MBeanListener listener = new MBeanListener(server, getBootAMXMBeanObjectName(), callback);
/* 246 */     listener.startListening();
/* 247 */     return listener;
/*     */   }
/*     */ 
/*     */   public ObjectName bootAMX(MBeanServerConnection conn)
/*     */     throws IOException
/*     */   {
/* 283 */     ObjectName domainRoot = domainRoot();
/*     */ 
/* 285 */     if (!conn.isRegistered(domainRoot))
/*     */     {
/* 288 */       BootAMXCallback callback = new BootAMXCallback(conn);
/* 289 */       listenForBootAMX(conn, callback);
/* 290 */       callback.await();
/*     */ 
/* 292 */       invokeBootAMX(conn);
/*     */ 
/* 294 */       WaitForDomainRootListenerCallback drCallback = new WaitForDomainRootListenerCallback(conn);
/* 295 */       listenForDomainRoot(conn, drCallback);
/* 296 */       drCallback.await();
/*     */ 
/* 298 */       invokeWaitAMXReady(conn, domainRoot);
/*     */     }
/*     */     else
/*     */     {
/* 302 */       invokeWaitAMXReady(conn, domainRoot);
/*     */     }
/* 304 */     return domainRoot;
/*     */   }
/*     */ 
/*     */   public ObjectName bootAMX(MBeanServer server)
/*     */   {
/*     */     try
/*     */     {
/* 311 */       return bootAMX(server);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 315 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class BootAMXCallback extends MBeanListener.CallbackImpl
/*     */   {
/*     */     private final MBeanServerConnection mConn;
/*     */ 
/*     */     public BootAMXCallback(MBeanServerConnection conn)
/*     */     {
/* 262 */       this.mConn = conn;
/*     */     }
/*     */ 
/*     */     public void mbeanRegistered(ObjectName objectName, MBeanListener listener)
/*     */     {
/* 268 */       super.mbeanRegistered(objectName, listener);
/* 269 */       this.mLatch.countDown();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class WaitForDomainRootListenerCallback extends MBeanListener.CallbackImpl
/*     */   {
/*     */     private final MBeanServerConnection mConn;
/*     */ 
/*     */     public WaitForDomainRootListenerCallback(MBeanServerConnection conn)
/*     */     {
/* 212 */       this.mConn = conn;
/*     */     }
/*     */ 
/*     */     public void mbeanRegistered(ObjectName objectName, MBeanListener listener)
/*     */     {
/* 217 */       super.mbeanRegistered(objectName, listener);
/* 218 */       AMXGlassfish.invokeWaitAMXReady(this.mConn, objectName);
/* 219 */       this.mLatch.countDown();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.amx.AMXGlassfish
 * JD-Core Version:    0.6.2
 */