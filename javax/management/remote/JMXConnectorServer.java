/*     */ package javax.management.remote;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.management.MBeanNotificationInfo;
/*     */ import javax.management.MBeanRegistration;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.NotificationBroadcasterSupport;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public abstract class JMXConnectorServer extends NotificationBroadcasterSupport
/*     */   implements JMXConnectorServerMBean, MBeanRegistration, JMXAddressable
/*     */ {
/*     */   public static final String AUTHENTICATOR = "jmx.remote.authenticator";
/* 402 */   private MBeanServer mbeanServer = null;
/*     */   private ObjectName myName;
/* 410 */   private final List<String> connectionIds = new ArrayList();
/*     */ 
/* 412 */   private static final int[] sequenceNumberLock = new int[0];
/*     */   private static long sequenceNumber;
/*     */ 
/*     */   public JMXConnectorServer()
/*     */   {
/*  86 */     this(null);
/*     */   }
/*     */ 
/*     */   public JMXConnectorServer(MBeanServer paramMBeanServer)
/*     */   {
/* 100 */     this.mbeanServer = paramMBeanServer;
/*     */   }
/*     */ 
/*     */   public synchronized MBeanServer getMBeanServer()
/*     */   {
/* 111 */     return this.mbeanServer;
/*     */   }
/*     */ 
/*     */   public synchronized void setMBeanServerForwarder(MBeanServerForwarder paramMBeanServerForwarder)
/*     */   {
/* 116 */     if (paramMBeanServerForwarder == null) {
/* 117 */       throw new IllegalArgumentException("Invalid null argument: mbsf");
/*     */     }
/* 119 */     if (this.mbeanServer != null) paramMBeanServerForwarder.setMBeanServer(this.mbeanServer);
/* 120 */     this.mbeanServer = paramMBeanServerForwarder;
/*     */   }
/*     */ 
/*     */   public String[] getConnectionIds() {
/* 124 */     synchronized (this.connectionIds) {
/* 125 */       return (String[])this.connectionIds.toArray(new String[this.connectionIds.size()]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public JMXConnector toJMXConnector(Map<String, ?> paramMap)
/*     */     throws IOException
/*     */   {
/* 174 */     if (!isActive()) throw new IllegalStateException("Connector is not active");
/*     */ 
/* 176 */     JMXServiceURL localJMXServiceURL = getAddress();
/* 177 */     return JMXConnectorFactory.newJMXConnector(localJMXServiceURL, paramMap);
/*     */   }
/*     */ 
/*     */   public MBeanNotificationInfo[] getNotificationInfo()
/*     */   {
/* 193 */     String[] arrayOfString = { "jmx.remote.connection.opened", "jmx.remote.connection.closed", "jmx.remote.connection.failed" };
/*     */ 
/* 198 */     String str = JMXConnectionNotification.class.getName();
/*     */ 
/* 201 */     return new MBeanNotificationInfo[] { new MBeanNotificationInfo(arrayOfString, str, "A client connection has been opened or closed") };
/*     */   }
/*     */ 
/*     */   protected void connectionOpened(String paramString1, String paramString2, Object paramObject)
/*     */   {
/* 232 */     if (paramString1 == null) {
/* 233 */       throw new NullPointerException("Illegal null argument");
/*     */     }
/* 235 */     synchronized (this.connectionIds) {
/* 236 */       this.connectionIds.add(paramString1);
/*     */     }
/*     */ 
/* 239 */     sendNotification("jmx.remote.connection.opened", paramString1, paramString2, paramObject);
/*     */   }
/*     */ 
/*     */   protected void connectionClosed(String paramString1, String paramString2, Object paramObject)
/*     */   {
/* 267 */     if (paramString1 == null) {
/* 268 */       throw new NullPointerException("Illegal null argument");
/*     */     }
/* 270 */     synchronized (this.connectionIds) {
/* 271 */       this.connectionIds.remove(paramString1);
/*     */     }
/*     */ 
/* 274 */     sendNotification("jmx.remote.connection.closed", paramString1, paramString2, paramObject);
/*     */   }
/*     */ 
/*     */   protected void connectionFailed(String paramString1, String paramString2, Object paramObject)
/*     */   {
/* 302 */     if (paramString1 == null) {
/* 303 */       throw new NullPointerException("Illegal null argument");
/*     */     }
/* 305 */     synchronized (this.connectionIds) {
/* 306 */       this.connectionIds.remove(paramString1);
/*     */     }
/*     */ 
/* 309 */     sendNotification("jmx.remote.connection.failed", paramString1, paramString2, paramObject);
/*     */   }
/*     */ 
/*     */   private void sendNotification(String paramString1, String paramString2, String paramString3, Object paramObject)
/*     */   {
/* 315 */     JMXConnectionNotification localJMXConnectionNotification = new JMXConnectionNotification(paramString1, getNotificationSource(), paramString2, nextSequenceNumber(), paramString3, paramObject);
/*     */ 
/* 322 */     sendNotification(localJMXConnectionNotification);
/*     */   }
/*     */ 
/*     */   private synchronized Object getNotificationSource() {
/* 326 */     if (this.myName != null) {
/* 327 */       return this.myName;
/*     */     }
/* 329 */     return this;
/*     */   }
/*     */ 
/*     */   private static long nextSequenceNumber() {
/* 333 */     synchronized (sequenceNumberLock) {
/* 334 */       return sequenceNumber++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*     */   {
/* 362 */     if ((paramMBeanServer == null) || (paramObjectName == null))
/* 363 */       throw new NullPointerException("Null MBeanServer or ObjectName");
/* 364 */     if (this.mbeanServer == null) {
/* 365 */       this.mbeanServer = paramMBeanServer;
/* 366 */       this.myName = paramObjectName;
/*     */     }
/* 368 */     return paramObjectName;
/*     */   }
/*     */ 
/*     */   public void postRegister(Boolean paramBoolean)
/*     */   {
/*     */   }
/*     */ 
/*     */   public synchronized void preDeregister()
/*     */     throws Exception
/*     */   {
/* 389 */     if ((this.myName != null) && (isActive())) {
/* 390 */       stop();
/* 391 */       this.myName = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void postDeregister() {
/* 396 */     this.myName = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.JMXConnectorServer
 * JD-Core Version:    0.6.2
 */